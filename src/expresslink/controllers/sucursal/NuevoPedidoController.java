package expresslink.controllers.sucursal;

import expresslink.model.*;

public class NuevoPedidoController {
    public NuevoPedidoController() {

    }

    private String formatoDimensiones(DimensionesPaquete paquete) {
        double ancho = paquete.getAncho();
        double largo = paquete.getLargo();
        double alto = paquete.getAlto();
        double peso = paquete.getPeso();

        return ancho + "x" + largo + "x" + alto + "," + peso + "kg";
    }

    public double calcularPrecio(DimensionesPaquete dimensionesPaquete) {
        double ancho = dimensionesPaquete.getAncho();
        double largo = dimensionesPaquete.getLargo();
        double alto = dimensionesPaquete.getAlto();
        double peso = dimensionesPaquete.getPeso();

        // Precio base por manejo
        double precioBase = 1000;

        // Calcular el volumen del paquete (en cm³)
        double volumen = ancho * largo * alto;

        // Costos adicionales según el peso
        double costoPorPeso = peso * 500; // Ejemplo: 500 pesos por cada kg

        // Costos adicionales según el volumen
        double costoPorVolumen = volumen * 0.02; // Ejemplo: 0.02 pesos por cm³

        // Precio final
        double precioFinal = precioBase + costoPorPeso + costoPorVolumen;

        return precioFinal;
    }

    public boolean crearNuevoPaquete(Paquete nuevoPaquete) {
        return false;
    }
}
