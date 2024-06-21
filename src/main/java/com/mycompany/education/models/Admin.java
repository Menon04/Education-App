package com.mycompany.education.models;

import java.time.LocalDate;

public record Admin(Long id, String nome, String sobrenome, String email, LocalDate dataNascimento, String cpf, String senha) implements Usuario {
  public Admin {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
    }
    if (senha == null || senha.isBlank()) {
      throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
    }
    if (nome == null || nome.isBlank()) {
      throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
    }
    if (sobrenome == null || sobrenome.isBlank()) {
      throw new IllegalArgumentException("Sobrenome não pode ser nulo ou vazio");
    }
    if (dataNascimento == null) {
      throw new IllegalArgumentException("Data de nascimento não pode ser nula");
    }
    if (cpf == null || cpf.isBlank()) {
      throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
    }
    if (cpf.length() != 11) {
      throw new IllegalArgumentException("CPF deve ter 11 caracteres");
    }
  }
}
