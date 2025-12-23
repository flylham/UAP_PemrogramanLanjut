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
        // User default
        users.add(new User(nextUserId++, "admin", "admin123", "admin@baobab.com", "ADMIN", "Administrator"));
        users.add(new User(nextUserId++, "user", "user123", "user@baobab.com", "USER", "Guest User"));
        users.add(new User(nextUserId++, "guest", "guest123", "guest@baobab.com", "USER", "Tam Safari"));
    }

    public User authenticate(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public boolean registerUser(String username, String password, String email, String fullName) {
        // Cek username sudah ada
        boolean usernameExists = users.stream()
                .anyMatch(user -> user.getUsername().equals(username));

        if (usernameExists) {
            return false;
        }

        User newUser = new User(nextUserId++, username, password, email, "USER", fullName);
        users.add(newUser);
        return true;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}