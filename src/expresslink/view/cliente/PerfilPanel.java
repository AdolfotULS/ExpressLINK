package expresslink.view.cliente;

import java.awt.*;

import javax.swing.*;

import expresslink.controllers.cliente.PerfilController;
import expresslink.model.Usuario;

public class PerfilPanel extends JPanel {
    private static final Color COLOR_PRIMARIO = new Color(33, 150, 243);
    private static final Color COLOR_FONDO = new Color(240, 242, 245);
    private static final Color COLOR_BLANCO = Color.WHITE;

    private Usuario usuario;
    private JTextField nombreField;
    private JTextField emailField;
    private JTextField telefonoField;
    private JPasswordField passwordActualField;
    private JPasswordField passwordNuevaField;
    private JPasswordField confirmPasswordField;
    private PerfilController controlador;

    public PerfilPanel(Usuario usuario, PerfilController controlador) {
        this.usuario = usuario;
        this.controlador = new PerfilController(this, usuario);
    }

    public void inicializarGUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de contenido principal con GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(COLOR_BLANCO);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

}