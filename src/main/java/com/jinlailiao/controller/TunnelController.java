package com.jinlailiao.controller;

import com.qcloud.weapp.tunnel.TunnelHandleOptions;
import com.qcloud.weapp.tunnel.TunnelService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Yang Longxiang on 2018/7/17 017.
 */
@RestController
@RequestMapping("/jinlailiao/tunnel")
public class TunnelController {

    private static final Logger logger = LoggerFactory.getLogger(TunnelController.class);

    @RequestMapping("")
    public String service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("tunnelController 调用");

        // 创建信道服务处理信道相关请求
        TunnelService tunnelService = new TunnelService(request, response);

        try {
            // 配置是可选的，配置 CheckLogin 为 true 的话，会在隧道建立之前获取用户信息，以便业务将隧道和用户关联起来
            TunnelHandleOptions options = new TunnelHandleOptions();
            options.setCheckLogin(false);

            // 需要实现信道处理器，ChatTunnelHandler 是一个实现的范例
            JSONObject result = tunnelService.handle(new ChatTunnelHandler(), options);
            logger.info("result =============" + result);
            JSONObject json = new JSONObject();
            json.put("code", 0);
            json.put("data", result);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
