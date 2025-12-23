package com.hotel.gui;

import com.hotel.database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class ReportScreen extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();

    public ReportScreen() {
        initUI();
        showReports();
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - Laporan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 241, 222));

        // Header
        JLabel titleLabel = new JLabel("LAPORAN RESERVASI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(121, 85, 72));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(244, 241, 222));

        JButton backBtn = new JButton("Kembali ke Dashboard");
        styleButton(backBtn, new Color(121, 85, 72));
        backBtn.addActionListener(e -> {
            new DashboardScreen().setVisible(true);
            dispose();
        });

        buttonPanel.add(backBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void showReports() {
        int totalRooms = db.getAllRooms().size();
        int availableRooms = (int) db.getAllRooms().stream().filter(r -> r.isAvailable()).count();
        int totalReservations = db.getAllReservations().size();

        String report = String.format("""
            <html>
            <div style='padding: 20px; font-family: Segoe UI;'>
                <h2 style='color: #795548;'>Laporan Sistem Reservasi</h2>
                <div style='background: #E8F5E8; padding: 15px; border-radius: 10px; margin: 10px 0;'>
                    <h3>📊 Statistik Kamar</h3>
                    <p>• Total Kamar: %d</p>
                    <p>• Kamar Tersedia: %d</p>
                    <p>• Kamar Terisi: %d</p>
                </div>
                <div style='background: #E3F2FD; padding: 15px; border-radius: 10px; margin: 10px 0;'>
                    <h3>📈 Statistik Reservasi</h3>
                    <p>• Total Reservasi: %d</p>
                    <p>• Reservasi Pending: 0</p>
                    <p>• Reservasi Dikonfirmasi: 0</p>
                </div>
                <div style='background: #FFF3E0; padding: 15px; border-radius: 10px; margin: 10px 0;'>
                    <h3>🎯 Ringkasan</h3>
                    <p>• Tingkat okupasi: %.1f%%</p>
                    <p>• Kamar paling populer: Junior Suite</p>
                </div>
            </div>
            </html>
            """, totalRooms, availableRooms, totalRooms - availableRooms,
                totalReservations, ((totalRooms - availableRooms) * 100.0 / totalRooms));

        JLabel reportLabel = new JLabel(report);
        reportLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(reportLabel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }
}