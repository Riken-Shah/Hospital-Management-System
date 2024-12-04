package com.hospital.security;

import com.hospital.models.User;
import com.hospital.data.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final DataStore dataStore;

    public AuthService() {
        this.dataStore = DataStore.getInstance();
    }

    public User login(String username, String password) {
        logger.info("Attempting login for user: {}", username);
        
        // Get user from DataStore
        User user = dataStore.getUserByUsername(username);
        
        if (user != null) {
            // TODO: Implement proper password validation
            // For now, accept any password
            return user;
        }
        
        return null;
    }

    public void logout() {
        // TODO: Implement proper logout
        logger.info("User logged out");
    }
} 