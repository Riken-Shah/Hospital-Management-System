package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class CommunityManagerDashboard extends BaseDashboard {
    private static final Logger logger = LoggerFactory.getLogger(CommunityManagerDashboard.class);
    private JPanel menuPanel;

    public CommunityManagerDashboard(User currentUser) {
        super(currentUser);
        setTitle("Community Manager Dashboard - " + currentUser.getName());
        initializeMenu();
        setVisible(true);
    }

    @Override
    protected void initializeMenu() {
        menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton eventsButton = createMenuButton("Events");
        JButton outreachButton = createMenuButton("Outreach Programs");
        JButton feedbackButton = createMenuButton("Community Feedback");
        JButton reportsButton = createMenuButton("Reports");
        JButton profileButton = createMenuButton("Profile");
        JButton logoutButton = createMenuButton("Logout");

        eventsButton.addActionListener(e -> showPanel(createEventsPanel()));
        outreachButton.addActionListener(e -> showPanel(createOutreachPanel()));
        feedbackButton.addActionListener(e -> showPanel(createFeedbackPanel()));
        reportsButton.addActionListener(e -> showPanel(createReportsPanel()));
        profileButton.addActionListener(e -> showPanel(createProfilePanel()));
        logoutButton.addActionListener(e -> logout());

        menuPanel.add(eventsButton);
        menuPanel.add(outreachButton);
        menuPanel.add(feedbackButton);
        menuPanel.add(reportsButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        // Show events panel by default
        showPanel(createEventsPanel());
    }

    protected JPanel createEventsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement events panel
        return panel;
    }

    protected JPanel createOutreachPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement outreach programs panel
        return panel;
    }

    protected JPanel createFeedbackPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement community feedback panel
        return panel;
    }

    protected JPanel createReportsPanel() {
        JPanel panel = createFormPanel();
        // TODO: Implement reports panel
        return panel;
    }

    protected JPanel createProfilePanel() {
        JPanel panel = createFormPanel();
        GridBagConstraints gbc = createGridBagConstraints();

        addProfileField(panel, "Name", currentUser.getName(), gbc);
        addProfileField(panel, "Role", currentUser.getRole(), gbc);
        addProfileField(panel, "Hospital ID", String.valueOf(currentUser.getHospitalId()), gbc);
        addProfileField(panel, "Email", currentUser.getEmail(), gbc);

        return panel;
    }
} 