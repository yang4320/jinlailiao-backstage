package com.jinlailiao.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class RoomRelationship implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,name = "room_id")
    private Long roomId;

    @Column(nullable = false,name = "openId")
    private String openId;

    @Column(nullable = false,name = "create_time")
    private Date createTime;

}
