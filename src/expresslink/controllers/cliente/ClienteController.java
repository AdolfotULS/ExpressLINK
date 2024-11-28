package expresslink.controllers.cliente;

import expresslink.controllers.auth.LoginController;
import expresslink.model.Usuario;
import expresslink.view.cliente.ClienteDashboard;
import expresslink.view.login.LoginView;

public class ClienteController {
    private ClienteDashboard vista;
    private Usuario usuario;
    private LoginView loginView;

    public ClienteController(Usuario usuario, LoginView loginView, ClienteDashboard vista) {
        this.usuario = usuario;
        this.vista = vista;
        this.loginView = loginView;
    }

    public void manejarCerradoSesion() {
        // Limpiar usuario
        loginView.controlador.cerrarSesion(); // Cerramos Session
        vista.dispose(); // Cerrar
    }

}
