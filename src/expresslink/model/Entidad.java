package expresslink.model;

public abstract class Entidad {
    private int id;

    // Constructor
    public Entidad(int id) {
        this.id = id;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

