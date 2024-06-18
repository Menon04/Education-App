package com.mycompany.education.views;

import com.mycompany.education.components.ActionButtonEditor;
import com.mycompany.education.components.ActionButtonRenderer;
import com.mycompany.education.components.AlunoButtonEditor;
import com.mycompany.education.components.AlunoButtonRenderer;
import com.mycompany.education.components.ButtonEditor;
import com.mycompany.education.components.ButtonRenderer;
import com.mycompany.education.components.MaterialActionButtonEditor;
import com.mycompany.education.components.MaterialActionButtonRenderer;
import com.mycompany.education.components.MaterialButtonRenderer;
import com.mycompany.education.components.MaterialButtonEditor;
import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.dao.MaterialDAO;
import com.mycompany.education.dao.UsuarioDAO;
import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Curso;
import com.mycompany.education.models.Material;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.session.UserSession;

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
    private MaterialDAO materialDAO;
    private UsuarioDAO usuarioDAO;

    public ProfessorDashBoard(UserSession userSession) {
        this.userSession = userSession;
        this.cursoDAO = new CursoDAO();
        this.materialDAO = new MaterialDAO();
        this.usuarioDAO = new UsuarioDAO();
        initComponents();
        carregarCursos();
        carregarMateriais();
        carregarAlunos();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Painel de cursos
        JPanel coursePanel = new JPanel(new BorderLayout());
        courseTable = new JTable();
        courseTable.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Curso", "Descrição", "Professor", "Ações" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        });
        courseTable.setRowHeight(30);
        courseTable.getColumn("Ações").setCellRenderer(new ActionButtonRenderer("Detalhes"));
        courseTable.getColumn("Ações").setCellEditor(new ActionButtonEditor(courseTable, cursoDAO, "Detalhes"));
        coursePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        JButton createCourseButton = new JButton("Criar Curso");
        createCourseButton.addActionListener(e -> abrirTelaCadastroCurso());
        coursePanel.add(createCourseButton, BorderLayout.SOUTH);
        tabbedPane.add("Cursos", coursePanel);

        // Painel de materiais
        JPanel materialPanel = new JPanel(new BorderLayout());
        materialTable = new JTable();
        materialTable.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Material", "Descrição", "Ações" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        });
        materialTable.setRowHeight(30);
        materialTable.getColumn("Ações").setCellRenderer(new ActionButtonRenderer("Detalhes"));
        materialTable.getColumn("Ações").setCellEditor(new ActionButtonEditor(materialTable, materialDAO, "Detalhes"));
        materialPanel.add(new JScrollPane(materialTable), BorderLayout.CENTER);
        JButton publishMaterialButton = new JButton("Publicar Material");
        publishMaterialButton.addActionListener(e -> abrirTelaCadastroMaterial());
        materialPanel.add(publishMaterialButton, BorderLayout.SOUTH);
        tabbedPane.add("Materiais", materialPanel);

        // Painel de alunos
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentTable = new JTable();
        studentTable.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Nome", "Sobrenome", "Email", "Ações" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        });
        studentTable.setRowHeight(30);
        studentTable.getColumn("Ações").setCellRenderer(new ActionButtonRenderer("Avaliar"));
        studentTable.getColumn("Ações").setCellEditor(new ActionButtonEditor(studentTable, usuarioDAO, "Avaliar"));
        studentPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        JButton evaluateStudentButton = new JButton("Avaliar Aluno");
        evaluateStudentButton.addActionListener(e -> abrirTelaAvaliacaoAluno());
        studentPanel.add(evaluateStudentButton, BorderLayout.SOUTH);
        tabbedPane.add("Alunos", studentPanel);

        logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(80, 30));
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

    private void abrirTelaCadastroMaterial() {
        new TelaCadastroMaterial(userSession.user()).setVisible(true);
        this.dispose();
    }

    private void abrirTelaAvaliacaoAluno() {
        new TelaAvaliacaoAluno(userSession.user()).setVisible(true);
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
            Object[] rowData = { curso.id(), curso.titulo(), curso.descricao(), professorNome, "" };
            tableModel.addRow(rowData);
        }

        courseTable.getColumn("Ações").setCellRenderer(new ButtonRenderer());
        courseTable.getColumn("Ações").setCellEditor(new ButtonEditor(courseTable, cursoDAO));
    }

    private void carregarMateriais() {
        List<Material> materiais = materialDAO.findAll();
        DefaultTableModel tableModel = (DefaultTableModel) materialTable.getModel();
        tableModel.setRowCount(0);

        for (Material material : materiais) {
            Object[] rowData = { material.id(), material.titulo(), material.conteudo(), "" };
            tableModel.addRow(rowData);
        }

        materialTable.getColumn("Ações").setCellRenderer(new MaterialActionButtonRenderer());
        materialTable.getColumn("Ações").setCellEditor(new MaterialActionButtonEditor(materialTable, materialDAO));
    }

    private void carregarAlunos() {
        List<Usuario> alunos = usuarioDAO.findAll();
        DefaultTableModel tableModel = (DefaultTableModel) studentTable.getModel();
        tableModel.setRowCount(0);

        for (Usuario aluno : alunos) {
            if (aluno instanceof Aluno) {
                Object[] rowData = { aluno.id(), aluno.nome(), aluno.sobrenome(), aluno.email(), "" };
                tableModel.addRow(rowData);
            }
        }

        studentTable.getColumn("Ações").setCellRenderer(new AlunoButtonRenderer("Avaliar"));
        studentTable.getColumn("Ações").setCellEditor(new AlunoButtonEditor(studentTable, usuarioDAO, "Avaliar"));
    }
}
