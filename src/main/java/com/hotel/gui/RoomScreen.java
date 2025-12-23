package com.hotel.gui;

import com.hotel.database.DatabaseManager;
import com.hotel.model.Room;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RoomScreen extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();
    private JTable roomTable;
    private DefaultTableModel tableModel;

    // KONSTRUKTOR
    public RoomScreen() {
        initUI();
        loadRoomData();
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - Daftar Kamar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 241, 222));

        // Header
        JLabel titleLabel = new JLabel("DAFTAR KAMAR TERSEDIA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(121, 85, 72));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Table
        String[] columns = {"ID", "Nama Kamar", "Tipe", "Harga", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tidak bisa edit langsung
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(30);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(roomTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(244, 241, 222));

        JButton backBtn = new JButton("Kembali ke Dashboard");
        JButton reserveBtn = new JButton("Buat Reservasi");

        styleButton(backBtn, new Color(121, 85, 72));
        styleButton(reserveBtn, new Color(139, 195, 74));

        backBtn.addActionListener(e -> {
            new DashboardScreen().setVisible(true);
            dispose();
        });

        reserveBtn.addActionListener(e -> {
            new ReservationScreen().setVisible(true);
            dispose();
        });

        buttonPanel.add(backBtn);
        buttonPanel.add(reserveBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadRoomData() {
        tableModel.setRowCount(0); // Clear existing data
        for (Room room : db.getAllRooms()) {
            String status = room.isAvailable() ? "Tersedia" : "Terisi";
            tableModel.addRow(new Object[]{
                    room.getId(),
                    room.getName(),
                    room.getType(),
                    String.format("Rp %,.0f", room.getPrice()),
                    status
            });
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }
}