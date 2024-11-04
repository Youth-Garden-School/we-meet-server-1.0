package com.videocall.server.service.implement;

import com.videocall.server.dto.request.UserCreationRequest;
import com.videocall.server.dto.response.IntrospectResponse;
import com.videocall.server.dto.response.UserResponse;
import com.videocall.server.entity.User;
import com.videocall.server.repository.UserRepository;
import com.videocall.server.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(UserCreationRequest request) {
        if(userRepository.existsByUserName(request.getUserName()))
            throw new RuntimeException("Username đã tồn tại");
        User user = userRepository.save(User.builder()
                .userName(request.getUserName())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build());

        return UserResponse.builder()
                .id(user.getId())
                .userName(request.getUserName())
                .fullName(request.getFullName())
                .build();
    }

    @Override
    public IntrospectResponse introspect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return IntrospectResponse.builder()
                    .isValid(false)
                    .build();
        }

        String username = authentication.getName();

        return IntrospectResponse.builder()
                .isValid(true)
                .userName(username)
                .build();
    }

}
