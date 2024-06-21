package com.mycompany.education.views;

import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.components.professor.*;
import com.mycompany.education.models.EnvioTarefa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnvioTarefaDetalhesFrame extends JFrame {
    private JTable envioTable;
    private Long tarefaId;
    private EnvioTarefaDAO envioTarefaDAO;

    public EnvioTarefaDetalhesFrame(Long tarefaId, EnvioTarefaDAO envioTarefaDAO) {
        this.tarefaId = tarefaId;
        this.envioTarefaDAO = envioTarefaDAO;
        initComponents();
        carregarEnvios();
    }

    private void initComponents() {
        setTitle("Detalhes dos Envios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        envioTable = new JTable();
        envioTable.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Nome do Aluno", "Nome da Tarefa", "Valor", "Descrição", "Resposta", "Ações" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        });
        envioTable.setRowHeight(60);
        envioTable.getColumn("Ações").setCellRenderer(new AvaliarEnvioButtonRenderer());
        envioTable.getColumn("Ações").setCellEditor(new AvaliarEnvioButtonEditor(envioTable, envioTarefaDAO));

        add(new JScrollPane(envioTable), BorderLayout.CENTER);
    }

    private void carregarEnvios() {
        List<EnvioTarefa> envios = envioTarefaDAO.findByTarefaId(tarefaId);
        DefaultTableModel tableModel = (DefaultTableModel) envioTable.getModel();
        tableModel.setRowCount(0);
    
        for (EnvioTarefa envio : envios) {
            String nomeAluno = envio.aluno().nome() + " " + envio.aluno().sobrenome();
            String status = (envio.nota() != null) ? "Avaliado" : "Avaliar";
            Object[] rowData = {
                    nomeAluno,
                    envio.tarefa().titulo(),
                    (envio.nota() != null) ? envio.nota() : "",
                    envio.tarefa().descricao(),
                    envio.resposta(),
                    status
            };
            tableModel.addRow(rowData);
        }
    }
    
}
