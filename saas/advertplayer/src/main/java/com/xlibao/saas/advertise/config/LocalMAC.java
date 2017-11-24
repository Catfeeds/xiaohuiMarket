package com.xlibao.saas.advertise.config;

/**
 * Created by user on 2017/8/22.
 */
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/*

 * 物理地址是48位，别和ipv6搞错了

 */

public class LocalMAC {

    /**

     * @param args

     * @throws UnknownHostException

     * @throws SocketException

     */

    /**
     * 获取本机MAC
     * @return
     */
    public static String getLocalMac()  {

        // TODO Auto-generated method stub
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //获取网卡，获取地址
        byte[] mac = new byte[0];
        try {
            mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer("");
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i]&0xff;
            String str = Integer.toHexString(temp);
            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }
}

