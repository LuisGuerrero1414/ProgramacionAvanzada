package Modelo;

public class LacteoHuevo extends Producto {
    public LacteoHuevo(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Lácteos y Huevo", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Cadena de Frío";
    }
}