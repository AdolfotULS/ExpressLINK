package expresslink.controllers.transportista;

import java.sql.*;
import expresslink.model.enums.EstadoPaquete;
import expresslink.utils.DatabaseConnection;
import expresslink.view.transportista.TransportistaDashboard;

public class TarjetaPedidoController {
    private String pedidoId;
    private TransportistaDashboard dashboard;

    public TarjetaPedidoController(String pedidoId, TransportistaDashboard dashboard) {
        this.pedidoId = pedidoId;
        this.dashboard = dashboard;
    }

    public boolean entregarPedido() throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Obtener datos del paquete
            String infoQuery = "SELECT p.id, p.destinatario, p.dir_destino, p.intentos_entrega " +
                    "FROM paquete p WHERE p.num_seguimiento = ?";
            int paqueteId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(infoQuery)) {
                stmt.setString(1, pedidoId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    paqueteId = rs.getInt("id");

                    // Agregar entrada al historial
                    dashboard.addHistoryEntry(
                            pedidoId,
                            rs.getString("destinatario"),
                            rs.getString("dir_destino"),
                            "Entregado",
                            java.time.LocalTime.now().toString().substring(0, 5),
                            rs.getInt("intentos_entrega"),
                            true);
                }
            }

            // 2. Actualizar estado del paquete
            String updateQuery = "UPDATE paquete SET estado = 'ENTREGADO' WHERE num_seguimiento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, pedidoId);
                int updated = stmt.executeUpdate();
                if (updated == 0)
                    return false;
            }

            // 3. Insertar en log_paquete
            String logQuery = "INSERT INTO log_paquete (paquete_id, estado_anterior, estado_nuevo, descripcion, fecha) "
                    +
                    "VALUES (?, 'EN_TRANSITO', 'ENTREGADO', 'Paquete entregado al destinatario', NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(logQuery)) {
                stmt.setInt(1, paqueteId);
                stmt.executeUpdate();
            }

            // 4. Registrar log_transportista
            String transportistaQuery = "INSERT INTO log_transportista (transportista_id, tipo_evento, paquete_id, descripcion, fecha, metadata) "
                    +
                    "SELECT t.id, 'FINALIZACION', ?, 'Entrega completada', NOW(), '{}' " +
                    "FROM paquete p " +
                    "JOIN transportista t ON p.transportista_id = t.id " +
                    "WHERE p.num_seguimiento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(transportistaQuery)) {
                stmt.setInt(1, paqueteId);
                stmt.setString(2, pedidoId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        }
    }

    // En TarjetaPedidoController.java

    public boolean cancelarEntrega(String motivo) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Obtener datos del paquete
            String infoQuery = "SELECT p.id, p.destinatario, p.dir_destino, p.intentos_entrega " +
                    "FROM paquete p WHERE p.num_seguimiento = ?";
            int paqueteId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(infoQuery)) {
                stmt.setString(1, pedidoId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    paqueteId = rs.getInt("id");

                    // Agregar entrada al historial
                    dashboard.addHistoryEntry(
                            pedidoId,
                            rs.getString("destinatario"),
                            rs.getString("dir_destino"),
                            "No entregado - " + motivo,
                            java.time.LocalTime.now().toString().substring(0, 5),
                            rs.getInt("intentos_entrega") + 1, // +1 porque se incrementará
                            false);
                }
            }

            // 2. Actualizar estado del paquete y limpiar transportista
            String updateQuery = "UPDATE paquete SET " +
                    "estado = 'PENDIENTE', " +
                    "transportista_id = NULL, " + // Limpiar asignación
                    "intentos_entrega = intentos_entrega + 1 " +
                    "WHERE num_seguimiento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, pedidoId);
                int updated = stmt.executeUpdate();
                if (updated == 0)
                    return false;
            }

            // 3. Insertar en log_paquete
            String logPaqueteQuery = "INSERT INTO log_paquete (paquete_id, estado_anterior, estado_nuevo, descripcion, fecha) "
                    +
                    "VALUES (?, 'EN_TRANSITO', 'PENDIENTE', ?, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(logPaqueteQuery)) {
                stmt.setInt(1, paqueteId);
                stmt.setString(2, "Entrega cancelada: " + motivo);
                stmt.executeUpdate();
            }

            // 4. Registrar log_transportista
            String logTransportistaQuery = "INSERT INTO log_transportista (transportista_id, tipo_evento, paquete_id, descripcion, fecha, metadata) "
                    +
                    "SELECT t.id, 'FINALIZACION', ?, ?, NOW(), ? " +
                    "FROM paquete p " +
                    "JOIN transportista t ON p.transportista_id = t.id " +
                    "WHERE p.num_seguimiento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(logTransportistaQuery)) {
                stmt.setInt(1, paqueteId);
                stmt.setString(2, "Entrega cancelada: " + motivo);
                stmt.setString(3, String.format("{\"motivo\": \"%s\"}", motivo.replace("\"", "\\\"")));
                stmt.setString(4, pedidoId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        }
    }
}