package com.mycompany.education.components.professor;

import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.models.EnvioTarefa;
import com.mycompany.education.views.AvaliarEnvioFrame;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AvaliarEnvioButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private JTable table;
    private EnvioTarefaDAO envioTarefaDAO;

    public AvaliarEnvioButtonEditor(JTable table, EnvioTarefaDAO envioTarefaDAO) {
        this.table = table;
        this.envioTarefaDAO = envioTarefaDAO;
        button = new JButton("Avaliar");
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText((value != null) ? value.toString() : "Avaliar");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        EnvioTarefa envio = (EnvioTarefa) table.getValueAt(row, 0);
        new AvaliarEnvioFrame(envioTarefaDAO, envio).setVisible(true);
        fireEditingStopped();
    }
}
