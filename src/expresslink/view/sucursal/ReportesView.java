package expresslink.view.sucursal;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.util.List;

import expresslink.controllers.sucursal.*;
import expresslink.model.*;
import expresslink.view.components.*;

public class ReportesView extends JPanel implements RefreshablePanel {
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final Sucursal sucursal;
    private final ReportesController controlador;
    private final Timer timer;
    private final JLabel statusLabel;
    private final JLabel contadorLabel;
    private JPanel cardsPanel;

    public ReportesView(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }

        this.sucursal = sucursal;
        this.controlador = new ReportesController(this.sucursal);

        // Configurar el timer para actualizar cada 30 segundos
        this.timer = new Timer(30000, e -> refreshData());
        timer.start();

        this.statusLabel = new JLabel("Última actualización: " + dateFormat.format(new java.util.Date()));
        this.contadorLabel = new JLabel("Total registros: 0");

        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        inicializarGUI();
        refreshData();
    }

    private void inicializarGUI() {
        // Panel de encabezado
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel izquierdo del header
        JPanel leftHeaderPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftHeaderPanel.setBackground(PRIMARY_COLOR);

        JLabel titleLabel = new JLabel("Registro de Actividades");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        contadorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        contadorLabel.setForeground(Color.WHITE);

        leftHeaderPanel.add(titleLabel);
        leftHeaderPanel.add(contadorLabel);

        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);

        // Agregar status label al header
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerPanel.add(statusLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Configurar panel de tarjetas
        cardsPanel = new JPanel(new BorderLayout());
        cardsPanel.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Panel contenedor para las tarjetas
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void refreshData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            List<LogSucursal> sucursalLogs = controlador.obtenerLogSucursal();
            List<LogTransportista> transportistaLogs = controlador.obtenerLogTransportista();
            List<LogPaquete> paqueteLogs = controlador.obtenerLogPaquete();

            JPanel newCardsPanel = LogCardManager.createLogCardsPanel(
                    sucursalLogs, transportistaLogs, paqueteLogs);

            cardsPanel.removeAll();
            cardsPanel.add(newCardsPanel, BorderLayout.CENTER);
            cardsPanel.revalidate();
            cardsPanel.repaint();

            // Actualizar contador y estado
            int totalLogs = sucursalLogs.size() + transportistaLogs.size() + paqueteLogs.size();
            contadorLabel.setText("Total registros: " + totalLogs);
            statusLabel.setText("Última actualización: " + dateFormat.format(new java.util.Date()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar los datos: " + e.getMessage(),
                    "Error de Actualización",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override
    public void stopRefresh() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    public void disposeView() {
        stopRefresh();
        SwingUtilities.getWindowAncestor(this).dispose();
    }
}