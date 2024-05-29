package com.mycompany.education.view;

import javax.swing.*;
import java.awt.*;

public class TelaCadastro extends JPanel {

    public TelaCadastro() {
        initComponents();
    }

    private void initComponents() {
        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField(20);

        JLabel sobrenomeLabel = new JLabel("Sobrenome:");
        JTextField sobrenomeField = new JTextField(20);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);

        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento (dd/mm/aaaa):");
        JTextField dataNascimentoField = new JTextField(20);

        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfField = new JTextField(20);

        JLabel senhaLabel = new JLabel("Senha:");
        JPasswordField senhaField = new JPasswordField(20);

        JLabel confirmacaoSenhaLabel = new JLabel("Confirmação de Senha:");
        JPasswordField confirmacaoSenhaField = new JPasswordField(20);

        JButton buttonSalvar = new JButton("Salvar");
        JButton buttonVoltar = new JButton("Voltar");

        buttonVoltar.addActionListener(evt -> {
            Container parent = getParent();
            if (parent != null && parent.getLayout() instanceof CardLayout) {
                CardLayout cl = (CardLayout) parent.getLayout();
                cl.show(parent, "Tela Inicial");
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nomeLabel, gbc);

        gbc.gridx = 1;
        add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(sobrenomeLabel, gbc);

        gbc.gridx = 1;
        add(sobrenomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(dataNascimentoLabel, gbc);

        gbc.gridx = 1;
        add(dataNascimentoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(cpfLabel, gbc);

        gbc.gridx = 1;
        add(cpfField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(senhaLabel, gbc);

        gbc.gridx = 1;
        add(senhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(confirmacaoSenhaLabel, gbc);

        gbc.gridx = 1;
        add(confirmacaoSenhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        add(buttonSalvar, gbc);

        gbc.gridx = 1;
        add(buttonVoltar, gbc);
    }
}
