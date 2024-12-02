package expresslink.controllers.cliente;

import expresslink.model.*;
import expresslink.utils.DatabaseConnection;

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
                "WHERE (cliente_id = ? OR email_cliente = ?) " +
                "AND estado = 'ENTREGADO' AND estado = 'CANCELADO'" +
                "ORDER BY fecha_estimada DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            // Establecer ambos parámetros
            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getEmail());

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

    // Método alternativo que acepta búsqueda específica por email
    public List<HistorialItem> obtenerHistorialPorEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        List<HistorialItem> historial = new ArrayList<>();
        String query = "SELECT num_seguimiento, destinatario, dir_destino, fecha_estimada, costo " +
                "FROM paquete " +
                "WHERE email_cliente = ? AND estado = 'ENTREGADO' " +
                "ORDER BY fecha_estimada DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

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