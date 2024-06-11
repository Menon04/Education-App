package com.mycompany.education.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.models.Curso;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private JTable table;
    private CursoDAO cursoDAO;

    public ButtonEditor(JTable table, CursoDAO cursoDAO) {
        this.table = table;
        this.cursoDAO = cursoDAO;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editButton = new JButton("Editar");
        deleteButton = new JButton("Apagar");

        panel.add(editButton);
        panel.add(deleteButton);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); 
                int row = table.getSelectedRow();
                Long cursoId = (Long) table.getValueAt(row, 0);
                editarCurso(cursoId);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); 
                int row = table.getSelectedRow();
                Long cursoId = (Long) table.getValueAt(row, 0);
                apagarCurso(cursoId);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    private void editarCurso(Long cursoId) {
        Curso curso = cursoDAO.findById(cursoId);

        if (curso != null) {
            JFrame editFrame = new JFrame("Editar Curso");
            editFrame.setSize(400, 300);
            editFrame.setLayout(new GridLayout(3, 2));

            JTextField tituloField = new JTextField(curso.titulo());
            JTextField descricaoField = new JTextField(curso.descricao());

            editFrame.add(new JLabel("Título:"));
            editFrame.add(tituloField);
            editFrame.add(new JLabel("Descrição:"));
            editFrame.add(descricaoField);

            JButton saveButton = new JButton("Salvar");
            AtomicReference<Curso> cursoRef = new AtomicReference<>(curso);

            saveButton.addActionListener(e -> {
                Curso updatedCurso = new Curso(cursoRef.get().id(), tituloField.getText(), descricaoField.getText(),
                        cursoRef.get().professor(), cursoRef.get().alunosInscritos(), cursoRef.get().materiais(),
                        cursoRef.get().tarefas(), cursoRef.get().enviosTarefas());
                cursoDAO.update(updatedCurso);
                carregarCursos();
                editFrame.dispose();
            });
            editFrame.add(saveButton);

            editFrame.setVisible(true);
        }
    }

    private void apagarCurso(Long cursoId) {
        Curso curso = cursoDAO.findById(cursoId);
        if (curso != null) {
            cursoDAO.delete(curso);
            carregarCursos();
        }
    }

    private void carregarCursos() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<Curso> cursos = cursoDAO.findAll();
        for (Curso curso : cursos) {
            String professorNome = curso.professor().nome() + " " + curso.professor().sobrenome();
            model.addRow(new Object[]{curso.id(), curso.titulo(), curso.descricao(), professorNome});
        }
    }
}
