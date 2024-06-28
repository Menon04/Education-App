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
    private Usuario usuario;

    public TelaCadastroCurso(Usuario usuario) {
        this.usuario = usuario;
        this.cursoService = new CursoService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Cadastrar Curso");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título label
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Curso:"), gbc);

        // Título field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        tituloField = new JTextField(20);
        panel.add(tituloField, gbc);

        // Descrição label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Descrição:"), gbc);

        // Descrição field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        descricaoField = new JTextArea(5, 20);
        panel.add(new JScrollPane(descricaoField), gbc);

        // Botão cadastrar
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(e -> cadastrarCurso());
        panel.add(cadastrarButton, gbc);

        // Botão voltar
        gbc.gridx = 2;
        gbc.gridy = 2;
        voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(e -> voltar());
        panel.add(voltarButton, gbc);

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

        Curso curso = new Curso(null, titulo, descricao, usuario, alunosInscritos, materiais, tarefas, enviosTarefas);
        cursoService.create(curso);

        JOptionPane.showMessageDialog(this, "Curso cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);


        if (usuario instanceof Professor) {
            new ProfessorDashBoard(UserSession.getInstance(usuario)).setVisible(true);
        } else {
            new AdminDashBoard(UserSession.getInstance(usuario)).setVisible(true);
        }
        this.dispose();
    }

    private void voltar() {
        if (usuario instanceof Professor) {
            new ProfessorDashBoard(UserSession.getInstance(usuario)).setVisible(true);
        } else {
            new AdminDashBoard(UserSession.getInstance(usuario)).setVisible(true);
        }
        this.dispose();
    }

    public static void main(String[] args) {
        Usuario professor = new Professor(1L, "Prof. Nome", "Sobrenome", "email@example.com", LocalDate.now(), "123.456.789-00", "senha");
        SwingUtilities.invokeLater(() -> new TelaCadastroCurso(professor).setVisible(true));
    }
}
