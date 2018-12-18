package com.jinlailiao.service;

import com.jinlailiao.bean.Room;
import com.jinlailiao.bean.RoomVo;
import com.jinlailiao.bean.UserVo;

import javax.websocket.Session;
import java.util.List;

public interface RoomService {


    public RoomVo findMyNearbyRoom(Double latitude, Double longitude, int pageNumber);

    public Room createNewRoom(Room room);

    public void joinRoom(long roomId,String openId);

    /**
     * 加入房间
     * @param session
     * @param roomId
     */
    public void addRoomSessionMap(Session session,Long roomId);

    /**
     * 离开房间
     * @param session
     * @param roomId
     */
    public void removeRoomSessionMap(Session session,Long roomId);

    /**
     * 获得房间人数
     * @param roomId
     * @return
     */
    public int getRoomPersonCount(Long roomId);




    /**
     * 发送消息
     * @param session
     * @param msg
     */
    public void sendMsg(Session session, String msg,String openId,Long roomId);

    UserVo getUserForbidden(String openId,String data);


}
