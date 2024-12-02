package expresslink.controllers.cliente;

import java.sql.SQLException;
import java.util.regex.*;
import expresslink.model.Usuario;
import expresslink.utils.DatabaseConnection;
import expresslink.view.cliente.PerfilPanel;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PerfilController {
    private Usuario usuario;
    private PerfilPanel view;

    public PerfilController(Usuario usuario, PerfilPanel view) {
        this.usuario = usuario;
        this.view = view;
    }

    // Actualiza información básica del usuario
    public boolean actualizarInformacionUsuario(String nuevoNombre, String nuevoTelefono)
            throws IllegalArgumentException, SQLException {
        // Validaciones
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            view.mostrarError("El nombre no puede estar vacío");
            return false;
        }
        if (!validarTelefonoChileno(nuevoTelefono)) {
            view.mostrarError("El formato del teléfono debe ser +56XXXXXXXXX");
            return false;
        }

        String query = "UPDATE usuario SET nombre = ?, telefono = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevoTelefono);
            stmt.setInt(3, usuario.getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                usuario.setNombre(nuevoNombre);
                usuario.setTelefono(nuevoTelefono);
                return true;
            }
            return false;
        }
    }

    // Actualiza la contraseña del usuario
    public boolean actualizarContrasena(String contrasenaActual, String nuevaContrasena,
            String confirmacionContrasena) throws IllegalArgumentException, SQLException {

        if (!nuevaContrasena.equals(confirmacionContrasena)) {
            view.mostrarError("Las contraseñas nuevas no coinciden");
            return false;
        }

        if (!verificarContrasenaActual(usuario.getId(), contrasenaActual)) {
            view.mostrarError("La contraseña actual es incorrecta");
            return false;
        }

        if (!validarContrasena(nuevaContrasena)) {
            view.mostrarError("La contraseña no cumple los requisitos mínimos");
            return false;
        }

        String query = "UPDATE usuario SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuevaContrasena);
            stmt.setInt(2, usuario.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // Métodos de validación privados
    private boolean verificarContrasenaActual(int userId, String contrasenaActual) throws SQLException {
        String query = "SELECT COUNT(*) FROM usuario WHERE id = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setString(2, contrasenaActual);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private boolean validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            view.mostrarError("La contraseña no puede estar vacía.");
            return false;
        }

        if (contrasena.length() < 3) {
            view.mostrarError("La contraseña no puede tener menos de 3 caracteres.");
            return false;
        }

        if (contrasena.length() >= 100) {
            view.mostrarError("La contraseña no puede tener más de 100 caracteres.");
            return false;
        }

        // Expresión regular para validar que la contraseña:
        // - No tenga espacios
        // - Solo permita caracteres alfanuméricos y símbolos comunes seguros
        String regex = "^[A-Za-z0-9!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/|~`]+$";
        if (!contrasena.matches(regex)) {
            view.mostrarError("La contraseña contiene caracteres no permitidos o espacios.");
            return false;
        }

        // Si pasa todas las validaciones
        return true;
    }

    private boolean validarTelefonoChileno(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }

        String regex = "^\\+56[2-9][0-9]{8}$";
        return telefono.matches(regex);
    }

    // Método para verificar estado de conexión
    public String obtenerEstadoConexion() {
        return DatabaseConnection.getConnectionStatus();
    }
}