package expresslink.view.cliente;

import expresslink.controllers.cliente.PerfilController;
import expresslink.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PerfilPanel extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(33, 150, 243);
    private static final Color COLOR_BACKGROUND = new Color(240, 242, 245);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_ERROR = new Color(244, 67, 54);

    private Usuario usuario;
    private JTextField nombreField;
    private JTextField emailField;
    private JTextField telefonoField;
    private JPasswordField passwordActualField;
    private JPasswordField passwordNuevaField;
    private JPasswordField confirmPasswordField;

    public PerfilPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout());
        setBackground(COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inicializarGUI();
    }

    private void inicializarGUI() {
        // Panel de contenido principal con GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(COLOR_WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Título
        JLabel titleLabel = new JLabel("Información Personal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        contentPanel.add(titleLabel, gbc);

        // Campos de información personal
        nombreField = createTextField(usuario.getNombre());
        emailField = createTextField(usuario.getCorreo());
        emailField.setEditable(false); // El email no se puede editar
        telefonoField = createTextField(usuario.getTelefono());

        // Agregar campos con sus etiquetas
        gbc.gridwidth = 1;
        addFormField(contentPanel, "Nombre:", nombreField, gbc, 1);
        addFormField(contentPanel, "Email:", emailField, gbc, 2);
        addFormField(contentPanel, "Teléfono:", telefonoField, gbc, 3);

        // Separador
        JSeparator separator = new JSeparator();
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(separator, gbc);

        // Título cambio de contraseña
        JLabel passwordTitle = new JLabel("Cambiar Contraseña");
        passwordTitle.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 10, 10, 10);
        contentPanel.add(passwordTitle, gbc);

        // Campos de contraseña
        passwordActualField = createPasswordField();
        passwordNuevaField = createPasswordField();
        confirmPasswordField = createPasswordField();

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        addFormField(contentPanel, "Contraseña Actual:", passwordActualField, gbc, 6);
        addFormField(contentPanel, "Nueva Contraseña:", passwordNuevaField, gbc, 7);
        addFormField(contentPanel, "Confirmar Contraseña:", confirmPasswordField, gbc, 8);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(COLOR_WHITE);

        JButton guardarButton = new JButton("Guardar Cambios");
        styleButton(guardarButton);
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

    private JTextField createTextField(String text) {
        JTextField field = new JTextField(text);
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private void addFormField(JPanel panel, String labelText, JComponent field,
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

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(COLOR_WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void guardarCambios() {
        try {
            // Crear una instancia del controlador
            PerfilController controlador = new PerfilController();

            // Actualizar información básica
            boolean infoActualizada = controlador.actualizarInformacionUsuario(
                    usuario,
                    nombreField.getText().trim(),
                    telefonoField.getText().trim());

            // Verificar si se está intentando cambiar la contraseña
            String passwordActual = new String(passwordActualField.getPassword());
            String passwordNueva = new String(passwordNuevaField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            boolean contrasenaActualizada = true;
            if (!passwordActual.isEmpty() || !passwordNueva.isEmpty() || !confirmPassword.isEmpty()) {
                contrasenaActualizada = controlador.actualizarContrasena(
                        usuario,
                        passwordActual,
                        passwordNueva,
                        confirmPassword);
            }

            if (infoActualizada && contrasenaActualizada) {
                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(this,
                        "Los cambios han sido guardados exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos de contraseña
                passwordActualField.setText("");
                passwordNuevaField.setText("");
                confirmPasswordField.setText("");
            } else {
                mostrarError("No se pudieron guardar algunos cambios");
            }

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al guardar los cambios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}