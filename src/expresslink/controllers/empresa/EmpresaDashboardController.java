package expresslink.controllers.empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import expresslink.utils.DatabaseConnection;

public class EmpresaDashboardController {
    public double obtenerIngresosTotales() throws SQLException {
        String query = """
                SELECT SUM(balance_actual) as total_balance,
                       SUM(ingresos_periodo) as total_ingresos
                FROM balance_sucursal
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_ingresos");
            }
            return 0.0;
        }
    }

    public int obtenerTotalTransportistas() throws SQLException {
        String query = """
                SELECT COUNT(*) as total
                FROM transportista t
                JOIN usuario u ON t.usuario_id = u.id
                WHERE u.rol = 'TRANSPORTISTA'
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    public int obtenerTotalClientes() throws SQLException {
        String query = """
                SELECT COUNT(*) as total
                FROM usuario
                WHERE rol = 'CLIENTE'
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    public int obtenerTotalPedidos() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM paquete";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    public int obtenerPedidosEnTransito() throws SQLException {
        String query = """
                SELECT COUNT(*) as total
                FROM paquete
                WHERE estado = 'EN_TRANSITO'
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    // Método para obtener datos históricos de ingresos por sucursal
    public ResultSet obtenerHistoricoIngresosPorSucursal() throws SQLException {
        String query = """
                SELECT s.nombre as sucursal, bs.ingresos_periodo, bs.fecha_actualizacion
                FROM balance_sucursal bs
                JOIN sucursal s ON bs.sucursal_id = s.id
                ORDER BY s.nombre, bs.fecha_actualizacion
                """;

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.executeQuery();
    }
}
