package expresslink.controllers.cliente;

import java.sql.SQLException;
import java.util.regex.Pattern;
import expresslink.model.Usuario;
import expresslink.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PerfilController {

    public boolean actualizarInformacionUsuario(Usuario usuario, String nuevoNombre, String nuevoTelefono)
            throws SQLException {
        if (!validarDatosBasicos(nuevoNombre, nuevoTelefono)) {
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
                // Actualizar el objeto Usuario si la actualización en BD fue exitosa
                usuario.setNombre(nuevoNombre);
                usuario.setTelefono(nuevoTelefono);
                return true;
            }

            return false;
        }
    }

    public boolean actualizarContrasena(Usuario usuario, String contrasenaActual,
            String nuevaContrasena, String confirmacionContrasena) throws SQLException {

        // Validar que las contraseñas nuevas coincidan
        if (!nuevaContrasena.equals(confirmacionContrasena)) {
            throw new IllegalArgumentException("Las contraseñas nuevas no coinciden");
        }

        // Validar la contraseña actual
        if (!verificarContrasenaActual(usuario.getId(), contrasenaActual)) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        // Validar la nueva contraseña
        if (!validarContrasena(nuevaContrasena)) {
            throw new IllegalArgumentException("La nueva contraseña no cumple con los requisitos mínimos");
        }

        String query = "UPDATE usuario SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuevaContrasena); // En producción, aquí se debería hashear la contraseña
            stmt.setInt(2, usuario.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

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

    private boolean validarDatosBasicos(String nombre, String telefono) {
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (!validarTelefonoChileno(telefono)) {
            throw new IllegalArgumentException("El formato del teléfono debe ser +56XXXXXXXXX");
        }

        return true;
    }

    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(regex);
    }

    private boolean validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            mostrarError("La contraseña no puede estar vacía.");
            return false;
        }

        if (contrasena.length() < 3) {
            mostrarError("La contraseña no puede tener menos de 3 caracteres.");
            return false;
        }

        if (contrasena.length() >= 100) {
            mostrarError("La contraseña no puede tener más de 100 caracteres.");
            return false;
        }

        // Expresión regular para validar que la contraseña:
        // - No tenga espacios
        // - Solo permita caracteres alfanuméricos y símbolos comunes seguros
        String regex = "^[A-Za-z0-9!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/|~`]+$";
        if (!contrasena.matches(regex)) {
            mostrarError("La contraseña contiene caracteres no permitidos o espacios.");
            return false;
        }

        // Si pasa todas las validaciones
        return true;
    }

    private boolean validarTelefonoChileno(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            mostrarError("El número de teléfono no puede estar vacío.");
            return false;
        }

        // Expresión regular para números de teléfono chilenos
        // - Deben comenzar con +56
        // - Luego tienen que tener 9 dígitos después del prefijo internacional
        String regex = "^\\+56[2-9][0-9]{8}$";

        if (!telefono.matches(regex)) {
            mostrarError("El número de teléfono no es válido. Debe tener el formato +56XXXXXXXXX.");
            return false;
        }

        // Si pasa todas las validaciones
        return true;
    }

    public String obtenerEstadoConexion() {
        return DatabaseConnection.getConnectionStatus();
    }
}