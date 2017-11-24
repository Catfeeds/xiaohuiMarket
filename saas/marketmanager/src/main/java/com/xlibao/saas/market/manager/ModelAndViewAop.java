package com.xlibao.saas.market.manager;

import com.xlibao.common.BasicWebService;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.saas.market.manager.config.LogicConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/21.
 */
@Service
@Aspect
public class ModelAndViewAop extends BaseController {

    @Around(value = "execution(* com.xlibao.saas.market.manager.controller.*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        getHttpServletResponse().setHeader("content-type", "text/html");
        try {
            return point.proceed(args);
        } catch (XlibaoIllegalArgumentException ex) {
            ex.printStackTrace();
            fali(ex.getMessage());
            return LogicConfig.FTL_ERROR;
        } catch (XlibaoRuntimeException ex) {
            ex.printStackTrace();
            fali(ex.getMessage());
            return LogicConfig.FTL_ERROR;
        } catch (Throwable cause) {
            cause.printStackTrace();
            fali("系统错误，请稍后重试！");
            return LogicConfig.FTL_ERROR;
        }
    }

    void fali(String error) {
        HttpServletRequest request = getHttpServletRequest();
        request.setAttribute("error", error);
    }
}
