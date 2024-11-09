//Chibi

package expresslink.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Pedido {

    private String numeroSeguimiento;
    private Cliente remitente;
    private String destinatario;
    private String direccionDestino;
    private String ciudadDestino;
    private double peso;
    private double alto;
    private double ancho;
    private double largo;
    private double costo;
    private EstadoPedido estado;
    private Date fechaCreacion;
    private Date fehcaEstimada;
    private Sucursal sucuralOrigen;
    private Sucursal sucuralDestino;
    private Transportista transportista;
    private boolean requiereTransporte;
    private int intentosEntrega;

    public enum EstadoPedido{
        INGRESADO,
        RECOLECTANDO,
        EN_TRANSITO,
        EN_SURCURSAL_DESTINO,
        ASIGNADO_TRANSPORTISTA,
        EN_CAMINO,
        ENTREGADO,
        NO_ENTREGADO,
        CANCELADO
    }
//Constructor
    public Pedido(String numeroSeguimiento, Cliente remitente, String destinatario, String direccionDestino, String ciudadDestino, double peso, double alto, double ancho, double largo, double costo, EstadoPedido estado, Date fechaCreacion, Date fehcaEstimada, Sucursal sucuralOrigen, Sucursal sucuralDestino, Transportista transportista, boolean requiereTransporte, int intentosEntrega) {
        this.numeroSeguimiento = numeroSeguimiento;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.direccionDestino = direccionDestino;
        this.ciudadDestino = ciudadDestino;
        this.peso = peso;
        this.alto = alto;
        this.ancho = ancho;
        this.largo = largo;
        this.costo = costo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fehcaEstimada = fehcaEstimada;
        this.sucuralOrigen = sucuralOrigen;
        this.sucuralDestino = sucuralDestino;
        this.transportista = transportista;
        this.requiereTransporte = requiereTransporte;
        this.intentosEntrega = intentosEntrega;
    }
        //Echale un ojo Adolfito
        public double calcularCosto() {
        double volumen = alto * ancho * largo;
        double costoBase = 1000;
        this.costo = costoBase + (peso * 10) + (volumen * 0.05); // Ejemplo de fórmula de cálculo
        return this.costo;
    }
        //Literal Acualiza estado
        public void actualizarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }
     //Asignacion del transportista
    public void asignarTransportista(Transportista transportista) {
        this.transportista = transportista;
        this.estado = EstadoPedido.ASIGNADO_TRANSPORTISTA;
        this.requiereTransporte = true; // Asigna el valor según la lógica de negocio
    }
    //Get

    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }

    public Cliente getRemitente() {
        return remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public double getPeso() {
        return peso;
    }

    public double getAlto() {
        return alto;
    }

    public double getAncho() {
        return ancho;
    }

    public double getLargo() {
        return largo;
    }

    public double getCosto() {
        return costo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Date getFehcaEstimada() {
        return fehcaEstimada;
    }

    public Sucursal getSucuralOrigen() {
        return sucuralOrigen;
    }

    public Sucursal getSucuralDestino() {
        return sucuralDestino;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public boolean isRequiereTransporte() {
        return requiereTransporte;
    }

    public int getIntentosEntrega() {
        return intentosEntrega;
    }
    //Set

    public void setNumeroSeguimiento(String numeroSeguimiento) {
        this.numeroSeguimiento = numeroSeguimiento;
    }

    public void setRemitente(Cliente remitente) {
        this.remitente = remitente;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFehcaEstimada(Date fehcaEstimada) {
        this.fehcaEstimada = fehcaEstimada;
    }

    public void setSucuralOrigen(Sucursal sucuralOrigen) {
        this.sucuralOrigen = sucuralOrigen;
    }

    public void setSucuralDestino(Sucursal sucuralDestino) {
        this.sucuralDestino = sucuralDestino;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public void setRequiereTransporte(boolean requiereTransporte) {
        this.requiereTransporte = requiereTransporte;
    }

    public void setIntentosEntrega(int intentosEntrega) {
        this.intentosEntrega = intentosEntrega;
    }
}

