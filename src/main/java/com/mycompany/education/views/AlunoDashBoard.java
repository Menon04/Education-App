package com.mycompany.education.views;

import javax.swing.*;
import java.awt.*;

public class AlunoDashBoard extends JFrame {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable courseTable;
    private JTable materialTable;
    private JTable taskTable;
    private JTable gradeTable;

    public AlunoDashBoard() {
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

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setTitle("Aluno Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AlunoDashBoard().setVisible(true));
    }
}