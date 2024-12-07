package expresslink.view.sucursal;

import javax.swing.*;
import expresslink.model.*;

import java.awt.*;

//Chibi
public class NuevoPedidoView extends JPanel {
    private Usuario usuario;
    private Sucursal sucursal;

    // Componentes graficos
    private JTextField remitenteNombreField, remitenteDNIField, remitenteTelefonoField, remitenteEmailField;
    private JTextField destinatarioNombreField, destinatarioDireccionField, destinatarioCiudadField;
    private JTextField altoField, anchoField, largoField, pesoField;
    private JButton generarPedidoBtn, cancelarBtn;

    public NuevoPedidoView(Usuario usuario, Sucursal sucursal) {
        this.usuario = usuario;
        this.sucursal = sucursal;

        inicializarGUI();
    }

    // Constructor que crea y organiza los componentes
    private void inicializarGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margenes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER; // Centrado de componentes

        // Colores y estilos
        Color headerColor = new Color(33, 150, 243); // Azul cabecera
        Color borderColor = new Color(200, 200, 200); // Gris claro bordes
        Color bgColor = Color.WHITE; // Fondo blanco
        Color btnColor = new Color(30, 144, 255); // Azul botones
        Color btnTextColor = Color.WHITE; // Texto botones blanco

        // Fondo del panel
        setBackground(bgColor);

        // Cabecera
        JLabel headerLabel = new JLabel("Ingreso de Nuevo Pedido");
        headerLabel.setOpaque(true);
        headerLabel.setBackground(headerColor);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(headerLabel, gbc);

        // Seccion del remitente
        addSectionTitle("Informacion del Remitente", gbc, 0, 1, 2);
        remitenteNombreField = createTextField();
        remitenteDNIField = createTextField();
        remitenteTelefonoField = createTextField();
        remitenteEmailField = createTextField();
        addField("Nombre:", remitenteNombreField, gbc, 0, 2);
        addField("DNI:", remitenteDNIField, gbc, 0, 3);
        addField("Telefono:", remitenteTelefonoField, gbc, 0, 4);
        addField("Email:", remitenteEmailField, gbc, 0, 5);

        // Seccion del paquete
        addSectionTitle("Dimensiones del Paquete", gbc, 0, 6, 2);
        altoField = createTextField();
        anchoField = createTextField();
        largoField = createTextField();
        pesoField = createTextField();
        addField("Alto (cm):", altoField, gbc, 0, 7);
        addField("Ancho (cm):", anchoField, gbc, 2, 7);
        addField("Largo (cm):", largoField, gbc, 0, 8);
        addField("Peso (kg):", pesoField, gbc, 2, 8);

        // Seccion del destinatario
        addSectionTitle("Informacion del Destinatario", gbc, 2, 1, 2);
        destinatarioNombreField = createTextField();
        destinatarioDireccionField = createTextField();
        destinatarioCiudadField = createTextField();
        addField("Nombre:", destinatarioNombreField, gbc, 2, 2);
        addField("Direccion:", destinatarioDireccionField, gbc, 2, 3);
        addField("Ciudad:", destinatarioCiudadField, gbc, 2, 4);

        // Panel de resumen del envio
        JPanel resumenPanel = new JPanel();
        resumenPanel.setLayout(new BorderLayout());
        resumenPanel.setBackground(new Color(230, 240, 255)); // Fondo azul claro
        resumenPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        JLabel resumenEnvioLabel = new JLabel(
                "<html><div style='text-align: left;'>Resumen del Envio<br>Sucursal Origen: Central La Serena<br>Tipo de Envio: Local<br>Tiempo Estimado: 24 horas<br>Costo Total: $15,000</div></html>");
        resumenEnvioLabel.setHorizontalAlignment(SwingConstants.LEFT);
        resumenPanel.add(resumenEnvioLabel, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 4;
        add(resumenPanel, gbc);

        // Botones de accion
        generarPedidoBtn = createStyledButton("Generar Pedido", btnColor, btnTextColor);
        cancelarBtn = createStyledButton("Cancelar", btnColor, btnTextColor);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(generarPedidoBtn);
        buttonPanel.add(cancelarBtn);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    // Metodo para crear un campo de texto
    private JTextField createTextField() {
        JTextField textField = new JTextField(15);
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return textField;
    }

    // Metodo para crear un boton estilizado
    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    // Metodo para agregar un titulo de seccion
    private void addSectionTitle(String title, GridBagConstraints gbc, int x, int y, int width) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setOpaque(true);
        label.setBackground(new Color(240, 240, 240));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        add(label, gbc);
    }

    // Metodo para agregar un campo y su etiqueta
    private void addField(String labelText, JTextField textField, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        add(new JLabel(labelText), gbc);
        gbc.gridx = x + 1;
        add(textField, gbc);
    }
}
