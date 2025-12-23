package com.hotel.gui;

import com.hotel.model.User;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReservationScreen extends JFrame {
    private User currentUser;
    private UserDashboardScreen parent;

    public ReservationScreen(User user, UserDashboardScreen parent) {
        this.currentUser = user;
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setTitle("Otto Dinoyo Resort - Buat Reservasi");
        setSize(800, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeader();
        JPanel formPanel = createReservationForm();

        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(800, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(new Color(44, 62, 80));

        JLabel iconLabel = new JLabel("📅");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(new Color(46, 204, 113));

        JLabel titleLabel = new JLabel("Buat Reservasi Baru");
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

    private JPanel createReservationForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        String[] labels = {"Nama Pemesan:", "Tipe Kamar:", "Check-in:", "Check-out:", "Jumlah Kamar:", "Jumlah Tamu:", "Permintaan Khusus:"};
        JComponent[] fields = new JComponent[labels.length];

        fields[0] = new JTextField(currentUser.getFullName());
        fields[0].setEnabled(false);

        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{
                "Standard Room", "Deluxe Room", "Family Room",
                "Suite Room", "Presidential Suite", "Safari View Room"
        });
        fields[1] = roomTypeCombo;

        JTextField checkinField = new JTextField(LocalDate.now().plusDays(1).toString());
        JButton checkinBtn = new JButton("📅");
        checkinBtn.addActionListener(e -> {
            String date = JOptionPane.showInputDialog(this, "Masukkan tanggal check-in (YYYY-MM-DD):",
                    LocalDate.now().plusDays(1).toString());
            if (date != null && !date.trim().isEmpty()) {
                checkinField.setText(date);
            }
        });

        JPanel checkinPanel = new JPanel(new BorderLayout(5, 0));
        checkinPanel.add(checkinField, BorderLayout.CENTER);
        checkinPanel.add(checkinBtn, BorderLayout.EAST);
        fields[2] = checkinPanel;

        JTextField checkoutField = new JTextField(LocalDate.now().plusDays(3).toString());
        JButton checkoutBtn = new JButton("📅");
        checkoutBtn.addActionListener(e -> {
            String date = JOptionPane.showInputDialog(this, "Masukkan tanggal check-out (YYYY-MM-DD):",
                    LocalDate.now().plusDays(3).toString());
            if (date != null && !date.trim().isEmpty()) {
                checkoutField.setText(date);
            }
        });

        JPanel checkoutPanel = new JPanel(new BorderLayout(5, 0));
        checkoutPanel.add(checkoutField, BorderLayout.CENTER);
        checkoutPanel.add(checkoutBtn, BorderLayout.EAST);
        fields[3] = checkoutPanel;

        JSpinner roomSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        fields[4] = roomSpinner;

        JSpinner guestSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        fields[5] = guestSpinner;

        JTextArea specialRequest = new JTextArea(3, 30);
        specialRequest.setLineWrap(true);
        specialRequest.setWrapStyleWord(true);
        specialRequest.setText("Tidak ada catatan");
        fields[6] = new JScrollPane(specialRequest);

        for (int i = 0; i < labels.length; i++) {
            JPanel fieldPanel = new JPanel(new BorderLayout(10, 10));
            fieldPanel.setBackground(Color.WHITE);
            fieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setPreferredSize(new Dimension(150, 30));

            if (fields[i] instanceof JComponent) {
                ((JComponent)fields[i]).setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }

            fieldPanel.add(label, BorderLayout.WEST);
            fieldPanel.add(fields[i], BorderLayout.CENTER);

            formPanel.add(fieldPanel);
            formPanel.add(Box.createVerticalStrut(10));
        }

        JButton submitBtn = new JButton("💾 Buat Reservasi");
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitBtn.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        submitBtn.addActionListener(e -> {
            String roomType = (String) roomTypeCombo.getSelectedItem();
            String checkIn = checkinField.getText();
            String checkOut = checkoutField.getText();
            int rooms = (int) roomSpinner.getValue();
            int guests = (int) guestSpinner.getValue();
            String notes = specialRequest.getText();

            if (checkIn.isEmpty() || checkOut.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Harap isi tanggal check-in dan check-out!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LocalDate.parse(checkIn);
                LocalDate.parse(checkOut);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Format tanggal tidak valid! Gunakan format YYYY-MM-DD",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String reservationCode = "OT" + String.format("%06d", (int)(Math.random() * 1000000));

            double pricePerNight = 0;
            switch (roomType) {
                case "Standard Room": pricePerNight = 750000; break;
                case "Deluxe Room": pricePerNight = 1200000; break;
                case "Family Room": pricePerNight = 1500000; break;
                case "Suite Room": pricePerNight = 2500000; break;
                case "Presidential Suite": pricePerNight = 5000000; break;
                case "Safari View Room": pricePerNight = 3750000; break;
            }

            long daysBetween = 3;
            try {
                LocalDate start = LocalDate.parse(checkIn);
                LocalDate end = LocalDate.parse(checkOut);
                daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end);
                if (daysBetween <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Tanggal check-out harus setelah tanggal check-in!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                daysBetween = 3;
            }

            double total = pricePerNight * rooms * daysBetween;
            String totalFormatted = String.format("Rp %,.0f", total);

            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            // Status langsung "Dikonfirmasi" (bukan "Pending")
            UserDashboardScreen.Reservation newReservation = parent.new Reservation(
                    reservationCode, currentDate, roomType, "Dikonfirmasi", totalFormatted,
                    notes, checkIn, checkOut, rooms, guests
            );

            // Simpan ke sistem (ini akan memanggil saveReservationToDailyFolder otomatis)
            parent.addReservation(newReservation);

            String confirmation = String.format("""
                <html>
                <div style='padding: 20px; max-width: 500px;'>
                    <h2 style='color: #27AE60;'>✅ Reservasi Berhasil!</h2>
                    <table style='width: 100%%; border-collapse: collapse; margin: 15px 0;'>
                        <tr><td><b>Nama Pemesan:</b></td><td>%s</td></tr>
                        <tr><td><b>Kode Reservasi:</b></td><td>%s</td></tr>
                        <tr><td><b>Tipe Kamar:</b></td><td>%s</td></tr>
                        <tr><td><b>Check-in:</b></td><td>%s</td></tr>
                        <tr><td><b>Check-out:</b></td><td>%s</td></tr>
                        <tr><td><b>Jumlah Kamar:</b></td><td>%d</td></tr>
                        <tr><td><b>Jumlah Tamu:</b></td><td>%d</td></tr>
                        <tr><td><b>Total:</b></td><td>%s</td></tr>
                        <tr><td><b>Status:</b></td><td><span style='color: #27AE60;'>✅ Dikonfirmasi</span></td></tr>
                    </table>
                    <p><small>Data telah disimpan ke folder tanggal</small></p>
                </div>
                </html>
                """,
                    currentUser.getFullName(), reservationCode, roomType, checkIn, checkOut,
                    rooms, guests, totalFormatted
            );

            JOptionPane.showMessageDialog(this, confirmation, "Reservasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitBtn);

        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(buttonPanel);

        return formPanel;
    }
}