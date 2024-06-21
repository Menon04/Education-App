package com.mycompany.education.components.professor;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class AvaliarAlunoButtonRenderer extends JButton implements TableCellRenderer {

    public AvaliarAlunoButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String status = (value != null) ? value.toString() : "Avaliar";
        setText(status);
        setBackground("Avaliar".equals(status) ? Color.YELLOW : Color.GREEN);
        return this;
    }
}
