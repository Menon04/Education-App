package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.exceptions.CadastroException;
import com.mycompany.education.factories.UsuarioFactory;
import com.mycompany.education.factories.UsuarioFactoryProvider;
import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements GenericDAO<Usuario, Long> {

  @Override
  public void create(Usuario usuario) throws CadastroException {
    String tipo = usuario instanceof Aluno ? "Aluno" : "Professor";
    String usuarioSql = "INSERT INTO usuario (nome, sobrenome, email, data_nascimento, cpf, senha, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement usuarioStmt = conn.prepareStatement(usuarioSql, Statement.RETURN_GENERATED_KEYS)) {

      conn.setAutoCommit(false);

      usuarioStmt.setString(1, usuario.nome());
      usuarioStmt.setString(2, usuario.sobrenome());
      usuarioStmt.setString(3, usuario.email());
      usuarioStmt.setDate(4, Date.valueOf(usuario.dataNascimento()));
      usuarioStmt.setString(5, usuario.cpf());
      usuarioStmt.setString(6, usuario.senha());
      usuarioStmt.setString(7, tipo);
      usuarioStmt.executeUpdate();

      ResultSet generatedKeys = usuarioStmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        conn.commit();
      } else {
        conn.rollback();
        throw new SQLException("Falha ao obter o ID do usuário inserido.");
      }
    } catch (SQLException e) {
      throw new CadastroException("Erro ao criar Usuário: " + e.getMessage(), e);
    }
  }

  @Override
  public Usuario findById(Long id) {
    String sql = "SELECT * FROM usuario WHERE id = ?";
    Usuario usuario = null;

    try (
        Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String tipo = rs.getString("tipo");
          UsuarioFactory factory = UsuarioFactoryProvider.getFactory(tipo);
          usuario = factory.criarUsuario(
              rs.getLong("id"),
              rs.getString("nome"),
              rs.getString("sobrenome"),
              rs.getString("email"),
              rs.getDate("data_nascimento").toLocalDate(),
              rs.getString("cpf"),
              rs.getString("senha"));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Usuário: " + e.getMessage(), e);
    }

    return usuario;
  }

  @Override
  public List<Usuario> findAll() {
    List<Usuario> usuarios = new ArrayList<>();
    String sql = "SELECT * FROM usuario";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        String tipo = rs.getString("tipo");
        UsuarioFactory factory = UsuarioFactoryProvider.getFactory(tipo);
        Usuario usuario = factory.criarUsuario(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("sobrenome"),
            rs.getString("email"),
            rs.getDate("data_nascimento").toLocalDate(),
            rs.getString("cpf"),
            rs.getString("senha"));
        usuarios.add(usuario);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar todos os Usuários: " + e.getMessage(), e);
    }
    return usuarios;
  }

  @Override
  public void update(Usuario usuario) {
    String tipo = usuario instanceof Aluno ? "Aluno" : "Professor";
    String sql = "UPDATE usuario SET nome = ?, sobrenome = ?, email = ?, data_nascimento = ?, cpf = ?, senha = ?, tipo = ? WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, usuario.nome());
      stmt.setString(2, usuario.sobrenome());
      stmt.setString(3, usuario.email());
      stmt.setDate(4, Date.valueOf(usuario.dataNascimento()));
      stmt.setString(5, usuario.cpf());
      stmt.setString(6, usuario.senha());
      stmt.setString(7, tipo);
      stmt.setLong(8, usuario.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao atualizar Usuário: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(Usuario usuario) {
    String sql = "DELETE FROM usuario WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, usuario.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar Usuário: " + e.getMessage(), e);
    }
  }
}