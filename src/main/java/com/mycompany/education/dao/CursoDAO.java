package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.factories.UsuarioFactoryProvider;
import com.mycompany.education.models.Aluno;
import com.mycompany.education.models.Curso;
import com.mycompany.education.models.EnvioTarefa;
import com.mycompany.education.models.Material;
import com.mycompany.education.models.Tarefa;
import com.mycompany.education.models.Usuario;

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
            curso.materiais(), curso.tarefas(), curso.enviosTarefas());
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
        Usuario professor = new UsuarioDAO().findById(rs.getLong("professor_id"));
        List<Usuario> alunos = findAlunosByCursoId(id);
        List<Material> materiais = findMateriaisByCursoId(id);
        List<Tarefa> tarefas = findTarefasByCursoId(id);
        List<EnvioTarefa> enviosTarefas = findEnviosTarefasByCursoId(id);
        return new Curso(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("descricao"),
            professor,
            alunos,
            materiais,
            tarefas,
            enviosTarefas);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Curso: " + e.getMessage(), e);
    }
    return null;
  }

  public List<Usuario> findAlunosByCursoId(Long cursoId) {
    List<Usuario> alunos = new ArrayList<>();
    String sql = "SELECT u.* FROM Usuario u INNER JOIN Inscricao i ON u.id = i.aluno_id WHERE i.curso_id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, cursoId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Usuario aluno = UsuarioFactoryProvider.getFactory("Aluno").criarUsuario(
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
      throw new RuntimeException("Erro ao encontrar alunos por curso: " + e.getMessage(), e);
    }
    return alunos;
  }

  public List<Material> findMateriaisByCursoId(Long cursoId) {
    List<Material> materiais = new ArrayList<>();
    String sql = "SELECT * FROM Material WHERE curso_id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, cursoId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Material material = new Material(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("conteudo"),
            rs.getDate("data_publicacao").toLocalDate());
        materiais.add(material);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar materiais por curso: " + e.getMessage(), e);
    }
    return materiais;
  }

  public List<Tarefa> findTarefasByCursoId(Long cursoId) {
    List<Tarefa> tarefas = new ArrayList<>();
    String sql = "SELECT * FROM Tarefa WHERE curso_id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, cursoId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Tarefa tarefa = new Tarefa(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("descricao"),
            rs.getDate("data_entrega").toLocalDate(),
            rs.getDate("data_publicacao").toLocalDate());
        tarefas.add(tarefa);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar tarefas por curso: " + e.getMessage(), e);
    }
    return tarefas;
  }

  public List<EnvioTarefa> findEnviosTarefasByCursoId(Long cursoId) {
    List<EnvioTarefa> enviosTarefas = new ArrayList<>();
    String sql = "SELECT et.*, u.*, t.* FROM EnvioTarefa et " +
        "INNER JOIN Usuario u ON et.aluno_id = u.id " +
        "INNER JOIN Tarefa t ON et.tarefa_id = t.id " +
        "WHERE t.curso_id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, cursoId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Aluno aluno = (Aluno) UsuarioFactoryProvider.getFactory("Aluno").criarUsuario(
            rs.getLong("u.id"),
            rs.getString("u.nome"),
            rs.getString("u.sobrenome"),
            rs.getString("u.email"),
            rs.getDate("u.data_nascimento").toLocalDate(),
            rs.getString("u.cpf"),
            rs.getString("u.senha"));

        Tarefa tarefa = new Tarefa(
            rs.getLong("t.id"),
            rs.getString("t.titulo"),
            rs.getString("t.descricao"),
            rs.getDate("t.data_entrega").toLocalDate(),
            rs.getDate("t.data_publicacao").toLocalDate());

        EnvioTarefa envioTarefa = new EnvioTarefa(
            rs.getLong("et.id"),
            aluno,
            tarefa,
            rs.getString("et.resposta"),
            rs.getDate("et.data_envio").toLocalDate(),
            rs.getDouble("et.nota"));
        enviosTarefas.add(envioTarefa);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar envios de tarefas por curso: " + e.getMessage(), e);
    }
    return enviosTarefas;
  }

  @Override
  public List<Curso> findAll() {
    List<Curso> cursos = new ArrayList<>();
    String sql = "SELECT * FROM Curso";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Long cursoId = rs.getLong("id");
        Usuario professor = new UsuarioDAO().findById(rs.getLong("professor_id"));
        List<Usuario> alunos = findAlunosByCursoId(cursoId);
        List<Material> materiais = findMateriaisByCursoId(cursoId);
        List<Tarefa> tarefas = findTarefasByCursoId(cursoId);
        List<EnvioTarefa> enviosTarefas = findEnviosTarefasByCursoId(cursoId);

        Curso curso = new Curso(
            cursoId,
            rs.getString("titulo"),
            rs.getString("descricao"),
            professor,
            alunos,
            materiais,
            tarefas,
            enviosTarefas);
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