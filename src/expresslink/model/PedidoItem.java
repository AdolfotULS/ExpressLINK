package expresslink.model;

import java.util.Date;

public class PedidoItem {
    private String numeroSeguimiento;
    private String destinatario;
    private String direccionDestino;
    private String estado;
    private Date fechaCreacion;
    private Date fechaEstimada;
    private int intentosEntrega;
    private String sucursalNombre;
    private String sucursalDireccion;

    // Datos del transportista (opcionales)
    private String transportistaNombre;
    private String transportistaTelefono;

    // Constructor y getters
    public PedidoItem(String numeroSeguimiento, String destinatario,
            String direccionDestino, String estado,
            Date fechaCreacion, Date fechaEstimada,
            int intentosEntrega, String sucursalNombre,
            String sucursalDireccion) {
        this.numeroSeguimiento = numeroSeguimiento;
        this.destinatario = destinatario;
        this.direccionDestino = direccionDestino;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEstimada = fechaEstimada;
        this.intentosEntrega = intentosEntrega;
        this.sucursalNombre = sucursalNombre;
        this.sucursalDireccion = sucursalDireccion;
    }

    public void setTransportista(String nombre, String telefono) {
        this.transportistaNombre = nombre;
        this.transportistaTelefono = telefono;
    }

    public String getNumeroSeguimiento() {
        return this.numeroSeguimiento;
    }

    public void setNumeroSeguimiento(String numeroSeguimiento) {
        this.numeroSeguimiento = numeroSeguimiento;
    }

    public String getDestinatario() {
        return this.destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getDireccionDestino() {
        return this.direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaEstimada() {
        return this.fechaEstimada;
    }

    public void setFechaEstimada(Date fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public int getIntentosEntrega() {
        return this.intentosEntrega;
    }

    public void setIntentosEntrega(int intentosEntrega) {
        this.intentosEntrega = intentosEntrega;
    }

    public String getSucursalNombre() {
        return this.sucursalNombre;
    }

    public void setSucursalNombre(String sucursalNombre) {
        this.sucursalNombre = sucursalNombre;
    }

    public String getSucursalDireccion() {
        return this.sucursalDireccion;
    }

    public void setSucursalDireccion(String sucursalDireccion) {
        this.sucursalDireccion = sucursalDireccion;
    }

    public String getTransportistaNombre() {
        return this.transportistaNombre;
    }

    public void setTransportistaNombre(String transportistaNombre) {
        this.transportistaNombre = transportistaNombre;
    }

    public String getTransportistaTelefono() {
        return this.transportistaTelefono;
    }

    public void setTransportistaTelefono(String transportistaTelefono) {
        this.transportistaTelefono = transportistaTelefono;
    }

}
