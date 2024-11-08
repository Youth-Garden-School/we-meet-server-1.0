package com.videocall.server.service.implement;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.videocall.server.config.JwtTokenProvider;
import com.videocall.server.dto.request.AuthenticationRequest;
import com.videocall.server.dto.response.AuthenticationResponse;
import com.videocall.server.entity.User;
import com.videocall.server.exception.AppException;
import com.videocall.server.exception.ErrorCode;
import com.videocall.server.repository.UserRepository;
import com.videocall.server.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImp implements AuthenticationService {
    UserRepository userRepository;
    JwtTokenProvider jwtTokenProvider;
    PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        String userName = request.getUserName();
        String password = request.getPassword();
        User user =
                userRepository.findByUserName(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_IS_NOT_CORRECT);

        return AuthenticationResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(user))
                .build();
    }
}
