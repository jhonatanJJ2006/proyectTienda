package Facturitaa;

public class Receptor {
    private String nombre;
    private String apellido;
    private String cedula;
    private String celular;

    public Receptor(String nombre, String apellido, String cedula, String celular) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.celular = celular;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCelular() {
        return celular;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + "\nCédula: " + cedula + "\nCelular: " + celular;
    }
}
