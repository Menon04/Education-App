package com.mycompany.education.services;

import com.mycompany.education.dao.CursoDAO;
import com.mycompany.education.models.Curso;
import com.mycompany.education.models.Material;
import com.mycompany.education.models.Tarefa;
import com.mycompany.education.models.EnvioTarefa;

import java.util.List;

public class CursoService {
  private CursoDAO cursoDAO;

  public CursoService() {
    this.cursoDAO = new CursoDAO();
  }

  public List<Curso> findAllCourses() {
    return cursoDAO.findAll();
  }

  public List<Material> findMaterialsByCursoId(Long cursoId) {
    return cursoDAO.findMateriaisByCursoId(cursoId);
  }

  public List<Tarefa> findTasksByCursoId(Long cursoId) {
    return cursoDAO.findTarefasByCursoId(cursoId);
  }

  public List<EnvioTarefa> findGradesByCursoId(Long cursoId) {
    return cursoDAO.findEnviosTarefasByCursoId(cursoId);
  }
}