package com.mycompany.education.views;

import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.models.EnvioTarefa;
import com.mycompany.education.models.Tarefa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class TarefaDetailsView extends JFrame {
    private Tarefa tarefa;
    private Usuario aluno;
    private EnvioTarefaDAO envioTarefaDAO;
    private EnvioTarefa envioTarefa;
    private AlunoDashBoard dashboard;

    private JTextArea respostaTextArea;
    private JButton enviarButton;

    public TarefaDetailsView(Tarefa tarefa, Usuario aluno, AlunoDashBoard dashboard) {
        this.tarefa = tarefa;
        this.aluno = aluno;
        this.dashboard = dashboard;
        this.envioTarefaDAO = new EnvioTarefaDAO();

        this.envioTarefa = envioTarefaDAO.findAllByAluno(aluno.id()).stream()
                .filter(e -> e.tarefa().id().equals(tarefa.id()))
                .findFirst()
                .orElse(null);

        initComponents();
    }

    private void initComponents() {
        setTitle("Detalhes da Tarefa");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel tituloLabel = new JLabel("Tarefa: " + tarefa.titulo());
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        mainPanel.add(tituloLabel, gbc);

        JTextArea descricaoTextArea = new JTextArea(tarefa.descricao());
        descricaoTextArea.setEditable(false);
        descricaoTextArea.setWrapStyleWord(true);
        descricaoTextArea.setLineWrap(true);
        descricaoTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descricaoTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane descricaoScrollPane = new JScrollPane(descricaoTextArea);
        gbc.gridy = 1;
        gbc.weighty = 0.3;  // Aumentar a altura do campo de descrição
        mainPanel.add(descricaoScrollPane, gbc);

        JLabel dataEntregaLabel = new JLabel("Data de Entrega: " + tarefa.dataEntrega());
        dataEntregaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        mainPanel.add(dataEntregaLabel, gbc);

        JLabel respostaLabel = new JLabel("Resposta do Aluno:");
        respostaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(respostaLabel, gbc);

        respostaTextArea = new JTextArea();
        respostaTextArea.setWrapStyleWord(true);
        respostaTextArea.setLineWrap(true);
        respostaTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        respostaTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane respostaScrollPane = new JScrollPane(respostaTextArea);
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0.5;  // Aumentar a altura do campo de resposta
        mainPanel.add(respostaScrollPane, gbc);

        if (envioTarefa != null) {
            respostaTextArea.setText(envioTarefa.resposta());
        }

        enviarButton = new JButton(envioTarefa == null ? "Enviar" : "Editar Envio");
        enviarButton.setFont(new Font("Arial", Font.BOLD, 14));
        enviarButton.setBackground(new Color(0, 123, 255));
        enviarButton.setForeground(Color.WHITE);
        enviarButton.setFocusPainted(false);
        enviarButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        gbc.gridy = 5;
        gbc.weighty = 0;
        mainPanel.add(enviarButton, gbc);

        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEnvio();
            }
        });

        add(mainPanel);
    }

    private void handleEnvio() {
        String resposta = respostaTextArea.getText();
        if (resposta.isBlank()) {
            JOptionPane.showMessageDialog(this, "Resposta não pode ser vazia.");
            return;
        }

        if (envioTarefa == null) {
            envioTarefa = new EnvioTarefa(null, aluno, tarefa, resposta, LocalDate.now(), null);
            envioTarefaDAO.create(envioTarefa);
        } else {
            if (LocalDate.now().isAfter(tarefa.dataEntrega())) {
                JOptionPane.showMessageDialog(this, "Tarefa finalizada. Não é possível editar.");
                return;
            }
            envioTarefa = new EnvioTarefa(envioTarefa.id(), aluno, tarefa, resposta, LocalDate.now(), null);
            envioTarefaDAO.update(envioTarefa);
        }

        JOptionPane.showMessageDialog(this, "Envio realizado com sucesso!");
        dashboard.updateTaskTable();
        this.dispose();
    }
}
