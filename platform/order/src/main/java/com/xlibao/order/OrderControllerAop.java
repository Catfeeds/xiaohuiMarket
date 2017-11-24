package com.xlibao.order;

import com.xlibao.common.BasicWebService;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.support.PassportRemoteService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @author chinahuangxc on 2017/2/8.
 */
@Service
@Aspect
public class OrderControllerAop extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(OrderControllerAop.class);

    @Around(value = "execution(* com.xlibao.order.controller.OrderController.*(..)) || " +
            "execution(* com.xlibao.order.controller.OrderPaymentController.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        try {
            //PassportRemoteService.signatureSecurity(getHttpServletRequest());
            return pjp.proceed();
        } catch (XlibaoIllegalArgumentException ex) {
            logger.error("订单系统拦截器发生异常，错误信息：" + ex.getMessage());
            return fail(ex.getMessage());
        } catch (XlibaoRuntimeException ex) {
            logger.error("订单系统拦截器发生异常，错误信息：" + ex.getMessage());
            return fail(ex.getCode(), ex.getMessage());
        } catch (Throwable cause) {
            logger.error("", cause);
            return fail("系统错误，请稍后重试！");
        }
    }
}