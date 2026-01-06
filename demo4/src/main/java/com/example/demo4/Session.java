package com.example.demo4;

public class Session {

    
    private static Integer currentUserId;
    private static String currentUsername;
    private static String currentRole;

    

    public static void login(Integer userId, String username, String role) {
        currentUserId   = userId;
        currentUsername = username;
        currentRole     = role;
    }

    public static void logout() {
        currentUserId   = null;
        currentUsername = null;
        currentRole     = null;
    }

    

    public static Integer getCurrentUserId() {
        return currentUserId;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    

    public static boolean isLoggedIn() {
        return currentUsername != null;
    }

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(currentRole);
    }

    public static boolean hasRole(String role) {
        return currentRole != null && currentRole.equalsIgnoreCase(role);
    }
}

