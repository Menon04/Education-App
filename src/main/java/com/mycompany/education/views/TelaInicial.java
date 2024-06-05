package com.mycompany.education.views;

import com.mycompany.education.controllers.LoginController;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField inputEmail;
    private JPasswordField inputPassword;

    public TelaInicial() {
        initComponents();
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel inicialPanel = createInicialPanel();
        TelaCadastroUsuario cadastroPanel = new TelaCadastroUsuario();
        // TelaDashboard dashboardPanel = new TelaDashboard();

        mainPanel.add(inicialPanel, "Tela Inicial");
        mainPanel.add(cadastroPanel, "Tela de Cadastro");
        // mainPanel.add(dashboardPanel, "Tela de Dashboard");

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
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Tela Inicial", SwingConstants.CENTER);
        JLabel emailLabel = new JLabel("Email:");
        inputEmail = new JTextField(20); 
        JLabel passwordLabel = new JLabel("Senha:");
        inputPassword = new JPasswordField(20); 
        JButton buttonLogin = new JButton("Login");
        JButton buttonCadastrar = new JButton("Cadastrar");

        buttonCadastrar.addActionListener(e -> cardLayout.show(mainPanel, "Tela de Cadastro"));
        buttonLogin.addActionListener(e -> handleLogin());

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

    private void handleLogin() {
        String email = inputEmail.getText();
        String password = new String(inputPassword.getPassword()); 

        if (LoginController.validateLogin(email, password)) {
            JOptionPane.showMessageDialog(this, "Login efetuado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            // cardLayout.show(mainPanel, "Tela de Dashboard");
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}