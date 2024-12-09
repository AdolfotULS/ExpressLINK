package expresslink.model;

import java.util.Date; // Para el manejo de fechas
import expresslink.model.*;
import expresslink.model.enums.*;

public class Paquete {
    private int id;
    private String numSeguimiento;
    private String emailCliente;
    private Usuario cliente; // Relacion con usuario
    private Sucursal sucursalOrigen; // Relacion con sucursal
    private Transportista transportista; // Relacion con transportista
    private String destinatario;
    private String direccionDestino;
    private DimensionesPaquete dimensionesPeso;
    private double costo;
    private EstadoPaquete estado;
    private Date fechaCreacion;
    private Date fechaEstimada;
    private int intentosEntrega;
    private Usuario usuarioCreador; // Relacion con Usuario

    // Constructor

    public Paquete(int id, String numSeguimiento, String emailCliente, Usuario cliente, Sucursal sucursalOrigen,
            Transportista transportista, String destinatario, String direccionDestino,
            DimensionesPaquete dimensionesPeso,
            double costo, EstadoPaquete estado, Date fechaCreacion, Date fechaEstimada, int intentosEntrega,
            Usuario usuarioCreador) {
        this.id = id;
        this.numSeguimiento = numSeguimiento;
        this.emailCliente = emailCliente;
        this.cliente = cliente;
        this.sucursalOrigen = sucursalOrigen;
        this.transportista = transportista;
        this.destinatario = destinatario;
        this.direccionDestino = direccionDestino;
        this.dimensionesPeso = dimensionesPeso;
        this.costo = costo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEstimada = fechaEstimada;
        this.intentosEntrega = intentosEntrega;
        this.usuarioCreador = usuarioCreador;
    }

    public Paquete(int id) {
        this.id = id;
        // Los demás campos quedarán null/0 por defecto
    }

    public int getId() {
        return id;
    }

    public String getNumSeguimiento() {
        return numSeguimiento;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public Sucursal getSucursalOrigen() {
        return sucursalOrigen;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public DimensionesPaquete getDimensionesPeso() {
        return dimensionesPeso;
    }

    public double getCosto() {
        return costo;
    }

    public EstadoPaquete getEstado() {
        return estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Date getFechaEstimada() {
        return fechaEstimada;
    }

    public int getIntentosEntrega() {
        return intentosEntrega;
    }

    public Usuario getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumSeguimiento(String numSeguimiento) {
        this.numSeguimiento = numSeguimiento;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public void setSucursalOrigen(Sucursal sucursalOrigen) {
        this.sucursalOrigen = sucursalOrigen;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public void setDimensionesPeso(DimensionesPaquete dimensionesPeso) {
        this.dimensionesPeso = dimensionesPeso;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public void setEstado(EstadoPaquete estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaEstimada(Date fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public void setIntentosEntrega(int intentosEntrega) {
        this.intentosEntrega = intentosEntrega;
    }

    public void setUsuarioCreador(Usuario usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

}
