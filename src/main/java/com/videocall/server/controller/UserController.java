package com.videocall.server.controller;

import org.springframework.web.bind.annotation.*;

import com.videocall.server.dto.ApiResponse;
import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.AuthenticationResponse;
import com.videocall.server.dto.response.UserResponse;
import com.videocall.server.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<AuthenticationResponse> register(@RequestBody UserCreationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Đăng ký")
                .result(userService.register(request))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> myInfo() {
        return ApiResponse.<UserResponse>builder()
                .message("Lấy thông tin người dùng")
                .result(userService.myInfo())
                .build();
    }
}
