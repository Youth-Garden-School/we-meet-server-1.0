package com.videocall.server.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.security.SecureRandom;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Room")
public class Room {
    @Id
    @Column(name = "Id")
    String id;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "id")
    @JsonIgnore
    User user;

    @PrePersist
    public void generateId() {
        if (id == null) {
            id = generateRandomId();
        }
    }

    // Hàm tạo id ngẫu nhiên dạng chữ cái, không có số
    private String generateRandomId() {
        SecureRandom random = new SecureRandom();
        StringBuilder idBuilder = new StringBuilder();
        String alphabet = "abcdefghijklmnopqrstuvwxyz"; // Chỉ dùng chữ cái

        // Tạo ba phần id, mỗi phần có độ dài từ 3 đến 4 ký tự
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) { // Mỗi phần 4 ký tự
                int index = random.nextInt(alphabet.length());
                idBuilder.append(alphabet.charAt(index));
            }
            if (i < 2) {
                idBuilder.append("-"); // Thêm dấu gạch ngang giữa các phần
            }
        }
        return idBuilder.toString();
    }
}
