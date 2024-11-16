package expresslink.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/expresslink";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getConnectionStatus() {
        try {
            if (connection == null) {
                return "Estado de la conexion: No se ha inicializado ninguna conexion.";
            } else if (connection.isClosed()) {
                return "Estado de la conexion: La conexion esta cerrada.";
            } else if (connection.isValid(2)) { // Verifica si la conexion es valida en 2 segundos
                return "Estado de la conexion: Conectado a la base de datos en " + URL;
            } else {
                return "Estado de la conexion: La conexion no es valida.";
            }
        } catch (SQLException e) {
            return "Estado de la conexion: Error al verificar el estado de la conexion. Detalles: " + e.getMessage();
        }
    }
}
