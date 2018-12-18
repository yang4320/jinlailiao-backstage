package com.jinlailiao.bean;

import lombok.Data;

@Data
public class UserVo {
    private String openId;
    private String nickName;
    private String avatarUrl;
    private String msg;
    private int forbiddenCount;
    //是否禁言
    private boolean isForbidden;

    //禁言进度条百分比
    private float forbiddenPercent;
    private String forbiddenStatus;
    //房间人数
    private String peopleCount;
    private double latitude;
    private double longitude;

}
