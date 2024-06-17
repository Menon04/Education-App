package com.mycompany.education.components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MaterialButtonRenderer extends JButton implements TableCellRenderer {

    public MaterialButtonRenderer(String text) {
        setText(text);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}
