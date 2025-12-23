package com.hotel.gui;

import com.hotel.database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class DashboardScreen extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();

    // KONSTRUKTOR WAJIB ADA
    public DashboardScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Main panel dengan warna tema safari
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 241, 222));

        // Header dengan tema Baobab
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(244, 241, 222));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Tombol menu utama
        JButton roomBtn = createMenuButton("Kamar", "🏨", new Color(139, 195, 74));
        JButton reserveBtn = createMenuButton("Reservasi", "📅", new Color(255, 193, 7));
        JButton manageBtn = createMenuButton("Kelola Kamar", "⚙️", new Color(121, 85, 72));
        JButton reportBtn = createMenuButton("Laporan", "📊", new Color(33, 150, 243));

        roomBtn.addActionListener(e -> openRoomScreen());
        reserveBtn.addActionListener(e -> openReservationScreen());
        manageBtn.addActionListener(e -> openManagementScreen());
        reportBtn.addActionListener(e -> openReportScreen());

        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(roomBtn, gbc);

        gbc.gridx = 1;
        contentPanel.add(reserveBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(manageBtn, gbc);

        gbc.gridx = 1;
        contentPanel.add(reportBtn, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("© 2025 Baobab Safari Resort - Taman Safari Prigen");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setForeground(new Color(121, 85, 72));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(121, 85, 72));
        headerPanel.setPreferredSize(new Dimension(1000, 150));

        // Judul
        JLabel titleLabel = new JLabel("BAOBAB SAFARI RESORT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Subtitle
        JLabel subtitleLabel = new JLabel("African-Themed Resort Experience");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(255, 243, 224));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(new Color(121, 85, 72));
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        headerPanel.add(textPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JButton createMenuButton(String text, String icon, Color color) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + text + "</center></html>");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 150));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Efek hover sederhana
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    // METHOD UNTUK NAVIGASI KE SCREEN LAIN
    private void openRoomScreen() {
        new RoomScreen().setVisible(true);
        this.dispose();
    }

    private void openReservationScreen() {
        new ReservationScreen().setVisible(true);
        this.dispose();
    }

    private void openManagementScreen() {
        new ManagementScreen().setVisible(true);
        this.dispose();
    }

    private void openReportScreen() {
        new ReportScreen().setVisible(true);
        this.dispose();
    }

    // HAPUS method main() jika ada di sini
    // JANGAN ada method main() di sini, pindah ke Main.java
}