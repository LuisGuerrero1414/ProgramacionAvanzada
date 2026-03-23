package controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import model.Academia;
import model.Asignatura;
import model.Calificacion;
import model.IndicadorCedula;
import model.IndicadorGrupo;
import model.ServicioIndicadores;
import persistence.LectorCsvCalificaciones;
import persistence.RepositorioAcademias;
import persistence.RepositorioAsignaturas;

/**
 * Orquesta la carga del CSV, los repositorios de catálogo y la generación de indicadores.
 */
public class AppController {

    private final RepositorioAcademias repositorioAcademias;
    private final RepositorioAsignaturas repositorioAsignaturas;

    private Path rutaCsv;
    private List<Calificacion> registrosEnMemoria = Collections.emptyList();

    public AppController(RepositorioAcademias repositorioAcademias, RepositorioAsignaturas repositorioAsignaturas) {
        this.repositorioAcademias = repositorioAcademias;
        this.repositorioAsignaturas = repositorioAsignaturas;
    }

    /**
     * Define la ruta del archivo CSV seleccionado por el usuario (aún no leído).
     *
     * @param ruta ruta absoluta o relativa al archivo
     */
    public void establecerRutaCsv(Path ruta) {
        this.rutaCsv = ruta;
    }

    /**
     * @return ruta del CSV actual o null si no se ha elegido archivo
     */
    public Path getRutaCsv() {
        return rutaCsv;
    }

    /**
     * Lee el CSV y almacena los registros en memoria para cálculos posteriores.
     *
     * @throws IOException error de lectura o formato
     */
    public void cargarCsv() throws IOException {
        if (rutaCsv == null) {
            throw new IOException("No hay archivo CSV seleccionado.");
        }
        registrosEnMemoria = LectorCsvCalificaciones.leer(rutaCsv);
    }

    /**
     * @return registros cargados desde el último {@link #cargarCsv()}
     */
    public List<Calificacion> getRegistrosEnMemoria() {
        return registrosEnMemoria;
    }

    /**
     * Calcula la tabla de promedios por grupo a partir de los datos cargados.
     *
     * @return filas para {@code JTable}; vacía si no hay registros
     */
    public List<IndicadorGrupo> calcularPromediosPorGrupo() {
        if (registrosEnMemoria.isEmpty()) {
            return Collections.emptyList();
        }
        return ServicioIndicadores.calcularIndicadoresPorGrupo(registrosEnMemoria);
    }

    /**
     * Genera los indicadores de Cédula 3.3.2 cruzando CSV con catálogos JSON.
     *
     * @return filas para la segunda tabla
     * @throws IOException si no se pueden leer los catálogos
     */
    public List<IndicadorCedula> generarCedula332() throws IOException {
        List<Academia> academias = repositorioAcademias.cargarTodas();
        List<Asignatura> asignaturas = repositorioAsignaturas.cargarTodas();
        if (registrosEnMemoria.isEmpty()) {
            return Collections.emptyList();
        }
        return ServicioIndicadores.calcularIndicadoresCedula(registrosEnMemoria, academias, asignaturas);
    }
}
