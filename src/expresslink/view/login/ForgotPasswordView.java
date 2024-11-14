package expresslink.view.login;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordView extends JFrame {

    public ForgotPasswordView() {
        // Configuracion de la ventana principal
        setTitle("Recuperar Contraseña");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Creacion del panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 255)); // Fondo similar
        mainPanel.setLayout(new GridBagLayout()); // Centrar el panel interno
        add(mainPanel);

        // Panel para el formulario de recuperacion de contraseña
        JPanel forgotPasswordPanel = new JPanel();
        forgotPasswordPanel.setLayout(new GridBagLayout());
        forgotPasswordPanel.setBackground(Color.WHITE);
        forgotPasswordPanel.setPreferredSize(new Dimension(300, 150));
        forgotPasswordPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Etiqueta del titulo
        JLabel titleLabel = new JLabel("Recuperar Contraseña", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(33, 150, 243)); // Fondo azul
        titleLabel.setPreferredSize(new Dimension(280, 30));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        forgotPasswordPanel.add(titleLabel, gbc);

        // Campo de texto para el correo electronico
        JTextField emailField = new JTextField(15);
        emailField.setBorder(BorderFactory.createTitledBorder("Correo Electronico"));
        
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        forgotPasswordPanel.add(emailField, gbc);

        // Boton de recuperacion de contraseña
        JButton recoverButton = new JButton("Recuperar Contraseña");
        recoverButton.setBackground(new Color(33, 150, 243));
        recoverButton.setForeground(Color.WHITE);
        
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        forgotPasswordPanel.add(recoverButton, gbc);

        // Enlace para "Iniciar Sesion"
        JLabel loginLabel = new JLabel("Iniciar Sesion", JLabel.CENTER);
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        forgotPasswordPanel.add(loginLabel, gbc);

        // Agregar el panel de recuperacion al panel principal
        mainPanel.add(forgotPasswordPanel);

        // Hacer visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        // Ejecutar la interfaz en el hilo de eventos de Swing
        SwingUtilities.invokeLater(ForgotPasswordView::new);
    }
}
