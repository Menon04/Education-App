// package com.mycompany.education.views;

// import com.mycompany.education.dao.MaterialDAO;
// import com.mycompany.education.models.Material;
// import com.mycompany.education.models.Usuario;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// public class TelaCadastroMaterial extends JFrame {
//     private JTextField tituloField;
//     private JTextArea descricaoArea;
//     private JButton salvarButton;
//     private JButton cancelarButton;
//     private MaterialDAO materialDAO;
//     private Usuario professor;

//     public TelaCadastroMaterial(Usuario professor) {
//         this.professor = professor;
//         this.materialDAO = new MaterialDAO();
//         initComponents();
//     }

//     private void initComponents() {
//         setTitle("Cadastrar Material");
//         setSize(400, 300);
//         setLayout(new GridLayout(3, 2));

//         add(new JLabel("Título:"));
//         tituloField = new JTextField();
//         add(tituloField);

//         add(new JLabel("Descrição:"));
//         descricaoArea = new JTextArea();
//         add(new JScrollPane(descricaoArea));

//         salvarButton = new JButton("Salvar");
//         salvarButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 salvarMaterial();
//             }
//         });
//         add(salvarButton);

//         cancelarButton = new JButton("Cancelar");
//         cancelarButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 cancelar();
//             }
//         });
//         add(cancelarButton);
//     }

//     private void salvarMaterial() {
//         String titulo = tituloField.getText();
//         String descricao = descricaoArea.getText();
//         if (titulo.isBlank() || descricao.isBlank()) {
//             JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         Material material = new Material(null, titulo, descricao, professor.id());
//         materialDAO.create(material);
//         JOptionPane.showMessageDialog(this, "Material salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
//         new ProfessorDashBoard(UserSession.getInstance()).setVisible(true);
//         this.dispose();
//     }

//     private void cancelar() {
//         new ProfessorDashBoard(UserSession.getInstance()).setVisible(true);
//         this.dispose();
//     }
// }
