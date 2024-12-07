
package expresslink;

import javax.swing.*;
import expresslink.view.login.*;

/*
sucursal@example.com
password123
*/

public class ExpressLINK {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView view = new LoginView();
            view.setVisible(true);
        });
    }
}
