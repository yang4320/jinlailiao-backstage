package com.jinlailiao.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "vote_forbidden")
@Data
public class VoteForbidden implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,name = "openid",length = 30)
    private String openId;
    @Column(nullable = false,name = "vote_count")
    private int voteCount;
    @Column(nullable = false,name = "update_time")
    private Date updateTime;

}
