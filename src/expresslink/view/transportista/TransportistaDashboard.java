package expresslink.view.transportista;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class TransportistaDashboard extends JFrame {
    // Colores corporativos
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);    // Azul principal
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);     // Verde para botones
    private final Color HOVER_COLOR = new Color(56, 142, 60);       // Verde oscuro al hacer hover
    
    private JPanel mainPanel;
    private String transportistaName = "Carlos Rodriguez"; // Esto vendria de la base de datos

    public TransportistaDashboard() {
        setTitle("Express Link - Centro de datos - Transportista");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        // Configuracion de panel principal
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Crear header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de contenido principal
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Panel izquierdo - Entregas Pendientes
        contentPanel.add(createPendingDeliveriesPanel());
        
        // Panel derecho - Resumen e Historial
        contentPanel.add(createSummaryPanel());

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

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

    private JPanel createPendingDeliveriesPanel() {
        JPanel panel = createBasePanel("Entregas Pendientes");
        JPanel deliveriesPanel = new JPanel();
        deliveriesPanel.setLayout(new BoxLayout(deliveriesPanel, BoxLayout.Y_AXIS));
        deliveriesPanel.setBackground(BACKGROUND_COLOR);

        // Agregar entregas de ejemplo
        deliveriesPanel.add(createDeliveryCard("#2024-001", "Prioridad Alta", "Juan Perez", "Av. Principal 123"));
        deliveriesPanel.add(Box.createVerticalStrut(10));
        deliveriesPanel.add(createDeliveryCard("#2024-002", "Prioridad Media", "Maria Garcia", "Calle Norte 456"));

        JScrollPane scrollPane = new JScrollPane(deliveriesPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDeliveryCard(String id, String priority, String name, String address) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(BevelBorder.RAISED, new Color(220, 220, 220), new Color(255, 255, 255)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        JPanel headerPanel = createHeaderPanelWithPriority(id, priority);

        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        clientPanel.setBackground(Color.WHITE);
        JLabel locationIcon = new JLabel("📍 ");
        locationIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel addressLabel = new JLabel(name + " - " + address);
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressLabel.setForeground(new Color(100, 100, 100));
        clientPanel.add(locationIcon);
        clientPanel.add(addressLabel);

        infoPanel.add(headerPanel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(clientPanel);
        card.add(infoPanel, BorderLayout.CENTER);

        JButton deliverButton = createDeliverButton();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(deliverButton);
        card.add(buttonPanel, BorderLayout.EAST);

    // Efecto de desplazamiento
    addHoverEffect(card, infoPanel, headerPanel, clientPanel, buttonPanel);

        return card;
    }

    private JPanel createHeaderPanelWithPriority(String id, String priority) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(Color.WHITE);
        JLabel idLabel = new JLabel(id);
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel priorityLabel = new JLabel(" - " + priority);
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priorityLabel.setForeground(priority.contains("Alta") ? new Color(198, 40, 40) : new Color(251, 140, 0));
        headerPanel.add(idLabel);
        headerPanel.add(priorityLabel);
        return headerPanel;
    }

    private JButton createDeliverButton() {
        JButton deliverButton = new JButton("Entregar");
        deliverButton.setPreferredSize(new Dimension(120, 40)); // Tamaño del botón
        deliverButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Fuente y estilo del texto
        deliverButton.setBackground(SUCCESS_COLOR); // Color de fondo del botón (verde)
        deliverButton.setForeground(Color.WHITE); // Color del texto (blanco)
        deliverButton.setFocusPainted(false); // Quita el borde de enfoque cuando el botón es seleccionado
        deliverButton.setOpaque(true); // Asegura que el fondo del botón esté completamente sólido
        deliverButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1, true)); // Borde verde oscuro
        deliverButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor cuando se pasa sobre el botón

        // Efectos al pasar el mouse y al hacer clic
        deliverButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deliverButton.setBackground(HOVER_COLOR); // Cambia a un verde oscuro al hacer hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                deliverButton.setBackground(SUCCESS_COLOR); // Regresa al color verde original al salir
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                deliverButton.setBackground(new Color(0, 100, 0)); // Cambia a un verde más oscuro al hacer clic
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                deliverButton.setBackground(HOVER_COLOR); // Regresa al color de hover cuando se suelta el clic
            }
        });

        return deliverButton; // Retorna el botón configurado
    }

    private void addHoverEffect(JPanel card, JPanel... panels) {
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                for (JPanel panel : panels) panel.setBackground(new Color(250, 250, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                for (JPanel panel : panels) panel.setBackground(Color.WHITE);
            }
        });
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BACKGROUND_COLOR);

        JPanel summaryPanel = createBasePanel("Resumen del Día");
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        statsPanel.setBackground(new Color(240, 247, 255));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.add(new JLabel("Entregas Completadas: 8/15"));
        statsPanel.add(new JLabel("Tiempo promedio: 15 min"));
        summaryPanel.add(statsPanel, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.NORTH);

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

    private JPanel createHistoryEntry(String id, String status, String time, boolean success) {
        JPanel entry = new JPanel(new BorderLayout());
        entry.setBackground(success ? new Color(232, 245, 233) : new Color(255, 235, 238));
        entry.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel label = new JLabel(id + " - " + status + " (" + time + ")");
        label.setForeground(success ? new Color(46, 125, 50) : new Color(198, 40, 40));
        entry.add(label);
        return entry;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransportistaDashboard dashboard = new TransportistaDashboard();
            dashboard.setVisible(true);
        });
    }
}
