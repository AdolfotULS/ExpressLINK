
package expresslink;

import javax.swing.SwingUtilities;

import expresslink.controllers.auth.LoginController;
import expresslink.model.Usuario;
import expresslink.view.login.LoginView;

public class ExpressLINK {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView view = new LoginView();
            LoginController controller = new LoginController(view);
            view.setVisible(true);
        });
    }
}
