package com.mycompany.education.dao;

import com.mycompany.education.models.Curso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CursoDAO implements GenericDAO<Curso, Long> {
  private final Map<Long, Curso> cursosMap = new HashMap<>();
  private Long currentId = 1L;

  public void create(Curso curso) {
    curso = new Curso(currentId, curso.titulo(), curso.descricao(), curso.professor(), curso.alunosInscritos(),
        curso.materiais(), curso.tarefas());
    cursosMap.put(currentId, curso);
    currentId++;
  }

  public Curso findById(Long id) {
    return cursosMap.get(id);
  }

  public List<Curso> findAll() {
    return new ArrayList<>(cursosMap.values());
  }

  public void update(Curso curso) {
    if (cursosMap.containsKey(curso.id())) {
      cursosMap.put(curso.id(), curso);
    }
  }

  public void delete(Curso curso) {
    cursosMap.remove(curso.id());
  }
}