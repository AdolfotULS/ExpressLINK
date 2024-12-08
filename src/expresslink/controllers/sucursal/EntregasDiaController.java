package expresslink.controllers.sucursal;

import expresslink.model.Sucursal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.Date;
import java.time.LocalDate;

import expresslink.model.*;
import expresslink.model.enums.EstadoPaquete;
import expresslink.utils.DatabaseConnection;

public class EntregasDiaController {
    private final Sucursal sucursal;

    public EntregasDiaController(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }
        this.sucursal = sucursal;
    }

    public List<EntregasDia> obtenerPaquetesEntregadosHoy() {
        List<EntregasDia> entregas = new ArrayList<>();

        String query = """
                    SELECT
                        p.id as paquete_id,
                        p.num_seguimiento,
                        p.intentos_entrega,
                        p.estado,
                        p.dir_destino,
                        p.destinatario,
                        p.dimensiones_peso,
                        p.costo,
                        lt.fecha as fecha_entrega,
                        u.nombre as transportista_nombre,
                        u.telefono as transportista_telefono,
                        c.nombre as cliente_nombre
                    FROM log_transportista lt
                    INNER JOIN paquete p ON lt.paquete_id = p.id
                    INNER JOIN transportista t ON lt.transportista_id = t.id
                    INNER JOIN usuario u ON t.usuario_id = u.id
                    INNER JOIN usuario c ON p.cliente_id = c.id
                    WHERE lt.tipo_evento = 'FINALIZACION'
                    AND DATE(lt.fecha) = DATE(NOW())
                    AND t.sucursal_id = ?
                    ORDER BY lt.fecha DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, sucursal.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EntregasDia entrega = new EntregasDia(
                            rs.getInt("paquete_id"),
                            rs.getString("num_seguimiento"),
                            rs.getString("destinatario"),
                            rs.getString("dir_destino"),
                            rs.getString("dimensiones_peso"),
                            rs.getDouble("costo"),
                            rs.getInt("intentos_entrega"),
                            rs.getTimestamp("fecha_entrega"),
                            rs.getString("transportista_nombre"),
                            rs.getString("transportista_telefono"),
                            rs.getString("cliente_nombre"));
                    entregas.add(entrega);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener las entregas del día: " + e.getMessage(),
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return entregas;
    }

    public static class EntregasDia {
        private final int paqueteId;
        private final String numSeguimiento;
        private final String destinatario;
        private final String direccionDestino;
        private final String dimensionesPeso;
        private final double costo;
        private final int intentosEntrega;
        private final java.util.Date fechaEntrega;
        private final String transportistaNombre;
        private final String transportistaTelefono;
        private final String clienteNombre;

        public EntregasDia(int paqueteId, String numSeguimiento,
                String destinatario, String direccionDestino,
                String dimensionesPeso, double costo,
                int intentosEntrega, java.util.Date fechaEntrega,
                String transportistaNombre, String transportistaTelefono,
                String clienteNombre) {
            this.paqueteId = paqueteId;
            this.numSeguimiento = numSeguimiento;
            this.destinatario = destinatario;
            this.direccionDestino = direccionDestino;
            this.dimensionesPeso = dimensionesPeso;
            this.costo = costo;
            this.intentosEntrega = intentosEntrega;
            this.fechaEntrega = fechaEntrega;
            this.transportistaNombre = transportistaNombre;
            this.transportistaTelefono = transportistaTelefono;
            this.clienteNombre = clienteNombre;
        }

        // Getters
        public int getPaqueteId() {
            return paqueteId;
        }

        public String getNumSeguimiento() {
            return numSeguimiento;
        }

        public String getDestinatario() {
            return destinatario;
        }

        public String getDireccionDestino() {
            return direccionDestino;
        }

        public String getDimensionesPeso() {
            return dimensionesPeso;
        }

        public double getCosto() {
            return costo;
        }

        public int getIntentosEntrega() {
            return intentosEntrega;
        }

        public java.util.Date getFechaEntrega() {
            return fechaEntrega;
        }

        public String getTransportistaNombre() {
            return transportistaNombre;
        }

        public String getTransportistaTelefono() {
            return transportistaTelefono;
        }

        public String getClienteNombre() {
            return clienteNombre;
        }
    }
}