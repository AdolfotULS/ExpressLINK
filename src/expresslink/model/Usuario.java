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
//Constructor
    public Usuario(int identificador, String nombre, String correo, String contrasenia, String telefono) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
    }
//Get
    public int getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getTelefono() {
        return telefono;
    }
    //Set

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
    @Override
    
    public String toString() {
        return "Usuario{" +
               "identificador=" + identificador +
               ", nombre='" + nombre + '\'' +
               ", correo='" + correo + '\'' +
               ", contrasenia='" + contrasenia + '\'' +
               ", telefono='" + telefono + '\'' +
               '}';
    }
}