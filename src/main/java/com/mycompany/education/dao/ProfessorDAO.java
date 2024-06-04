package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.models.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO implements GenericDAO<Professor, Long> {

  @Override
  public void create(Professor professor) {
    String sql = "INSERT INTO Professor (nome, sobrenome, email, data_nascimento, cpf, senha) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, professor.nome());
      stmt.setString(2, professor.sobrenome());
      stmt.setString(3, professor.email());
      stmt.setDate(4, Date.valueOf(professor.dataNascimento()));
      stmt.setString(5, professor.cpf());
      stmt.setString(6, professor.senha());
      stmt.executeUpdate();
    } catch (SQLException e) {
      if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("email")) {
        throw new RuntimeException("Email já cadastrado!");
      } else if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("cpf")) {
        throw new RuntimeException("CPF já cadastrado!");
      } else {
        throw new RuntimeException("Erro ao criar Professor: " + e.getMessage(), e);
      }
    }
  }

  @Override
  public Professor findById(Long id) {
    String sql = "SELECT * FROM Professor WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return new Professor(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("sobrenome"),
            rs.getString("email"),
            rs.getDate("data_nascimento").toLocalDate(),
            rs.getString("cpf"),
            rs.getString("senha"));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Professor: " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<Professor> findAll() {
    List<Professor> professores = new ArrayList<>();
    String sql = "SELECT * FROM Professor";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Professor professor = new Professor(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("sobrenome"),
            rs.getString("email"),
            rs.getDate("data_nascimento").toLocalDate(),
            rs.getString("cpf"),
            rs.getString("senha"));
        professores.add(professor);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar todos os Professores: " + e.getMessage(), e);
    }
    return professores;
  }

  @Override
  public void update(Professor professor) {
    String sql = "UPDATE Professor SET nome = ?, sobrenome = ?, email = ?, data_nascimento = ?, cpf = ?, senha = ? WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, professor.nome());
      stmt.setString(2, professor.sobrenome());
      stmt.setString(3, professor.email());
      stmt.setDate(4, Date.valueOf(professor.dataNascimento()));
      stmt.setString(5, professor.cpf());
      stmt.setString(6, professor.senha());
      stmt.setLong(7, professor.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao atualizar Professor: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(Professor professor) {
    String sql = "DELETE FROM Professor WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, professor.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar Professor: " + e.getMessage(), e);
    }
  }
}