package com.mycompany.education.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.education.connection.MySQLConnection;

public class LoginService {

  public static boolean searchUser(String email, String password) {
    String query = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {

      stmt.setString(1, email);
      stmt.setString(2, password);

      ResultSet rs = stmt.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}