package expresslink.view.components;

import javax.swing.*;
import expresslink.model.Pedido.EstadoPedido;
import java.awt.*;

public class SeguimientoPedidoPanel extends JPanel {
    private List<EstadoPedido> estadosCompletados;
    private JLabel ubicacionActual;
    private JLabel estimacionLlegada;
    
    public void actualizarProgreso(EstadoPedido nuevoEstado) {
        // Actualiza el progreso visual del envío
    }
}
