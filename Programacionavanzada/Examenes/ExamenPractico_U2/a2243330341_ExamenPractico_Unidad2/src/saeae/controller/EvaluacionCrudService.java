package saeae.controller;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import saeae.model.Evaluacion;
import saeae.model.EvaluacionEstadoUtil;
import saeae.model.EvaluacionesRoot;

public class EvaluacionCrudService {

    private final Path archivoJson;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public EvaluacionCrudService(Path archivoJson) {
        this.archivoJson = archivoJson;
    }

    public Path getArchivoJson() {
        return archivoJson;
    }

    public EvaluacionesRoot cargarTodo() throws IOException {
        if (!Files.isRegularFile(archivoJson)) {
            return new EvaluacionesRoot();
        }
        try (Reader r = Files.newBufferedReader(archivoJson, StandardCharsets.UTF_8)) {
            EvaluacionesRoot root = gson.fromJson(r, EvaluacionesRoot.class);
            if (root == null) {
                root = new EvaluacionesRoot();
            }
            for (Evaluacion e : root.getEvaluaciones()) {
                if (e.getDetalleInstrumento() == null) {
                    e.setDetalleInstrumento(new saeae.model.DetalleInstrumento());
                }
                if (e.getProductoIntegrador() == null) {
                    e.setProductoIntegrador(new saeae.model.ProductoIntegradorData());
                }
                if (e.getListaCotejo() == null) {
                    e.setListaCotejo(new saeae.model.ListaCotejoData());
                }
                if (e.getRubrica() == null) {
                    e.setRubrica(new java.util.ArrayList<>());
                }
                e.setRubrica(e.getRubrica());
                e.ensureStructure();
                EvaluacionEstadoUtil.aplicarEstado(e);
            }
            return root;
        }
    }

    private void guardarRoot(EvaluacionesRoot root) throws IOException {
        Path parent = archivoJson.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (Writer w = Files.newBufferedWriter(archivoJson, StandardCharsets.UTF_8)) {
            gson.toJson(root, w);
        }
    }

    public void guardar(Evaluacion evaluacion, String idAnteriorOPersistido) throws IOException {
        Objects.requireNonNull(evaluacion, "evaluacion");
        EvaluacionesRoot root = cargarTodo();
        List<Evaluacion> list = root.getEvaluaciones();
        String idNuevo = FileNameUtil.generarIdEvaluacion(
                evaluacion.getAsignatura(), evaluacion.getProfesor(), evaluacion.getGrupo());
        evaluacion.setId(idNuevo);
        validarDuplicadoTerna(list, evaluacion);
        EvaluacionEstadoUtil.aplicarEstado(evaluacion);
        if (idAnteriorOPersistido != null && !idAnteriorOPersistido.isBlank()
                && !idAnteriorOPersistido.equals(idNuevo)) {
            list.removeIf(x -> idAnteriorOPersistido.equals(x.getId()));
        }
        list.removeIf(x -> idNuevo.equals(x.getId()));
        list.add(evaluacion);
        guardarRoot(root);
    }

    public void eliminar(String id) throws IOException {
        if (id == null || id.isBlank()) {
            return;
        }
        EvaluacionesRoot root = cargarTodo();
        root.getEvaluaciones().removeIf(e -> id.equals(e.getId()));
        guardarRoot(root);
    }

    public Optional<Evaluacion> buscarPorTerna(String asignatura, String profesor, String grupo) throws IOException {
        EvaluacionesRoot root = cargarTodo();
        return root.getEvaluaciones().stream()
                .filter(e -> mismaTerna(e, asignatura, profesor, grupo))
                .findFirst();
    }

    public Optional<Evaluacion> buscarPorId(String id) throws IOException {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        EvaluacionesRoot root = cargarTodo();
        return root.getEvaluaciones().stream().filter(e -> id.equals(e.getId())).findFirst();
    }

    static boolean mismaTerna(Evaluacion e, String asignatura, String profesor, String grupo) {
        return key(e.getAsignatura()).equals(key(asignatura))
                && key(e.getProfesor()).equals(key(profesor))
                && key(e.getGrupo()).equals(key(grupo));
    }

    static String key(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    private static void validarDuplicadoTerna(List<Evaluacion> list, Evaluacion candidata)
            throws IllegalStateException {
        String cid = candidata.getId();
        for (Evaluacion o : list) {
            if (cid != null && cid.equals(o.getId())) {
                continue;
            }
            if (mismaTerna(o, candidata.getAsignatura(), candidata.getProfesor(), candidata.getGrupo())) {
                throw new IllegalStateException(
                        "Ya existe una evaluación para la misma asignatura, profesor y grupo.");
            }
        }
    }
}
