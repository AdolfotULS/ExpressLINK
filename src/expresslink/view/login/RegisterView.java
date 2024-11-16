package expresslink.view.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import expresslink.controllers.auth.RegisterController;

public class RegisterView extends JFrame {
    private JTextField nombreField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField telefonoField;
    private RegisterController controlador;

    public RegisterView() {
        inicializarGUI();
    }

    public void setControlador(RegisterController controlador) {
        this.controlador = controlador;
    }

    private void inicializarGUI() {
        setTitle("Registro Usuario");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 255));
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);

        // Panel de registro
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setPreferredSize(new Dimension(300, 350));
        registerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Título
        JLabel titleLabel = new JLabel("Registro de Usuario", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(33, 150, 243));
        titleLabel.setPreferredSize(new Dimension(280, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerPanel.add(titleLabel, gbc);

        // Campo nombre
        nombreField = new JTextField(15);
        nombreField.setBorder(BorderFactory.createTitledBorder("Nombre"));
        gbc.gridy = 1;
        registerPanel.add(nombreField, gbc);

        // Campo email
        emailField = new JTextField(15);
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        gbc.gridy = 2;
        registerPanel.add(emailField, gbc);

        // Campo teléfono
        telefonoField = new JTextField(15);
        telefonoField.setBorder(BorderFactory.createTitledBorder("Teléfono"));
        gbc.gridy = 3;
        registerPanel.add(telefonoField, gbc);

        // Campo contraseña
        passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        gbc.gridy = 4;
        registerPanel.add(passwordField, gbc);

        // Campo confirmar contraseña
        confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setBorder(BorderFactory.createTitledBorder("Confirmar Contraseña"));
        gbc.gridy = 5;
        registerPanel.add(confirmPasswordField, gbc);

        // Botón registro
        JButton registerButton = new JButton("Registrar");
        registerButton.setBackground(new Color(33, 150, 243));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> registrar());

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        registerPanel.add(registerButton, gbc);

        // Enlace para volver a login
        JLabel loginLabel = new JLabel("Volver a Iniciar Sesión", JLabel.CENTER);
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volverLogin();
            }
        });

        gbc.gridy = 7;
        gbc.gridwidth = 2;
        registerPanel.add(loginLabel, gbc);

        // Agregar panel de registro al panel principal
        mainPanel.add(registerPanel);
    }

    private void registrar() {
        if (controlador != null) {
            controlador.manejarRegistro(
                    nombreField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    new String(confirmPasswordField.getPassword()),
                    telefonoField.getText());
        }
    }

    private void volverLogin() {
        if (controlador != null) {
            controlador.volverLogin();
        }
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiarCampos() {
        nombreField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        telefonoField.setText("");
    }
}