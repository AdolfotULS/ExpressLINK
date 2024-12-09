package expresslink;

import javax.swing.*;
import expresslink.view.login.*;

public class ExpressLINK {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView view = new LoginView();
            view.setVisible(true);
        });
    }
}
