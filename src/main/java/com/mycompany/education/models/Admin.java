package com.mycompany.education.models;

public record Admin(Long id, String email, String senha) {
  public Admin {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
    }
    if (senha == null || senha.isBlank()) {
      throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
    }
  }
}
