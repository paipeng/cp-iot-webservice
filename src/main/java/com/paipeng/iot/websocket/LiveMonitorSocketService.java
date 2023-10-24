package com.paipeng.iot.websocket;

import com.paipeng.iot.entity.Record;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Component
@ServerEndpoint(value = "/websocket/live/{remoteClientUUID}")
public class LiveMonitorSocketService {
    private static final Logger log = LoggerFactory.getLogger(LiveMonitorSocketService.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象
     */
    private static ConcurrentHashMap<String, LiveMonitorSocketService> webSocketMap = new ConcurrentHashMap<>();
    public static ArrayList<String> sids = new ArrayList<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    private String sessionId = "";

    /**
     * 连接成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("remoteClientUUID") String remoteClientUUID) {
        log.info("remoteClientUuid:" + remoteClientUUID);
        this.session = session;
        this.sessionId = remoteClientUUID;
        webSocketMap.put(remoteClientUUID, this);
        addOnlineCount();
        log.info("用户:" + remoteClientUUID + " 加入！当前在线人数为：" + getOnlineCount());

        try {
            sendMessage("连接成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 关闭连接调用方法
     */
    @OnClose
    public void onClose() {
        log.info("用户:" + sessionId + " 退出");
        webSocketMap.remove(sessionId);
        subOnlineCount();
        log.info("有一个连接关闭,当前在线人数:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端用户：{} 消息:{}", sessionId, message);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("用户错误:" + this.session + ",原因:" + throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * 向客户端发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 群发自定义消息
     */
    public void sendData(Record record) {
        for (String item : webSocketMap.keySet()) {
            log.info("send data to: " + item);
            sendMessage(item, record);
        }
    }

    public void sendMessage(String sessionId, Record record) {
        log.info("服务端发送消息到 {},消息 -> 更加解码记录", sessionId);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = null;
            try {
                jsonString = objectMapper.writeValueAsString(record);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }

            log.info("jsonString: " + jsonString);

            if (webSocketMap == null) {
                log.error("webSocketMap invalid");
            }
            if (webSocketMap.get(sessionId) == null) {
                log.error("webSocketMap get " + sessionId + " invalid");
            }

            Session session = webSocketMap.get(sessionId).session;
            if (session != null) {
                if (session.getBasicRemote() != null) {
                    session.getBasicRemote().sendText(jsonString);
                } else {
                    log.error("session.getBasicRemote() is invalid");
                }
            } else {
                log.error("用户 {} 不在线", sessionId);
            }
        } catch (IOException e) {
            log.error("webSocketMap :" + e.getMessage());
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

}
