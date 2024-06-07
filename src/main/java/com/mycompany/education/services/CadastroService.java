package com.mycompany.education.services;

import com.mycompany.education.dao.UsuarioDAO;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.exceptions.CadastroException;

public class CadastroService {
  public static void insertUser(Usuario usuario) throws CadastroException {
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    usuarioDAO.create(usuario);
  }
}
