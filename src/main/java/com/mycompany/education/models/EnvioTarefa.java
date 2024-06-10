package com.mycompany.education.models;

import java.time.LocalDate;

public record EnvioTarefa(Long id, Aluno aluno, Tarefa tarefa, String resposta, LocalDate dataEnvio, Double nota) {

  public EnvioTarefa {
    if (aluno == null) {
      throw new IllegalArgumentException("Aluno não pode ser nulo");
    }
    if (tarefa == null) {
      throw new IllegalArgumentException("Tarefa não pode ser nula");
    }
    if (resposta == null || resposta.isBlank()) {
      throw new IllegalArgumentException("Resposta não pode ser nula ou vazia");
    }
    if (dataEnvio == null) {
      throw new IllegalArgumentException("Data de envio não pode ser nula");
    }
    if (nota != null && (nota < 0 || nota > 10)) {
      throw new IllegalArgumentException("Nota deve estar entre 0 e 10");
    }
  }
}
