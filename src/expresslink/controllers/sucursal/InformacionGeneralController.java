package expresslink.controllers.sucursal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import expresslink.model.DimensionesPaquete;
import expresslink.model.Paquete;
import expresslink.model.Sucursal;
import expresslink.model.Transportista;
import expresslink.model.Usuario;
import expresslink.model.enums.EstadoPaquete;
import expresslink.utils.DatabaseConnection;

public class InformacionGeneralController {
    private Sucursal sucursal;

    public InformacionGeneralController(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }
        this.sucursal = sucursal;
    }

    public List<Paquete> obtenerPaquetes() throws SQLException {
        List<Paquete> paquetes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Consulta para obtener paquetes actualizados hoy con datos relacionados
            String query = """
                        SELECT p.*, t.id as transportista_id, t.usuario_id as transportista_usuario_id,
                               t.licencia, t.disponible,
                               u.nombre as cliente_nombre, u.email as cliente_email,
                               uc.nombre as creador_nombre, uc.email as creador_email
                        FROM paquete p
                        LEFT JOIN transportista t ON p.transportista_id = t.id
                        LEFT JOIN usuario u ON p.cliente_id = u.id
                        LEFT JOIN usuario uc ON p.usuario_creador_id = uc.id
                        WHERE p.sucursal_origen_id = ?
                        AND DATE(p.fecha_actualizacion) = CURRENT_DATE
                        ORDER BY p.fecha_actualizacion DESC
                    """;

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, sucursal.getId());
            rs = stmt.executeQuery();

            while (rs.next()) {
                // Crear objeto Usuario para el cliente si existe
                Usuario cliente = null;
                if (rs.getObject("cliente_id") != null) {
                    cliente = new Usuario(
                            rs.getInt("cliente_id"),
                            rs.getString("cliente_nombre"),
                            rs.getString("cliente_email"),
                            "", // No necesitamos la contraseña
                            "", // No necesitamos el teléfono
                            null, // No necesitamos el rol
                            null // No necesitamos la sucursal
                    );
                }

                // Crear objeto Transportista si existe
                Transportista transportista = null;
                if (rs.getObject("transportista_id") != null) {
                    transportista = new Transportista(
                            rs.getInt("transportista_usuario_id"),
                            "", // nombre no necesario aquí
                            "", // email no necesario aquí
                            "", // password no necesario aquí
                            "", // telefono no necesario aquí
                            null, // rol no necesario aquí
                            null, // sucursal no necesaria aquí
                            rs.getString("licencia"),
                            rs.getBoolean("disponible"),
                            null // vehículo no necesario aquí
                    );
                }

                // Crear objeto Usuario para el creador
                Usuario creador = new Usuario(
                        rs.getInt("usuario_creador_id"),
                        rs.getString("creador_nombre"),
                        rs.getString("creador_email"),
                        "", // No necesitamos la contraseña
                        "", // No necesitamos el teléfono
                        null, // No necesitamos el rol
                        null // No necesitamos la sucursal
                );

                // Crear el objeto Paquete
                Paquete paquete = new Paquete(
                        rs.getInt("id"),
                        rs.getString("num_seguimiento"),
                        rs.getString("email_cliente"),
                        cliente,
                        sucursal,
                        transportista,
                        rs.getString("destinatario"),
                        rs.getString("dir_destino"),
                        new DimensionesPaquete(rs.getString("dimensiones_peso")),
                        rs.getDouble("costo"),
                        EstadoPaquete.valueOf(rs.getString("estado")),
                        rs.getTimestamp("fecha_creacion"),
                        rs.getTimestamp("fecha_estimada"),
                        rs.getInt("intentos_entrega"),
                        creador);

                paquetes.add(paquete);
            }
            return paquetes;

        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }

    @SuppressWarnings("deprecation")
    public int[] contadorPaquetes(List<Paquete> paquetes) {
        if (paquetes == null)
            return new int[] { 0, 0, 0 };

        int pendientes = 0;
        int transito = 0;
        int entregadosHoy = 0;

        // Obtener fecha actual sin tiempo
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date hoy = cal.getTime();

        for (Paquete paquete : paquetes) {
            switch (paquete.getEstado()) {
                case PENDIENTE:
                    pendientes++;
                    break;
                case EN_TRANSITO:
                    transito++;
                    break;
                case ENTREGADO:
                    // Verificar si la fecha de actualización es de hoy
                    Date fechaActualizacion = new Date(paquete.getFechaCreacion().getTime());
                    fechaActualizacion.setHours(0);
                    fechaActualizacion.setMinutes(0);
                    fechaActualizacion.setSeconds(0);

                    if (fechaActualizacion.equals(hoy)) {
                        entregadosHoy++;
                    }
                    break;
                default:
                    // Ignorar otros estados
                    break;
            }
        }

        return new int[] { pendientes, transito, entregadosHoy };
    }

    public int[] obtenerContadorTransportistas() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int[] resultado = new int[2]; // [activos, total]

        try {
            conn = DatabaseConnection.getConnection();
            String query = """
                        SELECT
                            COUNT(*) as total,
                            SUM(CASE WHEN disponible = true THEN 1 ELSE 0 END) as activos
                        FROM transportista
                        WHERE sucursal_id = ?
                    """;

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, sucursal.getId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                resultado[0] = rs.getInt("activos");
                resultado[1] = rs.getInt("total");
            }

            return resultado;
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }

    public List<Paquete> obtenerPaquetesActuales() throws SQLException {
        List<Paquete> paquetes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = """
                        SELECT p.*, t.id as transportista_id, t.usuario_id as transportista_usuario_id,
                               t.licencia, t.disponible,
                               u.nombre as cliente_nombre, u.email as cliente_email,
                               uc.nombre as creador_nombre, uc.email as creador_email
                        FROM paquete p
                        LEFT JOIN transportista t ON p.transportista_id = t.id
                        LEFT JOIN usuario u ON p.cliente_id = u.id
                        LEFT JOIN usuario uc ON p.usuario_creador_id = uc.id
                        WHERE p.sucursal_origen_id = ?
                        AND p.estado IN ('PENDIENTE', 'EN_TRANSITO')
                        ORDER BY p.fecha_actualizacion DESC
                    """;

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, sucursal.getId());
            rs = stmt.executeQuery();

            while (rs.next()) {
                // Crear objeto Usuario para el cliente si existe
                Usuario cliente = null;
                if (rs.getObject("cliente_id") != null) {
                    cliente = new Usuario(
                            rs.getInt("cliente_id"),
                            rs.getString("cliente_nombre"),
                            rs.getString("cliente_email"),
                            "", // No necesitamos la contraseña
                            "", // No necesitamos el teléfono
                            null, // No necesitamos el rol
                            null // No necesitamos la sucursal
                    );
                }

                // Crear objeto Transportista si existe
                Transportista transportista = null;
                if (rs.getObject("transportista_id") != null) {
                    transportista = new Transportista(
                            rs.getInt("transportista_usuario_id"),
                            "", // nombre no necesario aquí
                            "", // email no necesario aquí
                            "", // password no necesario aquí
                            "", // telefono no necesario aquí
                            null, // rol no necesario aquí
                            null, // sucursal no necesaria aquí
                            rs.getString("licencia"),
                            rs.getBoolean("disponible"),
                            null // vehículo no necesario aquí
                    );
                }

                // Crear objeto Usuario para el creador
                Usuario creador = new Usuario(
                        rs.getInt("usuario_creador_id"),
                        rs.getString("creador_nombre"),
                        rs.getString("creador_email"),
                        "", // No necesitamos la contraseña
                        "", // No necesitamos el teléfono
                        null, // No necesitamos el rol
                        null // No necesitamos la sucursal
                );

                // Crear el objeto Paquete
                Paquete paquete = new Paquete(
                        rs.getInt("id"),
                        rs.getString("num_seguimiento"),
                        rs.getString("email_cliente"),
                        cliente,
                        sucursal,
                        transportista,
                        rs.getString("destinatario"),
                        rs.getString("dir_destino"),
                        new DimensionesPaquete(rs.getString("dimensiones_peso")),
                        rs.getDouble("costo"),
                        EstadoPaquete.valueOf(rs.getString("estado")),
                        rs.getTimestamp("fecha_creacion"),
                        rs.getTimestamp("fecha_estimada"),
                        rs.getInt("intentos_entrega"),
                        creador);

                paquetes.add(paquete);
            }
            return paquetes;
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }

    public String formatearEstado(EstadoPaquete estado) {
        if (estado == null)
            return "";
        String texto = estado.toString().toLowerCase()
                .replace("_", " ");
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}