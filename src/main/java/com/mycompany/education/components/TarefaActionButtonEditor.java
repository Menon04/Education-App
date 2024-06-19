package com.mycompany.education.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

import com.mycompany.education.dao.TarefaDAO;
import com.mycompany.education.models.Tarefa;

public class TarefaActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel courseNameLabel;
    private TarefaDAO tarefaDAO;
    private JFrame editorFrame;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TarefaActionButtonEditor(JTable table, TarefaDAO tarefaDAO) {
        this.tarefaDAO = tarefaDAO;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editButton = new JButton("Editar");
        deleteButton = new JButton("Apagar");
        courseNameLabel = new JLabel();

        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(courseNameLabel);

        editButton.addActionListener(e -> {
            fireEditingStopped();
            int row = table.getSelectedRow();
            Long tarefaId = (Long) table.getValueAt(row, 0);
            editarTarefa(tarefaId, table);
        });

        deleteButton.addActionListener(e -> {
            fireEditingStopped();
            int row = table.getSelectedRow();
            Long tarefaId = (Long) table.getValueAt(row, 0);
            apagarTarefa(tarefaId, table);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof String) {
            courseNameLabel.setText((String) value);
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    private void editarTarefa(Long tarefaId, JTable table) {
        Tarefa tarefa = tarefaDAO.findById(tarefaId);
        if (tarefa != null) {
            editorFrame = new JFrame("Editar Tarefa");
            editorFrame.setSize(500, 500);
            editorFrame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField tituloField = new JTextField(tarefa.titulo(), 20);
            JTextArea descricaoField = new JTextArea(tarefa.descricao(), 5, 20);
            JTextField dataEntregaField = new JTextField(tarefa.dataEntrega().format(DATE_FORMATTER), 10);
            JTextField notaField = new JTextField(String.valueOf(tarefa.nota()), 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            editorFrame.add(new JLabel("Tarefa:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            editorFrame.add(tituloField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            editorFrame.add(new JLabel("Descrição:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.BOTH;
            editorFrame.add(new JScrollPane(descricaoField), gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            editorFrame.add(new JLabel("Data de Entrega (yyyy-MM-dd):"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            editorFrame.add(dataEntregaField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            editorFrame.add(new JLabel("Nota:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            editorFrame.add(notaField, gbc);

            JButton saveButton = new JButton("Salvar");
            AtomicReference<Tarefa> tarefaRef = new AtomicReference<>(tarefa);

            saveButton.addActionListener(e -> {
                try {
                    LocalDate dataEntrega = LocalDate.parse(dataEntregaField.getText(), DATE_FORMATTER);
                    Double nota = Double.valueOf(notaField.getText());
                    Tarefa updatedTarefa = new Tarefa(
                            tarefaRef.get().id(), tituloField.getText(), descricaoField.getText(),
                            nota, dataEntrega, tarefaRef.get().dataPublicacao(),
                            tarefaRef.get().cursoId());
                    tarefaDAO.update(updatedTarefa);
                    carregarTarefas(table);
                    editorFrame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(editorFrame, "Erro ao salvar a tarefa: " + ex.getMessage());
                }
            });

            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.gridwidth = 1;
            editorFrame.add(saveButton, gbc);

            editorFrame.setVisible(true);
        }
    }

    private void apagarTarefa(Long tarefaId, JTable table) {
        Tarefa tarefa = tarefaDAO.findById(tarefaId);
        if (tarefa != null) {
            tarefaDAO.delete(tarefa);
            carregarTarefas(table);
        }
    }

    private void carregarTarefas(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        java.util.List<Tarefa> tarefas = tarefaDAO.findAll();
        for (Tarefa tarefa : tarefas) {
            model.addRow(new Object[]{
                    tarefa.id(), tarefa.titulo(), tarefa.descricao(), tarefa.nota(),
                    tarefa.dataEntrega(), tarefa.dataPublicacao(), tarefa.cursoId()
            });
        }
    }
}
