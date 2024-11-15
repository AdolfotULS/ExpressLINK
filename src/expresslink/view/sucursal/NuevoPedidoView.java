package expresslink.view.sucursal;

import javax.swing.*;
import java.awt.*;

public class NuevoPedidoView extends JPanel {
    private JTextField clienteField;
    private JTextField destinatarioField;
    private JTextField direccionField;
    private JComboBox<String> ciudadDestino;
    private JSpinner pesoSpinner;
    private JSpinner dimensionesSpinner;
    private JButton calcularCostoBtn;
    private JButton confirmarPedidoBtn;
    
    // Panel para datos del remitente
    private JPanel datosRemitentePanel;
    // Panel para datos del destinatario
    private JPanel datosDestinatarioPanel;
    // Panel para detalles del paquete
    private JPanel detallesPaquetePanel;
}    