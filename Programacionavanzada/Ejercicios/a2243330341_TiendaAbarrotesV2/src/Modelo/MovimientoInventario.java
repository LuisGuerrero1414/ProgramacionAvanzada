package Modelo;

public class MovimientoInventario {
    private int idProducto;
    private String tipo;
    private int cantidad;
    private long timestamp;

    public MovimientoInventario(int idProducto, String tipo, int cantidad) {
        this.idProducto = idProducto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.timestamp = System.currentTimeMillis();
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
