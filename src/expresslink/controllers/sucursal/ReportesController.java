package expresslink.controllers.sucursal;

import expresslink.model.Sucursal;

public class ReportesController {
    private Sucursal sucursal;

    public ReportesController(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }
        this.sucursal = sucursal;
    }

}
