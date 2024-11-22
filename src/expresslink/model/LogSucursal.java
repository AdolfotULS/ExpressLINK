package expresslink.model;

import expresslink.model.*;
import java.sql.Date;

public class LogSucursal {
    
    private int id;
    private Sucursal sucursal ; // Relacion con Sucursal
    private TipoEvento tipoEvento;
    private String descripcion ;
    private Date fecha ;
    private String metadata ;
    private Usuario usuario ; // Relacion con Usuario

    enum TipoEvento {
        CREACION,
        ACTUALIZACION,
        ELIMINACION
    }

    public LogSucursal(int id, Sucursal sucursal, TipoEvento tipoEvento, String descripcion, Date fecha, String metadata, Usuario usuario) {
        this.id = id;
        this.sucursal = sucursal;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.metadata = metadata;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getMetadata() {
        return metadata;
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

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}