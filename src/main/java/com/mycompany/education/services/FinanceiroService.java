package com.mycompany.education.services;

import com.mycompany.education.dao.CursoDAO;

public class FinanceiroService {
  private CursoDAO cursoDAO;

  public FinanceiroService() {
    this.cursoDAO = new CursoDAO();
  }

  public int receitaTotal(Long cursoId) {
    int alunosInscritos = cursoDAO.countAlunosInscritos(cursoId);
    int total = 0;
    for (int i = 0; i < alunosInscritos; i++) {
      int mensalidade = 500 + (int) (Math.random() * 501);
      total += mensalidade;
    }
    return total;
  }
}
