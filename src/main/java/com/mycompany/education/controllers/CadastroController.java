package com.mycompany.education.controllers;

import com.mycompany.education.dao.AlunoDAO;
import com.mycompany.education.dao.ProfessorDAO;
import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Professor;
import com.mycompany.education.models.Usuario;

public class CadastroController {

  public static void salvarCadastroUsuario(Usuario usuario) {
    if (usuario instanceof Aluno) {
      Aluno aluno = (Aluno) usuario;
      AlunoDAO alunoDAO = new AlunoDAO();
      System.out.println("Aluno: " + aluno.toString());
      alunoDAO.create(aluno);
      System.out.println("Aluno: " + aluno.toString());
    } else if (usuario instanceof Professor) {
      Professor professor = (Professor) usuario;
      ProfessorDAO professorDAO = new ProfessorDAO();
      professorDAO.create(professor);
    }
    
  }
}
