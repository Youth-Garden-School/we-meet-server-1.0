package com.videocall.server.mapper;

import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.UserResponse;
import com.videocall.server.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
}
