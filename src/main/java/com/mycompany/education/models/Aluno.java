package com.mycompany.education.models;

import java.time.LocalDate;
import java.util.List;

public record Aluno(Long id, String nome, String sobrenome, String email, LocalDate dataNascimento, String cpf,
    String senha, List<EnvioTarefa> enviosTarefas) implements Usuario {

  public Aluno {
    if (nome == null || nome.isBlank()) {
      throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
    }
    if (sobrenome == null || sobrenome.isBlank()) {
      throw new IllegalArgumentException("Sobrenome não pode ser nulo ou vazio");
    }
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
    }
    if (dataNascimento == null) {
      throw new IllegalArgumentException("Data de nascimento não pode ser nula");
    }
    if (cpf == null || cpf.isBlank()) {
      throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
    }
    if (senha == null || senha.isBlank()) {
      throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
    }
    if (enviosTarefas == null) {
      throw new IllegalArgumentException("Envios de tarefas não podem ser nulos");
    }
  }
}
