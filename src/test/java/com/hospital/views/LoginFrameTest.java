package com.hospital.views;

import com.hospital.models.User;
import com.hospital.security.AuthService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoginFrameTest {
    @Mock
    private AuthService authService;

    private LoginFrame loginFrame;
    private static Robot robot;
    private static final int DIALOG_TIMEOUT_MS = 2000;

    @BeforeAll
    static void setUpClass() {
        // Ensure we're not in headless mode
        System.setProperty("java.awt.headless", "false");
        // Initialize toolkit
        Toolkit.getDefaultToolkit();
        try {
            robot = new Robot();
            // Set up UI Manager to automatically close dialog boxes
            UIManager.put("OptionPane.buttonTypes", new Object[]{UIManager.getString("OptionPane.okButtonText")});
        } catch (AWTException e) {
            fail("Failed to create robot: " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);
            SwingUtilities.invokeAndWait(() -> {
                loginFrame = new LoginFrame(authService);
            });
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        if (loginFrame != null) {
            SwingUtilities.invokeLater(() -> loginFrame.dispose());
        }
    }

    @Test
    void testLoginWithValidCredentials() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                // Given
                User mockUser = new User(1, "Test User", "testuser", "DOCTOR", 1);
                when(authService.login(anyString(), anyString())).thenReturn(mockUser);

                // When
                JTextField usernameField = findComponent(loginFrame, JTextField.class);
                JPasswordField passwordField = findComponent(loginFrame, JPasswordField.class);
                JButton loginButton = findComponent(loginFrame, JButton.class);

                assertNotNull(usernameField, "Username field should not be null");
                assertNotNull(passwordField, "Password field should not be null");
                assertNotNull(loginButton, "Login button should not be null");

                usernameField.setText("testuser");
                passwordField.setText("password123");
                loginButton.doClick();

                // Then
                verify(authService).login("testuser", "password123");
            });
            // Wait for any dialogs to be processed
            robot.delay(500);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testLoginWithInvalidCredentials() {
        try {
            CountDownLatch dialogShown = new CountDownLatch(1);
            AtomicReference<String> dialogMessage = new AtomicReference<>();

            // Set up dialog listener
            SwingUtilities.invokeAndWait(() -> {
                // Given
                when(authService.login(anyString(), anyString())).thenReturn(null);

                // When
                JTextField usernameField = findComponent(loginFrame, JTextField.class);
                JPasswordField passwordField = findComponent(loginFrame, JPasswordField.class);
                JButton loginButton = findComponent(loginFrame, JButton.class);

                usernameField.setText("invalid");
                passwordField.setText("invalid");

                // Add window listener to capture dialog
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("activeWindow", evt -> {
                    if (evt.getNewValue() instanceof JDialog) {
                        JDialog dialog = (JDialog) evt.getNewValue();
                        // Get the message from the dialog
                        for (Component comp : ((JDialog) evt.getNewValue()).getContentPane().getComponents()) {
                            if (comp instanceof JOptionPane) {
                                dialogMessage.set(((JOptionPane) comp).getMessage().toString());
                                dialog.dispose();
                                dialogShown.countDown();
                                break;
                            }
                        }
                    }
                });

                loginButton.doClick();
            });

            // Wait for dialog to appear and be processed
            assertTrue(dialogShown.await(DIALOG_TIMEOUT_MS, TimeUnit.MILLISECONDS), "Dialog did not appear within timeout");
            assertEquals("Invalid username or password", dialogMessage.get());
            verify(authService).login("invalid", "invalid");
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testEmptyCredentials() {
        try {
            CountDownLatch dialogShown = new CountDownLatch(1);
            AtomicReference<String> dialogMessage = new AtomicReference<>();

            // Set up dialog listener
            SwingUtilities.invokeAndWait(() -> {
                // When
                JTextField usernameField = findComponent(loginFrame, JTextField.class);
                JPasswordField passwordField = findComponent(loginFrame, JPasswordField.class);
                JButton loginButton = findComponent(loginFrame, JButton.class);

                usernameField.setText("");
                passwordField.setText("");

                // Add window listener to capture dialog
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("activeWindow", evt -> {
                    if (evt.getNewValue() instanceof JDialog) {
                        JDialog dialog = (JDialog) evt.getNewValue();
                        // Get the message from the dialog
                        for (Component comp : ((JDialog) evt.getNewValue()).getContentPane().getComponents()) {
                            if (comp instanceof JOptionPane) {
                                dialogMessage.set(((JOptionPane) comp).getMessage().toString());
                                dialog.dispose();
                                dialogShown.countDown();
                                break;
                            }
                        }
                    }
                });

                loginButton.doClick();
            });

            // Wait for dialog to appear and be processed
            assertTrue(dialogShown.await(DIALOG_TIMEOUT_MS, TimeUnit.MILLISECONDS), "Dialog did not appear within timeout");
            assertEquals("Invalid username or password", dialogMessage.get());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testLoginWithDifferentRoles() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                // Test different user roles
                String[] roles = {"DOCTOR", "NURSE", "PATIENT", "ADMIN", "STAFF", "PHARMACIST", 
                                "IT_SUPPORT", "COMMUNITY_MANAGER", "LAB_TECHNICIAN", "DIETICIAN"};

                for (String role : roles) {
                    // Given
                    User mockUser = new User(1, "Test User", "testuser", role, 1);
                    when(authService.login("testuser", "password123")).thenReturn(mockUser);

                    // When
                    JTextField usernameField = findComponent(loginFrame, JTextField.class);
                    JPasswordField passwordField = findComponent(loginFrame, JPasswordField.class);
                    JButton loginButton = findComponent(loginFrame, JButton.class);

                    usernameField.setText("testuser");
                    passwordField.setText("password123");
                    loginButton.doClick();

                    // Then
                    verify(authService, times(1)).login("testuser", "password123");

                    // Reset mock for next iteration
                    reset(authService);
                }
            });
            // Wait for any dialogs to be processed
            robot.delay(500);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    private <T extends Component> T findComponent(Container container, Class<T> componentClass) {
        for (Component component : container.getComponents()) {
            if (componentClass.isInstance(component)) {
                return componentClass.cast(component);
            }
            if (component instanceof Container) {
                T found = findComponent((Container) component, componentClass);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
} 