package com.mycompany.education;

import javax.swing.JOptionPane;

import com.mycompany.education.connection.MySQLConnection;
import com.mycompany.education.views.TelaInicial;

public class Main {
    public static void main(String args[]){
        try {
            MySQLConnection.getInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao conectar com o banco de dados: " + e.getMessage(), 
                "Erro de Conexão", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        TelaInicial tela = new TelaInicial();
        tela.setVisible(true);
    }
}
