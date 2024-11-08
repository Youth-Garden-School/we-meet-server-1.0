package com.videocall.server.mapper;

import org.mapstruct.Mapper;

import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.UserResponse;
import com.videocall.server.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}
