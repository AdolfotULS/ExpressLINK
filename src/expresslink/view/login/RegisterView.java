package expresslink.view.login;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {

    public RegisterView() {
        // Configuracion de la ventana principal
        setTitle("Registro Usuario");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Creacion del panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 255)); // Fondo similar
        mainPanel.setLayout(new GridBagLayout()); // Para centrar el panel interno
        add(mainPanel);

        // Panel para el formulario de registro
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setPreferredSize(new Dimension(300, 300));
        registerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Etiqueta del titulo
        JLabel titleLabel = new JLabel("Registro de Usuario", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(33, 150, 243)); // Fondo azul
        titleLabel.setPreferredSize(new Dimension(280, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerPanel.add(titleLabel, gbc);

        // Campo de texto para el usuario
        JTextField userField = new JTextField(15);
        userField.setBorder(BorderFactory.createTitledBorder("Usuario"));
        
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        registerPanel.add(userField, gbc);

        // Campo de texto para el correo electronico
        JTextField emailField = new JTextField(15);
        emailField.setBorder(BorderFactory.createTitledBorder("Correo Electronico"));
        
        gbc.gridy = 2;
        registerPanel.add(emailField, gbc);

        // Campo de texto para la contraseña
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        
        gbc.gridy = 3;
        registerPanel.add(passwordField, gbc);

        // Campo de texto para confirmar la contraseña
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setBorder(BorderFactory.createTitledBorder("Confirmar Contraseña"));
        
        gbc.gridy = 4;
        registerPanel.add(confirmPasswordField, gbc);

        // Boton de registro
        JButton registerButton = new JButton("Registrar");
        registerButton.setBackground(new Color(33, 150, 243));
        registerButton.setForeground(Color.WHITE);
        
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        registerPanel.add(registerButton, gbc);

        // Enlace para "Iniciar Sesion"
        JLabel loginLabel = new JLabel("Iniciar Sesion", JLabel.CENTER);
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        registerPanel.add(loginLabel, gbc);

        // Agregar el panel de registro al panel principal
        mainPanel.add(registerPanel);

        // Hacer visible la ventana
        setVisible(true);
    }

    void Boton() {
        // ESTE ES EL BOTON DE REGISTRO
    }           

    public static void main(String[] args) {
        // Ejecutar la interfaz en el hilo de eventos de Swing
        SwingUtilities.invokeLater(RegisterView::new);
    }
}
