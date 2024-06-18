package com.mycompany.education.models;

import java.time.LocalDate;

public record Material(Long id, String titulo, String conteudo, LocalDate dataPublicacao, Long professorId, Long cursoId) {

  public Material {
    if (titulo == null || titulo.isBlank()) {
      throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
    }
    if (conteudo == null || conteudo.isBlank()) {
      throw new IllegalArgumentException("Conteúdo não pode ser nulo ou vazio");
    }
    if (dataPublicacao == null) {
      throw new IllegalArgumentException("Data de publicação não pode ser nula");
    }
    if (professorId == null) {
      throw new IllegalArgumentException("ID do professor não pode ser nulo");
    }
    if (cursoId == null) {
      throw new IllegalArgumentException("ID do curso não pode ser nulo");
    }
  }
}
