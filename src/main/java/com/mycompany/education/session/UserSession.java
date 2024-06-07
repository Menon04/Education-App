package com.mycompany.education.session;

import com.mycompany.education.models.Usuario;

public class UserSession {
  private static UserSession instance;
  private Usuario usuario;

  public static UserSession getInstance(Usuario usuario) {
    if (instance == null) {
      instance = new UserSession(usuario);
    }
    return instance;
  }

  private UserSession(Usuario usuario) {
    this.usuario = usuario;
  }

  public Usuario user() {
    return this.usuario;
  }

  public void clearSession() {
    instance = null;
  }
}