package Modelo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
public class GestionProducto {
	
    private ArrayList<Producto> lista;

    public GestionProducto() {
        this.lista = new ArrayList<>();
    }

    public boolean insertar(Producto p) {
        if (existe(p.getId())) return false;
        return lista.add(p);
    }

    public Producto buscar(int id) {
        for (Producto p : lista) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public boolean actualizar(Producto actualizado) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == actualizado.getId()) {
                lista.set(i, actualizado);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        Iterator<Producto> it = lista.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean existe(int id) {
        return buscar(id) != null;
    }

    public ArrayList<Producto> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Producto> lista) {
        this.lista = lista;
    }

    /** Ordena la lista por ID ascendente (útil antes de guardar inventario.json). */
    public void ordenarPorId() {
        lista.sort(Comparator.comparingInt(Producto::getId));
    }
}