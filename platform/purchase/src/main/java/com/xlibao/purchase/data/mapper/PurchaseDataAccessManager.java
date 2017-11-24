package com.xlibao.purchase.data.mapper;
import com.xlibao.purchase.data.model.*;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


/**
 * @author chinahuangxc on 2017/01/26
 */
@Component
public class PurchaseDataAccessManager {

    @Autowired
    private PurchaseCommodityMapper commodityMapper;

    @Autowired
    private PurchaseCommodityStoresMapper commodityStoresMapper;

    @Autowired
    private  PurchaseEntryMapper entryMapper;

    @Autowired
    private  PurchaseSupplierMapper supplierMapper;

    @Autowired
    private  PurchaseWarehouseMapper warehouseMapper;

    @Autowired
    private  PurchaseWarehouseUserMapper warehouseUserMapper;

    @Autowired
    private PurchaseRepertoryRecordMapper purchaseRepertoryRecordMapper;


    public List<PurchaseSupplier> searchSupplierPage(String supplierName,int supplierType, int status,int pageSize, int pageStartIndex){
        return supplierMapper.searchSupplierPage(supplierName,supplierType,status,pageSize,pageStartIndex);
    }

    public int searchSupplierPageCount(String supplierName,int supplierType, int status){
        return supplierMapper.searchSupplierPageCount(supplierName,supplierType,status);
    }

    public List<PurchaseSupplier> getAllSupplier(){
        return supplierMapper.getAllSupplier();
    }

    public int saveSupplier(PurchaseSupplier supplier){
        return supplierMapper.insertSelective(supplier);
    }

    public int updateSupplier(PurchaseSupplier supplier){
        return supplierMapper.updateByPrimaryKeySelective(supplier);
    }

    public  PurchaseSupplier getSupplier(long id){
        return supplierMapper.selectByPrimaryKey(id);
    }
    /**仓库**/
    public List<ResultMap> searchWarehousePage(String warehouseName, int status, int pageSize, int pageStartIndex){
        return warehouseMapper.searchWarehousePage(warehouseName,status,pageSize,pageStartIndex);
    }

    public int searchWarehousePageCount(String warehouseName, int status){
        return warehouseMapper.searchWarehousePageCount(warehouseName,status);
    }

    public List<PurchaseWarehouse> getAllWarehouse(){
        return warehouseMapper.getAllWarehouse();
    }
    public int saveWarehouse(PurchaseWarehouse warehouse){
        return warehouseMapper.insertSelective(warehouse);
    }

    public int updateWarehouse(PurchaseWarehouse warehouse){
        return warehouseMapper.updateByPrimaryKeySelective(warehouse);
    }

    public  PurchaseWarehouse getWarehouse(long id){
        return warehouseMapper.selectByPrimaryKey(id);
    }

    public PurchaseWarehouse getByWarehouseCode(String warehouseCode){return warehouseMapper.getByWarehouseCode(warehouseCode);}

    /****仓库管理员***/
    public int saveWarehouseUser(PurchaseWarehouseUser warehouseUser){
        return warehouseUserMapper.insertSelective(warehouseUser);
    }

    public List<PurchaseWarehouseUser> getAllWarehouseUser(long warehouseId){
        return warehouseUserMapper.getAllWarehouseUser(warehouseId);
    }

    public int updateWarehouseUser(PurchaseWarehouseUser warehouseUser){
        return warehouseUserMapper.updateByPrimaryKeySelective(warehouseUser);
    }
    /**采购单**/
    public List<ResultMap> searchPurchasePage(String supplierName,String warehouseCode, String [] statusList, int pageSize, int pageStartIndex){
        return entryMapper.searchPurchasePage(supplierName,warehouseCode,statusList,pageSize,pageStartIndex);
    }

    public int searchPurchasePageCount(String supplierName,String warehouseCode,  String [] statusList){
        return entryMapper.searchPurchasePageCount(supplierName,warehouseCode,statusList);
    }
    public HashMap getPurchase(Long id){
        return entryMapper.getPurchase(id);
    }

    public List<PurchaseCommodity> getPurchaseCommodityS(Long purchaseID){
       return  commodityMapper.getPurchaseCommodityS(purchaseID);
    }
    public List getSupplierCommodityS(Long supplierID){
        return  commodityMapper.getSupplierCommodityS(supplierID);
    }

    public int updatePurchase(PurchaseEntry purchaseEntry){
        return entryMapper.updateByPrimaryKeySelective(purchaseEntry);
    }
    public int delPurchaseCommodity(PurchaseCommodity purchaseCommodity){
        return commodityMapper.updatePurchaseCommodity(purchaseCommodity);
    }
    public int updatePurchaseCommodity(PurchaseCommodity purchaseCommodity){
        return commodityMapper.updateByPrimaryKeySelective(purchaseCommodity);
    }
    public PurchaseCommodity getPurchaseCommodity(Long id){
        return  commodityMapper.selectByPrimaryKey(id);
    }

    public int savePurchase(PurchaseEntry purchaseEntry){
        return entryMapper.insertSelective(purchaseEntry);
    }

    public int savePurchaseCommodity(PurchaseCommodity purchaseCommodity){
        return commodityMapper.insertSelective(purchaseCommodity);
    }

    public int delPurchaseCommodity(Long purchaseID){
       return commodityMapper.deleteByPrimaryKey(purchaseID);
    }

    /**商品库存*/
    public  List<PurchaseCommodityStores> searchCommodityStoresPage(String warehouseCode, String itemName,String barcode,int pageSize,int pageStartIndex){
        return commodityStoresMapper.searchCommodityStoresPage(warehouseCode,itemName,barcode,pageSize,pageStartIndex);
    }

    public int searchCommodityStoresPageCount(String warehouseCode, String itemName,String barcode){
        return commodityStoresMapper.searchCommodityStoresPageCount(warehouseCode,itemName,barcode);
    }


    public int updateCommodityStores(PurchaseCommodityStores purchaseCommodityStores){
        return commodityStoresMapper.updateByPrimaryKeySelective(purchaseCommodityStores);
    }

    public PurchaseCommodityStores getByParameterID(String warehouseCode, Long itemId){
        return commodityStoresMapper.getByParameterID(warehouseCode,itemId);
    }

    public int savePurchaseCommodityStores(PurchaseCommodityStores purchaseCommodityStores){
        return commodityStoresMapper.insertSelective(purchaseCommodityStores);
    }
    public int updatePurchaseCommodityStores(PurchaseCommodityStores purchaseCommodityStores){
        return commodityStoresMapper.updateByPrimaryKeySelective(purchaseCommodityStores);
    }

    public  int savePurchaseRepertoryRecord(PurchaseRepertoryRecord purchaseRepertoryRecord){
        return purchaseRepertoryRecordMapper.insertSelective(purchaseRepertoryRecord);
    }

}