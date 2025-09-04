package com.univille.br;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    // Ajuste para o seu ambiente
    private static final String URL = "jdbc:mysql://localhost:3306/imobiliaria";
    private static final String USER = "root";
    private static final String PASS = "univille";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
