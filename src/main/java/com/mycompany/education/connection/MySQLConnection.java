package com.mycompany.education.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class MySQLConnection {

  private static MySQLConnection instance;
  private Connection connection;
  private String url;
  private String username;
  private String password;

  private MySQLConnection() throws SQLException {
    try {
      Dotenv dotenv = Dotenv.load();
      this.url = dotenv.get("DB_URL");
      this.username = dotenv.get("DB_USERNAME");
      this.password = dotenv.get("DB_PASSWORD");

      Class.forName("com.mysql.cj.jdbc.Driver");
      this.connection = DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException e) {
      throw new SQLException(e);
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public static MySQLConnection getInstance() throws SQLException {
    if (instance == null) {
      synchronized (MySQLConnection.class) {
        if (instance == null) {
          instance = new MySQLConnection();
        }
      }
    } else if (instance.getConnection().isClosed()) {
      instance = new MySQLConnection();
    }
    return instance;
  }
}