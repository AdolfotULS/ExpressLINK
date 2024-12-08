package expresslink.view.sucursal;

import expresslink.controllers.sucursal.EntregasDiaController;
import expresslink.controllers.sucursal.EntregasDiaController.EntregasDia;
import expresslink.model.Sucursal;
import expresslink.view.components.RefreshablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.List;

public class EntregasDiaView extends JPanel implements RefreshablePanel {
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");

    private final EntregasDiaController controlador;
    private final Timer timer;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JLabel statusLabel;
    private final JLabel contadorLabel;
    private final Sucursal sucursal;

    public EntregasDiaView(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }

        this.sucursal = sucursal;
        this.controlador = new EntregasDiaController(this.sucursal);

        // Configurar el timer para actualizar cada 30 segundos
        this.timer = new Timer(30000, e -> refreshData());
        timer.start();

        // Configurar el modelo de tabla
        String[] columnas = {
                "ID", "N° Seguimiento", "Cliente", "Destinatario", "Dirección",
                "Dimensiones", "Transportista", "Teléfono", "Costo",
                "Intentos", "Hora Entrega"
        };

        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 9)
                    return Integer.class;
                if (columnIndex == 8)
                    return Double.class;
                return String.class;
            }
        };

        this.tabla = new JTable(modeloTabla);
        this.statusLabel = new JLabel("Última actualización: " + dateFormat.format(new java.util.Date()));
        this.contadorLabel = new JLabel("Entregas del día: 0");

        inicializarGUI();
        refreshData();
    }

    private void inicializarGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de encabezado
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel izquierdo del header
        JPanel leftHeaderPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftHeaderPanel.setBackground(PRIMARY_COLOR);

        JLabel titleLabel = new JLabel("Entregas del Día");
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

        // Configurar tabla
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ajustarColumnas(tabla);

        // Configurar renderizadores personalizados
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Aplicar renderizadores
        tabla.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) {
                    value = moneyFormat.format((Double) value);
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        tabla.getColumnModel().getColumn(9).setCellRenderer(centerRenderer); // Intentos
        tabla.getColumnModel().getColumn(10).setCellRenderer(centerRenderer); // Hora

        // Configurar scroll pane
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Panel contenedor para la tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
    }

    private void ajustarColumnas(JTable tabla) {
        int maxFilasParaMedir = 50; // Limitar la cantidad de filas para medir el contenido
        int margenAdicional = 15; // Margen adicional para evitar recortes

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            int anchoMaximo = 75; // Ancho mínimo inicial

            // Calcular ancho del contenido de las filas (limitado a maxFilasParaMedir)
            for (int j = 0; j < Math.min(tabla.getRowCount(), maxFilasParaMedir); j++) {
                TableCellRenderer renderer = tabla.getCellRenderer(j, i);
                Component comp = tabla.prepareRenderer(renderer, j, i);
                anchoMaximo = Math.max(comp.getPreferredSize().width + margenAdicional, anchoMaximo);
            }

            // Calcular ancho del encabezado
            TableCellRenderer headerRenderer = tabla.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(tabla,
                    tabla.getColumnModel().getColumn(i).getHeaderValue(), false, false, 0, i);
            anchoMaximo = Math.max(headerComp.getPreferredSize().width + margenAdicional, anchoMaximo);

            // Asignar ancho ajustado a la columna (opcionalmente limitar a un máximo)
            int anchoMaximoPermitido = 300; // Ancho máximo opcional
            tabla.getColumnModel().getColumn(i).setPreferredWidth(Math.min(anchoMaximo, anchoMaximoPermitido));
        }
    }

    @Override
    public void refreshData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        modeloTabla.setRowCount(0); // Limpiar la tabla

        try {
            List<EntregasDia> entregas = controlador.obtenerPaquetesEntregadosHoy();

            for (EntregasDia entrega : entregas) {
                modeloTabla.addRow(new Object[] {
                        entrega.getPaqueteId(),
                        entrega.getNumSeguimiento(),
                        entrega.getClienteNombre(),
                        entrega.getDestinatario(),
                        entrega.getDireccionDestino(),
                        entrega.getDimensionesPeso(),
                        entrega.getTransportistaNombre(),
                        entrega.getTransportistaTelefono(),
                        entrega.getCosto(),
                        entrega.getIntentosEntrega(),
                        dateFormat.format(entrega.getFechaEntrega())
                });
            }

            contadorLabel.setText("Entregas del día: " + entregas.size());
            statusLabel.setText("Última actualización: " +
                    dateFormat.format(new java.util.Date()));

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