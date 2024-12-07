package expresslink.model;

import java.util.Objects;

public class DimensionesPaquete {
    private double ancho;
    private double largo;
    private double alto;
    private double peso;

    public DimensionesPaquete(String input) {
        input = input.replace(",", ".").replace("kg", "");

        String[] parts = input.split("x");

        this.ancho = 0;
        this.largo = 0;
        this.alto = 0;
        this.peso = 0;

        try {
            if (parts.length > 0)
                this.ancho = Double.parseDouble(parts[0].trim());
            if (parts.length > 1)
                this.largo = Double.parseDouble(parts[1].trim());
            if (parts.length > 2)
                this.alto = Double.parseDouble(parts[2].trim());
            if (parts.length > 3)
                this.peso = Double.parseDouble(parts[3].trim());
        } catch (NumberFormatException e) {
            // En caso de error, se mantienen los valores predeterminados (0)
            System.err.println("Error al parsear las dimensiones o el peso. Se usarán valores predeterminados.");
        }
    }

    public DimensionesPaquete(double ancho, double largo, double alto, double peso) {
        this.ancho = ancho;
        this.largo = largo;
        this.alto = alto;
        this.peso = peso;
    }

    public double getAncho() {
        return this.ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getLargo() {
        return this.largo;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    public double getAlto() {
        return this.alto;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }

    public double getPeso() {
        return this.peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getVolumen() {
        return this.ancho * this.largo * this.alto;
    }

    public DimensionesPaquete ancho(double ancho) {
        setAncho(ancho);
        return this;
    }

    public DimensionesPaquete largo(double largo) {
        setLargo(largo);
        return this;
    }

    public DimensionesPaquete alto(double alto) {
        setAlto(alto);
        return this;
    }

    public DimensionesPaquete peso(double peso) {
        setPeso(peso);
        return this;
    }
}
