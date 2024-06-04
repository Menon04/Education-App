package com.mycompany.education.factories;

import java.time.LocalDate;

import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Usuario;

public class AlunoFactory implements UsuarioFactory {
  public Usuario criarUsuario(String nome, String sobrenome, String email, LocalDate dataNascimento, String cpf, String senha) {
        return new Aluno(null, nome, sobrenome, email, dataNascimento, cpf, senha);
    }
}
