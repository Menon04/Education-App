package com.mycompany.education.views;

import javax.swing.*;

import com.mycompany.education.controllers.CadastroController;
import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Professor;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.utils.HashPassword;
import com.mycompany.education.utils.ValidateData;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TelaCadastroUsuario extends JPanel {
    private JComboBox<String> tipoUsuarioComboBox;
    private JTextField nomeField;
    private JTextField sobrenomeField;
    private JTextField emailField;
    private JTextField dataNascimentoField;
    private JTextField cpfField;
    private JPasswordField senhaField;
    private JPasswordField confirmacaoSenhaField;

    public TelaCadastroUsuario() {
        initComponents();
    }

    private void initComponents() {
        JLabel tipoUsuarioLabel = new JLabel("Tipo de Usuário:");
        tipoUsuarioComboBox = new JComboBox<>(new String[] { "Aluno", "Professor" });

        JLabel nomeLabel = new JLabel("Nome:");
        nomeField = new JTextField(20);

        JLabel sobrenomeLabel = new JLabel("Sobrenome:");
        sobrenomeField = new JTextField(20);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);

        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento (dd/mm/aaaa):");
        dataNascimentoField = new JTextField(20);

        JLabel cpfLabel = new JLabel("CPF:");
        cpfField = new JTextField(20);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaField = new JPasswordField(20);

        JLabel confirmacaoSenhaLabel = new JLabel("Confirmação de Senha:");
        confirmacaoSenhaField = new JPasswordField(20);

        JButton buttonSalvar = new JButton("Salvar");
        JButton buttonVoltar = new JButton("Voltar");

        buttonVoltar.addActionListener(evt -> voltarParaTelaInicial());

        buttonSalvar.addActionListener(evt -> salvarCadastroUsuario());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(tipoUsuarioLabel, gbc);

        gbc.gridx = 1;
        add(tipoUsuarioComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nomeLabel, gbc);

        gbc.gridx = 1;
        add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(sobrenomeLabel, gbc);

        gbc.gridx = 1;
        add(sobrenomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(dataNascimentoLabel, gbc);

        gbc.gridx = 1;
        add(dataNascimentoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(cpfLabel, gbc);

        gbc.gridx = 1;
        add(cpfField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(senhaLabel, gbc);

        gbc.gridx = 1;
        add(senhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(confirmacaoSenhaLabel, gbc);

        gbc.gridx = 1;
        add(confirmacaoSenhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        add(buttonSalvar, gbc);

        gbc.gridx = 1;
        add(buttonVoltar, gbc);
    }

    private void salvarCadastroUsuario() {
        Map<String, String> validationErrors = ValidateData.validateFields(nomeField, sobrenomeField, emailField,
                dataNascimentoField, cpfField, senhaField, confirmacaoSenhaField);

        if (validationErrors.isEmpty()) {
            Usuario usuario = criarUsuario();
            try {
                CadastroController.salvarCadastroUsuario(usuario);
                JOptionPane.showMessageDialog(this, "Cadastro salvo com sucesso!");
                limparCampos();
                voltarParaTelaInicial();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao salvar cadastro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            mostrarErrosDeValidacao(validationErrors);
        }
    }

    private Usuario criarUsuario() {
        String tipoUsuario = (String) tipoUsuarioComboBox.getSelectedItem();
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoField.getText(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String nome = nomeField.getText();
        String sobrenome = sobrenomeField.getText();
        String email = emailField.getText();
        String cpf = cpfField.getText();
        String hashedSenha = HashPassword.hashPassword(new String(senhaField.getPassword()));

        if ("Aluno".equals(tipoUsuario)) {
            return new Aluno(null, nome, sobrenome, email, dataNascimento, cpf, hashedSenha);
        } else {
            return new Professor(null, nome, sobrenome, email, dataNascimento, cpf, hashedSenha);
        }
    }

    private void mostrarErrosDeValidacao(Map<String, String> validationErrors) {
        StringBuilder errorMessage = new StringBuilder("Erro ao salvar cadastro:\n");
        for (String message : validationErrors.values()) {
            errorMessage.append(message).append("\n");
        }
        JOptionPane.showMessageDialog(this, errorMessage.toString(), "Erro de Validação",
                JOptionPane.ERROR_MESSAGE);
    }

    private void voltarParaTelaInicial() {
        Container parent = getParent();
        if (parent != null && parent.getLayout() instanceof CardLayout) {
            CardLayout cl = (CardLayout) parent.getLayout();
            cl.show(parent, "Tela Inicial");
        }
    }

    private void limparCampos() {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setText("");
            } else if (component instanceof JPasswordField) {
                JPasswordField passwordField = (JPasswordField) component;
                passwordField.setText("");
            }
        }
    }
}
