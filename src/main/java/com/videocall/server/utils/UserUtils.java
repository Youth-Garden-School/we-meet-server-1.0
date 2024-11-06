package com.videocall.server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public static String  getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) throw new RuntimeException("Bạn chưa đăng nhập");
        return authentication.getName();
    }
}
