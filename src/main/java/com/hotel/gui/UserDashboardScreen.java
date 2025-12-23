package com.hotel.gui;

import com.hotel.model.User;
import com.hotel.auth.LoginScreen;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserDashboardScreen extends JFrame {
    private User currentUser;
    private List<Reservation> reservations;

    // Tambahkan panel untuk konten dinamis
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Komponen untuk refresh
    private JPanel reservationsPanel;
    private JPanel profilePanel;

    // Class untuk menyimpan data reservasi
    class Reservation {
        String code;
        String date;
        String roomType;
        String status;
        String total;
        String notes;

        Reservation(String code, String date, String roomType, String status, String total, String notes) {
            this.code = code;
            this.date = date;
            this.roomType = roomType;
            this.status = status;
            this.total = total;
            this.notes = notes;
        }
    }

    public UserDashboardScreen(User user) {
        this.currentUser = user;
        this.reservations = new ArrayList<>();
        // Tambahkan data dummy
        initializeDummyReservations();
        initUI();
    }

    private void initializeDummyReservations() {
        reservations.add(new Reservation("BAO-001234", "25-12-2025", "Deluxe Room", "Dikonfirmasi", "Rp 3.600.000", "Request early check-in"));
        reservations.add(new Reservation("BAO-001235", "28-12-2025", "Family Room", "Pending", "Rp 4.500.000", "Membawa 2 anak"));
        reservations.add(new Reservation("BAO-001200", "20-12-2025", "Standard Room", "Selesai", "Rp 2.250.000", "Tidak ada catatan"));
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - User Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==================== HEADER ====================
        JPanel headerPanel = createHeaderPanel();

        // ==================== SIDEBAR MENU ====================
        JPanel sidebarPanel = createSidebarPanel();

        // ==================== MAIN CONTENT AREA ====================
        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);
        mainContentPanel.setBackground(Color.WHITE);

        // Tambahkan berbagai screen ke CardLayout
        mainContentPanel.add(createDashboardHomePanel(), "HOME");
        mainContentPanel.add(createRoomListPanel(), "ROOMS");
        mainContentPanel.add(createReservationPanel(), "RESERVATION");

        // Panel reservasi saya
        reservationsPanel = createMyReservationsPanel();
        mainContentPanel.add(reservationsPanel, "MY_RESERVATIONS");

        // Panel profil
        profilePanel = createProfilePanel();
        mainContentPanel.add(profilePanel, "PROFILE");

        // ==================== FOOTER ====================
        JPanel footerPanel = createFooterPanel();

        // ==================== FINAL ASSEMBLY ====================
        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(1200, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Logo & Judul
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(new Color(44, 62, 80));

        // Logo dengan emoji yang lebih reliable
        JLabel iconLabel = new JLabel("🏨");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(new Color(46, 204, 113));

        JLabel titleLabel = new JLabel("BAOBAB SAFARI RESORT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel dashLabel = new JLabel("| Dashboard User");
        dashLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dashLabel.setForeground(new Color(200, 200, 200));

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        titlePanel.add(dashLabel);

        // User Info & Logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setBackground(new Color(44, 62, 80));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(44, 62, 80));

        JLabel userLabel = new JLabel("👤 " + currentUser.getFullName());
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel("● Online");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(46, 204, 113));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        userInfoPanel.add(userLabel);
        userInfoPanel.add(statusLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(192, 57, 43)),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin ingin logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginScreen().setVisible(true);
                dispose();
            }
        });

        userPanel.add(userInfoPanel);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutBtn);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(52, 73, 94));
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Menu Items
        String[] menuItems = {"Dashboard", "Lihat Kamar", "Buat Reservasi", "Reservasi Saya", "Profil & Status"};
        String[] menuIcons = {"📊", "🏨", "📅", "📋", "👤"};
        String[] menuCommands = {"HOME", "ROOMS", "RESERVATION", "MY_RESERVATIONS", "PROFILE"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], menuIcons[i], menuCommands[i]);
            menuButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebarPanel.add(menuButton);
            sidebarPanel.add(Box.createVerticalStrut(5));
        }

        sidebarPanel.add(Box.createVerticalGlue());

        return sidebarPanel;
    }

    private JButton createMenuButton(String text, String icon, String command) {
        JButton button = new JButton("<html><div style='text-align: left; padding: 5px;'>"
                + "<span style='font-size: 20px; margin-right: 15px;'>" + icon + "</span>"
                + "<span style='font-size: 14px;'>" + text + "</span></div></html>");

        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        button.addActionListener(e -> {
            // Reset semua button ke warna default
            for (Component comp : ((JPanel)button.getParent()).getComponents()) {
                if (comp instanceof JButton) {
                    comp.setBackground(new Color(52, 73, 94));
                }
            }
            // Set button aktif
            button.setBackground(new Color(41, 128, 185));

            // Switch ke panel yang sesuai
            cardLayout.show(mainContentPanel, command);

            // Refresh data jika perlu
            if (command.equals("MY_RESERVATIONS")) {
                refreshReservationsPanel();
            } else if (command.equals("PROFILE")) {
                refreshProfilePanel();
            }
        });

        return button;
    }

    private JPanel createDashboardHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Color.WHITE);
        homePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Welcome Section
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(236, 240, 241));
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JLabel welcomeLabel = new JLabel("<html><div style='font-size: 28px; color: #2C3E50; font-weight: bold; margin-bottom: 10px;'>"
                + "Selamat Datang, " + currentUser.getFullName() + "!</div>"
                + "<div style='font-size: 16px; color: #7F8C8D; line-height: 1.6;'>"
                + "Selamat datang di sistem reservasi Baobab Safari Resort. "
                + "Nikmati pengalaman menginap tak terlupakan dengan pemandangan savana Afrika "
                + "yang menakjubkan langsung dari balkon kamar Anda.</div></html>");

        JLabel dateLabel = new JLabel("📅 " + LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setForeground(new Color(52, 152, 219));

        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.add(dateLabel, BorderLayout.SOUTH);

        // Quick Stats
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        String[] stats = {"148", "24/7", "Free", "4.8"};
        String[] statLabels = {"Kamar Tersedia", "Support", "WiFi Premium", "Rating"};
        Color[] statColors = {
                new Color(46, 204, 113), new Color(52, 152, 219),
                new Color(155, 89, 182), new Color(241, 196, 15)
        };

        for (int i = 0; i < 4; i++) {
            statsPanel.add(createStatCard(stats[i], statLabels[i], statColors[i]));
        }

        homePanel.add(welcomePanel, BorderLayout.NORTH);
        homePanel.add(statsPanel, BorderLayout.CENTER);

        return homePanel;
    }

    private JPanel createStatCard(String value, String label, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);

        JLabel labelLabel = new JLabel(label, SwingConstants.CENTER);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelLabel.setForeground(new Color(120, 120, 120));

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(labelLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createRoomListPanel() {
        JPanel roomPanel = new JPanel(new BorderLayout());
        roomPanel.setBackground(Color.WHITE);
        roomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("🏨 Kamar Tersedia");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        refreshBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Memuat ulang daftar kamar...");
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        // Room Grid
        JPanel roomsGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        roomsGrid.setBackground(Color.WHITE);

        String[] roomTypes = {"Standard", "Deluxe", "Family", "Suite", "Presidential", "Safari View"};
        String[] roomPrices = {"Rp 750.000", "Rp 1.200.000", "Rp 1.500.000",
                "Rp 2.500.000", "Rp 5.000.000", "Rp 3.750.000"};
        String[] roomIcons = {"🏠", "⭐", "👨‍👩‍👧‍👦", "🏰", "👑", "🦁"};

        for (int i = 0; i < roomTypes.length; i++) {
            roomsGrid.add(createRoomCard(roomTypes[i], roomPrices[i], roomIcons[i]));
        }

        roomPanel.add(headerPanel, BorderLayout.NORTH);
        roomPanel.add(new JScrollPane(roomsGrid), BorderLayout.CENTER);

        return roomPanel;
    }

    private JPanel createRoomCard(String type, String price, String icon) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel typeLabel = new JLabel(type, SwingConstants.CENTER);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        typeLabel.setForeground(new Color(44, 62, 80));

        JLabel priceLabel = new JLabel(price + "/malam", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(231, 76, 60));

        JButton detailBtn = new JButton("Lihat Detail");
        detailBtn.setBackground(new Color(46, 204, 113));
        detailBtn.setForeground(Color.WHITE);
        detailBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        detailBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        detailBtn.addActionListener(e -> {
            showRoomDetailDialog(type, price);
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(iconLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(typeLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(priceLabel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(detailBtn);

        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }

    private void showRoomDetailDialog(String type, String price) {
        JDialog detailDialog = new JDialog(this, "Detail Kamar", true);
        detailDialog.setSize(500, 400);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("🏨 Detail Kamar " + type, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        detailPanel.add(createDetailRow("💲 Harga:", price + "/malam"));
        detailPanel.add(Box.createVerticalStrut(15));
        detailPanel.add(createDetailRow("🛏️ Kapasitas:", "2-4 orang"));
        detailPanel.add(Box.createVerticalStrut(15));
        detailPanel.add(createDetailRow("🌟 Fasilitas:", "AC, TV LED, WiFi Gratis, Breakfast"));
        detailPanel.add(Box.createVerticalStrut(15));
        detailPanel.add(createDetailRow("🌄 View:", "Pemandangan Savana"));
        detailPanel.add(Box.createVerticalStrut(15));
        detailPanel.add(createDetailRow("✅ Status:", "Tersedia"));

        JButton bookBtn = new JButton("📅 Pesan Sekarang");
        bookBtn.setBackground(new Color(46, 204, 113));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        bookBtn.addActionListener(e -> {
            detailDialog.dispose();
            cardLayout.show(mainContentPanel, "RESERVATION");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(bookBtn);

        detailDialog.add(titleLabel, BorderLayout.NORTH);
        detailDialog.add(new JScrollPane(detailPanel), BorderLayout.CENTER);
        detailDialog.add(buttonPanel, BorderLayout.SOUTH);
        detailDialog.setVisible(true);
    }

    private JPanel createDetailRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelLbl.setPreferredSize(new Dimension(120, 25));

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        row.add(labelLbl, BorderLayout.WEST);
        row.add(valueLbl, BorderLayout.CENTER);
        return row;
    }

    private JPanel createReservationPanel() {
        JPanel reservationPanel = new JPanel(new BorderLayout(20, 20));
        reservationPanel.setBackground(Color.WHITE);
        reservationPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("📅 Buat Reservasi Baru");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Form fields
        String[] labels = {"Nama Pemesan:", "Tipe Kamar:", "Check-in:", "Check-out:", "Jumlah Kamar:", "Jumlah Tamu:", "Permintaan Khusus:"};
        JComponent[] fields = new JComponent[labels.length];

        // Nama pemesan
        fields[0] = new JTextField(currentUser.getFullName());

        // Combo box for room type
        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{
                "Standard Room", "Deluxe Room", "Family Room",
                "Suite Room", "Presidential Suite", "Safari View Room"
        });
        fields[1] = roomTypeCombo;

        // Date fields
        JTextField checkinField = new JTextField(LocalDate.now().plusDays(1).toString());
        JButton checkinBtn = new JButton("📅");
        checkinBtn.addActionListener(e -> {
            // TODO: Implement date picker
            checkinField.setText(LocalDate.now().plusDays(1).toString());
        });

        JPanel checkinPanel = new JPanel(new BorderLayout(5, 0));
        checkinPanel.add(checkinField, BorderLayout.CENTER);
        checkinPanel.add(checkinBtn, BorderLayout.EAST);
        fields[2] = checkinPanel;

        JTextField checkoutField = new JTextField(LocalDate.now().plusDays(3).toString());
        JButton checkoutBtn = new JButton("📅");
        checkoutBtn.addActionListener(e -> {
            // TODO: Implement date picker
            checkoutField.setText(LocalDate.now().plusDays(3).toString());
        });

        JPanel checkoutPanel = new JPanel(new BorderLayout(5, 0));
        checkoutPanel.add(checkoutField, BorderLayout.CENTER);
        checkoutPanel.add(checkoutBtn, BorderLayout.EAST);
        fields[3] = checkoutPanel;

        // Number fields
        JSpinner roomSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        fields[4] = roomSpinner;

        JSpinner guestSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        fields[5] = guestSpinner;

        // Special request text area
        JTextArea specialRequest = new JTextArea(3, 30);
        specialRequest.setLineWrap(true);
        specialRequest.setWrapStyleWord(true);
        specialRequest.setText("Tidak ada catatan");
        fields[6] = new JScrollPane(specialRequest);

        // Add form fields
        for (int i = 0; i < labels.length; i++) {
            JPanel fieldPanel = new JPanel(new BorderLayout(10, 10));
            fieldPanel.setBackground(Color.WHITE);
            fieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

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

        // Submit Button
        JButton submitBtn = new JButton("💾 Buat Reservasi");
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        submitBtn.addActionListener(e -> {
            // Get values
            String roomType = (String) roomTypeCombo.getSelectedItem();
            String checkIn = checkinField.getText();
            String checkOut = checkoutField.getText();
            int rooms = (int) roomSpinner.getValue();
            int guests = (int) guestSpinner.getValue();
            String notes = specialRequest.getText();

            // Validate
            if (checkIn.isEmpty() || checkOut.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Harap isi tanggal check-in dan check-out!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate reservation code
            String reservationCode = "BAO-" + String.format("%06d", (int)(Math.random() * 1000000));

            // Calculate total (dummy calculation)
            double pricePerNight = 0;
            switch (roomType) {
                case "Standard Room": pricePerNight = 750000; break;
                case "Deluxe Room": pricePerNight = 1200000; break;
                case "Family Room": pricePerNight = 1500000; break;
                case "Suite Room": pricePerNight = 2500000; break;
                case "Presidential Suite": pricePerNight = 5000000; break;
                case "Safari View Room": pricePerNight = 3750000; break;
            }

            long daysBetween = 3; // dummy
            double total = pricePerNight * rooms * daysBetween;
            String totalFormatted = String.format("Rp %,.0f", total);

            // Add to reservations list
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            reservations.add(new Reservation(reservationCode, currentDate, roomType, "Pending", totalFormatted, notes));

            // Show confirmation
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
                        <tr><td><b>Status:</b></td><td><span style='color: #F39C12;'>Pending</span></td></tr>
                    </table>
                    <p style='color: #666; font-size: 13px; margin-top: 15px;'>
                        Reservasi Anda telah berhasil dibuat dan akan diproses oleh admin.<br>
                        Cek status reservasi di menu <b>"Reservasi Saya"</b>.
                    </p>
                </div>
                </html>
                """,
                    currentUser.getFullName(), reservationCode, roomType, checkIn, checkOut,
                    rooms, guests, totalFormatted
            );

            JOptionPane.showMessageDialog(this, confirmation, "Reservasi Berhasil", JOptionPane.INFORMATION_MESSAGE);

            // Reset form
            checkinField.setText(LocalDate.now().plusDays(1).toString());
            checkoutField.setText(LocalDate.now().plusDays(3).toString());
            roomSpinner.setValue(1);
            guestSpinner.setValue(2);
            specialRequest.setText("Tidak ada catatan");

            // Refresh reservations panel
            refreshReservationsPanel();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitBtn);

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(buttonPanel);

        reservationPanel.add(titleLabel, BorderLayout.NORTH);
        reservationPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);

        return reservationPanel;
    }

    private JPanel createMyReservationsPanel() {
        JPanel reservationsPanel = new JPanel(new BorderLayout());
        reservationsPanel.setBackground(Color.WHITE);
        reservationsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("📋 Reservasi Saya - " + currentUser.getFullName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        // Update info label - FIXED: gunakan LocalDateTime untuk format dengan jam
        JLabel updateLabel = new JLabel("🔄 Update terakhir: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        updateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updateLabel.setForeground(new Color(127, 140, 141));
        updateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Table of reservations
        String[] columnNames = {"Kode", "Tanggal", "Tipe Kamar", "Status", "Total", "Catatan", "Aksi"};
        Object[][] data = getReservationTableData();

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        // Custom cell renderer for status
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Action buttons
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        JButton newReservationBtn = new JButton("➕ Reservasi Baru");
        newReservationBtn.setBackground(new Color(52, 152, 219));
        newReservationBtn.setForeground(Color.WHITE);
        newReservationBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "RESERVATION");
        });

        JButton refreshBtn = new JButton("🔄 Refresh Data");
        refreshBtn.setBackground(new Color(46, 204, 113));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> {
            refreshReservationsPanel();
            JOptionPane.showMessageDialog(this, "Data reservasi telah diperbarui!");
        });

        JButton printBtn = new JButton("🖨️ Cetak Laporan");
        printBtn.setBackground(new Color(155, 89, 182));
        printBtn.setForeground(Color.WHITE);
        printBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Fitur cetak laporan sedang dikembangkan.\nTotal reservasi: " + reservations.size(),
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(newReservationBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(printBtn);

        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setBackground(Color.WHITE);

        JLabel summaryLabel = new JLabel("Total: " + reservations.size() + " reservasi");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        summaryLabel.setForeground(new Color(44, 62, 80));
        summaryPanel.add(summaryLabel);

        actionPanel.add(buttonPanel, BorderLayout.WEST);
        actionPanel.add(summaryPanel, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(updateLabel, BorderLayout.SOUTH);

        reservationsPanel.add(northPanel, BorderLayout.NORTH);
        reservationsPanel.add(scrollPane, BorderLayout.CENTER);
        reservationsPanel.add(actionPanel, BorderLayout.SOUTH);

        return reservationsPanel;
    }

    private Object[][] getReservationTableData() {
        Object[][] data = new Object[reservations.size()][7];
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            data[i][0] = r.code;
            data[i][1] = r.date;
            data[i][2] = r.roomType;
            data[i][3] = r.status;
            data[i][4] = r.total;
            data[i][5] = r.notes;
            data[i][6] = "Detail";
        }
        return data;
    }

    // Custom cell renderer for status
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = (String) value;
            JLabel label = (JLabel) c;

            switch (status) {
                case "Dikonfirmasi":
                    label.setForeground(new Color(39, 174, 96));
                    label.setText("✅ " + status);
                    break;
                case "Pending":
                    label.setForeground(new Color(243, 156, 18));
                    label.setText("⏳ " + status);
                    break;
                case "Selesai":
                    label.setForeground(new Color(52, 152, 219));
                    label.setText("🏁 " + status);
                    break;
                case "Dibatalkan":
                    label.setForeground(new Color(231, 76, 60));
                    label.setText("❌ " + status);
                    break;
                default:
                    label.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    private void refreshReservationsPanel() {
        // Remove old panel
        mainContentPanel.remove(3); // Reservasi panel is at index 3

        // Create new panel with updated data
        reservationsPanel = createMyReservationsPanel();
        mainContentPanel.add(reservationsPanel, "MY_RESERVATIONS");

        // Refresh CardLayout
        if (cardLayout != null) {
            cardLayout.show(mainContentPanel, "MY_RESERVATIONS");
        }
        revalidate();
        repaint();
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel(new BorderLayout(20, 20));
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("👤 Profil & Status - " + currentUser.getFullName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        // Profile Card
        JPanel profileCard = new JPanel(new BorderLayout(20, 20));
        profileCard.setBackground(new Color(236, 240, 241));
        profileCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Avatar and basic info
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

        JLabel roleLabel = new JLabel("User • Member sejak 2024");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(127, 140, 141));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        avatarPanel.add(avatarLabel);
        avatarPanel.add(Box.createVerticalStrut(15));
        avatarPanel.add(nameLabel);
        avatarPanel.add(Box.createVerticalStrut(5));
        avatarPanel.add(roleLabel);

        // User details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(236, 240, 241));

        String[][] userDetails = {
                {"📧 Email:", currentUser.getEmail()},
                {"👤 Username:", currentUser.getUsername()},
                {"🔑 Role:", currentUser.getRole()},
                {"📅 Member Sejak:", "Januari 2024"},
                {"🎯 Status Akun:", "● Aktif"},
                {"📊 Total Reservasi:", reservations.size() + " reservasi"},
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

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton editProfileBtn = new JButton("✏️ Edit Profil");
        editProfileBtn.setBackground(new Color(52, 152, 219));
        editProfileBtn.setForeground(Color.WHITE);
        editProfileBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editProfileBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        editProfileBtn.addActionListener(e -> showEditProfileDialog());

        JButton changePasswordBtn = new JButton("🔒 Ganti Password");
        changePasswordBtn.setBackground(new Color(241, 196, 15));
        changePasswordBtn.setForeground(Color.WHITE);
        changePasswordBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        changePasswordBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        changePasswordBtn.addActionListener(e -> showChangePasswordDialog());

        JButton viewHistoryBtn = new JButton("📜 Riwayat");
        viewHistoryBtn.setBackground(new Color(155, 89, 182));
        viewHistoryBtn.setForeground(Color.WHITE);
        viewHistoryBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewHistoryBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewHistoryBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "MY_RESERVATIONS");
        });

        actionPanel.add(editProfileBtn);
        actionPanel.add(changePasswordBtn);
        actionPanel.add(viewHistoryBtn);

        profilePanel.add(titleLabel, BorderLayout.NORTH);
        profilePanel.add(profileCard, BorderLayout.CENTER);
        profilePanel.add(actionPanel, BorderLayout.SOUTH);

        return profilePanel;
    }

    private double calculateTotalSpent() {
        double total = 0;
        for (Reservation r : reservations) {
            if (r.status.equals("Selesai") || r.status.equals("Dikonfirmasi")) {
                // Extract numeric value from "Rp X.XXX.XXX"
                String numericStr = r.total.replaceAll("[^\\d]", "");
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
        JTextField phoneField = new JTextField("+62 812-3456-7890");
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Alamat:"));
        JTextArea addressField = new JTextArea("Jl. Contoh No. 123");
        addressField.setLineWrap(true);
        addressField.setRows(3);
        JScrollPane addressScroll = new JScrollPane(addressField);
        formPanel.add(addressScroll);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton saveBtn = new JButton("💾 Simpan Perubahan");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.addActionListener(e -> {
            // Validate input
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

            // Update user data
            currentUser.setFullName(nameField.getText().trim());
            currentUser.setEmail(emailField.getText().trim());
            currentUser.setUsername(userField.getText().trim());

            JOptionPane.showMessageDialog(editDialog,
                    "✅ Profil berhasil diperbarui!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Refresh profile panel
            editDialog.dispose();
            refreshProfilePanel();
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

    private void showChangePasswordDialog() {
        JDialog passDialog = new JDialog(this, "Ganti Password", true);
        passDialog.setSize(500, 400);
        passDialog.setLocationRelativeTo(this);
        passDialog.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("🔒 Ganti Password", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        formPanel.add(new JLabel("Password Lama:"));
        JPasswordField oldPassField = new JPasswordField();
        formPanel.add(oldPassField);

        formPanel.add(new JLabel("Password Baru:"));
        JPasswordField newPassField = new JPasswordField();
        formPanel.add(newPassField);

        formPanel.add(new JLabel("Konfirmasi Password:"));
        JPasswordField confirmPassField = new JPasswordField();
        formPanel.add(confirmPassField);

        formPanel.add(new JLabel(""));
        JLabel hintLabel = new JLabel("Minimal 8 karakter, mengandung angka dan huruf");
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(Color.GRAY);
        formPanel.add(hintLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton saveBtn = new JButton("🔑 Ganti Password");
        saveBtn.setBackground(new Color(241, 196, 15));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.addActionListener(e -> {
            char[] oldPass = oldPassField.getPassword();
            char[] newPass = newPassField.getPassword();
            char[] confirmPass = confirmPassField.getPassword();

            // Validate
            if (oldPass.length == 0) {
                JOptionPane.showMessageDialog(passDialog,
                        "Harap masukkan password lama!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPass.length < 8) {
                JOptionPane.showMessageDialog(passDialog,
                        "Password baru minimal 8 karakter!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check password complexity
            String newPassStr = new String(newPass);
            if (!newPassStr.matches(".*\\d.*") || !newPassStr.matches(".*[a-zA-Z].*")) {
                JOptionPane.showMessageDialog(passDialog,
                        "Password harus mengandung angka dan huruf!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!java.util.Arrays.equals(newPass, confirmPass)) {
                JOptionPane.showMessageDialog(passDialog,
                        "Password baru dan konfirmasi tidak cocok!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // For demo, just show success
            JOptionPane.showMessageDialog(passDialog,
                    "✅ Password berhasil diganti!\nSilakan gunakan password baru untuk login berikutnya.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields
            oldPassField.setText("");
            newPassField.setText("");
            confirmPassField.setText("");

            passDialog.dispose();
        });

        JButton cancelBtn = new JButton("❌ Batal");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.addActionListener(e -> passDialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        passDialog.add(titleLabel, BorderLayout.NORTH);
        passDialog.add(formPanel, BorderLayout.CENTER);
        passDialog.add(buttonPanel, BorderLayout.SOUTH);
        passDialog.setVisible(true);
    }

    private void refreshProfilePanel() {
        // Remove old panel
        mainContentPanel.remove(4); // Profile panel is at index 4

        // Create new panel with updated data
        profilePanel = createProfilePanel();
        mainContentPanel.add(profilePanel, "PROFILE");

        // Refresh CardLayout
        if (cardLayout != null) {
            cardLayout.show(mainContentPanel, "PROFILE");
        }
        revalidate();
        repaint();
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(44, 62, 80));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Left side - Copyright
        JLabel copyrightLabel = new JLabel("© 2025 Baobab Safari Resort. All rights reserved. | User: " + currentUser.getUsername());
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(200, 200, 200));

        // Right side - Contact info
        JLabel contactLabel = new JLabel("📞 (0343) 850-000 | 📧 info@baobabresort.com | 🕒 Update: " +
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contactLabel.setForeground(new Color(200, 200, 200));

        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        footerPanel.add(contactLabel, BorderLayout.EAST);

        return footerPanel;
    }
}