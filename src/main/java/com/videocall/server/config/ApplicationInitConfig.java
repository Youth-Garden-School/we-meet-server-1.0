package com.videocall.server.config;

import com.videocall.server.entity.User;
import com.videocall.server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {


    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        log.info("Init application");

        return args -> {
            if (!userRepository.existsByUserName("kaitou")) {

                User user = User.builder()
                        .userName("kaitou")
                        .fullName("Trần Hoàng Qun")
                        .password(passwordEncoder.encode("kaitou"))
                        .build();

                userRepository.save(user);
                log.info("init completed");
            }
        };
    }
}
