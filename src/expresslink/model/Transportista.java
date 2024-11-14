//Chibi

package expresslink.model;

import java.util.ArrayList;
import java.util.List;

public class Transportista extends Usuario {
    private String licencia;
    private boolean disponible;
    private List<Pedido> pedidosActuales;
    private Vehiculo vehiculoAsignado;

    // Constructor
    public Transportista(int id, String nombre, String correo, String contrasenia, String telefono, String licencia, boolean disponible) {
        super(id, nombre, correo, contrasenia, telefono); // Llama al constructor de la clase padre
        this.licencia = licencia;
        this.disponible = disponible;
        this.pedidosActuales = new ArrayList<>(); // Inicializa la lista
    }

    // Método para asignar un pedido
    public void asignarPedido(Pedido pedido) {
        if (pedido != null) {
            pedidosActuales.add(pedido);
            disponible = false; // El transportista no está disponible mientras tiene pedidos
        }
    }

    // Método para completar un pedido
    public void completarPedido(Pedido pedido) {
        pedidosActuales.remove(pedido);
        if (pedidosActuales.isEmpty()) {
            disponible = true; // Si no hay más pedidos, el transportista está disponible
        }
    }

    // Getters
    public String getLicencia() {
        return licencia;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public List<Pedido> getPedidosActuales() {
        return pedidosActuales;
    }

    public Vehiculo getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    // Setters
    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setVehiculoAsignado(Vehiculo vehiculoAsignado) {
        this.vehiculoAsignado = vehiculoAsignado;
    }
}
