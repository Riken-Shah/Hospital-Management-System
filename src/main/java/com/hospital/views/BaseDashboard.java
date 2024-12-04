package com.hospital.views;

import com.hospital.models.User;
import com.hospital.security.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDashboard extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(BaseDashboard.class);
    protected final User currentUser;
    protected static boolean isHeadless = GraphicsEnvironment.isHeadless();
    protected static final AuthService authService = new AuthService();

    public BaseDashboard(User currentUser) {
        this.currentUser = currentUser;
        if (!isHeadless) {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1024, 768);
            setLocationRelativeTo(null);
        }
    }

    protected abstract void initializeMenu();

    protected JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    protected JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    protected GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    protected void addProfileField(JPanel panel, String label, String value, GridBagConstraints gbc) {
        gbc.gridx = 0;
        panel.add(new JLabel(label + ":"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
        
        gbc.gridy++;
    }

    protected void showPanel(JPanel panel) {
        // Remove existing center panel if any
        Component centerComponent = ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent != null) {
            remove(centerComponent);
        }
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    protected void showError(String message) {
        if (!isHeadless) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void showSuccess(String message) {
        if (!isHeadless) {
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    protected void showConfirmation(String message, Runnable onConfirm) {
        if (!isHeadless) {
            int result = JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                onConfirm.run();
            }
        }
    }

    protected void logout() {
        dispose();
        if (!isHeadless) {
            SwingUtilities.invokeLater(() -> new LoginFrame(authService).setVisible(true));
        }
    }
} 