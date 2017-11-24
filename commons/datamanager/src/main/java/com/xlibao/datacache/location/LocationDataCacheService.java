package com.xlibao.datacache.location;

import com.xlibao.common.CommonUtils;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.datacache.DataCacheApplicationContextLoaderNotify;
import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;
import com.xlibao.metadata.passport.PassportStreet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author chinahuangxc on 2017/7/21.
 */
public class LocationDataCacheService {

    private static final Logger logger = LoggerFactory.getLogger(LocationDataCacheService.class);

    // 省份信息专用读写锁
    private static final ReentrantReadWriteLock PROVINCE_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    // 省份信息专用读锁
    private static final ReentrantReadWriteLock.ReadLock PROVINCE_READ_LOCK = PROVINCE_READ_WRITE_LOCK.readLock();
    // 省份信息专用写锁
    private static final ReentrantReadWriteLock.WriteLock PROVINCE_WRITE_LOCK = PROVINCE_READ_WRITE_LOCK.writeLock();
    // 所有省份信息映射关系 key -- 省份ID value -- 省份实体对象
    private static final Map<Long, PassportProvince> provinceMap = new ConcurrentHashMap<>();

    // 城市信息专用读写锁
    private static final ReentrantReadWriteLock CITY_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    // 城市信息专用读锁
    private static final ReentrantReadWriteLock.ReadLock CITY_READ_LOCK = CITY_READ_WRITE_LOCK.readLock();
    // 城市信息专用写锁
    private static final ReentrantReadWriteLock.WriteLock CITY_WRITE_LOCK = CITY_READ_WRITE_LOCK.writeLock();
    // 所有城市信息映射关系 key -- 城市ID value -- 城市实体对象
    private static final Map<Long, PassportCity> cityMap = new ConcurrentHashMap<>();
    private static final Map<Long, List<Long>> provinceCityCache = new ConcurrentHashMap<>();

    // 区域信息专用读写锁
    private static final ReentrantReadWriteLock DISTRICT_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    // 区域信息专用读锁
    private static final ReentrantReadWriteLock.ReadLock DISTRICT_READ_LOCK = DISTRICT_READ_WRITE_LOCK.readLock();
    // 区域信息专用写锁
    private static final ReentrantReadWriteLock.WriteLock DISTRICT_WRITE_LOCK = DISTRICT_READ_WRITE_LOCK.writeLock();
    // 所有区域信息映射关系 key -- 区域ID value -- 区域实体对象
    private static final Map<Long, PassportArea> districtMap = new ConcurrentHashMap<>();
    private static final Map<Long, List<Long>> cityDistrictCache = new ConcurrentHashMap<>();

    // 街道信息专用读写锁
    private static final ReentrantReadWriteLock STREET_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    // 街道信息专用读锁
    private static final ReentrantReadWriteLock.ReadLock STREET_READ_LOCK = STREET_READ_WRITE_LOCK.readLock();
    // 街道信息专用写锁
    private static final ReentrantReadWriteLock.WriteLock STREET_WRITE_LOCK = STREET_READ_WRITE_LOCK.writeLock();
    // 所有街道信息映射关系 key -- 街道ID value -- 街道实体对象
    private static final Map<Long, PassportStreet> streetMap = new ConcurrentHashMap<>();
    private static final Map<Long, List<Long>> districtStreetCache = new ConcurrentHashMap<>();

    public static void initLocationCache() {
        Callable<Boolean> loaderProvinceCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    logger.info("系统正在加载省份位置数据到缓存中......");
                    loaderProvinces();
                    logger.info("本次加载省份位置数据的数量为：" + provinceMap.size());
                } catch (Throwable cause) {
                    cause.printStackTrace();
                    return false;
                }
                AsyncScheduledService.submitDelayCacheTask(this, DataCacheApplicationContextLoaderNotify.DELAY, DataCacheApplicationContextLoaderNotify.TIME_UNIT);
                return true;
            }
        };
        boolean result = AsyncScheduledService.waitFutureCacheTask(loaderProvinceCallable);
        if (!result) {
            // 一般不能让服务器正常启动
            System.exit(0);
        }

        Callable<Boolean> loaderCityCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    logger.info("系统正在加载城市位置数据到缓存中......");
                    loaderCitys();
                    logger.info("本次加载城市位置数据的数量为：" + cityMap.size());
                } catch (Throwable cause) {
                    cause.printStackTrace();
                    return false;
                }
                AsyncScheduledService.submitDelayCacheTask(this, DataCacheApplicationContextLoaderNotify.DELAY, DataCacheApplicationContextLoaderNotify.TIME_UNIT);
                return true;
            }
        };
        result = AsyncScheduledService.waitFutureCacheTask(loaderCityCallable);
        if (!result) {
            // 一般不能让服务器正常启动
            System.exit(0);
        }

        Callable<Boolean> loaderDistrictCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    logger.info("系统正在加载区域位置数据到缓存中......");
                    loaderAreas();
                    logger.info("本次加载区域位置数据的数量为：" + districtMap.size());
                } catch (Throwable cause) {
                    cause.printStackTrace();
                    return false;
                }
                AsyncScheduledService.submitDelayCacheTask(this, DataCacheApplicationContextLoaderNotify.DELAY, DataCacheApplicationContextLoaderNotify.TIME_UNIT);
                return true;
            }
        };
        result = AsyncScheduledService.waitFutureCacheTask(loaderDistrictCallable);
        if (!result) {
            // 一般不能让服务器正常启动
            System.exit(0);
        }

        Callable<Boolean> loaderStreetCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    logger.info("系统正在加载街道位置数据到缓存中......");
                    loaderAreas();
                    logger.info("本次加载街道位置数据的数量为：" + streetMap.size());
                } catch (Throwable cause) {
                    cause.printStackTrace();
                    return false;
                }
                AsyncScheduledService.submitDelayCacheTask(this, DataCacheApplicationContextLoaderNotify.DELAY, DataCacheApplicationContextLoaderNotify.TIME_UNIT);
                return true;
            }
        };
        result = AsyncScheduledService.waitFutureCacheTask(loaderStreetCallable);
        if (!result) {
            // 一般不能让服务器正常启动
            System.exit(0);
        }
    }

    private static void loaderProvinces() {
        List<PassportProvince> provinces = LocationRemoteService.loaderProvinces();

        Map<Long, PassportProvince> tmpProvinceMap = new ConcurrentHashMap<>();
        for (PassportProvince province : provinces) {
            tmpProvinceMap.put(province.getId(), province);
        }
        try {
            if (PROVINCE_WRITE_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                try {
                    provinceMap.clear();
                    provinceMap.putAll(tmpProvinceMap);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    PROVINCE_WRITE_LOCK.unlock();
                }
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    private static void loaderCitys() {
        List<PassportCity> citys = LocationRemoteService.loaderCitys();

        Map<Long, PassportCity> tmpCityMap = new ConcurrentHashMap<>();
        Map<Long, List<Long>> tmpProvinceCityCache = new ConcurrentHashMap<>();
        for (PassportCity city : citys) {
            tmpCityMap.put(city.getId(), city);

            List<Long> cityIds = tmpProvinceCityCache.get(city.getProvinceId());
            if (cityIds == null) {
                cityIds = new ArrayList<>();
                tmpProvinceCityCache.put(city.getProvinceId(), cityIds);
            }
            cityIds.add(city.getId());
        }
        try {
            if (CITY_WRITE_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                try {
                    cityMap.clear();
                    cityMap.putAll(tmpCityMap);

                    provinceCityCache.clear();
                    provinceCityCache.putAll(tmpProvinceCityCache);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    CITY_WRITE_LOCK.unlock();
                }
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    private static void loaderAreas() {
        List<PassportArea> districts = LocationRemoteService.loaderDistricts();

        Map<Long, PassportArea> tmpDistrictMap = new ConcurrentHashMap<>();
        Map<Long, List<Long>> tmpCityDistrictCache = new ConcurrentHashMap<>();
        for (PassportArea area : districts) {
            tmpDistrictMap.put(area.getId(), area);

            List<Long> areas = tmpCityDistrictCache.get(area.getCityId());
            if (areas == null) {
                areas = new ArrayList<>();
                tmpCityDistrictCache.put(area.getCityId(), areas);
            }
            areas.add(area.getId());
        }
        try {
            if (DISTRICT_WRITE_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                try {
                    districtMap.clear();
                    districtMap.putAll(tmpDistrictMap);

                    cityDistrictCache.clear();
                    cityDistrictCache.putAll(tmpCityDistrictCache);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    DISTRICT_WRITE_LOCK.unlock();
                }
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    private static void loaderStreets() {
        List<PassportStreet> streets = LocationRemoteService.loaderStreets();

        Map<Long, PassportStreet> tmpStreetMap = new ConcurrentHashMap<>();
        Map<Long, List<Long>> tmpDistrictStreetCache = new ConcurrentHashMap<>();
        for (PassportStreet street : streets) {
            tmpStreetMap.put(street.getId(), street);

            List<Long> streetIds = tmpDistrictStreetCache.get(street.getAreaId());
            if (streetIds == null) {
                streetIds = new ArrayList<>();
                tmpDistrictStreetCache.put(street.getAreaId(), streetIds);
            }
            streetIds.add(street.getId());
        }
        try {
            if (STREET_WRITE_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                try {
                    streetMap.clear();
                    streetMap.putAll(tmpStreetMap);

                    districtStreetCache.clear();
                    districtStreetCache.putAll(tmpDistrictStreetCache);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    STREET_WRITE_LOCK.unlock();
                }
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    public static List<PassportProvince> getProvinces() {
        try {
            if (!PROVINCE_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return LocationRemoteService.loaderProvinces();
            }
            try {
                return new ArrayList<>(provinceMap.values());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                PROVINCE_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return LocationRemoteService.loaderProvinces();
    }

    public static List<PassportCity> getCitys(long provinceId) {
        try {
            if (!CITY_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return LocationRemoteService.getCitys(provinceId);
            }
            try {
                List<Long> citys = provinceCityCache.get(provinceId);
                if (CommonUtils.isEmpty(citys)) {
                    return LocationRemoteService.getCitys(provinceId);
                }

                List<PassportCity> cities = new ArrayList<>();
                for (Long L : citys) {
                    PassportCity city = cityMap.get(L);
                    if (city == null) {
                        continue;
                    }
                    cities.add(city);
                }
                return cities;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                CITY_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return LocationRemoteService.getCitys(provinceId);
    }

    public static List<PassportArea> getDistricts(long cityId) {
        try {
            if (!DISTRICT_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return LocationRemoteService.getDistricts(cityId);
            }
            try {
                List<Long> areas = cityDistrictCache.get(cityId);
                if (CommonUtils.isEmpty(areas)) {
                    return LocationRemoteService.getDistricts(cityId);
                }

                List<PassportArea> districts = new ArrayList<>();
                for (Long L : areas) {
                    PassportArea district = districtMap.get(L);
                    if (district == null) {
                        continue;
                    }
                    districts.add(district);
                }
                return districts;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                DISTRICT_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return LocationRemoteService.getDistricts(cityId);
    }

    public static List<PassportStreet> getStreets(long districtId) {
        try {
            if (!STREET_READ_LOCK.tryLock(DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_OUT, DataCacheApplicationContextLoaderNotify.WAIT_LOCK_TIME_UNIT)) {
                return LocationRemoteService.getStreets(districtId);
            }
            try {
                List<Long> streetIds = districtStreetCache.get(districtId);
                if (CommonUtils.isEmpty(streetIds)) {
                    return LocationRemoteService.getStreets(districtId);
                }

                List<PassportStreet> streets = new ArrayList<>();
                for (Long L : streetIds) {
                    PassportStreet street = streetMap.get(L);
                    if (street == null) {
                        continue;
                    }
                    streets.add(street);
                }
                return streets;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                STREET_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return LocationRemoteService.getStreets(districtId);
    }
}