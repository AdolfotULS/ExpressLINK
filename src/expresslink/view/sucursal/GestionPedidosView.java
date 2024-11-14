package expresslink.view.sucursal;

import javax.swing.*;
import java.awt.*;


public class GestionPedidosView extends JPanel {
    private JTable pedidosTable;
    private JComboBox<String> filtroEstado;
    private JButton asignarTransportistaBtn;
    private JButton verDetallesBtn;
    
    // Panel para lista de pedidos
    private JPanel listaPedidosPanel;
    // Panel para acciones sobre pedidos
    private JPanel accionesPedidosPanel;
}
