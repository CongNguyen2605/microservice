package org.example.gatewayservice.dto;


public class UserContext {
    private static Long userId;
    private static String name;

    public static Long getUserId() {
        return userId;
    }

    public static String getName() {
        return name;
    }

    public static void setUserInfo(Long userId, String name) {
        UserContext.userId = userId;
        UserContext.name = name;
    }

    public static void clear() {
        userId = null;
        name = null;
    }
}
