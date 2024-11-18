package expresslink.controllers.cliente;

import java.sql.SQLException;
import java.util.regex.Pattern;
import expresslink.model.Usuario;
import expresslink.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PerfilController {
    // Expresiones regulares para validación
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PHONE_REGEX = "^\\+56[2-9][0-9]{8}$";

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

        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede tener más de 100 caracteres");
        }

        // Validar teléfono
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }

        if (!Pattern.matches(PHONE_REGEX, telefono)) {
            throw new IllegalArgumentException("El formato del teléfono debe ser +56XXXXXXXXX");
        }

        return true;
    }

    private boolean validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (contrasena.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        if (contrasena.length() > 100) {
            throw new IllegalArgumentException("La contraseña no puede tener más de 100 caracteres");
        }

        // Comprobar requisitos mínimos de seguridad
        boolean tieneMayuscula = contrasena.matches(".*[A-Z].*");
        boolean tieneMinuscula = contrasena.matches(".*[a-z].*");
        boolean tieneNumero = contrasena.matches(".*\\d.*");
        boolean tieneEspecial = contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        if (!(tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial)) {
            throw new IllegalArgumentException(
                    "La contraseña debe contener al menos una mayúscula, una minúscula, " +
                            "un número y un carácter especial");
        }

        return true;
    }

    public String obtenerEstadoConexion() {
        return DatabaseConnection.getConnectionStatus();
    }
}