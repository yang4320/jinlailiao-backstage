package com.jinlailiao.controller;

import com.jinlailiao.bean.Room;
import com.jinlailiao.bean.RoomVo;
import com.jinlailiao.dao.RoomRepository;
import com.jinlailiao.service.RoomService;
import com.jinlailiao.utils.SensitiveWordUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Yang Longxiang on 2018/7/17 017.
 */
@RestController
@RequestMapping("/jinlailiao/room")
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    @GetMapping("/{longitude}/{latitude}/{pageNumber}")
    public RoomVo index(@PathVariable String longitude, @PathVariable String latitude, @PathVariable int pageNumber) {
        try {
            if(!StringUtils.isEmpty(longitude) && !StringUtils.isEmpty(latitude)){
                return roomService.findMyNearbyRoom(Double.parseDouble(latitude),Double.parseDouble(longitude),pageNumber);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 返回创建room的ID，用户打开socket
     * @param room
     * @return
     */
    @PostMapping("/create")
    public Room creatNewRoom(@RequestBody Room room) throws Exception {

        logger.info("创建主题，主题名{}，openid:{},latitude{},longtitude{}",room.getRoomName(),room.getOpenId(),room.getLatitude(),room.getLongitude());
        // 敏感词、特殊字符过滤
        if(StringUtils.isEmpty(room.getRoomName())){
            throw new Exception("主题名不能为空");
        }else if(StringUtils.isEmpty(room.getRoomName().trim())){
            throw new Exception("主题名不能为空");
        }else if(SensitiveWordUtil.contains(room.getRoomName())){
            throw new Exception("主题名包含敏感词，不能创建");
        }else {
            return roomService.createNewRoom(room);
        }
    }

    @PostMapping("/join")
    public String joinRoom(@RequestBody Room room) {
        JSONObject object = new JSONObject();
        String openId = room.getOpenId();
        long roomId = room.getId();

        roomService.createNewRoom(room);
        return object.toString();
    }

}
