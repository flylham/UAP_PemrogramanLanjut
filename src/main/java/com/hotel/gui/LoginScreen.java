package com.hotel.gui;

import com.hotel.database.AuthManager;
import com.hotel.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheck;

    public LoginScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Otto Dinoyo Resort - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel dengan background warna safari
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 241, 222)); // Krem

        // ========== HEADER ==========
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(121, 85, 72)); // Coklat safari
        headerPanel.setPreferredSize(new Dimension(450, 100));

        JLabel titleLabel = new JLabel("OTTO DINOYO RESORT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Sistem Reservasi Hotel");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(255, 243, 224));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(121, 85, 72));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel);

        // ========== FORM PANEL ==========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(244, 241, 222));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(new Color(121, 85, 72));
        formPanel.add(userLabel, gbc);

        // Username Field
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(new Color(121, 85, 72));
        formPanel.add(passLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setEchoChar('•');
        formPanel.add(passwordField, gbc);

        // Show Password Checkbox
        gbc.gridx = 1; gbc.gridy = 2;
        showPasswordCheck = new JCheckBox("Show Password");
        showPasswordCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheck.setBackground(new Color(244, 241, 222));
        showPasswordCheck.setForeground(new Color(100, 100, 100));
        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        formPanel.add(showPasswordCheck, gbc);

        // ========== BUTTON PANEL ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(244, 241, 222));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Keluar");

        // Style Login Button (Hijau safari)
        loginBtn.setBackground(new Color(121, 85, 72));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setPreferredSize(new Dimension(120, 40));
        loginBtn.setFocusPainted(false);

        // Style Exit Button (Coklat safari)
        exitBtn.setBackground(new Color(121, 85, 72));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitBtn.setPreferredSize(new Dimension(120, 40));
        exitBtn.setFocusPainted(false);

        // Action Listeners
        loginBtn.addActionListener(e -> performLogin());
        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginBtn);
        buttonPanel.add(exitBtn);

        // ========== FOOTER ==========
        JLabel footerLabel = new JLabel("© 2025 Otto Dinoyo Resort", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(121, 85, 72));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // ========== LAYOUT FINAL ==========
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        // Tambah buttonPanel ke SOUTH, footerLabel tetap di SOUTH (akan overlap)
        // Perbaiki dengan panel gabungan
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(244, 241, 222));
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(footerLabel, BorderLayout.SOUTH);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter key untuk login
        getRootPane().setDefaultButton(loginBtn);

        // Set focus ke username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username dan password harus diisi!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User authenticatedUser = AuthManager.getInstance().authenticate(username, password);

        if (authenticatedUser != null) {
            // Cek apakah user memiliki role yang valid (tidak perlu ADMIN)
            // Untuk sistem ini, semua user yang terautentikasi langsung ke dashboard user
            JOptionPane.showMessageDialog(this,
                    "Login berhasil!\nWelcome " + authenticatedUser.getFullName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Selalu redirect ke UserDashboardScreen tanpa pengecekan role
            new UserDashboardScreen(authenticatedUser).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Username atau password salah!",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }
}