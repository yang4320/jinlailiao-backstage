package com.jinlailiao.dao;

import com.jinlailiao.bean.VoteForbiddenDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface VoteForbiddenDetailRepository extends JpaRepository<VoteForbiddenDetail, Long> {

    @Transactional
    @Modifying
    @Query("update VoteForbiddenDetail set status = 2 where openId = ?1")
    void updateDetailStatusTo2(@Param("openId") String openId);
}