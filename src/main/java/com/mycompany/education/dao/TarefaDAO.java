package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.models.Tarefa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements GenericDAO<Tarefa, Long> {

  @Override
  public void create(Tarefa tarefa) {
    String sql = "INSERT INTO Tarefa (titulo, descricao, data_entrega, data_publicacao, nota, curso_id) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, tarefa.titulo());
      stmt.setString(2, tarefa.descricao());
      stmt.setDate(3, Date.valueOf(tarefa.dataEntrega()));
      stmt.setDate(4, Date.valueOf(tarefa.dataPublicacao()));
      stmt.setDouble(5, tarefa.nota());
      stmt.setLong(6, tarefa.cursoId());
      stmt.executeUpdate();

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          Long id = generatedKeys.getLong(1);
          tarefa = new Tarefa(id, tarefa.titulo(), tarefa.descricao(), tarefa.nota(), tarefa.dataEntrega(), tarefa.dataPublicacao(), tarefa.cursoId());
        } else {
          throw new SQLException("Falha ao obter o ID gerado para Tarefa.");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar Tarefa: " + e.getMessage(), e);
    }
  }

  @Override
  public Tarefa findById(Long id) {
    String sql = "SELECT * FROM Tarefa WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Tarefa(
              rs.getLong("id"),
              rs.getString("titulo"),
              rs.getString("descricao"),
              rs.getDouble("nota"),
              rs.getDate("data_entrega").toLocalDate(),
              rs.getDate("data_publicacao").toLocalDate(),
              rs.getLong("curso_id"));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Tarefa: " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<Tarefa> findAll() {
    List<Tarefa> tarefas = new ArrayList<>();
    String sql = "SELECT * FROM Tarefa";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Tarefa tarefa = new Tarefa(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("descricao"),
            rs.getDouble("nota"),
            rs.getDate("data_entrega").toLocalDate(),
            rs.getDate("data_publicacao").toLocalDate(),
            rs.getLong("curso_id"));
        tarefas.add(tarefa);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar todas as Tarefas: " + e.getMessage(), e);
    }
    return tarefas;
  }

  @Override
  public void update(Tarefa tarefa) {
    String sql = "UPDATE Tarefa SET titulo = ?, descricao = ?, data_entrega = ?, data_publicacao = ?, nota = ?, curso_id = ? WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, tarefa.titulo());
      stmt.setString(2, tarefa.descricao());
      stmt.setDate(3, Date.valueOf(tarefa.dataEntrega()));
      stmt.setDate(4, Date.valueOf(tarefa.dataPublicacao()));
      stmt.setDouble(5, tarefa.nota());
      stmt.setLong(6, tarefa.cursoId());
      stmt.setLong(7, tarefa.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao atualizar Tarefa: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(Tarefa tarefa) {
    String sql = "DELETE FROM Tarefa WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, tarefa.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar Tarefa: " + e.getMessage(), e);
    }
  }

  public List<Tarefa> findAllByCurso(Long cursoId) {
    List<Tarefa> tarefas = new ArrayList<>();
    String sql = "SELECT * FROM Tarefa WHERE curso_id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, cursoId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Tarefa tarefa = new Tarefa(
              rs.getLong("id"),
              rs.getString("titulo"),
              rs.getString("descricao"),
              rs.getDouble("nota"),
              rs.getDate("data_entrega").toLocalDate(),
              rs.getDate("data_publicacao").toLocalDate(),
              rs.getLong("curso_id"));
          tarefas.add(tarefa);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Tarefas por curso: " + e.getMessage(), e);
    }
    return tarefas;
  }

  public Tarefa findByTitulo(String nomeTarefa) {
    String sql = "SELECT * FROM Tarefa WHERE titulo = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, nomeTarefa);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Tarefa(
              rs.getLong("id"),
              rs.getString("titulo"),
              rs.getString("descricao"),
              rs.getDouble("nota"),
              rs.getDate("data_entrega").toLocalDate(),
              rs.getDate("data_publicacao").toLocalDate(),
              rs.getLong("curso_id"));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Tarefa por título: " + e.getMessage(), e);
    }
    return null;
  }
}
