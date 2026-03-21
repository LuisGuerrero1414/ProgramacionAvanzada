package Modelo;

public abstract class Producto {
    private int id;
    private String nombre;
    private double precio;
    private String categoria;
    private String rutaImagen; // Para mostrar la imagen en la UI

    public Producto(int id, String nombre, double precio, String categoria, String rutaImagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.rutaImagen = rutaImagen;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public String getRutaImagen() { return rutaImagen; }
    
    // Método para cumplir con la descripción de la tabla de la actividad
    public abstract String getTipoAlmacenamiento();
}