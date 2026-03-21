package Modelo;

public class CarnePescado extends Producto {
    public CarnePescado(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Carnes y Pescados", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Congelados / Frescos";
    }
}