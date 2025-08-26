package org.example.productservice.dto;


import lombok.Getter;

public class UserContext {
    @Getter
    private static Long userId;
    @Getter
    private static String name;

    public static void setUserInfo(Long userId, String name) {
        UserContext.userId = userId;
        UserContext.name = name;
    }

    public static void clear() {
        userId = null;
        name = null;
    }
}
