package com.mycompany.education.models;

import java.time.LocalDate;

public record Tarefa(Long id, String titulo, String descricao, LocalDate dataEntrega, LocalDate dataPublicacao) {
  public Tarefa {
    if (titulo == null || titulo.isBlank()) {
      throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
    }
    if (descricao == null || descricao.isBlank()) {
      throw new IllegalArgumentException("Descrição não pode ser nula ou vazia");
    }
    if (dataEntrega == null) {
      throw new IllegalArgumentException("Data de entrega não pode ser nula");
    }
    if (dataPublicacao == null) {
      throw new IllegalArgumentException("Data de publicação não pode ser nula");
    }
  }
}
