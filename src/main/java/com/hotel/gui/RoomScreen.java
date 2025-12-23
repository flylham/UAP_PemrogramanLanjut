package com.hotel.gui;

import com.hotel.database.DatabaseManager;
import com.hotel.model.Room;
import com.hotel.auth.LoginScreen;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RoomScreen extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();
    private JTable roomTable;
    private DefaultTableModel tableModel;

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
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(30);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(roomTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(244, 241, 222));

        JButton backBtn = new JButton("Kembali ke Login");
        JButton reserveBtn = new JButton("Buat Reservasi");
        JButton refreshBtn = new JButton("Refresh");

        // Style buttons
        backBtn.setBackground(new Color(121, 85, 72));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setPreferredSize(new Dimension(180, 40));
        backBtn.setFocusPainted(false);

        reserveBtn.setBackground(new Color(139, 195, 74));
        reserveBtn.setForeground(Color.WHITE);
        reserveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        reserveBtn.setPreferredSize(new Dimension(180, 40));
        reserveBtn.setFocusPainted(false);

        refreshBtn.setBackground(new Color(33, 150, 243));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setFocusPainted(false);

        // Back button - ke LoginScreen
        backBtn.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });

        // Reserve button - SIMPEL tanpa parameter User
        reserveBtn.addActionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();

            if (selectedRow >= 0) {
                int roomId = (int) tableModel.getValueAt(selectedRow, 0);
                String roomName = (String) tableModel.getValueAt(selectedRow, 1);
                String status = (String) tableModel.getValueAt(selectedRow, 4);

                if ("Tersedia".equals(status)) {
                    ReservationScreen reservationScreen = new ReservationScreen(roomId, roomName);
                    reservationScreen.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Kamar tidak tersedia!",
                            "Info", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                new ReservationScreen().setVisible(true);
                dispose();
            }
        });

        refreshBtn.addActionListener(e -> loadRoomData());

        buttonPanel.add(backBtn);
        buttonPanel.add(reserveBtn);
        buttonPanel.add(refreshBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadRoomData() {
        tableModel.setRowCount(0);
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
}