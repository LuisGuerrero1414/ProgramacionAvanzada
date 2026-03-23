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

import model.Academia;

/**
 * Persistencia JSON del catálogo de academias.
 */
public class RepositorioAcademias {

    private final Path archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RepositorioAcademias(Path directorioDatos) {
        this.archivo = directorioDatos.resolve("academias.json");
    }

    /**
     * Carga todas las academias desde disco.
     *
     * @return lista (vacía si el archivo no existe)
     * @throws IOException error de lectura
     */
    public List<Academia> cargarTodas() throws IOException {
        try {
            if (!Files.isRegularFile(archivo)) {
                return new ArrayList<>();
            }
            try (Reader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
                List<Academia> lista = gson.fromJson(reader, new TypeToken<List<Academia>>() {
                }.getType());
                if (lista == null) {
                    return new ArrayList<>();
                }
                return lista;
            }
        } catch (IOException e) {
            throw new IOException("No se pudo leer el archivo de academias: " + archivo, e);
        }
    }

    /**
     * Guarda el catálogo completo.
     *
     * @param academias lista a persistir
     * @throws IOException error de escritura
     */
    public void guardarTodas(List<Academia> academias) throws IOException {
        try {
            Files.createDirectories(archivo.getParent());
            try (Writer w = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(academias, w);
            }
        } catch (IOException e) {
            throw new IOException("No se pudo guardar el archivo de academias: " + archivo, e);
        }
    }

    /**
     * Inserta una academia con id autogenerado.
     */
    public Academia crear(String nombre) throws IOException {
        List<Academia> lista = cargarTodas();
        long nuevoId = siguienteId(lista);
        Academia a = new Academia(nuevoId, nombre.trim());
        lista.add(a);
        guardarTodas(lista);
        return a;
    }

    /**
     * Actualiza nombre por id.
     *
     * @return true si existía y se guardó
     */
    public boolean actualizar(long id, String nombre) throws IOException {
        List<Academia> lista = cargarTodas();
        boolean ok = false;
        for (Academia a : lista) {
            if (a.getId() == id) {
                a.setNombre(nombre.trim());
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
     *
     * @return true si se eliminó
     */
    public boolean eliminar(long id) throws IOException {
        List<Academia> lista = cargarTodas();
        boolean rem = lista.removeIf(a -> a.getId() == id);
        if (rem) {
            guardarTodas(lista);
        }
        return rem;
    }

    private static long siguienteId(List<Academia> lista) {
        OptionalLong max = lista.stream().mapToLong(Academia::getId).max();
        return max.isEmpty() ? 1L : max.getAsLong() + 1L;
    }
}
