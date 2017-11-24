package com.xlibao.datacache.item;

import com.xlibao.common.CommonUtils;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.datacache.DataCacheApplicationContextLoaderNotify;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.item.ItemType;
import com.xlibao.metadata.item.ItemUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author chinahuangxc on 2017/4/13.
 */
public class ItemDataCacheService {

    private static final Logger logger = LoggerFactory.getLogger(ItemDataCacheService.class);

    // 商品模版专用读写锁
    private static final ReentrantReadWriteLock ITEM_TEMPLATE_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    // 商品模版专用读锁
    private static final ReentrantReadWriteLock.ReadLock ITEM_TEMPLATE_READ_LOCK = ITEM_TEMPLATE_READ_WRITE_LOCK.readLock();
    // 商品模版专用写锁
    private static final ReentrantReadWriteLock.WriteLock ITEM_TEMPLATE_WRITE_LOCK = ITEM_TEMPLATE_READ_WRITE_LOCK.writeLock();
    // 所有商品模版映射关系 key -- 商品模版ID value -- 商品模版实体对象
    private static final Map<Long, ItemTemplate> itemTemplateMap = new ConcurrentHashMap<>();
    // 所有商品模版映射关系 key -- 商品名 value -- 商品模版ID
    private static final Map<String, Long> itemNameCache = new ConcurrentHashMap<>();
    // 所有商品模版映射关系 key -- 商品条码 value -- 商品模版ID
    private static final Map<String, Long> itemBarcodeCache = new ConcurrentHashMap<>();
    // 商品类型相关的商品集合
    private static final Map<Long, List<Long>> itemTypeCache = new ConcurrentHashMap<>();

    // 商品类型专用读写锁
    private static final ReentrantReadWriteLock ITEM_TYPE_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    // 商品类型专用读锁
    private static final ReentrantReadWriteLock.ReadLock ITEM_TYPE_READ_LOCK = ITEM_TYPE_READ_WRITE_LOCK.readLock();
    // 商品类型专用写锁
    private static final ReentrantReadWriteLock.WriteLock ITEM_TYPE_WRITE_LOCK = ITEM_TYPE_READ_WRITE_LOCK.writeLock();
    // 所有商品类型映射关系 key -- 商品类型ID value -- 商品类型实体对象
    private static final Map<Long, ItemType> itemTypeMap = new ConcurrentHashMap<>();
    // 商品类型的子级列表
    private static final Map<Long, List<Long>> childrenItemTypes = new ConcurrentHashMap<>();

    // 商品单位专用读写锁
    private static final ReentrantReadWriteLock ITEM_UNIT_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    // 商品单位专用读锁
    private static final ReentrantReadWriteLock.ReadLock ITEM_UNIT_READ_LOCK = ITEM_UNIT_READ_WRITE_LOCK.readLock();
    // 商品单位专用写锁
    private static final ReentrantReadWriteLock.WriteLock ITEM_UNIT_WRITE_LOCK = ITEM_UNIT_READ_WRITE_LOCK.writeLock();
    // 所有商品单位映射关系 key -- 商品单位ID value -- 商品单位实体对象
    private static final Map<Long, ItemUnit> itemUnitMap = new ConcurrentHashMap<>();

    private static ItemTypeComparable itemTypeComparable = new ItemTypeComparable();

    public static void initItemCache() {
        Callable<Boolean> loaderItemTemplateCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    logger.info("系统正在加载所有商品模版到缓存中......");
                    loaderItemTemplates();
                    logger.info("本次加载商品模版数量：" + itemTemplateMap.size());
                } catch (Throwable cause) {
                    cause.printStackTrace();
                    return false;
                }
                AsyncScheduledService.submitDelayCacheTask(this, DataCacheApplicationContextLoaderNotify.DELAY, DataCacheApplicationContextLoaderNotify.TIME_UNIT);
                return true;
            }
        };
        boolean result = AsyncScheduledService.waitFutureCacheTask(loaderItemTemplateCallable);
        if (!result) {
            // 一般不能让服务器正常启动
            System.exit(0);
        }

        Callable<Boolean> loaderItemTypeCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    logger.info("系统正在加载所有商品类型到缓存中......");
                    loaderItemTypes();
                    logger.info("本次加载商品类型数量：" + itemTypeMap.size());
                } catch (Throwable cause) {
                    cause.printStackTrace();
                    return false;
                }
                AsyncScheduledService.submitDelayCacheTask(this, DataCacheApplicationContextLoaderNotify.DELAY, DataCacheApplicationContextLoaderNotify.TIME_UNIT);
                return true;
            }
        };
        result = AsyncScheduledService.waitFutureCacheTask(loaderItemTypeCallable);
        if (!result) {
            // 一般不能让服务器正常启动
            System.exit(0);
        }

        Callable<Boolean> loaderItemUnitCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    logger.info("系统正在加载所有商品单位到缓存中......");
                    loaderItemUnits();
                    logger.info("本次加载商品单位数量：" + itemUnitMap.size());
                } catch (Throwable cause) {
                    cause.printStackTrace();
                    return false;
                }
                AsyncScheduledService.submitDelayCacheTask(this, DataCacheApplicationContextLoaderNotify.DELAY, DataCacheApplicationContextLoaderNotify.TIME_UNIT);
                return true;
            }
        };
        result = AsyncScheduledService.waitFutureCacheTask(loaderItemUnitCallable);
        if (!result) {
            // 一般不能让服务器正常启动
            System.exit(0);
        }
    }

    /**
     * <pre>
     *     加载商品模版数据到缓存中，为了不影响加载时其他线程的读取等待过程，执行的步骤为：
     *     1、从数据库获取最新的所有商品模版记录存放在临时列表中
     *     2、解释列表中的所有记录，将各个商品模版按照主键--实体的方式存放到临时映射关系表中
     *     3、同时存放商品名--模版ID的关系、商品类型--相关商品的列表集合
     *     4、获取写锁，将相应的映射关系存放至线程安全的全局映射表中(缓存)
     * </pre>
     */
    private static void loaderItemTemplates() {
        List<ItemTemplate> itemTemplates = ItemRemoteService.loaderItemTemplates();

        Map<Long, ItemTemplate> tmpItemTemplateMap = new ConcurrentHashMap<>();
        Map<String, Long> itemNameCacheMap = new ConcurrentHashMap<>();
        Map<String, Long> itemBarcodeCacheMap = new ConcurrentHashMap<>();
        Map<Long, List<Long>> itemTypeCacheMap = new ConcurrentHashMap<>();

        for (ItemTemplate itemTemplate : itemTemplates) {
            tmpItemTemplateMap.put(itemTemplate.getId(), itemTemplate);

            itemNameCacheMap.put(CommonUtils.nullToEmpty(itemTemplate.getName()), itemTemplate.getId());
            itemBarcodeCacheMap.put(CommonUtils.nullToEmpty(itemTemplate.getBarcode()), itemTemplate.getId());

            List<Long> itemTemplateList = itemTypeCacheMap.get(itemTemplate.getTypeId());
            if (itemTemplateList == null) {
                itemTemplateList = new ArrayList<>();
                itemTypeCacheMap.put(itemTemplate.getTypeId(), itemTemplateList);
            }
            itemTemplateList.add(itemTemplate.getId());
        }

        try {
            if (ITEM_TEMPLATE_WRITE_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                try {
                    itemTemplateMap.clear();
                    itemTemplateMap.putAll(tmpItemTemplateMap);

                    itemNameCache.clear();
                    itemNameCache.putAll(itemNameCacheMap);

                    itemBarcodeCache.clear();
                    itemBarcodeCache.putAll(itemBarcodeCacheMap);

                    itemTypeCache.clear();
                    itemTypeCache.putAll(itemTypeCacheMap);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    ITEM_TEMPLATE_WRITE_LOCK.unlock();
                }
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    private static void loaderItemTypes() {
        List<ItemType> itemTypes = ItemRemoteService.loaderItemTypes();

        Map<Long, ItemType> tmpItemTypeMap = new ConcurrentHashMap<>();
        Map<Long, List<Long>> childrenItemTypeMap = new ConcurrentHashMap<>();

        for (ItemType itemType : itemTypes) {
            tmpItemTypeMap.put(itemType.getId(), itemType);
            if (itemType.getParentId() != null && itemType.getParentId() > 0) {
                List<Long> childrenItemType = childrenItemTypeMap.get(itemType.getParentId());
                if (childrenItemType == null) {
                    childrenItemType = new ArrayList<>();
                    childrenItemTypeMap.put(itemType.getParentId(), childrenItemType);
                }
                childrenItemType.add(itemType.getId());
            }
        }
        try {
            if (ITEM_TYPE_WRITE_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                try {
                    itemTypeMap.clear();
                    itemTypeMap.putAll(tmpItemTypeMap);

                    childrenItemTypes.clear();
                    childrenItemTypes.putAll(childrenItemTypeMap);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    ITEM_TYPE_WRITE_LOCK.unlock();
                }
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    private static void loaderItemUnits() {
        List<ItemUnit> itemUnits = ItemRemoteService.loaderItemUnits();
        Map<Long, ItemUnit> tmpItemUnitMap = new ConcurrentHashMap<>();

        for (ItemUnit itemUnit : itemUnits) {
            tmpItemUnitMap.put(itemUnit.getId(), itemUnit);
        }
        try {
            if (ITEM_UNIT_WRITE_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                try {
                    itemUnitMap.clear();
                    itemUnitMap.putAll(tmpItemUnitMap);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    ITEM_UNIT_WRITE_LOCK.unlock();
                }
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    /**
     * <pre>
     *     获取执行主键的模版实体数据
     * </pre>
     *
     * @param itemTemplateId 模版ID
     */
    public static ItemTemplate getItemTemplate(long itemTemplateId) {
        try {
            if (!ITEM_TEMPLATE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return ItemRemoteService.getItemTemplate(itemTemplateId);
            }
            try {
                ItemTemplate itemTemplate = itemTemplateMap.get(itemTemplateId);
                if (itemTemplate == null) {
                    return ItemRemoteService.getItemTemplate(itemTemplateId);
                }
                return itemTemplate;
            } finally {
                ITEM_TEMPLATE_READ_LOCK.unlock();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ItemRemoteService.getItemTemplate(itemTemplateId);
    }

    /**
     * <pre>
     *     通过商品名获取商品实体记录 -- 精准匹配
     * </pre>
     *
     * @param itemName 商品名
     */
    public static ItemTemplate getItemTemplate(String itemName) {
        try {
            if (!ITEM_TEMPLATE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return ItemRemoteService.getItemTemplateByName(itemName);
            }
            try {
                Long itemTemplateId = itemNameCache.get(itemName);
                if (itemTemplateId == null) {
                    return ItemRemoteService.getItemTemplateByName(itemName);
                }
                return itemTemplateMap.get(itemTemplateId);
            } finally {
                ITEM_TEMPLATE_READ_LOCK.unlock();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ItemRemoteService.getItemTemplateByName(itemName);
    }

    public static ItemTemplate getItemTemplateForBarcode(String barcode) {
        try {
            if (!ITEM_TEMPLATE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return ItemRemoteService.getItemTemplateForBarcode(barcode);
            }
            try {
                Long itemTemplateId = itemBarcodeCache.get(barcode);
                if (itemTemplateId == null) {
                    return ItemRemoteService.getItemTemplateForBarcode(barcode);
                }
                return itemTemplateMap.get(itemTemplateId);
            } finally {
                ITEM_TEMPLATE_READ_LOCK.unlock();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ItemRemoteService.getItemTemplateForBarcode(barcode);
    }

    /**
     * <pre>
     *      获取所有商品模版信息 -- 只读
     * </pre>
     *
     * @deprecated 如非必要 请勿使用
     */
    public static List<ItemTemplate> getOnlyReadItemTemplates() {
        try {
            if (ITEM_TEMPLATE_WRITE_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return Collections.emptyList();
            }
            try {
                return Collections.unmodifiableList(new ArrayList<>(itemTemplateMap.values()));
            } finally {
                ITEM_TEMPLATE_WRITE_LOCK.unlock();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * <pre>
     *     通过商品名获取商品实体记录 -- 模糊查询(全品类)
     * </pre>
     *
     * @param fuzzyItemName 模糊匹配的商品名
     */
    public static List<ItemTemplate> fuzzyQueryItemTemplates(String fuzzyItemName) {
        try {
            if (!ITEM_TEMPLATE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return Collections.emptyList();
            }
            try {
                return itemNameCache.keySet().stream().filter(key -> key.contains(fuzzyItemName)).map(key -> itemTemplateMap.get(itemNameCache.get(key))).collect(Collectors.toList());
            } finally {
                ITEM_TEMPLATE_READ_LOCK.unlock();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static List<ItemTemplate> relationItemTemplate(long itemTypeId) {
        try {
            if (!ITEM_TEMPLATE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return ItemRemoteService.relationItemTemplates(itemTypeId);
            }
            try {
                List<Long> itemTemplateIds = itemTypeCache.get(itemTypeId);
                if (itemTemplateIds == null || itemTemplateIds.isEmpty()) {
                    return null;
                }
                return itemTemplateIds.stream().map(itemTemplateMap::get).collect(Collectors.toList());
            } finally {
                ITEM_TEMPLATE_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return ItemRemoteService.relationItemTemplates(itemTypeId);
    }

    public static ItemType getItemType(long itemTypeId) {
        try {
            if (!ITEM_TYPE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return ItemRemoteService.getItemType(itemTypeId);
            }
            try {
                ItemType itemType = itemTypeMap.get(itemTypeId);
                if (itemType == null) {
                    return ItemRemoteService.getItemType(itemTypeId);
                }
                return itemType;
            } finally {
                ITEM_TYPE_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return ItemRemoteService.getItemType(itemTypeId);
    }

    public static List<ItemType> getItemTypes() {
        try {
            if (!ITEM_TYPE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return Collections.emptyList();
            }
            try {
                List<Long> itemTypeSet = new ArrayList<>(childrenItemTypes.keySet());
                return fillItemTypes(itemTypeSet);
            } finally {
                ITEM_TYPE_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static List<ItemType> fillItemTypes(List<Long> itemTypeSet) {
        if (CommonUtils.isEmpty(itemTypeSet)) {
            return Collections.emptyList();
        }
        List<ItemType> itemTypes = new ArrayList<>();
        for (Long l : itemTypeSet) {
            ItemType itemType = itemTypeMap.get(l);
            if (itemType == null) {
                continue;
            }
            itemTypes.add(itemType);

            List<Long> subTypes = childrenItemTypes.get(l);
            itemType.setSubTypes(fillItemTypes(subTypes));
        }
        Collections.sort(itemTypes, itemTypeComparable);
        return itemTypes;
    }

    public static List<ItemType> getRecommendItemTypes() {
        try {
            if (!ITEM_TYPE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return Collections.emptyList();
            }
            try {
                List<Long> itemTypeSet = new ArrayList<>(childrenItemTypes.keySet());
                return fillRecommendItemTypes(itemTypeSet);
            } finally {
                ITEM_TYPE_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static List<ItemType> fillRecommendItemTypes(List<Long> itemTypeSet) {
        if (CommonUtils.isEmpty(itemTypeSet)) {
            return Collections.emptyList();
        }
        List<ItemType> itemTypes = new ArrayList<>();
        for (Long l : itemTypeSet) {
            ItemType itemType = itemTypeMap.get(l);
            if (itemType == null) {
                continue;
            }
            if (itemType.getTop() == 0) {
                continue;
            }
            itemTypes.add(itemType);
            List<Long> subTypes = childrenItemTypes.get(l);
            itemType.setSubTypes(fillRecommendItemTypes(subTypes));
        }
        Collections.sort(itemTypes, itemTypeComparable);
        return itemTypes;
    }

    public static List<ItemType> relationItemTypes(long parentItemTypeId) {
        try {
            if (!ITEM_TYPE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return ItemRemoteService.relationItemTypes(parentItemTypeId);
            }
            try {
                List<Long> childrenItemTypeSet = childrenItemTypes.get(parentItemTypeId);
                if (childrenItemTypeSet == null || childrenItemTypeSet.isEmpty()) {
                    return Collections.emptyList();
                }
                return childrenItemTypeSet.stream().map(itemTypeMap::get).collect(Collectors.toList());
            } finally {
                ITEM_TYPE_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return ItemRemoteService.relationItemTypes(parentItemTypeId);
    }

    public static ItemUnit getItemUnit(long itemUnitId) {
        try {
            if (!ITEM_UNIT_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return ItemRemoteService.getItemUnit(itemUnitId);
            }
            try {
                ItemUnit itemUnit = itemUnitMap.get(itemUnitId);
                if (itemUnit == null) {
                    return ItemRemoteService.getItemUnit(itemUnitId);
                }
                return itemUnit;
            } finally {
                ITEM_UNIT_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return ItemRemoteService.getItemUnit(itemUnitId);
    }

    public static List<ItemTemplate> appointItemType(long itemTypeId) {
        ItemType itemType = getItemType(itemTypeId);
        if (itemType.getParentId() == 0) {
            List<ItemType> itemTypes = relationItemTypes(itemTypeId);
            if (itemTypes == null || itemTypes.isEmpty()) {
                return null;
            }
            List<ItemTemplate> itemTemplates = new ArrayList<>();
            for (ItemType it : itemTypes) {
                List<ItemTemplate> tmpItemTemplates = appointItemType(it.getId());
                if (CommonUtils.isEmpty(tmpItemTemplates)) {
                    continue;
                }
                itemTemplates.addAll(tmpItemTemplates);
            }
            return itemTemplates;
        }
        return relationItemTemplate(itemTypeId);
    }

    public static String assembleItemTemplateSet(List<ItemTemplate> itemTemplates) {
        if (CommonUtils.isEmpty(itemTemplates)) {
            return "";
        }
        StringBuilder itemTemplateSet = new StringBuilder();
        for (ItemTemplate itemTemplate : itemTemplates) {
            itemTemplateSet.append(itemTemplate.getId()).append(CommonUtils.SPLIT_COMMA);
        }
        // 仓库集合
        itemTemplateSet = itemTemplateSet.deleteCharAt(itemTemplateSet.length() - 1);
        return itemTemplateSet.toString();
    }

    private static class ItemTypeComparable implements Comparator<ItemType> {

        @Override
        public int compare(ItemType o1, ItemType o2) {
            if (o1.getSort() < o2.getSort()) {
                return -1;
            }
            return o1.getSort().intValue() == o2.getSort().intValue() ? 0 : 1;
        }
    }
}