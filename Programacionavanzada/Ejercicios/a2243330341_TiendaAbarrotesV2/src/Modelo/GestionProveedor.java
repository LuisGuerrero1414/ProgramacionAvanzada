package Modelo;

import java.util.ArrayList;
import java.util.Iterator;

public class GestionProveedor {
    private final ArrayList<Proveedor> lista;

    public GestionProveedor() {
        this.lista = new ArrayList<>();
    }

    public ArrayList<Proveedor> getLista() {
        return lista;
    }

    public boolean insertar(Proveedor proveedor) {
        for (Proveedor p : lista) {
            if (p.getId() == proveedor.getId()) {
                return false;
            }
        }
        return lista.add(proveedor);
    }

    public boolean eliminar(int id) {
        Iterator<Proveedor> it = lista.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
