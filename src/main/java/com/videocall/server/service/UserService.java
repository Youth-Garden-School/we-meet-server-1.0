package com.videocall.server.service;

import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.AuthenticationResponse;

public interface UserService {
    AuthenticationResponse register(UserCreationRequest request);
}
