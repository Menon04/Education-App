package com.mycompany.education.views;

import com.mycompany.education.components.professor.*;
import com.mycompany.education.dao.*;
import com.mycompany.education.models.*;
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
    private JTable taskTable;
    private JButton logoutButton;
    private JButton financeiroButton;
    private UserSession userSession;
    private CursoDAO cursoDAO;
    private MaterialDAO materialDAO;
    private TarefaDAO tarefaDAO;
    private EnvioTarefaDAO envioTarefaDAO;

    public ProfessorDashBoard(UserSession userSession) {
        this.userSession = userSession;
        this.cursoDAO = new CursoDAO();
        this.materialDAO = new MaterialDAO();
        this.tarefaDAO = new TarefaDAO();
        this.envioTarefaDAO = new EnvioTarefaDAO();
        initComponents();
        carregarCursos();
        carregarMateriais();
        carregarTarefas();
        carregarAvaliacoes();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

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

        JPanel materialPanel = new JPanel(new BorderLayout());
        materialTable = new JTable();
        materialTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Titulo", "Conteudo", "Nome do Professor", "Nome do Curso", "Data de Publicacao",
                        "Ações"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        });
        materialTable.setRowHeight(60);
        materialPanel.add(new JScrollPane(materialTable), BorderLayout.CENTER);
        JButton publishMaterialButton = new JButton("Publicar Material");
        publishMaterialButton.addActionListener(e -> abrirTelaCadastroMaterial());
        materialPanel.add(publishMaterialButton, BorderLayout.SOUTH);
        tabbedPane.add("Materiais", materialPanel);

        JPanel taskPanel = new JPanel(new BorderLayout());
        taskTable = new JTable();
        taskTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Tarefa", "Descrição", "Nota", "Data de Entrega", "Data de Publicação", "Curso",
                        "Ações"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        });
        taskTable.setRowHeight(60);
        taskPanel.add(new JScrollPane(taskTable), BorderLayout.CENTER);
        JButton createTaskButton = new JButton("Criar Tarefa");
        createTaskButton.addActionListener(e -> abrirTelaCadastroTarefa());
        taskPanel.add(createTaskButton, BorderLayout.SOUTH);
        tabbedPane.add("Tarefas", taskPanel);

        JPanel studentPanel = new JPanel(new BorderLayout());
        studentTable = new JTable();
        studentTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Envio", "Nome da Tarefa", "Valor", "Nome do Aluno", "Nota", "Status"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        });
        studentTable.setRowHeight(30);
        studentPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        tabbedPane.add("Avaliar Alunos", studentPanel);

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

    private void abrirTelaCadastroTarefa() {
        new TelaCadastroTarefa(userSession).setVisible(true);
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

    private void carregarMateriais() {
        List<Material> materiais = materialDAO.findAll();
        DefaultTableModel tableModel = (DefaultTableModel) materialTable.getModel();
        tableModel.setRowCount(0);

        for (Material material : materiais) {
            Curso curso = cursoDAO.findById(material.cursoId());
            String courseName = curso != null ? curso.titulo() : "Desconhecido";
            String professorNome = curso != null ? curso.professor().nome() + " " + curso.professor().sobrenome()
                    : "Desconhecido";

            Object[] rowData = {material.id(), material.titulo(), material.conteudo(), professorNome, courseName,
                    material.dataPublicacao(), ""};
            tableModel.addRow(rowData);
        }

        materialTable.getColumn("Ações").setCellRenderer(new MaterialButtonRenderer());
        materialTable.getColumn("Ações").setCellEditor(new MaterialButtonEditor(materialTable, materialDAO));
    }

    private void carregarAvaliacoes() {
        List<EnvioTarefa> envios = envioTarefaDAO.findAll();
        DefaultTableModel tableModel = (DefaultTableModel) studentTable.getModel();
        tableModel.setRowCount(0);

        for (EnvioTarefa envio : envios) {
            String status = envio.nota() == 0 ? "Avaliar" : "Avaliado";
            Object[] rowData = {
                    envio.id(),
                    envio.tarefa().titulo(),
                    envio.tarefa().nota(),
                    envio.aluno().nome(),
                    envio.nota() != null ? envio.nota().toString() : "N/A",
                    status
            };
            tableModel.addRow(rowData);
        }

        studentTable.getColumn("Status").setCellRenderer(new AvaliarAlunoButtonRenderer());
        studentTable.getColumn("Status").setCellEditor(new AvaliarAlunoButtonEditor(studentTable, envioTarefaDAO));
    }

    private void carregarTarefas() {
        List<Tarefa> tarefas = tarefaDAO.findAll();
        DefaultTableModel tableModel = (DefaultTableModel) taskTable.getModel();
        tableModel.setRowCount(0);

        for (Tarefa tarefa : tarefas) {
            Curso curso = cursoDAO.findById(tarefa.cursoId());
            String courseName = curso != null ? curso.titulo() : "Desconhecido";

            Object[] rowData = {
                    tarefa.id(),
                    tarefa.titulo(),
                    tarefa.descricao(),
                    tarefa.nota(),
                    tarefa.dataEntrega(),
                    tarefa.dataPublicacao(),
                    courseName,
                    ""
            };
            tableModel.addRow(rowData);
        }

        taskTable.getColumn("Ações").setCellRenderer(new TarefaButtonRenderer());
        taskTable.getColumn("Ações").setCellEditor(new TarefaButtonEditor(taskTable, tarefaDAO));
    }
}