package Modelo;

public class ProductoProveedorRelacion {
    private int idProducto;
    private int idProveedor;

    public ProductoProveedorRelacion(int idProducto, int idProveedor) {
        this.idProducto = idProducto;
        this.idProveedor = idProveedor;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getIdProveedor() {
        return idProveedor;
    }
}
