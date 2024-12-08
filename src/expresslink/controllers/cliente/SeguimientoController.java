package expresslink.controllers.cliente;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import expresslink.model.*;
import expresslink.model.enums.*;
import expresslink.utils.DatabaseConnection;
import expresslink.view.cliente.SeguimientoPaquete;

public class SeguimientoController {
    private SeguimientoPaquete dashboard;

    public SeguimientoController(SeguimientoPaquete dashboard) {
        this.dashboard = dashboard;
    }

    public class DatosSeguimiento {
        public String numeroSeguimiento;
        public EstadoPaquete estado;
        public String sucursalOrigen;
        public String lugarDestino;
        public Date fechaEstimada;
        public int posicionEstado; // 0-4 para el tracker

        public DatosSeguimiento(String numeroSeguimiento, EstadoPaquete estado,
                String sucursalOrigen, String sucursalDestino,
                Date fechaEstimada) {
            this.numeroSeguimiento = numeroSeguimiento;
            this.estado = estado;
            this.sucursalOrigen = sucursalOrigen;
            this.lugarDestino = sucursalDestino;
            this.fechaEstimada = fechaEstimada;
            this.posicionEstado = calcularPosicionEstado(estado);
        }
    }

    public class LogPaqueteData {
        public Date fecha;
        public EstadoPaquete estadoAnterior;
        public EstadoPaquete estadoNuevo;
        public String descripcion;

        public LogPaqueteData(Date fecha, EstadoPaquete estadoAnterior,
                EstadoPaquete estadoNuevo, String descripcion) {
            this.fecha = fecha;
            this.estadoAnterior = estadoAnterior;
            this.estadoNuevo = estadoNuevo;
            this.descripcion = descripcion;
        }
    }

    public DatosSeguimiento buscarPedido(String numeroSeguimiento) throws SQLException {
        String query = "SELECT paquete.estado, paquete.sucursal_origen_id, " +
                "paquete.dir_destino, paquete.fecha_estimada " +
                "FROM paquete WHERE paquete.num_seguimiento = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroSeguimiento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new DatosSeguimiento(
                        numeroSeguimiento,
                        EstadoPaquete.valueOf(rs.getString("estado")),
                        rs.getString("sucursal_origen_id"),
                        rs.getString("dir_destino"),
                        rs.getTimestamp("fecha_estimada"));
            }
            return null;
        }
    }

    public List<LogPaqueteData> obtenerHistorialPedido(String numeroSeguimiento) throws SQLException {
        List<LogPaqueteData> historial = new ArrayList<>();

        // Primero obtenemos el ID del paquete
        String queryId = "SELECT id FROM paquete WHERE num_seguimiento = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtId = conn.prepareStatement(queryId)) {

            stmtId.setString(1, numeroSeguimiento);
            ResultSet rsId = stmtId.executeQuery();

            if (rsId.next()) {
                int paqueteId = rsId.getInt("id");

                // Ahora obtenemos el historial
                String queryLog = "SELECT fecha, estado_anterior, estado_nuevo, descripcion " +
                        "FROM log_paquete " +
                        "WHERE paquete_id = ? " +
                        "ORDER BY fecha DESC";

                try (PreparedStatement stmtLog = conn.prepareStatement(queryLog)) {
                    stmtLog.setInt(1, paqueteId);
                    ResultSet rsLog = stmtLog.executeQuery();

                    while (rsLog.next()) {
                        historial.add(new LogPaqueteData(
                                rsLog.getTimestamp("fecha"),
                                EstadoPaquete.valueOf(rsLog.getString("estado_anterior")),
                                EstadoPaquete.valueOf(rsLog.getString("estado_nuevo")),
                                rsLog.getString("descripcion")));
                    }
                }
            }
        }

        return historial;
    }

    private int calcularPosicionEstado(EstadoPaquete estado) {
        return switch (estado) {
            case PENDIENTE -> 0;
            case EN_TRANSITO -> 1;
            case ENTREGADO -> 2;
            default -> 0;
        };
    }

    public boolean existePedido(String numeroSeguimiento) throws SQLException {
        String query = "SELECT COUNT(*) FROM paquete WHERE num_seguimiento = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroSeguimiento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}