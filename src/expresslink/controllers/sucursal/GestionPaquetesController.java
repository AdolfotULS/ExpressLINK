package expresslink.controllers.sucursal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.HashMap;

import expresslink.model.*;
import expresslink.model.enums.*;
import expresslink.utils.DatabaseConnection;

public class GestionPaquetesController {
    private final Usuario usuario;
    private final Sucursal sucursal;
    private static final Logger LOGGER = Logger.getLogger(GestionPaquetesController.class.getName());
    private static final double VOLUMEN_MAXIMO_VEHICULO = 20.0; // 20 m³ máximo por vehículo

    public GestionPaquetesController(Usuario usuario, Sucursal sucursal) {
        if (usuario == null || sucursal == null) {
            throw new IllegalArgumentException("Usuario y sucursal no pueden ser nulos");
        }
        this.usuario = usuario;
        this.sucursal = sucursal;
        LOGGER.info("Inicializando GestionPaquetesController para sucursal: " + sucursal.getNombre());
    }

    /**
     * Obtiene todos los paquetes asociados a la sucursal actual
     */
    public List<Paquete> obtenerPaquetes() throws SQLException {
        List<Paquete> paquetes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = """
                        SELECT p.*,
                               t.id as transportista_id, t.licencia, t.disponible,
                               ut.id as transportista_usuario_id, ut.nombre as transportista_nombre,
                               ut.email as transportista_email, ut.telefono as transportista_telefono,
                               u.id as cliente_id, u.nombre as cliente_nombre, u.email as cliente_email,
                               uc.id as usuario_creador_id, uc.nombre as usuario_creador_nombre,
                               uc.email as usuario_creador_email,
                               v.id as vehiculo_id, v.patente, v.capacidad_volumen
                        FROM paquete p
                        LEFT JOIN transportista t ON p.transportista_id = t.id
                        LEFT JOIN usuario ut ON t.usuario_id = ut.id
                        LEFT JOIN usuario u ON p.cliente_id = u.id
                        LEFT JOIN usuario uc ON p.usuario_creador_id = uc.id
                        LEFT JOIN vehiculo v ON t.vehiculo_id = v.id
                        WHERE p.sucursal_origen_id = ?
                        ORDER BY p.fecha_creacion DESC
                    """;

            LOGGER.info("Ejecutando consulta de paquetes para sucursal: " + sucursal.getId());
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, sucursal.getId());
            rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    Paquete paquete = mapResultSetToPaquete(rs);
                    paquetes.add(paquete);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error mapeando paquete desde ResultSet", e);
                    LOGGER.severe("Detalles del error: " + e.getMessage());
                    throw e;
                }
            }
            LOGGER.info("Obtenidos " + paquetes.size() + " paquetes");
            return paquetes;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error obteniendo paquetes", e);
            LOGGER.severe("Error SQL: " + e.getMessage());
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }

    /**
     * Obtiene la lista de transportistas disponibles
     */
    public List<Transportista> obtenerTransportistasDisponibles() throws SQLException {
        List<Transportista> transportistas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            // String query = """
            // SELECT t.*, u.*, v.id as vehiculo_id, v.patente, v.capacidad_volumen
            // FROM transportista t
            // JOIN usuario u ON t.usuario_id = u.id
            // LEFT JOIN vehiculo v ON t.vehiculo_id = v.id
            // WHERE t.sucursal_id = ? AND t.disponible = true
            // """;
            String query = """
                        SELECT t.*, u.*, v.id as vehiculo_id, v.patente, v.capacidad_volumen
                        FROM transportista t
                        JOIN usuario u ON t.usuario_id = u.id
                        LEFT JOIN vehiculo v ON t.vehiculo_id = v.id
                        WHERE t.sucursal_id = ?
                    """;

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, sucursal.getId());
            rs = stmt.executeQuery();

            while (rs.next()) {
                Vehiculo vehiculo = null;
                if (rs.getObject("vehiculo_id") != null) {
                    vehiculo = new Vehiculo(
                            rs.getInt("vehiculo_id"),
                            rs.getString("patente"),
                            rs.getDouble("capacidad_volumen"));
                }

                Transportista transportista = new Transportista(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("telefono"),
                        RolUsuario.valueOf(rs.getString("rol")),
                        sucursal,
                        rs.getString("licencia"),
                        rs.getBoolean("disponible"),
                        vehiculo);
                transportistas.add(transportista);
            }
            return transportistas;

        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }

    /**
     * Cambia el estado de un paquete y registra el cambio
     */
    public void cambiarEstadoPaquete(String numSeguimiento, EstadoPaquete nuevoEstado)
            throws SQLException {
        if (numSeguimiento == null || nuevoEstado == null) {
            throw new IllegalArgumentException("Número de seguimiento y estado no pueden ser nulos");
        }

        Connection conn = null;
        PreparedStatement stmtPaquete = null;
        PreparedStatement stmtLog = null;
        PreparedStatement stmtSucursalLog = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Obtener estado actual
            EstadoPaquete estadoAnterior = obtenerEstadoPaquete(conn, numSeguimiento);

            // Actualizar estado del paquete
            String updateQuery = """
                        UPDATE paquete
                        SET estado = ?, fecha_actualizacion = NOW()
                        WHERE num_seguimiento = ?
                    """;

            stmtPaquete = conn.prepareStatement(updateQuery);
            stmtPaquete.setString(1, nuevoEstado.toString());
            stmtPaquete.setString(2, numSeguimiento);
            stmtPaquete.executeUpdate();

            // Registrar cambio en el log
            String logQuery = """
                        INSERT INTO log_paquete (
                            paquete_id, estado_anterior, estado_nuevo,
                            descripcion, fecha, metadata, usuario_id
                        ) VALUES (
                            (SELECT id FROM paquete WHERE num_seguimiento = ?),
                            ?, ?, ?, NOW(), '{}', ?
                        )
                    """;

            stmtLog = conn.prepareStatement(logQuery);
            stmtLog.setString(1, numSeguimiento);
            stmtLog.setString(2, estadoAnterior.toString());
            stmtLog.setString(3, nuevoEstado.toString());
            stmtLog.setString(4, "Cambio de estado por " + usuario.getNombre());
            stmtLog.setInt(5, usuario.getId());
            stmtLog.executeUpdate();

            String sucursalLogQuery = """
                        INSERT INTO log_sucursal (
                            sucursal_id, tipo_evento, descripcion,
                            fecha, metadata, usuario_id
                        ) VALUES (?, 'ACTUALIZACION', ?, NOW(), ?, ?)
                    """;

            stmtSucursalLog = conn.prepareStatement(sucursalLogQuery);
            stmtSucursalLog.setInt(1, sucursal.getId());
            stmtSucursalLog.setString(2, "Cambio de estado de paquete " + numSeguimiento +
                    " de " + estadoAnterior + " a " + nuevoEstado);
            stmtSucursalLog.setString(3, "{}"); // metadata en formato JSON
            stmtSucursalLog.setInt(4, usuario.getId());
            stmtSucursalLog.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (stmtLog != null)
                stmtLog.close();
            if (stmtPaquete != null)
                stmtPaquete.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // Métodos auxiliares privados

    private EstadoPaquete obtenerEstadoPaquete(Connection conn, String numSeguimiento)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT estado FROM paquete WHERE num_seguimiento = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, numSeguimiento);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return EstadoPaquete.valueOf(rs.getString("estado"));
            } else {
                throw new SQLException("Paquete no encontrado");
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }

    private Paquete mapResultSetToPaquete(ResultSet rs) throws SQLException {
        try {
            // Mapear cliente
            Usuario cliente = null;
            if (rs.getObject("cliente_id") != null) {
                LOGGER.fine("Mapeando cliente con ID: " + rs.getInt("cliente_id"));
                cliente = new Usuario(
                        rs.getInt("cliente_id"),
                        rs.getString("cliente_nombre"),
                        rs.getString("cliente_email"),
                        "", // No necesitamos la contraseña
                        "", // No necesitamos el teléfono
                        null, // No necesitamos el rol
                        null // No necesitamos la sucursal
                );
            }

            // Mapear transportista y su vehículo
            Transportista transportista = null;
            if (rs.getObject("transportista_id") != null) {
                LOGGER.fine("Mapeando transportista con ID: " + rs.getInt("transportista_id"));
                Vehiculo vehiculo = null;
                if (rs.getObject("vehiculo_id") != null) {
                    vehiculo = new Vehiculo(
                            rs.getInt("vehiculo_id"),
                            rs.getString("patente"),
                            rs.getDouble("capacidad_volumen"));
                }
                transportista = new Transportista(
                        rs.getInt("transportista_id"),
                        rs.getString("transportista_nombre"),
                        rs.getString("transportista_email"),
                        "", // No necesitamos la contraseña
                        rs.getString("transportista_telefono"),
                        RolUsuario.TRANSPORTISTA,
                        sucursal,
                        rs.getString("licencia"),
                        rs.getBoolean("disponible"),
                        vehiculo);
            }

            // Mapear creador
            Usuario creador = null;
            if (rs.getObject("usuario_creador_id") != null) {
                LOGGER.fine("Mapeando creador con ID: " + rs.getInt("usuario_creador_id"));
                creador = new Usuario(
                        rs.getInt("usuario_creador_id"),
                        rs.getString("usuario_creador_nombre"),
                        rs.getString("usuario_creador_email"),
                        "", // No necesitamos la contraseña
                        "", // No necesitamos el teléfono
                        null, // No necesitamos el rol
                        null // No necesitamos la sucursal
                );
            }

            String dimensionesStr = rs.getString("dimensiones_peso");
            LOGGER.fine("Parseando dimensiones: " + dimensionesStr);
            DimensionesPaquete dimensiones = new DimensionesPaquete(
                    dimensionesStr != null ? dimensionesStr : "0x0x0x0");

            // Crear y retornar el objeto Paquete
            LOGGER.fine("Creando objeto Paquete con número de seguimiento: " + rs.getString("num_seguimiento"));
            return new Paquete(
                    rs.getInt("id"),
                    rs.getString("num_seguimiento"),
                    rs.getString("email_cliente"),
                    cliente,
                    sucursal,
                    transportista,
                    rs.getString("destinatario"),
                    rs.getString("dir_destino"),
                    dimensiones,
                    rs.getDouble("costo"),
                    EstadoPaquete.valueOf(rs.getString("estado")),
                    rs.getTimestamp("fecha_creacion"),
                    rs.getTimestamp("fecha_estimada"),
                    rs.getInt("intentos_entrega"),
                    creador);
        } catch (SQLException e) {
            LOGGER.severe("Error mapeando ResultSet a Paquete");
            LOGGER.severe("Valores de ResultSet:");
            LOGGER.severe("ID: " + rs.getInt("id"));
            LOGGER.severe("Num. Seguimiento: " + rs.getString("num_seguimiento"));
            LOGGER.severe("Estado: " + rs.getString("estado"));
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Error inesperado mapeando ResultSet: " + e.getMessage());
            throw new SQLException("Error mapeando ResultSet", e);
        }
    }

    private Usuario crearUsuarioDesdeResultSet(ResultSet rs, String prefix) throws SQLException {
        return new Usuario(
                rs.getInt(prefix + "id"),
                rs.getString(prefix + "nombre"),
                rs.getString(prefix + "email"),
                "", // No necesitamos la contraseña
                "", // No necesitamos el teléfono
                null, // No necesitamos el rol
                null // No necesitamos la sucursal
        );
    }

    private Transportista crearTransportistaDesdeResultSet(ResultSet rs, Vehiculo vehiculo)
            throws SQLException {
        return new Transportista(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                "", // No necesitamos la contraseña
                rs.getString("telefono"),
                RolUsuario.TRANSPORTISTA,
                sucursal,
                rs.getString("licencia"),
                rs.getBoolean("disponible"),
                vehiculo);
    }

    /**
     * Valida que un paquete exista y pertenezca a la sucursal actual
     */
    private boolean validarPaquete(Connection conn, String numSeguimiento) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = """
                        SELECT 1 FROM paquete
                        WHERE num_seguimiento = ?
                        AND sucursal_origen_id = ?
                    """;

            stmt = conn.prepareStatement(query);
            stmt.setString(1, numSeguimiento);
            stmt.setInt(2, sucursal.getId());
            rs = stmt.executeQuery();

            return rs.next();
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }

    /**
     * Valida que un transportista esté disponible y pertenezca a la sucursal
     */
    private boolean validarTransportista(Connection conn, int transportistaId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = """
                        SELECT 1 FROM transportista
                        WHERE id = ?
                        AND sucursal_id = ?
                        AND disponible = true
                    """;

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, transportistaId);
            stmt.setInt(2, sucursal.getId());
            rs = stmt.executeQuery();

            return rs.next();
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }

    /**
     * Valida que un cambio de estado sea válido según el flujo de trabajo
     */
    private boolean validarCambioEstado(EstadoPaquete estadoActual, EstadoPaquete nuevoEstado) {
        if (estadoActual == null || nuevoEstado == null) {
            return false;
        }

        // Definir transiciones válidas
        switch (estadoActual) {
            case PENDIENTE:
                return nuevoEstado == EstadoPaquete.EN_TRANSITO;
            case EN_TRANSITO:
                return nuevoEstado == EstadoPaquete.ENTREGADO ||
                        nuevoEstado == EstadoPaquete.CANCELADO;
            case ENTREGADO:
                return false; // Estado final, no permite cambios
            case CANCELADO:
                return false; // Estado final, no permite cambios
            default:
                return false;
        }
    }

    /**
     * Valida el formato del número de seguimiento
     */
    private boolean validarNumeroSeguimiento(String numSeguimiento) {
        if (numSeguimiento == null || numSeguimiento.trim().isEmpty()) {
            return false;
        }
        // Formato esperado: PKG + 6 dígitos
        return numSeguimiento.matches("PKG\\d{6}");
    }

    /**
     * Formatea el estado del paquete para mostrar en la interfaz
     */
    public String formatearEstado(EstadoPaquete estado) {
        if (estado == null)
            return "";

        String estadoStr = estado.toString();
        String formateado = estadoStr.charAt(0) +
                estadoStr.substring(1).toLowerCase().replace('_', ' ');

        return formateado;
    }

    // Métodos auxiliares para manejo de volumen

    private double obtenerVolumenActual(Connection conn, int transportistaId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = """
                        SELECT v.capacidad_volumen
                        FROM vehiculo v
                        JOIN transportista t ON t.vehiculo_id = v.id
                        WHERE t.id = ?
                    """;

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, transportistaId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("capacidad_volumen");
            }
            return 0.0;
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }

    private void actualizarVolumenVehiculo(Connection conn, int transportistaId, double nuevoVolumen)
            throws SQLException {
        PreparedStatement stmt = null;

        try {
            String query = """
                        UPDATE vehiculo
                        JOIN transportista ON transportista.vehiculo_id = vehiculo.id
                        SET vehiculo.capacidad_volumen = ?
                        WHERE transportista.id = ?
                    """;

            stmt = conn.prepareStatement(query);
            stmt.setDouble(1, nuevoVolumen);
            stmt.setInt(2, transportistaId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    /**
     * Asigna transportistas a todos los paquetes pendientes
     * distribuidos según capacidad disponible
     */
    public int asignarTransportistasPendientes() throws SQLException {
        Connection conn = null;
        PreparedStatement stmtPaquetes = null;
        PreparedStatement stmtTransportistas = null;
        PreparedStatement stmtUpdate = null;
        PreparedStatement stmtLog = null;
        PreparedStatement stmtSucursalLog = null;
        ResultSet rsPaquetes = null;
        ResultSet rsTransportistas = null;
        int asignados = 0;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Obtener transportistas con vehículos
            String transportistasQuery = """
                        SELECT t.*, v.capacidad_volumen, u.nombre as transportista_nombre
                        FROM transportista t
                        JOIN vehiculo v ON t.vehiculo_id = v.id
                        JOIN usuario u ON t.usuario_id = u.id
                        WHERE t.sucursal_id = ?
                        AND t.disponible = true
                        ORDER BY v.capacidad_volumen DESC
                    """;

            stmtTransportistas = conn.prepareStatement(transportistasQuery);
            stmtTransportistas.setInt(1, sucursal.getId());
            rsTransportistas = stmtTransportistas.executeQuery();

            Map<Integer, TransportistaInfo> transportistasMap = new HashMap<>();
            while (rsTransportistas.next()) {
                int tId = rsTransportistas.getInt("id");
                transportistasMap.put(tId, new TransportistaInfo(
                        tId,
                        rsTransportistas.getString("transportista_nombre"),
                        rsTransportistas.getDouble("capacidad_volumen"),
                        obtenerVolumenActual(conn, tId)));
            }

            if (transportistasMap.isEmpty()) {
                throw new SQLException("No hay transportistas disponibles con vehículos");
            }

            // Obtener paquetes pendientes ordenados por volumen
            String paquetesQuery = """
                        SELECT id, num_seguimiento, dimensiones_peso
                        FROM paquete
                        WHERE estado = 'PENDIENTE'
                        AND transportista_id IS NULL
                        AND sucursal_origen_id = ?
                    """;

            stmtPaquetes = conn.prepareStatement(paquetesQuery);
            stmtPaquetes.setInt(1, sucursal.getId());
            rsPaquetes = stmtPaquetes.executeQuery();

            // Preparar statements
            String updateQuery = """
                        UPDATE paquete
                        SET transportista_id = ?, fecha_actualizacion = NOW()
                        WHERE id = ?
                    """;
            stmtUpdate = conn.prepareStatement(updateQuery);

            String logQuery = """
                        INSERT INTO log_transportista (
                            transportista_id, tipo_evento, paquete_id,
                            descripcion, fecha, metadata
                        ) VALUES (?, 'ASIGNACION', ?, ?, NOW(), '{}')
                    """;
            stmtLog = conn.prepareStatement(logQuery);

            // Asignar paquetes optimizando uso de capacidad
            while (rsPaquetes.next()) {
                int paqueteId = rsPaquetes.getInt("id");
                String numSeguimiento = rsPaquetes.getString("num_seguimiento");
                DimensionesPaquete dimPaquete = new DimensionesPaquete(
                        rsPaquetes.getString("dimensiones_peso"));
                double volumenPaquete = dimPaquete.getVolumen();

                // Buscar transportista óptimo
                TransportistaInfo mejorTransportista = null;
                double mejorAjuste = Double.MAX_VALUE;

                for (TransportistaInfo tInfo : transportistasMap.values()) {
                    double espacioDisponible = tInfo.capacidad - tInfo.volumenActual;
                    if (espacioDisponible >= volumenPaquete &&
                            espacioDisponible - volumenPaquete < mejorAjuste) {
                        mejorTransportista = tInfo;
                        mejorAjuste = espacioDisponible - volumenPaquete;
                    }
                }

                if (mejorTransportista == null) {
                    continue; // No hay transportista con capacidad suficiente
                }

                // Actualizar paquete
                stmtUpdate.setInt(1, mejorTransportista.id);
                stmtUpdate.setInt(2, paqueteId);
                stmtUpdate.executeUpdate();

                // Registrar en log
                stmtLog.setInt(1, mejorTransportista.id);
                stmtLog.setInt(2, paqueteId);
                stmtLog.setString(3, String.format(
                        "Asignación automática por %s. Volumen: %.2f m³",
                        usuario.getNombre(), volumenPaquete));
                stmtLog.executeUpdate();

                // Actualizar volumen actual
                mejorTransportista.volumenActual += volumenPaquete;
                asignados++;
            }

            // Log de sucursal
            if (asignados > 0) {
                String sucursalLogQuery = """
                            INSERT INTO log_sucursal (
                                sucursal_id, tipo_evento, descripcion,
                                fecha, metadata, usuario_id
                            ) VALUES (?, 'ACTUALIZACION', ?, NOW(), ?, ?)
                        """;

                stmtSucursalLog = conn.prepareStatement(sucursalLogQuery);
                stmtSucursalLog.setInt(1, sucursal.getId());
                stmtSucursalLog.setString(2, String.format(
                        "Asignación masiva de transportistas. %d paquetes asignados",
                        asignados));
                stmtSucursalLog.setString(3, "{}");
                stmtSucursalLog.setInt(4, usuario.getId());
                stmtSucursalLog.executeUpdate();
            }

            conn.commit();
            return asignados;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            // Cerrar recursos
            if (rsTransportistas != null)
                rsTransportistas.close();
            if (rsPaquetes != null)
                rsPaquetes.close();
            if (stmtLog != null)
                stmtLog.close();
            if (stmtUpdate != null)
                stmtUpdate.close();
            if (stmtTransportistas != null)
                stmtTransportistas.close();
            if (stmtPaquetes != null)
                stmtPaquetes.close();
            if (stmtSucursalLog != null)
                stmtSucursalLog.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private String crearMetadataJSON(Object... pares) {
        if (pares.length % 2 != 0) {
            throw new IllegalArgumentException("Debe proporcionar pares clave-valor");
        }

        StringBuilder json = new StringBuilder("{");
        for (int i = 0; i < pares.length; i += 2) {
            if (i > 0) {
                json.append(",");
            }
            String key = pares[i].toString();
            Object value = pares[i + 1];

            json.append("\"").append(key).append("\":");

            if (value == null) {
                json.append("null");
            } else if (value instanceof Number) {
                json.append(value.toString());
            } else if (value instanceof Boolean) {
                json.append(value.toString());
            } else {
                json.append("\"").append(value.toString().replace("\"", "\\\"")).append("\"");
            }
        }
        json.append("}");
        return json.toString();
    }

    public void asignarTransportista(String numSeguimiento, Transportista transportista)
            throws SQLException {

        // Validaciones iniciales
        if (numSeguimiento == null || transportista == null) {
            throw new IllegalArgumentException("Número de seguimiento y transportista no pueden ser nulos");
        }

        if (transportista.getVehiculo() == null) {
            throw new SQLException("El transportista no tiene vehículo asignado");
        }

        Connection conn = null;
        PreparedStatement stmtPaquete = null;
        PreparedStatement stmtLogPaquete = null;
        PreparedStatement stmtLogTransportista = null;
        PreparedStatement stmtSucursalLog = null;
        PreparedStatement stmtValidacion = null;
        ResultSet rsValidacion = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Validar paquete y obtener datos necesarios
            String validacionQuery = """
                        SELECT p.id, p.estado, p.dimensiones_peso
                        FROM paquete p
                        WHERE p.num_seguimiento = ?
                        AND p.sucursal_origen_id = ?
                    """;

            stmtValidacion = conn.prepareStatement(validacionQuery);
            stmtValidacion.setString(1, numSeguimiento);
            stmtValidacion.setInt(2, sucursal.getId());
            rsValidacion = stmtValidacion.executeQuery();

            if (!rsValidacion.next()) {
                throw new SQLException("Paquete no encontrado o no pertenece a esta sucursal");
            }

            int paqueteId = rsValidacion.getInt("id");
            String estadoActual = rsValidacion.getString("estado");

            if (!estadoActual.equals("PENDIENTE")) {
                throw new SQLException("El paquete debe estar en estado PENDIENTE para asignar transportista");
            }

            // 2. Validar capacidad del vehículo
            DimensionesPaquete dimPaquete = new DimensionesPaquete(rsValidacion.getString("dimensiones_peso"));
            double volumenPaquete = dimPaquete.getVolumen();
            double volumenActual = obtenerVolumenActual(conn, transportista.getId());

            if (volumenActual + volumenPaquete > VOLUMEN_MAXIMO_VEHICULO) {
                throw new SQLException(String.format(
                        "El vehículo excederá su capacidad máxima. Máximo: %.2f m³, Actual: %.2f m³, Paquete: %.2f m³",
                        VOLUMEN_MAXIMO_VEHICULO, volumenActual, volumenPaquete));
            }

            // 3. Actualizar volumen del vehículo
            double nuevoVolumen = volumenActual + volumenPaquete;
            actualizarVolumenVehiculo(conn, transportista.getId(), nuevoVolumen);

            // 4. Actualizar paquete
            String updateQuery = """
                        UPDATE paquete
                        SET transportista_id = ?,
                            estado = 'EN_TRANSITO',
                            fecha_actualizacion = NOW()
                        WHERE id = ?
                    """;

            stmtPaquete = conn.prepareStatement(updateQuery);
            stmtPaquete.setInt(1, transportista.getId());
            stmtPaquete.setInt(2, paqueteId);
            stmtPaquete.executeUpdate();

            // 5. Registrar cambio de estado en log_paquete
            String logPaqueteQuery = """
                        INSERT INTO log_paquete (
                            paquete_id, estado_anterior, estado_nuevo,
                            descripcion, fecha, metadata, usuario_id
                        ) VALUES (?, ?, ?, ?, NOW(), ?, ?)
                    """;

            String metadataPaquete = crearMetadataJSON(
                    "transportista_id", transportista.getId(),
                    "volumen", volumenPaquete);

            stmtLogPaquete = conn.prepareStatement(logPaqueteQuery);
            stmtLogPaquete.setInt(1, paqueteId);
            stmtLogPaquete.setString(2, "PENDIENTE");
            stmtLogPaquete.setString(3, "EN_TRANSITO");
            stmtLogPaquete.setString(4, "Asignación de transportista y cambio de estado");
            stmtLogPaquete.setString(5, metadataPaquete);
            stmtLogPaquete.setInt(6, usuario.getId());
            stmtLogPaquete.executeUpdate();

            // 6. Registrar en log_transportista
            String logTransportistaQuery = """
                        INSERT INTO log_transportista (
                            transportista_id, tipo_evento, paquete_id,
                            descripcion, fecha, metadata
                        ) VALUES (?, 'ASIGNACION', ?, ?, NOW(), ?)
                    """;

            String metadataTransportista = crearMetadataJSON(
                    "volumen_paquete", volumenPaquete,
                    "volumen_actual", nuevoVolumen,
                    "volumen_maximo", VOLUMEN_MAXIMO_VEHICULO);

            stmtLogTransportista = conn.prepareStatement(logTransportistaQuery);
            stmtLogTransportista.setInt(1, transportista.getId());
            stmtLogTransportista.setInt(2, paqueteId);
            stmtLogTransportista.setString(3, String.format(
                    "Asignado por %s. Volumen paquete: %.2f m³, Volumen acumulado: %.2f m³",
                    usuario.getNombre(), volumenPaquete, nuevoVolumen));
            stmtLogTransportista.setString(4, metadataTransportista);
            stmtLogTransportista.executeUpdate();

            // 7. Log de sucursal
            String logSucursalQuery = """
                        INSERT INTO log_sucursal (
                            sucursal_id, tipo_evento, descripcion,
                            fecha, metadata, usuario_id
                        ) VALUES (?, 'ACTUALIZACION', ?, NOW(), ?, ?)
                    """;

            String metadataSucursal = crearMetadataJSON(
                    "paquete_id", paqueteId,
                    "transportista_id", transportista.getId(),
                    "volumen_actual", nuevoVolumen);

            stmtSucursalLog = conn.prepareStatement(logSucursalQuery);
            stmtSucursalLog.setInt(1, sucursal.getId());
            stmtSucursalLog.setString(2, String.format(
                    "Paquete %s asignado a transportista %s. Volumen usado: %.2f/%.2f m³",
                    numSeguimiento, transportista.getNombre(),
                    nuevoVolumen, VOLUMEN_MAXIMO_VEHICULO));
            stmtSucursalLog.setString(3, metadataSucursal);
            stmtSucursalLog.setInt(4, usuario.getId());
            stmtSucursalLog.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.severe("Error en rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            // Cerrar recursos en orden inverso
            if (rsValidacion != null)
                rsValidacion.close();
            if (stmtValidacion != null)
                stmtValidacion.close();
            if (stmtSucursalLog != null)
                stmtSucursalLog.close();
            if (stmtLogTransportista != null)
                stmtLogTransportista.close();
            if (stmtLogPaquete != null)
                stmtLogPaquete.close();
            if (stmtPaquete != null)
                stmtPaquete.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // Clase auxiliar para tracking de transportistas
    private static class TransportistaInfo {
        final int id;
        final String nombre;
        final double capacidad;
        double volumenActual;

        TransportistaInfo(int id, String nombre, double capacidad, double volumenActual) {
            this.id = id;
            this.nombre = nombre;
            this.capacidad = capacidad;
            this.volumenActual = volumenActual;
        }
    }

}