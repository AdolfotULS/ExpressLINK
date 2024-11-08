//@Chibi

package expresslink.model;

public class Usuario {
    private int identificador;
    private String nombre;
    private String correo;
    private String contrasenia;
    private String telefono;
    public enum TipoUsuario {
        CLIENTE,
        TRANSPORTISTA,
        SUCURSAL,
        ADMIN
    }
}