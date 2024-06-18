package com.mycompany.education.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import com.mycompany.education.dao.MaterialDAO;
import com.mycompany.education.models.Material;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MaterialActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private JTable table;
    private MaterialDAO materialDAO;

    public MaterialActionButtonEditor(JTable table, MaterialDAO materialDAO) {
        this.table = table;
        this.materialDAO = materialDAO;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editButton = new JButton("Editar");
        deleteButton = new JButton("Apagar");

        panel.add(editButton);
        panel.add(deleteButton);

        editButton.addActionListener(e -> {
            fireEditingStopped();
            int row = table.getSelectedRow();
            Long materialId = (Long) table.getValueAt(row, 0);
            editarMaterial(materialId);
        });

        deleteButton.addActionListener(e -> {
            fireEditingStopped();
            int row = table.getSelectedRow();
            Long materialId = (Long) table.getValueAt(row, 0);
            apagarMaterial(materialId);
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

    private void editarMaterial(Long materialId) {
        Material material = materialDAO.findById(materialId);

        if (material != null) {
            JFrame editFrame = new JFrame("Editar Material");
            editFrame.setSize(500, 400);
            editFrame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField tituloField = new JTextField(material.titulo(), 20);
            JTextArea conteudoField = new JTextArea(material.conteudo(), 5, 20);

            // Título label
            gbc.gridx = 0;
            gbc.gridy = 0;
            editFrame.add(new JLabel("Título:"), gbc);

            // Título field
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            editFrame.add(tituloField, gbc);

            // Conteúdo label
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            editFrame.add(new JLabel("Conteúdo:"), gbc);

            // Conteúdo field
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.BOTH;
            editFrame.add(new JScrollPane(conteudoField), gbc);

            JButton saveButton = new JButton("Salvar");
            AtomicReference<Material> materialRef = new AtomicReference<>(material);

            saveButton.addActionListener(e -> {
                Material updatedMaterial = new Material(materialRef.get().id(), tituloField.getText(), conteudoField.getText(), 
                        materialRef.get().dataPublicacao(), materialRef.get().professorId(), materialRef.get().cursoId());
                materialDAO.update(updatedMaterial);
                carregarMateriais();
                editFrame.dispose();
            });

            // Save button
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            editFrame.add(saveButton, gbc);

            editFrame.setVisible(true);
        }
    }

    private void apagarMaterial(Long materialId) {
        Material material = materialDAO.findById(materialId);
        if (material != null) {
            materialDAO.delete(material);
            carregarMateriais();
        }
    }

    private void carregarMateriais() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<Material> materiais = materialDAO.findAll();
        for (Material material : materiais) {
            model.addRow(new Object[] { material.id(), material.titulo(), material.conteudo() });
        }
    }
}
