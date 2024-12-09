package expresslink.model;
import expresslink.model.*;
import expresslink.model.enums.*;
import java.sql.Date;

public class LogPaquete {
    private int id;
    private Paquete paquete ; // Relacion con Paquete
    private EstadoPaquete estadoAnterior ;
    private EstadoPaquete estadoNuevo ;
    private String descripcion ;
    private Date fecha ;
    private String metadata ;
    private Usuario usuario ; // Relacion con Usuario

    public LogPaquete(int id, Paquete paquete, EstadoPaquete estadoAnterior, EstadoPaquete estadoNuevo, String descripcion, Date fecha, String metadata, Usuario usuario) {
        this.id = id;
        this.paquete = paquete;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.metadata = metadata;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public EstadoPaquete getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoPaquete getEstadoNuevo() {
        return estadoNuevo;
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

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public void setEstadoAnterior(EstadoPaquete estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public void setEstadoNuevo(EstadoPaquete estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
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