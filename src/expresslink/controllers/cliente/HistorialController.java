package expresslink.controllers.cliente;

import expresslink.model.Usuario;
import expresslink.utils.DatabaseConnection;
import expresslink.view.cliente.HistorialPanel.HistorialItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialController {
    private Usuario usuario;

    public HistorialController(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<HistorialItem> obtenerHistorial() throws SQLException {
        List<HistorialItem> historial = new ArrayList<>();

        String query = "SELECT num_seguimiento, destinatario, dir_destino, fecha_estimada, costo " +
                "FROM paquete " +
                "WHERE cliente_id = ? AND estado = 'ENTREGADO' " +
                "ORDER BY fecha_estimada DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, usuario.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historial.add(new HistorialItem(
                            rs.getString("num_seguimiento"),
                            rs.getString("destinatario"),
                            rs.getString("dir_destino"),
                            rs.getTimestamp("fecha_estimada"),
                            rs.getDouble("costo")));
                }
            }
        }

        return historial;
    }
}