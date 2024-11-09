package com.videocall.server.service;

import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.AuthenticationResponse;
import com.videocall.server.dto.response.UserResponse;

public interface UserService {
    AuthenticationResponse register(UserCreationRequest request);

    UserResponse myInfo();
}
