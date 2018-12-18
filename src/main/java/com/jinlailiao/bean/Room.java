package com.jinlailiao.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "room")
@Data
public class Room implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,name = "room_name",length = 30)
    private String roomName;

    @Column(nullable = false,name = "min_longitude")
    private Double minLongitude;

    @Column(nullable = false,name = "max_longitude")
    private Double maxLongitude;

    @Column(nullable = false,name = "min_latitude")
    private Double minLatitude;

    @Column(nullable = false,name = "max_latitude")
    private Double maxLatitude;

    @Column(nullable = false,name = "creator_openid",length = 30)
    private String creatorOpenid;

    @Column(nullable = false,name = "create_time")
    private Date createTime;

    @Column(nullable = false,name = "lastedit_time")
    private Date lasteditTime;

    @Column(nullable = false,name = "status",length = 1)
    private int status;

    @Transient
    private Double longitude;

    @Transient
    private Double latitude;

    @Transient
    private String openId;
    //房间人数
    @Transient
    private int peopleCount;

}
