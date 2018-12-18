package com.jinlailiao.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "vote_forbidden_detail")
@Data
public class VoteForbiddenDetail implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,name = "openid",length = 30)
    private String openId;
    @Column(nullable = false,name = "vote_openid")
    private String voteOpenid;
    @Column(nullable = false,name = "create_time")
    private Date createTime;
    @Column(nullable = false,name = "status")
    private int status;

}
