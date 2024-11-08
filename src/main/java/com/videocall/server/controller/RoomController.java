package com.videocall.server.controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videocall.server.dto.ApiResponse;
import com.videocall.server.dto.message.WebRTCMessage;
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

    @MessageMapping("/join")
    public void joinRoom(WebRTCMessage message) {
        String roomId = message.getRoomId();
        String userId = message.getFrom();

        rooms.computeIfAbsent(roomId, k -> new HashSet<>()).add(userId);
        log.info("Rooms: {}", rooms.toString());

        // Gửi thông báo cho tất cả người tham gia
        rooms.get(roomId).forEach(user -> {
            messagingTemplate.convertAndSendToUser(user, "/topic/room", "User " + userId + " joined the room");
        });
    }

    @MessageMapping("/webrtc")
    public void handleWebRTCSignal(WebRTCMessage message) {
        String roomId = message.getRoomId();
        String signal;

        log.info("Rooms: {}", rooms.toString());

        // Xác định loại tín hiệu (offer, answer, candidate)
        if (message.getOffer() != null) {
            signal = "Offer: " + message.getOffer();
        } else if (message.getAnswer() != null) {
            signal = "Answer: " + message.getAnswer();
        } else if (message.getCandidate() != null) {
            signal = "Candidate: " + message.getCandidate();
        } else {
            signal = "";
        }

        log.info("Room: {}, Signal: {}", roomId, signal);

        // Gửi tín hiệu cho tất cả người tham gia phòng
        rooms.get(roomId).forEach(user -> {
            messagingTemplate.convertAndSendToUser(user, "/topic/webrtc", signal);
        });
    }
}
