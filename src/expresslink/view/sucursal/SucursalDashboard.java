package expresslink.view.sucursal;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SucursalDashboard extends JFrame {
    // Definimos los colores que usaremos en toda la aplicacion
    // RGB significa Red(Rojo), Green(Verde), Blue(Azul) y van de 0 a 255
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);    // Color azul para los encabezados
    private final Color BACKGROUND_COLOR = Color.WHITE;             // Fondo blanco
    private final Color MENU_BACKGROUND = new Color(240, 240, 240); // Gris clarito para el menu
    private final Color TEXT_COLOR = new Color(33, 33, 33);        // Color oscuro para el texto
    
    // Creamos las partes principales de nuestra ventana
    private JPanel mainPanel;          // Panel principal que contiene todo
    private JPanel contentPanel;       // Panel donde mostraremos el contenido
    private CardLayout cardLayout;     // Esto nos ayuda a cambiar entre diferentes pantallas
    private String currentLocation = "La Serena"; // Guardamos la ubicacion actual

    // Este es el constructor, se ejecuta cuando creamos una nueva ventana
    public SucursalDashboard() {
        // Configuramos la ventana principal
        setTitle("Express Link - Sucursal");               // Titulo de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Hace que se cierre cuando apretamos la X
        setSize(1200, 700);                               // Tamanno de la ventana
        setLocationRelativeTo(null);                      // Centra la ventana en la pantalla
        
        // Creamos el panel principal que contendra todo
        mainPanel = new JPanel(new BorderLayout());       // BorderLayout nos permite poner cosas arriba, abajo, izquierda, derecha y centro
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Creamos y agregamos el menu lateral
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);      // Lo ponemos al lado izquierdo

        // Configuramos el panel donde cambiaran las pantallas
        cardLayout = new CardLayout();                    // CardLayout nos permite cambiar entre diferentes pantallas
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Creamos todas las pantallas que necesitamos
        createContentPanels();
        
        // Juntamos todo y lo mostramos
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // Crea el menu lateral con botones
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(200, 600)); // Le damos un tamanno fijo al menu
        menuPanel.setBackground(MENU_BACKGROUND);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); // Organizamos los elementos de arriba a abajo
        menuPanel.setBorder(new EmptyBorder(20, 10, 20, 10));           // Agregamos espacio alrededor

        // Agregamos el titulo del menu
        JLabel titleLabel = new JLabel("Express Link");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));        // Configuramos la letra
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);           // Lo centramos
        menuPanel.add(titleLabel);
        
        // Agregamos el nombre de la sucursal
        JLabel locationLabel = new JLabel("Sucursal " + currentLocation);
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationLabel.setBorder(new EmptyBorder(5, 0, 20, 0));
        menuPanel.add(locationLabel);

        // Lista de opciones para el menu
        String[] menuItems = {
            "Nuevo Pedido",
            "Pedidos Pendientes",
            "Pedidos en Transito",
            "Entregas del Dia",
            "Reportes"
        };

        // Creamos un boton para cada opcion del menu
        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item);
            menuPanel.add(menuButton);
            menuPanel.add(Box.createVerticalStrut(10));   // Espacio entre botones
        }

        return menuPanel;
    }

    // Crea un boton bonito para el menu
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));    // Tamanno del boton
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(TEXT_COLOR);
        button.setBackground(BACKGROUND_COLOR);
        button.setFocusPainted(false);                    // Quitamos el borde feo cuando se selecciona
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centramos el boton

        // Cuando hacemos clic en el boton, cambia la pantalla
        button.addActionListener(e -> cardLayout.show(contentPanel, text));
        
        // Efecto cuando pasamos el mouse por encima
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 230));  // Gris cuando pasamos el mouse
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BACKGROUND_COLOR);          // Blanco cuando quitamos el mouse
            }
        });

        return button;
    }

    // Crea todas las pantallas que necesitamos
    private void createContentPanels() {
        // Panel principal (el que se ve al inicio)
        contentPanel.add(createDashboardPanel(), "Dashboard");
        
        // Otras pantallas con boton para volver
        contentPanel.add(createPanelWithBackButton("Nuevo Pedido"), "Nuevo Pedido");
        contentPanel.add(createPanelWithBackButton("Pedidos Pendientes"), "Pedidos Pendientes");
        contentPanel.add(createPanelWithBackButton("Pedidos en Transito"), "Pedidos en Transito");
        contentPanel.add(createPanelWithBackButton("Entregas del Dia"), "Entregas del Dia");
        contentPanel.add(createPanelWithBackButton("Reportes"), "Reportes");
    }

    // Crea la pantalla principal con estadisticas y tabla de pedidos
    private JPanel createDashboardPanel() {
        // Creamos el panel principal del dashboard con espaciado entre elementos
        JPanel dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setBackground(BACKGROUND_COLOR);
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Creamos la barra azul de arriba con el titulo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Agregamos el titulo de la sucursal en la barra azul
        JLabel titleLabel = new JLabel("Sucursal Central - " + currentLocation);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);           // Texto en blanco
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        dashboardPanel.add(headerPanel, BorderLayout.NORTH);

        // Creamos el panel para las tres tarjetas de estadisticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(BACKGROUND_COLOR);

        // Agregamos las tres tarjetas con numeros
        statsPanel.add(createStatCard("45", "Pedidos Pendientes"));
        statsPanel.add(createStatCard("12", "En Transito"));
        statsPanel.add(createStatCard("28", "Entregados Hoy"));

        dashboardPanel.add(statsPanel, BorderLayout.CENTER);

        // Creamos la seccion de pedidos recientes con su tabla
        JPanel recentOrdersPanel = new JPanel(new BorderLayout());
        recentOrdersPanel.setBackground(BACKGROUND_COLOR);
        recentOrdersPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Titulo de la seccion de pedidos recientes
        JLabel recentOrdersTitle = new JLabel("Pedidos Recientes");
        recentOrdersTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        recentOrdersPanel.add(recentOrdersTitle, BorderLayout.NORTH);
        
        // Creamos la tabla con sus columnas y datos
        String[] columnNames = {"N° Pedido", "Destinatario", "Estado", "Accion"};
        Object[][] data = {
            {"#2024-001", "Juan Perez", "Pendiente", "Ver detalles"},
            {"#2024-002", "Maria Garcia", "En Ruta", "Ver detalles"}
        };
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);  // Permite hacer scroll si hay muchos datos
        recentOrdersPanel.add(scrollPane, BorderLayout.CENTER);

        dashboardPanel.add(recentOrdersPanel, BorderLayout.SOUTH);

        return dashboardPanel;
    }

    // Crea las tarjetas que muestran numeros grandes con texto abajo
    private JPanel createStatCard(String number, String label) {
        // Creamos la tarjeta con un borde bonito
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(240, 247, 255));  // Azul muy clarito
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),  // Borde gris
            new EmptyBorder(15, 15, 15, 15)               // Espacio interior
        ));

        // Agregamos el numero grande
        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        numberLabel.setHorizontalAlignment(JLabel.CENTER);

        // Agregamos el texto descriptivo abajo
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setHorizontalAlignment(JLabel.CENTER);

        // Juntamos todo en la tarjeta
        card.add(numberLabel, BorderLayout.CENTER);
        card.add(textLabel, BorderLayout.SOUTH);

        return card;
    }

    // Crea un panel simple con un boton para volver al inicio
    private JPanel createPanelWithBackButton(String panelName) {
        // Panel basico con un titulo y un boton para volver
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(panelName, SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        // Boton para volver al inicio
        JButton backButton = new JButton("Volver al Inicio");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "Dashboard"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    // Metodo principal que inicia la aplicacion
    public static void main(String[] args) {
        // Esto asegura que la interfaz se cree en el hilo correcto
        SwingUtilities.invokeLater(() -> {
            SucursalDashboard dashboard = new SucursalDashboard();
            dashboard.setVisible(true);  // Hace visible la ventana
        });
    }
}