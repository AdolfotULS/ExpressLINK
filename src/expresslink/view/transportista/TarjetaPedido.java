package expresslink.view.transportista;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class TarjetaPedido extends JPanel {
    
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);    // Azul principal
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(100, 100, 100);      // Color para texto secundario
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);     // Verde para botones de accion
    
    // Atributos para almacenar la informacion del pedido
    private String pedidoId;
    private String clientName;
    private String address;
    private String status;
    
    // Constructor que inicializa los datos basicos del pedido
    public TarjetaPedido(String pedidoId, String clientName, String address) {
        this.pedidoId = pedidoId;
        this.clientName = clientName;
        this.address = address;
        this.status = "Pendiente";
        
        setupUI();
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
            new EmptyBorder(15, 15, 15, 15)
        ));

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
    
    // Crea el panel con el estado y boton de accion
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        actionPanel.setBackground(BACKGROUND_COLOR);
        
        // Etiqueta de estado
        JLabel statusLabel = new JLabel("Estado: " + status);
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Configuracion del boton de entrega
        JButton deliverButton = new JButton("Entregar");
        deliverButton.setPreferredSize(new Dimension(120, 40));
        deliverButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deliverButton.setBackground(SUCCESS_COLOR);
        deliverButton.setForeground(Color.WHITE);
        deliverButton.setFocusPainted(false);
        deliverButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Manejo de la accion del boton con confirmacion
        deliverButton.addActionListener(e -> {
            // Muestra dialogo de confirmacion
            int option = JOptionPane.showConfirmDialog(
                this,
                "¿Esta seguro que desea marcar como entregado el pedido " + pedidoId + "?",
                "Confirmar entrega",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            // Procesa la respuesta del usuario
            if (option == JOptionPane.YES_OPTION) {
                status = "Entregado";
                statusLabel.setText("Estado: " + status);
                deliverButton.setEnabled(false);
                
                // Muestra confirmacion de exito
                JOptionPane.showMessageDialog(
                    this,
                    "El pedido " + pedidoId + " ha sido marcado como entregado exitosamente.",
                    "Entrega confirmada",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        actionPanel.add(statusLabel);
        actionPanel.add(deliverButton);
        
        return actionPanel;
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
    public String getPedidoId() { return pedidoId; }
    public String getStatus() { return status; }
    
    // Metodo main para pruebas independientes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Tarjeta Pedido");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 200);
            frame.setLocationRelativeTo(null);
            
            TarjetaPedido tarjeta = new TarjetaPedido(
                "#2024-003", 
                "Juan Perez", 
                "Av. Principal 123"
            );
            
            frame.add(tarjeta);
            frame.setVisible(true);
        });
    }
}
