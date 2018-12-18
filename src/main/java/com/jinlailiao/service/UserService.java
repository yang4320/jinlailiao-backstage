package com.jinlailiao.service;

import com.jinlailiao.bean.VoteForbidden;
import com.jinlailiao.bean.VoteForbiddenDetail;

import java.util.List;

public interface UserService {
    boolean voteUseForbidden(String openId,String voteOpenId);

    int getVoteForbiddenCount(String openId);

    VoteForbiddenDetail saveVoteForbiddenDetail(String openId,String voteOpenid);

    VoteForbidden addUpdateVoteForbidden(String openId);

    //查询达到禁言条件的人员列表
    List<VoteForbidden> getVoteForbiddenToReduce();
    /**
     * 将禁言数清零操作，到期自动清零
     * @param openId
     */
    void reduceTo0VoteForbidden(String openId);

    /**
     * 查询被投票的用户是否已经达到禁言次数，被禁言
     * @param voteOpenId
     * @return
     */
    boolean haveBeenForbidden(String voteOpenId);
}
