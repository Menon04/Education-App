package com.mycompany.education.components.aluno;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TarefaButtonRenderer extends JButton implements TableCellRenderer {

    public TarefaButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText(value == null ? "" : value.toString());
        return this;
    }
}
