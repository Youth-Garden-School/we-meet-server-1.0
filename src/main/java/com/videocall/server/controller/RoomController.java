package com.videocall.server.controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.videocall.server.exception.AppException;
import com.videocall.server.exception.ErrorCode;
import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.videocall.server.dto.ApiResponse;
import com.videocall.server.dto.response.RoomResponse;
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

        Boolean isExist = roomService.exist(roomId);
        if(!isExist) throw new AppException(ErrorCode.ROOM_NOT_EXISTED);

        rooms.computeIfAbsent(roomId, s -> new HashSet<>()).add(userId);
        Set<String> roomUsers = rooms.get(roomId);

        log.info("Users in room {}: {}", roomId, roomUsers);

        for (String existingUserId : roomUsers) {
            if (!existingUserId.equals(userId)) {
                simpMessagingTemplate.convertAndSendToUser(existingUserId, "/topic/call", userId);
                log.info("Call sent to user: {}", existingUserId);
            }
        }
    }

    @MessageMapping("/call")
    public void call(String call) {
        JSONObject jsonObject = new JSONObject(call);
        log.info("Calling to: {} | Call from: {}", jsonObject.get("callTo"), jsonObject.get("callFrom"));
        log.info("Calling to class: {} | Call from class: {}", jsonObject.get("callTo").getClass(), jsonObject.get("callFrom").getClass());
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("callTo"), "/topic/call", jsonObject.get("callFrom"));
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


}
