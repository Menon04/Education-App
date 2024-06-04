package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.models.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO implements GenericDAO<Aluno, Long> {

  @Override
  public void create(Aluno aluno) {
    String sql = "INSERT INTO Aluno (nome, sobrenome, email, data_nascimento, cpf, senha) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, aluno.nome());
      stmt.setString(2, aluno.sobrenome());
      stmt.setString(3, aluno.email());
      stmt.setDate(4, Date.valueOf(aluno.dataNascimento()));
      stmt.setString(5, aluno.cpf());
      stmt.setString(6, aluno.senha());
      stmt.executeUpdate();

      ResultSet generatedKeys = stmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        Long id = generatedKeys.getLong(1);
        aluno = new Aluno(id, aluno.nome(), aluno.sobrenome(), aluno.email(), aluno.dataNascimento(), aluno.cpf(),
            aluno.senha());
      } else {
        throw new SQLException("Falha ao obter o ID gerado para Aluno.");
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar Aluno: " + e.getMessage(), e);
    }
  }

  @Override
  public Aluno findById(Long id) {
    String sql = "SELECT * FROM Aluno WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return new Aluno(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("sobrenome"),
            rs.getString("email"),
            rs.getDate("data_nascimento").toLocalDate(),
            rs.getString("cpf"),
            rs.getString("senha"));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Aluno: " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<Aluno> findAll() {
    List<Aluno> alunos = new ArrayList<>();
    String sql = "SELECT * FROM Aluno";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Aluno aluno = new Aluno(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("sobrenome"),
            rs.getString("email"),
            rs.getDate("data_nascimento").toLocalDate(),
            rs.getString("cpf"),
            rs.getString("senha"));
        alunos.add(aluno);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar todos os Alunos: " + e.getMessage(), e);
    }
    return alunos;
  }

  @Override
  public void update(Aluno aluno) {
    String sql = "UPDATE Aluno SET nome = ?, sobrenome = ?, email = ?, data_nascimento = ?, cpf = ?, senha = ? WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, aluno.nome());
      stmt.setString(2, aluno.sobrenome());
      stmt.setString(3, aluno.email());
      stmt.setDate(4, Date.valueOf(aluno.dataNascimento()));
      stmt.setString(5, aluno.cpf());
      stmt.setString(6, aluno.senha());
      stmt.setLong(7, aluno.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao atualizar Aluno: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(Aluno aluno) {
    String sql = "DELETE FROM Aluno WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, aluno.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar Aluno: " + e.getMessage(), e);
    }
  }
}