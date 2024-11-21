package expresslink.model;

import expresslink.model.enums.RolUsuario;

public class Transportista extends Usuario {
    private String licencia;
    private boolean disponible;
    private Vehiculo vehiculo; // Relación con Vehiculo

    // Constructor
    public Transportista(int id, String nombre, String email, String password, String telefono, RolUsuario rol, Sucursal sucursal,
                         String licencia, boolean disponible, Vehiculo vehiculo) {
        super(id, nombre, email, password, telefono, rol, sucursal);
        this.licencia = licencia;
        this.disponible = disponible;
        this.vehiculo = vehiculo;
    }

    // Getters and Setters
    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
}

