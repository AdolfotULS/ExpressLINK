package expresslink.view.transportista;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class TransportistaDashboard extends JFrame {
    // Colores corporativos para mantener la consistencia visual
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);    // Azul principal
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);     // Verde para botones
    private final Color HOVER_COLOR = new Color(56, 142, 60);       // Verde oscuro al hacer hover

    private JPanel mainPanel;
    private String transportistaName = "Carlos Rodriguez"; // Esto viene de la base de datos

    public TransportistaDashboard() {
        // Configuracion basica de la ventana
        setTitle("Express Link - Centro de datos - Transportista");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Configuracion del panel principal
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Crear y agregar los componentes principales
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de contenido principal con dos columnas
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Agregar paneles izquierdo y derecho
        contentPanel.add(createPendingDeliveriesPanel());
        contentPanel.add(createSummaryPanel());

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // Crea el encabezado con titulo del panel
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Panel de Transportista");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    // Panel que muestra las entregas pendientes
    private JPanel createPendingDeliveriesPanel() {
        JPanel panel = createBasePanel("Entregas Pendientes");

        // Panel que contendra las tarjetas de pedido
        JPanel deliveriesPanel = new JPanel();
        deliveriesPanel.setLayout(new BoxLayout(deliveriesPanel, BoxLayout.Y_AXIS));
        deliveriesPanel.setBackground(BACKGROUND_COLOR);

        // Agregar tarjetas de pedido
        deliveriesPanel.add(new TarjetaPedido("#2024-001", "Juan Perez", "Av. Principal 123"));
        deliveriesPanel.add(Box.createVerticalStrut(10));
        deliveriesPanel.add(new TarjetaPedido("#2024-002", "Maria Garcia", "Calle Norte 456"));
        deliveriesPanel.add(Box.createVerticalStrut(10));
        deliveriesPanel.add(new TarjetaPedido("#2024-003", "Carlos Lopez", "Calle Sur 789"));
        deliveriesPanel.add(Box.createVerticalStrut(10));
        deliveriesPanel.add(new TarjetaPedido("#2024-004", "Ana Martinez", "Av. Central 321"));

        // Configurar scroll para las tarjetas
        JScrollPane scrollPane = new JScrollPane(deliveriesPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Panel que muestra el resumen del dia y el historial de entregas
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BACKGROUND_COLOR);

        // Resumen diario
        JPanel summaryPanel = createBasePanel("Resumen del Dia");
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        statsPanel.setBackground(new Color(240, 247, 255));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.add(new JLabel("Entregas Completadas: 8/15"));
        statsPanel.add(new JLabel("Tiempo promedio: 15 min"));
        summaryPanel.add(statsPanel, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.NORTH);

        // Historial de entregas
        JPanel historyPanel = createBasePanel("Historial de Entregas");
        JPanel entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        entriesPanel.setBackground(BACKGROUND_COLOR);
        entriesPanel.add(createHistoryEntry("#2024-000", "Entregado", "10:30", true));
        entriesPanel.add(Box.createVerticalStrut(5));
        entriesPanel.add(createHistoryEntry("#2024-001", "Entregado", "11:15", true));
        entriesPanel.add(Box.createVerticalStrut(5));
        entriesPanel.add(createHistoryEntry("#2024-002", "No entregado", "12:00", false));
        historyPanel.add(entriesPanel, BorderLayout.CENTER);
        panel.add(historyPanel, BorderLayout.CENTER);

        return panel;
    }

    // Metodo para crear un panel con titulo
    private JPanel createBasePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

    // Entrada para el historial
    private JPanel createHistoryEntry(String id, String status, String time, boolean success) {
        JPanel entry = new JPanel(new BorderLayout());
        entry.setBackground(success ? new Color(232, 245, 233) : new Color(255, 235, 238));
        entry.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel label = new JLabel(id + " - " + status + " (" + time + ")");
        label.setForeground(success ? new Color(46, 125, 50) : new Color(198, 40, 40));
        entry.add(label);

        return entry;
    }

    // Metodo main para ejecutar la aplicacion
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransportistaDashboard dashboard = new TransportistaDashboard();
            dashboard.setVisible(true);
        });
    }
}