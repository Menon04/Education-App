package com.mycompany.education.views;

import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.models.Admin;
import com.mycompany.education.models.Curso;
import com.mycompany.education.models.Professor;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.session.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Random;

public class FinanceiroDashBoard extends JFrame {
  private UserSession userSession;
  private CursoDAO cursoDAO;

  public FinanceiroDashBoard(UserSession userSession) {
    this.userSession = userSession;
    this.cursoDAO = new CursoDAO();
    initComponents();
  }

  private void initComponents() {
    setTitle("Financeiro Dashboard");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel label = new JLabel("Bem-vindo ao Dashboard Financeiro", SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 24));
    panel.add(label, BorderLayout.NORTH);

    JTable table = new JTable();
    DefaultTableModel tableModel = new DefaultTableModel(
        new Object[] { "ID", "Nome do Curso", "Criador", "Alunos Inscritos", "Receita Total" }, 0);
    table.setModel(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    panel.add(scrollPane, BorderLayout.CENTER);

    List<Curso> cursos = cursoDAO.findAll();
    Random random = new Random();
    for (Curso curso : cursos) {
      Long cursoId = curso.id();
      String titulo = curso.titulo();
      Usuario professor = curso.professor();
      String criador = professor.nome() + " " + professor.sobrenome();
      int alunosInscritos = cursoDAO.countAlunosInscritos(cursoId);
      int mensalidade = 500 + random.nextInt(501);
      int receitaTotal = alunosInscritos * mensalidade;

      tableModel.addRow(new Object[] { cursoId, titulo, criador, alunosInscritos, receitaTotal });
    }

    JButton backButton = new JButton("Voltar");
    backButton.addActionListener((ActionEvent e) -> {
      this.dispose();
      if (userSession.user() instanceof Professor) {
        new ProfessorDashBoard(userSession).setVisible(true);
      } else if (userSession.user() instanceof Admin) {
        new AdminDashBoard(userSession).setVisible(true);
      }
    });
    panel.add(backButton, BorderLayout.SOUTH);

    add(panel);
  }
}