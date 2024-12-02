package expresslink.controllers.cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import expresslink.model.*;
import expresslink.utils.DatabaseConnection;

public class MisPedidosController {
    private Usuario usuario;

    public MisPedidosController(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<PedidoItem> obtenerMisPedidos() throws SQLException {
        List<PedidoItem> pedidos = new ArrayList<>();

        String query = "SELECT p.num_seguimiento, p.destinatario, p.dir_destino, " +
                "p.estado, p.fecha_creacion, p.fecha_estimada, p.intentos_entrega, " +
                "t.id as transportista_id, " +
                "u.nombre as transportista_nombre, u.telefono as transportista_telefono, " +
                "s.nombre as sucursal_nombre, s.direccion as sucursal_direccion " +
                "FROM paquete p " +
                "LEFT JOIN transportista t ON p.transportista_id = t.id " +
                "LEFT JOIN usuario u ON t.usuario_id = u.id " +
                "LEFT JOIN sucursal s ON p.sucursal_origen_id = s.id " +
                "WHERE (p.cliente_id = ? OR p.email_cliente = ?) " +
                "AND p.estado != 'ENTREGADO'AND p.estado != 'CANCELADO' " +
                "ORDER BY p.fecha_creacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getEmail());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PedidoItem pedido = new PedidoItem(
                            rs.getString("num_seguimiento"),
                            rs.getString("destinatario"),
                            rs.getString("dir_destino"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_estimada"),
                            rs.getInt("intentos_entrega"),
                            rs.getString("sucursal_nombre"),
                            rs.getString("sucursal_direccion"));

                    // Agregar datos del transportista si existe
                    if (rs.getObject("transportista_id") != null) {
                        pedido.setTransportista(
                                rs.getString("transportista_nombre"),
                                rs.getString("transportista_telefono"));
                    }

                    pedidos.add(pedido);
                }
            }
        }
        return pedidos;
    }
}