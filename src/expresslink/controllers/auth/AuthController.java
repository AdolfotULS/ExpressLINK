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

    public boolean registrarUsuario(String nombre, String email, String password, String telefono, TipoUsuario rol)
            throws SQLException {
        String query = "INSERT INTO usuario (nombre, email, password, telefono, rol) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            // Primero verificar si el email ya existe
            if (emailExiste(email)) {
                throw new SQLException("El email ya está registrado");
            }

            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, password); // En producción usar hash
            stmt.setString(4, telefono);
            stmt.setString(5, rol.toString());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new SQLException("Error durante el registro: " + e.getMessage(), e);
        }
    }

    private boolean emailExiste(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM usuario WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
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