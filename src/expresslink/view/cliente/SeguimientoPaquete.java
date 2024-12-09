package expresslink.view.cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import expresslink.controllers.cliente.SeguimientoController;
import expresslink.controllers.cliente.SeguimientoController.DatosSeguimiento;
import expresslink.controllers.cliente.SeguimientoController.LogPaqueteData;
import expresslink.view.components.RefreshablePanel;
import expresslink.model.enums.EstadoPaquete;

public class SeguimientoPaquete extends JPanel implements RefreshablePanel {
    private static final Color COLOR_PRIMARIO = new Color(33, 150, 243);
    private static final Color COLOR_FONDO = new Color(240, 242, 245);
    private static final Color COLOR_EXITO = new Color(76, 175, 80);
    private static final Color COLOR_PENDIENTE = new Color(255, 152, 0);
    private static final Color COLOR_BLANCO = Color.WHITE;

    private SeguimientoController controlador;
    private JTextField txtNumeroSeguimiento;
    private JButton btnBuscar;
    private JPanel panelEstados;
    private JPanel panelInfoPaquete;
    private JPanel panelHistorialContainer;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private Timer refreshTimer;
    private String currentTracking;
    private SimpleDateFormat formatoFecha;

    public SeguimientoPaquete() {
        this.controlador = new SeguimientoController(this);
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        inicializarGUI();
        inicializarTimer();
        resetearPanel();
    }

    private void inicializarGUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setBackground(COLOR_FONDO);

        txtNumeroSeguimiento = new JTextField();
        txtNumeroSeguimiento.setPreferredSize(new Dimension(0, 35));
        txtNumeroSeguimiento.putClientProperty("JTextField.placeholderText", "Ingrese número de seguimiento");

        btnBuscar = new JButton("BUSCAR");
        btnBuscar.setPreferredSize(new Dimension(120, 35));
        btnBuscar.setBackground(new Color(139, 195, 74));
        btnBuscar.setForeground(COLOR_BLANCO);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.addActionListener(e -> buscarPaquete());

        panelBusqueda.add(txtNumeroSeguimiento, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        // Panel de contenido central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(COLOR_FONDO);

        // Panel de estados
        panelEstados = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(100, 50, getWidth() - 100, 50);

                g2d.dispose();
            }
        };
        panelEstados.setPreferredSize(new Dimension(0, 100));
        panelEstados.setBackground(COLOR_FONDO);
        panelEstados.setLayout(null);

        // Listener para redibujar esferas dinámicamente
        panelEstados.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (currentTracking != null) {
                    try {
                        actualizarEstados(controlador.buscarPedido(currentTracking));
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });

        // Panel de información del paquete
        panelInfoPaquete = new JPanel();
        panelInfoPaquete.setLayout(new GridLayout(0, 2, 10, 5));
        panelInfoPaquete.setBackground(COLOR_BLANCO);
        panelInfoPaquete.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 1)));

        // Panel de historial
        panelHistorialContainer = new JPanel(new BorderLayout(0, 10));
        panelHistorialContainer.setBackground(COLOR_FONDO);

        JLabel labelHistorial = new JLabel("Historial de Estados");
        labelHistorial.setFont(new Font("Arial", Font.BOLD, 14));
        labelHistorial.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        String[] columnas = { "Fecha", "Estado Anterior", "Nuevo Estado", "Descripción" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFillsViewportHeight(true);
        tablaHistorial.setBackground(COLOR_BLANCO);
        tablaHistorial.getTableHeader().setBackground(COLOR_PRIMARIO);
        tablaHistorial.getTableHeader().setForeground(COLOR_BLANCO);

        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setBackground(COLOR_BLANCO);

        panelHistorialContainer.add(labelHistorial, BorderLayout.NORTH);
        panelHistorialContainer.add(scrollPane, BorderLayout.CENTER);

        // Agregar componentes al panel central
        panelCentral.add(panelEstados); // Panel de estados arriba
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20))); // Mayor separación
        panelCentral.add(panelInfoPaquete); // Panel de información
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20))); // Mayor separación
        panelCentral.add(panelHistorialContainer); // Historial al final

        // Agregar todo al panel principal
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    private void resetearPanel() {
        txtNumeroSeguimiento.setText("");
        currentTracking = null;
        modeloTabla.setRowCount(0);
        panelEstados.setVisible(false);
        panelInfoPaquete.setVisible(false);
        panelHistorialContainer.setVisible(false);
    }

    private void mostrarInformacionPaquete(DatosSeguimiento datos) {
        panelInfoPaquete.removeAll();

        // Agregar información del paquete
        agregarCampoInfo("Estado:", datos.estado.toString());
        agregarCampoInfo("Sucursal Origen:", datos.sucursalOrigen);
        agregarCampoInfo("Destino:", datos.lugarDestino);
        agregarCampoInfo("Fecha Estimada:", formatoFecha.format(datos.fechaEstimada));

        panelInfoPaquete.revalidate();
        panelInfoPaquete.repaint();
    }

    private void agregarCampoInfo(String etiqueta, String valor) {
        JLabel lblEtiqueta = new JLabel(etiqueta);
        JLabel lblValor = new JLabel(valor);
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 12));
        lblValor.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInfoPaquete.add(lblEtiqueta);
        panelInfoPaquete.add(lblValor);
    }

    private void inicializarTimer() {
        refreshTimer = new Timer(30000, e -> {
            if (currentTracking != null && !currentTracking.isEmpty()) {
                buscarPaquete();
            }
        });
        refreshTimer.start();
    }

    private void buscarPaquete() {
        String numeroSeguimiento = txtNumeroSeguimiento.getText().trim();
        if (numeroSeguimiento.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un número de seguimiento",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DatosSeguimiento datos = controlador.buscarPedido(numeroSeguimiento);
            if (datos != null) {
                currentTracking = numeroSeguimiento;
                panelInfoPaquete.setVisible(true);
                panelEstados.setVisible(true);
                panelHistorialContainer.setVisible(true);

                mostrarInformacionPaquete(datos);
                actualizarEstados(datos);
                actualizarHistorial(numeroSeguimiento);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el paquete con el número de seguimiento proporcionado",
                        "No encontrado",
                        JOptionPane.INFORMATION_MESSAGE);
                resetearPanel();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar el paquete: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            resetearPanel();
        }
    }

    private void actualizarEstados(DatosSeguimiento datos) {
        panelEstados.removeAll();

        // Asegúrate de que las posiciones se calculen dinámicamente
        int anchoDisponible = panelEstados.getWidth() - 100; // Resto de los márgenes laterales
        int separacion = anchoDisponible / 2;

        crearCirculoEstado("EN SUCURSAL", 50, datos != null && datos.posicionEstado >= 0);
        crearCirculoEstado("EN TRÁNSITO", 50 + separacion, datos != null && datos.posicionEstado >= 1);
        crearCirculoEstado(datos != null && datos.estado == EstadoPaquete.CANCELADO ? "CANCELADO" : "ENTREGADO",
                50 + separacion * 2, datos != null && datos.posicionEstado >= 2);

        panelEstados.revalidate();
        panelEstados.repaint();
    }

    private void crearCirculoEstado(String texto, int x, boolean activo) {
        JPanel circulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (activo) {
                    g2d.setColor(COLOR_EXITO);
                } else {
                    g2d.setColor(COLOR_PENDIENTE);
                }

                g2d.fillOval(0, 0, 30, 30);
                g2d.dispose();
            }
        };

        circulo.setOpaque(false);
        circulo.setBounds(x - 15, 35, 30, 30);

        JLabel label = new JLabel(texto);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(x - 50, 70, 100, 20);

        panelEstados.add(circulo);
        panelEstados.add(label);
    }

    private void actualizarHistorial(String numeroSeguimiento) {
        modeloTabla.setRowCount(0);

        try {
            var historial = controlador.obtenerHistorialPedido(numeroSeguimiento);
            for (LogPaqueteData log : historial) {
                modeloTabla.addRow(new Object[] {
                        formatoFecha.format(log.fecha),
                        log.estadoAnterior.toString(),
                        log.estadoNuevo.toString(),
                        log.descripcion
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar el historial: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refreshData() {
        if (currentTracking != null && !currentTracking.isEmpty()) {
            buscarPaquete();
        }
    }

    @Override
    public void stopRefresh() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
        resetearPanel();
    }
}