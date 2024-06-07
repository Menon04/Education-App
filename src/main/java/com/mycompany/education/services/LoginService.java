package com.mycompany.education.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.exceptions.LoginException;
import com.mycompany.education.models.Usuario;
import com.mycompany.education.factories.UsuarioFactory;
import com.mycompany.education.factories.UsuarioFactoryProvider;
import com.mycompany.education.utils.HashPassword;

public class LoginService {

  public static Usuario searchUser(String email, String password) throws LoginException {
    String query = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {

      stmt.setString(1, email);
      stmt.setString(2, HashPassword.hashPassword(password));

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String sobrenome = rs.getString("sobrenome");
        LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
        String cpf = rs.getString("cpf");
        String tipo = rs.getString("tipo");

        UsuarioFactory factory = UsuarioFactoryProvider.getFactory(tipo);
        return factory.criarUsuario(id, nome, sobrenome, email, dataNascimento, cpf, password);
      } else {
        throw new LoginException("Email ou senha inválidos.");
      }
    } catch (SQLException e) {
      throw new LoginException("Erro de conexão com o banco de dados: " + e.getMessage());
    }
  }
}