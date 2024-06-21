package com.mycompany.education.views;

import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.models.EnvioTarefa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AvaliarEnvioFrame extends JFrame {
    private EnvioTarefaDAO envioTarefaDAO;
    private EnvioTarefa envioTarefa;
    private JTextField notaField;
    private JTextArea respostaArea;
    private JButton salvarButton;
    private JButton cancelarButton;

    public AvaliarEnvioFrame(EnvioTarefaDAO envioTarefaDAO, EnvioTarefa envioTarefa) {
        this.envioTarefaDAO = envioTarefaDAO;
        this.envioTarefa = envioTarefa;
        initComponents();
    }

    private void initComponents() {
        setTitle("Avaliar Envio");
        setSize(400, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel respostaLabel = new JLabel("Resposta do Aluno:");
        respostaArea = new JTextArea(envioTarefa.resposta());
        respostaArea.setEditable(false);
        respostaArea.setLineWrap(true);
        respostaArea.setWrapStyleWord(true);
        JScrollPane respostaScrollPane = new JScrollPane(respostaArea);
        respostaScrollPane.setPreferredSize(new Dimension(380, 150));

        JLabel notaLabel = new JLabel("Nota (máximo " + envioTarefa.tarefa().nota() + "):");
        notaField = new JTextField();
        if (envioTarefa.nota() != null) {
            notaField.setText(envioTarefa.nota().toString());
        }
        notaField.setMaximumSize(new Dimension(Integer.MAX_VALUE, notaField.getPreferredSize().height));

        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarAvaliacao();
            }
        });

        cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        contentPanel.add(respostaLabel);
        contentPanel.add(respostaScrollPane);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(notaLabel);
        contentPanel.add(notaField);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void salvarAvaliacao() {
        try {
            Double nota = Double.parseDouble(notaField.getText());
            Double notaMaxima = envioTarefa.tarefa().nota();
            if (nota < 0 || nota > notaMaxima) {
                throw new NumberFormatException("Nota deve estar entre 0 e " + notaMaxima);
            }
            EnvioTarefa novoEnvio = new EnvioTarefa(envioTarefa.id(), envioTarefa.aluno(), envioTarefa.tarefa(),
                    envioTarefa.resposta(), envioTarefa.dataEnvio(), nota);
            envioTarefaDAO.update(novoEnvio);
            JOptionPane.showMessageDialog(this, "Avaliação salva com sucesso.");
            dispose();
        } catch (NumberFormatException e) {
            Double notaMaxima = envioTarefa.tarefa().nota();
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor válido para a nota (0-" + notaMaxima + ").");
        }
    }
}
