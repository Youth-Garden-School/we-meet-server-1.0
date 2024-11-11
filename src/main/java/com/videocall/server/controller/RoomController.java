package com.videocall.server.controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.videocall.server.exception.SocketException;
import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.videocall.server.dto.ApiResponse;
import com.videocall.server.dto.response.RoomResponse;
import com.videocall.server.exception.AppException;
import com.videocall.server.exception.ErrorCode;
import com.videocall.server.service.RoomService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoomController {
    Map<String, Set<String>> rooms = new ConcurrentHashMap<>();
    SimpMessagingTemplate simpMessagingTemplate;
    RoomService roomService;

    @ResponseBody
    @PostMapping()
    ApiResponse<RoomResponse> create() {
        return ApiResponse.<RoomResponse>builder()
                .message("Tạo phòng mới")
                .result(roomService.create())
                .build();
    }

    @MessageMapping("/join")
    public void join(String join) {
        JSONObject jsonObject = new JSONObject(join);
        String roomId = jsonObject.getString("roomId");
        String userId = jsonObject.getString("userId");

        if (!roomService.exist(roomId)) throw new SocketException(ErrorCode.ROOM_NOT_EXISTED, userId);

        rooms.computeIfAbsent(roomId, s -> new HashSet<>()).add(userId);
        Set<String> roomUsers = rooms.get(roomId);

        log.info("Users in room {}: {}", roomId, roomUsers);

        for (String existingUserId : roomUsers) {
            if (!existingUserId.equals(userId)) {
                simpMessagingTemplate.convertAndSendToUser(existingUserId, "/topic/call", join);
                log.info("Call sent to user: {}", existingUserId);
            }
        }
    }

    @MessageMapping("/call")
    public void call(String call) {
        JSONObject jsonObject = new JSONObject(call);
        log.info("Calling to: {} | Call from: {}", jsonObject.get("callTo"), jsonObject.get("callFrom"));
        log.info(
                "Calling to class: {} | Call from class: {}",
                jsonObject.get("callTo").getClass(),
                jsonObject.get("callFrom").getClass());
        simpMessagingTemplate.convertAndSendToUser(
                jsonObject.getString("callTo"), "/topic/call", call);
    }

    @MessageMapping("/offer")
    public void offer(String offer) {
        log.info("Offer came");
        JSONObject jsonObject = new JSONObject(offer);
        log.info("Offer: {}", jsonObject.get("offer"));
        log.info("To User: {}", jsonObject.get("toUser"));
        log.info("From User: {}", jsonObject.get("fromUser"));
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("toUser"), "/topic/offer", offer);
        log.info("Offer sent");
    }

    @MessageMapping("/answer")
    public void answer(String answer) {
        log.info("Answer came");
        JSONObject jsonObject = new JSONObject(answer);
        log.info("To User: {}", jsonObject.get("toUser"));
        log.info("From User: {}", jsonObject.get("fromUser"));
        log.info("Answer: {}", jsonObject.get("answer"));
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("toUser"), "/topic/answer", answer);
        log.info("Answer sent");
    }

    @MessageMapping("/candidate")
    public void candidate(String candidate) {
        log.info("Candidate came");
        JSONObject jsonObject = new JSONObject(candidate);
        log.info("To User: {}", jsonObject.get("toUser"));
        log.info("From User: {}", jsonObject.get("fromUser"));
        log.info("Candidate: {}", jsonObject.get("candidate"));
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("toUser"), "/topic/candidate", candidate);
        log.info("Candidate sent");
    }

    @MessageMapping("/leave")
    public void leave(String leave) {
        JSONObject jsonObject = new JSONObject(leave);
        String roomId = jsonObject.getString("roomId");
        String userId = jsonObject.getString("userId");

        if (!roomService.exist(roomId)) throw new SocketException(ErrorCode.ROOM_NOT_EXISTED, userId);

        Set<String> roomUsers = rooms.get(roomId);
        if (roomUsers != null) {
            roomUsers.remove(userId);

            if (roomUsers.isEmpty()) {
                rooms.remove(roomId);
                log.info("Room {} has been removed because it's empty", roomId);
            } else {
                log.info("User {} left room {}. Remaining users: {}", userId, roomId, roomUsers);

                for (String existingUserId : roomUsers) {
                    simpMessagingTemplate.convertAndSendToUser(existingUserId, "/topic/leave", leave);
                    log.info("Leave notification sent to user: {}", existingUserId);
                }
            }
        } else {
            log.warn("Room {} ko tồn tại hoặc đã bị xóa", roomId);
        }
    }

    @MessageMapping("/videoToggle")
    public void handleVideoToggle(String message) {
        JSONObject jsonObject = new JSONObject(message);
        String userId = jsonObject.getString("userId");
        boolean isVideoMuted = jsonObject.getBoolean("isVideoMuted");
        String roomId = jsonObject.getString("roomId");

        log.info("Nhận videoToggle từ người dùng {} trong phòng {}: video {}",
                userId, roomId, isVideoMuted ? "tắt" : "bật");

        for (String participantId : getRoomParticipants(roomId)) {
            if (!participantId.equals(userId)) { // Không gửi cho người đã tắt/bật camera
                simpMessagingTemplate.convertAndSendToUser(
                        participantId,
                        "/topic/videoToggle",
                        message
                );
            }
        }
    }

    @MessageMapping("/audioToggle")
    public void handleAudioToggle(String message) {
        JSONObject jsonObject = new JSONObject(message);
        String userId = jsonObject.getString("userId");
        boolean isMuted = jsonObject.getBoolean("isMuted");
        String roomId = jsonObject.getString("roomId");

        log.info("Nhận audioToggle từ người dùng {} trong phòng {}: mic {}",
                userId, roomId, isMuted ? "tắt" : "bật");

        for (String participantId : getRoomParticipants(roomId)) {
            if (!participantId.equals(userId)) {
                simpMessagingTemplate.convertAndSendToUser(
                        participantId,
                        "/topic/audioToggle",
                        message
                );
                log.info("Đã gửi thông báo audioToggle đến người dùng {}", participantId);
            }
        }
    }


    private Set<String> getRoomParticipants(String roomId) {
        return rooms.getOrDefault(roomId, new HashSet<>());
    }

}
