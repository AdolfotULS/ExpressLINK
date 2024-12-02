package expresslink.view.cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import expresslink.model.Usuario;
import expresslink.controllers.cliente.HistorialController;

public class HistorialPanel extends JPanel {
    private static final Color COLOR_PRIMARIO = new Color(33, 150, 243);
    private static final Color COLOR_FONDO = new Color(240, 242, 245);
    private static final Color COLOR_BLANCO = Color.WHITE;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private HistorialController controlador;
    private Usuario usuario;

    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();

    public HistorialPanel(Usuario usuario) {
        this.usuario = usuario;
        this.controlador = new HistorialController(usuario);
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        inicializarComponentes();
        cargarDatos();
    }

    private void inicializarComponentes() {
        // Crear modelo de tabla
        String[] columnas = { "N° Seguimiento", "Destinatario", "Dirección", "Fecha Entrega", "Costo" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        // Crear y configurar la tabla
        tabla = new JTable(modeloTabla);
        tabla.setBackground(COLOR_BLANCO);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
        tabla.getTableHeader().setForeground(COLOR_BLANCO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100); // N° Seguimiento
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150); // Destinatario
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200); // Dirección
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha
        tabla.getColumnModel().getColumn(4).setPreferredWidth(80); // Costo

        // Panel superior con título
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(COLOR_BLANCO);
        JLabel titulo = new JLabel("Historial de Envíos");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelSuperior.add(titulo);

        // Agregar componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel inferior para información adicional
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.setBackground(COLOR_BLANCO);
        JLabel totalEnvios = new JLabel("Total de envíos: 0");
        panelInferior.add(totalEnvios);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);

        try {
            List<HistorialItem> historial = controlador.obtenerHistorial();

            for (HistorialItem item : historial) {
                modeloTabla.addRow(new Object[] {
                        item.getNumeroSeguimiento(),
                        item.getDestinatario(),
                        item.getDireccion(),
                        formatoFecha.format(item.getFechaEstimada()),
                        formatoMoneda.format(item.getCosto())
                });
            }

            // Actualizar contador de envíos
            ((JLabel) ((JPanel) getComponent(2)).getComponent(0))
                    .setText("Total de envíos: " + historial.size());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar el historial: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Clase interna para manejar los datos de cada fila
    public static class HistorialItem {
        private String numeroSeguimiento;
        private String destinatario;
        private String direccion;
        private Date fechaEstimada;
        private double costo;

        // Constructor
        public HistorialItem(String numeroSeguimiento, String destinatario,
                String direccion, Date fechaEstimada, double costo) {
            this.numeroSeguimiento = numeroSeguimiento;
            this.destinatario = destinatario;
            this.direccion = direccion;
            this.fechaEstimada = fechaEstimada;
            this.costo = costo;
        }

        // Getters
        public String getNumeroSeguimiento() {
            return numeroSeguimiento;
        }

        public String getDestinatario() {
            return destinatario;
        }

        public String getDireccion() {
            return direccion;
        }

        public Date getFechaEstimada() {
            return fechaEstimada;
        }

        public double getCosto() {
            return costo;
        }
    }
}