package expresslink.view.cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import expresslink.model.*;
import expresslink.view.components.RefreshablePanel;
import expresslink.controllers.cliente.MisPedidosController;
import expresslink.view.components.RefreshablePanel;

public class MisPedidosPanel extends JPanel implements RefreshablePanel {
    private static final Color COLOR_PRIMARIO = new Color(33, 150, 243);
    private static final Color COLOR_FONDO = new Color(240, 242, 245);
    private static final Color COLOR_BLANCO = Color.WHITE;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private MisPedidosController controlador;
    private Usuario usuario;
    private JPanel panelDetalle;

    private List<PedidoItem> pedidosActuales;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public MisPedidosPanel(Usuario usuario) {
        this.usuario = usuario;
        this.controlador = new MisPedidosController(usuario);
        this.pedidosActuales = new ArrayList<>();
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        inicializarComponentes();
        // Escuchar cuando el panel se hace visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshData();
            }
        });

        cargarDatos(); // Carga inicial
    }

    private void inicializarComponentes() {
        // Panel Superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_BLANCO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Mis Pedidos Activos");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelSuperior.add(titulo, BorderLayout.WEST);

        // Tabla
        String[] columnas = {
                "N° Seguimiento", "Destinatario", "Estado",
                "Fecha Creación", "Fecha Estimada", "Sucursal"
        };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setBackground(COLOR_BLANCO);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
        tabla.getTableHeader().setForeground(COLOR_BLANCO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ajustar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100); // N° Seguimiento
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150); // Destinatario
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100); // Estado
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120); // Fecha Creación
        tabla.getColumnModel().getColumn(4).setPreferredWidth(120); // Fecha Estimada
        tabla.getColumnModel().getColumn(5).setPreferredWidth(150); // Sucursal

        // Panel de Detalle
        panelDetalle = new JPanel();
        panelDetalle.setBackground(COLOR_BLANCO);
        panelDetalle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Detalles del Envío"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelDetalle.setPreferredSize(new Dimension(0, 200));
        panelDetalle.setVisible(false);

        // Listener para selección de tabla
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalles(tabla.getSelectedRow());
            }
        });

        // Agregar componentes
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelDetalle, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        try {
            pedidosActuales = controlador.obtenerMisPedidos();
            actualizarTabla();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los pedidos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (PedidoItem pedido : pedidosActuales) {
            modeloTabla.addRow(new Object[] {
                    pedido.getNumeroSeguimiento(),
                    pedido.getDestinatario(),
                    formatearEstado(pedido.getEstado()),
                    formatoFecha.format(pedido.getFechaCreacion()),
                    formatoFecha.format(pedido.getFechaEstimada()),
                    pedido.getSucursalNombre()
            });
        }
    }

    private void mostrarDetalles(int row) {
        if (row < 0 || row >= pedidosActuales.size()) {
            panelDetalle.setVisible(false);
            return;
        }

        PedidoItem pedido = pedidosActuales.get(row);
        panelDetalle.removeAll();
        panelDetalle.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Primera columna
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;

        JPanel col1 = new JPanel(new GridLayout(4, 2, 5, 5));
        col1.setBackground(COLOR_BLANCO);
        agregarCampoDetalle(col1, "Estado:", formatearEstado(pedido.getEstado()));
        agregarCampoDetalle(col1, "Destinatario:", pedido.getDestinatario());
        agregarCampoDetalle(col1, "Dirección:", pedido.getDireccionDestino());
        agregarCampoDetalle(col1, "Intentos de entrega:", String.valueOf(pedido.getIntentosEntrega()));
        panelDetalle.add(col1, gbc);

        // Segunda columna
        gbc.gridx = 1;
        JPanel col2 = new JPanel(new GridLayout(4, 2, 5, 5));
        col2.setBackground(COLOR_BLANCO);
        agregarCampoDetalle(col2, "Sucursal:", pedido.getSucursalNombre());
        agregarCampoDetalle(col2, "Dir. Sucursal:", pedido.getSucursalDireccion());

        if (pedido.getTransportistaNombre() != null) {
            agregarCampoDetalle(col2, "Transportista:", pedido.getTransportistaNombre());
            agregarCampoDetalle(col2, "Tel. Transportista:", pedido.getTransportistaTelefono());
        } else {
            agregarCampoDetalle(col2, "Transportista:", "No asignado");
            agregarCampoDetalle(col2, "Tel. Transportista:", "-");
        }
        panelDetalle.add(col2, gbc);

        panelDetalle.setVisible(true);
        panelDetalle.revalidate();
        panelDetalle.repaint();
    }

    private void agregarCampoDetalle(JPanel panel, String label, String valor) {
        JLabel lblEtiqueta = new JLabel(label);
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblEtiqueta);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblValor);
    }

    private String formatearEstado(String estado) {
        return estado != null ? estado.replace("_", " ") : "DESCONOCIDO";
    }

    @Override
    public void refreshData() {
        cargarDatos();
    }

    @Override
    public void stopRefresh() {

    }
}