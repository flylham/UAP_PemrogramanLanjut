package com.hotel.gui;

import com.hotel.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserDashboardScreen extends JFrame {
    private User currentUser;
    private List<Reservation> reservations;

    private static final String DATA_FOLDER = "data";
    private static final String DAILY_FOLDER = DATA_FOLDER + "/daily";
    private static final String USERS_FILE = DATA_FOLDER + "/users.txt";

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public class Reservation {
        String code;
        String date;
        String roomType;
        String status;
        String total;
        String notes;
        String checkIn;
        String checkOut;
        int rooms;
        int guests;

        public Reservation(String code, String date, String roomType, String status, String total, String notes,
                           String checkIn, String checkOut, int rooms, int guests) {
            this.code = code;
            this.date = date;
            this.roomType = roomType;
            this.status = status;
            this.total = total;
            this.notes = notes;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.rooms = rooms;
            this.guests = guests;
        }

        // Getter methods
        public String getCode() { return code; }
        public String getDate() { return date; }
        public String getRoomType() { return roomType; }
        public String getStatus() { return status; }
        public String getTotal() { return total; }
        public String getNotes() { return notes; }
        public String getCheckIn() { return checkIn; }
        public String getCheckOut() { return checkOut; }
        public int getRooms() { return rooms; }
        public int getGuests() { return guests; }
    }

    public UserDashboardScreen(User user) {
        this.currentUser = user;
        this.reservations = new ArrayList<>();

        createDataFolder();
        loadReservationsFromDailyFolder();

        initUI();
    }

    private void createDataFolder() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File dailyFolder = new File(DAILY_FOLDER);
        if (!dailyFolder.exists()) {
            dailyFolder.mkdir();
        }

        try {
            new File(USERS_FILE).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========== LOAD DATA - DIPERBAIKI ===========
    private void loadReservationsFromDailyFolder() {
        reservations.clear();
        try {
            File dailyDir = new File(DAILY_FOLDER);
            if (dailyDir.exists() && dailyDir.isDirectory()) {
                File[] dateFolders = dailyDir.listFiles((dir, name) -> name.startsWith("Reservasi_"));

                if (dateFolders != null) {
                    for (File dateFolder : dateFolders) {
                        File reservationsFile = new File(dateFolder, "reservations.txt");
                        if (reservationsFile.exists()) {
                            try (BufferedReader reader = new BufferedReader(new FileReader(reservationsFile))) {
                                String line;
                                String currentCode = null;
                                String currentDate = null;
                                String currentRoomType = null;
                                String currentStatus = null;
                                String currentTotal = null;
                                String currentNotes = "Tidak ada catatan";
                                String currentCheckIn = LocalDate.now().plusDays(1).toString();
                                String currentCheckOut = LocalDate.now().plusDays(3).toString();
                                int currentRooms = 1;
                                int currentGuests = 2;

                                while ((line = reader.readLine()) != null) {
                                    line = line.trim();

                                    if (line.startsWith("Kode: ")) {
                                        currentCode = line.substring(6);
                                    }
                                    else if (line.startsWith("Tipe Kamar: ")) {
                                        currentRoomType = line.substring(12);
                                    }
                                    else if (line.startsWith("Status: ")) {
                                        currentStatus = line.substring(8);
                                    }
                                    else if (line.startsWith("Total: ")) {
                                        currentTotal = line.substring(7);
                                    }
                                    else if (line.startsWith("Tanggal Reservasi: ")) {
                                        currentDate = line.substring(19);
                                    }
                                    else if (line.startsWith("Check-in: ")) {
                                        currentCheckIn = line.substring(10);
                                    }
                                    else if (line.startsWith("Check-out: ")) {
                                        currentCheckOut = line.substring(11);
                                    }
                                    else if (line.startsWith("Jumlah Kamar: ")) {
                                        try {
                                            currentRooms = Integer.parseInt(line.substring(14));
                                        } catch (NumberFormatException e) {
                                            currentRooms = 1;
                                        }
                                    }
                                    else if (line.startsWith("Jumlah Tamu: ")) {
                                        try {
                                            currentGuests = Integer.parseInt(line.substring(13));
                                        } catch (NumberFormatException e) {
                                            currentGuests = 2;
                                        }
                                    }
                                    else if (line.startsWith("Catatan: ")) {
                                        currentNotes = line.substring(9);
                                    }
                                    else if (line.equals("------------------------")) {
                                        if (currentCode != null && currentRoomType != null && currentStatus != null) {
                                            // Jika tanggal null, ambil dari folder
                                            if (currentDate == null) {
                                                String folderName = dateFolder.getName();
                                                if (folderName.startsWith("Reservasi_")) {
                                                    try {
                                                        String dateStr = folderName.substring(10);
                                                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("ddMMMMyyyy");
                                                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                                        LocalDate folderDate = LocalDate.parse(dateStr, inputFormatter);
                                                        currentDate = folderDate.format(outputFormatter);
                                                    } catch (Exception e) {
                                                        currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                                    }
                                                }
                                            }

                                            // Cek apakah sudah ada
                                            boolean exists = false;
                                            for (Reservation r : reservations) {
                                                if (r.getCode().equals(currentCode)) {
                                                    exists = true;
                                                    break;
                                                }
                                            }

                                            if (!exists) {
                                                Reservation newRes = new Reservation(
                                                        currentCode, currentDate, currentRoomType,
                                                        currentStatus, currentTotal, currentNotes,
                                                        currentCheckIn, currentCheckOut, currentRooms, currentGuests
                                                );
                                                reservations.add(newRes);
                                            }
                                        }

                                        // Reset
                                        currentCode = null;
                                        currentDate = null;
                                        currentRoomType = null;
                                        currentStatus = null;
                                        currentTotal = null;
                                        currentNotes = "Tidak ada catatan";
                                        currentCheckIn = LocalDate.now().plusDays(1).toString();
                                        currentCheckOut = LocalDate.now().plusDays(3).toString();
                                        currentRooms = 1;
                                        currentGuests = 2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Error loading from daily folders: " + ex.getMessage());
        }
    }

    // =========== SAVE/UPDATE DATA - DIPERBAIKI ===========
    public void saveReservationToDailyFolder(Reservation reservation) {
        try {
            // Tentukan tanggal
            LocalDate reservationDate;
            try {
                reservationDate = LocalDate.parse(reservation.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (Exception e) {
                reservationDate = LocalDate.now();
            }

            String folderName = "Reservasi_" + reservationDate.format(DateTimeFormatter.ofPattern("ddMMMMyyyy"));
            File folder = new File(DAILY_FOLDER + "/" + folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, "reservations.txt");

            List<String> lines = new ArrayList<>();
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }
            }

            // Format data baru
            String dateFormatted = reservationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String header = "=== RESERVASI " + dateFormatted + " ===";
            String userInfo = "User: " + currentUser.getUsername() + " (" + currentUser.getFullName() + ")";
            String separator = "======================================";

            List<String> newReservationLines = new ArrayList<>();
            newReservationLines.add("Kode: " + reservation.getCode());
            newReservationLines.add("Tipe Kamar: " + reservation.getRoomType());
            newReservationLines.add("Status: " + reservation.getStatus());
            newReservationLines.add("Total: " + reservation.getTotal());
            newReservationLines.add("Tanggal Reservasi: " + reservation.getDate());
            newReservationLines.add("Check-in: " + reservation.getCheckIn());
            newReservationLines.add("Check-out: " + reservation.getCheckOut());
            newReservationLines.add("Jumlah Kamar: " + reservation.getRooms());
            newReservationLines.add("Jumlah Tamu: " + reservation.getGuests());
            newReservationLines.add("Catatan: " + reservation.getNotes());
            newReservationLines.add("------------------------");

            // Cari dan hapus reservasi lama dengan kode yang sama
            List<String> newLines = new ArrayList<>();
            boolean found = false;
            boolean inUserSection = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                // Jika menemukan reservasi dengan kode yang sama
                if (line.startsWith("Kode: " + reservation.getCode())) {
                    found = true;
                    // Skip reservasi lama (10 baris + separator)
                    int skipCount = 0;
                    for (int j = i; j < Math.min(i + 12, lines.size()); j++) {
                        if (lines.get(j).equals("------------------------")) {
                            skipCount = j - i + 1;
                            break;
                        }
                    }
                    if (skipCount > 0) {
                        i += skipCount - 1;
                    }
                    continue;
                }

                // Simpan line lainnya
                newLines.add(line);
            }

            // Jika tidak ditemukan, tambahkan header dan user info jika perlu
            if (!found) {
                boolean headerExists = false;
                boolean userExists = false;

                for (String line : newLines) {
                    if (line.startsWith("=== RESERVASI ")) headerExists = true;
                    if (line.startsWith("User: ") && line.contains(currentUser.getUsername())) userExists = true;
                }

                if (!headerExists) {
                    newLines.add(0, separator);
                    newLines.add(0, userInfo);
                    newLines.add(0, header);
                } else if (!userExists) {
                    // Cari posisi header dan tambahkan user info setelahnya
                    for (int i = 0; i < newLines.size(); i++) {
                        if (newLines.get(i).startsWith("=== RESERVASI ")) {
                            newLines.add(i + 1, userInfo);
                            newLines.add(i + 2, separator);
                            break;
                        }
                    }
                }
            }

            // Tambahkan reservasi baru di akhir
            newLines.addAll(newReservationLines);

            // Tulis kembali ke file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : newLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException ex) {
            System.err.println("Gagal menyimpan ke folder harian: " + ex.getMessage());
        }
    }

    // =========== DELETE DATA - DIPERBAIKI ===========
    public void deleteReservationFromDailyFolder(String reservationCode) {
        try {
            File dailyDir = new File(DAILY_FOLDER);
            if (dailyDir.exists() && dailyDir.isDirectory()) {
                File[] dateFolders = dailyDir.listFiles((dir, name) -> name.startsWith("Reservasi_"));

                if (dateFolders != null) {
                    for (File dateFolder : dateFolders) {
                        File file = new File(dateFolder, "reservations.txt");
                        if (file.exists()) {
                            boolean deleted = deleteReservationFromFile(file, reservationCode);
                            if (deleted) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Gagal menghapus dari folder harian: " + ex.getMessage());
        }
    }

    private boolean deleteReservationFromFile(File file, String reservationCode) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            List<String> newLines = new ArrayList<>();
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                // Jika menemukan reservasi dengan kode yang sama
                if (line.startsWith("Kode: " + reservationCode)) {
                    found = true;
                    // Skip 11 baris (reservasi data + separator)
                    int skipCount = 0;
                    for (int j = i; j < Math.min(i + 12, lines.size()); j++) {
                        if (lines.get(j).equals("------------------------")) {
                            skipCount = j - i + 1;
                            break;
                        }
                    }
                    if (skipCount > 0) {
                        i += skipCount - 1;
                    }
                    continue;
                }

                newLines.add(line);
            }

            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String line : newLines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                return true;
            }

        } catch (IOException ex) {
            System.err.println("Error deleting from file: " + file.getPath() + " - " + ex.getMessage());
        }
        return false;
    }

    // =========== USER MANAGEMENT ===========
    private void saveUserToFile() {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(USERS_FILE);

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            lines.add(line);
                        }
                    }
                }
            }

            boolean userFound = false;
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split("\\|");
                if (parts.length >= 4 && parts[2].equals(currentUser.getUsername())) {
                    lines.set(i, currentUser.getFullName() + "|" + currentUser.getEmail() + "|" +
                            currentUser.getUsername() + "|" + getCurrentPassword() + "|USER");
                    userFound = true;
                    break;
                }
            }

            if (!userFound) {
                lines.add(currentUser.getFullName() + "|" + currentUser.getEmail() + "|" +
                        currentUser.getUsername() + "|" + "password123" + "|USER");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error menyimpan user: " + e.getMessage());
        }
    }

    private String getCurrentPassword() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 4 && parts[2].equals(currentUser.getUsername())) {
                                return parts[3];
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "password123";
    }

    // =========== GUI METHODS ===========
    private void initUI() {
        setTitle("OTTO DINOYO RESORT - Dashboard Utama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        JPanel sidebarPanel = createSidebarPanel();

        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);
        mainContentPanel.setBackground(Color.WHITE);

        mainContentPanel.add(createDashboardHomePanel(), "HOME");
        mainContentPanel.add(createRoomListPanel(), "ROOMS");

        JPanel footerPanel = createFooterPanel();

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

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(new Color(44, 62, 80));

        JLabel iconLabel = new JLabel("🏨");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(new Color(46, 204, 113));

        JLabel titleLabel = new JLabel("OTTO DINOYO RESORT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel dashLabel = new JLabel("| Dashboard Utama");
        dashLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dashLabel.setForeground(new Color(200, 200, 200));

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        titlePanel.add(dashLabel);

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
                saveUserToFile();
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

        String[] menuItems = {"Dashboard Utama", "Lihat Kamar", "Buat Reservasi", "Kelola Reservasi", "Profil"};
        String[] menuIcons = {"📊", "🏨", "📅", "📋", "👤"};
        String[] menuCommands = {"HOME", "ROOMS", "NEW_RESERVATION", "MANAGE_RESERVATIONS", "PROFILE"};

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

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        button.addActionListener(e -> {
            for (Component comp : ((JPanel)button.getParent()).getComponents()) {
                if (comp instanceof JButton) {
                    comp.setBackground(new Color(52, 73, 94));
                }
            }
            button.setBackground(new Color(41, 128, 185));

            if (command.equals("HOME") || command.equals("ROOMS")) {
                cardLayout.show(mainContentPanel, command);
            } else if (command.equals("NEW_RESERVATION")) {
                openReservationScreen();
            } else if (command.equals("MANAGE_RESERVATIONS")) {
                openManageReservationsScreen();
            } else if (command.equals("PROFILE")) {
                openProfileScreen();
            }
        });

        return button;
    }

    private void openReservationScreen() {
        SwingUtilities.invokeLater(() -> {
            ReservationScreen reservationScreen = new ReservationScreen(currentUser, this);
            reservationScreen.setVisible(true);
        });
    }

    private void openManageReservationsScreen() {
        SwingUtilities.invokeLater(() -> {
            ManageReservationsScreen manageScreen = new ManageReservationsScreen(currentUser, this);
            manageScreen.setVisible(true);
        });
    }

    private void openProfileScreen() {
        SwingUtilities.invokeLater(() -> {
            ProfileScreen profileScreen = new ProfileScreen(currentUser, this);
            profileScreen.setVisible(true);
        });
    }

    private JPanel createDashboardHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Color.WHITE);
        homePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(236, 240, 241));
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JLabel welcomeLabel = new JLabel("<html><div style='font-size: 28px; color: #2C3E50; font-weight: bold; margin-bottom: 10px;'>"
                + "Selamat Datang, " + currentUser.getFullName() + "!</div>"
                + "<div style='font-size: 16px; color: #7F8C8D; line-height: 1.6;'>"
                + "Selamat datang di sistem reservasi OTTO DINOYO RESORT. "
                + "Nikmati pengalaman menginap tak terlupakan dengan fasilitas premium "
                + "dan pelayanan terbaik langsung dari kamar Anda.</div></html>");

        JLabel dateLabel = new JLabel("📅 " + LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setForeground(new Color(52, 152, 219));

        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.add(dateLabel, BorderLayout.SOUTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        String[] stats = {String.valueOf(reservations.size()), "5", "Free", "4.8"};
        String[] statLabels = {"Reservasi Aktif", "Kamar Tersedia", "WiFi Premium", "Rating"};
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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("🏨 Kamar Tersedia - OTTO DINOYO RESORT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JButton bookButton = new JButton("📅 Buat Reservasi");
        bookButton.setBackground(new Color(46, 204, 113));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bookButton.addActionListener(e -> openReservationScreen());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(bookButton, BorderLayout.EAST);

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

        JLabel titleLabel = new JLabel("🏨 Detail Kamar " + type + " - OTTO DINOYO RESORT", SwingConstants.CENTER);
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
        detailPanel.add(createDetailRow("🌄 View:", "Pemandangan Premium"));
        detailPanel.add(Box.createVerticalStrut(15));
        detailPanel.add(createDetailRow("✅ Status:", "Tersedia"));

        JButton bookBtn = new JButton("📅 Pesan Sekarang");
        bookBtn.setBackground(new Color(46, 204, 113));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        bookBtn.addActionListener(e -> {
            detailDialog.dispose();
            openReservationScreen();
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

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    public void addReservation(Reservation reservation) {
        // Cek apakah reservasi dengan kode yang sama sudah ada
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getCode().equals(reservation.getCode())) {
                // UPDATE: Ganti reservasi lama dengan yang baru
                reservations.set(i, reservation);
                saveReservationToDailyFolder(reservation);
                return;
            }
        }

        // Jika belum ada, TAMBAH baru
        reservations.add(reservation);
        saveReservationToDailyFolder(reservation);
    }

    public void removeReservation(int index) {
        if (index >= 0 && index < reservations.size()) {
            String reservationCode = reservations.get(index).getCode();
            reservations.remove(index);
            deleteReservationFromDailyFolder(reservationCode);
        }
    }

    // =========== METHOD UPDATE RESERVATION YANG BENAR ===========
    public void updateReservation(int index, Reservation updatedReservation) {
        if (index >= 0 && index < reservations.size()) {
            // Simpan kode lama untuk delete
            String oldCode = reservations.get(index).getCode();

            // Update di memory
            reservations.set(index, updatedReservation);

            // Delete old record dari file
            deleteReservationFromDailyFolder(oldCode);

            // Save new record ke file
            saveReservationToDailyFolder(updatedReservation);
        }
    }

    public void updateReservations() {
        loadReservationsFromDailyFolder();
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(44, 62, 80));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel copyrightLabel = new JLabel("© 2024 OTTO DINOYO RESORT. All rights reserved. | User: " + currentUser.getUsername());
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(200, 200, 200));

        JLabel contactLabel = new JLabel("📞 (0343) 850-000 | 📧 info@ottodinoyo.com | 🕒 Data tersimpan di folder 'data/daily/'");
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contactLabel.setForeground(new Color(200, 200, 200));

        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        footerPanel.add(contactLabel, BorderLayout.EAST);

        return footerPanel;
    }
}