package com.xlibao.common.servlet;

import com.xlibao.common.thread.AsyncScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

public class XlibaoFilterChain implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(XlibaoFilterChain.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String encoding = "utf-8";
        // 在请求里设置上指定的编码
        request.setCharacterEncoding(encoding);
        response.setContentType("application/json; charset=" + encoding);
        response.setCharacterEncoding(encoding);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 每次会话的超时时间
        httpServletRequest.getSession().setMaxInactiveInterval(1200);
        // 请求信息跟踪
        requestInfoTrack(httpServletRequest);
        // 逻辑处理
        filterChain.doFilter(request, response);
    }

    private void requestInfoTrack(HttpServletRequest request) {
        Map<String, String[]> requestParams = request.getParameterMap();
        if (requestParams == null || requestParams.isEmpty()) {
            return;
        }
        HttpSession session = request.getSession();

        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder builder = new StringBuilder();
        builder.append("Access url = ").append(request.getRequestURL().toString()).append("\r\n\t\tSession id = ").append(session.getId()).append("\r\n\t\t");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            builder.append(headerName).append(" = ");
            builder.append(request.getHeader(headerName)).append("\r\n\t\t");
        }

        StringBuilder fullRequestURL = new StringBuilder().append(request.getRequestURL().toString());
        fullRequestURL.append("?");

        builder.append("\r\n\t\t========================================== Parameter begin ==========================================\r\n\t\t");
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            String value = "";
            for (int i = 0; i < values.length; i++) {
                value += values[i] + ((i == values.length - 1) ? "" : ",");
            }
            fullRequestURL.append(name).append("=").append(value).append("&");
            builder.append("\t").append(name).append(" = ").append(value).append("\r\n\t\t");
        }
        builder.append("=========================================== Parameter end ===========================================\r\n\t\t");

        builder.append("Full request url is ").append(fullRequestURL).append("\r\n\t\t");

        logger.info("\r\n\t\t↓↓↓↓↓↓↓↓↓↓ 消息头内容 ↓↓↓↓↓↓↓↓↓↓\r\n\t\t"
                + builder +
                "↑↑↑↑↑↑↑↑↑↑ 消息头内容 ↑↑↑↑↑↑↑↑↑↑");
    }

    @Override
    public void destroy() {
        logger.info("-============================== 系统正在停止服务 ==============================-");
        logger.warn("【严重警告】系统发起了停服指令，必须确认是否系统在维护中");
        logger.info("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 准备执行所有等待中的任务 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        AsyncScheduledService.destory();
        logger.info("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 执行完成所有等待中的任务 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");

        System.exit(0);
    }
}