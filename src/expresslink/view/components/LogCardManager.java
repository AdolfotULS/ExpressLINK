package expresslink.view.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

import expresslink.model.*;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LogCardManager {
    public static JPanel createLogCardsPanel(List<LogSucursal> sucursalLogs,
            List<LogTransportista> transportistaLogs,
            List<LogPaquete> paqueteLogs) {
        List<LogCard> allCards = new ArrayList<>();

        // Create cards for each log type
        for (LogSucursal log : sucursalLogs) {
            SucursalLogCard card = new SucursalLogCard();
            card.setLogContent(log);
            allCards.add(card);
        }

        for (LogTransportista log : transportistaLogs) {
            TransportistaLogCard card = new TransportistaLogCard();
            card.setLogContent(log);
            allCards.add(card);
        }

        for (LogPaquete log : paqueteLogs) {
            PaqueteLogCard card = new PaqueteLogCard();
            card.setLogContent(log);
            allCards.add(card);
        }

        // Sort cards by date (most recent first)
        Collections.sort(allCards, (c1, c2) -> c2.getLogDate().compareTo(c1.getLogDate()));

        // Create panel with GridBagLayout for responsive layout
        JPanel cardsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add cards to panel
        for (LogCard card : allCards) {
            cardsPanel.add(card, gbc);
        }

        return cardsPanel;
    }
}