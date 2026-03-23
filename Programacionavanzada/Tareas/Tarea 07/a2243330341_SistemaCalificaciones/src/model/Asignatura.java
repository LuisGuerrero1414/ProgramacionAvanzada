package model;

import java.util.Objects;

/**
 * Entidad catálogo: asignatura vinculada a una academia.
 */
public class Asignatura {

    private long id;
    private String nombre;
    private long idAcademia;

    public Asignatura() {
    }

    public Asignatura(long id, String nombre, long idAcademia) {
        this.id = id;
        this.nombre = nombre;
        this.idAcademia = idAcademia;
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

    public long getIdAcademia() {
        return idAcademia;
    }

    public void setIdAcademia(long idAcademia) {
        this.idAcademia = idAcademia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Asignatura that = (Asignatura) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
