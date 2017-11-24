package com.xlibao.saas.market.core.service.hisdk;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

import java.io.UnsupportedEncodingException;

class Vapi {

    public interface Vdll extends Library {

        Vdll INSTANCE = (Vdll) Native.loadLibrary("D:/VguangX1000Dll", Vdll.class);

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
    public boolean vbarOpen() {
        return Vdll.INSTANCE.connectDevice() > 0;
    }

    // 背光控制
    public void vbarBackLight(boolean bool) {
        if (bool) {
            Vdll.INSTANCE.lightOn();
            return;
        }
        Vdll.INSTANCE.lightOff();
    }

    // 设置码制
    // 参数symbol_type：0-无，1-QR, 2-DM, 4-一维码,
    // 3-QR&DM, 5-QR&一维码, 6-DM&一维码,
    // 7-QR&DM&一维码
    public boolean vbarAddSymbolType(int symbol_type) {
        return Vdll.INSTANCE.setBarcodeFormat(symbol_type) > 0;
    }

    //关闭设备
    public void vbarClose() {
        Vdll.INSTANCE.disconnectDevice();
    }

    private byte[] resultDev = new byte[1024];
    private IntByReference length = new IntByReference(256);

    // 获取版本信息
    public String getDevInfo() throws UnsupportedEncodingException {
        if (Vdll.INSTANCE.getDeviceVer(resultDev, length) > 0) {
            return new String(resultDev, "UTF-8");
        }
        return "获取版本失败";
    }

    private byte[] resultDecode = new byte[1024];
    private IntByReference decodeLength = new IntByReference(256);

    // 获取扫码结果
    public String getDecodeResult() throws UnsupportedEncodingException {
        if (Vdll.INSTANCE.getResultStr(resultDecode, decodeLength, 1024, 2) > 0) {
            return new String(resultDecode, "UTF-8");
        }
        return null;
    }
}