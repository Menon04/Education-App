package com.mycompany.education.views;

import com.mycompany.education.session.UserSession;

import javax.swing.*;
import java.awt.*;

public class AdminDashBoard extends JFrame {
    private UserSession userSession;

    public AdminDashBoard(UserSession session) {
        this.userSession = session;
        initComponents();
    }

    private void initComponents() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Bem-vindo, Admin: " + userSession.user().email(), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);

        add(panel);
    }
}
