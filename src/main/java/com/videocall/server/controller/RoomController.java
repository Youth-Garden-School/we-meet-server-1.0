package com.videocall.server.controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videocall.server.dto.ApiResponse;
import com.videocall.server.dto.response.RoomResponse;
import com.videocall.server.service.RoomService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoomController {
    Map<String, Set<String>> rooms = new ConcurrentHashMap<>();
    SimpMessagingTemplate messagingTemplate;
    RoomService roomService;

    @PostMapping()
    ApiResponse<RoomResponse> create() {
        return ApiResponse.<RoomResponse>builder()
                .message("Tạo phòng mới")
                .result(roomService.create())
                .build();
    }
}
