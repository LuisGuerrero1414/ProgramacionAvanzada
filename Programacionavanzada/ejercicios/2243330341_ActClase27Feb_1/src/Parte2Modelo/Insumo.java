package Parte2Modelo;

public class Insumo {
    private String idProducto;
    private String producto;
    private String idCategoria;

    // Constructor
    public Insumo(String idProducto, String producto, String idCategoria) {
        this.idProducto = idProducto;
        this.producto = producto;
        this.idCategoria = idCategoria;
    }

    // Getter y Setter para idProducto
    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    // Getter y Setter para producto
    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    // Getter y Setter para idCategoria
    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    // Sobreescritura del método toString para visualización formateada
    @Override
    public String toString() {
        return idProducto + "\t\t" + producto + "\t\t" + idCategoria;
    }
}