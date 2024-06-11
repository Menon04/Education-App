package com.mycompany.education.views;

import com.mycompany.education.models.Curso;
import com.mycompany.education.models.Professor;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.services.CursoService;
import com.mycompany.education.session.UserSession;
import com.mycompany.education.models.Material;
import com.mycompany.education.models.Tarefa;
import com.mycompany.education.models.EnvioTarefa;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TelaCadastroCurso extends JFrame {
    private JTextField tituloField;
    private JTextArea descricaoField;
    private JButton cadastrarButton;
    private JButton voltarButton;
    private CursoService cursoService;
    private Usuario professor;

    public TelaCadastroCurso(Usuario professor) {
        this.professor = professor;
        this.cursoService = new CursoService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Cadastrar Curso");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1));

        panel.add(new JLabel("Título:"));
        tituloField = new JTextField();
        panel.add(tituloField);

        panel.add(new JLabel("Descrição:"));
        descricaoField = new JTextArea();
        panel.add(new JScrollPane(descricaoField));

        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(e -> cadastrarCurso());
        panel.add(cadastrarButton);

        voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(e -> voltar());
        panel.add(voltarButton);

        add(panel);
    }

    private void cadastrarCurso() {
        String titulo = tituloField.getText();
        String descricao = descricaoField.getText();

        if (titulo.isBlank() || descricao.isBlank()) {
            JOptionPane.showMessageDialog(this, "Título e Descrição são obrigatórios", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Usuario> alunosInscritos = new ArrayList<>();
        List<Material> materiais = new ArrayList<>();
        List<Tarefa> tarefas = new ArrayList<>();
        List<EnvioTarefa> enviosTarefas = new ArrayList<>();

        Curso curso = new Curso(null, titulo, descricao, professor, alunosInscritos, materiais, tarefas, enviosTarefas);
        cursoService.create(curso);

        JOptionPane.showMessageDialog(this, "Curso cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
        new ProfessorDashBoard(UserSession.getInstance(professor)).setVisible(true);
    }

    private void voltar() {
        this.dispose();
        new ProfessorDashBoard(UserSession.getInstance(professor)).setVisible(true);
    }

    public static void main(String[] args) {
        Usuario professor = new Professor(1L, "Prof. Nome", "Sobrenome", "email@example.com", LocalDate.now(), "123.456.789-00", "senha");
        SwingUtilities.invokeLater(() -> new TelaCadastroCurso(professor).setVisible(true));
    }
}
