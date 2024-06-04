package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.models.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO implements GenericDAO<Admin, Long> {

  @Override
  public void create(Admin entity) {
    String sql = "INSERT INTO Admin (email, senha) VALUES (?, ?)";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, entity.email());
      stmt.setString(2, entity.senha());
      stmt.executeUpdate();
      
      ResultSet generatedKeys = stmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        Long id = generatedKeys.getLong(1);
        entity = new Admin(id, entity.email(), entity.senha());
      } else {
        throw new SQLException("Falha ao obter o ID gerado para Admin.");
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar Admin: " + e.getMessage(), e);
    }
  }

  @Override
  public Admin findById(Long id) {
    String sql = "SELECT * FROM Admin WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return new Admin(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("senha"));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Admin: " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<Admin> findAll() {
    List<Admin> admins = new ArrayList<>();
    String sql = "SELECT * FROM Admin";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Admin admin = new Admin(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("senha"));
        admins.add(admin);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar todos os Admins: " + e.getMessage(), e);
    }
    return admins;
  }

  @Override
  public void update(Admin entity) {
    String sql = "UPDATE Admin SET email = ?, senha = ? WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.email());
      stmt.setString(2, entity.senha());
      stmt.setLong(3, entity.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao atualizar Admin: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(Admin entity) {
    String sql = "DELETE FROM Admin WHERE id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, entity.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar Admin: " + e.getMessage(), e);
    }
  }
}
