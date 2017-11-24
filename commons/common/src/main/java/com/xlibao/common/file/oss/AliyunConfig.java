package com.xlibao.common.file.oss;

import com.aliyun.oss.model.CannedAccessControlList;

public class AliyunConfig {

	// 外网访问节点
	private static String OSS_PUBLIC_END_POINT = "oss-cn-shenzhen.aliyuncs.com";
	// ECS访问节点
	private static String OSS_INTERNAL_END_POINT = "oss-cn-shenzhen-internal.aliyuncs.com";
	// 阿里云分配的Access key id
	private static String ACCESS_KEY_ID = "LTAIzupnl78sRpBm";
	//阿里云分配的Access key secret
	private static String ACCESS_KEY_SECRET = "4zbTwU9ghdfpmr2XPE8GHQsDeIv3eP";
	// 开放读取权限
	public static final CannedAccessControlList PUBLIC_READ = CannedAccessControlList.PublicRead;
	// 私有权限
	public static final CannedAccessControlList PRIVATE = CannedAccessControlList.Private;
	// 开放读写权限
	public static final CannedAccessControlList PUBLIC_READ_WRITE = CannedAccessControlList.PublicReadWrite;

    public static void setOssPublicEndPoint(String ossPublicEndPoint) {
        AliyunConfig.OSS_PUBLIC_END_POINT = ossPublicEndPoint;
    }

    public static void setOssInternalEndPoint(String ossInternalEndPoint) {
        AliyunConfig.OSS_INTERNAL_END_POINT = ossInternalEndPoint;
    }

    public static void setAccessKeyId(String accessKeyId) {
        AliyunConfig.ACCESS_KEY_ID = accessKeyId;
    }

    public static void setAccessKeySecret(String accessKeySecret) {
        AliyunConfig.ACCESS_KEY_SECRET = accessKeySecret;
    }

    public static String getOssPublicEndPoint() {
        return OSS_PUBLIC_END_POINT;
    }

    public static String getOssInternalEndPoint() {
        return OSS_INTERNAL_END_POINT;
    }

    public static String getAccessKeyId() {
        return ACCESS_KEY_ID;
    }

    public static String getAccessKeySecret() {
        return ACCESS_KEY_SECRET;
    }
}