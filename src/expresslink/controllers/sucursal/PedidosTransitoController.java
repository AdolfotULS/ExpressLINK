package expresslink.controllers.sucursal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;
import javax.swing.JOptionPane;

import expresslink.model.*;
import expresslink.model.enums.EstadoPaquete;
import expresslink.utils.DatabaseConnection;

public class PedidosTransitoController {
    private final Sucursal sucursal;

    public PedidosTransitoController(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }
        this.sucursal = sucursal;
    }

    public List<PaqueteTransito> obtenerPaquetesTransitoSucursal() {
        List<PaqueteTransito> paquetes = new ArrayList<>();

        String query = """
                    SELECT p.id, p.num_seguimiento, p.email_cliente,
                           p.dir_destino, p.destinatario, p.costo,
                           p.estado, p.fecha_creacion, p.fecha_estimada,
                           p.intentos_entrega, t.nombre as transportista_nombre,
                           t.telefono as transportista_telefono
                    FROM paquete p
                    LEFT JOIN transportista tr ON p.transportista_id = tr.id
                    LEFT JOIN usuario t ON tr.usuario_id = t.id
                    WHERE p.sucursal_origen_id = ?
                    AND p.estado = 'EN_TRANSITO'
                    ORDER BY p.fecha_creacion DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, sucursal.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PaqueteTransito paquete = new PaqueteTransito(
                            rs.getInt("id"),
                            rs.getString("num_seguimiento"),
                            rs.getString("email_cliente"),
                            rs.getString("destinatario"),
                            rs.getString("dir_destino"),
                            rs.getDouble("costo"),
                            EstadoPaquete.valueOf(rs.getString("estado")),
                            rs.getDate("fecha_creacion"),
                            rs.getDate("fecha_estimada"),
                            rs.getInt("intentos_entrega"),
                            rs.getString("transportista_nombre"),
                            rs.getString("transportista_telefono"));
                    paquetes.add(paquete);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener los paquetes en tránsito: " + e.getMessage(),
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return paquetes;
    }

    // Clase interna para manejar los datos específicos de paquetes en tránsito
    public static class PaqueteTransito {
        private final int id;
        private final String numSeguimiento;
        private final String emailCliente;
        private final String destinatario;
        private final String direccionDestino;
        private final double costo;
        private final EstadoPaquete estado;
        private final java.util.Date fechaCreacion;
        private final java.util.Date fechaEstimada;
        private final int intentosEntrega;
        private final String transportistaNombre;
        private final String transportistaTelefono;

        public PaqueteTransito(int id, String numSeguimiento, String emailCliente,
                String destinatario, String direccionDestino, double costo,
                EstadoPaquete estado, java.util.Date fechaCreacion,
                java.util.Date fechaEstimada, int intentosEntrega,
                String transportistaNombre, String transportistaTelefono) {
            this.id = id;
            this.numSeguimiento = numSeguimiento;
            this.emailCliente = emailCliente;
            this.destinatario = destinatario;
            this.direccionDestino = direccionDestino;
            this.costo = costo;
            this.estado = estado;
            this.fechaCreacion = fechaCreacion;
            this.fechaEstimada = fechaEstimada;
            this.intentosEntrega = intentosEntrega;
            this.transportistaNombre = transportistaNombre;
            this.transportistaTelefono = transportistaTelefono;
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getNumSeguimiento() {
            return numSeguimiento;
        }

        public String getEmailCliente() {
            return emailCliente;
        }

        public String getDestinatario() {
            return destinatario;
        }

        public String getDireccionDestino() {
            return direccionDestino;
        }

        public double getCosto() {
            return costo;
        }

        public String getEstado() {
            switch (estado) {
                case PENDIENTE:
                    return "Pendiente";
                case EN_TRANSITO:
                    return "En Transito";
                case ENTREGADO:
                    return "Entregado";
                case CANCELADO:
                    return "Cancelado";

                default:
                    return "Desconocido";
            }
        }

        public java.util.Date getFechaCreacion() {
            return fechaCreacion;
        }

        public java.util.Date getFechaEstimada() {
            return fechaEstimada;
        }

        public int getIntentosEntrega() {
            return intentosEntrega;
        }

        public String getTransportistaNombre() {
            return transportistaNombre;
        }

        public String getTransportistaTelefono() {
            return transportistaTelefono;
        }
    }
}