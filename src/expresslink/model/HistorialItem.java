package expresslink.model;

import java.util.Date;

public class HistorialItem {
    private String numeroSeguimiento;
    private String destinatario;
    private String direccion;
    private Date fechaEstimada;
    private double costo;

    // Constructor
    public HistorialItem(String numeroSeguimiento, String destinatario,
            String direccion, Date fechaEstimada, double costo) {
        this.numeroSeguimiento = numeroSeguimiento;
        this.destinatario = destinatario;
        this.direccion = direccion;
        this.fechaEstimada = fechaEstimada;
        this.costo = costo;
    }

    // Getters
    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getDireccion() {
        return direccion;
    }

    public Date getFechaEstimada() {
        return fechaEstimada;
    }

    public double getCosto() {
        return costo;
    }
}