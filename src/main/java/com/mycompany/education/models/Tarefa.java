package com.mycompany.education.models;

import java.time.LocalDate;

public record Tarefa(Long id, String titulo, String descricao, Double nota, LocalDate dataEntrega, LocalDate dataPublicacao, Long cursoId) {
  public Tarefa {
    if (titulo == null || titulo.isBlank()) {
      throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
    }
    if (descricao == null || descricao.isBlank()) {
      throw new IllegalArgumentException("Descrição não pode ser nula ou vazia");
    }
    if (nota == null) {
      throw new IllegalArgumentException("Nota não pode ser nula ou vazia");
    }
    if (dataEntrega == null) {
      throw new IllegalArgumentException("Data de entrega não pode ser nula");
    }
    if (dataPublicacao == null) {
      throw new IllegalArgumentException("Data de publicação não pode ser nula");
    }
    if (cursoId == null) {
      throw new IllegalArgumentException("Curso ID não pode ser nulo");
    }
  }
}
