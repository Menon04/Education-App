package com.mycompany.education.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.education.models.Professor;

public class ProfessorDAO implements GenericDAO<Professor, Long> {
  private final Map<Long, Professor> professoresMap = new HashMap<>();
  private Long currentId = 1L;

  public void create(Professor professor) {
    professor = new Professor(currentId, professor.nome(), professor.sobrenome(), professor.email(),
        professor.dataNascimento(), professor.cpf(),
        professor.senha());
    professoresMap.put(currentId, professor);
    currentId++;
  }

  public Professor findById(Long id) {
    return professoresMap.get(id);
  }

  public List<Professor> findAll() {
    return new ArrayList<>(professoresMap.values());
  }

  public void update(Professor professor) {
    if (professoresMap.containsKey(professor.id())) {
      professoresMap.put(professor.id(), professor);
    }
  }

  public void delete(Professor professor) {
    professoresMap.remove(professor.id());
  }
}
