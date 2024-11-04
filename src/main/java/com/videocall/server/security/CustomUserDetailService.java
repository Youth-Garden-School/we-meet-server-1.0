package com.videocall.server.security;

import com.videocall.server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class CustomUserDetailService implements UserDetailsService {
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUserName(username)
                .map(CustomUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
