package com.example.testechatgemini;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Postgresql {
    public static void main(String[] args) {
        Properties props = new Properties();

        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);

            String url = props.getProperty("db.url");
            String usuario = props.getProperty("db.user");
            String senha = props.getProperty("db.password");

            try (Connection conexao = DriverManager.getConnection(url, usuario, senha)) {
                System.out.println("Conexão bem-sucedida com o PostgreSQL!");
            }
        } catch (IOException | SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados:");
            e.printStackTrace();
        }
    }


}
