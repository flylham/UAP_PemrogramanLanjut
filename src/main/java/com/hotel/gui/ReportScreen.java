package com.hotel.gui;

import com.hotel.database.DatabaseManager;
import com.hotel.auth.LoginScreen;
import javax.swing.*;
import java.awt.*;

public class ReportScreen extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();

    public ReportScreen() {
        initUI();
        showReports();
    }

    private void initUI() {
        setTitle("Baobab Safari Resort - Laporan & Statistik");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 241, 222));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(121, 85, 72));
        headerPanel.setPreferredSize(new Dimension(900, 120));

        JLabel titleLabel = new JLabel("📊 LAPORAN & STATISTIK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Back button di header
        JButton backBtn = new JButton("← Kembali ke Login");
        backBtn.setBackground(new Color(121, 85, 72));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            new LoginScreen().setVisible(true); // UPDATE: ke LoginScreen
            dispose();
        });

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(backBtn, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button panel di bawah
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(244, 241, 222));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton refreshBtn = new JButton("🔄 Refresh Data");
        JButton printBtn = new JButton("🖨️ Cetak Laporan");
        JButton exportBtn = new JButton("📥 Export ke PDF");

        styleButton(refreshBtn, new Color(33, 150, 243), new Dimension(180, 40));
        styleButton(printBtn, new Color(76, 175, 80), new Dimension(180, 40));
        styleButton(exportBtn, new Color(156, 39, 176), new Dimension(180, 40));

        refreshBtn.addActionListener(e -> showReports());
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Fitur cetak akan segera tersedia!", "Info", JOptionPane.INFORMATION_MESSAGE));
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Fitur export PDF akan segera tersedia!", "Info", JOptionPane.INFORMATION_MESSAGE));

        buttonPanel.add(refreshBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(exportBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void showReports() {
        int totalRooms = db.getTotalRooms();
        int availableRooms = db.getAvailableRooms();
        int occupiedRooms = totalRooms - availableRooms;
        int totalReservations = db.getTotalReservations();

        double occupancyRate = totalRooms > 0 ? (occupiedRooms * 100.0 / totalRooms) : 0;

        // Hitung revenue estimasi
        double estimatedRevenue = 0;
        for (com.hotel.model.Room room : db.getAllRooms()) {
            if (!room.isAvailable()) {
                estimatedRevenue += room.getPrice() * 2; // Asumsi 2 malam
            }
        }

        String report = String.format("""
            <html>
            <div style='padding: 20px; font-family: Segoe UI;'>
                <h1 style='color: #795548; text-align: center;'>📈 LAPORAN SISTEM</h1>
                <p style='text-align: center; color: #666;'>Periode: %s</p>
                
                <div style='display: flex; flex-wrap: wrap; gap: 20px; justify-content: center; margin: 30px 0;'>
                    <!-- Card 1: Kamar -->
                    <div style='background: linear-gradient(135deg, #E8F5E8 0%%, #C8E6C9 100%%); 
                         padding: 25px; border-radius: 15px; width: 250px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>
                        <div style='font-size: 48px; text-align: center;'>🏨</div>
                        <h3 style='color: #2E7D32; margin-top: 10px;'>STATUS KAMAR</h3>
                        <div style='margin-top: 15px;'>
                            <div style='display: flex; justify-content: space-between;'>
                                <span>Total Kamar:</span>
                                <span style='font-weight: bold;'>%d</span>
                            </div>
                            <div style='display: flex; justify-content: space-between; margin-top: 8px;'>
                                <span>Tersedia:</span>
                                <span style='color: #4CAF50; font-weight: bold;'>%d</span>
                            </div>
                            <div style='display: flex; justify-content: space-between; margin-top: 8px;'>
                                <span>Terisi:</span>
                                <span style='color: #F44336; font-weight: bold;'>%d</span>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Card 2: Reservasi -->
                    <div style='background: linear-gradient(135deg, #E3F2FD 0%%, #BBDEFB 100%%); 
                         padding: 25px; border-radius: 15px; width: 250px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>
                        <div style='font-size: 48px; text-align: center;'>📅</div>
                        <h3 style='color: #1565C0; margin-top: 10px;'>RESERVASI</h3>
                        <div style='margin-top: 15px;'>
                            <div style='display: flex; justify-content: space-between;'>
                                <span>Total Reservasi:</span>
                                <span style='font-weight: bold;'>%d</span>
                            </div>
                            <div style='display: flex; justify-content: space-between; margin-top: 8px;'>
                                <span>Pending:</span>
                                <span style='color: #FF9800; font-weight: bold;'>0</span>
                            </div>
                            <div style='display: flex; justify-content: space-between; margin-top: 8px;'>
                                <span>Confirmed:</span>
                                <span style='color: #4CAF50; font-weight: bold;'>0</span>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Card 3: Okupansi -->
                    <div style='background: linear-gradient(135deg, #FFF3E0 0%%, #FFE0B2 100%%); 
                         padding: 25px; border-radius: 15px; width: 250px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>
                        <div style='font-size: 48px; text-align: center;'>📊</div>
                        <h3 style='color: #EF6C00; margin-top: 10px;'>TINGKAT OKUPANSI</h3>
                        <div style='margin-top: 15px; text-align: center;'>
                            <div style='font-size: 36px; font-weight: bold; color: #795548;'>
                                %.1f%%
                            </div>
                            <div style='margin-top: 10px;'>
                                <div style='background: #E0E0E0; height: 10px; border-radius: 5px; overflow: hidden;'>
                                    <div style='background: linear-gradient(90deg, #FF9800, #F44336); 
                                         width: %.1f%%; height: 100%%;'></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Card 4: Revenue -->
                    <div style='background: linear-gradient(135deg, #F3E5F5 0%%, #E1BEE7 100%%); 
                         padding: 25px; border-radius: 15px; width: 250px; box-shadow: 0 4x 12px rgba(0,0,0,0.1);'>
                        <div style='font-size: 48px; text-align: center;'>💰</div>
                        <h3 style='color: #7B1FA2; margin-top: 10px;'>ESTIMASI REVENUE</h3>
                        <div style='margin-top: 15px; text-align: center;'>
                            <div style='font-size: 28px; font-weight: bold; color: #795548;'>
                                Rp %,.0f
                            </div>
                            <p style='font-size: 12px; color: #666; margin-top: 10px;'>
                                Estimasi dari kamar terisi (2 malam)
                            </p>
                        </div>
                    </div>
                </div>
                
                <!-- Table Ringkasan -->
                <div style='background: white; border-radius: 10px; padding: 20px; margin-top: 30px; 
                     box-shadow: 0 2px 8px rgba(0,0,0,0.1);'>
                    <h3 style='color: #795548; border-bottom: 2px solid #795548; padding-bottom: 10px;'>
                        📋 RINGKASAN KAMAR POPULER
                    </h3>
                    <table style='width: 100%%; border-collapse: collapse; margin-top: 15px;'>
                        <thead>
                            <tr style='background-color: #795548; color: white;'>
                                <th style='padding: 12px; text-align: left;'>Tipe Kamar</th>
                                <th style='padding: 12px; text-align: left;'>Jumlah</th>
                                <th style='padding: 12px; text-align: left;'>Harga Rata-rata</th>
                                <th style='padding: 12px; text-align: left;'>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr style='border-bottom: 1px solid #eee;'>
                                <td style='padding: 12px;'>Junior Suite</td>
                                <td style='padding: 12px;'>1</td>
                                <td style='padding: 12px;'>Rp 2,500,000</td>
                                <td style='padding: 12px;'><span style='color: #4CAF50;'>●</span> Tersedia</td>
                            </tr>
                            <tr style='border-bottom: 1px solid #eee;'>
                                <td style='padding: 12px;'>Family Room</td>
                                <td style='padding: 12px;'>1</td>
                                <td style='padding: 12px;'>Rp 1,800,000</td>
                                <td style='padding: 12px;'><span style='color: #4CAF50;'>●</span> Tersedia</td>
                            </tr>
                            <tr style='border-bottom: 1px solid #eee;'>
                                <td style='padding: 12px;'>Deluxe Room</td>
                                <td style='padding: 12px;'>1</td>
                                <td style='padding: 12px;'>Rp 1,500,000</td>
                                <td style='padding: 12px;'><span style='color: #4CAF50;'>●</span> Tersedia</td>
                            </tr>
                            <tr>
                                <td style='padding: 12px;'>Standard Room</td>
                                <td style='padding: 12px;'>1</td>
                                <td style='padding: 12px;'>Rp 800,000</td>
                                <td style='padding: 12px;'><span style='color: #4CAF50;'>●</span> Tersedia</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- Footer Report -->
                <div style='margin-top: 30px; padding: 15px; background-color: #F5F5F5; border-radius: 8px; 
                     border-left: 4px solid #795548;'>
                    <p style='margin: 0; color: #666;'>
                        <b>Catatan:</b> Laporan ini dihasilkan otomatis oleh sistem Baobab Safari Resort.
                        Data diperbarui setiap kali halaman di-refresh.
                    </p>
                </div>
            </div>
            </html>
            """,
                java.time.LocalDate.now().toString(),
                totalRooms, availableRooms, occupiedRooms,
                totalReservations,
                occupancyRate, occupancyRate,
                estimatedRevenue
        );

        JLabel reportLabel = new JLabel(report);
        reportLabel.setVerticalAlignment(SwingConstants.TOP);

        JScrollPane scrollPane = new JScrollPane(reportLabel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Clear previous content and add new report
        getContentPane().removeAll();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Re-add button panel
        JPanel buttonPanel = (JPanel) ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        if (buttonPanel != null) {
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        }

        revalidate();
        repaint();
    }

    private void styleButton(JButton button, Color color, Dimension size) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(size);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Efek hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }
}