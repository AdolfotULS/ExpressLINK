package expresslink.controllers.auth;

import expresslink.model.Usuario;
import expresslink.model.enums.RolUsuario;
import expresslink.view.login.*;

public class RegisterController {
    private final RegisterView vista;
    private final AuthController authController;

    public RegisterController(RegisterView vista) {
        this.vista = vista;
        this.authController = new AuthController();
    }

    public void manejarRegistro(String nombre, String email, String password, String confirmPassword, String telefono) {
        try {
            // Validaciones básicas
            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }

            if (!password.equals(confirmPassword)) {
                mostrarError("Las contraseñas no coinciden");
                return;
            }

            if (!validarContrasena(password)) {
                return;
            }

            if (!validarEmail(email)) {
                mostrarError("El formato del email no es válido");
                return;
            }

            if (!validarTelefonoChileno(telefono)) {
                return;
            }

            // Usuario newUsuario = new Usuario(nombre, email, password, telefono,
            // RolUsuario.CLIENTE);
            // Por defecto registramos como CLIENTE
            Usuario nuevoCliente = new Usuario(nombre, email, confirmPassword, telefono, RolUsuario.CLIENTE);
            boolean registroExitoso = authController.registrarUsuario(nuevoCliente);

            if (registroExitoso) {
                mostrarExito("Usuario registrado exitosamente");
                // Abrir ventana de login
                abrirLogin();
                vista.dispose();
            }

        } catch (Exception e) {
            mostrarError("Error durante el registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void volverLogin() {
        abrirLogin();
        vista.dispose();
    }

    private void abrirLogin() {
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
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

    private void mostrarError(String mensaje) {
        vista.mostrarError(mensaje);
    }

    private void mostrarExito(String mensaje) {
        vista.mostrarExito(mensaje);
    }
}