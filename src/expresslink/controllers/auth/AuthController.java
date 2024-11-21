package expresslink.controllers.auth;

import expresslink.model.*;
import expresslink.model.enums.*;
import expresslink.utils.DatabaseConnection;
import java.sql.*;

public class AuthController {
    public Usuario inicioSesion(String email, String password) throws SQLException {
        String query = "SELECT usuario.id, usuario.nombre, usuario.telefono, usuario.rol, usuario.sucursal_id FROM usuario WHERE usuario.email = ? AND usuario.`password` = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            email,
                            password,
                            rs.getString("telefono"),
                            RolUsuario.valueOf(rs.getString("rol")),
                            rs.getInt("sucursal_id") == 0 ? null
                                    : obteneSucursal(rs.getInt("sucursal_id")));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error durante el login [Inicio Sesion]", e);
        }
    }

    // public Transportista obtenerTransportista(Usuario usuario) {
    // String query = "";
    // try (Connection conn = DatabaseConnection.getConnection();
    // PreparedStatement stmt = conn.prepareStatement(query)) {
    // stmt.setString(1, email);
    // } catch (SQLException e) {
    // throw new SQLException("Error durante recoleccion de datos [Transportista]",
    // e);ow
    // }

    // return null;
    // }

    public Sucursal obteneSucursal(int id) throws SQLException {
        if (id <= 0)
            return null;
        String query = "SELECT sucursal.nombre, sucursal.direccion, sucursal.ciudad FROM sucursal WHERE sucursal.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Sucursal(id,
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("ciudad"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error durante el login [Sucursal]", e);
        }
    }

    public boolean registrarUsuario(Usuario nuevoUsuario) throws SQLException {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciamos una transacción

            // Primero verificamos si el email existe
            String checkQuery = "SELECT COUNT(*) FROM usuario WHERE email = ?";
            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, nuevoUsuario.getEmail());
            rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("El email ya está registrado");
            }

            // Si no existe, procedemos con el registro
            String insertQuery = "INSERT INTO USUARIO (nombre, email, password, telefono, rol, sucursal_id) VALUES (?, ?, ?, ?, 'CLIENTE', NULL)";
            insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, nuevoUsuario.getNombre());
            insertStmt.setString(2, nuevoUsuario.getEmail());
            insertStmt.setString(3, nuevoUsuario.getPassword());
            insertStmt.setString(4, nuevoUsuario.getTelefono());

            int filasAfectadas = insertStmt.executeUpdate();

            conn.commit(); // Confirmamos la transacción
            return filasAfectadas > 0;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Si hay error, revertimos la transacción
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new SQLException("Error durante el registro: " + e.getMessage(), e);
        } finally {
            // Cerramos todos los recursos en orden inverso
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (checkStmt != null) {
                try {
                    checkStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (insertStmt != null) {
                try {
                    insertStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String recuperarContrasena(String email) throws SQLException {
        String query = "SELECT password FROM usuario WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    return enmascararContrasena(password);
                }
                throw new SQLException("No se encontró ningún usuario con ese email");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al recuperar la contraseña: " + e.getMessage());
        }
    }

    private String enmascararContrasena(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }

        int longitud = password.length();
        int mitad = longitud / 2;

        // Mostrar la primera mitad y reemplazar el resto con asteriscos
        StringBuilder resultado = new StringBuilder();
        resultado.append(password.substring(0, mitad));
        for (int i = mitad; i < longitud; i++) {
            resultado.append('*');
        }

        return resultado.toString();
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