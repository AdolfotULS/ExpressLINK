package expresslink.model;

import expresslink.model.*;
import expresslink.model.enums.*;

public class Usuario extends Persona {
    private String password;
    private String telefono;
    private RolUsuario rol;
    private Sucursal sucursal; // Relación con Sucursal

    // Constructor
    public Usuario(int id, String nombre, String email, String password, String telefono, RolUsuario rol,
            Sucursal sucursal) {
        super(id, nombre, email);
        this.password = password;
        this.telefono = telefono;
        this.rol = rol;
        this.sucursal = sucursal;
    }

    // Getters and Setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    @Override
    public String toString() {
        return "{" +
                " id=" + super.getId() + "," +
                " password='" + getPassword() + "'" +
                ", telefono='" + getTelefono() + "'" +
                ", rol='" + getRol() + "'" +
                ", sucursal='" + getSucursal() + "'" +
                "}";
    }

}
