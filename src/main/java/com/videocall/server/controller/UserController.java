package com.videocall.server.controller;

import com.videocall.server.dto.ApiResponse;
import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.IntrospectResponse;
import com.videocall.server.dto.response.UserResponse;
import com.videocall.server.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect() {
        return ApiResponse.<IntrospectResponse>builder()
                .message("F5")
                .result(userService.introspect())
                .build();
    }

    @PostMapping()
    ApiResponse<UserResponse> register(@RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("Đăng ký")
                .result(userService.register(request))
                .build();
    }
}
