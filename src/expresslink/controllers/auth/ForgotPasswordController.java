package expresslink.controllers.auth;

import expresslink.view.login.ForgotPasswordView;
import expresslink.view.login.LoginView;
import javax.swing.JOptionPane;

public class ForgotPasswordController {
    private final ForgotPasswordView vista;
    private final AuthController authController;

    public ForgotPasswordController(ForgotPasswordView vista) {
        this.vista = vista;
        this.authController = new AuthController();
    }

    public void manejarRecuperarContrasena(String email) {
        try {
            // Validaciones básicas
            if (email.isEmpty()) {
                mostrarError("Por favor ingrese un email");
                return;
            }

            if (!validarEmail(email)) {
                mostrarError("El formato del email no es válido");
                return;
            }

            // Intentar recuperar la contraseña
            String contrasenaEnmascarada = authController.recuperarContrasena(email);
            mostrarResultado(
                    "Su contraseña es: " + contrasenaEnmascarada + "\n" +
                            "Por seguridad, solo se muestra la mitad de la contraseña.\n" +
                            "Si no recuerda su contraseña, contacte con soporte.");
            // Si todo sale bien, volver al login después de un momento
            volverLogin();

        } catch (Exception e) {
            mostrarError("Error al recuperar la contraseña: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void volverLogin() {
        // Cerrar ventana actual y abrir login
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
        vista.dispose();
    }

    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(regex);
    }

    private void mostrarError(String mensaje) {
        vista.mostrarError(mensaje);
    }

    private void mostrarResultado(String mensaje) {
        vista.mostrarExito(mensaje);
    }
}