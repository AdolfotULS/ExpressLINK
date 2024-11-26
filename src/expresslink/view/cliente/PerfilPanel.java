package expresslink.view.cliente;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import expresslink.controllers.cliente.PerfilController;

public class PerfilPanel extends JPanel {
    private PerfilController controlador;

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
}