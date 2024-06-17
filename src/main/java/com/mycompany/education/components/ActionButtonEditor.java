package com.mycompany.education.components;


import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.dao.GenericDAO;
import com.mycompany.education.dao.MaterialDAO;
import com.mycompany.education.dao.UsuarioDAO;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

@SuppressWarnings("rawtypes")
public class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private GenericDAO dao;

    public ActionButtonEditor(JTable table, GenericDAO dao, String buttonLabel) {
        this.table = table;
        this.dao = dao;
        this.button = new JButton(buttonLabel);
        this.button.addActionListener(this);
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPushed) {
            int row = table.getSelectedRow();
            int id = (int) table.getValueAt(row, 0);
            if (dao instanceof CursoDAO) {
                // Example: Open course details screen
            } else if (dao instanceof MaterialDAO) {
                // Example: Open material details screen
            } else if (dao instanceof UsuarioDAO) {
                // Example: Open student details screen
            }
        }
        isPushed = false;
        fireEditingStopped();
    }
}
