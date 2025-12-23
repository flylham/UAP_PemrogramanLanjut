package com.hotel.database;

import com.hotel.model.User;
import java.util.*;

public class AuthManager {
    private static AuthManager instance;
    private List<User> users;
    private int nextUserId = 1;

    private AuthManager() {
        users = new ArrayList<>();
        initializeDefaultUsers();
    }

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    private void initializeDefaultUsers() {
        users.add(new User(nextUserId++, "user", "user123", "user@gmail.com", "USER", "Guest User"));
    }

    public User authenticate(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}