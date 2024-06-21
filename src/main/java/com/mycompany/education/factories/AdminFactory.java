package com.mycompany.education.factories;

import java.time.LocalDate;

import com.mycompany.education.models.Admin;
import com.mycompany.education.models.Usuario;

public class AdminFactory implements UsuarioFactory {
  @Override
  public Usuario criarUsuario(Long id, String nome, String sobrenome, String email, LocalDate dataNascimento, String cpf, String senha) {
        return new Admin(id, nome, sobrenome, email, dataNascimento, cpf, senha);
    }
}
