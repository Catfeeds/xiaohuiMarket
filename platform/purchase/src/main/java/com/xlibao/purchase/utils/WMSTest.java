package com.xlibao.purchase.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.http.HttpRequest;

import java.net.URLEncoder;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by user on 2017/10/9.
 */
public class WMSTest {

    public static  void itemRemote(){
        try {
            String url = "http://localhost:8080/item/getItemTemplateList.do?pageSize=100&pageStartIndex=0";
            String json = HttpRequest.get(url);
            JSONObject response = parseObject(json);
            JSONObject itemResponse = response.getJSONObject("response");
            JSONArray items = itemResponse.getJSONArray("data");
            putInWMS(items);
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    public static void putInWMS(JSONArray items){
        RemoteThread thread = new RemoteThread(items);
        thread.start();
    }

    //下载的线程
    private static class RemoteThread extends Thread{

        private JSONArray items;

        public RemoteThread(JSONArray items) {
            this.items = items;

        }

        @Override
        public void run() {
            for(int i=0;i<items.size();i++) {
                String url = "http://121.199.165.250/wms/wmservice/syncProduct.do";
                String appkey = "111111";
                String sessionkey = "222222";
                String version = "1.0";
                String param = "";
                String ownercode ="0030";

                JSONArray array = new JSONArray();

                JSONObject object = items.getJSONObject(i);
                String barcode = object.getString("barcode");
                String id = object.getString("id");
                String name = object.getString("name");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code",id);
                jsonObject.put("name",name);
                jsonObject.put("productType","0000");
                jsonObject.put("ownercode","xiaohuikeji");

                JSONObject dataMap = new JSONObject();
                dataMap.put("barcode", barcode);
                dataMap.put("ownercode", "xiaohuikeji");
                dataMap.put("productcode", id);
                dataMap.put("productname", name);
                dataMap.put("status", "Y");
                array.add(dataMap);

                jsonObject.put("scSkus",array);
                String jsonStr = jsonObject.toString();

                param = "appkey="+ URLEncoder.encode(appkey)
                        +"&"+"sessionkey="+URLEncoder.encode(sessionkey)+"&"+"version="+URLEncoder.encode(version)+"&ownercode="+URLEncoder.encode(ownercode)
                        +"&body="+URLEncoder.encode(jsonStr);

                String json = HttpRequest.get(url+"?"+param);
                JSONObject response = parseObject(json);
                if(response.getBoolean("success")==true){
                    System.out.println("商品同步成功"+i);
                }else {
                    throw new XlibaoIllegalArgumentException("WMS入库失败");
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public static void main(String []agr){
        itemRemote();
    }
}
