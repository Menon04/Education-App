package com.mycompany.education.views;

import com.mycompany.education.exceptions.LoginException;
import com.mycompany.education.services.LoginService;
import com.mycompany.education.models.Admin;
import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Professor;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.session.UserSession;

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

        try {
            Usuario usuario = LoginService.searchUser(email, password);
            if (usuario != null) {
                UserSession userSession = UserSession.getInstance(usuario);
                JOptionPane.showMessageDialog(this, "Login efetuado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                navigateToDashboard(userSession);
            } else {
                JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (LoginException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void navigateToDashboard(UserSession session) {
        if (session.user() instanceof Aluno) {
            new AlunoDashBoard(session).setVisible(true);
        } else if (session.user() instanceof Professor) {
            new ProfessorDashBoard(session).setVisible(true);
        } else if (session.user() instanceof Admin) {
            new AdminDashBoard(session).setVisible(true);
        }
        this.dispose();
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}
