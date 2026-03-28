package Modelo;

import java.util.ArrayList;

public class GestionMovimientoInventario {
    private final ArrayList<MovimientoInventario> movimientos;

    public GestionMovimientoInventario() {
        this.movimientos = new ArrayList<>();
    }

    public ArrayList<MovimientoInventario> getMovimientos() {
        return movimientos;
    }

    public void registrarMovimiento(int idProducto, String tipo, int cantidad) {
        movimientos.add(new MovimientoInventario(idProducto, tipo, cantidad));
    }
}
