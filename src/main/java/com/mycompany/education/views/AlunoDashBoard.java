package com.mycompany.education.views;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import com.mycompany.education.session.UserSession;
import com.mycompany.education.models.Curso;
import com.mycompany.education.models.Material;
import com.mycompany.education.models.Tarefa;
import com.mycompany.education.services.CursoService;
import com.mycompany.education.models.EnvioTarefa;

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
    private CursoService cursoService;

    public AlunoDashBoard(UserSession userSession) {
        this.userSession = userSession;
        this.cursoService = new CursoService();
        initComponents();
        addListeners();
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
        List<Curso> courses = cursoService.findAllCourses();
        DefaultTableModel model = new DefaultTableModel(new Object[] { "ID", "Título", "Descrição", "Professor" }, 0);
        for (Curso curso : courses) {
            model.addRow(new Object[] { curso.id(), curso.titulo(), curso.descricao(), curso.professor().nome() });
        }
        courseTable.setModel(model);
    }
    
    private void loadGrades() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadGrades'");
    }

    private void loadTasks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadTasks'");
    }

    private void loadMaterials() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadMaterials'");
    }


    private void logout() {
        userSession.clearSession();
        new TelaInicial().setVisible(true);
        this.dispose();
    }

    // public static void main(String[] args) {
    // SwingUtilities.invokeLater(() -> new AlunoDashBoard(new
    // UserSession()).setVisible(true));
    // }
}
