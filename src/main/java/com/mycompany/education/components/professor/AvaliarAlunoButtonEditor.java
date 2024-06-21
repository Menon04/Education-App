package com.mycompany.education.components.professor;

import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.models.EnvioTarefa;
import com.mycompany.education.views.AvaliarEnvioFrame;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableModel;
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
        Long envioId = (Long) table.getValueAt(row, 0); // Assuming the ID is in the first column
        EnvioTarefa envio = envioTarefaDAO.findById(envioId);

        if (envio != null) {
            AvaliarEnvioFrame avaliarFrame = new AvaliarEnvioFrame(envioTarefaDAO, envio);
            avaliarFrame.setVisible(true);
            avaliarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    EnvioTarefa updatedEnvio = envioTarefaDAO.findById(envioId);
                    updateTableRow(row, updatedEnvio);
                }
            });
        } else {
            JOptionPane.showMessageDialog(table, "Nenhum envio encontrado com este ID.");
        }
        fireEditingStopped();
    }

    private void updateTableRow(int row, EnvioTarefa envio) {
        table.setValueAt(envio.nota() != null ? envio.nota().toString() : "N/A", row, 4);
        table.setValueAt(envio.nota() != null ? "Avaliado" : "Avaliar", row, 5);
        ((DefaultTableModel) table.getModel()).fireTableRowsUpdated(row, row);
    }
}
