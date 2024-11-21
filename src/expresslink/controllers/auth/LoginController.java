package expresslink.controllers.auth;

import expresslink.model.Usuario;
import expresslink.view.login.ForgotPasswordView;
import expresslink.view.login.LoginView;
import expresslink.view.login.RegisterView;
import javax.swing.JOptionPane;

public class LoginController {
    private final LoginView vista;
    private final AuthController authController;
    private Usuario usuarioActual;

    public LoginController(LoginView vista) {
        this.vista = vista;
        this.authController = new AuthController();
        this.vista.setControlador(this);
    }

    public void manejarInicioSesion(String email, String password) {
        try {
            // Validaciones básicas
            if (email.isEmpty() || password.isEmpty()) {
                mostrarError("Por favor ingrese email y contraseña");
                return;
            }

            // Intento de login
            usuarioActual = authController.inicioSesion(email, password);
            mostrarInformacion(usuarioActual.toString(), "Base de Datos");
            // mostrarInformacion(usuarioActual.getSucursal().toString(), "Sucursal");

            if (usuarioActual != null) {
                vista.limpiarCampos();
                abrirDashboardSegunRol();
                vista.dispose(); // Cerrar ventana de login
                mostrarInformacion("Usted ha Iniciado session como " + email, "Exitoso");
            } else {
                mostrarError("Credenciales inválidas");
            }

        } catch (Exception e) {
            mostrarError("Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void manejarRegistro() {
        // Abrir ventana de registro
        RegisterView registerView = new RegisterView();
        RegisterController registerController = new RegisterController(registerView);
        registerView.setVisible(true);
        vista.dispose(); // Cerrar ventana de login
    }

    public void manejarRecuperarContrasena() {
        // Abrir ventana de recuperación de contraseña
        ForgotPasswordView forgotPasswordView = new ForgotPasswordView();
        ForgotPasswordController forgotPasswordController = new ForgotPasswordController(forgotPasswordView);
        forgotPasswordView.setVisible(true);
        vista.dispose(); // Cerrar ventana de login
    }

    private void abrirDashboardSegunRol() {
        if (usuarioActual == null)
            return;

        try {
            switch (usuarioActual.getRol()) {
                case CLIENTE:
                    // new ClienteDashboard(usuarioActual).setVisible(true);
                    break;
                case TRANSPORTISTA:
                    // new TransportistaDashboard(usuarioActual).setVisible(true);
                    mostrarInformacion("Dashboard de Transportista en desarrollo",
                            "Administrador");
                    break;
                case SUCURSAL:
                    // new SucursalDashboard(usuarioActual).setVisible(true);
                    mostrarInformacion("Dashboard de Sucursal en desarrollo",
                            "Administrador");
                    break;
                case ADMIN:
                    mostrarInformacion("Dashboard de administrador en desarrollo",
                            "Administrador");
                    break;
                default:
                    mostrarError("Tipo de usuario no soportado");
            }
        } catch (Exception e) {
            mostrarError("Error al abrir el dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        vista.mostrarError(mensaje);
    }

    private void mostrarInformacion(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(vista, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void cerrarSesion() {
        try {
            authController.cerrarSesion();
            usuarioActual = null;
            // Volver a mostrar la vista de login
            vista.limpiarCampos();
            vista.setVisible(true);
        } catch (Exception e) {
            mostrarError("Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}