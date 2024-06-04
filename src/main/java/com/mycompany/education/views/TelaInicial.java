package com.mycompany.education.views;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public TelaInicial() {
        initComponents();
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel inicialPanel = createInicialPanel();
        TelaCadastroUsuario cadastroPanel = new TelaCadastroUsuario();

        mainPanel.add(inicialPanel, "Tela Inicial");
        mainPanel.add(cadastroPanel, "Tela de Cadastro");

        add(mainPanel);

        setTitle("Aplicativo com CardLayout");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createInicialPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Tela Inicial", SwingConstants.CENTER);
        JLabel emailLabel = new JLabel("Email:");
        JTextField inputEmail = new JTextField(20);
        JLabel passwordLabel = new JLabel("Senha:");
        JPasswordField inputPassword = new JPasswordField(20);
        JButton buttonLogin = new JButton("Login");
        JButton buttonCadastrar = new JButton("Cadastrar");

        buttonCadastrar.addActionListener(e -> cardLayout.show(mainPanel, "Tela de Cadastro"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(inputEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(inputPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(buttonLogin, gbc);

        gbc.gridx = 1;
        panel.add(buttonCadastrar, gbc);

        return panel;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}