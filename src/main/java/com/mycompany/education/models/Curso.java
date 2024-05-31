package com.mycompany.education.models;

import java.util.List;

public record Curso(Long id, String titulo, String descricao, Professor professor, List<Aluno> alunosInscritos, List<Material> materiais, List<Tarefa> tarefas) {

  public Curso {
    if (titulo == null || titulo.isBlank()) {
      throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
    }
    if (descricao == null || descricao.isBlank()) {
      throw new IllegalArgumentException("Descrição não pode ser nula ou vazia");
    }
    if (professor == null) {
      throw new IllegalArgumentException("Professor não pode ser nulo");
    }
    if (alunosInscritos == null) {
      throw new IllegalArgumentException("Alunos inscritos não podem ser nulos");
    }
    if (materiais == null) {
      throw new IllegalArgumentException("Materiais não podem ser nulos");
    }
    if (tarefas == null) {
      throw new IllegalArgumentException("Tarefas não podem ser nulas");
    }
  }
}