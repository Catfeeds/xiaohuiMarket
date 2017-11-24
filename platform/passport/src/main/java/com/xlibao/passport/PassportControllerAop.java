package com.xlibao.passport;

import com.xlibao.common.BasicWebService;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.exception.XlibaoRuntimeException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *     <b>Aop切面控制器</b>
 * </pre>
 *
 * @author chinahuangxc on 2016/11/20.
 */
@Service
@Aspect
public class PassportControllerAop extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(PassportControllerAop.class);

    @Around(value = "execution(* com.xlibao.passport.controller.*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            return point.proceed(point.getArgs());
        } catch (XlibaoIllegalArgumentException ex) {
            logger.error("通行证切面拦截异常(非法参数)，错误信息：" + ex.getMessage());
            return fail(ex.getMessage());
        } catch (XlibaoRuntimeException ex) {
            logger.error("通行证切面拦截异常(运行时异常)，错误信息：" + ex.getMessage());
            return fail(ex.getCode(), ex.getMessage());
        } catch (Throwable cause) {
            logger.error("通行证切面拦截异常(未知错误)", cause);
            return fail("系统错误，请稍后重试！");
        }
    }
}