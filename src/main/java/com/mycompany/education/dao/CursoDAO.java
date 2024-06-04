package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.models.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO implements GenericDAO<Curso, Long> {

  @Override
  public void create(Curso curso) {
    String sql = "INSERT INTO Curso (titulo, descricao, professor_id) VALUES (?, ?, ?)";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, curso.titulo());
      stmt.setString(2, curso.descricao());
      stmt.setLong(3, curso.professor().id());
      stmt.executeUpdate();

      ResultSet generatedKeys = stmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        Long id = generatedKeys.getLong(1);
        curso = new Curso(id, curso.titulo(), curso.descricao(), curso.professor(), curso.alunosInscritos(),
            curso.materiais(), curso.tarefas());
      } else {
        throw new SQLException("Falha ao obter o ID gerado para Curso.");
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar Curso: " + e.getMessage(), e);
    }
  }

  @Override
  public Curso findById(Long id) {
    String sql = "SELECT * FROM Curso WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return new Curso(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("descricao"),
            null, 
            null, 
            null, 
            null 
        );
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Curso: " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<Curso> findAll() {
    List<Curso> cursos = new ArrayList<>();
    String sql = "SELECT * FROM Curso";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Curso curso = new Curso(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("descricao"),
            null, 
            null, 
            null,
            null 
        );
        cursos.add(curso);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar todos os Cursos: " + e.getMessage(), e);
    }
    return cursos;
  }

  @Override
  public void update(Curso curso) {
    String sql = "UPDATE Curso SET titulo = ?, descricao = ? WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, curso.titulo());
      stmt.setString(2, curso.descricao());
      stmt.setLong(3, curso.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao atualizar Curso: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(Curso curso) {
    String sql = "DELETE FROM Curso WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, curso.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar Curso: " + e.getMessage(), e);
    }
  }
}