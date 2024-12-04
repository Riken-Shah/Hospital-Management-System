package com.hospital.views;

import com.hospital.data.DataStore;
import com.hospital.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieticianDashboardTest {
    private DataStore dataStore;
    private User testUser;
    private DieticianDashboard dashboard;

    @BeforeEach
    void setUp() {
        dataStore = DataStore.getInstance();
        dataStore.clear();

        // Create test user with DIETICIAN role
        testUser = new User(1, "Test Dietician", "testdiet", "DIETICIAN", 1);
        dataStore.addUser(testUser);

        dashboard = new DieticianDashboard(testUser);
    }

    @Test
    void testDashboardInitialization() {
        assertNotNull(dashboard);
        assertEquals("Dietician Dashboard - " + testUser.getName(), dashboard.getTitle());
    }

    @Test
    void testCleanup() {
        dataStore.clear();
    }
} 