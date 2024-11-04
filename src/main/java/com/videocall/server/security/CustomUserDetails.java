package com.videocall.server.security;

import com.videocall.server.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomUserDetails implements UserDetails {
    String userName;
    String password;

    public CustomUserDetails(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
