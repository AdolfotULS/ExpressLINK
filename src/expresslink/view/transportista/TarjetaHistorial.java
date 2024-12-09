package expresslink.view.transportista;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

// Clase que representa una tarjeta de historial de entregas
public class TarjetaHistorial extends JPanel {
    // Colores para representar el estado de la entrega (exitoso o fallido)
    private final Color SUCCESS_COLOR = new Color(46, 125, 50); // Verde
    private final Color ERROR_COLOR = new Color(198, 40, 40); // Rojo

    // Atributos para almacenar la informacion de cada entrega
    private String pedidoId;
    private String transportista;
    private String clientName;
    private String address;
    private String status;
    private String time;
    private int intentos; // Numero de intentos de entrega
    private boolean success; // Indica si la entrega fue exitosa

    // Constructor que inicializa la tarjeta con los datos de la entrega
    public TarjetaHistorial(String pedidoId, String transportista, String clientName,
            String address, String status, String time, int intentos, boolean success) {
        this.pedidoId = pedidoId;
        this.transportista = transportista;
        this.clientName = clientName;
        this.address = address;
        this.status = status;
        this.time = time;
        this.intentos = intentos;
        this.success = success;

        // Configurar la interfaz grafica de la tarjeta
        setupUI();
    }

    // Metodo que configura el diseño y componentes de la tarjeta
    private void setupUI() {
        // Establecer un diseño de bordes con espacios
        setLayout(new BorderLayout(10, 5));
        // Cambiar el color de fondo dependiendo del estado (verde para exitoso, rojo
        // para fallido)
        setBackground(success ? new Color(232, 245, 233) : new Color(255, 235, 238));
        setBorder(new EmptyBorder(10, 15, 10, 15)); // Agregar margen interno

        // Panel principal que contiene la informacion
        JPanel mainInfoPanel = new JPanel();
        mainInfoPanel.setLayout(new BoxLayout(mainInfoPanel, BoxLayout.Y_AXIS)); // Diseño vertical
        mainInfoPanel.setBackground(getBackground()); // Igual color que el fondo de la tarjeta

        // Agregar los subpaneles con informacion a la tarjeta
        mainInfoPanel.add(createHeaderPanel()); // Encabezado
        mainInfoPanel.add(Box.createVerticalStrut(5)); // Espacio vertical
        mainInfoPanel.add(createClientPanel()); // Informacion del cliente
        mainInfoPanel.add(Box.createVerticalStrut(5)); // Espacio vertical
        mainInfoPanel.add(createDeliveryDetailsPanel()); // Detalles de la entrega

        add(mainInfoPanel, BorderLayout.CENTER); // Agregar al centro del panel principal
    }

    // Crear el panel con el encabezado (ID y estado)
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(getBackground());

        // Etiqueta para mostrar el ID del pedido
        JLabel idLabel = new JLabel(pedidoId);
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(success ? SUCCESS_COLOR : ERROR_COLOR);

        // Etiqueta para mostrar el estado de la entrega y la hora
        JLabel statusLabel = new JLabel(status + " (" + time + ")");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(success ? SUCCESS_COLOR : ERROR_COLOR);

        // Agregar las etiquetas al panel
        headerPanel.add(idLabel);
        headerPanel.add(new JLabel(" - "));
        headerPanel.add(statusLabel);

        return headerPanel;
    }

    // Crear el panel con informacion del cliente y direccion
    private JPanel createClientPanel() {
        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        clientPanel.setBackground(getBackground());

        // Icono y etiqueta para el nombre del cliente
        JLabel userIcon = new JLabel("👤"); // Icono de usuario
        JLabel clientLabel = new JLabel(clientName);
        clientLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Icono y etiqueta para la direccion
        JLabel locationIcon = new JLabel("📍"); // Icono de ubicacion
        JLabel addressLabel = new JLabel(address);
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Agregar los componentes al panel
        clientPanel.add(userIcon);
        clientPanel.add(clientLabel);
        clientPanel.add(Box.createHorizontalStrut(15)); // Espacio horizontal
        clientPanel.add(locationIcon);
        clientPanel.add(addressLabel);

        return clientPanel;
    }

    // Crear el panel con detalles de la entrega
    private JPanel createDeliveryDetailsPanel() {
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        detailsPanel.setBackground(getBackground());

        // Icono y etiqueta para el transportista
        JLabel deliveryIcon = new JLabel("🚚"); // Icono de camion
        JLabel transportistaLabel = new JLabel(transportista);
        transportistaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Etiqueta para los intentos de entrega
        JLabel attemptsLabel = new JLabel("Intentos: " + intentos);
        attemptsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Agregar los componentes al panel
        detailsPanel.add(deliveryIcon);
        detailsPanel.add(transportistaLabel);
        detailsPanel.add(Box.createHorizontalStrut(15)); // Espacio horizontal
        detailsPanel.add(attemptsLabel);

        return detailsPanel;
    }
}
