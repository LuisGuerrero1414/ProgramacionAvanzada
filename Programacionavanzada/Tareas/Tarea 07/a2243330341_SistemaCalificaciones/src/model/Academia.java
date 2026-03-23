package model;

import java.util.Objects;

/**
 * Entidad catálogo: academia académica.
 */
public class Academia {

    private long id;
    private String nombre;

    public Academia() {
    }

    public Academia(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Academia academia = (Academia) o;
        return id == academia.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
