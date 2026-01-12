package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://bq8w1cisdiqk6fgpyktc-mysql.services.clever-cloud.com:3306/bq8w1cisdiqk6fgpyktc" + "?useSSL=true" + "&requireSSL=true" + "&verifyServerCertificate=false" + "&serverTimezone=UTC";
    private static final String User = "uv2saymxwwmdcecv";
    private static final String Pass = "Viy1JaxPlfgS89av3OBX";

    public Connection getConexion() {
        try {
            Connection con = DriverManager.getConnection(URL, User, Pass);
            System.out.println("CONEXIÓN EXITOSA A MYSQL");
            return con;
        } catch (SQLException e) {
            System.out.println("ERROR DE CONEXIÓN");
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Código SQL: " + e.getSQLState());
            return null;
        }
    }
}