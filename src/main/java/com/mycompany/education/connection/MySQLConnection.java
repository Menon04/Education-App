package com.mycompany.education.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

  private static MySQLConnection instance;
  private Connection connection;
  private String url = "jdbc:mysql://localhost:3306/education_db";
  private String username = "root";
  private String password = "m666";

  private MySQLConnection() throws SQLException {
    try {
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