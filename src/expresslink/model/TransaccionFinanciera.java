package expresslink.model;

import expresslink.model.*;
import java.util.Date; // Para el manejo de fechas

public class TransaccionFinanciera {
    private int id;
    private Sucursal sucursal;
    private TipoTransaccion tipo;
    private double monto;
    private String concepto;
    private Date fecha;
    private Strinf referencia;
    private Paquete paquete;
    private Usuario usuario;

    public TransaccionFinanciera(int id, Sucursal sucursal, TipoTransaccion tipo, double monto, String concepto, Date fecha, Strinf referencia, Paquete paquete, Usuario usuario) {
        this.id = id;
        this.sucursal = sucursal;
        this.tipo = tipo;
        this.monto = monto;
        this.concepto = concepto;
        this.fecha = fecha;
        this.referencia = referencia;
        this.paquete = paquete;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public Date getFecha() {
        return fecha;
    }

    public Strinf getReferencia() {
        return referencia;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setReferencia(Strinf referencia) {
        this.referencia = referencia;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
