package com.jinlailiao.controller;

import com.jinlailiao.bean.Room;
import com.jinlailiao.bean.UserVo;
import com.jinlailiao.service.RoomService;
import com.jinlailiao.service.UserService;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Yang Longxiang on 2018/7/17 017.
 */
@RestController
@RequestMapping("/jinlailiao/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RoomService roomService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 通过 ServletRequest 和 ServletResponse 初始化登录服务
        LoginService service = new LoginService(request, response);
        try {
            // 调用登录接口，如果登录成功可以获得登录信息
            JSONObject session = service.login();
            System.out.println("========= LoginSuccess, UserInfo: ==========");
            System.out.println(session);
            JSONObject json = new JSONObject();
            json.put("code",0);
            json.put("data",session);

            return json.toString();
        } catch (LoginServiceException e) {
            // 登录失败会抛出登录失败异常
            e.printStackTrace();
        } catch (ConfigurationException e) {
            // SDK 如果还没有配置会抛出配置异常
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/check")
    public void check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginService service = new LoginService(request, response);
        try {
            // 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
            UserInfo userInfo = service.check();

            // 获取会话成功，输出获得的用户信息
            JSONObject result = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("userInfo", new JSONObject(userInfo));
            result.put("code", 0);
            result.put("message", "OK");
            result.put("data", data);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(result.toString());

        } catch (LoginServiceException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户投票发言人禁言
     * @param openId
     * @return
     */
    @PostMapping("/voteUseForbidden")
    public boolean voteUseForbidden(@RequestParam String openId,@RequestParam String voteOpenId) throws Exception{
        if(StringUtils.isEmpty(openId) || StringUtils.isEmpty(voteOpenId)){
            throw new Exception("参数名不能为空");
        }
        //查询是否已经是达到了次数了
        if(userService.haveBeenForbidden(voteOpenId)){
            throw new Exception("TA已经被大家投票禁言了");
        }

        return userService.voteUseForbidden(openId,voteOpenId);
    }

    @GetMapping("/getUserForbidden")
    public UserVo getUserForbidden(@RequestParam String openId){
        return roomService.getUserForbidden(openId,null);
    }
}
