package com.videocall.server.service;

import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.IntrospectResponse;
import com.videocall.server.dto.response.UserResponse;

public interface UserService {
    UserResponse register(UserCreationRequest request);

    IntrospectResponse introspect();
}
