package com.xlibao.saas.market.core.scan;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

import java.io.UnsupportedEncodingException;

/**
 * @author chinahuangxc on 2017/9/1.
 */
public class VGApi {

    private static final String DLL_PATH = "D:/VguangX1000Dll";

    public interface VGDll extends Library {

        VGDll INSTANCE = (VGDll) Native.loadLibrary(DLL_PATH, VGDll.class);

        // 连接设备
        int connectDevice();

        // 开灯
        int lightOn();

        // 关灯
        int lightOff();

        // 设置码制
        int setBarcodeFormat(int barcodeFormat);

        // 关闭设备
        int disconnectDevice();

        // 得到设备版本号
        int getDeviceVer(byte[] result_dev, IntByReference length);

        // 得到扫码信息
        int getResultStr(byte[] result_decode, IntByReference length, int maxLen, int timeout);
    }

    // 打开设备
    public static boolean connectDevice() {
        return VGDll.INSTANCE.connectDevice() > 0;
    }

    // 背光控制
    public static void lightControl(boolean bool) {
        if (bool) {
            VGDll.INSTANCE.lightOn();
            return;
        }
        VGDll.INSTANCE.lightOff();
    }

    // 设置码制
    // 参数symbol_type：0-无，1-QR, 2-DM, 4-一维码,
    // 3-QR&DM, 5-QR&一维码, 6-DM&一维码,
    // 7-QR&DM&一维码
    public static boolean setBarcodeFormat(int symbolType) {
        return VGDll.INSTANCE.setBarcodeFormat(symbolType) > 0;
    }

    // 关闭设备
    public static void disconnectDevice() {
        VGDll.INSTANCE.disconnectDevice();
    }

    private static byte[] resultDev = new byte[1024];
    private static IntByReference length = new IntByReference(256);

    // 获取版本信息
    public static String getDeviceVersion() throws Exception {
        return (VGDll.INSTANCE.getDeviceVer(resultDev, length) > 0) ? new String(resultDev, "UTF-8") : "获取版本失败";
    }

    private static byte[] resultDecode = new byte[1024];
    private static IntByReference decodeLength = new IntByReference(256);

    // 获取扫码结果
    public static String getScanContent() throws UnsupportedEncodingException {
        return (VGDll.INSTANCE.getResultStr(resultDecode, decodeLength, 1024, 2) > 0) ? new String(resultDecode, "UTF-8") : null;
    }
}