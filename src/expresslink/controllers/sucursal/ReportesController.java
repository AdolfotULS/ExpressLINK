package expresslink.controllers.sucursal;

import expresslink.model.*;
import expresslink.model.enums.*;
import expresslink.utils.DatabaseConnection;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportesController {
    private Sucursal sucursal;

    public ReportesController(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }
        this.sucursal = sucursal;
    }

    public List<LogSucursal> obtenerLogSucursal() {
        List<LogSucursal> logs = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                    SELECT ls.*, s.nombre as sucursal_nombre, s.direccion, s.ciudad,
                           u.nombre as usuario_nombre, u.email as usuario_email
                    FROM log_sucursal ls
                    JOIN sucursal s ON ls.sucursal_id = s.id
                    JOIN usuario u ON ls.usuario_id = u.id
                    WHERE ls.sucursal_id = ?
                    AND DATE(ls.fecha) = CURDATE()
                    ORDER BY ls.fecha DESC
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, sucursal.getId());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Sucursal suc = new Sucursal(
                            rs.getInt("sucursal_id"),
                            rs.getString("sucursal_nombre"),
                            rs.getString("direccion"),
                            rs.getString("ciudad"));

                    Usuario user = new Usuario(
                            rs.getInt("usuario_id"),
                            rs.getString("usuario_nombre"),
                            rs.getString("usuario_email"),
                            null, null, null);

                    LogSucursal log = new LogSucursal(
                            rs.getInt("id"),
                            suc,
                            LogSucursal.TipoEvento.valueOf(rs.getString("tipo_evento")),
                            rs.getString("descripcion"),
                            rs.getDate("fecha"),
                            rs.getString("metadata"),
                            user);
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener logs de sucursal: " + e.getMessage());
        }
        return logs;
    }

    public List<LogTransportista> obtenerLogTransportista() {
        List<LogTransportista> logs = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                    SELECT lt.*,
                           t.licencia,
                           t.disponible,
                           u.nombre AS transportista_nombre,
                           u.email AS transportista_email,
                           v.id AS vehiculo_id,
                           v.patente,
                           v.capacidad_volumen,
                           p.num_seguimiento
                    FROM log_transportista lt
                    JOIN transportista t ON lt.transportista_id = t.id
                    JOIN usuario u ON t.usuario_id = u.id
                    LEFT JOIN vehiculo v ON t.vehiculo_id = v.id
                    JOIN paquete p ON lt.paquete_id = p.id
                    WHERE t.sucursal_id = ?
                      AND DATE(lt.fecha) = CURDATE()
                    ORDER BY lt.fecha DESC;
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, sucursal.getId());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Vehiculo vehiculo = new Vehiculo(
                            rs.getInt("vehiculo_id"),
                            rs.getString("patente"),
                            rs.getString("capacidad_volumen"));

                    Usuario userTransportista = new Usuario(
                            rs.getInt("transportista_id"),
                            rs.getString("transportista_nombre"),
                            rs.getString("transportista_email"),
                            null, null, null);

                    Transportista transportista = new Transportista(
                            rs.getInt("transportista_id"),
                            rs.getString("transportista_nombre"),
                            rs.getString("transportista_email"),
                            null, null, null,
                            sucursal,
                            rs.getString("licencia"),
                            rs.getBoolean("disponible"),
                            vehiculo);

                    Paquete paquete = new Paquete(rs.getInt("paquete_id"));
                    paquete.setNumSeguimiento(rs.getString("num_seguimiento"));

                    LogTransportista log = new LogTransportista(
                            rs.getInt("id"),
                            transportista,
                            LogTransportista.TipoEvento.valueOf(rs.getString("tipo_evento")),
                            paquete,
                            rs.getString("descripcion"),
                            rs.getDate("fecha"),
                            rs.getString("metadata"));
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener logs de transportista: " + e.getMessage());
        }
        return logs;
    }

    public List<LogPaquete> obtenerLogPaquete() {
        List<LogPaquete> logs = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                    SELECT lp.*, p.num_seguimiento,
                           u.nombre as usuario_nombre, u.email as usuario_email
                    FROM log_paquete lp
                    JOIN paquete p ON lp.paquete_id = p.id
                    JOIN usuario u ON lp.usuario_id = u.id
                    WHERE p.sucursal_origen_id = ?
                    AND DATE(lp.fecha) = CURDATE()
                    ORDER BY lp.fecha DESC
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, sucursal.getId());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Usuario user = new Usuario(
                            rs.getInt("usuario_id"),
                            rs.getString("usuario_nombre"),
                            rs.getString("usuario_email"),
                            null, null, null);

                    Paquete paquete = new Paquete(rs.getInt("paquete_id"));
                    paquete.setNumSeguimiento(rs.getString("num_seguimiento"));

                    LogPaquete log = new LogPaquete(
                            rs.getInt("id"),
                            paquete,
                            EstadoPaquete.valueOf(rs.getString("estado_anterior")),
                            EstadoPaquete.valueOf(rs.getString("estado_nuevo")),
                            rs.getString("descripcion"),
                            rs.getDate("fecha"),
                            rs.getString("metadata"),
                            user);
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener logs de paquete: " + e.getMessage());
        }
        return logs;
    }
}