package com.jinlailiao.dao;

import com.jinlailiao.bean.RoomRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRelationshipRepository extends JpaRepository<RoomRelationship, Long> {

//    //查询我附近的房间
//    @Query("from Room r where r.maxLatitude >= ?1 and r.maxLongitude >= ?2 and r.minLatitude <= ?1 and r.minLongitude <= ?2")
//    List<Room> findMyNearbyRoom(Double latitude, Double longitude);



}