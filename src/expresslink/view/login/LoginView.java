package expresslink.view.login;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import expresslink.model.enums.*;
import java.awt.*;

public class LoginView extends JFrame {

    public LoginView() {
        // Configuracion de la ventana principal
        setTitle("Inicio de Sesion");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Creacion del panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 255)); // Color de fondo similar
        mainPanel.setLayout(new GridBagLayout()); // Usamos GridBagLayout para centrar el panel interno
        add(mainPanel);

        // Panel para el formulario de inicio de sesion
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setPreferredSize(new Dimension(300, 200));
        loginPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Etiqueta del titulo
        JLabel titleLabel = new JLabel("Sistema de Envios", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(33, 150, 243)); // Color de fondo azul
        titleLabel.setPreferredSize(new Dimension(280, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        // Campo de texto para el usuario
        JTextField userField = new JTextField(15);
        userField.setBorder(BorderFactory.createTitledBorder("Usuario"));

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        loginPanel.add(userField, gbc);

        // Campo de texto para la contraseña
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createTitledBorder("Contraseña"));

        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);

        // Boton de inicio de sesion
        JButton loginButton = new JButton("Iniciar Sesion");
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.WHITE);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        // Enlaces para "Olvide mi contraseña" y "Registrarse"
        JLabel forgotPasswordLabel = new JLabel("Olvide mi contraseña", JLabel.CENTER);
        forgotPasswordLabel.setForeground(Color.BLUE);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel registerLabel = new JLabel("Registrarse", JLabel.CENTER);
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        loginPanel.add(forgotPasswordLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(registerLabel, gbc);

        // Agregar el panel de login al panel principal
        mainPanel.add(loginPanel);

        // Hacer visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        // Ejecutar la interfaz en el hilo de eventos de Swing
        SwingUtilities.invokeLater(LoginView::new);
    }
}