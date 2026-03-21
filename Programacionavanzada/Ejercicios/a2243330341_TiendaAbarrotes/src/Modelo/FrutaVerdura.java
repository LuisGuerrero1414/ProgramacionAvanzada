package Modelo;

public class FrutaVerdura extends Producto {
    public FrutaVerdura(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Frutas y Verduras", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Perecedero (Peso variable)";
    }
}