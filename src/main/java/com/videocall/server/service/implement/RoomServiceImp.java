package com.videocall.server.service.implement;

import com.videocall.server.exception.AppException;
import com.videocall.server.exception.ErrorCode;
import org.springframework.stereotype.Service;

import com.videocall.server.dto.response.RoomResponse;
import com.videocall.server.entity.Room;
import com.videocall.server.entity.User;
import com.videocall.server.repository.RoomRepository;
import com.videocall.server.repository.UserRepository;
import com.videocall.server.service.RoomService;
import com.videocall.server.utils.UserUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomServiceImp implements RoomService {
    UserRepository userRepository;
    RoomRepository roomRepository;

    @Override
    public RoomResponse create() {
        String currentUserName = UserUtils.getCurrentUserName();
        User user = userRepository.findById(currentUserName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Room room = roomRepository.save(Room.builder().user(user).build());
        return RoomResponse.builder().id(room.getId()).build();
    }

    @Override
    public Boolean exist(String roomId) {
        return roomRepository.existsById(roomId);
    }


}
