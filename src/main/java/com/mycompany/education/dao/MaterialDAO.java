package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.models.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO implements GenericDAO<Material, Long> {

    @Override
    public void create(Material material) {
        String sql = "INSERT INTO material (titulo, conteudo, data_publicacao, professor_id, curso_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, material.titulo());
            stmt.setString(2, material.conteudo());
            stmt.setDate(3, Date.valueOf(material.dataPublicacao()));
            stmt.setLong(4, material.professorId());
            stmt.setLong(5, material.cursoId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    material = new Material(
                            generatedKeys.getLong(1),
                            material.titulo(),
                            material.conteudo(),
                            material.dataPublicacao(),
                            material.professorId(),
                            material.cursoId());
                } else {
                    throw new SQLException("Falha ao obter o ID gerado para Material.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar Material: " + e.getMessage(), e);
        }
    }

    @Override
    public Material findById(Long id) {
        String sql = "SELECT * FROM material WHERE id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Material(
                            rs.getLong("id"),
                            rs.getString("titulo"),
                            rs.getString("conteudo"),
                            rs.getDate("data_publicacao").toLocalDate(),
                            rs.getLong("professor_id"),
                            rs.getLong("curso_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar Material: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Material> findAll() {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT * FROM material";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Material material = new Material(
                        rs.getLong("id"),
                        rs.getString("titulo"),
                        rs.getString("conteudo"),
                        rs.getDate("data_publicacao").toLocalDate(),
                        rs.getLong("professor_id"),
                        rs.getLong("curso_id"));
                materiais.add(material);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar todos os Materiais: " + e.getMessage(), e);
        }
        return materiais;
    }

    public List<Material> findAllByCurso(Long cursoId) {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT * FROM material WHERE curso_id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cursoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Material material = new Material(
                            rs.getLong("id"),
                            rs.getString("titulo"),
                            rs.getString("conteudo"),
                            rs.getDate("data_publicacao").toLocalDate(),
                            rs.getLong("professor_id"),
                            rs.getLong("curso_id"));
                    materiais.add(material);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar Materiais do Curso: " + e.getMessage(), e);
        }
        return materiais;
    }

    @Override
    public void update(Material material) {
        String sql = "UPDATE material SET titulo = ?, conteudo = ?, data_publicacao = ?, professor_id = ?, curso_id = ? WHERE id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, material.titulo());
            stmt.setString(2, material.conteudo());
            stmt.setDate(3, Date.valueOf(material.dataPublicacao()));
            stmt.setLong(4, material.professorId());
            stmt.setLong(5, material.cursoId());
            stmt.setLong(6, material.id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Material: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Material material) {
        String sql = "DELETE FROM material WHERE id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, material.id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar Material: " + e.getMessage(), e);
        }
    }
}
