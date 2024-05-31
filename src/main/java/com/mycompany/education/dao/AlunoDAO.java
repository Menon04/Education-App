package com.mycompany.education.dao;

import com.mycompany.education.models.Aluno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlunoDAO implements GenericDAO<Aluno, Long>{
  private final Map<Long, Aluno> alunosMap = new HashMap<>();
  private Long currentId = 1L;

  public void create(Aluno aluno) {
    aluno = new Aluno(currentId, aluno.nome(), aluno.sobrenome(), aluno.email(), aluno.dataNascimento(), aluno.cpf(),
        aluno.senha());
    alunosMap.put(currentId, aluno);
    currentId++;
  }

  public Aluno findById(Long id) {
    return alunosMap.get(id);
  }

  public List<Aluno> findAll() {
    return new ArrayList<>(alunosMap.values());
  }

  public void update(Aluno aluno) {
    if (alunosMap.containsKey(aluno.id())) {
      alunosMap.put(aluno.id(), aluno);
    }
  }

  public void delete(Aluno aluno) {
    alunosMap.remove(aluno.id());
  }
}