package expresslink.view.empresa;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.border.EmptyBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import expresslink.controllers.empresa.EmpresaDashboardController;
import expresslink.model.Usuario;
import expresslink.view.login.LoginView;
import expresslink.view.components.RefreshablePanel;

public class EmpresaDashboard extends JFrame implements RefreshablePanel {
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final DecimalFormat moneyFormat = new DecimalFormat("$#,##0");

    private EmpresaDashboardController controller;
    private final Timer refreshTimer;

    // UI Components
    private JLabel ingresosTotalesLabel;
    private JLabel transportistasLabel;
    private JLabel clientesLabel;
    private JLabel pedidosTotalesLabel;
    private JLabel pedidosTransitoLabel;
    private JLabel lastUpdateLabel;

    public EmpresaDashboard(Usuario usuario, LoginView loginView) {
        this.controller = new EmpresaDashboardController();

        // Configurar timer para actualizar cada 30 segundos
        this.refreshTimer = new Timer(30000, e -> refreshData());
        refreshTimer.start();

        initializeGUI();
        refreshData();
    }

    private void initializeGUI() {
        setTitle("Express Link - Dashboard Empresarial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header with gradient
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Metrics Cards Panel
        JPanel metricsPanel = createMetricsPanel();
        mainPanel.add(metricsPanel, BorderLayout.CENTER);

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

        JLabel titleLabel = new JLabel("Dashboard Empresarial");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        lastUpdateLabel = new JLabel("Última actualización: ");
        lastUpdateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lastUpdateLabel.setForeground(Color.WHITE);

        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftPanel.setOpaque(false);
        leftPanel.add(titleLabel);
        leftPanel.add(lastUpdateLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createMetricsPanel() {
        JPanel metricsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        metricsPanel.setBackground(BACKGROUND_COLOR);

        // Ingresos Totales Card
        ingresosTotalesLabel = new JLabel("$0");
        JPanel ingresosCard = createMetricCard("Ingresos Totales", ingresosTotalesLabel, new Color(76, 175, 80));

        // Transportistas Card
        transportistasLabel = new JLabel("0");
        JPanel transportistasCard = createMetricCard("Transportistas Activos", transportistasLabel,
                new Color(33, 150, 243));

        // Clientes Card
        clientesLabel = new JLabel("0");
        JPanel clientesCard = createMetricCard("Total Clientes", clientesLabel, new Color(156, 39, 176));

        // Pedidos Totales Card
        pedidosTotalesLabel = new JLabel("0");
        JPanel pedidosCard = createMetricCard("Pedidos Totales", pedidosTotalesLabel, new Color(255, 152, 0));

        // Pedidos en Tránsito Card
        pedidosTransitoLabel = new JLabel("0");
        JPanel transitoCard = createMetricCard("Pedidos en Tránsito", pedidosTransitoLabel, new Color(244, 67, 54));

        metricsPanel.add(ingresosCard);
        metricsPanel.add(transportistasCard);
        metricsPanel.add(clientesCard);
        metricsPanel.add(pedidosCard);
        metricsPanel.add(transitoCard);

        return metricsPanel;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15)));

        // Título
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);

        // Valor
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);

        // Layout
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(titleLabel);
        contentPanel.add(valueLabel);

        // Línea de acento superior
        JPanel accentLine = new JPanel();
        accentLine.setPreferredSize(new Dimension(0, 3));
        accentLine.setBackground(accentColor);

        card.add(accentLine, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    @Override
    public void refreshData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            // Obtener métricas del controlador
            double ingresosTotales = controller.obtenerIngresosTotales();
            int totalTransportistas = controller.obtenerTotalTransportistas();
            int totalClientes = controller.obtenerTotalClientes();
            int totalPedidos = controller.obtenerTotalPedidos();
            int pedidosTransito = controller.obtenerPedidosEnTransito();

            // Actualizar UI
            ingresosTotalesLabel.setText(moneyFormat.format(ingresosTotales));
            transportistasLabel.setText(String.valueOf(totalTransportistas));
            clientesLabel.setText(String.valueOf(totalClientes));
            pedidosTotalesLabel.setText(String.valueOf(totalPedidos));
            pedidosTransitoLabel.setText(String.valueOf(pedidosTransito));

            // Actualizar timestamp
            lastUpdateLabel.setText("Última actualización: " +
                    new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override
    public void stopRefresh() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    public void dispose() {
        stopRefresh();
        super.dispose();
    }
}