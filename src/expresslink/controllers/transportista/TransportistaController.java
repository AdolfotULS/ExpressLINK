package expresslink.controllers.transportista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import expresslink.model.*;
import expresslink.model.enums.EstadoPaquete;
import expresslink.model.enums.RolUsuario;
import expresslink.utils.DatabaseConnection;
import expresslink.view.transportista.TarjetaPedido;
// En TransportistaDashboard.java
import java.util.List;

// En TransportistaController.java 
import java.util.List;
import java.util.ArrayList;

public class TransportistaController {
    private Usuario usuario;

    public TransportistaController(Usuario usuario) {
        this.usuario = usuario;
    }

    public Transportista obtenerDatosTransportista() throws SQLException {
        String query = "SELECT t.id, t.licencia, t.disponible, " +
                "s.id as sucursal_id, s.nombre as sucursal_nombre, s.direccion as sucursal_direccion, s.ciudad as sucursal_ciudad, "
                +
                "v.id as vehiculo_id, v.patente, v.capacidad_volumen " +
                "FROM transportista t " +
                "JOIN usuario u ON t.usuario_id = u.id " +
                "LEFT JOIN sucursal s ON t.sucursal_id = s.id " +
                "LEFT JOIN vehiculo v ON t.vehiculo_id = v.id " +
                "WHERE u.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, usuario.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Crear objeto Sucursal si existe
                Sucursal sucursal = null;
                if (rs.getObject("sucursal_id") != null) {
                    sucursal = new Sucursal(
                            rs.getInt("sucursal_id"),
                            rs.getString("sucursal_nombre"),
                            rs.getString("sucursal_direccion"),
                            rs.getString("sucursal_ciudad"));
                }

                // Crear objeto Vehiculo si existe
                Vehiculo vehiculo = null;
                if (rs.getObject("vehiculo_id") != null) {
                    vehiculo = new Vehiculo(
                            rs.getInt("vehiculo_id"),
                            rs.getString("patente"),
                            rs.getDouble("capacidad_volumen"));
                }

                // Crear objeto Transportista con todos los datos
                return new Transportista(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getPassword(),
                        usuario.getTelefono(),
                        RolUsuario.TRANSPORTISTA,
                        sucursal,
                        rs.getString("licencia"),
                        rs.getBoolean("disponible"),
                        vehiculo);
            }
            return null;
        }
    }

    public void actualizarDisponibilidad(boolean disponible) throws SQLException {
        String query = "UPDATE transportista t " +
                "JOIN usuario u ON t.usuario_id = u.id " +
                "SET t.disponible = ? " +
                "WHERE u.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, disponible);
            stmt.setInt(2, usuario.getId());
            stmt.executeUpdate();
        }
    }

    public void registrarEntregaPedido(String numSeguimiento) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Obtener datos del paquete
            Paquete paquete = obtenerPaquete(conn, numSeguimiento);
            if (paquete == null) {
                throw new SQLException("No se encontró el paquete");
            }

            // 2. Actualizar estado del paquete
            String updateQuery = "UPDATE paquete SET estado = ? WHERE num_seguimiento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, EstadoPaquete.ENTREGADO.toString());
                stmt.setString(2, numSeguimiento);
                stmt.executeUpdate();
            }

            // 3. Registrar en log_paquete
            LogPaquete logPaquete = new LogPaquete(
                    0, // ID autogenerado
                    paquete,
                    EstadoPaquete.EN_TRANSITO,
                    EstadoPaquete.ENTREGADO,
                    "Paquete entregado al destinatario",
                    new java.sql.Date(System.currentTimeMillis()),
                    "{}", // metadata vacía
                    usuario);
            registrarLogPaquete(conn, logPaquete);

            // 4. Registrar en log_transportista
            LogTransportista logTransportista = new LogTransportista(
                    0, // ID autogenerado
                    obtenerDatosTransportista(),
                    LogTransportista.TipoEvento.FINALIZACION,
                    paquete,
                    "Entrega completada",
                    new java.sql.Date(System.currentTimeMillis()),
                    "{}" // metadata vacía
            );
            registrarLogTransportista(conn, logTransportista);

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
        }
    }

    private Paquete obtenerPaquete(Connection conn, String numSeguimiento) throws SQLException {
        String query = "SELECT id, cliente_id, estado FROM paquete WHERE num_seguimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, numSeguimiento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Retornar objeto Paquete mínimo necesario para los logs
                return new Paquete(rs.getInt("id"));
            }
            return null;
        }
    }

    public List<TarjetaPedido> obtenerPedidosAsignados() throws SQLException {
        List<TarjetaPedido> pedidos = new ArrayList<>();

        String query = "SELECT p.num_seguimiento, p.destinatario, p.dir_destino " +
                "FROM paquete p " +
                "JOIN transportista t ON p.transportista_id = t.id " +
                "JOIN usuario u ON t.usuario_id = u.id " +
                "WHERE u.id = ? AND p.estado = 'EN_TRANSITO'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, usuario.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TarjetaPedido tarjeta = new TarjetaPedido(
                        rs.getString("num_seguimiento"),
                        rs.getString("destinatario"),
                        rs.getString("dir_destino"));
                pedidos.add(tarjeta);
            }
        }

        return pedidos;
    }

    private void registrarLogPaquete(Connection conn, LogPaquete log) throws SQLException {
        String query = "INSERT INTO log_paquete (paquete_id, estado_anterior, estado_nuevo, descripcion, fecha, metadata, usuario_id) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, log.getPaquete().getId());
            stmt.setString(2, log.getEstadoAnterior().toString());
            stmt.setString(3, log.getEstadoNuevo().toString());
            stmt.setString(4, log.getDescripcion());
            stmt.setDate(5, log.getFecha());
            stmt.setString(6, log.getMetadata());
            stmt.setInt(7, log.getUsuario().getId());
            stmt.executeUpdate();
        }
    }

    private void registrarLogTransportista(Connection conn, LogTransportista log) throws SQLException {
        String query = "INSERT INTO log_transportista (transportista_id, tipo_evento, paquete_id, descripcion, fecha, metadata) "
                +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, log.getTransportista().getId());
            stmt.setString(2, log.getTipoEvento().toString());
            stmt.setInt(3, log.getPaquete().getId());
            stmt.setString(4, log.getDescripcion());
            stmt.setDate(5, log.getFecha());
            stmt.setString(6, log.getMetadata());
            stmt.executeUpdate();
        }
    }
}