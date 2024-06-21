package com.mycompany.education.components.professor;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class AvaliarEnvioButtonRenderer extends JButton implements TableCellRenderer {
  public AvaliarEnvioButtonRenderer() {
    setText("Avaliar");
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(UIManager.getColor("Button.background"));
    }
    return this;
  }
}