package com.xlibao.saas.market.manager.service.purchasemanager.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.common.support.BasicRemoteService;
import com.xlibao.metadata.item.ItemIdName;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.service.purchasemanager.SupplierManagerService;
import com.xlibao.saas.market.manager.service.purchasemanager.WarehouseManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by user on 2017/9/20.
 */
@Transactional
@Service("warehouseManagerService")
public class WarehouseManagerServiceImpl extends BasicRemoteService implements WarehouseManagerService {

    @Override
    public JSONObject searchWarehousePage() {
        String warehouseName = getUTF("warehouseName", "");
        int status = getIntParameter("status", -1);
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("warehouseName", warehouseName);
        parameters.put("status", String.valueOf(status));
        parameters.put("pageSize", String.valueOf(pageSize));
        parameters.put("pageIndex", String.valueOf(pageIndex));

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/searchWarehousePage.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject saveWarehouse() {
        String warehouseCode = getUTF("warehouseCode","");
        String warehouseName = getUTF("warehouseName","");
        String address = getUTF("address","");
        String remark = getUTF("remark","");
        String wmsKEY = getUTF("wmsKEY","");

        if(warehouseCode == "" || warehouseCode.isEmpty()){
            return fail("缺少仓库编码");
        }else if(warehouseName == "" || warehouseName.isEmpty()){
            return fail("缺少仓库名称");
        }else if(address == "" || address.isEmpty()){
            return fail("缺少仓库地址");
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("warehouseCode", warehouseCode);
        parameters.put("warehouseName", warehouseName);
        parameters.put("address", address);
        parameters.put("remark", remark);
        parameters.put("wmsKEY", wmsKEY);

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/saveWarehouse.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject getAllWarehouse() {
        Map<String, String> parameters = new HashMap<>();
        try{
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/getAllWarehouse.do";
            JSONObject response = executor(url,parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject updateWarehouse() {
        long id = getLongParameter("id",-1);
        String warehouseCode = getUTF("warehouseCode","");
        String warehouseName = getUTF("warehouseName","");
        String address = getUTF("address","");
        String remark = getUTF("remark","");
        String wmsKEY = getUTF("wmsKEY","");
        if(id==-1){
            return fail("缺少仓库ID");
        }else if(warehouseCode == "" || warehouseCode.isEmpty()){
            return fail("缺少仓库编码");
        }else if(warehouseName == "" || warehouseName.isEmpty()){
            return fail("缺少仓库名称");
        }else if(address == "" || address.isEmpty()){
            return fail("缺少仓库地址");
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", String.valueOf(id));
        parameters.put("warehouseCode", warehouseCode);
        parameters.put("warehouseName", warehouseName);
        parameters.put("address", address);
        parameters.put("remark", remark);
        parameters.put("wmsKEY", wmsKEY);

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/updateWarehouse.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject updateWarehouseStatus() {
        long id = getLongParameter("id",-1);

        int status = getIntParameter("status",-1);
        String  stopRemark = getUTF("stopRemark","");

        if(id==-1){
            return fail("缺少仓库ID");
        }else if(status==-1){
            return fail("缺少状态参数");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", String.valueOf(id));
        parameters.put("status",  String.valueOf(status));
        parameters.put("stopRemark", stopRemark);

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/updateWarehouseStatus.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject getWarehouse() {
        long id = getLongParameter("id",-1);
        if(id==-1){
            return fail("缺少仓库ID");
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", String.valueOf(id));

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/getWarehouse.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject saveWarehouseUser() {
        long warehouseID = getLongParameter("warehouseID",-1);
        String username = getUTF("userName","");
        String remark = getUTF("remark","");
        if(warehouseID==-1){
            return fail("缺少仓库ID");
        }else if(username == "" || username.isEmpty()){
            return fail("缺少仓库管理员用户名");
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("warehouseID", String.valueOf(warehouseID));
        parameters.put("username", username);
        parameters.put("password", "xiaohui2017");
        parameters.put("remark", remark);

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/saveWarehouseUser.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject getWarehouseUsers() {
        long warehouseID = getLongParameter("id",-1);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("warehouseID",String.valueOf(warehouseID));
        try{
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/getWarehouseUsers.do";
            JSONObject response = executor(url,parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject updateWarehouseUserPassword() {
        long id = getLongParameter("id",-1);
        String password = getUTF("password",null);

        if(id==-1){
            return fail("缺少仓库管理员ID");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", String.valueOf(id));
        parameters.put("password", "xiaohui2017");

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/updateWarehouseUserPwd.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject deleteWarehouseUser() {
        long id = getLongParameter("id",-1);

        if(id==-1){
            return fail("缺少仓库管理员ID");
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", String.valueOf(id));

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/deleteWarehouseUser.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }
    @Override
    public JSONObject searchCommodityStoresPage() {

        String warehouseCode = getUTF("warehouseCode", "");
        String itemName = getUTF("itemName", "");
        String barcode = getUTF("barcode", "");
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("warehouseCode", warehouseCode);
        parameters.put("itemName", itemName);
        parameters.put("barcode", barcode);
        parameters.put("pageSize", String.valueOf(pageSize));
        parameters.put("pageIndex", String.valueOf(pageIndex));

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/searchCommodityStoresPage.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject updateCommodityStores() {
        long id = getLongParameter("id",-1);
        int warnNumber = getIntParameter("warnNumber", -1);

        if(id == -1){
            return fail("缺少商品库存ID");
        }else if(warnNumber==-1){
            return fail("缺少预警数量");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", String.valueOf(id));
        parameters.put("warnNumber", String.valueOf(warnNumber));

        try {
            String url = ConfigFactory.getDomainNameConfig().purchaseRemoteURL + "purchase/updateCommodityStores.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }



}
