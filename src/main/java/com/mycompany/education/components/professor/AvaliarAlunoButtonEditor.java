package com.mycompany.education.components.professor;

import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.models.EnvioTarefa;
import com.mycompany.education.views.AvaliarEnvioFrame;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AvaliarAlunoButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private JTable table;
    private EnvioTarefaDAO envioTarefaDAO;

    public AvaliarAlunoButtonEditor(JTable table, EnvioTarefaDAO envioTarefaDAO) {
        this.table = table;
        this.envioTarefaDAO = envioTarefaDAO;
        button = new JButton("Avaliar");
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String status = (value != null) ? value.toString() : "Avaliar";
        button.setText(status);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        Long envioId = (Long) table.getValueAt(row, 0);
        EnvioTarefa envio = envioTarefaDAO.findById(envioId);

        if (envio != null) {
            new AvaliarEnvioFrame(envioTarefaDAO, envio).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(table, "Nenhum envio encontrado com este ID.");
        }
        fireEditingStopped();
    }
}
