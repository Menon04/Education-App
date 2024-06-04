package com.mycompany.education.controllers;

import com.mycompany.education.dao.AlunoDAO;
import com.mycompany.education.dao.ProfessorDAO;
import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Professor;
import com.mycompany.education.models.Usuario;

public class CadastroController {

  public static void salvarCadastroUsuario(Usuario usuario) {
    try {
      if (usuario instanceof Aluno aluno) {
        AlunoDAO alunoDAO = new AlunoDAO();
        alunoDAO.create(aluno);
      } else if (usuario instanceof Professor professor) {
        ProfessorDAO professorDAO = new ProfessorDAO();
        professorDAO.create(professor);
      }
    } catch (RuntimeException e) {
      throw new RuntimeException("Erro ao salvar cadastro: " + e.getMessage(), e);
    }
  }
}
