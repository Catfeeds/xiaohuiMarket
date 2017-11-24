package com.xlibao.saas.market.manager;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhumg on 8/9.
 */
public class BaseFreeMarkerView extends FreeMarkerView {

    private static final String REQUEST_PATH = "base";
    private static final String RES_PATH = "res";

    @Override
    protected void exposeHelpers(Map<String, Object> model,
                                 HttpServletRequest request) throws Exception
    {
        //如果有登录过，则设置accessToken

        Object obj = request.getSession().getAttribute("accessToken");
        if(obj != null) {
            model.put("accessToken", (String)obj);
        } else {
            model.put("accessToken", "");
        }

        //设置res地址
        model.put(RES_PATH, request.getContextPath());
        //设置base地址
        model.put(REQUEST_PATH, request.getContextPath() + "/marketmanager");

        super.exposeHelpers(model, request);
    }
}