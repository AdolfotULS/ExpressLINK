package expresslink.controllers.cliente;

import java.sql.*;
import java.util.Date;
import expresslink.model.*;
import expresslink.model.enums.*;
import expresslink.utils.DatabaseConnection;

public class SeguimientoController {

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

    public DatosSeguimiento buscarPedido(String numeroSeguimiento) throws SQLException {
        String query = "SELECT paquete.estado, paquete.sucursal_origen_id, paquete.dir_destino, paquete.fecha_estimada FROM paquete WHERE paquete.num_seguimiento = ?";

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

    private int calcularPosicionEstado(EstadoPaquete estado) {
        return switch (estado) {
            case PENDIENTE -> 0;
            case EN_TRANSITO -> 1;
            case ENTREGADO -> 2;
            default -> 0;
        };
    }

    public boolean existePedido(String numeroSeguimiento) throws SQLException {
        String query = "SELECT COUNT(*) FROM pedido WHERE num_seguimiento = ?";

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

    // public DatosSeguimiento obtenerUltimoPedido(int clienteId) throws
    // SQLException {
    // String query = """
    // SELECT p.num_seguimiento, p.estado, p.fecha_estimada,
    // so.nombre as sucursal_origen,
    // sd.nombre as sucursal_destino
    // FROM pedido p
    // JOIN sucursal so ON p.sucursal_origen_id = so.id
    // JOIN sucursal sd ON p.sucursal_destino_id = sd.id
    // WHERE p.cliente_id = ?
    // ORDER BY p.fecha_creacion DESC
    // LIMIT 1
    // """;

    // try (Connection conn = DatabaseConnection.getConnection();
    // PreparedStatement stmt = conn.prepareStatement(query)) {

    // stmt.setInt(1, clienteId);
    // ResultSet rs = stmt.executeQuery();

    // if (rs.next()) {
    // return new DatosSeguimiento(
    // rs.getString("num_seguimiento"),
    // EstadoPedido.valueOf(rs.getString("estado")),
    // rs.getString("sucursal_origen"),
    // rs.getString("sucursal_destino"),
    // rs.getTimestamp("fecha_estimada"));
    // }

    // return null;
    // }
    // }
}