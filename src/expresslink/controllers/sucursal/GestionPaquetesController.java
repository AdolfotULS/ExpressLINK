package expresslink.controllers.sucursal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import expresslink.model.*;
import expresslink.model.LogSucursal.TipoEvento;
import expresslink.model.enums.EstadoPaquete;

public class GestionPaquetesController {
    private Sucursal sucursal;
    private Usuario usuario;
    private List<Transportista> transportistas;
    private double capacidad_maxima = 15;

    public GestionPaquetesController(Usuario usuario, Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }
        this.sucursal = sucursal;
        this.usuario = usuario;
    }

    public List<Transportista> obtenerTrasnportistas() {
        return null;
    }

    public boolean asignacionTransportistasAuto(Paquete paquete) throws SQLException {

        for (Transportista transportista : transportistas) {
            double capacidad_volumen = transportista.getVehiculo().getCapacidadVolumen();
            if (capacidad_volumen + paquete.getDimensionesPeso().getVolumen() < capacidad_maxima) {
                int trans_asignado = transportista.getId();
                // Agregar a ese transportista el paquete
                modificarEstado(paquete, EstadoPaquete.EN_TRANSITO);
                registrarLogPaquete(null, null); // Registrar Cambios
                registrarLogTransportista(null, null); // REGISTRAR ASIGNACION
                registrarLogSucursal(null, null, TipoEvento.ACTUALIZACION); // Regisrrar actualizacion
            }
        }

        return false;
    }

    // Obtener todos los paquetes que no han sido entregados
    public List<Paquete> obtenerTodosPaquetes() {
        return null;
    }

    public boolean modificarEstado(Paquete paquete, EstadoPaquete nuevoEstado) {

        try {
            if (paquete.getEstado() == nuevoEstado) {
                return false;
            }
            // INTENTAR MODIFICAR el paquete
            String query = "UPDATE";

        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    private void registrarLogSucursal(Connection conn, String descripcion, LogSucursal.TipoEvento tipoEvento)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            String query = "INSERT INTO log_sucursal (sucursal_id, tipo_evento, descripcion, fecha, metadata, usuario_id) "
                    +
                    "VALUES (?, ?, ?, NOW(), NULL, ?)";

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, sucursal.getId());
            stmt.setString(2, tipoEvento.name());
            stmt.setString(3, descripcion);
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
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
