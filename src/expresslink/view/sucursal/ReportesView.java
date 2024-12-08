package expresslink.view.sucursal;

import javax.swing.JPanel;

import expresslink.controllers.sucursal.PedidosTransitoController;
import expresslink.model.Sucursal;
import expresslink.view.components.RefreshablePanel;

public class ReportesView extends JPanel implements RefreshablePanel {
    private Sucursal sucursal;

    public ReportesView(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }

        this.sucursal = sucursal;
        this.controlador = new PedidosTransitoController(this.sucursal);
    }

    @Override
    public void refreshData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshData'");
    }

    @Override
    public void stopRefresh() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopRefresh'");
    }

}
