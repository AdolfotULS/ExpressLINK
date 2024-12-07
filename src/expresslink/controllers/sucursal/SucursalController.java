package expresslink.controllers.sucursal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import expresslink.model.Sucursal;
import expresslink.model.Usuario;
import expresslink.utils.DatabaseConnection;
import expresslink.view.sucursal.SucursalDashboard;

public class SucursalController {
    private SucursalDashboard view;
    private Usuario usuario;

    public SucursalController(Usuario usuario, SucursalDashboard view) {
        this.view = view;
        this.usuario = usuario;
    }

    public Sucursal obtenerDatosSucursal() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            // Query optimizada para obtener solo los datos necesarios
            String query = "SELECT s.id, s.nombre, s.direccion, s.ciudad " +
                    "FROM sucursal s " +
                    "INNER JOIN usuario u ON s.id = u.sucursal_id " +
                    "WHERE u.id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, usuario.getId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Sucursal(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("ciudad"));
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }

}