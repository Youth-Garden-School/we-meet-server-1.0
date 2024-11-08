package com.videocall.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "USER_NAME_IS_REQUIRED")
    String userName;

    @NotBlank(message = "PASSWORD_IS_REQUIRED")
    String password;


    String fullName;
}
