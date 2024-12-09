package expresslink.view.transportista;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.border.*;

import expresslink.controllers.transportista.TransportistaController;
import expresslink.model.Transportista;
import expresslink.model.Usuario;
import expresslink.view.login.LoginView;

// En TransportistaDashboard.java
import java.util.List;

// En TransportistaController.java 
import java.util.List;
import java.util.ArrayList;

public class TransportistaDashboard extends JFrame {
    private final Color COLOR_PRIMARIO = new Color(33, 150, 243);
    private final Color COLOR_FONDO = Color.WHITE;

    private Usuario usuario;
    private Transportista transportista;
    private TransportistaController controlador;
    private LoginView view;

    private JLabel completadasLabel;
    private JLabel tiempoPromedioLabel;
    private int paquetesTotales;
    private int paquetesProcesados;
    private final LocalTime startTime;
    private double tiempoPromedio;

    private JPanel mainPanel;
    private JPanel deliveriesPanel; // Hacer panel de entregas accesible
    private JPanel entriesPanel;
    private String transportistaName;

    public TransportistaDashboard(Usuario usuario, LoginView view) {
        this.usuario = usuario;
        this.controlador = new TransportistaController(usuario);
        this.view = view;
        this.paquetesTotales = 0;
        this.paquetesProcesados = 0;
        this.startTime = LocalTime.now();
        this.tiempoPromedio = 0.0;

        try {
            this.transportista = controlador.obtenerDatosTransportista();
            if (this.transportista == null) {
                throw new RuntimeException("No se encontraron datos de transportista");
            }
            this.transportistaName = transportista.getNombre();

            // Actualizar disponibilidad al iniciar
            controlador.actualizarDisponibilidad(true);

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
        cargarPedidosPendientes();
        actualizarEstadisticas(false);
    }

    private void inicializarGUI() {
        // Configuracion basica de la ventana
        setTitle("Express Link - Transportista " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600); // Tamaño de la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel principal con un margen
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Crear el encabezado
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Crear el contenido principal (2 paneles)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(COLOR_FONDO);

        // Agregar paneles secundarios al contenido principal
        contentPanel.add(createPendingDeliveriesPanel()); // Panel de entregas pendientes
        contentPanel.add(createSummaryPanel()); // Panel de resumen

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel); // Agregar el panel principal a la ventana
    }

    // Crea el panel del encabezado superior.

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_PRIMARIO);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel titleLabel = new JLabel("Panel de Transportista");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Panel derecho para botón de cerrar sesión
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(COLOR_PRIMARIO);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(255, 87, 34)); // Color rojo-naranja
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> cerrarSesion());

        rightPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private void cerrarSesion() {
        try {
            // Actualizar estado a no disponible
            controlador.actualizarDisponibilidad(false);

            // Cerrar ventana actual
            this.dispose();

            // Volver a la pantalla de login
            view.controlador.cerrarSesion();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar estado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Crea el panel de entregas pendientes.

    private JPanel createPendingDeliveriesPanel() {
        JPanel panel = createBasePanel("Entregas Pendientes");

        // Panel interno para las tarjetas de entregas
        deliveriesPanel = new JPanel(); // Inicializar el panel
        deliveriesPanel.setLayout(new BoxLayout(deliveriesPanel, BoxLayout.Y_AXIS));
        deliveriesPanel.setBackground(COLOR_FONDO);

        // El panel empieza vacío - los pedidos se cargan en cargarPedidosPendientes()

        JScrollPane scrollPane = new JScrollPane(deliveriesPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public synchronized void actualizarEstadisticas(boolean process) {
        try {
            if (process) {
                this.paquetesProcesados++;

                // Calculate elapsed time in minutes
                LocalTime currentTime = LocalTime.now();
                long elapsedMinutes = startTime.until(currentTime, java.time.temporal.ChronoUnit.MINUTES);

                // Calculate average time per delivery
                this.tiempoPromedio = (double) elapsedMinutes / paquetesProcesados;
            }

            // Update labels
            if (completadasLabel != null) {
                completadasLabel.setText(String.format("Entregas Completadas: %d/%d",
                        paquetesProcesados, paquetesTotales));
            }

            if (tiempoPromedioLabel != null) {
                tiempoPromedioLabel.setText(String.format("Tiempo promedio: %.1f min",
                        tiempoPromedio));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Crea el panel de resumen diario y el historial.

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(COLOR_FONDO);

        // Panel de resumen del día
        JPanel summaryPanel = createBasePanel("Resumen del Día");
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        statsPanel.setBackground(new Color(240, 247, 255));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Crear y guardar referencias a los labels
        completadasLabel = new JLabel("Entregas Completadas: 0/0");
        tiempoPromedioLabel = new JLabel("Tiempo promedio: 0 min");

        statsPanel.add(completadasLabel);
        statsPanel.add(tiempoPromedioLabel);

        summaryPanel.add(statsPanel, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.NORTH);

        // Panel de historial de entregas
        JPanel historyPanel = createBasePanel("Historial de Entregas");
        entriesPanel = new JPanel(); // Panel global para agregar entradas
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        entriesPanel.setBackground(COLOR_FONDO);

        // Hacer el historial desplazable
        JScrollPane scrollPane = new JScrollPane(entriesPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        historyPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(historyPanel, BorderLayout.CENTER);

        return panel;
    }

    // Crea un panel base con un titulo en la parte superior.

    private JPanel createBasePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_FONDO);
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
        // Agregar al inicio del panel para mostrar los más recientes primero
        entriesPanel.add(new TarjetaHistorial(
                pedidoId, transportistaName, clientName, address,
                status, time, intentos, success), 0);
        entriesPanel.add(Box.createVerticalStrut(10), 1);

        // Actualizar UI
        entriesPanel.revalidate();
        entriesPanel.repaint();

        // Si el panel tiene muchas entradas, considerar limitar
        if (entriesPanel.getComponentCount() > 50) { // 25 entradas * 2 (tarjeta + espacio)
            entriesPanel.remove(entriesPanel.getComponentCount() - 1); // Remover último espacio
            entriesPanel.remove(entriesPanel.getComponentCount() - 1); // Remover última tarjeta
        }
    }

    void cargarPedidosPendientes() {
        try {
            // Limpiar entradas existentes
            deliveriesPanel.removeAll();

            List<TarjetaPedido> pedidos = controlador.obtenerPedidosAsignados();
            if (paquetesTotales == 0) {
                this.paquetesTotales = pedidos.size();
            }

            for (TarjetaPedido pedido : pedidos) {
                // Configurar dashboard antes de agregar al panel
                pedido.setDashboard(this);

                // Agregar al panel y asegurar que tenga un contenedor padre
                deliveriesPanel.add(pedido);
                deliveriesPanel.add(Box.createVerticalStrut(10));
            }

            // Actualizar UI
            deliveriesPanel.revalidate();
            deliveriesPanel.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar pedidos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
