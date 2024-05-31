package com.mycompany.education.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.education.models.Tarefa;

public class TarefaDAO implements GenericDAO<Tarefa, Long> {
  private final Map<Long, Tarefa> tarefasMap = new HashMap<>();
  private Long currentId = 1L;

  public void create(Tarefa tarefa) {
    tarefa = new Tarefa(currentId, tarefa.titulo(), tarefa.descricao(), tarefa.dataEntrega(), tarefa.dataPublicacao());
    tarefasMap.put(currentId, tarefa);
    currentId++;
  }

  public Tarefa findById(Long id) {
    return tarefasMap.get(id);
  }

  public List<Tarefa> findAll() {
    return new ArrayList<>(tarefasMap.values());
  }

  public void update(Tarefa tarefa) {
    if (tarefasMap.containsKey(tarefa.id())) {
      tarefasMap.put(tarefa.id(), tarefa);
    }
  }

  public void delete(Tarefa tarefa) {
    tarefasMap.remove(tarefa.id());
  }
}
