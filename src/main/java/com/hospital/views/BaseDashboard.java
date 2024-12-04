package com.hospital.views;

import com.hospital.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDashboard extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(BaseDashboard.class);
    protected User currentUser;
    protected JPanel contentPanel;
    protected boolean isHeadless = GraphicsEnvironment.isHeadless();

    public BaseDashboard(User currentUser) {
        this.currentUser = currentUser;
        if (!isHeadless) {
            setupFrame();
        }
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create content panel
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);
    }

    protected abstract void initializeMenu();

    protected void showPanel(JPanel panel) {
        if (contentPanel != null) {
            contentPanel.removeAll();
            contentPanel.add(panel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    protected JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }

    protected JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    protected GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    protected void addProfileField(JPanel panel, String label, String value, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value != null ? value : "N/A"), gbc);
        gbc.gridy++;
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void logout() {
        logger.info("User {} logged out", currentUser.getUsername());
        dispose();
        // TODO: Show login screen
    }
} 