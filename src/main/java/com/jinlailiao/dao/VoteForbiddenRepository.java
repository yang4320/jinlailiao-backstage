package com.jinlailiao.dao;

import com.jinlailiao.bean.VoteForbidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteForbiddenRepository extends JpaRepository<VoteForbidden, Long> {

    @Query("from VoteForbidden where openId = ?1")
    VoteForbidden getVoteForbiddenCount(String openId);

    //查询达到禁言条件的人员列表
    @Query(nativeQuery = true,value = "select * from vote_forbidden where vote_count >= ?1 and TIMESTAMPDIFF(DAY, update_time, NOW()) >= ?2")
    List<VoteForbidden> getVoteForbiddenToReduce(@Param("maxForbiddenVoteCount") int maxForbiddenVoteCount,@Param("maxForbiddenDay") int maxForbiddenDay);

}