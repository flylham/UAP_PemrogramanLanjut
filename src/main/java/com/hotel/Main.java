package com.hotel;

import com.hotel.auth.LoginScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Pakai tema Nimbus yang modern
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                try {
                    // Fallback ke tema sistem
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // Mulai dari Login Screen, bukan langsung Dashboard
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
            loginScreen.setLocationRelativeTo(null);
        });
    }
}