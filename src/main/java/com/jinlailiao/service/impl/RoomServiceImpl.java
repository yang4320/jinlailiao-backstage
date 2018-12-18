package com.jinlailiao.service.impl;

import com.jinlailiao.bean.Room;
import com.jinlailiao.bean.RoomRelationship;
import com.jinlailiao.bean.RoomVo;
import com.jinlailiao.bean.UserVo;
import com.jinlailiao.dao.RoomRelationshipRepository;
import com.jinlailiao.dao.RoomRepository;
import com.jinlailiao.service.RoomService;
import com.jinlailiao.service.UserService;
import com.jinlailiao.utils.LocationUtils;
import com.jinlailiao.utils.SensitiveWordUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.websocket.Session;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    //半径范围
    @Value("${location.raidus}")
    private int raidus;
    @Value(("${max.forbidden.votecount}"))
    private float maxForbiddenVoteCount;
    @Value(("${max.forbidden.day}"))
    private int maxForbiddenDay;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomRelationshipRepository roomRelationshipRepository;
    @Resource
    private UserService userService;

    private static final int pageSize = 10;


////    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象，用于群发消息
//    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    //用于存放roomId和其中用户socket的map
    public static ConcurrentHashMap<Long,CopyOnWriteArraySet<Session>> allRoomSessionMap = new ConcurrentHashMap<Long,CopyOnWriteArraySet<Session>>();

    public RoomVo findMyNearbyRoom(Double latitude, Double longitude, int pageNumber){
        RoomVo roomVo = new RoomVo();
//        roomVo.setPageNumber(pageNumber);

        List<Room> rooms =  roomRepository.findMyNearbyRoom(latitude,longitude,(pageNumber -1) * pageSize,pageSize);
        //设置房间人数
        rooms.forEach(room -> {
            room.setPeopleCount(getRoomPersonCount(room.getId()));
        });

        roomVo.setRoomList(rooms);

        int totalCount = roomRepository.findMyNearbyRoomCount(latitude,longitude);
        if(((pageNumber -1) * pageSize + pageSize) >= totalCount){
            roomVo.setLastPage(true);
        }
        return roomVo;
    }

    public Room createNewRoom(Room room){
        room.setCreatorOpenid(room.getOpenId());
        room.setCreateTime(new Date());
        room.setLasteditTime(new Date());
        room.setStatus(1);//设置状态有效

        //计算房间的覆盖范围
        double[] doubles = LocationUtils.getAround(room.getLatitude(),room.getLongitude(),raidus);

        room.setMinLatitude(doubles[0]);
        room.setMinLongitude(doubles[1]);
        room.setMaxLatitude(doubles[2]);
        room.setMaxLongitude(doubles[3]);

        return roomRepository.save(room);
    }

    public void joinRoom(long roomId,String openId){
        RoomRelationship roomRelationship = new RoomRelationship();
        roomRelationship.setRoomId(roomId);
        roomRelationship.setOpenId(openId);
        roomRelationship.setCreateTime(new Date());

        roomRelationshipRepository.save(roomRelationship);
    }

    /**
     * 加入房间
     * @param session
     * @param roomId
     */
    public void addRoomSessionMap(Session session,Long roomId) {
        CopyOnWriteArraySet<Session> sessionArray = allRoomSessionMap.get(roomId);     //加入set中
        if(sessionArray == null){
            sessionArray = new CopyOnWriteArraySet<>();
        }
        sessionArray.add(session);
        allRoomSessionMap.put(roomId,sessionArray);

        sendPeopleCountMsg(roomId);

//        setSessionRoomToRedis(session,roomId);
    }

    //获得房间人数，并发送信息到客户端更新标题
    private void sendPeopleCountMsg(Long roomId){
        UserVo userVo = new UserVo();
        userVo.setPeopleCount(String.valueOf(getRoomPersonCount(roomId)));
        String data = JSONObject.fromObject(userVo).toString();

        sendMsg(null,data,"",roomId);
    }

    /**
     * 离开房间
     * @param session
     * @param roomId
     */
    @Transactional
    public void removeRoomSessionMap(Session session,Long roomId) {
        CopyOnWriteArraySet<Session> sessionArray = allRoomSessionMap.get(roomId);     //加入set中
        if(sessionArray != null){
            sessionArray.remove(session);
            allRoomSessionMap.put(roomId,sessionArray);
        }


        //房间人数为0时，设置房间无效
        if(getRoomPersonCount(roomId) == 0){
            Room room = roomRepository.getOne(roomId);
            room.setStatus(2);//设置无效
            room.setLasteditTime(new Date());
            roomRepository.save(room);
        }else{
            sendPeopleCountMsg(roomId);
        }
    }

    /**
     * 获得房间人数
     * @param roomId
     * @return
     */
    public int getRoomPersonCount(Long roomId){
        CopyOnWriteArraySet<Session> sessionArray = allRoomSessionMap.get(roomId);     //加入set中
        if(sessionArray == null){
            return 0;
        }else {
            return sessionArray.size();
        }
    }

    /**
     * 讲session与room的对照关系写到redis中
     * @param session
     * @param roomId
     */
    private void setSessionRoomToRedis(Session session,Long roomId) {
        redisTemplate.opsForValue().set(session, roomId);
    }

    /**
     * 删除对照关系
     * @param session
     * @param roomId
     */
    private void removeSessionRoomToRedis(Session session,Long roomId) {
        redisTemplate.delete(session);
    }

    private UserVo convertStringToUser(String data){
        UserVo userVo = new UserVo();
        if(!StringUtils.isEmpty(data)){
            JSONObject jsonObject = JSONObject.fromObject(data);
            userVo.setOpenId(String.valueOf(jsonObject.get("openId")));
            userVo.setNickName(String.valueOf(jsonObject.get("nickName")));
            userVo.setAvatarUrl(String.valueOf(jsonObject.get("avatarUrl")));
            userVo.setLatitude(Double.parseDouble(String.valueOf(jsonObject.get("latitude"))));
            userVo.setLongitude(Double.parseDouble(String.valueOf(jsonObject.get("longitude"))));
            String msg = String.valueOf(jsonObject.get("msg"));
            if(!StringUtils.isEmpty(msg)){
                userVo.setMsg(SensitiveWordUtil.replaceSensitiveWord(msg, '*'));
            }
            userVo.setPeopleCount(String.valueOf(jsonObject.get("peopleCount")));
        }
        return userVo;
    }

    @Override
    public UserVo getUserForbidden(String openId,String data) {
        UserVo userVo = convertStringToUser(data);
        if(!StringUtils.isEmpty(openId)){
            int count = userService.getVoteForbiddenCount(openId);
            logger.info("查询用户的被投票禁言数，openId:{},数量为：{}",openId,count);

            userVo.setForbiddenCount(count);
            userVo.setForbiddenPercent(((maxForbiddenVoteCount - count)/maxForbiddenVoteCount) * 100);
            if(userVo.getForbiddenPercent() >= 90){
                userVo.setForbiddenStatus("success");
            }else if(userVo.getForbiddenPercent() < 90 && userVo.getForbiddenPercent() >= 30){
                userVo.setForbiddenStatus("normal");
            }else if(userVo.getForbiddenPercent() < 30){
                userVo.setForbiddenStatus("wrong");
            }
            if(count > maxForbiddenVoteCount){
                userVo.setForbidden(true);
                userVo.setMsg("你已经被投票禁言，" + maxForbiddenDay +"天后可以再次发言");
            }
        }
        return userVo;
    }

    /**
     * 发送消息
     * @param session
     * @param data
     */
    public void sendMsg(Session session, String data,String openId,Long roomId){
        // 处理敏感词，转码等
        //查询用户禁言程度
        UserVo userVo = getUserForbidden(openId,data);

        //查询用户所处的房间
//        Long roomId = (Long) redisTemplate.opsForValue().get(session);


        CopyOnWriteArraySet<Session> sessionArray = allRoomSessionMap.get(roomId);
        sessionArray.forEach(ses -> {
            ses.getAsyncRemote().sendText(JSONObject.fromObject(userVo).toString());
        });
    }


}
