package com.mycompany.education.components;

import com.mycompany.education.dao.MaterialDAO;
import com.mycompany.education.models.Material;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaterialButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private JTable table;
    private MaterialDAO materialDAO;
    private String text;

    public MaterialButtonEditor(JTable table, MaterialDAO materialDAO, String text) {
        this.table = table;
        this.materialDAO = materialDAO;
        this.text = text;

        button = new JButton(text);
        button.addActionListener(this);
    }

    @Override
    public Object getCellEditorValue() {
        return text;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText((value == null) ? text : value.toString());
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        Long materialId = (Long) table.getValueAt(row, 0);
        Material material = materialDAO.findById(materialId);
        JOptionPane.showMessageDialog(table, "Detalhes do material: " + material.titulo());
        fireEditingStopped();
    }
}
