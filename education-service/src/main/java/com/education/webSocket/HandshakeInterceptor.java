package com.education.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wangxue
 * @version 1.0
 * @create 2018-08-14 22:24
 **/
@Slf4j
@Component
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * 握手前
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest)serverHttpRequest;
            HttpServletRequest request = servletServerHttpRequest.getServletRequest();
            String sessionId = request.getSession().getId();
            attributes.put("sessionId", sessionId);
        }
        log.info("beforeHandshake");
        return true;
    }

    /**
     * 握手后
     * @param request
     * @param response
     * @param wsHandler
     * @param ex
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        log.info("afterHandshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
