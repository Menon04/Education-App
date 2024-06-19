package com.mycompany.education.components.professor;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

public class TarefaButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton editButton;
    private JButton deleteButton;
    private JLabel courseNameLabel;

    public TarefaButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editButton = new JButton("Editar");
        deleteButton = new JButton("Apagar");
        courseNameLabel = new JLabel(); 
        add(editButton);
        add(deleteButton);
        add(courseNameLabel); 
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof String) {
            courseNameLabel.setText((String) value);
        }
        return this;
    }

    public void setActionListeners(ActionListener editListener, ActionListener deleteListener) {
        editButton.addActionListener(editListener);
        deleteButton.addActionListener(deleteListener);
    }
}
