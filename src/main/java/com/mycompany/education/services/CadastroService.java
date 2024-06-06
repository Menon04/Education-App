package com.mycompany.education.services;

import com.mycompany.education.dao.UsuarioDAO;
import com.mycompany.education.models.Usuario;

public class CadastroService {
  public static void insertUser(Usuario usuario) {
    try {
      UsuarioDAO usuarioDAO = new UsuarioDAO();
      usuarioDAO.create(usuario);
    } catch (RuntimeException e) {
      throw new RuntimeException("Erro ao salvar cadastro: " + e.getMessage(), e);
    }
  }
}
