package expresslink.view.login;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import expresslink.model.enums.*;
import java.awt.*;
import java.awt.event.*;
import expresslink.controllers.auth.LoginController;

public class LoginView extends JFrame {
    private JTextField userField;
    private JPasswordField passwordField;
    public LoginController controlador;

    public LoginView() {
        inicializarGUI();
        this.controlador = new LoginController(this);
    }

    private void inicializarGUI() {
        // Configuración de la ventana principal
        setTitle("Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Creación del panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(240, 240, 255));
        panelPrincipal.setLayout(new GridBagLayout());
        add(panelPrincipal);

        // Panel para el formulario de inicio de sesión
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new GridBagLayout());
        panelLogin.setBackground(Color.WHITE);
        panelLogin.setPreferredSize(new Dimension(300, 200));
        panelLogin.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Etiqueta del título
        JLabel etiquetaTitulo = new JLabel("Sistema de Envíos", JLabel.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaTitulo.setForeground(Color.WHITE);
        etiquetaTitulo.setOpaque(true);
        etiquetaTitulo.setBackground(new Color(33, 150, 243));
        etiquetaTitulo.setPreferredSize(new Dimension(280, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelLogin.add(etiquetaTitulo, gbc);

        // Campo de texto para el usuario
        userField = new JTextField(15);
        userField.setBorder(BorderFactory.createTitledBorder("Email"));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelLogin.add(userField, gbc);

        // Campo de texto para la contraseña
        passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        gbc.gridy = 2;
        panelLogin.add(passwordField, gbc);

        // Botón de inicio de sesión
        JButton botonLogin = new JButton("Iniciar Sesión");
        botonLogin.setBackground(new Color(33, 150, 243));
        botonLogin.setForeground(Color.WHITE);
        botonLogin.addActionListener(e -> iniciarSesion());

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelLogin.add(botonLogin, gbc);

        // Enlaces para "Olvidé mi contraseña" y "Registrarse"
        JLabel etiquetaOlvidePass = new JLabel("Olvidé mi contraseña", JLabel.CENTER);
        etiquetaOlvidePass.setForeground(Color.BLUE);
        etiquetaOlvidePass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        etiquetaOlvidePass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                recuperarContrasena();
            }
        });

        JLabel etiquetaRegistro = new JLabel("Registrarse", JLabel.CENTER);
        etiquetaRegistro.setForeground(Color.BLUE);
        etiquetaRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        etiquetaRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                registrarUsuario();
            }
        });

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panelLogin.add(etiquetaOlvidePass, gbc);

        gbc.gridx = 1;
        panelLogin.add(etiquetaRegistro, gbc);

        // Agregar el panel de login al panel principal
        panelPrincipal.add(panelLogin);

        // Agregar KeyListener para el campo de contraseña
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    iniciarSesion();
                }
            }
        });
    }

    private void iniciarSesion() {
        if (controlador != null) {
            controlador.manejarInicioSesion(
                    userField.getText(),
                    new String(passwordField.getPassword()));
        }
    }

    private void recuperarContrasena() {
        if (controlador != null) {
            controlador.manejarRecuperarContrasena();
        }
    }

    private void registrarUsuario() {
        if (controlador != null) {
            controlador.manejarRegistro();
        }
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiarCampos() {
        userField.setText("");
        passwordField.setText("");
    }
}