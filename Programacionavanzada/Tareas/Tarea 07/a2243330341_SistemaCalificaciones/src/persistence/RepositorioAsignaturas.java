package persistence;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.Asignatura;

/**
 * Persistencia JSON del catálogo de asignaturas.
 */
public class RepositorioAsignaturas {

    private final Path archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RepositorioAsignaturas(Path directorioDatos) {
        this.archivo = directorioDatos.resolve("asignaturas.json");
    }

    /**
     * Carga todas las asignaturas desde disco.
     */
    public List<Asignatura> cargarTodas() throws IOException {
        try {
            if (!Files.isRegularFile(archivo)) {
                return new ArrayList<>();
            }
            try (Reader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
                List<Asignatura> lista = gson.fromJson(reader, new TypeToken<List<Asignatura>>() {
                }.getType());
                if (lista == null) {
                    return new ArrayList<>();
                }
                return lista;
            }
        } catch (IOException e) {
            throw new IOException("No se pudo leer el archivo de asignaturas: " + archivo, e);
        }
    }

    /**
     * Guarda el catálogo completo.
     */
    public void guardarTodas(List<Asignatura> asignaturas) throws IOException {
        try {
            Files.createDirectories(archivo.getParent());
            try (Writer w = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(asignaturas, w);
            }
        } catch (IOException e) {
            throw new IOException("No se pudo guardar el archivo de asignaturas: " + archivo, e);
        }
    }

    /**
     * Inserta una asignatura con id autogenerado.
     */
    public Asignatura crear(String nombre, long idAcademia) throws IOException {
        List<Asignatura> lista = cargarTodas();
        long nuevoId = siguienteId(lista);
        Asignatura a = new Asignatura(nuevoId, nombre.trim(), idAcademia);
        lista.add(a);
        guardarTodas(lista);
        return a;
    }

    /**
     * Actualiza nombre e id de academia.
     */
    public boolean actualizar(long id, String nombre, long idAcademia) throws IOException {
        List<Asignatura> lista = cargarTodas();
        boolean ok = false;
        for (Asignatura a : lista) {
            if (a.getId() == id) {
                a.setNombre(nombre.trim());
                a.setIdAcademia(idAcademia);
                ok = true;
                break;
            }
        }
        if (ok) {
            guardarTodas(lista);
        }
        return ok;
    }

    /**
     * Elimina por id.
     */
    public boolean eliminar(long id) throws IOException {
        List<Asignatura> lista = cargarTodas();
        boolean rem = lista.removeIf(a -> a.getId() == id);
        if (rem) {
            guardarTodas(lista);
        }
        return rem;
    }

    private static long siguienteId(List<Asignatura> lista) {
        OptionalLong max = lista.stream().mapToLong(Asignatura::getId).max();
        return max.isEmpty() ? 1L : max.getAsLong() + 1L;
    }
}
