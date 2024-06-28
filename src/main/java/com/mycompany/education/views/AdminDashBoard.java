package com.mycompany.education.views;

import com.mycompany.education.components.professor.CursoButtonEditor;
import com.mycompany.education.components.professor.CursoButtonRenderer;
import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.models.Curso;
import com.mycompany.education.session.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashBoard extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable courseTable;
    private JButton logoutButton;
    private JButton financeiroButton;
    private UserSession userSession;
    private CursoDAO cursoDAO;

    public AdminDashBoard(UserSession userSession) {
        this.userSession = userSession;
        this.cursoDAO = new CursoDAO();
        initComponents();
        carregarCursos();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Painel e tabela de cursos
        JPanel coursePanel = new JPanel(new BorderLayout());
        courseTable = new JTable();
        courseTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Curso", "Descrição", "Criador", "Ações"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        });
        courseTable.setRowHeight(30);
        coursePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        JButton createCourseButton = new JButton("Criar Curso");
        createCourseButton.addActionListener(e -> abrirTelaCadastroCurso());
        coursePanel.add(createCourseButton, BorderLayout.SOUTH);

        tabbedPane.add("Cursos", coursePanel);

        logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(80, 30));
        logoutButton.addActionListener(e -> logout());

        financeiroButton = new JButton("Financeiro");
        financeiroButton.setPreferredSize(new Dimension(100, 30));
        financeiroButton.addActionListener(e -> abrirDashboardFinanceiro());

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

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        topPanel.add(financeiroButton, gbc);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(mainPanel);
    }

    private void abrirTelaCadastroCurso() {
        new TelaCadastroCurso(userSession.user()).setVisible(true);
        this.dispose();
    }

    private void abrirDashboardFinanceiro() {
        new FinanceiroDashBoard(userSession).setVisible(true);
        this.dispose();
    }

    private void logout() {
        userSession.clearSession();
        new TelaInicial().setVisible(true);
        this.dispose();
    }

    private void carregarCursos() {
        List<Curso> cursos = cursoDAO.findAll();
        DefaultTableModel tableModel = (DefaultTableModel) courseTable.getModel();
        tableModel.setRowCount(0);

        for (Curso curso : cursos) {
            String professorNome = curso.professor().nome() + " " + curso.professor().sobrenome();
            Object[] rowData = {curso.id(), curso.titulo(), curso.descricao(), professorNome, ""};
            tableModel.addRow(rowData);
        }

        courseTable.getColumn("Ações").setCellRenderer(new CursoButtonRenderer());
        courseTable.getColumn("Ações").setCellEditor(new CursoButtonEditor(courseTable, cursoDAO));
    }
}