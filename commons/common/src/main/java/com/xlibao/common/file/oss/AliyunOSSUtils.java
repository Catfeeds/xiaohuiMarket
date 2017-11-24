package com.xlibao.common.file.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.xlibao.common.CommonUtils;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class AliyunOSSUtils {

    private static final AtomicLong LOGIC_UNIQUE_KEY = new AtomicLong(10000);

    public static void createBucket(String bucketName) {
        // 创建OSSClient实例
        OSSClient ossClient = getOSSClient();

        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        // 设置bucket权限
        createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
        // 创建bucket
        ossClient.createBucket(createBucketRequest);
        // 关闭client
        ossClient.shutdown();
    }

    public static List<Bucket> listBuckets() {
        // 创建OSSClient实例
        OSSClient ossClient = getOSSClient();
        // 列举bucket
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }
        // 关闭client
        ossClient.shutdown();
        return buckets;
    }

    public static void removeBucket(String bucketName) {
        // 创建OSSClient实例
        OSSClient ossClient = getOSSClient();
        try {
            // 删除bucket
            ossClient.deleteBucket(bucketName);
        } catch (Throwable cause) {
            String message = cause.getMessage();
            if (CommonUtils.nullToEmpty(message).contains("The specified bucket does not exist.")) {
                System.out.println(bucketName + " 已不存在");
                return;
            }
            cause.printStackTrace();
        }
        // 关闭client
        ossClient.shutdown();
    }

    public static boolean doesBucketExist(String bucketName) {
        OSSClient ossClient = getOSSClient();
        boolean exists = ossClient.doesBucketExist(bucketName);
        // 关闭client
        ossClient.shutdown();
        return exists;
    }

    public static String uploadImageFile(String bucketName, String remoteDirectory, String filePath) throws FileNotFoundException {
        OSSClient ossClient = getOSSClient();

        File file = new File(filePath);
        InputStream input = new FileInputStream(file);

        String key = remoteDirectory + "/" + file.getName();
        key = key.replaceAll(" ", "");

        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentType("image/jpeg");

        ossClient.putObject(bucketName, key, input, objectMeta);
        ossClient.shutdown();
        return "http://" + bucketName + "." + getPublicAccessURL(key);
    }

    private static String getPublicOnlyKey(String path) {
        return "public/" + path + "/" + System.currentTimeMillis() + "/" + LOGIC_UNIQUE_KEY.incrementAndGet();
    }

    private static String getPublicAccessURL(String key) {
        return AliyunConfig.getOssPublicEndPoint() + "/" + key;
    }

    private static OSSClient getOSSClient() {
        return new OSSClient(AliyunConfig.getOssPublicEndPoint(), AliyunConfig.getAccessKeyId(), AliyunConfig.getAccessKeySecret());
    }

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\item_image");
        for (File f : file.listFiles()) {
            String address = uploadImageFile("xmarket", "market/app/icon", f.getAbsolutePath());
            System.out.println(address);
        }
//        String address = uploadImageFile("xmarket", "item", "D:\\item_image\\6921168593033.jpg");
//        System.out.println(address);
//
//        address = uploadImageFile("xmarket", "item", "D:\\item_image\\6923644242961.jpg");
//        System.out.println(address);
//
//        address = uploadImageFile("xmarket", "item", "D:\\item_image\\6932512600369.jpg");
//        System.out.println(address);
//
//        address = uploadImageFile("xmarket", "item", "D:\\item_image\\6949352201434.jpg");
//        System.out.println(address);
    }
}