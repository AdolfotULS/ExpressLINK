package expresslink.controllers.auth;

import expresslink.model.*;
import expresslink.view.cliente.*;
import expresslink.view.empresa.EmpresaDashboard;
import expresslink.view.login.*;
import expresslink.view.transportista.*;
import expresslink.view.sucursal.*;

import javax.swing.*;

public class LoginController {
    private final LoginView vista;
    private final AuthController authController;
    private Usuario usuarioActual;

    public LoginController(LoginView vista) {
        this.vista = vista;
        this.authController = new AuthController();
    }

    public Usuario obtenerUsuario() {
        if (usuarioActual != null) {
            return usuarioActual;
        }
        return null;
    }

    public boolean manejarInicioSesion(String email, String password) {
        try {
            // Validaciones básicas
            if (email.isEmpty() || password.isEmpty()) {
                mostrarError("Por favor ingrese email y contraseña");
                return false;
            }

            // Intento de login
            usuarioActual = authController.inicioSesion(email, password);
            // mostrarInformacion(usuarioActual.toString(), "Base de Datos");
            // mostrarInformacion(usuarioActual.getSucursal().toString(), "Sucursal");

            if (usuarioActual != null) {
                vista.limpiarCampos();
                abrirDashboardSegunRol();

                vista.setVisible(false); // TEST
                // vista.dispose(); // Cerrar ventana de login
                mostrarInformacion("Usted ha Iniciado session como " + usuarioActual.getNombre(), "Exitoso");
                return true;
            } else {
                mostrarError("Credenciales inválidas");
            }

        } catch (Exception e) {
            mostrarError("Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void manejarRegistro() {
        // Abrir ventana de registro
        RegisterView registerView = new RegisterView();
        registerView.setVisible(true);
        vista.dispose(); // Cerrar ventana de login
    }

    public void manejarRecuperarContrasena() {
        // Abrir ventana de recuperación de contraseña
        ForgotPasswordView forgotPasswordView = new ForgotPasswordView();
        forgotPasswordView.setVisible(true);
        vista.dispose(); // Cerrar ventana de login
    }

    private void abrirDashboardSegunRol() {
        if (usuarioActual == null)
            return;

        try {
            switch (usuarioActual.getRol()) {
                case CLIENTE:
                    new ClienteDashboard(usuarioActual, vista).setVisible(true);
                    break;
                case TRANSPORTISTA:
                    new TransportistaDashboard(usuarioActual, vista).setVisible(true);
                    break;
                case SUCURSAL:
                    new SucursalDashboard(usuarioActual, vista).setVisible(true);
                    break;
                case EMPRESA:
                    new EmpresaDashboard(usuarioActual, vista).setVisible(true);
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