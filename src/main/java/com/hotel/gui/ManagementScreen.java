package com.hotel.gui;

import com.hotel.database.DatabaseManager;
import com.hotel.model.Room;
import javax.swing.*;
import java.awt.*;

public class ManagementScreen extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();

    public ManagementScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - Manajemen Kamar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 241, 222));

        // Header
        JLabel titleLabel = new JLabel("KELOLA KAMAR");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(121, 85, 72));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Form CRUD
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(244, 241, 222));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Button CRUD
        JButton addBtn = new JButton("Tambah Kamar Baru");
        JButton editBtn = new JButton("Edit Kamar");
        JButton deleteBtn = new JButton("Hapus Kamar");
        JButton viewBtn = new JButton("Lihat Semua Kamar");

        styleButton(addBtn, new Color(139, 195, 74));
        styleButton(editBtn, new Color(33, 150, 243));
        styleButton(deleteBtn, new Color(244, 67, 54));
        styleButton(viewBtn, new Color(255, 193, 7));

        // Layout buttons
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(addBtn, gbc);

        gbc.gridx = 1;
        formPanel.add(editBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(deleteBtn, gbc);

        gbc.gridx = 1;
        formPanel.add(viewBtn, gbc);

        // Button actions
        addBtn.addActionListener(e -> showAddRoomDialog());
        viewBtn.addActionListener(e -> showAllRoomsDialog());
        editBtn.addActionListener(e -> showEditRoomDialog());
        deleteBtn.addActionListener(e -> showDeleteRoomDialog());

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(244, 241, 222));

        JButton backBtn = new JButton("Kembali ke Dashboard");
        styleButton(backBtn, new Color(121, 85, 72));
        // Di ManagementScreen.java, cari bagian back button:
        backBtn.addActionListener(e -> {
            new com.hotel.auth.LoginScreen().setVisible(true); // UPDATE
            dispose();
        });

        buttonPanel.add(backBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void showAddRoomDialog() {
        JTextField nameField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField priceField = new JTextField();
        JTextArea descArea = new JTextArea(3, 20);

        Object[] fields = {
                "Nama Kamar:", nameField,
                "Tipe:", typeField,
                "Harga:", priceField,
                "Deskripsi:", new JScrollPane(descArea)
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Tambah Kamar Baru", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Room room = new Room(
                        0,
                        nameField.getText(),
                        typeField.getText(),
                        Double.parseDouble(priceField.getText()),
                        descArea.getText(),
                        true
                );
                db.addRoom(room);
                JOptionPane.showMessageDialog(this, "Kamar berhasil ditambahkan!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Format harga salah!");
            }
        }
    }

    private void showAllRoomsDialog() {
        StringBuilder sb = new StringBuilder("DAFTAR SEMUA KAMAR:\n\n");
        for (Room room : db.getAllRooms()) {
            sb.append(String.format("ID: %d\n", room.getId()))
                    .append(String.format("Nama: %s\n", room.getName()))
                    .append(String.format("Tipe: %s\n", room.getType()))
                    .append(String.format("Harga: Rp %,.0f\n", room.getPrice()))
                    .append(String.format("Status: %s\n\n",
                            room.isAvailable() ? "Tersedia" : "Terisi"));
        }

        JTextArea textArea = new JTextArea(sb.toString(), 15, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane,
                "Daftar Kamar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEditRoomDialog() {
        String idStr = JOptionPane.showInputDialog(this, "Masukkan ID kamar yang akan diedit:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Room room = db.getRoomById(id);
                if (room != null) {
                    // Dialog edit serupa dengan tambah
                    JOptionPane.showMessageDialog(this,
                            "Fitur edit lengkap dapat ditambahkan di sini");
                } else {
                    JOptionPane.showMessageDialog(this, "Kamar tidak ditemukan!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID harus angka!");
            }
        }
    }

    private void showDeleteRoomDialog() {
        String idStr = JOptionPane.showInputDialog(this, "Masukkan ID kamar yang akan dihapus:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                boolean deleted = db.deleteRoom(id);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Kamar berhasil dihapus!");
                } else {
                    JOptionPane.showMessageDialog(this, "Kamar tidak ditemukan!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID harus angka!");
            }
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 60));
    }
}