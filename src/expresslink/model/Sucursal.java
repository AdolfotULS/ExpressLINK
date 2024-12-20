package expresslink.model;
public class Sucursal {
    private int id;
    private String nombre;
    private String direccion;
    private String ciudad;

    // Constructor
    public Sucursal(int id, String nombre, String direccion, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "{" +
            " id=" + id +
            ", nombre='" + nombre + "'" +
            ", direccion='" + direccion + "'" +
            ", ciudad='" + ciudad + "'" +
            "}";
    }
}