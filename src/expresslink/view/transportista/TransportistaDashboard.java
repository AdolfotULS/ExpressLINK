package expresslink.view.transportista;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;


 //Clase que representa el panel principal del transportista.

public class TransportistaDashboard extends JFrame {
    // Colores definidos para mantener consistencia en el diseño
    private final Color PRIMARY_COLOR = new Color(33, 150, 243); // Azul principal
    private final Color BACKGROUND_COLOR = Color.WHITE; // Fondo blanco
    private final Color SUCCESS_COLOR = new Color(76, 175, 80); // Verde para exitos
    private final Color HOVER_COLOR = new Color(56, 142, 60); // Verde oscuro

    // Paneles globales para organizar contenido
    private JPanel mainPanel; 
    private JPanel entriesPanel; // Panel donde se agregaran las tarjetas de historial
    private String transportistaName = "Carlos Rodriguez"; // Nombre del transportista

    
    //Constructor de la clase que inicializa la ventana.
     
    public TransportistaDashboard() {
        // Configuracion basica de la ventana
        setTitle("Express Link - Centro de datos - Transportista");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600); // Tamaño de la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel principal con un margen
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Crear el encabezado
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Crear el contenido principal (2 paneles)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Agregar paneles secundarios al contenido principal
        contentPanel.add(createPendingDeliveriesPanel()); // Panel de entregas pendientes
        contentPanel.add(createSummaryPanel()); // Panel de resumen

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel); // Agregar el panel principal a la ventana
    }

    
      //Crea el panel del encabezado superior.
     
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR); // Fondo azul
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Margen interno

        // Titulo del encabezado
        JLabel titleLabel = new JLabel("Panel de Transportista");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // Texto en blanco

        headerPanel.add(titleLabel, BorderLayout.WEST); // Alinear a la izquierda
        return headerPanel;
    }

    
     //Crea el panel de entregas pendientes.
     
    private JPanel createPendingDeliveriesPanel() {
        JPanel panel = createBasePanel("Entregas Pendientes"); // Panel base con titulo

        // Panel interno para las tarjetas de entregas
        JPanel deliveriesPanel = new JPanel();
        deliveriesPanel.setLayout(new BoxLayout(deliveriesPanel, BoxLayout.Y_AXIS)); // Organizar en columnas
        deliveriesPanel.setBackground(BACKGROUND_COLOR);

        // Agregar ejemplos de tarjetas de pedidos
        deliveriesPanel.add(new TarjetaPedido("#2024-001", "Juan Perez", "Av. Principal 123"));
        deliveriesPanel.add(Box.createVerticalStrut(10)); // Espaciado
        deliveriesPanel.add(new TarjetaPedido("#2024-002", "Maria Garcia", "Calle Norte 456"));
        deliveriesPanel.add(Box.createVerticalStrut(10));
        deliveriesPanel.add(new TarjetaPedido("#2024-003", "Carlos Lopez", "Av. Central 321"));

        // Hacer que el panel sea desplazable
        JScrollPane scrollPane = new JScrollPane(deliveriesPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollPane, BorderLayout.CENTER); // Agregar al panel principal
        return panel;
    }

    
    //Crea el panel de resumen diario y el historial.
     
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20)); // Layout con espaciado
        panel.setBackground(BACKGROUND_COLOR);

        // Panel de resumen del dia
        JPanel summaryPanel = createBasePanel("Resumen del Dia");
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 5, 5)); // Layout en filas
        statsPanel.setBackground(new Color(240, 247, 255)); // Fondo claro
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Margen interno
        statsPanel.add(new JLabel("Entregas Completadas: 8/15"));
        statsPanel.add(new JLabel("Tiempo promedio: 15 min"));
        summaryPanel.add(statsPanel, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.NORTH);

        // Panel de historial de entregas
        JPanel historyPanel = createBasePanel("Historial de Entregas");
        entriesPanel = new JPanel(); // Panel global para agregar entradas
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        entriesPanel.setBackground(BACKGROUND_COLOR);

        // Agregar ejemplos de historial
        entriesPanel.add(new TarjetaHistorial(
            "#2024-000", transportistaName, "Juan Perez", "Av. Principal 123",
            "Entregado", "10:30", 1, true
        ));
        entriesPanel.add(Box.createVerticalStrut(10));

        // Hacer el historial desplazable
        JScrollPane scrollPane = new JScrollPane(entriesPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        historyPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(historyPanel, BorderLayout.CENTER);

        return panel;
    }
    
    
      //Crea un panel base con un titulo en la parte superior.
     
    private JPanel createBasePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true), // Borde gris
            new EmptyBorder(20, 20, 20, 20) // Margen interno
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0)); // Espaciado inferior
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

 
     // Metodo para agregar una nueva tarjeta al historial.
    
    public void addHistoryEntry(String pedidoId, String clientName, String address,
                                String status, String time, int intentos, boolean success) {
        entriesPanel.add(new TarjetaHistorial(
            pedidoId, transportistaName, clientName, address, status, time, intentos, success
        ));
        entriesPanel.add(Box.createVerticalStrut(10)); // Espaciado entre tarjetas
        entriesPanel.revalidate(); // Actualizar el panel
        entriesPanel.repaint(); // Redibujar el panel
    }

    //Metodo principal para ejecutar el programa.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransportistaDashboard dashboard = new TransportistaDashboard();
            dashboard.setVisible(true);

            // Ejemplo de agregar una entrada al historial despues de mostrar la ventana
            dashboard.addHistoryEntry(
                "#2024-005", "Carlos Lopez", "Av. Central 321",
                "Entregado", "13:00", 1, true
            );
        });
    }
}
