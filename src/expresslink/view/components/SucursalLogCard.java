package expresslink.view.components;

import java.awt.Color;

import expresslink.model.LogSucursal;

// Implementation for Sucursal Log
public class SucursalLogCard extends LogCard {
    private static final Color SUCURSAL_COLOR = new Color(33, 150, 243); // Blue

    public SucursalLogCard() {
        super(SUCURSAL_COLOR);
    }

    @Override
    public void setLogContent(Object logObject) {
        LogSucursal log = (LogSucursal) logObject;
        titleLabel.setText("Log Sucursal - " + log.getTipoEvento());
        setLogDate(log.getFecha());
        contentArea.setText(String.format("Sucursal: %s\nDescripción: %s\nUsuario: %s",
                log.getSucursal().getNombre(),
                log.getDescripcion(),
                log.getUsuario().getNombre()));
    }
}