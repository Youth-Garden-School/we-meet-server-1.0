package com.videocall.server.controller;

import com.videocall.server.dto.ApiResponse;
import com.videocall.server.dto.request.RoomRequest;
import com.videocall.server.dto.response.RoomResponse;
import com.videocall.server.service.RoomService;
import com.videocall.server.utils.UserUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoomController {
    Map<String, List<String>> rooms = new ConcurrentHashMap<>();
    SimpMessagingTemplate messagingTemplate;
    RoomService roomService;

    @PostMapping()
    ApiResponse<RoomResponse> create() {
        return ApiResponse.<RoomResponse>builder()
                .message("Tạo phòng mới")
                .result(roomService.create())
                .build();
    }

    @MessageMapping("/rooms/join")
    @SendTo("/topic/rooms")
    public void joinRoom(RoomRequest request) {
        String roomId = request.getRoomId();
        String userName = UserUtils.getCurrentUserName();

        rooms.computeIfAbsent(roomId, k -> new ArrayList<>()).add(userName);
        log.info("Rooms: {}", rooms);
    }
}
