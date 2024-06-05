package com.mycompany.education.controllers;

import com.mycompany.education.services.LoginService;
import com.mycompany.education.utils.HashPassword;

public class LoginController {

  public static boolean validateLogin(String email, String password) {
    String senha = HashPassword.hashPassword(password);
    return LoginService.searchUser(email, senha);
  }
}