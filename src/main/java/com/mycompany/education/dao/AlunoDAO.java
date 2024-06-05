package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.models.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO implements GenericDAO<Aluno, Long> {

  @Override
  public void create(Aluno aluno) {
    String alunoSql = "INSERT INTO aluno (nome, sobrenome, email, data_nascimento, cpf, senha) VALUES (?, ?, ?, ?, ?, ?)";
    String usuarioSql = "INSERT INTO usuario (email, senha, aluno_id) VALUES (?, ?, ?)";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement alunoStmt = conn.prepareStatement(alunoSql, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement usuarioStmt = conn.prepareStatement(usuarioSql)) {

      conn.setAutoCommit(false);

      alunoStmt.setString(1, aluno.nome());
      alunoStmt.setString(2, aluno.sobrenome());
      alunoStmt.setString(3, aluno.email());
      alunoStmt.setDate(4, Date.valueOf(aluno.dataNascimento()));
      alunoStmt.setString(5, aluno.cpf());
      alunoStmt.setString(6, aluno.senha());
      alunoStmt.executeUpdate();

      ResultSet generatedKeys = alunoStmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        long alunoId = generatedKeys.getLong(1);
        usuarioStmt.setString(1, aluno.email());
        usuarioStmt.setString(2, aluno.senha());
        usuarioStmt.setLong(3, alunoId);
        usuarioStmt.executeUpdate();
        conn.commit();
      } else {
        conn.rollback();
        throw new SQLException("Falha ao obter o ID do aluno inserido.");
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar Aluno e Usuário: " + e.getMessage(), e);
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