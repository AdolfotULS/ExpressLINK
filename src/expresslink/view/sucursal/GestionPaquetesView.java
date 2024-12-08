package expresslink.view.sucursal;

import javax.swing.JPanel;

import expresslink.controllers.sucursal.GestionPaquetesController;
import expresslink.model.Sucursal;
import expresslink.model.Usuario;
import expresslink.view.components.RefreshablePanel;

public class GestionPaquetesView extends JPanel implements RefreshablePanel {
    private Sucursal sucursal;
    private Usuario usuario;
    private GestionPaquetesController controlador;

    public GestionPaquetesView(Usuario usuario, Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }

        this.sucursal = sucursal;
        this.usuario = usuario;
        this.controlador = new GestionPaquetesController(usuario, sucursal);

    }

    private void inicializarGUI() {
        // Boton Actualizar
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
