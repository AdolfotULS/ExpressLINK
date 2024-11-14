//Chibi

package expresslink.model;

import java.util.ArrayList;
import java.util.List;

import expresslink.model.enums.TipoUsuario;

public class Cliente extends Usuario {
    private String direccion;
    private List<Pedido> historialPedidos;

    // Constructor
    public Cliente(int id, String nombre, String correo, String contrasenia, String telefono, String direccion) {
        super(id, nombre, correo, contrasenia, telefono, TipoUsuario.CLIENTE); // Llama al constructor de la clase padre
        this.direccion = direccion;
        this.historialPedidos = new ArrayList<>(); // Inicializa la lista de pedidos
    }

    // Método para agregar un pedido al historial
    public void agregarPedido(Pedido pedido) {
        if (pedido != null) {
            historialPedidos.add(pedido);
        }
    }

    // Getters
    public String getDireccion() {
        return direccion;
    }

    public List<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

    // Setters
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setHistorialPedidos(List<Pedido> historialPedidos) {
        this.historialPedidos = historialPedidos;
    }
}
