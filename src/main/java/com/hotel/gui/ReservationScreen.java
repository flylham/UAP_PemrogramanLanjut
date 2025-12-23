package com.hotel.gui;

import com.hotel.database.DatabaseManager;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.auth.LoginScreen;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ReservationScreen extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();
    private JTextField nameField, emailField, phoneField;
    private JComboBox<String> roomComboBox;
    private JSpinner checkInSpinner, checkOutSpinner;
    private int preselectedRoomId = -1;

    // Constructor 1: Tanpa parameter
    public ReservationScreen() {
        initUI();
        loadAvailableRooms();
    }

    // Constructor 2: Dengan pre-selected room
    public ReservationScreen(int roomId, String roomName) {
        this.preselectedRoomId = roomId;
        initUI();
        loadAvailableRooms();

        if (roomId > 0 && roomName != null) {
            for (int i = 0; i < roomComboBox.getItemCount(); i++) {
                if (roomComboBox.getItemAt(i).contains(roomName)) {
                    roomComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - Form Reservasi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 241, 222));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(121, 85, 72));
        headerPanel.setPreferredSize(new Dimension(800, 100));

        JLabel titleLabel = new JLabel("FORMULIR RESERVASI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(244, 241, 222));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));

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
        roomComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(roomComboBox, gbc);

        // Check-in
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Tanggal Check-in:"), gbc);

        gbc.gridx = 1;
        checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor1 = new JSpinner.DateEditor(checkInSpinner, "dd/MM/yyyy");
        checkInSpinner.setEditor(dateEditor1);
        checkInSpinner.setValue(new Date());
        formPanel.add(checkInSpinner, gbc);

        // Check-out
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Tanggal Check-out:"), gbc);

        gbc.gridx = 1;
        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor2 = new JSpinner.DateEditor(checkOutSpinner, "dd/MM/yyyy");
        checkOutSpinner.setEditor(dateEditor2);

        Date tomorrow = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 2));
        checkOutSpinner.setValue(tomorrow);
        formPanel.add(checkOutSpinner, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(244, 241, 222));

        JButton submitBtn = new JButton("Simpan Reservasi");
        JButton cancelBtn = new JButton("Batal");
        JButton clearBtn = new JButton("Bersihkan");

        // Style buttons
        submitBtn.setBackground(new Color(139, 195, 74));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setPreferredSize(new Dimension(180, 40));
        submitBtn.setFocusPainted(false);

        cancelBtn.setBackground(new Color(121, 85, 72));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(120, 40));
        cancelBtn.setFocusPainted(false);

        clearBtn.setBackground(new Color(33, 150, 243));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearBtn.setPreferredSize(new Dimension(120, 40));
        clearBtn.setFocusPainted(false);

        submitBtn.addActionListener(e -> saveReservation());
        cancelBtn.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(submitBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(clearBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadAvailableRooms() {
        roomComboBox.removeAllItems();
        roomComboBox.addItem("-- Pilih Kamar --");

        for (Room room : db.getAllRooms()) {
            if (room.isAvailable()) {
                roomComboBox.addItem(room.getName() + " - Rp " +
                        String.format("%,.0f", room.getPrice()));

                // Simpan ID di client property
                roomComboBox.putClientProperty("room_" + roomComboBox.getItemCount(), room.getId());
            }
        }

        if (roomComboBox.getItemCount() == 1) {
            roomComboBox.addItem("Tidak ada kamar tersedia");
            roomComboBox.setEnabled(false);
        }
    }

    private void saveReservation() {
        if (!validateForm()) {
            return;
        }

        try {
            int roomId = getSelectedRoomId();
            if (roomId == -1) {
                JOptionPane.showMessageDialog(this,
                        "Pilih kamar terlebih dahulu!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date checkInDate = (Date) checkInSpinner.getValue();
            Date checkOutDate = (Date) checkOutSpinner.getValue();

            LocalDate checkIn = checkInDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate checkOut = checkOutDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                JOptionPane.showMessageDialog(this,
                        "Tanggal check-out harus setelah tanggal check-in!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            Room selectedRoom = db.getRoomById(roomId);
            double totalPrice = selectedRoom.getPrice() * nights;

            // Konfirmasi
            String confirmation = String.format(
                    "Konfirmasi Reservasi:\n\n" +
                            "Nama: %s\n" +
                            "Email: %s\n" +
                            "Telepon: %s\n" +
                            "Kamar: %s\n" +
                            "Check-in: %s\n" +
                            "Check-out: %s (%d malam)\n" +
                            "Total: Rp %,.0f\n\n" +
                            "Simpan reservasi?",
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    selectedRoom.getName(),
                    checkIn,
                    checkOut,
                    nights,
                    totalPrice
            );

            int confirm = JOptionPane.showConfirmDialog(this,
                    confirmation, "Konfirmasi Reservasi",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Reservation reservation = new Reservation(
                        0,
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneField.getText().trim(),
                        roomId,
                        checkIn,
                        checkOut,
                        "PENDING"
                );

                db.addReservation(reservation);

                String successMsg = String.format(
                        "Reservasi Berhasil!\n\n" +
                                "ID Reservasi: %d\n" +
                                "Total pembayaran: Rp %,.0f",
                        db.getTotalReservations(),
                        totalPrice
                );

                JOptionPane.showMessageDialog(this,
                        successMsg, "Sukses", JOptionPane.INFORMATION_MESSAGE);

                clearForm();
                new LoginScreen().setVisible(true);
                dispose();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private int getSelectedRoomId() {
        int selectedIndex = roomComboBox.getSelectedIndex();
        if (selectedIndex <= 0) return -1;

        Object roomId = roomComboBox.getClientProperty("room_" + selectedIndex);
        return roomId != null ? (int) roomId : -1;
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Harap lengkapi semua field!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (roomComboBox.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih kamar terlebih dahulu!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        roomComboBox.setSelectedIndex(0);
        checkInSpinner.setValue(new Date());

        Date tomorrow = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 2));
        checkOutSpinner.setValue(tomorrow);

        nameField.requestFocus();
    }
}