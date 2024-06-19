package com.mycompany.education.views;

import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.models.EnvioTarefa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnvioTarefaDetalhesFrame extends JFrame {
    private JTable envioTable;
    private EnvioTarefaDAO envioTarefaDAO;
    private Long tarefaId;

    public EnvioTarefaDetalhesFrame(Long tarefaId, EnvioTarefaDAO envioTarefaDAO) {
        this.tarefaId = tarefaId;
        this.envioTarefaDAO = envioTarefaDAO;
        initComponents();
        carregarEnvios();
    }

    private void initComponents() {
        setTitle("Detalhes dos Envios da Tarefa");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        envioTable = new JTable();
        envioTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Aluno", "Resposta", "Data de Envio", "Nota"}
        ));
        envioTable.setRowHeight(30);

        setLayout(new BorderLayout());
        add(new JScrollPane(envioTable), BorderLayout.CENTER);
    }

    private void carregarEnvios() {
        List<EnvioTarefa> envios = envioTarefaDAO.findAllByTarefa(tarefaId);
        DefaultTableModel tableModel = (DefaultTableModel) envioTable.getModel();
        tableModel.setRowCount(0);

        for (EnvioTarefa envio : envios) {
            Object[] rowData = {
                    envio.id(),
                    envio.aluno().nome() + " " + envio.aluno().sobrenome(),
                    envio.resposta(),
                    envio.dataEnvio(),
                    envio.nota()
            };
            tableModel.addRow(rowData);
        }
    }
}