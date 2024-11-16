package expresslink.controllers.auth;

import expresslink.model.*;
import expresslink.model.enums.*;
import expresslink.utils.DatabaseConnection;
import java.sql.*;

public class AuthController {
    public Usuario inicioSesion(String email, String password) throws SQLException {
        String query = "SELECT id, nombre, email, telefono, rol FROM usuario WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password); // En producción usar hash

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            TipoUsuario.valueOf(rs.getString("rol")));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error durante el login", e);
        }
    }

    public boolean validarCredenciales(String email, String password) {
        try {
            return inicioSesion(email, password) != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cerrarSesion() {
        DatabaseConnection.closeConnection();
    }
}