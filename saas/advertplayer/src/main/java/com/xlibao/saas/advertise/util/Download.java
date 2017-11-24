package com.xlibao.saas.advertise.util;

import com.xlibao.common.exception.XlibaoRuntimeException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 2017/8/22.
 */
@Component
public class Download {

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static int  downLoadFromUrl(String urlStr,String fileName,String savePath) {
        int isDown=1;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }catch (FileNotFoundException e) {
            isDown=0;
            throw new XlibaoRuntimeException("找不到文件");
        } catch (MalformedURLException e) {
            isDown=0;
            throw new XlibaoRuntimeException("网络地址有误");
        } catch (IOException e) {
            isDown=0;
            throw new XlibaoRuntimeException("下载失败");
        }
    return isDown;
    }



    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void main(String args[]) throws Exception {
        String urlStr="http://ourufece1.bkt.clouddn.com/2528.mkv";
        String fileName="121M.mkv";
        String savePath="D:\\\\video";
        Download.downLoadFromUrl(urlStr,fileName,savePath);
    }

}
