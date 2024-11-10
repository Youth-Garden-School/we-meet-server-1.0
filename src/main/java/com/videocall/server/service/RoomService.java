package com.videocall.server.service;

import com.videocall.server.dto.response.RoomResponse;

public interface RoomService {
    RoomResponse create();

    Boolean exist(String roomId);
}
