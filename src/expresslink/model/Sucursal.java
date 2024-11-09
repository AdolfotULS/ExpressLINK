//Chibi

package expresslink.model;

import java.util.List;
import java.util.ArrayList;

public class Sucursal {
    private int id; //Ojo no hay que confundice el id de usuario es diferente al de la sucursal
    private String nombre;
    private String direccion;
    private String ciudad;
    private List<Pedido> inventario;
    private List<Transportista> transportistas;

    public Sucursal(int id, String nombre, String direccion, String ciudad, List<Pedido> inventario, List<Transportista> transportistas) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.inventario = inventario;
        this.transportistas = transportistas;
    }

    //Agrega
        public void agregarPedido(Pedido pedido) {
        if (pedido != null) {
            inventario.add(pedido);
        }
    }

    //Transportista
            public void asignarTransportista(Pedido pedido, Transportista transportista) {
        if (pedido != null && transportista != null) {
            pedido.asignarTransportista(transportista);
            if (!transportistas.contains(transportista)) {
                transportistas.add(transportista); // Agrega al transportista si no esta ya en la lista
            }
        }
    }
     //Get
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public List<Pedido> getInventario() {
        return inventario;
    }

    public List<Transportista> getTransportistas() {
        return transportistas;
    }
   //Set

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setInventario(List<Pedido> inventario) {
        this.inventario = inventario;
    }

    public void setTransportistas(List<Transportista> transportistas) {
        this.transportistas = transportistas;
    }
}

