package com.mycompany.education.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.exceptions.LoginException;
import com.mycompany.education.utils.HashPassword;

public class LoginService {

  public static String searchUser(String email, String password) throws LoginException {
    String query = "SELECT professor_id, aluno_id FROM usuario WHERE email = ? AND senha = ?";

    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {

      stmt.setString(1, email);
      stmt.setString(2, HashPassword.hashPassword(password));

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        if (rs.getObject("professor_id") != null) {
          return "Professor";
        } else if (rs.getObject("aluno_id") != null) {
          return "Aluno";
        }
      } else {
        throw new LoginException("Email ou senha inválidos.");
      }
    } catch (SQLException e) {
      throw new LoginException("Erro de conexão com o banco de dados: " + e.getMessage());
    }
    throw new LoginException("Email ou senha inválidos.");
  }
}