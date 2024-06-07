package com.mycompany.education.views;

import javax.swing.*;
import com.mycompany.education.session.UserSession;

import java.awt.*;

public class AlunoDashBoard extends JFrame {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable courseTable;
    private JTable materialTable;
    private JTable taskTable;
    private JTable gradeTable;
    private JButton logoutButton;
    private UserSession userSession;

    public AlunoDashBoard(UserSession userSession) {
        this.userSession = userSession;
        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Cursos
        JPanel coursePanel = new JPanel(new BorderLayout());
        courseTable = new JTable();
        coursePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        tabbedPane.add("Cursos", coursePanel);

        // Materiais
        JPanel materialPanel = new JPanel(new BorderLayout());
        materialTable = new JTable();
        materialPanel.add(new JScrollPane(materialTable), BorderLayout.CENTER);
        tabbedPane.add("Materiais", materialPanel);

        // Tarefas
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskTable = new JTable();
        taskPanel.add(new JScrollPane(taskTable), BorderLayout.CENTER);
        tabbedPane.add("Tarefas", taskPanel);

        // Avaliações
        JPanel gradePanel = new JPanel(new BorderLayout());
        gradeTable = new JTable();
        gradePanel.add(new JScrollPane(gradeTable), BorderLayout.CENTER);
        tabbedPane.add("Avaliações", gradePanel);

        // Botão de logout
        logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(80, 30)); // Tamanho menor
        logoutButton.addActionListener(e -> logout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(tabbedPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        topPanel.add(logoutButton, gbc);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setTitle("Aluno Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(mainPanel);
    }

    private void logout() {
        userSession.clearSession();
        new TelaInicial().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        // SwingUtilities.invokeLater(() -> new AlunoDashBoard(new UserSession()).setVisible(true));
    }
}