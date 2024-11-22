package expresslink.view.cliente;

import expresslink.model.Usuario;
import expresslink.controllers.cliente.ClienteController;
import expresslink.view.login.LoginView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClienteDashboard extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Usuario usuario;
    private ClienteController controlador;

    // Paneles principales
    private JPanel panelMisPedidos;
    private SeguimientoPanel panelSeguimiento;
    private JPanel panelHistorial;
    private PerfilPanelV1 panelPerfil;

    // Colores
    private static final Color COLOR_PRIMARY = new Color(33, 150, 243); // Azul
    private static final Color COLOR_BACKGROUND = new Color(240, 242, 245); // Gris claro
    private static final Color COLOR_WHITE = Color.WHITE;

    public ClienteDashboard(Usuario usuario) {
        this.usuario = usuario;
        this.controlador = new ClienteController();
        initComponents();
    }

    private void initComponents() {
        // Configuración básica de la ventana
        setTitle("ExpressLink - Panel de Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BACKGROUND);

        // Panel de navegación (izquierda)
        JPanel navPanel = createNavPanel();
        mainPanel.add(navPanel, BorderLayout.WEST);

        // Panel de contenido con CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_BACKGROUND);

        // Inicializar los paneles principales
        initializePanels();

        // Agregar los paneles al cardPanel
        cardPanel.add(panelMisPedidos, "PEDIDOS");
        cardPanel.add(panelSeguimiento, "SEGUIMIENTO");
        cardPanel.add(panelHistorial, "HISTORIAL");
        cardPanel.add(panelPerfil, "PERFIL");

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Header con información del usuario
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setPreferredSize(new Dimension(200, 0));
        navPanel.setBackground(COLOR_WHITE);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo o título
        JLabel logoLabel = new JLabel("ExpressLink");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navPanel.add(logoLabel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Botones de navegación
        String[] buttonLabels = { "Mis Pedidos", "Seguimiento", "Historial", "Perfil"
        };
        String[] cardNames = { "PEDIDOS", "SEGUIMIENTO", "HISTORIAL", "PERFIL" };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createNavButton(buttonLabels[i], cardNames[i]);
            navPanel.add(button);
            navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Botón de cerrar sesión al final
        navPanel.add(Box.createVerticalGlue());
        JButton logoutButton = new JButton("Cerrar Sesión");
        styleButton(logoutButton);
        logoutButton.addActionListener(e -> handleLogout());
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        navPanel.add(logoutButton);

        return navPanel;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(e -> {
            cardLayout.show(cardPanel, cardName);
            if (cardName.equals("SEGUIMIENTO")) {
                panelSeguimiento.resetBusqueda(); // Limpiar búsqueda anterior si existe
            }
        });
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private void styleButton(JButton button) {
        button.setMaximumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(COLOR_WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Información del usuario
        JLabel userLabel = new JLabel("Bienvenido, " + usuario.getNombre());
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(COLOR_WHITE);
        headerPanel.add(userLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private void initializePanels() {
        // Panel de Mis Pedidos (Temporal hasta implementar MisPedidosPanel)
        panelMisPedidos = new JPanel();
        panelMisPedidos.setBackground(COLOR_BACKGROUND);
        panelMisPedidos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // TODO: Implementar MisPedidosPanel

        // Panel de Seguimiento
        panelSeguimiento = new SeguimientoPanel();

        // Panel de Perfil
        panelPerfil = new PerfilPanelV1(usuario);

        // Panel de Historial (Temporal hasta implementar HistorialPanel)
        panelHistorial = new JPanel();
        panelHistorial.setBackground(COLOR_BACKGROUND);
        panelHistorial.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // TODO: Implementar HistorialPanel

        // Panel de Perfil
        panelPerfil = new PerfilPanelV1(usuario);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            // TODO: Implementar lógica de cierre de sesión en el controlador
            new LoginView().setVisible(true);
        }
    }
}