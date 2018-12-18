package com.jinlailiao.dao;

import com.jinlailiao.bean.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


    //查询我附近的房间
    @Query(nativeQuery = true,value = "select * from room where status = 1 and max_latitude >= ?1 and max_longitude >= ?2 and min_latitude <= ?1 and min_longitude <= ?2 order by create_time desc limit ?3,?4")
    List<Room> findMyNearbyRoom(Double latitude, Double longitude,int recordNum,int pageSize);

    @Query(nativeQuery = true,value = "SELECT count(id) FROM room r WHERE status = 1 and r.max_latitude >= ?1 and r.max_longitude >= ?2 and r.min_latitude <= ?1 and r.min_longitude <= ?2")
    int findMyNearbyRoomCount(Double latitude, Double longitude);


}