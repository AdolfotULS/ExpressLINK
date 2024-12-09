package expresslink.view.sucursal;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.List;

import expresslink.controllers.sucursal.GestionPaquetesController;
import expresslink.model.*;
import expresslink.model.enums.*;
import expresslink.view.components.RefreshablePanel;

public class GestionPaquetesView extends JPanel implements RefreshablePanel {
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private Sucursal sucursal;
    private Usuario usuario;
    private GestionPaquetesController controlador;
    private final Timer timer;

    // Componentes UI
    private JTable paquetesTable;
    private DefaultTableModel tableModel;
    private JComboBox<EstadoPaquete> estadoComboBox;
    private JComboBox<Transportista> transportistaComboBox;
    private JButton asignarTransportistaBtn;
    private JButton asignarTodosBtn;
    private JButton cambiarEstadoBtn;
    private JButton refreshBtn;
    private JLabel lastUpdateLabel;
    private JTextField searchField;

    public GestionPaquetesView(Usuario usuario, Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }

        this.sucursal = sucursal;
        this.usuario = usuario;
        this.controlador = new GestionPaquetesController(usuario, sucursal);

        // Timer para actualización automática cada 30 segundos
        this.timer = new Timer(30000, e -> refreshData());
        timer.start();

        setLayout(new BorderLayout());
        inicializarGUI();
        refreshData();
    }

    private void inicializarGUI() {
        setBackground(BACKGROUND_COLOR);

        // Panel Header con gradiente
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
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título y timestamp
        JLabel titleLabel = new JLabel("Gestión de Paquetes");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        lastUpdateLabel = new JLabel("Última actualización: " + dateFormat.format(new java.util.Date()));
        lastUpdateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lastUpdateLabel.setForeground(Color.WHITE);

        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftPanel.setOpaque(false);
        leftPanel.add(titleLabel);
        leftPanel.add(lastUpdateLabel);

        // Botón de actualización manual
        refreshBtn = new JButton("Actualizar");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.addActionListener(e -> refreshData());

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Panel principal con tabla y controles
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tabla de paquetes
        String[] columns = {
                "Seguimiento", "Destinatario", "Dirección", "Estado",
                "Transportista", "Fecha Estimada", "Intentos"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        paquetesTable = new JTable(tableModel);
        paquetesTable.setRowHeight(35);
        paquetesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        paquetesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        paquetesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(paquetesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.putClientProperty("JTextField.placeholderText",
                "Buscar por número de seguimiento o destinatario...");
        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Panel de acciones
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(BACKGROUND_COLOR);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // ComboBoxes y botones
        estadoComboBox = new JComboBox<>(EstadoPaquete.values());
        estadoComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // estadoComboBox.setPreferredSize(new Dimension(200, 30)); // Ancho fijo
        transportistaComboBox = new JComboBox<>();
        transportistaComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // transportistaComboBox.setPreferredSize(new Dimension(200, 30)); // Ancho fijo
        // ajustarComboBoxSize(estadoComboBox);
        // ajustarComboBoxSize(transportistaComboBox);

        cambiarEstadoBtn = createActionButton("Cambiar Estado");
        asignarTransportistaBtn = createActionButton("Asignar Transportista");
        asignarTodosBtn = createActionButton("Asignar Todos los Pendientes");

        // Añadir componentes al panel de acciones
        actionsPanel.add(createActionComponent("Estado", estadoComboBox));
        actionsPanel.add(Box.createVerticalStrut(10));
        actionsPanel.add(createActionComponent("", cambiarEstadoBtn));
        actionsPanel.add(Box.createVerticalStrut(20));

        actionsPanel.add(createActionComponent("Transportista", transportistaComboBox));
        actionsPanel.add(Box.createVerticalStrut(10));
        actionsPanel.add(createActionComponent("", asignarTransportistaBtn));
        actionsPanel.add(Box.createVerticalStrut(10));
        actionsPanel.add(createActionComponent("", asignarTodosBtn));

        mainPanel.add(actionsPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);

        // Event Listeners
        cambiarEstadoBtn.addActionListener(e -> cambiarEstado());
        asignarTransportistaBtn.addActionListener(e -> asignarTransportista());
        asignarTodosBtn.addActionListener(e -> asignarTodosLosPendientes());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarTabla();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarTabla();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrarTabla();
            }
        });

        transportistaComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Transportista) {
                    Transportista t = (Transportista) value;
                    String vehiculoInfo = t.getVehiculo() != null ? " - " + t.getVehiculo().getPatente()
                            : " - Sin vehículo";
                    setText(String.format("%s (%s)%s",
                            t.getNombre(),
                            t.getLicencia(),
                            vehiculoInfo));
                }
                return this;
            }
        });

    }

    private void ajustarComboBoxSize(JComboBox<?> comboBox) {
        int anchoMaximo = 0;
        FontMetrics metrics = comboBox.getFontMetrics(comboBox.getFont());

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Object item = comboBox.getItemAt(i);
            if (item != null) {
                anchoMaximo = Math.max(anchoMaximo, metrics.stringWidth(item.toString()));
            }
        }

        // Agregar un margen adicional al ancho calculado
        anchoMaximo += 20; // Espacio adicional para márgenes
        comboBox.setPreferredSize(new Dimension(anchoMaximo, 30)); // 30px de alto
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 35));
        return button;
    }

    private void cambiarEstado() {
        int selectedRow = paquetesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un paquete para cambiar su estado.",
                    "Selección Requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        EstadoPaquete nuevoEstado = (EstadoPaquete) estadoComboBox.getSelectedItem();
        String numSeguimiento = (String) paquetesTable.getValueAt(selectedRow, 0);

        try {
            controlador.cambiarEstadoPaquete(numSeguimiento, nuevoEstado);
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cambiar el estado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asignarTransportista() {
        int selectedRow = paquetesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un paquete para asignar transportista.",
                    "Selección Requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Transportista transportista = (Transportista) transportistaComboBox.getSelectedItem();
        String numSeguimiento = (String) paquetesTable.getValueAt(selectedRow, 0);

        try {
            controlador.asignarTransportista(numSeguimiento, transportista);
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al asignar transportista: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asignarTodosLosPendientes() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea asignar transportistas a todos los paquetes pendientes?",
                "Confirmar Asignación Masiva",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                int asignados = controlador.asignarTransportistasPendientes();
                JOptionPane.showMessageDialog(this,
                        "Se han asignado transportistas a " + asignados + " paquetes pendientes.",
                        "Asignación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al asignar transportistas: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filtrarTabla() {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        paquetesTable.setRowSorter(sorter);

        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    @Override
    public void refreshData() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // Actualizar lista de transportistas
            List<Transportista> transportistas = controlador.obtenerTransportistasDisponibles();
            transportistaComboBox.removeAllItems();
            for (Transportista t : transportistas) {
                transportistaComboBox.addItem(t);
            }

            // Actualizar tabla de paquetes
            List<Paquete> paquetes = controlador.obtenerPaquetes();
            tableModel.setRowCount(0);

            for (Paquete p : paquetes) {
                tableModel.addRow(new Object[] {
                        p.getNumSeguimiento(),
                        p.getDestinatario(),
                        p.getDireccionDestino(),
                        p.getEstado().toString(),
                        p.getTransportista() != null ? p.getTransportista().getNombre() : "No asignado",
                        dateFormat.format(p.getFechaEstimada()),
                        p.getIntentosEntrega()
                });
            }

            // Actualizar timestamp
            lastUpdateLabel.setText("Última actualización: " + dateFormat.format(new java.util.Date()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar los datos: " + e.getMessage(),
                    "Error de Actualización",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    // Agregar en GestionPaquetesView.java

    private JPanel createTransportistaDetailsPanel() {
        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)));

        // Panel de título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Detalles del Transportista");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Contenedor principal para los detalles
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Agregar componentes al panel principal
        detailsPanel.add(titlePanel, BorderLayout.NORTH);
        detailsPanel.add(contentPanel, BorderLayout.CENTER);

        // Método para actualizar los detalles
        transportistaComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                contentPanel.removeAll();

                if (e.getItem() instanceof Transportista) {
                    Transportista t = (Transportista) e.getItem();

                    // Avatar o ícono
                    JLabel avatarLabel = new JLabel();
                    avatarLabel.setIcon(createAvatarIcon());
                    avatarLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentPanel.add(avatarLabel);
                    contentPanel.add(Box.createVerticalStrut(15));

                    // Información personal
                    contentPanel.add(createDetailSection("Información Personal", new String[] {
                            "Nombre: " + t.getNombre(),
                            "Email: " + t.getEmail(),
                            "Teléfono: " + t.getTelefono(),
                            "Licencia: " + t.getLicencia()
                    }));
                    contentPanel.add(Box.createVerticalStrut(10));

                    // Información del vehículo
                    if (t.getVehiculo() != null) {
                        contentPanel.add(createDetailSection("Vehículo", new String[] {
                                "Patente: " + t.getVehiculo().getPatente(),
                                "Capacidad: " + t.getVehiculo().getCapacidadVolumen() + " m³"
                        }));
                        contentPanel.add(Box.createVerticalStrut(10));
                    }

                    // Estado
                    contentPanel.add(createDetailSection("Estado", new String[] {
                            "Disponibilidad: " + (t.isDisponible() ? "Disponible" : "No disponible")
                    }));

                    // Indicador de estado visual
                    JPanel statusIndicator = new JPanel();
                    statusIndicator.setBackground(t.isDisponible() ? new Color(76, 175, 80) : new Color(244, 67, 54));
                    statusIndicator.setPreferredSize(new Dimension(contentPanel.getWidth(), 5));
                    contentPanel.add(statusIndicator);
                }

                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });

        return detailsPanel;
    }

    private JPanel createDetailSection(String title, String[] details) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BACKGROUND_COLOR);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Título de la sección
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(5));

        // Detalles
        for (String detail : details) {
            JLabel detailLabel = new JLabel(detail);
            detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            detailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            section.add(detailLabel);
            section.add(Box.createVerticalStrut(3));
        }

        return section;
    }

    private ImageIcon createAvatarIcon() {
        // Crear un icono de avatar básico
        int size = 64;
        BufferedImage avatar = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = avatar.createGraphics();

        // Configurar renderizado
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar círculo de fondo
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillOval(0, 0, size, size);

        // Dibujar ícono de persona
        g2d.setColor(Color.WHITE);
        // Cabeza
        g2d.fillOval(size / 4, size / 8, size / 2, size / 2);
        // Cuerpo
        g2d.fillArc(size / 6, size / 2, 2 * size / 3, size / 2, 0, 180);

        g2d.dispose();
        return new ImageIcon(avatar);
    }

    // Modificar el método createActionComponent para incluir el panel de detalles
    private JPanel createActionComponent(String label, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        if (!label.isEmpty()) {
            JLabel titleLabel = new JLabel(label);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(5));
        }

        if (component == transportistaComboBox) {
            // Panel horizontal para combo box y detalles
            JPanel horizontalPanel = new JPanel(new BorderLayout(10, 0));
            horizontalPanel.setBackground(BACKGROUND_COLOR);
            horizontalPanel.add(component, BorderLayout.CENTER);
            horizontalPanel.add(createTransportistaDetailsPanel(), BorderLayout.EAST);
            panel.add(horizontalPanel);
        } else {
            component.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(component);
        }

        return panel;
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