package com.mycompany.education.views;

import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.dao.TarefaDAO;
import com.mycompany.education.models.Curso;
import com.mycompany.education.models.Tarefa;
import com.mycompany.education.session.UserSession;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelaCadastroTarefa extends JFrame {
    private UserSession userSession;
    private JTextField tituloField;
    private JTextArea descricaoArea;
    private JFormattedTextField dataEntregaField;
    private JTextField notaField;
    private JComboBox<String> cursoComboBox;
    private JButton salvarButton;
    private JButton cancelarButton;
    private TarefaDAO tarefaDAO;
    private CursoDAO cursoDAO;

    public TelaCadastroTarefa(UserSession userSession) {
        this.userSession = userSession;
        this.tarefaDAO = new TarefaDAO();
        this.cursoDAO = new CursoDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Cadastro de Tarefa");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel, BorderLayout.CENTER);

        // Título
        mainPanel.add(new JLabel("Título:"));
        tituloField = new JTextField();
        mainPanel.add(tituloField);

        // Descrição
        mainPanel.add(new JLabel("Descrição:"));
        descricaoArea = new JTextArea(5, 20);
        descricaoArea.setWrapStyleWord(true);
        descricaoArea.setLineWrap(true);
        mainPanel.add(new JScrollPane(descricaoArea));

        // Data de Entrega
        mainPanel.add(new JLabel("Data de Entrega:"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataEntregaField = new JFormattedTextField(new DateFormatter(dateFormat));
        dataEntregaField.setValue(new Date());
        mainPanel.add(dataEntregaField);

        // Nota
        mainPanel.add(new JLabel("Nota:"));
        notaField = new JTextField();
        mainPanel.add(notaField);

        // Curso
        mainPanel.add(new JLabel("Curso:"));
        cursoComboBox = new JComboBox<>();
        List<Curso> cursos = cursoDAO.findAll();
        for (Curso curso : cursos) {
            cursoComboBox.addItem(curso.titulo());
        }
        mainPanel.add(cursoComboBox);

        // Botões
        JPanel buttonPanel = new JPanel();
        salvarButton = new JButton("Salvar");
        salvarButton.setBackground(new Color(0, 123, 255));
        salvarButton.setForeground(Color.WHITE);
        salvarButton.addActionListener(e -> salvarTarefa());
        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBackground(new Color(220, 53, 69));
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.addActionListener(e -> cancelar());
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void salvarTarefa() {
        String titulo = tituloField.getText();
        String descricao = descricaoArea.getText();
        LocalDate dataEntrega;
        try {
            dataEntrega = LocalDate.parse(dataEntregaField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data de entrega inválida. Use o formato dd/MM/yyyy.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double nota;
        try {
            nota = Double.parseDouble(notaField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nota inválida. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nomeCurso = (String) cursoComboBox.getSelectedItem();
        Curso curso = cursoDAO.findCursoByTitulo(nomeCurso);

        if (curso == null) {
            JOptionPane.showMessageDialog(this, "Selecione um curso.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Tarefa novaTarefa = new Tarefa(null, titulo, descricao, nota, dataEntrega, LocalDate.now(), curso.id());
            tarefaDAO.create(novaTarefa);
            JOptionPane.showMessageDialog(this, "Tarefa criada com sucesso.", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            new ProfessorDashBoard(userSession).setVisible(true);
            dispose();
        } catch (Exception ex) {
            Logger.getLogger(TelaCadastroTarefa.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Erro ao salvar a tarefa.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        new ProfessorDashBoard(userSession).setVisible(true);
        dispose();
    }
}
