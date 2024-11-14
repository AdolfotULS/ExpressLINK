//Chibi

package expresslink.model;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String contrasenia;
    private String telefono;

    public enum TipoUsuario { //Roles del usuario
        CLIENTE,
        TRANSPORTISTA,
        SUCURSAL,
        ADMIN
    }

    // Constructor
    public Usuario(int id, String nombre, String correo, String contrasenia, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
    }

    // Getters
    public int getId() {
        return id;
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

    // Setters
    public void setId(int id) {
        this.id = id;
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
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", correo='" + correo + '\'' +
               ", contrasenia='" + contrasenia + '\'' +
               ", telefono='" + telefono + '\'' +
               '}';
    }
}