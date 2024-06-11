package com.mycompany.education.components;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import com.mycompany.education.views.AlunoDashBoard;

import java.awt.Component;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private AlunoDashBoard alunoDashBoard;
    private int row;
    private int column;

    public ButtonEditor(JCheckBox checkBox, AlunoDashBoard alunoDashBoard) {
        super(checkBox);
        this.alunoDashBoard = alunoDashBoard;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        this.row = row;
        this.column = column;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            alunoDashBoard.handleButtonAction(row, column);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
