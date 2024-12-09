package expresslink.model;

import java.util.Objects;

public class DimensionesPaquete {
    private double ancho;
    private double largo;
    private double alto;
    private double peso;

    public DimensionesPaquete(String input) {
        if (input == null || input.trim().isEmpty()) {
            setDefaultValues();
            return;
        }

        try {
            // Separar dimensiones del peso
            String[] partes = input.split(",");
            if (partes.length < 2) {
                setDefaultValues();
                return;
            }

            // Procesar dimensiones
            String[] dimensiones = partes[0].split("x");
            if (dimensiones.length < 3) {
                setDefaultValues();
                return;
            }

            // Convertir dimensiones
            this.ancho = Double.parseDouble(dimensiones[0].trim());
            this.largo = Double.parseDouble(dimensiones[1].trim());
            this.alto = Double.parseDouble(dimensiones[2].trim());

            // Procesar peso
            String pesoStr = partes[1].replace("kg", "").trim();
            this.peso = Double.parseDouble(pesoStr);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error al parsear las dimensiones o el peso: " + input);
            System.err.println("Error específico: " + e.getMessage());
            setDefaultValues();
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
        // Convertir de cm³ a m³ dividiendo entre 1,000,000
        return (this.ancho * this.largo * this.alto) / 1_000_000.0;
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

    private void setDefaultValues() {
        this.ancho = 0;
        this.largo = 0;
        this.alto = 0;
        this.peso = 0;
        System.err.println("Error al parsear las dimensiones o el peso. Se usarán valores predeterminados.");
    }
}
