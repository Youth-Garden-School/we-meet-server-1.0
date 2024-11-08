package com.videocall.server.dto.message;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebRTCMessage {
    String to;
    String from;
    String offer;
    String answer;
    String candidate;
    String roomId;
}
