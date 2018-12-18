package com.jinlailiao;

import com.jinlailiao.controller.RoomController;
import com.jinlailiao.dao.RoomRepository;
import com.jinlailiao.service.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JinlailiaoApplicationTests {

	@Resource
	private RoomRepository roomRepository;
	@Resource
	private RoomController roomController;
	@Resource
	private RoomService roomService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testMysql(){
		System.out.println(roomService.findMyNearbyRoom(null,null,1));
	}

}
