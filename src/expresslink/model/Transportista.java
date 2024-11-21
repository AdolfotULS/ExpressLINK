package expresslink.model;

import expresslink.model.*;
import expresslink.model.enums.*;

public class Transportista extends Usuario {
    private String licencia;
    private boolean disponible;
    private Vehiculo vehiculo;

    // Constructor

    public Transportista(int id, String nombre, String email, String password, String telefono, RolUsuario rol,
            Sucursal sucursal,
            String licencia, boolean disponible, Vehiculo vehiculo) {
        super(id, nombre, email, password, telefono, rol, sucursal);
        if (licencia == null || licencia.isEmpty()) {
            throw new IllegalArgumentException("La licencia no puede ser nula o vacía.");
        }
        this.licencia = licencia;
        this.disponible = disponible;
        this.vehiculo = vehiculo;
    }

    // Getters y Setters
    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        if (licencia == null || licencia.isEmpty()) {
            throw new IllegalArgumentException("La licencia no puede ser nula o vacía.");
        }
        this.licencia = licencia;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Vehiculo getVehiculo() {
        return vehiculo != null ? vehiculo : null;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo != null ? vehiculo : null;
    }

    @Override
    public String toString() {
        return "Transportista{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", licencia='" + licencia + '\'' +
                ", disponible=" + disponible +
                ", vehiculo=" + (vehiculo != null ? vehiculo.getPatente() : "Sin asignar") +
                '}';
    }
}