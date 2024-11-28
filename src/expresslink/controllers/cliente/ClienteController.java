package expresslink.controllers.cliente;

import expresslink.controllers.auth.LoginController;
import expresslink.model.Usuario;
import expresslink.view.cliente.ClienteDashboard;
import expresslink.view.login.LoginView;

public class ClienteController {
    private ClienteDashboard vista;
    private Usuario usuario;
    private LoginController cLoginController;

    public ClienteController(Usuario usuario, LoginView loginView, LoginController loginController) {
        this.usuario = usuario;
        this.vista = vista;
        this.vista.setControlador(this);
    }

    public void manejarCerradoSesion() {
        // Limpiar usuario

        cLoginController.cerrarSesion();
    }

}
