package expresslink.view.sucursal;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;

import expresslink.controllers.sucursal.NuevoPedidoController;
import expresslink.model.*;

//Chibi
public class NuevoPedidoView extends JPanel {
    private Usuario usuario;
    private Sucursal sucursal;
    private NuevoPedidoController controlador;
    private DimensionesPaquete dimensionesPaquete;

    // Componentes graficos
    private JTextField remitenteNombreField, remitenteTelefonoField, remitenteEmailField;
    private JTextField destinatarioNombreField, destinatarioDireccionField, destinatarioCiudadField;
    private JTextField altoField, anchoField, largoField, pesoField;
    private JButton generarPedidoBtn, cancelarBtn;
    private JLabel resumenEnvioLabel;
    private double costo;

    public NuevoPedidoView(Usuario usuario, Sucursal sucursal) {
        this.usuario = usuario;
        this.sucursal = sucursal;
        this.controlador = new NuevoPedidoController(usuario, sucursal, this);
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
        remitenteTelefonoField = createTextField();
        remitenteEmailField = createTextField();
        addField("Nombre:", remitenteNombreField, gbc, 0, 2);
        addField("Telefono:", remitenteTelefonoField, gbc, 0, 3);
        addField("Email:", remitenteEmailField, gbc, 0, 4);

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
        resumenEnvioLabel = new JLabel(
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

        // Listeners
        generarPedidoBtn.addActionListener(e -> generarOrdenPaquete());
        cancelarBtn.addActionListener(e -> limpiarCampos());

        DocumentListener dimensionesListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarResumen();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarResumen();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarResumen();
            }
        };

        // Agregar el DocumentListener a cada campo
        altoField.getDocument().addDocumentListener(dimensionesListener);
        anchoField.getDocument().addDocumentListener(dimensionesListener);
        largoField.getDocument().addDocumentListener(dimensionesListener);
        pesoField.getDocument().addDocumentListener(dimensionesListener);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(generarPedidoBtn);
        buttonPanel.add(cancelarBtn);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        remitenteTelefonoField.setText("+56");
        // limpiarCampos();
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

    private boolean validarCampos() {
        if (remitenteNombreField.getText().trim().isEmpty()) {
            mostrarError("Por favor complete el nombre del remitente");
            return false;
        }

        if (destinatarioNombreField.getText().trim().isEmpty() ||
                destinatarioDireccionField.getText().trim().isEmpty() ||
                destinatarioCiudadField.getText().trim().isEmpty()) {
            mostrarError("Por favor complete todos los campos del destinatario");
            return false;
        }

        String email = remitenteEmailField.getText().trim();
        if (!email.isEmpty() && !controlador.validarEmail(email)) {
            mostrarError("El formato del email no es válido");
            return false;
        }

        String telefono = remitenteTelefonoField.getText().trim();
        if (!telefono.equals("+56") && !controlador.validarTelefonoChileno(telefono)) {
            mostrarError("El formato del teléfono no es válido. Debe tener el formato +56XXXXXXXXX");
            return false;
        }

        if (!controlador.validarNombre(remitenteNombreField.getText().trim())) {
            return false;
        }

        if (!controlador.validarDireccion(destinatarioDireccionField.getText().trim())) {
            return false;
        }

        if (!controlador.validarCiudad(destinatarioCiudadField.getText().trim())) {
            return false;
        }

        // Validar dimensiones
        if (altoField.getText().trim().isEmpty() ||
                anchoField.getText().trim().isEmpty() ||
                largoField.getText().trim().isEmpty() ||
                pesoField.getText().trim().isEmpty()) {
            mostrarError("Por favor complete todas las dimensiones del paquete");
            return false;
        }

        try {
            double alto = Double.parseDouble(altoField.getText().trim());
            double ancho = Double.parseDouble(anchoField.getText().trim());
            double largo = Double.parseDouble(largoField.getText().trim());
            double peso = Double.parseDouble(pesoField.getText().trim());

            if (alto <= 0 || ancho <= 0 || largo <= 0 || peso <= 0) {
                mostrarError("Las dimensiones y peso deben ser mayores a 0");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("Por favor ingrese valores numéricos válidos para las dimensiones");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        remitenteNombreField.setText("");
        remitenteTelefonoField.setText("");
        remitenteEmailField.setText("");

        destinatarioNombreField.setText("");
        destinatarioDireccionField.setText("");
        destinatarioCiudadField.setText("");

        altoField.setText("");
        anchoField.setText("");
        largoField.setText("");
        pesoField.setText("");

        remitenteTelefonoField.setText("+56");

        resumenEnvioLabel.setText(String.format(
                "<html><div style='text-align: left;'>Resumen del Envio<br>" +
                        "Sucursal Origen: %s<br>" +
                        "Tipo de Envio: Local<br>" +
                        "Tiempo Estimado: 5 Días<br>" +
                        "Costo Total: $--</div></html>",
                sucursal.getNombre()));
    }

    private void actualizarResumen() {
        if (controlador != null) {
            try {
                String anchoText = anchoField.getText().trim();
                String largoText = largoField.getText().trim();
                String altoText = altoField.getText().trim();
                String pesoText = pesoField.getText().trim();

                // Si algún campo está vacío, mostrar el resumen sin precio
                if (anchoText.isEmpty() || largoText.isEmpty() ||
                        altoText.isEmpty() || pesoText.isEmpty()) {
                    resumenEnvioLabel.setText(String.format(
                            "<html><div style='text-align: left;'>Resumen del Envio<br>" +
                                    "Sucursal Origen: %s<br>" +
                                    "Tipo de Envio: Local<br>" +
                                    "Tiempo Estimado: 5 Días<br>" +
                                    "Costo Total: $--</div></html>",
                            sucursal.getNombre()));
                    return;
                }

                // Intentar parsear los valores
                double ancho = Double.parseDouble(anchoText);
                double largo = Double.parseDouble(largoText);
                double alto = Double.parseDouble(altoText);
                double peso = Double.parseDouble(pesoText);

                // Validar que los valores sean positivos
                if (ancho <= 0 || largo <= 0 || alto <= 0 || peso <= 0) {
                    resumenEnvioLabel.setText(String.format(
                            "<html><div style='text-align: left;'>Resumen del Envio<br>" +
                                    "Sucursal Origen: %s<br>" +
                                    "Tipo de Envio: Local<br>" +
                                    "Tiempo Estimado: 5 Días<br>" +
                                    "Costo Total: $--<br>" +
                                    "<font color='red'>Las dimensiones deben ser mayores a 0</font></div></html>",
                            sucursal.getNombre()));
                    return;
                }

                this.dimensionesPaquete = new DimensionesPaquete(ancho, largo, alto, peso);
                costo = controlador.calcularPrecio(dimensionesPaquete);

                resumenEnvioLabel.setText(String.format(
                        "<html><div style='text-align: left;'>Resumen del Envio<br>" +
                                "Sucursal Origen: %s<br>" +
                                "Tipo de Envio: Local<br>" +
                                "Tiempo Estimado: 5 Días<br>" +
                                "Costo Total: $%.2f</div></html>",
                        sucursal.getNombre(), costo));

            } catch (NumberFormatException e) {
                // No mostrar mensaje de error, solo actualizar el resumen sin precio
                resumenEnvioLabel.setText(String.format(
                        "<html><div style='text-align: left;'>Resumen del Envio<br>" +
                                "Sucursal Origen: %s<br>" +
                                "Tipo de Envio: Local<br>" +
                                "Tiempo Estimado: 5 Días<br>" +
                                "Costo Total: $--<br>" +
                                "<font color='red'>Ingrese solo valores numéricos</font></div></html>",
                        sucursal.getNombre()));
            }
        }
    }

    private boolean generarOrdenPaquete() {
        if (!validarCampos()) {
            return false;
        }

        try {
            double alto = Double.parseDouble(altoField.getText().trim());
            double ancho = Double.parseDouble(anchoField.getText().trim());
            double largo = Double.parseDouble(largoField.getText().trim());
            double peso = Double.parseDouble(pesoField.getText().trim());
            DimensionesPaquete dimensiones = new DimensionesPaquete(ancho, largo, alto, peso);

            String email = remitenteEmailField.getText().trim();
            String remitenteNombre = remitenteNombreField.getText().trim();
            String remitenteTelefono = remitenteTelefonoField.getText().trim();

            if (remitenteTelefono.equals("+56")) {
                remitenteTelefono = null;
            }

            String destinatario = destinatarioNombreField.getText().trim();
            String direccion = destinatarioDireccionField.getText().trim();
            String ciudad = destinatarioCiudadField.getText().trim();

            if (email.isEmpty()) {
                email = null;
            }

            boolean success = controlador.crearNuevoPaquete(
                    email,
                    destinatario,
                    direccion,
                    ciudad,
                    remitenteNombre,
                    remitenteTelefono,
                    dimensiones,
                    costo);

            if (success) {
                mostrarExito("Pedido generado exitosamente");
                limpiarCampos();
                return true;
            } else {
                mostrarError("Error al generar el pedido");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
            return false;
        }
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

}
