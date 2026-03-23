package controller;

import java.io.IOException;
import java.util.List;

import model.Academia;
import model.Asignatura;
import persistence.RepositorioAcademias;
import persistence.RepositorioAsignaturas;

/**
 * Orquesta el CRUD de academias y asignaturas sobre archivos JSON.
 */
public class CatalogoController {

    private final RepositorioAcademias academias;
    private final RepositorioAsignaturas asignaturas;

    public CatalogoController(RepositorioAcademias academias, RepositorioAsignaturas asignaturas) {
        this.academias = academias;
        this.asignaturas = asignaturas;
    }

    /**
     * Lista todas las academias persistidas.
     */
    public List<Academia> listarAcademias() throws IOException {
        return academias.cargarTodas();
    }

    /**
     * Crea una academia con nombre no vacío.
     *
     * @throws IllegalArgumentException si el nombre es inválido
     */
    public Academia crearAcademia(String nombre) throws IOException {
        validarNombre(nombre);
        return academias.crear(nombre);
    }

    /**
     * Actualiza el nombre de una academia existente.
     */
    public boolean actualizarAcademia(long id, String nombre) throws IOException {
        validarNombre(nombre);
        return academias.actualizar(id, nombre);
    }

    /**
     * Elimina una academia por id.
     */
    public boolean eliminarAcademia(long id) throws IOException {
        return academias.eliminar(id);
    }

    /**
     * Lista todas las asignaturas.
     */
    public List<Asignatura> listarAsignaturas() throws IOException {
        return asignaturas.cargarTodas();
    }

    /**
     * Crea una asignatura vinculada a una academia existente.
     */
    public Asignatura crearAsignatura(String nombre, long idAcademia) throws IOException {
        validarNombre(nombre);
        List<Academia> ac = academias.cargarTodas();
        boolean existe = ac.stream().anyMatch(a -> a.getId() == idAcademia);
        if (!existe) {
            throw new IllegalArgumentException("La academia indicada no existe.");
        }
        return asignaturas.crear(nombre, idAcademia);
    }

    /**
     * Actualiza nombre e id de academia de una asignatura.
     */
    public boolean actualizarAsignatura(long id, String nombre, long idAcademia) throws IOException {
        validarNombre(nombre);
        List<Academia> ac = academias.cargarTodas();
        boolean existe = ac.stream().anyMatch(a -> a.getId() == idAcademia);
        if (!existe) {
            throw new IllegalArgumentException("La academia indicada no existe.");
        }
        return asignaturas.actualizar(id, nombre, idAcademia);
    }

    /**
     * Elimina una asignatura por id.
     */
    public boolean eliminarAsignatura(long id) throws IOException {
        return asignaturas.eliminar(id);
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
    }
}
