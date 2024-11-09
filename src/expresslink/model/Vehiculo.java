//Chibi

package expresslink.model;

public class Vehiculo {
    private String placa;
    private TipoVehiculo tipo;
    private double capacidadMaxima;
    private boolean enServicio;
    private Transportista conductor;

    public enum TipoVehiculo{
        CAMION,
        FURGONETA,
        MOTO
    }
        public void asignarConductor(Transportista conductor) {
        this.conductor = conductor;
        this.enServicio = true; // Opcional, si deseas marcar el vehículo como "en servicio" al asignar un conductor
    }
    public Vehiculo(String placa, TipoVehiculo tipo, double capacidadMaxima, boolean enServicio, Transportista conductor) {
        this.placa = placa;
        this.tipo = tipo;
        this.capacidadMaxima = capacidadMaxima;
        this.enServicio = enServicio;
        this.conductor = conductor;
    }
//Get
    public String getPlaca() {
        return placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public boolean isEnServicio() {
        return enServicio;
    }

    public Transportista getConductor() {
        return conductor;
    }
//Set
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public void setCapacidadMaxima(double capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public void setEnServicio(boolean enServicio) {
        this.enServicio = enServicio;
    }

    public void setConductor(Transportista conductor) {
        this.conductor = conductor;
    }

}
