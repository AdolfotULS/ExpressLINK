package expresslink.view.transportista;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.border.*;

import expresslink.controllers.transportista.TarjetaPedidoController;

public class TarjetaPedido extends JPanel {

    private final Color PRIMARY_COLOR = new Color(33, 150, 243); // Azul principal
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(100, 100, 100); // Color para texto secundario
    private final Color SUCCESS_COLOR = new Color(76, 175, 80); // Verde para botones de accion

    // Atributos para almacenar la informacion del pedido
    private String pedidoId;
    private String clientName;
    private String address;
    private String status;
    private TransportistaDashboard dashboard;

    // Constructor que inicializa los datos basicos del pedido
    public TarjetaPedido(String pedidoId, String clientName, String address) {
        this.pedidoId = pedidoId;
        this.clientName = clientName;
        this.address = address;
        this.status = "Pendiente";

        setupUI();
    }

    public void setDashboard(TransportistaDashboard dashboard) {
        this.dashboard = dashboard;
    }

    // Configura la estructura basica del panel
    private void setupUI() {
        setLayout(new BorderLayout(10, 5));
        setBackground(BACKGROUND_COLOR);
        // Crea un borde compuesto para dar profundidad visual
        setBorder(BorderFactory.createCompoundBorder(
                new SoftBevelBorder(BevelBorder.RAISED,
                        new Color(220, 220, 220),
                        new Color(255, 255, 255)),
                new EmptyBorder(15, 15, 15, 15)));

        // Organiza los componentes principales
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.EAST);

        addHoverEffect();
    }

    // Crea el panel que muestra la informacion del pedido
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(BACKGROUND_COLOR);

        // Configuracion del ID del pedido con estilo destacado
        JLabel idLabel = new JLabel(pedidoId);
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Panel para la informacion del cliente
        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        clientPanel.setBackground(BACKGROUND_COLOR);

        JLabel nameLabel = new JLabel(clientName);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(TEXT_COLOR);

        // Panel para la direccion con icono
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        addressPanel.setBackground(BACKGROUND_COLOR);
        JLabel locationIcon = new JLabel("📍 ");
        JLabel addressLabel = new JLabel(address);
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressLabel.setForeground(TEXT_COLOR);

        // Organiza todos los componentes
        clientPanel.add(nameLabel);
        addressPanel.add(locationIcon);
        addressPanel.add(addressLabel);

        infoPanel.add(idLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(clientPanel);
        infoPanel.add(addressPanel);

        return infoPanel;
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Crea el panel con el estado y boton de accion
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        actionPanel.setBackground(BACKGROUND_COLOR);

        // Etiqueta de estado
        JLabel statusLabel = new JLabel("Estado: " + status);
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Botón de entrega exitosa
        JButton deliverButton = new JButton("Entregar");
        styleButton(deliverButton, SUCCESS_COLOR);
        deliverButton.addActionListener(e -> handleDeliveryAction());

        // Botón de cancelar intento
        JButton cancelButton = new JButton("Cancelar Intento");
        styleButton(cancelButton, new Color(211, 47, 47)); // Rojo más suave
        cancelButton.addActionListener(e -> handleCancelAction());

        actionPanel.add(statusLabel);
        actionPanel.add(deliverButton);
        actionPanel.add(cancelButton);

        return actionPanel;
    }

    private void handleCancelAction() {
        String motivo = JOptionPane.showInputDialog(this,
                "Por favor, ingrese el motivo de la cancelación:",
                "Cancelar Entrega",
                JOptionPane.QUESTION_MESSAGE);

        if (motivo != null && !motivo.trim().isEmpty()) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                TarjetaPedidoController controller = new TarjetaPedidoController(pedidoId, dashboard);

                if (controller.cancelarEntrega(motivo)) {
                    // Verificar que el componente tenga un padre antes de intentar removerlo
                    Container parent = getParent();
                    if (parent != null) {
                        setVisible(false);
                        parent.remove(this);
                        parent.revalidate();
                        parent.repaint();
                    } else {
                        // Si no hay padre, solo ocultar el componente
                        setVisible(false);
                    }

                    // Refrescar lista de pedidos pendientes
                    if (dashboard != null) {
                        dashboard.cargarPedidosPendientes();
                        dashboard.actualizarEstadisticas(true);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al cancelar la entrega: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                setCursor(Cursor.getDefaultCursor());
                dashboard.actualizarEstadisticas(false);
            }
        }
    }

    private void handleDeliveryAction() {
        if (dashboard == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: Dashboard no configurado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea marcar como entregado el pedido " + pedidoId + "?",
                "Confirmar entrega",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                TarjetaPedidoController controller = new TarjetaPedidoController(pedidoId, dashboard);

                if (controller.entregarPedido()) {
                    // Verificar que el componente tenga un padre antes de intentar removerlo
                    Container parent = getParent();
                    if (parent != null) {
                        setVisible(false);
                        parent.remove(this);
                        parent.revalidate();
                        parent.repaint();
                    } else {
                        setVisible(false);
                    }

                    // Refrescar vista de pendientes
                    if (dashboard != null) {
                        dashboard.actualizarEstadisticas(true);
                        dashboard.cargarPedidosPendientes();
                    }

                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al entregar el pedido: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                setCursor(Cursor.getDefaultCursor());
                dashboard.actualizarEstadisticas(false);
            }
        }
    }

    // Agrega efecto visual al pasar el mouse
    private void addHoverEffect() {
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(250, 250, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(BACKGROUND_COLOR);
            }
        });
    }

    // Metodos getter para acceder a la informacion del pedido
    public String getPedidoId() {
        return pedidoId;
    }

    public String getStatus() {
        return status;
    }

    /*
     * // Metodo main para pruebas independientes
     * public static void main(String[] args) {
     * SwingUtilities.invokeLater(() -> {
     * JFrame frame = new JFrame("Test Tarjeta Pedido");
     * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     * frame.setSize(600, 200);
     * frame.setLocationRelativeTo(null);
     * TarjetaPedido tarjeta = new TarjetaPedido(
     * "#2024-003",
     * "Juan Perez",
     * "Av. Principal 123"
     * );
     * frame.add(tarjeta);
     * frame.setVisible(true);
     * });
     * }
     */
}
