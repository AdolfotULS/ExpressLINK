package expresslink.model;
import expresslink.model.*;
import java.sql.Date;

public class LogTransportista {
    private int id;
    private Transportista transportista ; // Relacion con Transportista
    private TipoEvento tipoEvento ;
    private Paquete paquete ; // Relacion con Paquete
    private String descripcion ;
    private Date fecha ;
    private String metadata ;

    enum TipoEvento {
        ASIGNACION,
        ACTUALIZACION,
        FINALIZACION
    }

    public LogTransportista(int id, Transportista transportista, TipoEvento tipoEvento, Paquete paquete, String descripcion, Date fecha, String metadata) {
        this.id = id;
        this.transportista = transportista;
        this.tipoEvento = tipoEvento;
        this.paquete = paquete;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.metadata = metadata;
    }

    public int getId() {
        return id;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public Paquete getPaquete() {
        return paquete;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
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
}