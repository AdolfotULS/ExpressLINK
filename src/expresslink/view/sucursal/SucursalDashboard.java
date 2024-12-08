package expresslink.view.sucursal;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import javax.swing.border.*;

import expresslink.controllers.sucursal.SucursalController;
import expresslink.model.Sucursal;
import expresslink.model.Usuario;
import expresslink.view.login.LoginView;
import expresslink.view.sucursal.*;

public class SucursalDashboard extends JFrame {
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color BACKGROUND_COLOR = Color.WHITE;

    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel currentPanel;

    private LoginView loginView;
    private Usuario usuario;
    private Sucursal sucursal;
    private SucursalController controlador;

    public SucursalDashboard(Usuario usuario, LoginView loginView) {
        this.usuario = usuario;
        this.loginView = loginView;
        this.controlador = new SucursalController(usuario, this);

        try {
            this.sucursal = controlador.obtenerDatosSucursal();
            if (this.sucursal == null) {
                throw new RuntimeException("No se encontraron datos de Sucursal");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos del transportista: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            dispose();
            return;
        }

        inicializarGUI();
    }

    private void inicializarGUI() {
        setTitle("Express Link - " + sucursal.getNombre() + " [" + sucursal.getCiudad() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Panel de menú
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        // CardLayout para contenido dinámico
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Crear los paneles de contenido
        createContentPanels();

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(200, 600));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Logo y título
        JLabel titleLabel = new JLabel("Express Link");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(30)); // Más espacio después del título

        // Elementos del menú actualizados
        String[] menuItems = {
                "Informacion General", // Cambiado de "Dashboard"
                "Nuevo Paquete",
                "Pedidos Pendientes",
                "Pedidos en Transito",
                "Entregas del Dia",
                "Reportes"
        };

        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setMaximumSize(new Dimension(180, 40));
            button.setPreferredSize(new Dimension(180, 40));
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(33, 33, 33));
            button.setFocusPainted(false);
            button.setBorderPainted(true);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Efecto hover
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(245, 245, 245));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(Color.WHITE);
                }
            });

            button.addActionListener(e -> {
                String cardName = item.equals("Informacion General") ? "Dashboard" : item;
                cardLayout.show(contentPanel, cardName);

                // Actualizar título de la ventana con nombre de sección actual
                setTitle("Express Link - " + sucursal.getNombre() + " [" + sucursal.getCiudad() + "] - " + item);
            });

            menuPanel.add(button);
            menuPanel.add(Box.createVerticalStrut(10)); // Espacio entre botones
        }

        menuPanel.add(Box.createVerticalGlue()); // Empuja todo hacia arriba
        return menuPanel;
    }

    private void createContentPanels() {
        // Crear los paneles de contenido
        contentPanel.add(new InformacionGeneralView(sucursal), "Dashboard");
        contentPanel.add(new NuevoPedidoView(usuario, sucursal), "Nuevo Paquete");
        contentPanel.add(new GestionPaquetesView(usuario, sucursal), "Pedidos Pendientes");
        contentPanel.add(new PedidosTransitoView(sucursal), "Pedidos en Transito");
        contentPanel.add(new EntregasDiaView(sucursal), "Entregas del Dia");
        contentPanel.add(new ReportesView(sucursal), "Reportes");
    }

    private JPanel createPanelWithBackButton(String panelName) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Barra superior
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(panelName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Si es "Pedidos Pendientes", agregamos elementos adicionales
        if (panelName.equals("Pedidos Pendientes")) {
            String[] columnNames = { "N° Pedido", "Destinatario", "Estado", "Acción" };
            Object[][] data = {
                    { "#2024-001", "Juan Pérez", "Pendiente", "Ver detalles" },
                    { "#2024-002", "María García", "En Ruta", "Ver detalles" }
            };

            JTable table = new JTable(data, columnNames);
            table.setRowHeight(35);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton verMasBtn = new JButton("VER MAS INFORMACIÓN");
            JButton editarBtn = new JButton("EDITAR");

            // Estilo para los botones
            for (JButton btn : new JButton[] { verMasBtn, editarBtn }) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(PRIMARY_COLOR);
                btn.setFocusPainted(false);
                btn.setBorder(new EmptyBorder(8, 15, 8, 15));
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                buttonPanel.add(btn);
            }
            headerPanel.add(buttonPanel, BorderLayout.EAST);
        }

        panel.add(headerPanel, BorderLayout.NORTH);
        return panel;
    }
}
