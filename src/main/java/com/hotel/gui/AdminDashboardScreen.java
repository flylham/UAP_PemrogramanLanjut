package com.hotel.gui;

import com.hotel.model.User;
import com.hotel.auth.LoginScreen;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminDashboardScreen extends JFrame {
    private User currentUser;

    // Tambahkan panel untuk konten dinamis
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public AdminDashboardScreen(User user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - Admin Dashboard");
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
        mainContentPanel.add(createReservationsPanel(), "RESERVATIONS");
        mainContentPanel.add(createUsersPanel(), "USERS");
        mainContentPanel.add(createRoomsPanel(), "ROOMS");
        mainContentPanel.add(createReportsPanel(), "REPORTS");
        mainContentPanel.add(createSettingsPanel(), "SETTINGS");

        // ==================== FOOTER ====================
        JPanel footerPanel = createFooterPanel();

        // ==================== FINAL ASSEMBLY ====================
        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Tampilkan dashboard home secara default
        cardLayout.show(mainContentPanel, "HOME");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(121, 85, 72)); // Warna coklat untuk admin
        headerPanel.setPreferredSize(new Dimension(1200, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Logo & Judul
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(new Color(121, 85, 72));

        // Logo dengan emoji yang lebih reliable
        JLabel iconLabel = new JLabel("👑");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(new Color(255, 243, 224));

        JLabel titleLabel = new JLabel("BAOBAB SAFARI RESORT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel dashLabel = new JLabel("| Admin Dashboard");
        dashLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dashLabel.setForeground(new Color(255, 243, 224));

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        titlePanel.add(dashLabel);

        // User Info & Logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setBackground(new Color(121, 85, 72));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(121, 85, 72));

        JLabel userLabel = new JLabel("👑 " + currentUser.getFullName());
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel("● Administrator");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(255, 243, 224));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        userInfoPanel.add(userLabel);
        userInfoPanel.add(statusLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(244, 67, 54));
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
                    "Yakin ingin logout dari admin panel?",
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
        sidebarPanel.setBackground(new Color(93, 64, 55));
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Menu Items untuk Admin
        String[] menuItems = {"Dashboard", "Kelola Reservasi", "Kelola User", "Kelola Kamar", "Laporan", "Pengaturan"};
        String[] menuIcons = {"📊", "📅", "👥", "🏨", "📈", "⚙️"};
        String[] menuCommands = {"HOME", "RESERVATIONS", "USERS", "ROOMS", "REPORTS", "SETTINGS"};

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

        button.setBackground(new Color(93, 64, 55));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(121, 85, 72));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(93, 64, 55));
            }
        });

        button.addActionListener(e -> {
            // Reset semua button ke warna default
            for (Component comp : ((JPanel)button.getParent()).getComponents()) {
                if (comp instanceof JButton) {
                    comp.setBackground(new Color(93, 64, 55));
                }
            }
            // Set button aktif
            button.setBackground(new Color(121, 85, 72));

            // Switch ke panel yang sesuai
            cardLayout.show(mainContentPanel, command);
        });

        return button;
    }

    private JPanel createDashboardHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Color.WHITE);
        homePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Welcome Section
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(244, 241, 222));
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JLabel welcomeLabel = new JLabel("<html><div style='font-size: 28px; color: #5D4037; font-weight: bold; margin-bottom: 10px;'>"
                + "Selamat Datang, " + currentUser.getFullName() + "!</div>"
                + "<div style='font-size: 16px; color: #795548; line-height: 1.6;'>"
                + "Anda login sebagai <strong>Administrator</strong>. "
                + "Gunakan panel ini untuk mengelola seluruh operasional Baobab Safari Resort, "
                + "termasuk reservasi, pengguna, kamar, dan laporan.</div></html>");

        JLabel dateLabel = new JLabel("📅 " + LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setForeground(new Color(121, 85, 72));

        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.add(dateLabel, BorderLayout.SOUTH);

        // Quick Stats untuk Admin
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        String[] stats = {"148", "42", "Rp 89.5 Jt", "92%", "5", "4.8"};
        String[] statLabels = {"Kamar Tersedia", "Reservasi Aktif", "Pendapatan Bulan Ini",
                "Occupancy Rate", "Staff Aktif", "Rating Resort"};
        Color[] statColors = {
                new Color(41, 128, 185), new Color(39, 174, 96), new Color(230, 126, 34),
                new Color(155, 89, 182), new Color(231, 76, 60), new Color(52, 152, 219)
        };

        for (int i = 0; i < 6; i++) {
            statsPanel.add(createAdminStatCard(stats[i], statLabels[i], statColors[i]));
        }

        homePanel.add(welcomePanel, BorderLayout.NORTH);
        homePanel.add(statsPanel, BorderLayout.CENTER);

        return homePanel;
    }

    private JPanel createAdminStatCard(String value, String label, Color color) {
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

    private JPanel createReservationsPanel() {
        JPanel reservationPanel = new JPanel(new BorderLayout());
        reservationPanel.setBackground(Color.WHITE);
        reservationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("📅 Kelola Reservasi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("➕ Tambah Reservasi");
        addBtn.setBackground(new Color(39, 174, 96));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addBtn.addActionListener(e -> showAddReservationDialog());

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        refreshBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Data reservasi diperbarui!");
        });

        buttonPanel.add(addBtn);
        buttonPanel.add(refreshBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Tabel sederhana untuk demo
        String[] columnNames = {"Kode", "Nama Customer", "Check-in", "Check-out", "Status", "Total"};
        Object[][] data = {
                {"BAO-001234", "Budi Santoso", "25-12-2025", "28-12-2025", "Dikonfirmasi", "Rp 3.600.000"},
                {"BAO-001235", "Sari Dewi", "28-12-2025", "30-12-2025", "Pending", "Rp 4.500.000"},
                {"BAO-001236", "Ahmad Wijaya", "20-12-2025", "22-12-2025", "Selesai", "Rp 2.250.000"},
                {"BAO-001237", "Lisa Permata", "15-01-2026", "18-01-2026", "Dibatalkan", "Rp 5.000.000"}
        };

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(93, 64, 55));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton editBtn = new JButton("✏️ Edit Reservasi");
        editBtn.setBackground(new Color(52, 152, 219));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(e -> {
            if (table.getSelectedRow() != -1) {
                String kode = (String) table.getValueAt(table.getSelectedRow(), 0);
                JOptionPane.showMessageDialog(this, "Edit reservasi: " + kode);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih reservasi terlebih dahulu!");
            }
        });

        JButton deleteBtn = new JButton("🗑️ Hapus Reservasi");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> {
            if (table.getSelectedRow() != -1) {
                String kode = (String) table.getValueAt(table.getSelectedRow(), 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Hapus reservasi " + kode + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Reservasi " + kode + " dihapus!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih reservasi terlebih dahulu!");
            }
        });

        JButton printBtn = new JButton("🖨️ Cetak Invoice");
        printBtn.setBackground(new Color(155, 89, 182));
        printBtn.setForeground(Color.WHITE);
        printBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Mencetak invoice...");
        });

        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(printBtn);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        reservationPanel.add(headerPanel, BorderLayout.NORTH);
        reservationPanel.add(contentPanel, BorderLayout.CENTER);

        return reservationPanel;
    }

    private void showAddReservationDialog() {
        JDialog dialog = new JDialog(this, "Tambah Reservasi Baru", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("➕ Tambah Reservasi Baru", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        formPanel.add(new JLabel("Nama Customer:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Tipe Kamar:"));
        JComboBox<String> roomCombo = new JComboBox<>(new String[]{
                "Standard Room", "Deluxe Room", "Family Room", "Suite Room"
        });
        formPanel.add(roomCombo);

        formPanel.add(new JLabel("Check-in:"));
        JTextField checkinField = new JTextField(LocalDate.now().plusDays(1).toString());
        formPanel.add(checkinField);

        formPanel.add(new JLabel("Check-out:"));
        JTextField checkoutField = new JTextField(LocalDate.now().plusDays(3).toString());
        formPanel.add(checkoutField);

        formPanel.add(new JLabel("Catatan:"));
        JTextArea notesField = new JTextArea(2, 20);
        notesField.setLineWrap(true);
        formPanel.add(new JScrollPane(notesField));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton saveBtn = new JButton("💾 Simpan");
        saveBtn.setBackground(new Color(39, 174, 96));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nama customer harus diisi!");
                return;
            }
            JOptionPane.showMessageDialog(dialog, "Reservasi berhasil ditambahkan!");
            dialog.dispose();
        });

        JButton cancelBtn = new JButton("❌ Batal");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(titleLabel, BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createUsersPanel() {
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.setBackground(Color.WHITE);
        usersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel titleLabel = new JLabel("👥 Kelola User");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        // User cards
        JPanel userCardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        userCardsPanel.setBackground(Color.WHITE);
        userCardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        String[] userTypes = {"Administrator", "Staff", "Customer", "Resepsionis", "Housekeeping", "Manager"};
        String[] userIcons = {"👑", "👨‍💼", "👤", "💁", "🧹", "👔"};
        int[] userCounts = {1, 3, 42, 5, 8, 2};
        Color[] userColors = {
                new Color(231, 76, 60), new Color(52, 152, 219), new Color(39, 174, 96),
                new Color(155, 89, 182), new Color(230, 126, 34), new Color(121, 85, 72)
        };

        for (int i = 0; i < userTypes.length; i++) {
            userCardsPanel.add(createUserCard(userTypes[i], userIcons[i], userCounts[i], userColors[i]));
        }

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton addUserBtn = new JButton("➕ Tambah User");
        addUserBtn.setBackground(new Color(155, 89, 182));
        addUserBtn.setForeground(Color.WHITE);
        addUserBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addUserBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        addUserBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Fitur Tambah User - Coming Soon!\n\n" +
                            "Anda akan dapat menambahkan user dengan role:\n" +
                            "- Administrator\n" +
                            "- Staff\n" +
                            "- Customer", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton manageBtn = new JButton("📋 Kelola Hak Akses");
        manageBtn.setBackground(new Color(52, 152, 219));
        manageBtn.setForeground(Color.WHITE);
        manageBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        manageBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        manageBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fitur Kelola Hak Akses - Coming Soon!");
        });

        actionPanel.add(addUserBtn);
        actionPanel.add(manageBtn);

        usersPanel.add(titleLabel, BorderLayout.NORTH);
        usersPanel.add(userCardsPanel, BorderLayout.CENTER);
        usersPanel.add(actionPanel, BorderLayout.SOUTH);

        return usersPanel;
    }

    private JPanel createUserCard(String type, String icon, int count, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel typeLabel = new JLabel(type, SwingConstants.CENTER);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        typeLabel.setForeground(new Color(44, 62, 80));

        JLabel countLabel = new JLabel(count + " users", SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        countLabel.setForeground(color);

        infoPanel.add(typeLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(countLabel);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(card,
                        "Daftar " + type + " (" + count + " users)\n\n" +
                                "Fitur detail user untuk role " + type + " sedang dikembangkan.",
                        "Info " + type, JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }

    private JPanel createRoomsPanel() {
        JPanel roomsPanel = new JPanel(new BorderLayout());
        roomsPanel.setBackground(Color.WHITE);
        roomsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("🏨 Kelola Kamar");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        refreshBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Memuat ulang data kamar...");
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
        int[] availableRooms = {45, 30, 20, 12, 3, 15};
        int[] totalRooms = {50, 35, 25, 15, 5, 18};

        for (int i = 0; i < roomTypes.length; i++) {
            roomsGrid.add(createAdminRoomCard(roomTypes[i], roomPrices[i], roomIcons[i], availableRooms[i], totalRooms[i]));
        }

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton addRoomBtn = new JButton("➕ Tambah Kamar");
        addRoomBtn.setBackground(new Color(39, 174, 96));
        addRoomBtn.setForeground(Color.WHITE);
        addRoomBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addRoomBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fitur Tambah Kamar - Coming Soon!");
        });

        JButton editRoomBtn = new JButton("✏️ Edit Harga");
        editRoomBtn.setBackground(new Color(52, 152, 219));
        editRoomBtn.setForeground(Color.WHITE);
        editRoomBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editRoomBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fitur Edit Harga Kamar - Coming Soon!");
        });

        actionPanel.add(addRoomBtn);
        actionPanel.add(editRoomBtn);

        roomsPanel.add(headerPanel, BorderLayout.NORTH);
        roomsPanel.add(new JScrollPane(roomsGrid), BorderLayout.CENTER);
        roomsPanel.add(actionPanel, BorderLayout.SOUTH);

        return roomsPanel;
    }

    private JPanel createAdminRoomCard(String type, String price, String icon, int available, int total) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel typeLabel = new JLabel(type, SwingConstants.CENTER);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        typeLabel.setForeground(new Color(44, 62, 80));

        JLabel priceLabel = new JLabel(price + "/malam", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(231, 76, 60));

        // Room stats
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        statsPanel.setBackground(Color.WHITE);

        JLabel totalLabel = new JLabel("Total: " + total, SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel availLabel = new JLabel("Tersedia: " + available, SwingConstants.CENTER);
        availLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        availLabel.setForeground(new Color(39, 174, 96));

        JLabel occupiedLabel = new JLabel("Terisi: " + (total - available), SwingConstants.CENTER);
        occupiedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        occupiedLabel.setForeground(new Color(52, 152, 219));

        int occupancyRate = (int) (((double) (total - available) / total) * 100);
        JLabel occupancyLabel = new JLabel("Occupancy: " + occupancyRate + "%", SwingConstants.CENTER);
        occupancyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        occupancyLabel.setForeground(new Color(155, 89, 182));

        statsPanel.add(totalLabel);
        statsPanel.add(availLabel);
        statsPanel.add(occupiedLabel);
        statsPanel.add(occupancyLabel);

        infoPanel.add(typeLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(statsPanel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createReportsPanel() {
        JPanel reportsPanel = new JPanel(new BorderLayout());
        reportsPanel.setBackground(Color.WHITE);
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel titleLabel = new JLabel("📈 Laporan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        // Report cards
        JPanel reportCards = new JPanel(new GridLayout(2, 3, 20, 20));
        reportCards.setBackground(Color.WHITE);
        reportCards.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        String[] reportTypes = {"Laporan Pendapatan", "Laporan Occupancy", "Laporan Tamu",
                "Laporan Reservasi", "Laporan Kamar", "Laporan Staff"};
        String[] reportIcons = {"💰", "📊", "👥", "📅", "🏨", "👤"};
        Color[] reportColors = {
                new Color(39, 174, 96), new Color(41, 128, 185), new Color(155, 89, 182),
                new Color(230, 126, 34), new Color(231, 76, 60), new Color(52, 73, 94)
        };

        for (int i = 0; i < reportTypes.length; i++) {
            reportCards.add(createReportCard(reportTypes[i], reportIcons[i], reportColors[i]));
        }

        // Export buttons
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        exportPanel.setBackground(Color.WHITE);
        exportPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton pdfBtn = new JButton("📄 Export PDF");
        pdfBtn.setBackground(new Color(231, 76, 60));
        pdfBtn.setForeground(Color.WHITE);
        pdfBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pdfBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        pdfBtn.addActionListener(e -> {
            new ReportScreen().setVisible(true);
        });

        JButton excelBtn = new JButton("📊 Export Excel");
        excelBtn.setBackground(new Color(39, 174, 96));
        excelBtn.setForeground(Color.WHITE);
        excelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        excelBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        excelBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Export ke Excel - Coming Soon!");
        });

        exportPanel.add(pdfBtn);
        exportPanel.add(excelBtn);

        reportsPanel.add(titleLabel, BorderLayout.NORTH);
        reportsPanel.add(reportCards, BorderLayout.CENTER);
        reportsPanel.add(exportPanel, BorderLayout.SOUTH);

        return reportsPanel;
    }

    private JPanel createReportCard(String title, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        iconLabel.setForeground(color);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));

        JLabel descLabel = new JLabel("Klik untuk lihat laporan", SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(card,
                        "Membuka " + title + "\n\n" +
                                "Fitur laporan detail sedang dikembangkan.",
                        "Info Laporan", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setBackground(Color.WHITE);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("⚙️ Pengaturan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        // Settings options
        JPanel settingsList = new JPanel();
        settingsList.setLayout(new BoxLayout(settingsList, BoxLayout.Y_AXIS));
        settingsList.setBackground(Color.WHITE);
        settingsList.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        String[] settings = {
                "⚙️  Pengaturan Umum Resort",
                "📧  Konfigurasi Email & Notifikasi",
                "💳  Pengaturan Payment Gateway",
                "📱  Integrasi Mobile App",
                "🔐  Keamanan Sistem",
                "🧹  Backup & Restore Data",
                "📊  Template Invoice & Laporan",
                "👥  Hak Akses & Role Management"
        };

        for (String setting : settings) {
            JButton settingBtn = new JButton(setting);
            settingBtn.setHorizontalAlignment(SwingConstants.LEFT);
            settingBtn.setBackground(Color.WHITE);
            settingBtn.setForeground(new Color(44, 62, 80));
            settingBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            settingBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            settingBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this,
                        "Membuka: " + setting + "\n\n" +
                                "Fitur pengaturan sistem sedang dikembangkan.",
                        "Info Pengaturan", JOptionPane.INFORMATION_MESSAGE);
            });
            settingsList.add(settingBtn);
        }

        // System info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(244, 241, 222));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel infoTitle = new JLabel("ℹ️ Informasi Sistem");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoTitle.setForeground(new Color(44, 62, 80));

        JLabel infoContent = new JLabel("<html><div style='padding: 10px;'>"
                + "<b>Versi Sistem:</b> 2.1.0<br>"
                + "<b>Database:</b> MySQL 8.0<br>"
                + "<b>Terakhir Backup:</b> " + LocalDate.now().minusDays(1).toString() + "<br>"
                + "<b>Status Sistem:</b> <span style='color: #27AE60;'>● Berjalan Normal</span><br>"
                + "<b>Update Terakhir:</b> " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "<br>"
                + "</div></html>");
        infoContent.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        infoPanel.add(infoTitle, BorderLayout.NORTH);
        infoPanel.add(infoContent, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 30));
        centerPanel.add(new JScrollPane(settingsList), BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.SOUTH);

        settingsPanel.add(titleLabel, BorderLayout.NORTH);
        settingsPanel.add(centerPanel, BorderLayout.CENTER);

        return settingsPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(93, 64, 55));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Left side - Copyright
        JLabel copyrightLabel = new JLabel("© 2025 Baobab Safari Resort. Admin Panel v2.1.0 | User: " + currentUser.getUsername() + " (ADMIN)");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(255, 243, 224));

        // Right side - Status
        JLabel statusLabel = new JLabel("🟢 Sistem Aktif | 🕒 " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(255, 243, 224));

        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        footerPanel.add(statusLabel, BorderLayout.EAST);

        return footerPanel;
    }
}