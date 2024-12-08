package expresslink.view.sucursal;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.List;

import expresslink.controllers.sucursal.InformacionGeneralController;
import expresslink.model.Paquete;
import expresslink.model.Sucursal;
import expresslink.view.components.RefreshablePanel;

public class InformacionGeneralView extends JPanel implements RefreshablePanel {
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color CARD_BORDER = new Color(230, 230, 230);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font CARD_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font COUNTER_FONT = new Font("Segoe UI", Font.BOLD, 40);
    private final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final DecimalFormat numberFormat = new DecimalFormat("#,###");

    private InformacionGeneralController controlador;
    private Sucursal sucursal;
    private final Timer timer;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // Componentes que necesitan actualización
    private JLabel transportistasLabel;
    private JLabel pendientesLabel;
    private JLabel entregadosLabel;
    private JLabel transitoLabel;
    private JLabel pendientesCounter;
    private JLabel transitoCounter;
    private JLabel entregadosCounter;
    private JLabel pendientesSubtitle;
    private JLabel transitoSubtitle;
    private JLabel entregadosSubtitle;
    private DefaultTableModel tableModel;
    private JLabel lastUpdateLabel;

    public InformacionGeneralView(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }

        this.sucursal = sucursal;
        this.controlador = new InformacionGeneralController(this.sucursal);

        // Configurar el timer para actualizar cada 30 segundos
        this.timer = new Timer(30000, e -> refreshData());
        timer.start();

        setLayout(new BorderLayout());
        inicializarGUI();
        refreshData();
    }

    private void inicializarGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Header Panel con gradiente
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);

        // Panel de estadísticas rápidas
        JPanel quickStatsPanel = createQuickStatsPanel();
        mainPanel.add(quickStatsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaciado

        // // Panel de métricas principales
        // JPanel metricsPanel = createMetricsPanel();
        // mainPanel.add(metricsPanel);
        // mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaciado

        // Panel de la tabla
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, w, h,
                        new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 220));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel(sucursal.getNombre() + " - " + sucursal.getCiudad());
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        lastUpdateLabel = new JLabel("Última actualización: " + dateFormat.format(new java.util.Date()));
        lastUpdateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lastUpdateLabel.setForeground(Color.WHITE);

        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftPanel.setOpaque(false);
        leftPanel.add(titleLabel);
        leftPanel.add(lastUpdateLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createQuickStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(new EmptyBorder(20, 20, 0, 20));

        // Transportistas Activos
        transportistasLabel = new JLabel("0/0");
        JPanel transportistasCard = createStatCard("Transportistas Activos", transportistasLabel,
                new Color(63, 81, 181));

        // Paquetes Pendientes
        pendientesLabel = new JLabel("0");
        JPanel pendientesCard = createStatCard("Paquetes Pendientes", pendientesLabel, new Color(255, 152, 0));

        // Paquetes Entregados
        entregadosLabel = new JLabel("0");
        JPanel entregadosCard = createStatCard("Paquetes Entregados", entregadosLabel, new Color(76, 175, 80));

        // Paquetes en Tránsito
        transitoLabel = new JLabel("0");
        JPanel transitoCard = createStatCard("Paquetes en Tránsito", transitoLabel, new Color(33, 150, 243));

        statsPanel.add(transportistasCard);
        statsPanel.add(pendientesCard);
        statsPanel.add(entregadosCard);
        statsPanel.add(transitoCard);

        return statsPanel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(CARD_BORDER, 1),
                new EmptyBorder(12, 15, 12, 15)));

        // Línea superior de color
        JPanel colorLine = new JPanel();
        colorLine.setPreferredSize(new Dimension(0, 3));
        colorLine.setBackground(accentColor);
        card.add(colorLine, BorderLayout.NORTH);

        // Panel de contenido
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);

        contentPanel.add(titleLabel);
        contentPanel.add(valueLabel);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createMetricsPanel() {
        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        metricsPanel.setBackground(BACKGROUND_COLOR);
        metricsPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        pendientesCounter = createCounterLabel("0");
        transitoCounter = createCounterLabel("0");
        entregadosCounter = createCounterLabel("0");

        pendientesSubtitle = createSubtitleLabel("paquetes pendientes");
        transitoSubtitle = createSubtitleLabel("paquetes en tránsito");
        entregadosSubtitle = createSubtitleLabel("entregas realizadas hoy");

        metricsPanel.add(
                createStatusCard("Pedidos Pendientes", pendientesCounter, pendientesSubtitle, new Color(255, 152, 0)));
        metricsPanel.add(createStatusCard("En Tránsito", transitoCounter, transitoSubtitle, new Color(33, 150, 243)));
        metricsPanel
                .add(createStatusCard("Entregados Hoy", entregadosCounter, entregadosSubtitle, new Color(76, 175, 80)));

        return metricsPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(0, 0, 10, 0)));

        JLabel tableTitle = new JLabel("Pedidos Recientes");
        tableTitle.setFont(CARD_TITLE_FONT);
        titlePanel.add(tableTitle, BorderLayout.WEST);
        tablePanel.add(titlePanel, BorderLayout.NORTH);

        String[] columnNames = { "N° Seguimiento", "Destinatario", "Dirección", "Estado", "Fecha Estimada" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(CARD_BORDER);
        table.setShowVerticalLines(true);
        table.setIntercellSpacing(new Dimension(10, 0));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JLabel createCounterLabel(String initialValue) {
        JLabel label = new JLabel(initialValue, SwingConstants.CENTER);
        label.setFont(COUNTER_FONT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(Color.GRAY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createStatusCard(String title, JLabel counter, JLabel subtitle, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(CARD_BORDER, 1),
                new EmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 15, 0);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentColor));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(CARD_TITLE_FONT);
        titlePanel.add(titleLabel);

        gbc.weighty = 0;
        card.add(titlePanel, gbc);

        gbc.insets = new Insets(15, 0, 5, 0);
        gbc.weighty = 1;
        counter.setForeground(accentColor);
        card.add(counter, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weighty = 0;
        card.add(subtitle, gbc);

        return card;
    }

    @Override
    public void refreshData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            // Actualizar contadores de transportistas
            int[] transportistas = controlador.obtenerContadorTransportistas();
            transportistasLabel.setText(transportistas[0] + "/" + transportistas[1]);

            // Obtener todos los paquetes actuales
            List<Paquete> paquetesActuales = controlador.obtenerPaquetesActuales();

            // Obtener paquetes actualizados hoy
            List<Paquete> paquetesHoy = controlador.obtenerPaquetes();

            // Contar paquetes actuales por estado
            int[] contadoresActuales = controlador.contadorPaquetes(paquetesActuales);
            int[] contadoresHoy = controlador.contadorPaquetes(paquetesHoy);

            // Actualizar etiquetas de resumen
            pendientesLabel.setText(String.valueOf(contadoresActuales[0]));
            transitoLabel.setText(String.valueOf(contadoresActuales[1]));
            entregadosLabel.setText(String.valueOf(contadoresHoy[2])); // Solo los entregados hoy

            // Actualizar contadores principales con animación
            // actualizarContadorConAnimacion(pendientesCounter, contadoresActuales[0]);
            // actualizarContadorConAnimacion(transitoCounter, contadoresActuales[1]);
            // actualizarContadorConAnimacion(entregadosCounter, contadoresHoy[2]);

            // Actualizar subtítulos
            // pendientesSubtitle.setText("paquetes pendientes");
            // transitoSubtitle.setText("paquetes en tránsito");
            // entregadosSubtitle.setText("entregas realizadas hoy");

            // Actualizar tabla
            tableModel.setRowCount(0);
            for (Paquete paquete : paquetesActuales) {
                tableModel.addRow(new Object[] {
                        paquete.getNumSeguimiento(),
                        paquete.getDestinatario(),
                        paquete.getDireccionDestino(),
                        controlador.formatearEstado(paquete.getEstado()),
                        dateFormat.format(paquete.getFechaEstimada())
                });
            }

            // Actualizar timestamp
            lastUpdateLabel.setText("Última actualización: " +
                    dateFormat.format(new java.util.Date()));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar los datos: " + e.getMessage(),
                    "Error de Actualización",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    // private void actualizarContadorConAnimacion(JLabel counter, int nuevoValor) {
    // Timer timer = new Timer(50, null);
    // final int[] valorActual = {
    // Integer.parseInt(counter.getText().replaceAll(",", "")) };
    // final int incremento = (nuevoValor - valorActual[0]) / 10;

    // timer.addActionListener(e -> {
    // if (valorActual[0] != nuevoValor) {
    // valorActual[0] += incremento;
    // if (Math.abs(nuevoValor - valorActual[0]) < Math.abs(incremento)) {
    // valorActual[0] = nuevoValor;
    // timer.stop();
    // }
    // counter.setText(numberFormat.format(valorActual[0]));
    // } else {
    // timer.stop();
    // }
    // });

    // timer.start();
    // }

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