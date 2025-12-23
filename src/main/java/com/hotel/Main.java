package com.hotel;

import com.hotel.gui.DashboardScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Pakai tema Nimbus yang sudah modern
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                try {
                    // Fallback ke tema sistem
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // Buat dan tampilkan dashboard
            DashboardScreen dashboard = new DashboardScreen();
            dashboard.setVisible(true);
            dashboard.setLocationRelativeTo(null); // Center window
        });
    }
}