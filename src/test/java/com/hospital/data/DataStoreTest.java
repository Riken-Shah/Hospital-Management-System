package com.hospital.data;

import com.hospital.models.DietPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class DataStoreTest {
    private DataStore dataStore;

    @BeforeEach
    void setUp() {
        dataStore = DataStore.getInstance();
        dataStore.clear();
    }

    @Test
    void testDietPlanOperations() {
        int id = dataStore.getNextId();
        int patientId = dataStore.getNextId();
        int dieticianId = dataStore.getNextId();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        String goal = "Weight loss";
        ArrayList<String> restrictions = new ArrayList<>(Arrays.asList("Dairy", "Gluten"));
        ArrayList<String> recommendations = new ArrayList<>(Arrays.asList("Eat more vegetables", "Exercise daily"));
        String notes = "Patient is motivated";
        String status = "ACTIVE";

        DietPlan plan = new DietPlan(id, patientId, dieticianId, startDate, endDate, goal,
                restrictions, recommendations, notes, status);

        // Test adding and retrieving diet plan
        dataStore.addDietPlan(plan);
        DietPlan retrieved = dataStore.getDietPlan(id);

        assertNotNull(retrieved);
        assertEquals(plan.getGoal(), retrieved.getGoal());
        assertEquals(plan.getStartDate(), retrieved.getStartDate());
        assertEquals(plan.getEndDate(), retrieved.getEndDate());
        assertEquals(plan.getStatus(), retrieved.getStatus());
    }
} 