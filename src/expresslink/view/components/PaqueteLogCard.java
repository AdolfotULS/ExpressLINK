package expresslink.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import expresslink.model.LogPaquete;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

// Implementation for Paquete Log
public class PaqueteLogCard extends LogCard {
    private static final Color PAQUETE_COLOR = new Color(255, 87, 34); // Orange

    public PaqueteLogCard() {
        super(PAQUETE_COLOR);
    }

    @Override
    public void setLogContent(Object logObject) {
        LogPaquete log = (LogPaquete) logObject;
        titleLabel.setText("Log Paquete - Cambio Estado");
        setLogDate(log.getFecha());
        contentArea.setText(
                String.format("Paquete: %s\nEstado Anterior: %s\nEstado Nuevo: %s\nDescripción: %s\nUsuario: %s",
                        log.getPaquete().getNumSeguimiento(),
                        log.getEstadoAnterior(),
                        log.getEstadoNuevo(),
                        log.getDescripcion(),
                        log.getUsuario().getNombre()));
    }
}