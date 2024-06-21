package com.mycompany.education.views;

import com.mycompany.education.components.aluno.CursoButtonEditor;
import com.mycompany.education.components.aluno.CursoButtonRenderer;
import com.mycompany.education.components.aluno.TarefaButtonEditor;
import com.mycompany.education.components.aluno.TarefaButtonRenderer;
import com.mycompany.education.dao.*;
import com.mycompany.education.models.*;
import com.mycompany.education.session.UserSession;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class AlunoDashBoard extends JFrame {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable courseTable;
    private JTable materialTable;
    private JTable taskTable;
    private JTable gradeTable;
    private JButton logoutButton;
    private UserSession userSession;
    private CursoDAO cursoDAO;
    private MaterialDAO materialDAO;
    private UsuarioDAO usuarioDAO;
    private TarefaDAO tarefaDAO;
    private EnvioTarefaDAO envioTarefaDAO;

    public AlunoDashBoard(UserSession userSession) {
        this.userSession = userSession;
        this.cursoDAO = new CursoDAO();
        this.materialDAO = new MaterialDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.tarefaDAO = new TarefaDAO();
        this.envioTarefaDAO = new EnvioTarefaDAO();
        initComponents();
        addListeners();
        loadCourses();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        JPanel coursePanel = new JPanel(new BorderLayout());
        courseTable = new JTable();
        coursePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        tabbedPane.add("Cursos", coursePanel);

        JPanel materialPanel = new JPanel(new BorderLayout());
        materialTable = new JTable();
        materialPanel.add(new JScrollPane(materialTable), BorderLayout.CENTER);
        tabbedPane.add("Materiais", materialPanel);

        JPanel taskPanel = new JPanel(new BorderLayout());
        taskTable = new JTable();
        taskPanel.add(new JScrollPane(taskTable), BorderLayout.CENTER);
        tabbedPane.add("Tarefas", taskPanel);

        JPanel gradePanel = new JPanel(new BorderLayout());
        gradeTable = new JTable();
        gradePanel.add(new JScrollPane(gradeTable), BorderLayout.CENTER);
        tabbedPane.add("Avaliações", gradePanel);

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

        setTitle("Aluno Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(mainPanel);
    }

    private void addListeners() {
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex) {
                    case 0:
                        loadCourses();
                        break;
                    case 1:
                        loadMaterials();
                        break;
                    case 2:
                        loadTasks();
                        break;
                    case 3:
                        loadGrades();
                        break;
                }
            }
        });
    }

    private void loadCourses() {
        List<Curso> cursos = cursoDAO.findAll();
        DefaultTableModel model = new DefaultTableModel(new Object[] { "Curso", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        for (Curso curso : cursos) {
            boolean isInscrito = curso.alunosInscritos().stream()
                    .anyMatch(aluno -> aluno.id().equals(userSession.user().id()));
            String actionText = isInscrito ? "Inscrito" : "Inscrever-se";
            model.addRow(new Object[] { curso.titulo(), actionText });
        }

        courseTable.setModel(model);

        TableColumnModel columnModel = courseTable.getColumnModel();
        columnModel.getColumn(1).setCellRenderer(new CursoButtonRenderer());
        columnModel.getColumn(1).setCellEditor(new CursoButtonEditor(this));
    }

    public void handleButtonAction(int row, int column) {
        if (column == 1) {
            Curso curso = cursoDAO.findAll().get(row);
            if (curso.alunosInscritos().stream().anyMatch(aluno -> aluno.id().equals(userSession.user().id()))) {
                JOptionPane.showMessageDialog(this, "Você já está inscrito neste curso!");
            } else {
                inscreverNoCurso(curso);
            }
        }
    }

    private void inscreverNoCurso(Curso curso) {
        cursoDAO.inscreverAluno(curso, userSession.user());
        loadCourses();
    }

    private void loadGrades() {
        List<EnvioTarefa> envios = envioTarefaDAO.findAllByAluno(userSession.user().id());
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Curso", "Tarefa", "Valor", "Nota" }, 0);

        for (EnvioTarefa envio : envios) {
            Tarefa tarefa = envio.tarefa();
            Curso curso = cursoDAO.findById(tarefa.cursoId());
            model.addRow(new Object[] { curso.titulo(), tarefa.titulo(), tarefa.nota(), envio.nota() });
        }

        gradeTable.setModel(model);
    }

    private void loadTasks() {
        List<Curso> cursos = cursoDAO.findAll();
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Título da Tarefa", "Descrição", "Valor", "Data de Entrega", "Nome do Curso", "Status" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        for (Curso curso : cursos) {
            if (curso.alunosInscritos().stream().anyMatch(aluno -> aluno.id().equals(userSession.user().id()))) {
                List<Tarefa> tarefas = tarefaDAO.findAllByCurso(curso.id());
                for (Tarefa tarefa : tarefas) {
                    String status = "A Fazer";
                    EnvioTarefa envioTarefa = envioTarefaDAO.findByAlunoAndTarefa(userSession.user().id(), tarefa.id());
                    if (envioTarefa != null) {
                        status = "Feito";
                    }
                    model.addRow(new Object[] {
                            tarefa.titulo(),
                            tarefa.descricao(),
                            tarefa.nota(),
                            tarefa.dataEntrega(),
                            curso.titulo(),
                            status
                    });
                }
            }
        }

        taskTable.setModel(model);

        TableColumnModel columnModel = taskTable.getColumnModel();
        columnModel.getColumn(5).setCellRenderer(new TarefaButtonRenderer());
        columnModel.getColumn(5).setCellEditor(new TarefaButtonEditor(userSession.user(), taskTable, this));
    }

    private void loadMaterials() {
        List<Material> materiais = materialDAO.findAllMaterialsByStudent(userSession.user().id());
        DefaultTableModel model = new DefaultTableModel(new Object[] { "Curso", "Professor", "Título", "Conteúdo" }, 0);

        for (Material material : materiais) {
            Curso curso = cursoDAO.findById(material.cursoId());
            Usuario professor = usuarioDAO.findById(material.professorId());
            model.addRow(new Object[] { curso.titulo(), professor.nome(), material.titulo(), material.conteudo() });
        }

        materialTable.setModel(model);
    }

    public void updateTaskTable() {
        loadTasks();
    }

    private void logout() {
        userSession.clearSession();
        new TelaInicial().setVisible(true);
        this.dispose();
    }
}
