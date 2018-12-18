package com.jinlailiao.service.impl;

import com.jinlailiao.bean.VoteForbidden;
import com.jinlailiao.bean.VoteForbiddenDetail;
import com.jinlailiao.dao.VoteForbiddenDetailRepository;
import com.jinlailiao.dao.VoteForbiddenRepository;
import com.jinlailiao.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl  implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private VoteForbiddenRepository voteForbiddenRepository;
    @Resource
    private VoteForbiddenDetailRepository voteForbiddenDetailRepository;
    @Value(("${max.forbidden.votecount}"))
    private float maxForbiddenVoteCount;
    @Value(("${max.forbidden.day}"))
    private int maxForbiddenDay;

    @Override
    @Transactional
    public boolean voteUseForbidden(String openId,String voteOpenId) {
        boolean flag = false;

        logger.info("用户：{}投票禁言用户：{}",voteOpenId,openId);
        if(StringUtils.isEmpty(openId) || StringUtils.isEmpty(voteOpenId)){
            return false;
        }
        try {
            addUpdateVoteForbidden(openId);
            saveVoteForbiddenDetail(openId,voteOpenId);

            flag = true;
        }catch (Exception e){
            flag = false;
            logger.error("投票禁言用户失败");
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean haveBeenForbidden(String voteOpenId) {
        int count = getVoteForbiddenCount(voteOpenId);
        if(maxForbiddenVoteCount == count){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int getVoteForbiddenCount(String openId) {
        logger.info("查询用户的被投票禁言数，openId:{}",openId);

        VoteForbidden voteForbidden = voteForbiddenRepository.getVoteForbiddenCount(openId);
        return voteForbidden == null ? 0 : voteForbidden.getVoteCount();
    }

    @Override
    public VoteForbiddenDetail saveVoteForbiddenDetail(String openId,String voteOpenid) {
        VoteForbiddenDetail detail = new VoteForbiddenDetail();
        detail.setCreateTime(new Date());
        detail.setOpenId(openId);
        detail.setVoteOpenid(voteOpenid);
        detail.setStatus(1);
        return voteForbiddenDetailRepository.save(detail);
    }

    @Override
    public VoteForbidden addUpdateVoteForbidden(String openId) {
        VoteForbidden voteForbidden = voteForbiddenRepository.getVoteForbiddenCount(openId);
        int oldCount = voteForbidden == null ? 0 : voteForbidden.getVoteCount();
        voteForbidden.setVoteCount(oldCount + 1);
        voteForbidden.setUpdateTime(new Date());
        return voteForbiddenRepository.save(voteForbidden);
    }

    @Override
    public List<VoteForbidden> getVoteForbiddenToReduce() {
        return voteForbiddenRepository.getVoteForbiddenToReduce((int) maxForbiddenVoteCount,maxForbiddenDay);
    }

    @Transactional
    @Override
    public void reduceTo0VoteForbidden(String openId) {
        //清零
        VoteForbidden voteForbidden = voteForbiddenRepository.getVoteForbiddenCount(openId);
        voteForbidden.setVoteCount(0);
        voteForbidden.setUpdateTime(new Date());
        voteForbiddenRepository.save(voteForbidden);

        //设置detail状态无效
        voteForbiddenDetailRepository.updateDetailStatusTo2(openId);
    }
}
