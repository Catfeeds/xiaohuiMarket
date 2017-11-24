package com.xlibao.saas.market.adver;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by admin on 2017/8/22.
 */
public class BaseFreeMarkerView extends FreeMarkerView{

    private static final String REQUEST_PATH = "base";
    private static final String RES_PATH = "res";

    @Override
    protected void exposeHelpers(Map<String, Object> model,
                                 HttpServletRequest request) throws Exception
    {
        //如果有登录过，则设置用户名，用户ID
        Object obj = request.getSession().getAttribute("loginUserName");
        if(obj != null) {
            model.put("userName", (String)obj);
        } else {
            model.put("userName", "未登录");
        }
        obj = request.getSession().getAttribute("loginUserId");
        if(obj != null) {
            model.put("userId", (String)obj);
        } else {
            model.put("userId", "0");
        }

        model.put(RES_PATH, request.getContextPath());
        //设置base
        model.put(REQUEST_PATH, request.getContextPath() + "/marketadver");
        super.exposeHelpers(model, request);
    }
}
