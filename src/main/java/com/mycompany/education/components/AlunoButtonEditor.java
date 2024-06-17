package com.mycompany.education.components;

import com.mycompany.education.dao.UsuarioDAO;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.models.Aluno;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlunoButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private JTable table;
    private UsuarioDAO usuarioDAO;
    private String text;

    public AlunoButtonEditor(JTable table, UsuarioDAO usuarioDAO, String text) {
        this.table = table;
        this.usuarioDAO = usuarioDAO;
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
        Long alunoId = (Long) table.getValueAt(row, 0);
        Usuario aluno = usuarioDAO.findById(alunoId);
        if (aluno instanceof Aluno) {
            JOptionPane.showMessageDialog(table, "Detalhes do aluno: " + aluno.nome() + " " + aluno.sobrenome());
        }
        fireEditingStopped();
    }
}
