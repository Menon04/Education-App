package com.mycompany.education.dao;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.factories.UsuarioFactoryProvider;
import com.mycompany.education.models.*;

import java.sql.*;
import java.util.*;

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

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          Long id = generatedKeys.getLong(1);
          curso = new Curso(id, curso.titulo(), curso.descricao(), curso.professor(), curso.alunosInscritos(),
              curso.materiais(), curso.tarefas(), curso.enviosTarefas());
        } else {
          throw new SQLException("Falha ao obter o ID gerado para Curso.");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar Curso: " + e.getMessage(), e);
    }
  }

  @Override
  public Curso findById(Long id) {
    String sql = "SELECT * FROM Curso WHERE id = ?";
    Curso curso = null;

    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          Long professorId = rs.getLong("professor_id");
          Usuario professor = new UsuarioDAO().findById(professorId);

          curso = new Curso(
              rs.getLong("id"),
              rs.getString("titulo"),
              rs.getString("descricao"),
              professor,
              new ArrayList<>(), // alunosInscritos
              new ArrayList<>(), // materiais
              new ArrayList<>(), // tarefas
              new ArrayList<>() // enviosTarefas
          );
        }
      }

      if (curso != null) {
        curso.alunosInscritos().addAll(findAlunosByCursoId(curso.id()));
        curso.materiais().addAll(findMateriaisByCursoId(curso.id()));
        curso.tarefas().addAll(findTarefasByCursoId(curso.id()));
        curso.enviosTarefas().addAll(findEnviosTarefasByCursoId(curso.id()));
      }

    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Curso: " + e.getMessage(), e);
    }

    return curso;
  }

  public List<Usuario> findAlunosByCursoId(Long cursoId) {
    List<Usuario> alunos = new ArrayList<>();
    String sql = "SELECT u.* FROM Usuario u INNER JOIN Inscricao i ON u.id = i.aluno_id WHERE i.curso_id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, cursoId);
      try (ResultSet rs = stmt.executeQuery()) {
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
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Material material = new Material(
              rs.getLong("id"),
              rs.getString("titulo"),
              rs.getString("conteudo"),
              rs.getDate("data_publicacao").toLocalDate(),
              rs.getLong("professor_id"),
              rs.getLong("curso_id") 
          );
          materiais.add(material);
        }
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
      try (ResultSet rs = stmt.executeQuery()) {
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
              rs.getDouble("t.nota"),
              rs.getDate("t.data_entrega").toLocalDate(),
              rs.getDate("t.data_publicacao").toLocalDate(),
              rs.getLong("curso_id"));

          EnvioTarefa envioTarefa = new EnvioTarefa(
              rs.getLong("et.id"),
              aluno,
              tarefa,
              rs.getString("et.resposta"),
              rs.getDate("et.data_envio").toLocalDate(),
              rs.getDouble("et.nota"));
          enviosTarefas.add(envioTarefa);
        }
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
    Map<Long, Long> cursoProfessorMap = new HashMap<>();
    Map<Long, String[]> cursoDataMap = new HashMap<>();

    try (Connection conn = MySQLConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Long cursoId = rs.getLong("id");
        Long professorId = rs.getLong("professor_id");

        cursoProfessorMap.put(cursoId, professorId);
        cursoDataMap.put(cursoId, new String[] {
            rs.getString("titulo"),
            rs.getString("descricao")
        });
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar todos os Cursos: " + e.getMessage(), e);
    }

    Map<Long, Usuario> usuarioCache = new HashMap<>();

    for (Map.Entry<Long, String[]> entry : cursoDataMap.entrySet()) {
      Long cursoId = entry.getKey();
      String[] data = entry.getValue();
      Long professorId = cursoProfessorMap.get(cursoId);

      Usuario professor = usuarioCache.computeIfAbsent(professorId, id -> {
        try {
          return new UsuarioDAO().findById(id);
        } catch (RuntimeException e) {
          throw new RuntimeException("Erro ao carregar professor para o curso: " + cursoId, e);
        }
      });

      Curso curso = new Curso(
          cursoId,
          data[0],
          data[1],
          professor,
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>());

      try {
        curso.alunosInscritos().addAll(findAlunosByCursoId(curso.id()));
        curso.materiais().addAll(findMateriaisByCursoId(curso.id()));
        curso.tarefas().addAll(findTarefasByCursoId(curso.id()));
        curso.enviosTarefas().addAll(findEnviosTarefasByCursoId(curso.id()));
      } catch (Exception e) {
        throw new RuntimeException("Erro ao carregar dados relacionados para o curso: " + curso.id(), e);
      }

      cursos.add(curso);
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

  public void inscreverAluno(Curso curso, Usuario aluno) {
    String sql = "INSERT INTO Inscricao (curso_id, aluno_id) VALUES (?, ?)";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, curso.id());
      stmt.setLong(2, aluno.id());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao inscrever aluno no curso: " + e.getMessage(), e);
    }
  }

  public List<Curso> findCursosByProfessorId(Long professorId) {
    List<Curso> cursos = new ArrayList<>();
    String sql = "SELECT * FROM Curso WHERE professor_id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, professorId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Curso curso = new Curso(
              rs.getLong("id"),
              rs.getString("titulo"),
              rs.getString("descricao"),
              new UsuarioDAO().findById(professorId),
              new ArrayList<>(), // Alunos
              new ArrayList<>(), // Materiais
              new ArrayList<>(), // Tarefas
              new ArrayList<>() // Envios de Tarefas
          );
          cursos.add(curso);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar cursos do professor: " + e.getMessage(), e);
    }
    return cursos;
  }

  public Curso findCursoByTitulo(String titulo) {
    String sql = "SELECT * FROM Curso WHERE titulo = ?";
    Curso curso = null;
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, titulo);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          Long professorId = rs.getLong("professor_id");
          Usuario professor = new UsuarioDAO().findById(professorId);
          curso = new Curso(
              rs.getLong("id"),
              rs.getString("titulo"),
              rs.getString("descricao"),
              professor,
              new ArrayList<>(), // alunosInscritos
              new ArrayList<>(), // materiais
              new ArrayList<>(), // tarefas
              new ArrayList<>() // enviosTarefas
          );

          curso.alunosInscritos().addAll(findAlunosByCursoId(curso.id()));
          curso.materiais().addAll(findMateriaisByCursoId(curso.id()));
          curso.tarefas().addAll(findTarefasByCursoId(curso.id()));
          curso.enviosTarefas().addAll(findEnviosTarefasByCursoId(curso.id()));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao encontrar Curso pelo título: " + e.getMessage(), e);
    }
    return curso;
  }

  public int countAlunosInscritos(Long cursoId) {
    String sql = "SELECT COUNT(*) AS total FROM Inscricao WHERE curso_id = ?";
    try (Connection conn = MySQLConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, cursoId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("total");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao contar alunos inscritos: " + e.getMessage(), e);
    }
    return 0;
  }

}
