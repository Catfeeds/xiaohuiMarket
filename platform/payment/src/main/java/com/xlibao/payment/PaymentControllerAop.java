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
 * @author chinahuangxc on 2017/2/8.
 */
@Service
@Aspect
public class PaymentControllerAop extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentControllerAop.class);

    @Around(value = "execution(* com.xlibao.payment.controller.PaymentController.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        try {
//            HttpServletRequest request = getHttpServletRequest();
//            // 获取请求地址
//            String requestPath = request.getRequestURL().toString();
//            Map<String, String[]> requestParams = request.getParameterMap();
//            Map<String, String> signParameters = new HashMap<>();
//            String partnerId = getUTF("partnerId");
//            String appId = getUTF("appId");
//            String sign = getUTF("sign");
//            for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
//                String name = entry.getKey();
//                String[] values = entry.getValue();
//                String valueStr = "";
//                for (int i = 0; i < values.length; i++) {
//                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//                }
//                if ("sign".equals(name)) {
//                    continue;
//                }
//                signParameters.put(name, valueStr);
//            }
//            // 参数排序(去除内容为空的参数)
//            String parameterSort = CommonUtils.parameterSort(signParameters, new ArrayList<>());
//            JSONObject response = PassportRemoteService.signatureSecurity(partnerId, appId, sign, parameterSort);
//            logger.info("支付请求验签结果：" + response + "，前端请求链接：" + requestPath + "");
            // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
            return pjp.proceed();
        } catch (XlibaoIllegalArgumentException ex) {
            logger.error("", ex);
            return fail(ex.getMessage());
        } catch (XlibaoRuntimeException ex) {
            logger.error("", ex);
            return fail(ex.getCode(), ex.getMessage());
        } catch (Throwable cause) {
            logger.error("", cause);
            return fail("系统错误，请稍后重试！");
        }
    }
}