package Modelo;

public class CuidadoPersonal extends Producto {
    public CuidadoPersonal(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Cuidado Personal", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Higiene";
    }
}