package com.hotel.gui;

import com.hotel.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManageReservationsScreen extends JFrame {
    private User currentUser;
    private UserDashboardScreen parent;
    private JTable table;

    public ManageReservationsScreen(User user, UserDashboardScreen parent) {
        this.currentUser = user;
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setTitle("Otto Dinoyo Resort - Kelola Reservasi");
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeader();
        JPanel contentPanel = createContentPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(1000, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(new Color(44, 62, 80));

        JLabel iconLabel = new JLabel("📋");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(new Color(46, 204, 113));

        JLabel titleLabel = new JLabel("Kelola Reservasi - " + currentUser.getFullName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(44, 62, 80));

        JButton backBtn = new JButton("← Kembali");
        backBtn.setBackground(new Color(52, 152, 219));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.addActionListener(e -> dispose());

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(46, 204, 113));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.addActionListener(e -> refreshTable());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(backBtn);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel updateLabel = new JLabel("🔄 Update terakhir: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                " | Total: " + parent.getReservations().size() + " reservasi");
        updateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updateLabel.setForeground(new Color(127, 140, 141));
        updateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        String[] columnNames = {"Kode", "Tanggal", "Tipe Kamar", "Status", "Total", "Catatan", "Aksi"};
        Object[][] data = getReservationTableData();

        // Gunakan model yang hanya bisa edit kolom aksi
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Hanya kolom "Aksi" yang bisa diedit
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        // Set renderer untuk kolom status
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        // Set renderer dan editor untuk kolom aksi
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        JButton exportBtn = new JButton("💾 Export ke File");
        exportBtn.setBackground(new Color(155, 89, 182));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.addActionListener(e -> {
            exportReservationsToFile();
        });

        buttonPanel.add(exportBtn);

        actionPanel.add(buttonPanel, BorderLayout.WEST);

        contentPanel.add(updateLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private Object[][] getReservationTableData() {
        List<UserDashboardScreen.Reservation> reservations = parent.getReservations();
        Object[][] data = new Object[reservations.size()][7];
        for (int i = 0; i < reservations.size(); i++) {
            UserDashboardScreen.Reservation r = reservations.get(i);
            data[i][0] = r.getCode();
            data[i][1] = r.getDate();
            data[i][2] = r.getRoomType();
            data[i][3] = r.getStatus();
            data[i][4] = r.getTotal();
            data[i][5] = r.getNotes();
            data[i][6] = "Detail/Edit";
        }
        return data;
    }

    private void refreshTable() {
        parent.updateReservations();
        String[] columnNames = {"Kode", "Tanggal", "Tipe Kamar", "Status", "Total", "Catatan", "Aksi"};
        Object[][] newData = getReservationTableData();

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        model.setDataVector(newData, columnNames);

        // Set kembali renderer dan editor
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

        JOptionPane.showMessageDialog(this, "Data reservasi telah diperbarui!\nTotal: " + parent.getReservations().size() + " reservasi");
    }

    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof String) {
                String status = (String) value;
                JLabel label = (JLabel) c;

                switch (status) {
                    case "Dikonfirmasi":
                        label.setForeground(new Color(39, 174, 96));
                        label.setText("✅ " + status);
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
                        label.setText(status != null ? status.toString() : "");
                }
            }

            return c;
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Detail/Edit");
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0 && row < parent.getReservations().size()) {
                    UserDashboardScreen.Reservation reservation = parent.getReservations().get(row);
                    showReservationDetailDialog(reservation, row);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setBackground(new Color(52, 152, 219));
            button.setForeground(Color.WHITE);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }

    private void showReservationDetailDialog(UserDashboardScreen.Reservation reservation, int index) {
        JDialog detailDialog = new JDialog(this, "Detail Reservasi", true);
        detailDialog.setSize(500, 550);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("📋 Detail Reservasi: " + reservation.getCode(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new GridLayout(9, 2, 10, 10));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        detailPanel.add(new JLabel("Kode Reservasi:"));
        JTextField codeField = new JTextField(reservation.getCode());
        codeField.setEditable(false);
        detailPanel.add(codeField);

        detailPanel.add(new JLabel("Tanggal Reservasi:"));
        JTextField dateField = new JTextField(reservation.getDate());
        detailPanel.add(dateField);

        detailPanel.add(new JLabel("Tipe Kamar:"));
        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{
                "Standard Room", "Deluxe Room", "Family Room",
                "Suite Room", "Presidential Suite", "Safari View Room"
        });
        roomTypeCombo.setSelectedItem(reservation.getRoomType());
        detailPanel.add(roomTypeCombo);

        detailPanel.add(new JLabel("Status:"));
        JLabel statusLabel = new JLabel(reservation.getStatus());
        if (reservation.getStatus().equals("Dikonfirmasi")) {
            statusLabel.setForeground(new Color(39, 174, 96));
            statusLabel.setText("✅ " + reservation.getStatus());
        }
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailPanel.add(statusLabel);

        detailPanel.add(new JLabel("Check-in:"));
        JTextField checkInField = new JTextField(reservation.getCheckIn());
        detailPanel.add(checkInField);

        detailPanel.add(new JLabel("Check-out:"));
        JTextField checkOutField = new JTextField(reservation.getCheckOut());
        detailPanel.add(checkOutField);

        detailPanel.add(new JLabel("Jumlah Kamar:"));
        JSpinner roomSpinner = new JSpinner(new SpinnerNumberModel(reservation.getRooms(), 1, 5, 1));
        detailPanel.add(roomSpinner);

        detailPanel.add(new JLabel("Jumlah Tamu:"));
        JSpinner guestSpinner = new JSpinner(new SpinnerNumberModel(reservation.getGuests(), 1, 10, 1));
        detailPanel.add(guestSpinner);

        detailPanel.add(new JLabel("Catatan:"));
        JTextArea notesArea = new JTextArea(reservation.getNotes(), 3, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        detailPanel.add(new JScrollPane(notesArea));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton saveBtn = new JButton("💾 Simpan Perubahan");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.addActionListener(e -> {
            // Validasi input
            if (dateField.getText().trim().isEmpty() ||
                    checkInField.getText().trim().isEmpty() ||
                    checkOutField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(detailDialog,
                        "Harap isi semua field yang wajib!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LocalDate start = LocalDate.parse(checkInField.getText());
                LocalDate end = LocalDate.parse(checkOutField.getText());
                if (end.isBefore(start) || end.isEqual(start)) {
                    JOptionPane.showMessageDialog(detailDialog,
                            "Tanggal check-out harus setelah tanggal check-in!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(detailDialog,
                        "Format tanggal tidak valid! Gunakan format YYYY-MM-DD",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Hitung total baru
            String roomType = (String) roomTypeCombo.getSelectedItem();
            double pricePerNight = 0;
            switch (roomType) {
                case "Standard Room": pricePerNight = 750000; break;
                case "Deluxe Room": pricePerNight = 1200000; break;
                case "Family Room": pricePerNight = 1500000; break;
                case "Suite Room": pricePerNight = 2500000; break;
                case "Presidential Suite": pricePerNight = 5000000; break;
                case "Safari View Room": pricePerNight = 3750000; break;
            }

            try {
                LocalDate start = LocalDate.parse(checkInField.getText());
                LocalDate end = LocalDate.parse(checkOutField.getText());
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end);
                if (daysBetween <= 0) daysBetween = 1;

                double total = pricePerNight * (int) roomSpinner.getValue() * daysBetween;
                String totalFormatted = String.format("Rp %,.0f", total);

                UserDashboardScreen.Reservation updatedReservation =
                        parent.new Reservation(
                                reservation.getCode(),
                                dateField.getText(),
                                roomType,
                                reservation.getStatus(),
                                totalFormatted,
                                notesArea.getText(),
                                checkInField.getText(),
                                checkOutField.getText(),
                                (int) roomSpinner.getValue(),
                                (int) guestSpinner.getValue()
                        );

                parent.updateReservation(index, updatedReservation);

                JOptionPane.showMessageDialog(detailDialog,
                        "✅ Reservasi berhasil diperbarui!\n\n" +
                                "Kode: " + reservation.getCode() + "\n" +
                                "Total baru: " + totalFormatted,
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);

                detailDialog.dispose();
                refreshTable();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(detailDialog,
                        "Error menghitung total: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        });

        JButton deleteBtn = new JButton("🗑️ Hapus Reservasi");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(detailDialog,
                    "Yakin ingin menghapus reservasi " + reservation.getCode() + "?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                parent.removeReservation(index);
                JOptionPane.showMessageDialog(detailDialog,
                        "✅ Reservasi berhasil dihapus!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                detailDialog.dispose();
                refreshTable();
            }
        });

        JButton cancelBtn = new JButton("❌ Batal");
        cancelBtn.setBackground(new Color(127, 140, 141));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.addActionListener(e -> detailDialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(cancelBtn);

        detailDialog.add(titleLabel, BorderLayout.NORTH);
        detailDialog.add(new JScrollPane(detailPanel), BorderLayout.CENTER);
        detailDialog.add(buttonPanel, BorderLayout.SOUTH);
        detailDialog.setVisible(true);
    }

    private void exportReservationsToFile() {
        try {
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdir();
            }

            LocalDate today = LocalDate.now();
            String exportFolder = "data/exports/" + today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Files.createDirectories(Paths.get(exportFolder));

            String filename = exportFolder + "/reservasi_" + currentUser.getUsername() + "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

            File file = new File(filename);

            List<UserDashboardScreen.Reservation> reservations = parent.getReservations();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("=== LAPORAN RESERVASI OTTO DINOYO RESORT ===");
                writer.newLine();
                writer.write("Nama Pelanggan: " + currentUser.getFullName());
                writer.newLine();
                writer.write("Username: " + currentUser.getUsername());
                writer.newLine();
                writer.write("Email: " + currentUser.getEmail());
                writer.newLine();
                writer.write("Tanggal Cetak: " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                writer.newLine();
                writer.write("Total Reservasi: " + reservations.size());
                writer.newLine();
                writer.write("===============================================");
                writer.newLine();
                writer.newLine();

                if (reservations.isEmpty()) {
                    writer.write("Tidak ada data reservasi.");
                    writer.newLine();
                } else {
                    int counter = 1;
                    for (UserDashboardScreen.Reservation r : reservations) {
                        writer.write("RESERVASI #" + counter);
                        writer.newLine();
                        writer.write("Kode Reservasi: " + r.getCode());
                        writer.newLine();
                        writer.write("Tanggal Pemesanan: " + r.getDate());
                        writer.newLine();
                        writer.write("Tipe Kamar: " + r.getRoomType());
                        writer.newLine();
                        writer.write("Status: " + r.getStatus());
                        writer.newLine();
                        writer.write("Check-in: " + r.getCheckIn());
                        writer.newLine();
                        writer.write("Check-out: " + r.getCheckOut());
                        writer.newLine();
                        writer.write("Jumlah Kamar: " + r.getRooms());
                        writer.newLine();
                        writer.write("Jumlah Tamu: " + r.getGuests());
                        writer.newLine();
                        writer.write("Total Biaya: " + r.getTotal());
                        writer.newLine();
                        writer.write("Catatan: " + r.getNotes());
                        writer.newLine();
                        writer.write("-----------------------------------");
                        writer.newLine();
                        writer.newLine();
                        counter++;
                    }
                }

                writer.write("=== END OF REPORT ===");
            }

            String message = String.format("""
                <html>
                <div style='padding: 10px;'>
                    <h3 style='color: #27AE60;'>✅ Export Berhasil!</h3>
                    <p>Data reservasi telah berhasil diexport ke file:</p>
                    <p><b>%s</b></p>
                    <p>Total reservasi: <b>%d</b></p>
                    <p style='font-size: 11px; color: #666;'>
                    File tersimpan di: <code>%s</code>
                    </p>
                </div>
                </html>
                """,
                    file.getName(),
                    reservations.size(),
                    file.getAbsolutePath()
            );

            JOptionPane.showMessageDialog(this, message, "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error export data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}