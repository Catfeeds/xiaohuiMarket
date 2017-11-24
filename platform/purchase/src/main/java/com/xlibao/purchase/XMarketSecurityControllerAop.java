package com.xlibao.purchase;

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
 * @author chinahuangxc on 2017/10/15.
 */
@Service
@Aspect
public class XMarketSecurityControllerAop extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(XMarketSecurityControllerAop.class);

    @Around(value = "execution(* com.xlibao.purchase.controller.PurchaseAppController.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        try {
            long passportId = getLongParameter("passportId");
            String accessToken = getUTF("accessToken");

            accessToken = PassportRemoteService.changeAccessToken(passportId, accessToken);
            setAccessToken(accessToken);

            return point.proceed(args);
        } catch (XlibaoIllegalArgumentException ex) {
            logger.error("(安全)采购APP拦截异常(非法参数)，错误信息：" + ex.getMessage());
            return fail(ex.getMessage());
        } catch (XlibaoRuntimeException ex) {
            logger.error("(安全)采购APP拦截异常(运行时异常)，错误信息：" + ex.getMessage());
            return fail(ex.getCode(), ex.getMessage());
        } catch (Throwable cause) {
            logger.error("(安全)采购APP拦截异常(未知错误)", cause);
            return fail("系统错误，请稍后重试！");
        }
    }
}
