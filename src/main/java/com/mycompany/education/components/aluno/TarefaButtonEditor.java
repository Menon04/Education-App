package com.mycompany.education.components.aluno;

import com.mycompany.education.models.Usuario;
import com.mycompany.education.dao.TarefaDAO;
import com.mycompany.education.models.Tarefa;
import com.mycompany.education.views.AlunoDashBoard;
import com.mycompany.education.views.TarefaDetailsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TarefaButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private Usuario usuario;
    private TarefaDAO tarefaDAO;
    private JTable taskTable;
    private AlunoDashBoard alunoDashBoard;

    public TarefaButtonEditor(Usuario usuario, JTable taskTable, AlunoDashBoard alunoDashBoard) {
        super(new JCheckBox());
        this.usuario = usuario;
        this.taskTable = taskTable;
        this.tarefaDAO = new TarefaDAO();
        this.alunoDashBoard = alunoDashBoard;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow >= 0) {
                int row = taskTable.convertRowIndexToModel(selectedRow);
                String tituloTarefa = (String) taskTable.getModel().getValueAt(row, 0);
                Tarefa tarefa = tarefaDAO.findByTitulo(tituloTarefa);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new TarefaDetailsView(tarefa, usuario, alunoDashBoard).setVisible(true);
                    }
                });
            }
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
