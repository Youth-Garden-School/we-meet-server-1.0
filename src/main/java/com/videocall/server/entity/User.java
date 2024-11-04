package com.videocall.server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true)
    String userName;

    String password;

    String fullName;

    @OneToMany(mappedBy = "user")
    Set<Room> rooms;
}
