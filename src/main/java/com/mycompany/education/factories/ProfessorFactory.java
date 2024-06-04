package com.mycompany.education.factories;

import java.time.LocalDate;

import com.mycompany.education.models.Professor;
import com.mycompany.education.models.Usuario;

public class ProfessorFactory implements UsuarioFactory {
  @Override
    public Usuario criarUsuario(String nome, String sobrenome, String email, LocalDate dataNascimento, String cpf, String senha) {
        return new Professor(null, nome, sobrenome, email, dataNascimento, cpf, senha);
    }
}
