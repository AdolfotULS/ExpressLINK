package expresslink.model;

public class Vehiculo {
    private int id;
    private String patente;
    private double capacidadVolumen;

    // Constructor

    public Vehiculo(int id, String patente, double capacidadVolumen) {
        this.id = id;
        this.patente = patente;
        this.capacidadVolumen = capacidadVolumen;
    }

    public int getId() {
        return id;
    }

    public String getPatente() {
        return patente;
    }

    public double getCapacidadVolumen() {
        return capacidadVolumen;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public void setCapacidadVolumen(double capacidadVolumen) {
        this.capacidadVolumen = capacidadVolumen;
    }

}
