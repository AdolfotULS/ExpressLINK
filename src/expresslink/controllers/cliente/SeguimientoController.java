package expresslink.controllers.cliente;

import java.sql.*;
import java.util.Date;
import expresslink.model.*;
import expresslink.model.enums.EstadoPedido;
import expresslink.utils.DatabaseConnection;

public class SeguimientoController {

    public class DatosSeguimiento {
        public String numeroSeguimiento;
        public EstadoPedido estado;
        public String sucursalOrigen;
        public String sucursalDestino;
        public Date fechaEstimada;
        public int posicionEstado; // 0-4 para el tracker

        public DatosSeguimiento(String numeroSeguimiento, EstadoPedido estado,
                String sucursalOrigen, String sucursalDestino,
                Date fechaEstimada) {
            this.numeroSeguimiento = numeroSeguimiento;
            this.estado = estado;
            this.sucursalOrigen = sucursalOrigen;
            this.sucursalDestino = sucursalDestino;
            this.fechaEstimada = fechaEstimada;
            this.posicionEstado = calcularPosicionEstado(estado);
        }
    }

    public DatosSeguimiento buscarPedido(String numeroSeguimiento) throws SQLException {
        String query = """
                    SELECT p.num_seguimiento, p.estado, p.fecha_estimada,
                           so.nombre as sucursal_origen,
                           sd.nombre as sucursal_destino
                    FROM pedido p
                    JOIN sucursal so ON p.sucursal_origen_id = so.id
                    JOIN sucursal sd ON p.sucursal_destino_id = sd.id
                    WHERE p.num_seguimiento = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroSeguimiento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new DatosSeguimiento(
                        rs.getString("num_seguimiento"),
                        EstadoPedido.valueOf(rs.getString("estado")),
                        rs.getString("sucursal_origen"),
                        rs.getString("sucursal_destino"),
                        rs.getTimestamp("fecha_estimada"));
            }

            return null;
        }
    }

    private int calcularPosicionEstado(EstadoPedido estado) {
        return switch (estado) {
            case INGRESADO -> 0;
            case RECOLECTANDO -> 1;
            case EN_TRANSITO -> 2;
            case EN_SURCURSAL_DESTINO -> 3;
            case ENTREGADO -> 4;
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

    public DatosSeguimiento obtenerUltimoPedido(int clienteId) throws SQLException {
        String query = """
                    SELECT p.num_seguimiento, p.estado, p.fecha_estimada,
                           so.nombre as sucursal_origen,
                           sd.nombre as sucursal_destino
                    FROM pedido p
                    JOIN sucursal so ON p.sucursal_origen_id = so.id
                    JOIN sucursal sd ON p.sucursal_destino_id = sd.id
                    WHERE p.cliente_id = ?
                    ORDER BY p.fecha_creacion DESC
                    LIMIT 1
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new DatosSeguimiento(
                        rs.getString("num_seguimiento"),
                        EstadoPedido.valueOf(rs.getString("estado")),
                        rs.getString("sucursal_origen"),
                        rs.getString("sucursal_destino"),
                        rs.getTimestamp("fecha_estimada"));
            }

            return null;
        }
    }
}