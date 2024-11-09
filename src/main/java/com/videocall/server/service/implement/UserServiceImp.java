package com.videocall.server.service.implement;

import org.springframework.stereotype.Service;

import com.videocall.server.config.JwtTokenProvider;
import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.AuthenticationResponse;
import com.videocall.server.dto.response.UserResponse;
import com.videocall.server.entity.User;
import com.videocall.server.exception.AppException;
import com.videocall.server.exception.ErrorCode;
import com.videocall.server.mapper.UserMapper;
import com.videocall.server.repository.UserRepository;
import com.videocall.server.service.UserService;
import com.videocall.server.utils.UserUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthenticationResponse register(UserCreationRequest request) {
        User user = userRepository.save(userMapper.toUser(request));
        return AuthenticationResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(user))
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Override
    public UserResponse myInfo() {
        String id = UserUtils.getCurrentUserName();
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
}
