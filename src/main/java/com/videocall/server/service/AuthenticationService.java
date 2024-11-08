package com.videocall.server.service;

import com.videocall.server.dto.request.AuthenticationRequest;
import com.videocall.server.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
}
