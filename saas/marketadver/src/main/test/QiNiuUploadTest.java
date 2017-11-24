import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;
import com.qiniu.storage.Configuration;


/**
 * Created by admin on 2017/8/22.
 */
public class QiNiuUploadTest {

    private String accessKey = "-H7UsuTzqZpl5hShnGsKrLN7PeK3X1AsFWmnf7r-";
    private String secretKey = "-_mtaspBa3qLlVKxXhbjIjvIQ-mNl3OvfbhLEDLb";
    private String bucket = "marketadver";

    @Test
    public void test(){

        Configuration cfg = new Configuration(Zone.zone2());

        UploadManager uploadManager = new UploadManager(cfg);

        String localFilePath = "C:\\Users\\admin\\Desktop\\test.wmv";

        String key = "test";

        Auth auth = Auth.create(accessKey,secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath,key,upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);

        } catch (QiniuException e) {
            Response r = e.response;
            System.out.println(r.toString());
            try {
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {

            }
        }


    }
}
