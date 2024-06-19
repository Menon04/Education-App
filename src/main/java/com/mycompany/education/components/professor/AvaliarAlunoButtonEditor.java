package com.mycompany.education.components.professor;

import com.mycompany.education.dao.EnvioTarefaDAO;
import com.mycompany.education.views.EnvioTarefaDetalhesFrame;

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
        button = new JButton("Detalhes");
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        Long tarefaId = (Long) table.getValueAt(row, 0);
        new EnvioTarefaDetalhesFrame(tarefaId, envioTarefaDAO).setVisible(true);
        fireEditingStopped();
    }
}