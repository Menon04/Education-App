package com.mycompany.education.views;

import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.session.UserSession;
import com.mycompany.education.models.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class ProfessorDashBoard extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable courseTable;
    private JTable materialTable;
    private JTable studentTable;
    private JButton logoutButton;
    private UserSession userSession;
    private CursoDAO cursoDAO; 
    public ProfessorDashBoard(UserSession userSession) {
        this.userSession = userSession;
        this.cursoDAO = new CursoDAO(); 
        initComponents();
        carregarCursos(); 
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        JPanel coursePanel = new JPanel(new BorderLayout());
        courseTable = new JTable();
        coursePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        JButton createCourseButton = new JButton("Criar Curso");
        createCourseButton.addActionListener(e -> abrirTelaCadastroCurso());
        coursePanel.add(createCourseButton, BorderLayout.SOUTH);
        tabbedPane.add("Cursos", coursePanel);

        JPanel materialPanel = new JPanel(new BorderLayout());
        materialTable = new JTable();
        materialPanel.add(new JScrollPane(materialTable), BorderLayout.CENTER);
        JButton publishMaterialButton = new JButton("Publicar Material");
        publishMaterialButton.addActionListener(e -> publishMaterial());
        materialPanel.add(publishMaterialButton, BorderLayout.SOUTH);
        tabbedPane.add("Materiais", materialPanel);

        JPanel studentPanel = new JPanel(new BorderLayout());
        studentTable = new JTable();
        studentPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        JButton evaluateStudentButton = new JButton("Avaliar Aluno");
        evaluateStudentButton.addActionListener(e -> evaluateStudent());
        studentPanel.add(evaluateStudentButton, BorderLayout.SOUTH);
        tabbedPane.add("Alunos", studentPanel);

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

        setTitle("Professor Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(mainPanel);
    }

    private void abrirTelaCadastroCurso() {
        new TelaCadastroCurso(userSession.user()).setVisible(true);
        this.dispose();
    }

    private void publishMaterial() {
        // Adicione aqui a lógica para publicar material
    }

    private void evaluateStudent() {
        // Adicione aqui a lógica para avaliar aluno
    }

    private void logout() {
        userSession.clearSession();
        new TelaInicial().setVisible(true);
        this.dispose();
    }

    private void carregarCursos() {
        List<Curso> cursos = cursoDAO.findAll(); 
        String[] columns = {"ID", "Título", "Descrição", "Professor"}; 

        // Criar o modelo da tabela
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        // Adicionar os cursos ao modelo
        for (Curso curso : cursos) {
            String professorNome = curso.professor().nome() + " " + curso.professor().sobrenome();
            Object[] rowData = {curso.id(), curso.titulo(), curso.descricao(), professorNome};
            tableModel.addRow(rowData);
        }

        // Configurar a tabela com o modelo
        courseTable.setModel(tableModel);
    }

    // public static void main(String[] args) {
    //     UserSession userSession = UserSession.getInstance(); // Crie o objeto UserSession apropriado
    //     SwingUtilities.invokeLater(() -> new ProfessorDashBoard(userSession).setVisible(true));
    // }
}
