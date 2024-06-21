package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.models.EnvioTarefa;
import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Tarefa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnvioTarefaDAO implements GenericDAO<EnvioTarefa, Long> {

    @Override
    public void create(EnvioTarefa envioTarefa) {
        String sql = "INSERT INTO enviotarefa (aluno_id, tarefa_id, resposta, data_envio, nota) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, envioTarefa.aluno().id());
            stmt.setLong(2, envioTarefa.tarefa().id());
            stmt.setString(3, envioTarefa.resposta());
            stmt.setDate(4, Date.valueOf(envioTarefa.dataEnvio()));
            if (envioTarefa.nota() != null) {
                stmt.setDouble(5, envioTarefa.nota());
            } else {
                stmt.setNull(5, Types.DOUBLE);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    envioTarefa = new EnvioTarefa(
                            generatedKeys.getLong(1),
                            envioTarefa.aluno(),
                            envioTarefa.tarefa(),
                            envioTarefa.resposta(),
                            envioTarefa.dataEnvio(),
                            envioTarefa.nota());
                } else {
                    throw new SQLException("Falha ao obter o ID gerado para Envio de Tarefa.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar Envio de Tarefa: " + e.getMessage(), e);
        }
    }

    @Override
    public EnvioTarefa findById(Long id) {
        EnvioTarefa envio = null;
        String sql = "SELECT et.*, " +
                "u.id AS aluno_id, u.nome AS aluno_nome, u.sobrenome AS aluno_sobrenome, " +
                "u.email AS aluno_email, u.data_nascimento AS aluno_data_nascimento, u.cpf AS aluno_cpf, " +
                "u.senha AS aluno_senha, " +
                "t.id AS tarefa_id, t.titulo AS tarefa_titulo, " +
                "t.descricao AS tarefa_descricao, t.nota AS tarefa_nota, " +
                "t.data_entrega AS tarefa_data_entrega, t.data_publicacao AS tarefa_data_publicacao, " +
                "t.curso_id AS tarefa_curso_id " +
                "FROM enviotarefa et " +
                "JOIN usuario u ON et.aluno_id = u.id " +
                "JOIN tarefa t ON et.tarefa_id = t.id " +
                "WHERE et.id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Aluno aluno = new Aluno(
                            rs.getLong("aluno_id"),
                            rs.getString("aluno_nome"),
                            rs.getString("aluno_sobrenome"),
                            rs.getString("aluno_email"),
                            rs.getDate("aluno_data_nascimento").toLocalDate(),
                            rs.getString("aluno_cpf"),
                            rs.getString("aluno_senha"),
                            new ArrayList<>());
                    Tarefa tarefa = new Tarefa(
                            rs.getLong("tarefa_id"),
                            rs.getString("tarefa_titulo"),
                            rs.getString("tarefa_descricao"),
                            rs.getDouble("tarefa_nota"),
                            rs.getDate("tarefa_data_entrega").toLocalDate(),
                            rs.getDate("tarefa_data_publicacao").toLocalDate(),
                            rs.getLong("tarefa_curso_id"));
                    envio = new EnvioTarefa(
                            rs.getLong("id"),
                            aluno,
                            tarefa,
                            rs.getString("resposta"),
                            rs.getDate("data_envio").toLocalDate(),
                            rs.getDouble("nota"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar Envio de Tarefa por ID: " + e.getMessage(), e);
        }
        return envio;
    }

    @Override
    public List<EnvioTarefa> findAll() {
        List<EnvioTarefa> envios = new ArrayList<>();
        String sql = "SELECT et.*, " +
                "u.id AS aluno_id, u.nome AS aluno_nome, u.sobrenome AS aluno_sobrenome, " +
                "u.email AS aluno_email, u.data_nascimento AS aluno_data_nascimento, u.cpf AS aluno_cpf, " +
                "u.senha AS aluno_senha, " +
                "t.id AS tarefa_id, t.titulo AS tarefa_titulo, " +
                "t.descricao AS tarefa_descricao, t.nota AS tarefa_nota, " +
                "t.data_entrega AS tarefa_data_entrega, t.data_publicacao AS tarefa_data_publicacao, " +
                "t.curso_id AS tarefa_curso_id " +
                "FROM enviotarefa et " +
                "JOIN usuario u ON et.aluno_id = u.id " +
                "JOIN tarefa t ON et.tarefa_id = t.id";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getLong("aluno_id"),
                        rs.getString("aluno_nome"),
                        rs.getString("aluno_sobrenome"),
                        rs.getString("aluno_email"),
                        rs.getDate("aluno_data_nascimento").toLocalDate(),
                        rs.getString("aluno_cpf"),
                        rs.getString("aluno_senha"),
                        new ArrayList<>());
                Tarefa tarefa = new Tarefa(
                        rs.getLong("tarefa_id"),
                        rs.getString("tarefa_titulo"),
                        rs.getString("tarefa_descricao"),
                        rs.getDouble("tarefa_nota"),
                        rs.getDate("tarefa_data_entrega").toLocalDate(),
                        rs.getDate("tarefa_data_publicacao").toLocalDate(),
                        rs.getLong("tarefa_curso_id"));
                EnvioTarefa envio = new EnvioTarefa(
                        rs.getLong("id"),
                        aluno,
                        tarefa,
                        rs.getString("resposta"),
                        rs.getDate("data_envio").toLocalDate(),
                        rs.getDouble("nota"));
                envios.add(envio);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar Envios de Tarefas: " + e.getMessage(), e);
        }
        return envios;
    }

    public List<EnvioTarefa> findAllByAluno(Long alunoId) {
        List<EnvioTarefa> envios = new ArrayList<>();
        String sql = "SELECT et.*, " +
                "u.id AS aluno_id, u.nome AS aluno_nome, u.sobrenome AS aluno_sobrenome, " +
                "u.email AS aluno_email, u.data_nascimento AS aluno_data_nascimento, u.cpf AS aluno_cpf, " +
                "u.senha AS aluno_senha, " +
                "t.id AS tarefa_id, t.titulo AS tarefa_titulo, " +
                "t.descricao AS tarefa_descricao, t.nota AS tarefa_nota, " +
                "t.data_entrega AS tarefa_data_entrega, t.data_publicacao AS tarefa_data_publicacao, " +
                "t.curso_id AS tarefa_curso_id " +
                "FROM enviotarefa et " +
                "JOIN usuario u ON et.aluno_id = u.id " +
                "JOIN tarefa t ON et.tarefa_id = t.id " +
                "WHERE et.aluno_id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, alunoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Aluno aluno = new Aluno(
                            rs.getLong("aluno_id"),
                            rs.getString("aluno_nome"),
                            rs.getString("aluno_sobrenome"),
                            rs.getString("aluno_email"),
                            rs.getDate("aluno_data_nascimento").toLocalDate(),
                            rs.getString("aluno_cpf"),
                            rs.getString("aluno_senha"),
                            new ArrayList<>());
                    Tarefa tarefa = new Tarefa(
                            rs.getLong("tarefa_id"),
                            rs.getString("tarefa_titulo"),
                            rs.getString("tarefa_descricao"),
                            rs.getDouble("tarefa_nota"),
                            rs.getDate("tarefa_data_entrega").toLocalDate(),
                            rs.getDate("tarefa_data_publicacao").toLocalDate(),
                            rs.getLong("tarefa_curso_id")); // Corrigido aqui
                    EnvioTarefa envio = new EnvioTarefa(
                            rs.getLong("id"),
                            aluno,
                            tarefa,
                            rs.getString("resposta"),
                            rs.getDate("data_envio").toLocalDate(),
                            rs.getDouble("nota"));
                    envios.add(envio);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar Envios de Tarefas do Aluno: " + e.getMessage(), e);
        }
        return envios;
    }

    @Override
    public void update(EnvioTarefa envioTarefa) {
        String sql = "UPDATE enviotarefa SET resposta = ?, data_envio = ?, nota = ? WHERE id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, envioTarefa.resposta());
            stmt.setDate(2, Date.valueOf(envioTarefa.dataEnvio()));
            if (envioTarefa.nota() != null) {
                stmt.setDouble(3, envioTarefa.nota());
            } else {
                stmt.setNull(3, Types.DOUBLE);
            }
            stmt.setLong(4, envioTarefa.id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Envio de Tarefa: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(EnvioTarefa envioTarefa) {
        String sql = "DELETE FROM enviotarefa WHERE id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, envioTarefa.id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar Envio de Tarefa: " + e.getMessage(), e);
        }
    }

    public List<EnvioTarefa> findAllByTarefa(Long id) {
        List<EnvioTarefa> envios = new ArrayList<>();
        String sql = "SELECT et.*, " +
                "u.id AS aluno_id, u.nome AS aluno_nome, u.sobrenome AS aluno_sobrenome, " +
                "u.email AS aluno_email, u.data_nascimento AS aluno_data_nascimento, u.cpf AS aluno_cpf, " +
                "u.senha AS aluno_senha, " +
                "t.id AS tarefa_id, t.titulo AS tarefa_titulo, " +
                "t.descricao AS tarefa_descricao, t.nota AS tarefa_nota, " +
                "t.data_entrega AS tarefa_data_entrega, t.data_publicacao AS tarefa_data_publicacao " +
                "FROM enviotarefa et " +
                "JOIN usuario u ON et.aluno_id = u.id " +
                "JOIN tarefa t ON et.tarefa_id = t.id " +
                "WHERE et.tarefa_id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Aluno aluno = new Aluno(
                            rs.getLong("aluno_id"),
                            rs.getString("aluno_nome"),
                            rs.getString("aluno_sobrenome"),
                            rs.getString("aluno_email"),
                            rs.getDate("aluno_data_nascimento").toLocalDate(),
                            rs.getString("aluno_cpf"),
                            rs.getString("aluno_senha"),
                            new ArrayList<>());
                    Tarefa tarefa = new Tarefa(
                            rs.getLong("tarefa_id"),
                            rs.getString("tarefa_titulo"),
                            rs.getString("tarefa_descricao"),
                            rs.getDouble("tarefa_nota"),
                            rs.getDate("tarefa_data_entrega").toLocalDate(),
                            rs.getDate("tarefa_data_publicacao").toLocalDate(),
                            rs.getLong("tarefa_curso_id"));
                    EnvioTarefa envio = new EnvioTarefa(
                            rs.getLong("id"),
                            aluno,
                            tarefa,
                            rs.getString("resposta"),
                            rs.getDate("data_envio").toLocalDate(),
                            rs.getDouble("nota"));
                    envios.add(envio);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar Envios de Tarefas para a Tarefa: " + e.getMessage(), e);
        }
        return envios;
    }

    public EnvioTarefa findByAlunoAndTarefa(Long alunoId, Long tarefaId) {
        String sql = "SELECT et.*, " +
                "u.id AS aluno_id, u.nome AS aluno_nome, u.sobrenome AS aluno_sobrenome, " +
                "u.email AS aluno_email, u.data_nascimento AS aluno_data_nascimento, u.cpf AS aluno_cpf, " +
                "u.senha AS aluno_senha, " +
                "t.id AS tarefa_id, t.titulo AS tarefa_titulo, " +
                "t.descricao AS tarefa_descricao, t.nota AS tarefa_nota, " +
                "t.data_entrega AS tarefa_data_entrega, t.data_publicacao AS tarefa_data_publicacao, " +
                "t.curso_id AS tarefa_curso_id " +
                "FROM enviotarefa et " +
                "JOIN usuario u ON et.aluno_id = u.id " +
                "JOIN tarefa t ON et.tarefa_id = t.id " +
                "WHERE et.aluno_id = ? AND et.tarefa_id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, alunoId);
            stmt.setLong(2, tarefaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Aluno aluno = new Aluno(
                            rs.getLong("aluno_id"),
                            rs.getString("aluno_nome"),
                            rs.getString("aluno_sobrenome"),
                            rs.getString("aluno_email"),
                            rs.getDate("aluno_data_nascimento").toLocalDate(),
                            rs.getString("aluno_cpf"),
                            rs.getString("aluno_senha"),
                            new ArrayList<>());
                    Tarefa tarefa = new Tarefa(
                            rs.getLong("tarefa_id"),
                            rs.getString("tarefa_titulo"),
                            rs.getString("tarefa_descricao"),
                            rs.getDouble("tarefa_nota"),
                            rs.getDate("tarefa_data_entrega").toLocalDate(),
                            rs.getDate("tarefa_data_publicacao").toLocalDate(),
                            rs.getLong("tarefa_curso_id"));
                    return new EnvioTarefa(
                            rs.getLong("id"),
                            aluno,
                            tarefa,
                            rs.getString("resposta"),
                            rs.getDate("data_envio").toLocalDate(),
                            rs.getDouble("nota"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar Envio de Tarefa por Aluno e Tarefa: " + e.getMessage(), e);
        }
        return null;
    }

    public List<EnvioTarefa> findByTarefaId(Long tarefaId) {
        List<EnvioTarefa> envios = new ArrayList<>();
        String sql = "SELECT et.*, " +
                "u.id AS aluno_id, u.nome AS aluno_nome, u.sobrenome AS aluno_sobrenome, " +
                "u.email AS aluno_email, u.data_nascimento AS aluno_data_nascimento, u.cpf AS aluno_cpf, " +
                "u.senha AS aluno_senha, " +
                "t.id AS tarefa_id, t.titulo AS tarefa_titulo, " +
                "t.descricao AS tarefa_descricao, t.nota AS tarefa_nota, " +
                "t.data_entrega AS tarefa_data_entrega, t.data_publicacao AS tarefa_data_publicacao, " +
                "t.curso_id AS tarefa_curso_id " +
                "FROM enviotarefa et " +
                "JOIN usuario u ON et.aluno_id = u.id " +
                "JOIN tarefa t ON et.tarefa_id = t.id " +
                "WHERE et.tarefa_id = ?";
        try (Connection conn = MySQLConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, tarefaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Aluno aluno = new Aluno(
                            rs.getLong("aluno_id"),
                            rs.getString("aluno_nome"),
                            rs.getString("aluno_sobrenome"),
                            rs.getString("aluno_email"),
                            rs.getDate("aluno_data_nascimento").toLocalDate(),
                            rs.getString("aluno_cpf"),
                            rs.getString("aluno_senha"),
                            new ArrayList<>());
                    Tarefa tarefa = new Tarefa(
                            rs.getLong("tarefa_id"),
                            rs.getString("tarefa_titulo"),
                            rs.getString("tarefa_descricao"),
                            rs.getDouble("tarefa_nota"),
                            rs.getDate("tarefa_data_entrega").toLocalDate(),
                            rs.getDate("tarefa_data_publicacao").toLocalDate(),
                            rs.getLong("tarefa_curso_id"));
                    EnvioTarefa envio = new EnvioTarefa(
                            rs.getLong("id"),
                            aluno,
                            tarefa,
                            rs.getString("resposta"),
                            rs.getDate("data_envio").toLocalDate(),
                            rs.getDouble("nota"));
                    envios.add(envio);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar Envios de Tarefas por ID da Tarefa: " + e.getMessage(), e);
        }
        return envios;
    }

}
