package com.mycompany.education;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.views.TelaInicial;

public class Main {
    public static void main(String args[]){
        try {
            MySQLConnection.getInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
        
        TelaInicial tela = new TelaInicial();
        tela.setVisible(true);
    }
}
