package com.mycompany.education.factories;

public class UsuarioFactoryProvider {
  public static UsuarioFactory getFactory(String tipo) {
        return switch (tipo) {
            case "Aluno" -> new AlunoFactory();
            case "Professor" -> new ProfessorFactory();
            default -> throw new IllegalArgumentException("Tipo de usuário desconhecido: " + tipo);
        };
    }
}
