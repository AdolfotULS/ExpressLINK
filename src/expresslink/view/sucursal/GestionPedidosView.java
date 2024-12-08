package expresslink.view.sucursal;

//Chibi

// Importaciones necesarias
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionPedidosView extends JPanel {
    // Tabla para mostrar pedidos
    private JTable pedidosTable;
    // ComboBox para elegir filtro de estado
    private JComboBox<String> filtroEstado;
    // Botones para acciones sobre los pedidos
    private JButton asignarTransportistaBtn;
    private JButton verDetallesBtn;
    private JButton borrarSeleccionBtn;
    private JButton cambiarEstadoBtn;
    // Otro ComboBox para cambiar estado de pedidos
    private JComboBox<String> comboBoxEstado;

    // Constructor de la vista
    public GestionPedidosView() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Color de fondo muy clarito

        JLabel titleLabel = new JLabel("Gestión de Pedidos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(9, 12, 155)); // Color azul para el titulo
        titleLabel.setPreferredSize(new Dimension(280, 50));
        add(titleLabel, BorderLayout.NORTH); // Agrega el titulo arriba

        // Columnas y datos de ejemplo para la tabla
        String[] columnNames = { "ID Pedido", "Cliente", "Estado", "Transportista", "Fecha" };
        Object[][] data = {
                { "001", "Cliente A", "Pendiente", "N/A", "2024-11-01" },
                { "002", "Cliente B", "En Proceso", "Transportista X", "2024-11-02" },
                { "003", "Cliente C", "Completado", "Transportista Y", "2024-10-30" },
                { "004", "Cliente D", "Pendiente", "N/A", "2024-11-03" }
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pedidosTable = new JTable(tableModel);
        pedidosTable.setFont(new Font("Arial", Font.PLAIN, 14));
        pedidosTable.setRowHeight(30);
        pedidosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        pedidosTable.getTableHeader().setBackground(new Color(48, 102, 190)); // Color azul para la cabecera
        pedidosTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane tableScrollPane = new JScrollPane(pedidosTable);

        JPanel listaPedidosPanel = new JPanel(new BorderLayout());
        listaPedidosPanel.setBackground(new Color(245, 247, 250)); // Color clarito
        listaPedidosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listaPedidosPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Filtro para elegir estado de pedidos
        filtroEstado = new JComboBox<>(new String[] { "Todos", "Pendiente", "En Proceso", "Completado" });
        filtroEstado.setBackground(new Color(210, 225, 250)); // Color azul claro
        filtroEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        filtroEstado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        filtroEstado.addActionListener(e -> filtrarPedidosPorEstado());

        // ComboBox para cambiar el estado de un pedido
        comboBoxEstado = new JComboBox<>(new String[] { "Pendiente", "En Proceso", "Completado" });
        comboBoxEstado.setBackground(new Color(210, 225, 250)); // Azul claro
        comboBoxEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBoxEstado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cambiarEstadoBtn = createStyledButton("Cambiar Estado", new Color(48, 102, 190));
        cambiarEstadoBtn.addActionListener(e -> cambiarEstadoPedido());

        JPanel cambiarEstadoPanel = new JPanel(new BorderLayout());
        cambiarEstadoPanel.setBackground(new Color(245, 247, 250)); // Fondo clarito
        cambiarEstadoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cambiarEstadoPanel.add(comboBoxEstado, BorderLayout.NORTH);
        cambiarEstadoPanel.add(cambiarEstadoBtn, BorderLayout.CENTER);

        // Creacion de los botones
        asignarTransportistaBtn = createStyledButton("Asignar Transportista", new Color(48, 102, 190));
        verDetallesBtn = createStyledButton("Ver Disponibles", new Color(48, 102, 190));
        borrarSeleccionBtn = createStyledButton("Borrar Selección", new Color(60, 55, 68));

        // Panel para poner acciones
        JPanel accionesPedidosPanel = new JPanel();
        accionesPedidosPanel.setLayout(new BoxLayout(accionesPedidosPanel, BoxLayout.Y_AXIS));
        accionesPedidosPanel.setBackground(new Color(245, 247, 250));
        accionesPedidosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ordenando los componentes en el panel de acciones
        accionesPedidosPanel.add(createLabeledComponent("Filtrar Estado", filtroEstado));
        accionesPedidosPanel.add(createLabeledComponent("Cambiar Estado", cambiarEstadoPanel));
        accionesPedidosPanel.add(createLabeledComponent("Asignar Transportista", asignarTransportistaBtn));
        accionesPedidosPanel.add(createLabeledComponent("Ver Disponibles", verDetallesBtn));
        accionesPedidosPanel.add(createLabeledComponent("Borrar Selección", borrarSeleccionBtn));

        add(listaPedidosPanel, BorderLayout.CENTER);
        add(accionesPedidosPanel, BorderLayout.EAST);
    }

    private void cambiarEstadoPedido() {
        int selectedRow = pedidosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un pedido para cambiar el estado.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nuevoEstado = (String) comboBoxEstado.getSelectedItem();
        pedidosTable.setValueAt(nuevoEstado, selectedRow, 2);
        JOptionPane.showMessageDialog(this, "El estado del pedido ha sido cambiado a: " + nuevoEstado);
    }

    private void filtrarPedidosPorEstado() {
        String estadoSeleccionado = (String) filtroEstado.getSelectedItem();
        DefaultTableModel tableModel = (DefaultTableModel) pedidosTable.getModel();
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
        Object[][] todosLosDatos = {
                { "001", "Cliente A", "Pendiente", "N/A", "2024-11-01" },
                { "002", "Cliente B", "En Proceso", "Transportista X", "2024-11-02" },
                { "003", "Cliente C", "Completado", "Transportista Y", "2024-10-30" },
                { "004", "Cliente D", "Pendiente", "N/A", "2024-11-03" }
        };
        for (Object[] fila : todosLosDatos) {
            if ("Todos".equals(estadoSeleccionado) || fila[2].equals(estadoSeleccionado)) {
                tableModel.addRow(fila);
            }
        }
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(9, 12, 155));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        // Crear el marco principal
        JFrame frame = new JFrame("Gestión de Pedidos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Agregar el panel GestionPedidosView al marco
        GestionPedidosView gestionPedidosView = new GestionPedidosView();
        frame.add(gestionPedidosView);

        // Configurar el tamaño del marco
        frame.setSize(800, 600); // Ancho x Alto

        // Hacer visible el marco
        frame.setVisible(true);
    }
}
