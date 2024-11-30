package expresslink.view.sucursal;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class SucursalDashboard extends JFrame {
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color BACKGROUND_COLOR = Color.WHITE;

    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public SucursalDashboard() {
        setTitle("Express Link - Sucursal");
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
        "Informacion General",  // Cambiado de "Dashboard"
        "Nuevo Pedido", 
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
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
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

        // Necesitamos mapear "Informacion General" de vuelta a "Dashboard" para el CardLayout
        button.addActionListener(e -> {
            String cardName = item.equals("Informacion General") ? "Dashboard" : item;
            cardLayout.show(contentPanel, cardName);
        });

        menuPanel.add(button);
        menuPanel.add(Box.createVerticalStrut(10)); // Espacio entre botones
    }

    menuPanel.add(Box.createVerticalGlue()); // Empuja todo hacia arriba
    return menuPanel;
}

    private void createContentPanels() {
        contentPanel.add(createDashboardPanel(), "Dashboard");
        contentPanel.add(createPanelWithBackButton("Nuevo Pedido"), "Nuevo Pedido");
        contentPanel.add(createPanelWithBackButton("Pedidos Pendientes"), "Pedidos Pendientes");
        contentPanel.add(createPanelWithBackButton("Pedidos en Transito"), "Pedidos en Transito");
        contentPanel.add(createPanelWithBackButton("Entregas del Dia"), "Entregas del Dia");
        contentPanel.add(createPanelWithBackButton("Reportes"), "Reportes");
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
            String[] columnNames = {"N° Pedido", "Destinatario", "Estado", "Acción"};
            Object[][] data = {
                {"#2024-001", "Juan Pérez", "Pendiente", "Ver detalles"},
                {"#2024-002", "María García", "En Ruta", "Ver detalles"}
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
            for (JButton btn : new JButton[]{verMasBtn, editarBtn}) {
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

    private JPanel createDashboardPanel() {
        // Panel principal con márgenes
        JPanel dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setBackground(BACKGROUND_COLOR);
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
        // Barra superior azul
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
        JLabel titleLabel = new JLabel("Sucursal Central - La Serena");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        dashboardPanel.add(headerPanel, BorderLayout.NORTH);
    
        // Panel para las tarjetas de estado
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(BACKGROUND_COLOR);
        
        // Crear las tres tarjetas de estado
        cardsPanel.add(createStatusCard("Pedidos Pendientes"));
        cardsPanel.add(createStatusCard("En Transito"));
        cardsPanel.add(createStatusCard("Entregados Hoy"));
        dashboardPanel.add(cardsPanel, BorderLayout.CENTER);
    
        // Panel para la tabla de pedidos recientes
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
    
        // Título de la sección
        JLabel tableTitle = new JLabel("Pedidos Recientes");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tablePanel.add(tableTitle, BorderLayout.NORTH);
    
        // Tabla de pedidos recientes
        String[] columnNames = {"N° Pedido", "Destinatario", "Estado", "Accion"};
        Object[][] data = {
            {"#2024-001", "Juan Perez", "Pendiente", "ver detalles"},
            {"#2024-002", "Maria Garcia", "En Ruta", "ver detalles"}
        };
    
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        dashboardPanel.add(tablePanel, BorderLayout.SOUTH);
    
        return dashboardPanel;
    }
    
    // Método auxiliar para crear las tarjetas de estado
    private JPanel createStatusCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
    
        // Contenedor para centrar el contenido
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
    
        // Texto del título
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    
        // Añadir los componentes al contenedor
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
    
        // Añadir el contenedor a la tarjeta
        card.add(contentPanel);
    
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SucursalDashboard dashboard = new SucursalDashboard();
            dashboard.setVisible(true);
        });
    }
}
