package com.mycompany.education.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ValidateData {
  public static Map<String, String> validateFields(JTextField nomeField, JTextField sobrenomeField,
      JTextField emailField,
      JTextField dataNascimentoField, JTextField cpfField, JPasswordField senhaField,
      JPasswordField confirmacaoSenhaField) {
    Map<String, String> errors = new HashMap<>();

    if (nomeField.getText().trim().isEmpty()) {
      errors.put("nome", "Nome é obrigatório.");
    }
    if (sobrenomeField.getText().trim().isEmpty()) {
      errors.put("sobrenome", "Sobrenome é obrigatório.");
    }
    if (!isValidEmail(emailField.getText().trim())) {
      errors.put("email", "Email inválido.");
    }
    if (!isValidDate(dataNascimentoField.getText().trim())) {
      errors.put("dataNascimento", "Data de nascimento inválida.");
    }
    if (!isValidCPF(cpfField.getText().trim())) {
      errors.put("cpf", "CPF inválido.");
    }
    if (new String(senhaField.getPassword()).isEmpty()) {
      errors.put("senha", "Senha é obrigatória.");
    }
    if (!new String(senhaField.getPassword()).equals(new String(confirmacaoSenhaField.getPassword()))) {
      errors.put("confirmacaoSenha", "Confirmação de senha não coincide.");
    }

    return errors;
  }

  private static boolean isValidEmail(String email) {
    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    return Pattern.matches(emailPattern, email);
  }

  private static boolean isValidDate(String date) {
    String datePattern = "^\\d{2}/\\d{2}/\\d{4}$";
    if (!Pattern.matches(datePattern, date)) {
      return false;
    }
    try {
      String[] parts = date.split("/");
      int day = Integer.parseInt(parts[0]);
      int month = Integer.parseInt(parts[1]);
      int year = Integer.parseInt(parts[2]);
      if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2100) {
        return false;
      }
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private static boolean isValidCPF(String cpf) {
    String cpfPattern = "^\\d{11}$";
    return Pattern.matches(cpfPattern, cpf);
  }
}
