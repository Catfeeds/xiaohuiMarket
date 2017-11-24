package com.xlibao.saas.advertise.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 读取网络时间
 *
 * @author SHANHY(365384722@QQ.COM)
 * @date   2015年11月27日
 */
public class GetNetworkTime {


    /**
     * 获取指定网站的日期时间
     *
     * @param
     * @return
     * @author SHANHY
     * @date   2015年11月27日
     */
    public static Date getWebsiteDatetime(){
        String webUrl = "http://www.baidu.com";
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
           // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            //sdf.format(date);
            return date;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
