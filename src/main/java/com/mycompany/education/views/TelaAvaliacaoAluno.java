package com.mycompany.education.views;

import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.dao.TarefaDAO;
import com.mycompany.education.models.Curso;
import com.mycompany.education.models.EnvioTarefa;
import com.mycompany.education.models.Tarefa;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.session.UserSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaAvaliacaoAluno extends JFrame {
    private JComboBox<Curso> cursoComboBox;
    private JComboBox<Tarefa> tarefaComboBox;
    private JComboBox<EnvioTarefa> enviosComboBox;
    private JTextArea comentarioArea;
    private JTextField notaField;
    private JButton salvarButton;
    private JButton cancelarButton;
    private CursoDAO cursoDAO;
    private TarefaDAO tarefaDAO;
    private EnvioTarefaDAO envioTarefaDAO;
    private Usuario professor;

    public TelaAvaliacaoAluno(Usuario professor) {
        this.cursoDAO = new CursoDAO();
        this.tarefaDAO = new TarefaDAO();
        this.envioTarefaDAO = new EnvioTarefaDAO();
        this.professor = professor;
        initComponents();
    }

    private void initComponents() {
        setTitle("Avaliar Aluno");
        setSize(400, 400);
        setLayout(new GridLayout(5, 2));

        cursoComboBox = new JComboBox<>();
        tarefaComboBox = new JComboBox<>();
        enviosComboBox = new JComboBox<>();
        notaField = new JTextField();
        comentarioArea = new JTextArea();
        salvarButton = new JButton("Salvar");
        cancelarButton = new JButton("Cancelar");

        salvarButton.addActionListener(e -> salvarAvaliacao());
        cancelarButton.addActionListener(e -> cancelar());

        add(new JLabel("Curso:"));
        add(cursoComboBox);
        add(new JLabel("Tarefa:"));
        add(tarefaComboBox);
        add(new JLabel("Envio:"));
        add(enviosComboBox);
        add(new JLabel("Nota:"));
        add(notaField);
        add(new JLabel("Comentário:"));
        add(new JScrollPane(comentarioArea));
        add(salvarButton);
        add(cancelarButton);

        cursoComboBox.addActionListener(e -> carregarTarefas());
        tarefaComboBox.addActionListener(e -> carregarEnvios());
        enviosComboBox.addActionListener(e -> carregarDetalhesEnvio());

        carregarCursos();
    }

    private void carregarCursos() {
        List<Curso> cursos = cursoDAO.findAll();
        cursoComboBox.removeAllItems();
        for (Curso curso : cursos) {
            cursoComboBox.addItem(curso);
        }
    }

    private void carregarTarefas() {
        Curso curso = (Curso) cursoComboBox.getSelectedItem();
        if (curso != null) {
            List<Tarefa> tarefas = tarefaDAO.findAllByCurso(curso.id());
            tarefaComboBox.removeAllItems();
            for (Tarefa tarefa : tarefas) {
                tarefaComboBox.addItem(tarefa);
            }
        }
    }

    private void carregarEnvios() {
        Tarefa tarefa = (Tarefa) tarefaComboBox.getSelectedItem();
        if (tarefa != null) {
            List<EnvioTarefa> envios = envioTarefaDAO.findAllByTarefa(tarefa.id());
            enviosComboBox.removeAllItems();
            for (EnvioTarefa envio : envios) {
                enviosComboBox.addItem(envio);
            }
        }
    }

    private void carregarDetalhesEnvio() {
        EnvioTarefa envio = (EnvioTarefa) enviosComboBox.getSelectedItem();
        if (envio != null) {
            notaField.setText(envio.nota() != null ? envio.nota().toString() : "");
        }
    }

    private void salvarAvaliacao() {
        EnvioTarefa envio = (EnvioTarefa) enviosComboBox.getSelectedItem();
        String nota = notaField.getText();

        if (envio == null || nota.isBlank()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double notaValue = Double.parseDouble(nota);
            EnvioTarefa updatedEnvio = new EnvioTarefa(envio.id(), envio.aluno(), envio.tarefa(), envio.resposta(), envio.dataEnvio(), notaValue);
            envioTarefaDAO.update(updatedEnvio);

            JOptionPane.showMessageDialog(this, "Avaliação salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            new ProfessorDashBoard(UserSession.getInstance(professor)).setVisible(true);
            this.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nota deve ser um número válido", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        new ProfessorDashBoard(UserSession.getInstance(professor)).setVisible(true);
        this.dispose();
    }
}
