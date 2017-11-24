package com.xlibao.purchase.service.purchase.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.purchase.config.ConfigFactory;
import com.xlibao.purchase.data.mapper.PurchaseDataAccessManager;
import com.xlibao.purchase.data.model.*;
import com.xlibao.purchase.service.purchase.PurchaseService;
import com.xlibao.purchase.utils.DateUtil;
import org.apache.ibatis.annotations.ResultMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

import static com.alibaba.fastjson.JSON.parseObject;


/**
 * Created by admin on 2017/8/28.
 */
@Transactional
@Service("purchaseService")
public class PurchaseServiceImpl extends BasicWebService implements PurchaseService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    @Autowired
    private PurchaseDataAccessManager purchaseDataAccessManager;

    @Override
    public JSONObject searchSupplierPage() {

        String supplierName = getUTF("supplierName", null);
        int supplierType = getIntParameter("supplierType", -1);
        int status = getIntParameter("status", -1);
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<PurchaseSupplier> purchaseSuppliers = purchaseDataAccessManager.searchSupplierPage(supplierName,supplierType,status,pageSize,pageStartIndex);
        int count = purchaseDataAccessManager.searchSupplierPageCount(supplierName,supplierType,status);

        JSONObject response = new JSONObject();
        response.put("data", purchaseSuppliers);
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1));
        return success(response);

    }
    @Override
    public JSONObject getAllSupplier(){
        List<PurchaseSupplier> uppliers = purchaseDataAccessManager.getAllSupplier();
        return success(uppliers);
    }
    @Override
    public JSONObject saveSupplier(){
        String supplierName = getUTF("supplierName",null);
        String address = getUTF("address",null);
        int supplierType = getIntParameter("supplierType",-1);
        String deliverPeriod = getUTF("deliverPeriod",null);
        String salesmanName = getUTF("salesmanName",null);
        String phone = getUTF("phone",null);
        if(supplierName == null || supplierName.isEmpty()){
            return fail("缺少供应商名称supplierName");
        }else if(address == null || address.isEmpty()){
            return fail("缺少供应商地址address");
        }else if(deliverPeriod == null || deliverPeriod.isEmpty()){
            return fail("缺少供货周期deliverPeriod");
        }else if(salesmanName == null || salesmanName.isEmpty()){
            return fail("缺少供业务员salesmanName");
        }else if(phone == null || phone.isEmpty()){
            return fail("缺少业务员联系电话phone");
        }else if(supplierType==-1){
            return fail("缺少供应商级别supplierType");
        }
        PurchaseSupplier supplier = new PurchaseSupplier();
        supplier.setSupplierName(supplierName);
        supplier.setAddress(address);
        supplier.setDeliverPeriod(deliverPeriod);
        supplier.setSalesmanName(salesmanName);
        supplier.setPhone(phone);
        supplier.setSupplierType(supplierType);
        if (purchaseDataAccessManager.saveSupplier(supplier) > 0) {
            return success("添加成功");
        }
        return fail("添加失败");
    }
    @Override
    public JSONObject updateSupplier(){
        long id = getLongParameter("id",-1);
        String supplierName = getUTF("supplierName",null);
        String address = getUTF("address",null);
        int supplierType = getIntParameter("supplierType",-1);
        String deliverPeriod = getUTF("deliverPeriod",null);
        String salesmanName = getUTF("salesmanName",null);
        String phone = getUTF("phone",null);
        if(id==-1){
            return fail("缺少供应商id");
        }else if(supplierName == null || supplierName.isEmpty()){
            return fail("缺少供应商名称supplierName");
        }else if(address == null || address.isEmpty()){
            return fail("缺少供应商地址address");
        }else if(deliverPeriod == null || deliverPeriod.isEmpty()){
            return fail("缺少供货周期deliverPeriod");
        }else if(salesmanName == null || salesmanName.isEmpty()){
            return fail("缺少供业务员salesmanName");
        }else if(phone == null || phone.isEmpty()){
            return fail("缺少业务员联系电话phone");
        }else if(supplierType==-1){
            return fail("缺少供应商级别supplierType");
        }
        PurchaseSupplier supplier = new PurchaseSupplier();
        supplier.setId(id);
        supplier.setSupplierName(supplierName);
        supplier.setAddress(address);
        supplier.setDeliverPeriod(deliverPeriod);
        supplier.setSalesmanName(salesmanName);
        supplier.setPhone(phone);
        supplier.setSupplierType(supplierType);
        supplier.setUpdateTime(DateUtil.getNowDate());
        if (purchaseDataAccessManager.updateSupplier(supplier) > 0) {
            return success("修改成功");
        }
        return fail("修改失败");
    }

    public JSONObject updateSupplierStatus(){
        long id = getLongParameter("id",-1);

        int status = getIntParameter("status",-1);
        String  stopRemark = getUTF("stopRemark",null);

        if(id==-1){
            return fail("缺少供应商id");
        }else if(status==-1){
            return fail("缺少状态参数status");
        }

        PurchaseSupplier supplier = new PurchaseSupplier();
        supplier.setId(id);
        supplier.setStatus(status);
        if(stopRemark != null && !stopRemark.isEmpty()){
            supplier.setStopRemark(stopRemark);
        }
        supplier.setUpdateTime(DateUtil.getNowDate());
        if (purchaseDataAccessManager.updateSupplier(supplier) > 0) {
            return success("修改成功");
        }
        return fail("修改失败");
    }

    public JSONObject getSupplier(){
        long id = getLongParameter("id",-1);
        if(id==-1){
            return fail("缺少供应商id");
        }
        PurchaseSupplier supplier =purchaseDataAccessManager.getSupplier(id);
        if (supplier == null) {
            return fail("找不到供应商信息");
        }
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(supplier));
        return success(response);
    }

    @Override
    public JSONObject searchWarehousePage() {
        String warehouseName = getUTF("warehouseName", null);
        int status = getIntParameter("status", -1);
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<ResultMap> purchaseWarehouses = purchaseDataAccessManager.searchWarehousePage(warehouseName,status,pageSize,pageStartIndex);
        int count = purchaseDataAccessManager.searchWarehousePageCount(warehouseName,status);

        JSONObject response = new JSONObject();
        response.put("data", purchaseWarehouses);
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1));
        return success(response);
    }

    @Override
    public JSONObject saveWarehouse() {
        String warehouseCode = getUTF("warehouseCode",null);
        String warehouseName = getUTF("warehouseName",null);
        String address = getUTF("address",null);
        String remark = getUTF("remark",null);
        String wmsKEY = getUTF("wmsKEY",null);

        if(warehouseCode == null || warehouseCode.isEmpty()){
            return fail("缺少仓库编码warehouseCode");
        }else if(warehouseName == null || warehouseName.isEmpty()){
            return fail("缺少仓库名称warehouseName");
        }else if(address == null || address.isEmpty()){
            return fail("缺少仓库地址address");
        }
        PurchaseWarehouse warehouse = new PurchaseWarehouse();
        warehouse.setWarehouseCode(warehouseCode);
        warehouse.setWarehouseName(warehouseName);
        warehouse.setAddress(address);
        warehouse.setRemark(remark);
        warehouse.setWmsKEY(wmsKEY);
        if (purchaseDataAccessManager.saveWarehouse(warehouse) > 0) {
            return success("添加成功");
        }
        return fail("添加失败");
    }

    @Override
    public JSONObject getAllWarehouse() {
        List<PurchaseWarehouse> warehouses = purchaseDataAccessManager.getAllWarehouse();
        return success(warehouses);
    }

    @Override
    public JSONObject updateWarehouse() {
        long id = getLongParameter("id",-1);
        String warehouseCode = getUTF("warehouseCode",null);
        String warehouseName = getUTF("warehouseName",null);
        String address = getUTF("address",null);
        String remark = getUTF("remark",null);
        String wmsKEY = getUTF("wmsKEY",null);
        if(id==-1){
            return fail("缺少仓库id");
        }else if(warehouseCode == null || warehouseCode.isEmpty()){
            return fail("缺少仓库编码");
        }else if(warehouseName == null || warehouseName.isEmpty()){
            return fail("缺少仓库名称");
        }else if(address == null || address.isEmpty()){
            return fail("缺少仓库地址");
        }
        PurchaseWarehouse warehouse = new PurchaseWarehouse();
        warehouse.setId(id);
        warehouse.setWarehouseCode(warehouseCode);
        warehouse.setWarehouseName(warehouseName);
        warehouse.setAddress(address);
        warehouse.setRemark(remark);
        warehouse.setWmsKEY(wmsKEY);
        warehouse.setUpdateTime(DateUtil.getNowDate());
        if (purchaseDataAccessManager.updateWarehouse(warehouse) > 0) {
            return success("修改成功");
        }
        return fail("修改失败");
    }

    @Override
    public JSONObject updateWarehouseStatus() {
        long id = getLongParameter("id",-1);

        int status = getIntParameter("status",-1);
        String  stopRemark = getUTF("stopRemark",null);

        if(id==-1){
            return fail("缺少仓库id");
        }else if(status==-1){
            return fail("缺少状态参数");
        }

        PurchaseWarehouse warehouse = new PurchaseWarehouse();
        warehouse.setId(id);
        warehouse.setStatus(status);
        if(stopRemark != null && !stopRemark.isEmpty()){
            warehouse.setStopRemark(stopRemark);
        }
        warehouse.setUpdateTime(DateUtil.getNowDate());
        if (purchaseDataAccessManager.updateWarehouse(warehouse) > 0) {
            return success("修改成功");
        }
        return fail("修改失败");
    }

    @Override
    public JSONObject getWarehouse() {
        long id = getLongParameter("id",-1);
        if(id==-1){
            return fail("缺少仓库id");
        }
        PurchaseWarehouse warehouse =purchaseDataAccessManager.getWarehouse(id);
        if (warehouse == null) {
            return fail("找不到仓库信息");
        }
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(warehouse));
        return success(response);
    }

    @Override
    public JSONObject saveWarehouseUser() {
        long warehouseID = getLongParameter("warehouseID",-1);
        String username = getUTF("username",null);
        String password = getUTF("password",null);
        String remark = getUTF("remark",null);
        if(warehouseID==-1){
            return fail("缺少仓库warehouseID");
        }else if(username == null || username.isEmpty()){
            return fail("缺少仓库管理员用户名");
        }else if(password == null || password.isEmpty()){
            return fail("缺少管理员密码");
        }
        PurchaseWarehouseUser warehouseUser = new PurchaseWarehouseUser();
        warehouseUser.setWarehouseId(warehouseID);
        warehouseUser.setUsername(username);
        warehouseUser.setPassword(password);
        warehouseUser.setRemark(remark);

        if (purchaseDataAccessManager.saveWarehouseUser(warehouseUser) > 0) {
            return success("添加成功");
        }
        return fail("添加失败");
    }

    @Override
    public JSONObject getWarehouseUsers() {
        long warehouseID = getLongParameter("warehouseID",-1);
        List<PurchaseWarehouseUser> warehouseUsers = purchaseDataAccessManager.getAllWarehouseUser(warehouseID);
        return success(warehouseUsers);
    }

    @Override
    public JSONObject updateWarehouseUserPassword() {
        long id = getLongParameter("id",-1);
        String password = getUTF("password",null);

        if(id==-1){
            return fail("缺少仓库管理员id");
        }if(password == null || password.isEmpty()){
            return fail("缺少管理员密码");
        }

        PurchaseWarehouseUser warehouseUser = new PurchaseWarehouseUser();
        warehouseUser.setId(id);
        warehouseUser.setPassword(password);
        warehouseUser.setUpdateTime(DateUtil.getNowDate());
        if (purchaseDataAccessManager.updateWarehouseUser(warehouseUser) > 0) {
            return success("修改成功");
        }
        return fail("修改失败");
    }

    @Override
    public JSONObject deleteWarehouseUser() {
        long id = getLongParameter("id",-1);

        if(id==-1){
            return fail("缺少仓库管理员id");
        }

        PurchaseWarehouseUser warehouseUser = new PurchaseWarehouseUser();
        warehouseUser.setId(id);
        warehouseUser.setIsDelete(1);
        warehouseUser.setUpdateTime(DateUtil.getNowDate());
        if (purchaseDataAccessManager.updateWarehouseUser(warehouseUser) > 0) {
            return success("删除成功");
        }
        return fail("删除失败");
    }


    @Override
    public JSONObject searchPurchasePage() {
        String supplierName = getUTF("supplierName", null);
        String warehouseCode = getUTF("warehouseCode", null);
        String status = getUTF("status", null);
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);
        String []  statusList=null;
        if(status != null && !"-1".equals(status)){
            statusList= status.split(CommonUtils.SPLIT_COMMA);
        }


        List<ResultMap> purchaseWarehouses = purchaseDataAccessManager.searchPurchasePage(supplierName,warehouseCode,statusList,pageSize,pageStartIndex);
        int count = purchaseDataAccessManager.searchPurchasePageCount(supplierName,warehouseCode,statusList);

        JSONObject response = new JSONObject();
        response.put("data", purchaseWarehouses);
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1));
        return success(response);
    }

    @Override
    public JSONObject getPurchase() {
        long id = getLongParameter("id",-1);
        if(id==-1){
            return fail("缺少采购单id");
        }
        HashMap purchase =purchaseDataAccessManager.getPurchase(id);
        if (purchase == null) {
            return fail("找不到采购单信息");
        }
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(purchase));
        return success(response);
    }
    @Override
    public JSONObject getPurchaseCommodityS() {
        long purchaseID = getLongParameter("purchaseID",-1);
        List<PurchaseCommodity> purchaseCommodityS = purchaseDataAccessManager.getPurchaseCommodityS(purchaseID);
        return success(purchaseCommodityS);
    }
    @Override
    public JSONObject getSupplierCommodityS() {
        long supplierID = getLongParameter("supplierID",-1);
        List supplierCommodityS = purchaseDataAccessManager.getSupplierCommodityS(supplierID);
        return success(supplierCommodityS);
    }
    @Override
    public JSONObject deletePurchase() {
        long id = getLongParameter("id",-1);

        if(id==-1){
            return fail("缺少采购单id");
        }

        PurchaseEntry purchaseEntry = new PurchaseEntry();
        purchaseEntry.setId(id);
        purchaseEntry.setIsDelete(1);
        purchaseEntry.setUpdateTime(DateUtil.getNowDate());
        if (purchaseDataAccessManager.updatePurchase(purchaseEntry) > 0) {
            PurchaseCommodity purchaseCommodity = new PurchaseCommodity();
            purchaseCommodity.setPurchaseId(id);
            purchaseCommodity.setIsDelete(1);
            purchaseCommodity.setUpdateTime(DateUtil.getNowDate());
            purchaseDataAccessManager.delPurchaseCommodity(purchaseCommodity);
            return success("删除成功");
        }
        return fail("删除失败");
    }

    @Override
    public JSONObject savePurchase() {
        String warehouseCode = getUTF("warehouseCode",null);
        long supplierID = getLongParameter("supplierID",-1);
        int status = getIntParameter("status", -1);

        String  itemIDs= getUTF("itemIDs","");
        String  itemTypeIDs= getUTF("itemTypeIDs","");
        String  itemNames= getUTF("itemNames","");
        String  itemTypeTitles= getUTF("itemTypeTitles","");
        String  barcodes= getUTF("barcodes","");
        String  purchaseTimes= getUTF("purchaseTimes","");
        String  purchaseNumbers= getUTF("purchaseNumbers","");
        String  costPrices= getUTF("costPrices","");


        if(warehouseCode == null){
            return fail("缺少仓库编码warehouseCode");
        }else if(supplierID == -1){
            return fail("缺少供应商supplierID");
        }else if(status==-1){
            return fail("缺少状态status");
        }
        PurchaseEntry purchaseEntry = new PurchaseEntry();
        purchaseEntry.setWarehouseCode(warehouseCode);
        purchaseEntry.setSupplierId(supplierID);
        purchaseEntry.setStatus(status);

        if (purchaseDataAccessManager.savePurchase(purchaseEntry) > 0) {
            try {
            //添加采购单成功再添加采购物品
                String []  itemIDList= itemIDs.split(CommonUtils.SPLIT_COMMA);
                String [] itemTypeIDList= itemTypeIDs.split(CommonUtils.SPLIT_COMMA);
                String [] itemNameList= itemNames.split(CommonUtils.SPLIT_COMMA);
                String [] itemTypeTitleList= itemTypeTitles.split(CommonUtils.SPLIT_COMMA);
                String [] barcodeList= barcodes.split(CommonUtils.SPLIT_COMMA);
                String [] purchaseTimeList= purchaseTimes.split(CommonUtils.SPLIT_COMMA);
                String [] purchaseNumberList=purchaseNumbers.split(CommonUtils.SPLIT_COMMA);
                String [] costPriceList=costPrices.split(CommonUtils.SPLIT_COMMA);

                for (int i = 0; i < itemIDList.length; i++) {
                    PurchaseCommodity purchaseCommodity = new PurchaseCommodity();
                    purchaseCommodity.setPurchaseId(purchaseEntry.getId());
                    purchaseCommodity.setItemId(Long.parseLong(itemIDList[i]));
                    purchaseCommodity.setItemName(itemNameList[i].trim());
                    purchaseCommodity.setItemTypeId(Long.parseLong(itemTypeIDList[i]));
                    purchaseCommodity.setItemTypeTitle(itemTypeTitleList[i].trim());
                    purchaseCommodity.setBarcode(barcodeList[i]);
                    purchaseCommodity.setPurchaseNumber(Integer.parseInt(purchaseNumberList[i]));
                    purchaseCommodity.setPurchaseTime(purchaseTimeList[i]);
                    purchaseCommodity.setCostPrice(Double.parseDouble(costPriceList[i]));


                    if (purchaseDataAccessManager.savePurchaseCommodity(purchaseCommodity) <= 0) {
                        return fail("添加订单商品失败");
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("添加订单商品失败");
            }
            JSONObject response = new JSONObject();
            response.put("purchaseID",purchaseEntry.getId());
            return success("添加成功",response);
        }
        return fail("订单添加失败");
    }
    @Override
    public JSONObject savePurchaseCommodity() {
        long purchaseID = getLongParameter("purchaseID",-1);
        long itemID = getLongParameter("itemID",-1);
        long itemTypeID = getLongParameter("itemTypeID",-1);
        String itemName = getUTF("itemName",null);
        String itemTypeTitle = getUTF("itemTypeTitle",null);
        String barcode = getUTF("barcode",null);
        String purchaseTime = getUTF("purchaseTime",null);
        int purchaseNumber = getIntParameter("purchaseNumber",-1);

        if(purchaseID == -1){
            return fail("缺少采购单purchaseID");
        }else if(itemID == -1){
            return fail("缺少商品itemID");
        }else if(itemTypeID==-1){
            return fail("缺少商品类型itemTypeID");
        }else if(purchaseNumber==-1){
            return fail("缺少采购数量purchaseNumber");
        }else if(itemName == null || itemName.isEmpty()){
            return fail("缺少商品名称itemName");
        }else if(itemTypeTitle == null || itemTypeTitle.isEmpty()){
            return fail("缺少商品类型itemTypeTitle");
        }else if(purchaseTime == null || purchaseTime.isEmpty()){
            return fail("缺少采购日期purchaseTime");
        }else if(barcode == null || barcode.isEmpty()){
            return fail("缺少商品条形码barcode");
        }
        PurchaseCommodity purchaseCommodity = new PurchaseCommodity();
        purchaseCommodity.setPurchaseId(purchaseID);
        purchaseCommodity.setItemId(itemID);
        purchaseCommodity.setItemName(itemName);
        purchaseCommodity.setItemTypeId(itemTypeID);
        purchaseCommodity.setItemTypeTitle(itemTypeTitle);
        purchaseCommodity.setBarcode(barcode);
        purchaseCommodity.setPurchaseNumber(purchaseNumber);
        purchaseCommodity.setPurchaseTime(purchaseTime);

        if (purchaseDataAccessManager.savePurchaseCommodity(purchaseCommodity) > 0) {
            return success("添加成功");
        }
        return fail("添加失败");
    }

    @Override
    public JSONObject updatePurchase() {
        long id = getLongParameter("id",-1);
        String warehouseCode = getUTF("warehouseCode",null);
        long supplierID = getLongParameter("supplierID",-1);
        int status = getIntParameter("status", -1);
        String coerceRemark = getUTF("coerceRemark",null);

        String  itemIDs= getUTF("itemIDs","");
        String  itemTypeIDs= getUTF("itemTypeIDs","");
        String  itemNames= getUTF("itemNames","");
        String  itemTypeTitles= getUTF("itemTypeTitles","");
        String  barcodes= getUTF("barcodes","");
        String  purchaseTimes= getUTF("purchaseTimes","");
        String  purchaseNumbers= getUTF("purchaseNumbers","");
        String  costPrices= getUTF("costPrices","");

        if(id == -1){
            return fail("缺少采购单id");
        }else if(warehouseCode == null){
            return fail("缺少仓库编码warehouseCode");
        }else if(supplierID == -1){
            return fail("缺少供应商supplierID");
        }else if(status==-1){
            return fail("缺少状态status");
        }
        PurchaseEntry purchaseEntry = new PurchaseEntry();
        purchaseEntry.setId(id);
        purchaseEntry.setWarehouseCode(warehouseCode);
        purchaseEntry.setSupplierId(supplierID);
        purchaseEntry.setStatus(status);
        purchaseEntry.setUpdateTime(DateUtil.getNowDate());
        purchaseEntry.setCoerceRemark(coerceRemark);
        if(status==4) {
            purchaseEntry.setExceptionRemark(null);
        }

        if (purchaseDataAccessManager.updatePurchase(purchaseEntry) > 0) {
            if(status!=4) {
                //删除采购单下的物品
                purchaseDataAccessManager.delPurchaseCommodity(id);
                try {
                    //添加采购单成功再添加采购物品
                    String[] itemIDList = itemIDs.split(CommonUtils.SPLIT_COMMA);
                    String[] itemTypeIDList = itemTypeIDs.split(CommonUtils.SPLIT_COMMA);
                    String[] itemNameList = itemNames.split(CommonUtils.SPLIT_COMMA);
                    String[] itemTypeTitleList = itemTypeTitles.split(CommonUtils.SPLIT_COMMA);
                    String[] barcodeList = barcodes.split(CommonUtils.SPLIT_COMMA);
                    String[] purchaseTimeList = purchaseTimes.split(CommonUtils.SPLIT_COMMA);
                    String[] purchaseNumberList = purchaseNumbers.split(CommonUtils.SPLIT_COMMA);
                    String[] costPriceList = costPrices.split(CommonUtils.SPLIT_COMMA);
                    for (int i = 0; i < itemIDList.length; i++) {
                        PurchaseCommodity purchaseCommodity = new PurchaseCommodity();
                        purchaseCommodity.setPurchaseId(purchaseEntry.getId());
                        purchaseCommodity.setItemId(Long.parseLong(itemIDList[i]));
                        purchaseCommodity.setItemName(itemNameList[i].trim());
                        purchaseCommodity.setItemTypeId(Long.parseLong(itemTypeIDList[i]));
                        purchaseCommodity.setItemTypeTitle(itemTypeTitleList[i].trim());
                        purchaseCommodity.setBarcode(barcodeList[i]);
                        purchaseCommodity.setPurchaseNumber(Integer.parseInt(purchaseNumberList[i]));
                        purchaseCommodity.setPurchaseTime(purchaseTimeList[i]);
                        purchaseCommodity.setCostPrice(Double.parseDouble(costPriceList[i]));

                        if (purchaseDataAccessManager.savePurchaseCommodity(purchaseCommodity) <= 0) {
                            return fail("修改订单商品失败");
                        }
                    }
                } catch (Exception ex) {
                    throw new RuntimeException("修改订单商品失败");
                }
            }
            JSONObject response = new JSONObject();
            response.put("purchaseID",purchaseEntry.getId());
            return success("更新订单成功",response);
        }
        return fail("更新订单失败");
    }
    @Override
    public JSONObject delPurchaseCommodity() {
        long purchaseID = getLongParameter("purchaseID", -1);
        if(purchaseID == -1){
            return fail("缺少采购单ID");
        }
        purchaseDataAccessManager.delPurchaseCommodity(purchaseID);

        return success("删除成功");

    }

    @Override
    public JSONObject purchasePutIn() {
        long id = getLongParameter("id",-1);
        String warehouseCode = getUTF("warehouseCode",null);
        String warehouseName = getUTF("warehouseName",null);
        String username = getUTF("username",null);
        int status = getIntParameter("status", -1);
        String exceptionRemark = getUTF("exceptionRemark",null);

        String datas = getUTF("datas",null);

        if(id == -1){
            return fail("缺少采购单id");
        }else if(status==-1){
            return fail("缺少状态status");
        }else if(warehouseCode == null){
            return fail("缺少仓库编码warehouseCode");
        }else if(username== null){
            return fail("缺少入庫用戶名信息username");
        }else if(warehouseName == null){
            return fail("缺少仓库名称warehouseName");
        }else if(datas== null){
            return fail("缺少入库产品明细信息");
        }

        PurchaseWarehouse warehouse =  purchaseDataAccessManager.getByWarehouseCode(warehouseCode);
        if(warehouse==null){
            return fail("仓库不存在！编码："+warehouseCode);
        }

        PurchaseEntry purchaseEntry = new PurchaseEntry();
        purchaseEntry.setId(id);
        purchaseEntry.setStatus(status);
        purchaseEntry.setExceptionRemark(exceptionRemark);
        purchaseEntry.setUsername(username);
        purchaseEntry.setDepositTime(DateUtil.getNowDate());
        purchaseEntry.setUpdateTime(DateUtil.getNowDate());

        if (purchaseDataAccessManager.updatePurchase(purchaseEntry) > 0) {

            if(datas!=null){
                JSONArray dataArray = JSONObject.parseArray(datas);
                for(int i=0;i<dataArray.size();i++){
                    JSONObject object = dataArray.getJSONObject(i);
                    Long commodityId = object.getLong("id");
                    int depositNumber = object.getInteger("depositNumber");
                    int depositSum = object.getInteger("depositSum");
                    PurchaseCommodity purchaseCommodity = new PurchaseCommodity();
                    purchaseCommodity.setId(commodityId);
                    purchaseCommodity.setDepositTime(DateUtil.getNowDate());
                    purchaseCommodity.setDepositNumber(depositSum);
                    purchaseCommodity.setUpdateTime(DateUtil.getNowDate());

                    int result = purchaseDataAccessManager.updatePurchaseCommodity(purchaseCommodity) ;
                    if (result <= 0) {
                        throw new XlibaoIllegalArgumentException("产品入库数量失败");
                    }else {
                        //获取入库产品信息
                        PurchaseCommodity commodity = purchaseDataAccessManager.getPurchaseCommodity(commodityId);
                        //更新商品库存
                        updateStockNumber(id,warehouseCode,warehouseName,commodity.getItemTypeId(),commodity.getItemTypeTitle(),commodity.getItemId(),commodity.getItemName(),commodity.getBarcode(),1,depositNumber);
                    }
                }
            }
            putInWMS(id,datas,warehouse.getWmsKEY(),warehouse.getWarehouseCode());
            //将入库信息推送至WMS系统
        }else {
            throw new XlibaoIllegalArgumentException("入库失败");
        }
        return success("入库成功");
    }

    public void putInWMS(long id,String datas,String ownercode,String whcode){
        String url = ConfigFactory.getDomainNameConfig().wmsRemoteURL+"/sycnInputPlan.do";
        String appkey = "111111";
        String sessionkey = "222222";
        String version = "1.0";
        String param = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceno",id);
        jsonObject.put("planno","00");
        jsonObject.put("title","小惠科技入库单");
        jsonObject.put("ownercode",ownercode);
        jsonObject.put("whcode",whcode);
        jsonObject.put("type","21");

        JSONArray array = new JSONArray();

        JSONArray dataArray = JSONObject.parseArray(datas);
        for(int i=0;i<dataArray.size();i++) {
            JSONObject object = dataArray.getJSONObject(i);
            String commodityId = object.getString("id");
            String itemId = object.getString("itemId");
            String itemName = object.getString("itemName");
            String itemTypeId = object.getString("itemTypeId");
            String itemTypeTitle = object.getString("itemTypeTitle");
            String barcode = object.getString("barcode");
            String depositNumber = object.getString("depositNumber");
            JSONObject dataMap = new JSONObject();
            dataMap.put("procode", itemId);
            dataMap.put("proname", itemName.trim());
            dataMap.put("skucode", itemTypeId);
            dataMap.put("skuname", itemTypeTitle.trim());
            dataMap.put("skuoid", barcode);
            dataMap.put("skucount", depositNumber);
            array.add(dataMap);
        }

        jsonObject.put("details",array);
        String jsonStr = jsonObject.toString();

        param = "appkey="+ URLEncoder.encode(appkey)
                +"&"+"sessionkey="+URLEncoder.encode(sessionkey)+"&"+"version="+URLEncoder.encode(version)+"&ownercode="+URLEncoder.encode(ownercode)
                +"&body="+URLEncoder.encode(jsonStr);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("appkey", URLEncoder.encode(appkey));
        parameters.put("sessionkey", URLEncoder.encode(sessionkey));
        parameters.put("version", URLEncoder.encode(version));
        parameters.put("ownercode", URLEncoder.encode(ownercode));
        parameters.put("body", jsonStr);

        String json = HttpRequest.post(url,parameters);
        JSONObject response = parseObject(json);
        if(response.getBoolean("success")==true){
            logger.info("WMS入库成功！入库码："+response.getString("data"));
        }else {
            throw new XlibaoIllegalArgumentException("WMS入库失败");
        }

    }

    @Override
    public JSONObject updateStock(){
        String warehouseCode = getUTF("warehouseCode",null);
        Long itemId = getLongParameter("itemId",-1);
        int stockType = getIntParameter("stockType",-1);
        int number = getIntParameter("number",-1);

        if(warehouseCode == null){
            return fail("缺少仓库编码warehouseCode");
        }else if(itemId == -1){
            return fail("缺少商品itemId");
        }else if(stockType==-1){
            return fail("缺少操作类型stockType");
        }else if(number==-1){
            return fail("缺少商品数量number");
        }
       JSONObject response = updateStockNumber((long) 0,warehouseCode,null,-1,null,itemId,null,null,stockType,number);
        return success("商品库存更新",response);
    }

    public JSONObject updateStockNumber(Long purchaseID,String warehouseCode,String warehouseName,long itemTypeId,String itemTypeName,long itemId,String itemName,String  barcode,int stockType,int number){
        int stockNumber=number;
        //更新库存数量stockType:0出库1入库2退货
        if(stockType == 0){
            PurchaseCommodityStores purchaseCommodityStore =  purchaseDataAccessManager.getByParameterID(warehouseCode,itemId);
            if(purchaseCommodityStore!=null){
                int storesNumber=purchaseCommodityStore.getStoresNumber();
                stockNumber=storesNumber-number;
                purchaseCommodityStore.setStoresNumber(storesNumber-number);
                purchaseCommodityStore.setUpdateTime(DateUtil.getNowDate());
                if(purchaseDataAccessManager.updateCommodityStores(purchaseCommodityStore)>0){
                    JSONObject response = new JSONObject();
                    response.put("itemId",itemId);
                    response.put("stockNumber",stockNumber);
                    return success(response);
                }else {
                    throw new XlibaoIllegalArgumentException("更新商品库存失败");
                }
            }else{
                return fail("商品库存不存在");
            }
        }else  if(stockType == 1){
            PurchaseRepertoryRecord repertoryRecord = new PurchaseRepertoryRecord();
            repertoryRecord.setPurchaseId(purchaseID);
            repertoryRecord.setBarcode(barcode);
            repertoryRecord.setDepositNumber(number);
            repertoryRecord.setItemId(itemId);
            repertoryRecord.setItemName(itemName);
            repertoryRecord.setRecordType(1);
            repertoryRecord.setWarehouseName(warehouseName);
            repertoryRecord.setWarehouseCode(warehouseCode);
            purchaseDataAccessManager.savePurchaseRepertoryRecord(repertoryRecord);

            PurchaseCommodityStores purchaseCommodityStores =  purchaseDataAccessManager.getByParameterID(warehouseCode,itemId);
            if(purchaseCommodityStores==null){
                PurchaseCommodityStores purchaseCommodityStoresa = new PurchaseCommodityStores();
                purchaseCommodityStoresa.setWarehouseCode(warehouseCode);
                purchaseCommodityStoresa.setWarehouseName(warehouseName);
                purchaseCommodityStoresa.setItemTypeId(itemTypeId);
                purchaseCommodityStoresa.setItemTypeName(itemTypeName);
                purchaseCommodityStoresa.setItemId(itemId);
                purchaseCommodityStoresa.setItemName(itemName);
                purchaseCommodityStoresa.setBarcode(barcode);
                purchaseCommodityStoresa.setStoresNumber(number);
                purchaseCommodityStoresa.setWarnNumber(number);
                purchaseCommodityStoresa.setUpdateTime(DateUtil.getNowDate());
                //添加库存信息
                if(purchaseDataAccessManager.savePurchaseCommodityStores(purchaseCommodityStoresa)>0){
                    JSONObject response = new JSONObject();
                    response.put("itemId",itemId);
                    response.put("stockNumber",stockNumber);
                    return success(response);
                }else {
                    throw new XlibaoIllegalArgumentException("新增商品库存失败");
                }
            }else{
                int storesNumber=purchaseCommodityStores.getStoresNumber();
                stockNumber=storesNumber+number;
                purchaseCommodityStores.setStoresNumber(storesNumber+number);
                purchaseCommodityStores.setUpdateTime(DateUtil.getNowDate());
                if(purchaseDataAccessManager.updateCommodityStores(purchaseCommodityStores)>0){
                    JSONObject response = new JSONObject();
                    response.put("itemId",itemId);
                    response.put("stockNumber",stockNumber);
                    return success(response);
                }else {
                    throw new XlibaoIllegalArgumentException("更新商品库存失败");
                }
            }
        }else if(stockType==2){
            PurchaseCommodityStores purchaseCommodityStore =  purchaseDataAccessManager.getByParameterID(warehouseCode,itemId);
            if(purchaseCommodityStore!=null){
                int storesNumber=purchaseCommodityStore.getStoresNumber();
                stockNumber=storesNumber+number;
                purchaseCommodityStore.setStoresNumber(storesNumber+number);
                purchaseCommodityStore.setUpdateTime(DateUtil.getNowDate());
                if(purchaseDataAccessManager.updateCommodityStores(purchaseCommodityStore)>0){
                    JSONObject response = new JSONObject();
                    response.put("itemId",itemId);
                    response.put("stockNumber",stockNumber);
                    return success(response);
                }else {
                    throw new XlibaoIllegalArgumentException("更新商品库存失败");
                }
            }else{
                return fail("商品库存不存在");
            }
        }
        JSONObject response = new JSONObject();
        response.put("itemId",itemId);
        response.put("stockNumber",stockNumber);
        return success(response);
    }

    @Override
    public JSONObject searchCommodityStoresPage() {

        String warehouseCode = getUTF("warehouseCode", null);
        String itemName = getUTF("itemName", null);
        String barcode = getUTF("barcode", null);
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<PurchaseCommodityStores> purchaseCommodityStores = purchaseDataAccessManager.searchCommodityStoresPage(warehouseCode,itemName,barcode,pageSize,pageStartIndex);
        int count = purchaseDataAccessManager.searchCommodityStoresPageCount(warehouseCode,itemName,barcode);

        JSONObject response = new JSONObject();
        response.put("data", purchaseCommodityStores);
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1) - 1);
        return success(response);
    }

    @Override
    public JSONObject updateCommodityStores() {
        long id = getLongParameter("id",-1);
        int warnNumber = getIntParameter("warnNumber", -1);

        if(id == -1){
            return fail("缺少商品库存id");
        }else if(warnNumber==-1){
            return fail("缺少预警数量warnNumber");
        }
        PurchaseCommodityStores purchaseCommodityStores = new PurchaseCommodityStores();
        purchaseCommodityStores.setId(id);
        purchaseCommodityStores.setWarnNumber(warnNumber);
        purchaseCommodityStores.setUpdateTime(DateUtil.getNowDate());

        if (purchaseDataAccessManager.updateCommodityStores(purchaseCommodityStores)> 0) {
            return success("修改成功");
        }
        return fail("修改失败");
    }
}
