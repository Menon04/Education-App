package com.mycompany.education.factories;

import java.time.LocalDate;

import com.mycompany.education.models.Usuario;

public interface UsuarioFactory {
  Usuario criarUsuario(String nome, String sobrenome, String email, LocalDate dataNascimento, String cpf, String senha);
}
