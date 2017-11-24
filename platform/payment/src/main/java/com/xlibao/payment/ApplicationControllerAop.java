package com.xlibao.payment;

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
public class ApplicationControllerAop extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationControllerAop.class);

    @Around(value = "execution(* com.xlibao.payment.controller.*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        try {
            return point.proceed(args);
        } catch (XlibaoIllegalArgumentException ex) {
            logger.error("支付模块切面拦截异常(非法参数)", ex);
            return fail(ex.getMessage());
        } catch (XlibaoRuntimeException ex) {
            logger.error("支付模块切面拦截异常(运行时异常)", ex);
            return fail(ex.getCode(), ex.getMessage());
        } catch (Throwable cause) {
            logger.error("支付模块切面拦截异常(未知错误)", cause);
            return fail("系统错误，请稍后重试！");
        }
    }
}