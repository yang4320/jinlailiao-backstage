package com.jinlailiao.controller;

import com.jinlailiao.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

//指定WebSocket连接的地址，和前端的new WebSocket(".../websocket")相对应
@ServerEndpoint(value = "/websocket/{openId}/{roomId}")
@Component
//由于要保存会话，spring boot默认为单例，难以操作，声明为protoType避免错误
@Scope("Prototype")
public class MyWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocket.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象，用于群发消息
    private static CopyOnWriteArraySet<Session> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String openId;

//    @Autowired
    public static RoomService roomService;


    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,
                       @PathParam(value = "openId") String openId,
                       @PathParam(value = "roomId") String roomId) {
        logger.info("打开新的socket连接，openId:{},roomId:{}",openId,roomId);
        this.openId = openId;//给openid置值
        this.session = session;

        roomService.addRoomSessionMap(session,Long.parseLong(roomId));

//        webSocketSet.add(this);     //加入set中
//        CopyOnWriteArraySet<Session> sessionArray = roomService.allRoomSessionMap.get(Long.parseLong(roomId));     //加入set中
//        sessionArray.add(session);
//        roomService.allRoomSessionMap.put(Long.parseLong(roomId),sessionArray);

//        addOnlineCount();           //在线数加1
//        sendMessage("当前连接数为：" + onlineCount);
//        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session,
                        @PathParam(value = "roomId") String roomId) {
        roomService.removeRoomSessionMap(session,Long.parseLong(roomId));
//        webSocketSet.remove(this);  //从set中删除
//        subOnlineCount();           //在线数减1
//        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * 客户端发送过来的消息
     * @param data
     * */
    @OnMessage
    public void onMessage(String data, Session session,@PathParam(value = "openId") String openId,
                          @PathParam(value = "roomId") String roomId) {
        logger.info("收到消息，消息内容为：{}" , data);
//        sendMessage(message);
        roomService.sendMsg(session,data,openId,Long.parseLong(roomId));
    }


    /**
     * 发生异常时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    /**
     * 用于主动推送信息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
//        this.session.getBasicRemote().sendText(message);
        this.session.getAsyncRemote().sendText(message);
        //TODO
        //增加查询用户禁言程度的方法
    }


    /**
     * 用于群发自定义消息
     * */
//    public static void sendInfo(String message) throws IOException {
//        for (MyWebSocket item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                continue;
//            }
//        }
//    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}
