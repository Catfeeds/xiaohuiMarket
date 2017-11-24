package com.xlibao.saas.market.adver;

import com.xlibao.common.BasicWebService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/8/22.
 */
@Service
@Aspect
public class ModelAndViewAop extends BasicWebService {

    @Around(value = "execution(* com.xlibao.saas.market.adver.controller.*.*(..))")
    public Object around(ProceedingJoinPoint point){
        Object[] args = point.getArgs();

        getHttpServletResponse().setHeader("content-type", "text/html");

        try {
            return point.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return "error";
        }
    }
}
