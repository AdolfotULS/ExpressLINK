package expresslink.view.sucursal;

//Chibi

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

    // Panel donde se muestran los pedidos
    private JPanel listaPedidosPanel;
    // Panel para mostrar las acciones que se pueden hacer
    private JPanel accionesPedidosPanel;

    // Constructor de la vista
    public GestionPedidosView() {
        // Configura como se muestra el panel
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Color de fondo muy clarito

        // Titulo del panel
        JLabel titleLabel = new JLabel("Gestión de Pedidos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(9, 12, 155)); // Color azul para el titulo
        titleLabel.setPreferredSize(new Dimension(280, 50));
        add(titleLabel, BorderLayout.NORTH); // Agrega el titulo arriba

        // Columnas y datos de ejemplo para la tabla
        String[] columnNames = {"ID Pedido", "Cliente", "Estado", "Transportista", "Fecha"};
        Object[][] data = {
            {"001", "Cliente A", "Pendiente", "N/A", "2024-11-01"},
            {"002", "Cliente B", "En Proceso", "Transportista X", "2024-11-02"},
            {"003", "Cliente C", "Completado", "Transportista Y", "2024-10-30"},
            {"004", "Cliente D", "Pendiente", "N/A", "2024-11-03"}
        };
        // Modelo para la tabla
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            // Hace que las celdas no se puedan editar
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Creacion de la tabla
        pedidosTable = new JTable(tableModel);
        pedidosTable.setFont(new Font("Arial", Font.PLAIN, 14));
        pedidosTable.setRowHeight(30);
        pedidosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        pedidosTable.getTableHeader().setBackground(new Color(48, 102, 190)); // Color azul para la cabecera
        pedidosTable.getTableHeader().setForeground(Color.WHITE);
        pedidosTable.setGridColor(new Color(210, 225, 250)); // Color para las lineas de la grilla
        pedidosTable.setShowGrid(true);
        pedidosTable.setIntercellSpacing(new Dimension(5, 5));
        JScrollPane tableScrollPane = new JScrollPane(pedidosTable);

        // Panel para la lista de pedidos
        listaPedidosPanel = new JPanel(new BorderLayout());
        listaPedidosPanel.setBackground(new Color(245, 247, 250)); // Color clarito
        listaPedidosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listaPedidosPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Filtro para elegir estado de pedidos
        filtroEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "En Proceso", "Completado"});
        filtroEstado.setBackground(new Color(210, 225, 250)); // Color azul claro
        filtroEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        filtroEstado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ComboBox para cambiar el estado de un pedido
        comboBoxEstado = new JComboBox<>(new String[]{"Pendiente", "En Proceso", "Completado"});
        comboBoxEstado.setBackground(new Color(210, 225, 250)); // Azul claro
        comboBoxEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBoxEstado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cambiarEstadoBtn = createStyledButton("Cambiar Estado", new Color(48, 102, 190)); // Boton azul
        JPanel cambiarEstadoPanel = new JPanel(new BorderLayout());
        cambiarEstadoPanel.setBackground(new Color(245, 247, 250)); // Fondo clarito
        cambiarEstadoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cambiarEstadoPanel.add(comboBoxEstado, BorderLayout.NORTH);
        cambiarEstadoPanel.add(Box.createVerticalStrut(5)); // Espacio
        cambiarEstadoPanel.add(cambiarEstadoBtn, BorderLayout.CENTER);

        // Creacion de los botones
        asignarTransportistaBtn = createStyledButton("Asignar Transportista", new Color(48, 102, 190)); // Boton azul
        verDetallesBtn = createStyledButton("Ver Disponibles", new Color(48, 102, 190)); // Boton azul
        borrarSeleccionBtn = createStyledButton("Borrar Selección", new Color(60, 55, 68)); // Boton gris

        // Panel para poner acciones
        accionesPedidosPanel = new JPanel();
        accionesPedidosPanel.setLayout(new BoxLayout(accionesPedidosPanel, BoxLayout.Y_AXIS));
        accionesPedidosPanel.setBackground(new Color(245, 247, 250)); // Fondo clarito
        accionesPedidosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ordenando los componentes en el panel de acciones
        accionesPedidosPanel.add(createLabeledComponent("Filtrar Estado", filtroEstado));
        accionesPedidosPanel.add(Box.createVerticalStrut(10)); // Espacio
        accionesPedidosPanel.add(createLabeledComponent("Cambiar Estado", cambiarEstadoPanel));
        accionesPedidosPanel.add(Box.createVerticalStrut(10)); // Espacio
        accionesPedidosPanel.add(createLabeledComponent("Asignar Transportista", asignarTransportistaBtn));
        accionesPedidosPanel.add(Box.createVerticalStrut(10)); // Espacio
        accionesPedidosPanel.add(createLabeledComponent("Ver Disponibles", verDetallesBtn));
        accionesPedidosPanel.add(Box.createVerticalStrut(10)); // Espacio
        accionesPedidosPanel.add(createLabeledComponent("Borrar Selección", borrarSeleccionBtn));

        // Añadir los paneles principales
        add(listaPedidosPanel, BorderLayout.CENTER);
        add(accionesPedidosPanel, BorderLayout.EAST);
    }

    // Metodo para crear botones estilizados
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Espacio interno
        button.setFocusPainted(false);
        return button;
    }

    // Metodo para crear un panel con un componente y etiqueta
    private JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250)); // Fondo clarito
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(9, 12, 155)); // Azul oscuro
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // Espacio abajo
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(48, 102, 190), 1, true), // Borde azul
            BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Espacio alrededor
        return panel;
    }
}
