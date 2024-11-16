package expresslink.controllers.auth;

import expresslink.model.enums.TipoUsuario;
import expresslink.view.login.RegisterView;
import expresslink.view.login.LoginView;
import javax.swing.JOptionPane;

public class RegisterController {
    private final RegisterView vista;
    private final AuthController authController;

    public RegisterController(RegisterView vista) {
        this.vista = vista;
        this.authController = new AuthController();
        this.vista.setControlador(this);
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

            if (!validarEmail(email)) {
                mostrarError("El formato del email no es válido");
                return;
            }

            // Por defecto registramos como CLIENTE
            boolean registroExitoso = authController.registrarUsuario(
                    nombre,
                    email,
                    password,
                    telefono,
                    TipoUsuario.CLIENTE);

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
        LoginController loginController = new LoginController(loginView);
        loginView.setVisible(true);
    }

    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    private void mostrarError(String mensaje) {
        vista.mostrarError(mensaje);
    }

    private void mostrarExito(String mensaje) {
        vista.mostrarExito(mensaje);
    }
}