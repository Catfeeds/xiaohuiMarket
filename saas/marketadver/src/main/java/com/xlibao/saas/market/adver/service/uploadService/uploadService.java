package com.xlibao.saas.market.adver.service.uploadService;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by admin on 2017/8/24.
 */
public interface uploadService {

    public JSONObject uploadToQiniu(MultipartFile[] files);
}
