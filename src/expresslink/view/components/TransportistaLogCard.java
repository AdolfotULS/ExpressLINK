package expresslink.view.components;

import java.awt.Color;

import expresslink.model.LogTransportista;

// Implementation for Transportista Log
public class TransportistaLogCard extends LogCard {
    private static final Color TRANSPORTISTA_COLOR = new Color(76, 175, 80); // Green

    public TransportistaLogCard() {
        super(TRANSPORTISTA_COLOR);
    }

    @Override
    public void setLogContent(Object logObject) {
        LogTransportista log = (LogTransportista) logObject;
        titleLabel.setText("Log Transportista - " + log.getTipoEvento());
        setLogDate(log.getFecha());
        contentArea.setText(String.format("Transportista: %s\nPaquete: %s\nDescripción: %s",
                log.getTransportista().getNombre(),
                log.getPaquete().getNumSeguimiento(),
                log.getDescripcion()));
    }
}