package com.mycompany.education.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.mycompany.education.utils.HashPassword;

public class CadastroController {

  public static void salvarCadastro(String nome, String sobrenome, String email, String dataNascimento, String cpf, String senha) {
    String hashedSenha = HashPassword.hashPassword(senha);
    
    Map<String, String> cadastroMap = new HashMap<>();
    cadastroMap.put("nome", nome);
    cadastroMap.put("sobrenome", sobrenome);
    cadastroMap.put("email", email);
    cadastroMap.put("data_de_nascimento", dataNascimento);
    cadastroMap.put("cpf", cpf);
    cadastroMap.put("senha", hashedSenha);

    JSONObject cadastroJSON = new JSONObject(cadastroMap);

    String path = Paths.get("src", "main", "java", "com", "mycompany", "education", "api", "cadastro.json").toString();

    try (FileWriter file = new FileWriter(path)) {
      file.write(cadastroJSON.toJSONString());
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar cadastro", e);
    }
  }
}