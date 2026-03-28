package Modelo;

import java.util.ArrayList;
import java.util.Iterator;

public class GestionUnidadMedida {
    private final ArrayList<UnidadMedida> unidades;

    public GestionUnidadMedida() {
        this.unidades = new ArrayList<>();
    }

    public ArrayList<UnidadMedida> getUnidades() {
        return unidades;
    }

    public boolean agregar(UnidadMedida unidad) {
        for (UnidadMedida existente : unidades) {
            if (existente.getClave().equalsIgnoreCase(unidad.getClave())) {
                return false;
            }
        }
        return unidades.add(unidad);
    }

    /** Elimina la unidad cuya clave coincide (ignora mayúsculas). */
    public boolean eliminarPorClave(String clave) {
        if (clave == null || clave.isBlank()) {
            return false;
        }
        Iterator<UnidadMedida> it = unidades.iterator();
        while (it.hasNext()) {
            if (it.next().getClave().equalsIgnoreCase(clave.trim())) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
