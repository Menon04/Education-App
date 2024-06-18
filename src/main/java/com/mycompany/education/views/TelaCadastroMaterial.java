package com.mycompany.education.views;

import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.dao.MaterialDAO;
import com.mycompany.education.models.Curso;
import com.mycompany.education.models.Material;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.session.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class TelaCadastroMaterial extends JFrame {
    private JTextField tituloField;
    private JTextArea descricaoArea;
    private JComboBox<String> cursoComboBox;
    private JButton salvarButton;
    private JButton cancelarButton;
    private MaterialDAO materialDAO;
    private CursoDAO cursoDAO;
    private Usuario professor;

    public TelaCadastroMaterial(Usuario professor) {
        this.professor = professor;
        this.materialDAO = new MaterialDAO();
        this.cursoDAO = new CursoDAO();
        initComponents();
        loadCursos();
    }

    private void initComponents() {
        setTitle("Cadastrar Material");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel tituloLabel = new JLabel("Título:");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tituloField = new JTextField(20);

        JLabel descricaoLabel = new JLabel("Descrição:");
        descricaoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descricaoArea = new JTextArea(5, 20);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        JScrollPane descricaoScrollPane = new JScrollPane(descricaoArea);

        JLabel cursoLabel = new JLabel("Curso:");
        cursoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cursoComboBox = new JComboBox<>();

        salvarButton = new JButton("Salvar");
        salvarButton.setFont(new Font("Arial", Font.BOLD, 12));
        salvarButton.setBackground(new Color(0, 153, 76));
        salvarButton.setForeground(Color.WHITE);
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarMaterial();
            }
        });

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelarButton.setBackground(new Color(204, 0, 0));
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tituloLabel)
                        .addComponent(descricaoLabel)
                        .addComponent(cursoLabel))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tituloField)
                        .addComponent(descricaoScrollPane)
                        .addComponent(cursoComboBox)))
                .addComponent(buttonPanel)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(tituloLabel)
                    .addComponent(tituloField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(descricaoLabel)
                    .addComponent(descricaoScrollPane))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cursoLabel)
                    .addComponent(cursoComboBox))
                .addComponent(buttonPanel)
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void loadCursos() {
        List<Curso> cursos = cursoDAO.findCursosByProfessorId(professor.id());
        for (Curso curso : cursos) {
            cursoComboBox.addItem(curso.titulo());
        }
    }

    private void salvarMaterial() {
        String titulo = tituloField.getText();
        String descricao = descricaoArea.getText();
        String cursoSelecionado = (String) cursoComboBox.getSelectedItem();

        if (titulo.isBlank() || descricao.isBlank() || cursoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Curso curso = cursoDAO.findCursoByTitulo(cursoSelecionado);
        Material material = new Material(null, titulo, descricao, LocalDate.now(), professor.id(), curso.id());
        materialDAO.create(material);
        JOptionPane.showMessageDialog(this, "Material salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        new ProfessorDashBoard(UserSession.getInstance(professor)).setVisible(true);
        this.dispose();
    }

    private void cancelar() {
        new ProfessorDashBoard(UserSession.getInstance(professor)).setVisible(true);
        this.dispose();
    }
}
