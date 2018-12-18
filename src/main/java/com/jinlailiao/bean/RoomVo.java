package com.jinlailiao.bean;

import lombok.Data;

import java.util.List;

@Data
public class RoomVo {
    private List<Room> roomList;
    private boolean isLastPage;
//    private int pageNumber;
}
