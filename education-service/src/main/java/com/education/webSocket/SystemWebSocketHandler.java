package com.education.webSocket;


import com.education.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author taoge
 * @version 1.0
 * @create_at 2018年11月18日下午5:24:06
 */
@Component
@Slf4j
public class SystemWebSocketHandler implements WebSocketHandler {

    private static final Map<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();

    /**
     * 连接 就绪时
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Map<String, Object> attr = webSocketSession.getAttributes();
        String sessionId = (String) attr.get("sessionId");
        webSocketSessionMap.put(sessionId, webSocketSession);
        log.info("webSocket连接成功");
    }

    /**
     * 处理消息
     * @param webSocketSession
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        System.err.println("handleMessage");
    }

    /**
     * 发送消息到页面
     * @param sessionId
     * @param message
     */
    public void sendMessageToPage(String sessionId, String message) {
        WebSocketSession webSocketSession = webSocketSessionMap.get(sessionId);
        if (ObjectUtils.isNotEmpty(webSocketSession)) {
            try {
                webSocketSession.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("webSocket 消息发送异常");
            }
        }
    }

    /**
     * 处理传输时异常
     * @param webSocketSession
     * @param throwable
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        log.error("webSocket连接异常");
    }

    /**
     * 关闭 连接时
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.warn("webSocket成功关闭");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
