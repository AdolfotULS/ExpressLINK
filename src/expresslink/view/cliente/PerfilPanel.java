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

    public PerfilPanel(Usuario usuario) {
        this.usuario = usuario;
        this.controlador = new PerfilController(usuario, this);
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        inicializarGUI();
    }

    private void inicializarGUI() {
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

        // Titulo
        JLabel titleLabel = new JLabel("Informacion Personal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(titleLabel, gbc);

        // Campos de informacion personal
        nombreField = crearTextField(usuario.getNombre());
        emailField = crearTextField(usuario.getEmail());
        emailField.setEditable(false); // El email no se puede editar
        telefonoField = crearTextField(usuario.getTelefono());

        // Agregar campos con sus etiquetas
        gbc.gridwidth = 1;
        agregarCampoForm(contentPanel, "Nombre:", nombreField, gbc, 1);
        agregarCampoForm(contentPanel, "Email:", emailField, gbc, 2);
        agregarCampoForm(contentPanel, "Telefono:", telefonoField, gbc, 3);

        // Separador
        JSeparator separator = new JSeparator();
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(separator, gbc);

        // Titulo cambio de contraseña
        JLabel passwordTitle = new JLabel("Cambiar Contraseña");
        passwordTitle.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 10, 10, 10);
        contentPanel.add(passwordTitle, gbc);

        // Campos de contraseña
        passwordActualField = crearPasswordField();
        passwordNuevaField = crearPasswordField();
        confirmPasswordField = crearPasswordField();

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        agregarCampoForm(contentPanel, "Contraseña Actual:", passwordActualField, gbc, 6);
        agregarCampoForm(contentPanel, "Nueva Contraseña:", passwordNuevaField, gbc, 7);
        agregarCampoForm(contentPanel, "Confirmar Contraseña:", confirmPasswordField, gbc, 8);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(COLOR_BLANCO);

        JButton guardarButton = new JButton("Guardar Cambios");
        botonEstilo(guardarButton);
        guardarButton.addActionListener(e -> guardarCambios());

        buttonPanel.add(guardarButton);

        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(buttonPanel, gbc);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField crearTextField(String text) {
        JTextField field = new JTextField(text);
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private JPasswordField crearPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private void agregarCampoForm(JPanel panel, String labelText, JComponent field,
            GridBagConstraints gbc, int row) {
        gbc.gridy = row;

        // Etiqueta
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, gbc);

        // Campo
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void botonEstilo(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(COLOR_PRIMARIO);
        button.setForeground(COLOR_BLANCO);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void guardarCambios() {
        try {
            boolean cambiosRealizados = false;

            // Actualizar información básica si hubo cambios
            if (!nombreField.getText().equals(usuario.getNombre()) ||
                    !telefonoField.getText().equals(usuario.getTelefono())) {

                boolean infoActualizada = controlador.actualizarInformacionUsuario(
                        nombreField.getText().trim(),
                        telefonoField.getText().trim());

                if (infoActualizada) {
                    cambiosRealizados = true;
                }
            }

            // Verificar si hay intento de cambio de contraseña
            String passwordActual = new String(passwordActualField.getPassword());
            String passwordNueva = new String(passwordNuevaField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!passwordActual.isEmpty() || !passwordNueva.isEmpty() || !confirmPassword.isEmpty()) {
                boolean contrasenaActualizada = controlador.actualizarContrasena(
                        passwordActual,
                        passwordNueva,
                        confirmPassword);

                if (contrasenaActualizada) {
                    cambiosRealizados = true;
                    limpiarCamposPassword();
                }
            }

            if (cambiosRealizados) {
                mostrarExito("Los cambios han sido guardados exitosamente");
            }

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al guardar los cambios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarCamposPassword() {
        passwordActualField.setText("");
        passwordNuevaField.setText("");
        confirmPasswordField.setText("");
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

}