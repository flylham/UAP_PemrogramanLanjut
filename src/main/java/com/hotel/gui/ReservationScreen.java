package com.hotel.gui;

import com.hotel.database.DatabaseManager;
import com.hotel.model.Reservation;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import com.hotel.model.Room;

public class ReservationScreen extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();
    private JTextField nameField, emailField, phoneField;
    private JComboBox<String> roomComboBox;
    private JSpinner checkInSpinner, checkOutSpinner;

    public ReservationScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - Reservasi Baru");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 241, 222));

        // Header
        JLabel titleLabel = new JLabel("FORMULIR RESERVASI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(121, 85, 72));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(244, 241, 222));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nama
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Lengkap:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        // Telepon
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("No. Telepon:"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);

        // Kamar
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Pilih Kamar:"), gbc);

        gbc.gridx = 1;
        roomComboBox = new JComboBox<>();
        db.getAllRooms().stream()
                .filter(Room::isAvailable)
                .forEach(r -> roomComboBox.addItem(r.getName() + " - Rp " + String.format("%,.0f", r.getPrice())));
        formPanel.add(roomComboBox, gbc);

        // Check-in
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Tanggal Check-in:"), gbc);

        gbc.gridx = 1;
        checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor1 = new JSpinner.DateEditor(checkInSpinner, "dd/MM/yyyy");
        checkInSpinner.setEditor(dateEditor1);
        formPanel.add(checkInSpinner, gbc);

        // Check-out
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Tanggal Check-out:"), gbc);

        gbc.gridx = 1;
        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor2 = new JSpinner.DateEditor(checkOutSpinner, "dd/MM/yyyy");
        checkOutSpinner.setEditor(dateEditor2);
        formPanel.add(checkOutSpinner, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(244, 241, 222));

        JButton submitBtn = new JButton("Simpan Reservasi");
        JButton cancelBtn = new JButton("Batal");

        styleButton(submitBtn, new Color(139, 195, 74));
        styleButton(cancelBtn, new Color(121, 85, 72));

        submitBtn.addActionListener(e -> saveReservation());
        cancelBtn.addActionListener(e -> {
            new DashboardScreen().setVisible(true);
            dispose();
        });

        buttonPanel.add(submitBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void saveReservation() {
        if (validateForm()) {
            Reservation reservation = new Reservation(
                    0,
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    1, // ID kamar sederhana
                    LocalDate.now(),
                    LocalDate.now().plusDays(3),
                    "PENDING"
            );

            db.addReservation(reservation);
            JOptionPane.showMessageDialog(this, "Reservasi berhasil disimpan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            new DashboardScreen().setVisible(true);
            dispose();
        }
    }

    private boolean validateForm() {
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap lengkapi semua field!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }
}