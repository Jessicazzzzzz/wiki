package com.example.springwiki.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author jessica~
 * @version 1.0
 */
@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 每一个客户端一个token
     */
    private String token;
    private static final HashMap<String, Session> map = new HashMap<>();

    /**
     * 连接成功
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        map.put(token, session);
        this.token = token;
        LOG.info("有新的连接:token:{},session id:{},当前连接数:{}", token, session.getId(), map.size());

    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(Session session) {
        map.remove(this.token);
        LOG.info("连接关闭:token:{},session id:{},当前连接数:{}", token, session.getId(), map.size());

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("收到消息:{},内容:{}", token, message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("发生错误", error);
    }

    /**
     * 群发消息
     * 添加async ,进行异步化操作
     */

    public void sendInfo(String message) {
        for (String token : map.keySet()) {
            Session session = map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("推送消息失败:{},内容:{}", token, message);
            }
            LOG.info("推送消息:{},内容:{}", token, message);
        }
    }
}
