package com.hotel.gui;

import com.hotel.model.User;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileScreen extends JFrame {
    private User currentUser;
    private UserDashboardScreen parent;

    public ProfileScreen(User user, UserDashboardScreen parent) {
        this.currentUser = user;
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setTitle("Otto Dinoyo Resort - Profil");
        setSize(800, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeader();
        JPanel contentPanel = createContentPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(800, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(new Color(44, 62, 80));

        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(new Color(46, 204, 113));

        JLabel titleLabel = new JLabel("Profil - " + currentUser.getFullName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        JButton backBtn = new JButton("← Kembali");
        backBtn.setBackground(new Color(52, 152, 219));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.addActionListener(e -> dispose());

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(backBtn, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel profileCard = new JPanel(new BorderLayout(20, 20));
        profileCard.setBackground(new Color(236, 240, 241));
        profileCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JPanel avatarPanel = new JPanel();
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
        avatarPanel.setBackground(new Color(236, 240, 241));

        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(currentUser.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(new Color(44, 62, 80));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel("User • Member sejak 2025");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(127, 140, 141));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        avatarPanel.add(avatarLabel);
        avatarPanel.add(Box.createVerticalStrut(15));
        avatarPanel.add(nameLabel);
        avatarPanel.add(Box.createVerticalStrut(5));
        avatarPanel.add(roleLabel);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(236, 240, 241));

        String[][] userDetails = {
                {"📧 Email:", currentUser.getEmail()},
                {"👤 Username:", currentUser.getUsername()},
                {"🔑 Role:", "USER"},
                {"📅 Member Sejak:", "Januari 2025"},
                {"🎯 Status Akun:", "● Aktif"},
                {"📊 Total Reservasi:", parent.getReservations().size() + " reservasi"},
                {"💰 Total Pengeluaran:", "Rp " + String.format("%,.0f", calculateTotalSpent())}
        };

        for (String[] detail : userDetails) {
            JPanel detailPanel = new JPanel(new BorderLayout(10, 0));
            detailPanel.setBackground(new Color(236, 240, 241));
            detailPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

            JLabel keyLabel = new JLabel(detail[0]);
            keyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            keyLabel.setPreferredSize(new Dimension(150, 25));

            JLabel valueLabel = new JLabel(detail[1]);
            valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            detailPanel.add(keyLabel, BorderLayout.WEST);
            detailPanel.add(valueLabel, BorderLayout.CENTER);
            detailsPanel.add(detailPanel);
        }

        profileCard.add(avatarPanel, BorderLayout.WEST);
        profileCard.add(detailsPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton editProfileBtn = new JButton("✏️ Edit Profil");
        editProfileBtn.setBackground(new Color(52, 152, 219));
        editProfileBtn.setForeground(Color.WHITE);
        editProfileBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editProfileBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        editProfileBtn.addActionListener(e -> showEditProfileDialog());

        JButton viewHistoryBtn = new JButton("📜 Lihat Reservasi");
        viewHistoryBtn.setBackground(new Color(155, 89, 182));
        viewHistoryBtn.setForeground(Color.WHITE);
        viewHistoryBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewHistoryBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewHistoryBtn.addActionListener(e -> {
            dispose();
            ManageReservationsScreen manageScreen = new ManageReservationsScreen(currentUser, parent);
            manageScreen.setVisible(true);
        });

        actionPanel.add(editProfileBtn);
        actionPanel.add(viewHistoryBtn);

        contentPanel.add(profileCard, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private double calculateTotalSpent() {
        double total = 0;
        for (UserDashboardScreen.Reservation r : parent.getReservations()) {
            if (r.getStatus().equals("Selesai") || r.getStatus().equals("Dikonfirmasi")) {
                String numericStr = r.getTotal().replaceAll("[^\\d]", "");
                if (!numericStr.isEmpty()) {
                    total += Double.parseDouble(numericStr);
                }
            }
        }
        return total;
    }

    private void showEditProfileDialog() {
        JDialog editDialog = new JDialog(this, "Edit Profil", true);
        editDialog.setSize(500, 450);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("✏️ Edit Profil - " + currentUser.getFullName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        formPanel.add(new JLabel("Nama Lengkap:"));
        JTextField nameField = new JTextField(currentUser.getFullName());
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(currentUser.getEmail());
        formPanel.add(emailField);

        formPanel.add(new JLabel("Username:"));
        JTextField userField = new JTextField(currentUser.getUsername());
        formPanel.add(userField);

        formPanel.add(new JLabel("Nomor Telepon:"));
        JTextField phoneField = new JTextField("");
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Alamat:"));
        JTextArea addressField = new JTextArea("");
        addressField.setLineWrap(true);
        addressField.setRows(3);
        JScrollPane addressScroll = new JScrollPane(addressField);
        formPanel.add(addressScroll);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton saveBtn = new JButton("💾 Simpan Perubahan");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(editDialog,
                        "Nama lengkap tidak boleh kosong!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (emailField.getText().trim().isEmpty() || !emailField.getText().contains("@")) {
                JOptionPane.showMessageDialog(editDialog,
                        "Email tidak valid!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(editDialog,
                        "Username tidak boleh kosong!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simpan profil ke file
            currentUser.setFullName(nameField.getText().trim());
            currentUser.setEmail(emailField.getText().trim());
            currentUser.setUsername(userField.getText().trim());

            try {
                List<String> lines = new ArrayList<>();
                File file = new File("data/users.txt");

                if (file.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().isEmpty()) {
                                lines.add(line);
                            }
                        }
                    }
                }

                for (int i = 0; i < lines.size(); i++) {
                    String[] parts = lines.get(i).split("\\|");
                    if (parts.length >= 4 && parts[2].equals(currentUser.getUsername())) {
                        // Simpan dengan format: fullName|email|username|password|role
                        String password = parts.length >= 4 ? parts[3] : "password123";
                        lines.set(i, currentUser.getFullName() + "|" + currentUser.getEmail() + "|" +
                                currentUser.getUsername() + "|" + password + "|USER");
                        break;
                    }
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(editDialog,
                        "Error menyimpan profil: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(editDialog,
                    "✅ Profil berhasil diperbarui!\nPerubahan akan berlaku saat login berikutnya.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            editDialog.dispose();
            dispose();
            ProfileScreen newProfileScreen = new ProfileScreen(currentUser, parent);
            newProfileScreen.setVisible(true);
        });

        JButton cancelBtn = new JButton("❌ Batal");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.addActionListener(e -> editDialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        editDialog.add(titleLabel, BorderLayout.NORTH);
        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
    }
}