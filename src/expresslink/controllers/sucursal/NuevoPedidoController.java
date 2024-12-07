package expresslink.controllers.sucursal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import com.google.gson.Gson;

import expresslink.model.*;
import expresslink.model.LogSucursal.TipoEvento;
import expresslink.model.enums.EstadoPaquete;
import expresslink.model.enums.RolUsuario;
import expresslink.utils.DatabaseConnection;
import expresslink.view.sucursal.NuevoPedidoView;

public class NuevoPedidoController {
    private Usuario usuario;
    private Sucursal sucursal;
    private NuevoPedidoView vista;

    public NuevoPedidoController(Usuario usuario, Sucursal sucursal, NuevoPedidoView vista) {
        this.usuario = usuario;
        this.vista = vista;
        this.sucursal = sucursal;
    }

    public boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(regex);
    }

    public boolean validarNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacío.");
            return false;
        }

        // Validar longitud
        if (nombre.length() < 2 || nombre.length() > 100) {
            mostrarError("El nombre debe tener entre 2 y 100 caracteres.");
            return false;
        }

        // Permitir solo letras, espacios y algunos caracteres especiales
        String regex = "^[A-Za-zÁáÉéÍíÓóÚúÑñ\\s'.-]+$";
        if (!nombre.matches(regex)) {
            mostrarError("El nombre contiene caracteres no permitidos.");
            return false;
        }

        return true;
    }

    public boolean validarDireccion(String direccion) {
        if (direccion == null || direccion.isEmpty()) {
            mostrarError("La dirección no puede estar vacía.");
            return false;
        }

        // Validar longitud
        if (direccion.length() < 5 || direccion.length() > 200) {
            mostrarError("La dirección debe tener entre 5 y 200 caracteres.");
            return false;
        }

        // Permitir letras, números, espacios y caracteres comunes en direcciones
        String regex = "^[A-Za-zÁáÉéÍíÓóÚúÑñ0-9\\s,.'#-]+$";
        if (!direccion.matches(regex)) {
            mostrarError("La dirección contiene caracteres no permitidos.");
            return false;
        }

        return true;
    }

    public boolean validarCiudad(String ciudad) {
        if (ciudad == null || ciudad.isEmpty()) {
            mostrarError("La ciudad no puede estar vacía.");
            return false;
        }

        // Validar longitud
        if (ciudad.length() < 2 || ciudad.length() > 100) {
            mostrarError("El nombre de la ciudad debe tener entre 2 y 100 caracteres.");
            return false;
        }

        // Permitir solo letras, espacios y algunos caracteres especiales
        String regex = "^[A-Za-zÁáÉéÍíÓóÚúÑñ\\s'.-]+$";
        if (!ciudad.matches(regex)) {
            mostrarError("El nombre de la ciudad contiene caracteres no permitidos.");
            return false;
        }

        return true;
    }

    public boolean validarTelefonoChileno(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            mostrarError("El número de teléfono no puede estar vacío.");
            return false;
        }

        // Expresión regular para números de teléfono chilenos
        // - Deben comenzar con +56
        // - Luego tienen que tener 9 dígitos después del prefijo internacional
        String regex = "^\\+56[2-9][0-9]{8}$";

        if (!telefono.matches(regex)) {
            mostrarError("El número de teléfono no es válido. Debe tener el formato +56XXXXXXXXX.");
            return false;
        }

        // Si pasa todas las validaciones
        return true;
    }

    private void mostrarError(String mensaje) {
        vista.mostrarError(mensaje);
    }

    private void mostrarExito(String mensaje) {
        vista.mostrarExito(mensaje);
    }

    private String formatoDimensiones(DimensionesPaquete paquete) {
        double ancho = paquete.getAncho();
        double largo = paquete.getLargo();
        double alto = paquete.getAlto();
        double peso = paquete.getPeso();

        return ancho + "x" + largo + "x" + alto + "," + peso + "kg";
    }

    public double calcularPrecio(DimensionesPaquete dimensionesPaquete) {
        double ancho = dimensionesPaquete.getAncho();
        double largo = dimensionesPaquete.getLargo();
        double alto = dimensionesPaquete.getAlto();
        double peso = dimensionesPaquete.getPeso();

        double precioBase = 1000;

        // Calcular el volumen del paquete (en cm cubicos)
        double volumen = ancho * largo * alto;
        double costoPorPeso = peso * 500;
        double costoPorVolumen = volumen * 0.02;

        // Precio final
        double precioFinal = precioBase + costoPorPeso + costoPorVolumen;

        return precioFinal;
    }

    public static String generarNumeroSeguimiento() {
        int longitud = 10;
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder numeroSeguimiento = new StringBuilder(longitud);
        Random random = new Random();

        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            char caracterAleatorio = caracteres.charAt(indice);
            numeroSeguimiento.append(caracterAleatorio);
        }

        return numeroSeguimiento.toString();
    }

    private void logSucursal(Connection conn, String descripcion, LogSucursal.TipoEvento tipoEvento,
            Map<String, Object> datosCompletos) throws SQLException {
        PreparedStatement stmt = null;
        try {
            // Configurar datos del remitente
            Map<String, Object> metadata = new HashMap<>();
            Map<String, String> remitente = new HashMap<>();
            remitente.put("nombre", (String) datosCompletos.get("remitenteNombre"));

            // Agregar campos opcionales solo si tienen valor
            String telefono = (String) datosCompletos.get("remitenteTelefono");
            if (telefono != null && !telefono.equals("+569")) {
                remitente.put("telefono", telefono);
            }

            String email = (String) datosCompletos.get("remitenteEmail");
            if (email != null && !email.isEmpty()) {
                remitente.put("email", email);
            }

            metadata.put("remitente", remitente);

            // Configurar datos del destinatario
            Map<String, String> destinatario = new HashMap<>();
            destinatario.put("nombre", (String) datosCompletos.get("destinatarioNombre"));
            destinatario.put("direccion", (String) datosCompletos.get("destinatarioDireccion"));
            destinatario.put("ciudad", (String) datosCompletos.get("destinatarioCiudad"));
            metadata.put("destinatario", destinatario);

            // Configurar dimensiones
            Map<String, Double> dimensiones = new HashMap<>();
            dimensiones.put("alto", ((Number) datosCompletos.get("alto")).doubleValue());
            dimensiones.put("ancho", ((Number) datosCompletos.get("ancho")).doubleValue());
            dimensiones.put("largo", ((Number) datosCompletos.get("largo")).doubleValue());
            dimensiones.put("peso", ((Number) datosCompletos.get("peso")).doubleValue());
            metadata.put("dimensiones", dimensiones);

            // Configurar datos adicionales
            metadata.put("costoCalculado", ((Number) datosCompletos.get("costo")).doubleValue());
            metadata.put("sucursalOrigen", sucursal.getNombre());
            metadata.put("usuarioCreador", usuario.getNombre());

            // Convertir a JSON usando Gson
            Gson gson = new Gson();
            String jsonMetadata = gson.toJson(metadata);

            // Query modificado para MySQL - removido el ::json
            String query = "INSERT INTO log_sucursal (sucursal_id, tipo_evento, descripcion, fecha, metadata, usuario_id) "
                    +
                    "VALUES (?, ?, ?, NOW(), ?, ?)";

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, sucursal.getId());
            stmt.setString(2, tipoEvento.name());
            stmt.setString(3, descripcion);
            stmt.setString(4, jsonMetadata); // MySQL convertirá automáticamente el string JSON
            stmt.setInt(5, usuario.getId());

            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private void actualizarBalance(Connection conn, double ingreso) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String query = "UPDATE balance_sucursal " +
                    "SET balance_actual = (SELECT balance_actual FROM balance_sucursal " +
                    "    WHERE sucursal_id = ? FOR UPDATE) + ?, " +
                    "    ingresos_periodo = (SELECT ingresos_periodo FROM balance_sucursal " +
                    "    WHERE sucursal_id = ? FOR UPDATE) + ?, " +
                    "    fecha_actualizacion = NOW() " +
                    "WHERE sucursal_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, sucursal.getId());
            stmt.setDouble(2, ingreso);
            stmt.setInt(3, sucursal.getId());
            stmt.setDouble(4, ingreso);
            stmt.setInt(5, sucursal.getId());
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private Usuario obtenerUsuarioSiExiste(Connection conn, String email) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT u.*, s.nombre as sucursal_nombre, s.direccion as sucursal_direccion, " +
                    "s.ciudad as sucursal_ciudad " +
                    "FROM usuario u " +
                    "LEFT JOIN sucursal s ON u.sucursal_id = s.id " +
                    "WHERE u.email = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Sucursal sucursal = null;
                if (rs.getObject("sucursal_id") != null) {
                    sucursal = new Sucursal(
                            rs.getInt("sucursal_id"),
                            rs.getString("sucursal_nombre"),
                            rs.getString("sucursal_direccion"),
                            rs.getString("sucursal_ciudad"));
                }

                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("telefono"),
                        RolUsuario.valueOf(rs.getString("rol")),
                        sucursal);
            }
            return null;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public boolean crearNuevoPaquete(String email, String destinatario, String direccion,
            String ciudad, String remitenteNombre, String remitenteTelefono,
            DimensionesPaquete dimensionesPaquete, double costo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String numeroSeguimiento = generarNumeroSeguimiento();
            // Buscar cliente existente solo si hay email
            Usuario clienteExistente = email != null && !email.trim().isEmpty() ? obtenerUsuarioSiExiste(conn, email)
                    : null;

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 5);
            Date fechaEstimada = new Date(calendar.getTimeInMillis());

            String query = "INSERT INTO paquete (num_seguimiento, email_cliente, cliente_id, " +
                    "sucursal_origen_id, transportista_id, destinatario, dir_destino, " +
                    "dimensiones_peso, costo, estado, fecha_creacion, fecha_estimada, " +
                    "intentos_entrega, usuario_creador_id, fecha_actualizacion) " +
                    "VALUES (?, ?, ?, ?, NULL, ?, ?, ?, ?, " +
                    "        'PENDIENTE', NOW(), ?, 0, ?, NOW())";

            stmt = conn.prepareStatement(query);
            int paramIndex = 1;
            stmt.setString(paramIndex++, numeroSeguimiento);
            // Manejar email nulo
            if (email != null && !email.trim().isEmpty()) {
                stmt.setString(paramIndex++, email.trim());
            } else {
                stmt.setNull(paramIndex++, Types.VARCHAR);
            }
            if (clienteExistente != null) {
                stmt.setInt(paramIndex++, clienteExistente.getId());
            } else {
                stmt.setNull(paramIndex++, Types.INTEGER);
            }
            stmt.setInt(paramIndex++, sucursal.getId());
            stmt.setString(paramIndex++, destinatario);
            stmt.setString(paramIndex++, direccion);
            stmt.setString(paramIndex++, formatoDimensiones(dimensionesPaquete));
            stmt.setDouble(paramIndex++, costo);
            stmt.setDate(paramIndex++, new java.sql.Date(fechaEstimada.getTime()));
            stmt.setInt(paramIndex++, usuario.getId());

            int result = stmt.executeUpdate();

            if (result > 0) {
                Map<String, Object> datosCompletos = new HashMap<>();
                datosCompletos.put("remitenteNombre", remitenteNombre);
                // Manejar campos opcionales en el log
                datosCompletos.put("remitenteTelefono",
                        (remitenteTelefono != null && !remitenteTelefono.trim().isEmpty()) ? remitenteTelefono.trim()
                                : null);
                datosCompletos.put("remitenteEmail",
                        (email != null && !email.trim().isEmpty()) ? email.trim() : null);
                datosCompletos.put("destinatarioNombre", destinatario);
                datosCompletos.put("destinatarioDireccion", direccion);
                datosCompletos.put("destinatarioCiudad", ciudad);
                datosCompletos.put("alto", dimensionesPaquete.getAlto());
                datosCompletos.put("ancho", dimensionesPaquete.getAncho());
                datosCompletos.put("largo", dimensionesPaquete.getLargo());
                datosCompletos.put("peso", dimensionesPaquete.getPeso());
                datosCompletos.put("costo", costo);

                logSucursal(conn, "Ingreso a Bodega Paquete " + numeroSeguimiento,
                        LogSucursal.TipoEvento.CREACION, datosCompletos);
                actualizarBalance(conn, costo);

                conn.commit();
                return true;
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
