package com.mycompany.education.factories;

import java.util.HashMap;
import java.util.Map;

public class UsuarioFactoryProvider {
  private static final Map<String, UsuarioFactory> factories = new HashMap<>();

    static {
        factories.put("Aluno", new AlunoFactory());
        factories.put("Professor", new ProfessorFactory());
    }

    public static UsuarioFactory getFactory(String tipoUsuario) {
        UsuarioFactory factory = factories.get(tipoUsuario);
        if (factory == null) {
            throw new IllegalArgumentException("Tipo de usuário inválido");
        }
        return factory;
    }
}
