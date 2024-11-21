package expresslink.view.cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import expresslink.controllers.cliente.SeguimientoController;
import expresslink.controllers.cliente.SeguimientoController.DatosSeguimiento;
import expresslink.model.enums.*;

public class SeguimientoPanel extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(33, 150, 243);
    private static final Color COLOR_SUCCESS = new Color(76, 175, 80);
    private static final Color COLOR_INACTIVE = new Color(189, 189, 189);
    private static final Color COLOR_BACKGROUND = new Color(240, 242, 245);
    private static final Color COLOR_WHITE = Color.WHITE;

    private final SeguimientoController controlador;
    private JTextField searchField;
    private JPanel statusPanel;
    private JPanel trackerPanel;
    private int currentState = 0;
    private DatosSeguimiento datosActuales;

    public SeguimientoPanel() {
        this.controlador = new SeguimientoController();
        setLayout(new BorderLayout());
        setBackground(COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
        statusPanel.setVisible(false);
    }

    private void initComponents() {
        // Panel de búsqueda
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);

        // Panel de estado
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(COLOR_WHITE);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)));

        // Número de pedido
        JLabel pedidoLabel = new JLabel("Pedido #2024-0001");
        pedidoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusPanel.add(pedidoLabel, BorderLayout.NORTH);

        // Panel del tracker
        trackerPanel = createTrackerPanel();
        statusPanel.add(trackerPanel, BorderLayout.CENTER);

        // Panel de información
        JPanel infoPanel = createInfoPanel();
        statusPanel.add(infoPanel, BorderLayout.SOUTH);

        add(statusPanel, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(COLOR_BACKGROUND);

        JLabel searchLabel = new JLabel("Número de Seguimiento:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 35));

        JButton searchButton = new JButton("Buscar");
        searchButton.setPreferredSize(new Dimension(100, 35));
        searchButton.setBackground(COLOR_PRIMARY);
        searchButton.setForeground(COLOR_WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchButton.addActionListener(e -> buscarPedido());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private JPanel createTrackerPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTracker(g);
            }
        };
        panel.setBackground(COLOR_WHITE);
        panel.setPreferredSize(new Dimension(0, 100));

        return panel;
    }

    private void drawTracker(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = trackerPanel.getWidth();
        int height = trackerPanel.getHeight();
        int startX = 50;
        int endX = width - 50;
        int y = height / 2;

        // Estados del pedido
        String[] estados = { "Pendiente", "En Transito", "Entregado" };
        int numEstados = estados.length;
        int spacing = (endX - startX) / (numEstados - 1);

        // Dibujar línea de progreso
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(COLOR_INACTIVE);
        g2d.drawLine(startX, y, endX, y);

        // Dibujar línea de progreso completada
        int currentState = 3; // Este valor debería venir del pedido actual
        g2d.setColor(COLOR_SUCCESS);
        g2d.drawLine(startX, y, startX + (spacing * currentState), y);

        // Dibujar círculos y etiquetas
        for (int i = 0; i < numEstados; i++) {
            int x = startX + (i * spacing);

            // Determinar color del círculo
            if (i < currentState) {
                g2d.setColor(COLOR_SUCCESS); // Completado
            } else if (i == currentState) {
                g2d.setColor(COLOR_PRIMARY); // Actual
            } else {
                g2d.setColor(COLOR_INACTIVE); // Pendiente
            }

            // Dibujar círculo
            g2d.fillOval(x - 10, y - 10, 20, 20);

            // Dibujar etiqueta
            g2d.setColor(Color.DARK_GRAY);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(estados[i]);
            g2d.drawString(estados[i], x - (labelWidth / 2), y + 30);
        }
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_WHITE);

        // Mostrar mensaje inicial
        JLabel mensajeInicial = new JLabel("Ingrese un número de seguimiento para ver los detalles del pedido");
        mensajeInicial.setFont(new Font("Arial", Font.PLAIN, 14));
        mensajeInicial.setForeground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 20, 5);
        panel.add(mensajeInicial, gbc);

        return panel;
    }

    private void buscarPedido() {
        String numeroSeguimiento = searchField.getText().trim();
        if (numeroSeguimiento.isEmpty()) {
            mostrarError("Por favor ingrese un número de seguimiento");
            return;
        }

        try {
            datosActuales = controlador.buscarPedido(numeroSeguimiento);

            if (datosActuales == null) {
                mostrarError("No se encontró el pedido con el número proporcionado");
                statusPanel.setVisible(false);
                return;
            }

            // Actualizar UI con datos reales
            actualizarUI(datosActuales);

            statusPanel.setVisible(true);
            revalidate();
            repaint();

        } catch (SQLException e) {
            mostrarError("Error al buscar el pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void actualizarUI(DatosSeguimiento datos) {
        // Actualizar número de pedido
        JLabel pedidoLabel = (JLabel) statusPanel.getComponent(0);
        pedidoLabel.setText("Pedido #" + datos.numeroSeguimiento);

        // Actualizar estado en el tracker
        currentState = datos.posicionEstado;
        trackerPanel.repaint();

        // Actualizar panel de información
        JPanel infoPanel = (JPanel) statusPanel.getComponent(2);
        actualizarInfoPanel(infoPanel, datos);
    }

    private void actualizarInfoPanel(JPanel infoPanel, DatosSeguimiento datos) {
        infoPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] labels = {
                "Origen: " + datos.sucursalOrigen,
                "Destino: " + datos.lugarDestino,
                "Fecha estimada: " + new SimpleDateFormat("dd/MM/yyyy").format(datos.fechaEstimada),
                "Estado actual: " + datos.estado.toString()
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            infoPanel.add(label, gbc);
        }

        infoPanel.revalidate();
        infoPanel.repaint();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void resetBusqueda() {
        searchField.setText("");
        statusPanel.setVisible(false);
        datosActuales = null;
        currentState = 0;
        revalidate();
        repaint();
    }
}