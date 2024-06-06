package com.mycompany.education.models;

import java.time.LocalDate;

public interface Usuario {
  Long id();
  String nome();
  String sobrenome();
  String email();
  LocalDate dataNascimento();
  String cpf();
  String senha();
}