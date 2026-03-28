package Modelo;

import java.util.ArrayList;
import java.util.Iterator;

public class GestionRelacionProductoProveedor {
    private final ArrayList<ProductoProveedorRelacion> relaciones;

    public GestionRelacionProductoProveedor() {
        this.relaciones = new ArrayList<>();
    }

    public ArrayList<ProductoProveedorRelacion> getRelaciones() {
        return relaciones;
    }

    public boolean agregarRelacion(int idProducto, int idProveedor) {
        for (ProductoProveedorRelacion r : relaciones) {
            if (r.getIdProducto() == idProducto && r.getIdProveedor() == idProveedor) {
                return false;
            }
        }
        return relaciones.add(new ProductoProveedorRelacion(idProducto, idProveedor));
    }

    public boolean eliminarRelacion(int idProducto, int idProveedor) {
        Iterator<ProductoProveedorRelacion> it = relaciones.iterator();
        while (it.hasNext()) {
            ProductoProveedorRelacion r = it.next();
            if (r.getIdProducto() == idProducto && r.getIdProveedor() == idProveedor) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
