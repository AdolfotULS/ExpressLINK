package expresslink.view.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import expresslink.controllers.auth.ForgotPasswordController;

public class ForgotPasswordView extends JFrame {
    private JTextField emailField;
    private ForgotPasswordController controlador;

    public ForgotPasswordView() {
        initializeGUI();
    }

    public void setControlador(ForgotPasswordController controlador) {
        this.controlador = controlador;
    }

    private void initializeGUI() {
        setTitle("Recuperar Contraseña");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 255));
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);

        // Panel para el formulario
        JPanel forgotPasswordPanel = new JPanel();
        forgotPasswordPanel.setLayout(new GridBagLayout());
        forgotPasswordPanel.setBackground(Color.WHITE);
        forgotPasswordPanel.setPreferredSize(new Dimension(300, 150));
        forgotPasswordPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Título
        JLabel titleLabel = new JLabel("Recuperar Contraseña", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(33, 150, 243));
        titleLabel.setPreferredSize(new Dimension(280, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        forgotPasswordPanel.add(titleLabel, gbc);

        // Campo email
        emailField = new JTextField(15);
        emailField.setBorder(BorderFactory.createTitledBorder("Correo Electrónico"));

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        forgotPasswordPanel.add(emailField, gbc);

        // Botón recuperar
        JButton recoverButton = new JButton("Recuperar Contraseña");
        recoverButton.setBackground(new Color(33, 150, 243));
        recoverButton.setForeground(Color.WHITE);
        recoverButton.addActionListener(e -> recuperarContrasena());

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        forgotPasswordPanel.add(recoverButton, gbc);

        // Enlace para volver
        JLabel loginLabel = new JLabel("Volver a Iniciar Sesión", JLabel.CENTER);
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volverLogin();
            }
        });

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        forgotPasswordPanel.add(loginLabel, gbc);

        // Agregar panel al principal
        mainPanel.add(forgotPasswordPanel);

        // Enter key listener
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    recuperarContrasena();
                }
            }
        });
    }

    private void recuperarContrasena() {
        if (controlador != null) {
            controlador.manejarRecuperarContrasena(emailField.getText());
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
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ForgotPasswordView vista = new ForgotPasswordView();
            ForgotPasswordController controlador = new ForgotPasswordController(vista);
            vista.setVisible(true);
        });
    }
}