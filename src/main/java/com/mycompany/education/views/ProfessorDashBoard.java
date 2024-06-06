package com.mycompany.education.views;

import javax.swing.*;
import java.awt.*;

public class ProfessorDashBoard extends JFrame {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable courseTable;
    private JTable materialTable;
    private JTable studentTable;

    public ProfessorDashBoard() {
        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Cursos
        JPanel coursePanel = new JPanel(new BorderLayout());
        courseTable = new JTable(); 
        coursePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        JButton createCourseButton = new JButton("Criar Curso");
        createCourseButton.addActionListener(e -> createCourse());
        coursePanel.add(createCourseButton, BorderLayout.SOUTH);
        tabbedPane.add("Cursos", coursePanel);

        // Materiais
        JPanel materialPanel = new JPanel(new BorderLayout());
        materialTable = new JTable(); 
        materialPanel.add(new JScrollPane(materialTable), BorderLayout.CENTER);
        JButton publishMaterialButton = new JButton("Publicar Material");
        publishMaterialButton.addActionListener(e -> publishMaterial());
        materialPanel.add(publishMaterialButton, BorderLayout.SOUTH);
        tabbedPane.add("Materiais", materialPanel);

        // Alunos
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentTable = new JTable(); 
        studentPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        JButton evaluateStudentButton = new JButton("Avaliar Aluno");
        evaluateStudentButton.addActionListener(e -> evaluateStudent());
        studentPanel.add(evaluateStudentButton, BorderLayout.SOUTH);
        tabbedPane.add("Alunos", studentPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setTitle("Professor Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(mainPanel);
    }

    private void createCourse() {
        
    }

    private void publishMaterial() {
        
    }

    private void evaluateStudent() {
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProfessorDashBoard().setVisible(true));
    }
}
