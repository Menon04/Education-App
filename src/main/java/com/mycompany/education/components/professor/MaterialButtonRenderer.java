package com.mycompany.education.components.professor;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MaterialButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton editButton;
    private JButton deleteButton;

    public MaterialButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));  // Use FlowLayout to layout buttons horizontally
        editButton = new JButton("Editar");
        deleteButton = new JButton("Apagar");
        add(editButton);
        add(deleteButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}
