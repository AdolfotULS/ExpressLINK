package expresslink.view.sucursal;

import expresslink.controllers.sucursal.PedidosTransitoController;
import expresslink.controllers.sucursal.PedidosTransitoController.PaqueteTransito;
import expresslink.model.Sucursal;
import expresslink.view.components.RefreshablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PedidosTransitoView extends JPanel implements RefreshablePanel {
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final PedidosTransitoController controlador;
    private final Timer timer;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JLabel statusLabel;
    private final Sucursal sucursal;
    private final JLabel contadorLabel;

    public PedidosTransitoView(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }

        this.sucursal = sucursal;
        this.controlador = new PedidosTransitoController(this.sucursal);

        // Configurar el timer para actualizar cada 30 segundos
        this.timer = new Timer(30000, e -> refreshData());
        timer.start();

        // Configurar el modelo de tabla
        String[] columnas = {
                "ID", "N° Seguimiento", "Destinatario", "Dirección Destino",
                "Transportista", "Teléfono", "Costo", "Estado",
                "Fecha Creación", "Fecha Estimada", "Intentos"
        };

        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Integer.class;
                if (columnIndex == 6)
                    return Double.class;
                if (columnIndex == 10)
                    return Integer.class;
                return String.class;
            }
        };

        this.tabla = new JTable(modeloTabla);
        this.statusLabel = new JLabel("Última actualización: " + dateFormat.format(new java.util.Date()));
        this.contadorLabel = new JLabel("Pedidos en tránsito: 0");

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

        JLabel titleLabel = new JLabel("Pedidos en Tránsito");
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

        // Deshabilitar ajuste automático de columnas al tamaño del contenedor
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Ajustar el tamaño de las columnas basado en su contenido
        ajustarColumnas(tabla);

        // Configurar renderizadores personalizados
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Aplicar renderizadores
        tabla.getColumnModel().getColumn(6).setCellRenderer(rightRenderer); // Costo
        tabla.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Estado
        tabla.getColumnModel().getColumn(10).setCellRenderer(centerRenderer); // Intentos

        // Configurar scroll pane
        JScrollPane scrollPane = new JScrollPane(tabla, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Panel contenedor para la tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
    }

    private void ajustarColumnas(JTable tabla) {
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            int anchoMaximo = 75; // Ancho mínimo inicial

            // Calcular ancho del contenido de las filas
            for (int j = 0; j < tabla.getRowCount(); j++) {
                TableCellRenderer renderer = tabla.getCellRenderer(j, i);
                Component comp = tabla.prepareRenderer(renderer, j, i);
                anchoMaximo = Math.max(comp.getPreferredSize().width + 10, anchoMaximo);
            }

            // Calcular ancho del encabezado
            TableCellRenderer headerRenderer = tabla.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(tabla,
                    tabla.getColumnModel().getColumn(i).getHeaderValue(), false, false, 0, i);
            anchoMaximo = Math.max(headerComp.getPreferredSize().width + 10, anchoMaximo);

            // Asignar ancho ajustado a la columna
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchoMaximo);
        }
    }

    @Override
    public void refreshData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        modeloTabla.setRowCount(0); // Limpiar la tabla

        try {
            List<PaqueteTransito> paquetes = controlador.obtenerPaquetesTransitoSucursal();

            for (PaqueteTransito paquete : paquetes) {
                modeloTabla.addRow(new Object[] {
                        paquete.getId(),
                        paquete.getNumSeguimiento(),
                        paquete.getDestinatario(),
                        paquete.getDireccionDestino(),
                        paquete.getTransportistaNombre() != null ? paquete.getTransportistaNombre() : "No asignado",
                        paquete.getTransportistaTelefono() != null ? paquete.getTransportistaTelefono() : "N/A",
                        "$" + paquete.getCosto(),
                        paquete.getEstado(),
                        paquete.getFechaCreacion() != null ? dateFormat.format(paquete.getFechaCreacion()) : "N/A",
                        paquete.getFechaEstimada() != null ? dateFormat.format(paquete.getFechaEstimada()) : "N/A",
                        paquete.getIntentosEntrega()
                });
            }

            // Actualizar el contador
            contadorLabel.setText("Pedidos en tránsito: " + paquetes.size());

            statusLabel.setText("Última actualización: " +
                    dateFormat.format(new java.util.Date()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar los datos: " + e.getMessage(),
                    "Error de Actualización",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
            ajustarColumnas(tabla);
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