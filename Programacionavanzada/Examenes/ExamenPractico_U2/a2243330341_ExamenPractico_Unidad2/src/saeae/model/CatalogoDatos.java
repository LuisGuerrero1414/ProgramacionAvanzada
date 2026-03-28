package saeae.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CatalogoDatos {
    private final List<String> profesoresOrdenados = new ArrayList<>();
    private final Map<String, Set<String>> materiasPorProfesor = new LinkedHashMap<>();
    private final Map<String, Set<String>> gruposPorProfesorMateria = new LinkedHashMap<>();
    private final Map<String, List<RegistroAlumno>> alumnosPorTerna = new LinkedHashMap<>();

    public List<String> getProfesoresOrdenados() {
        return profesoresOrdenados;
    }

    public Map<String, Set<String>> getMateriasPorProfesor() {
        return materiasPorProfesor;
    }

    public Map<String, Set<String>> getGruposPorProfesorMateria() {
        return gruposPorProfesorMateria;
    }

    public Map<String, List<RegistroAlumno>> getAlumnosPorTerna() {
        return alumnosPorTerna;
    }

    public static String keyProfesorMateria(String profesor, String materia) {
        return profesor.trim().toLowerCase() + "||" + materia.trim().toLowerCase();
    }

    public static String keyTerna(String profesor, String materia, String grupo) {
        return profesor.trim().toLowerCase() + "||" + materia.trim().toLowerCase() + "||" + grupo.trim().toLowerCase();
    }

    public List<String> materiasDe(String profesor) {
        Set<String> set = materiasPorProfesor.getOrDefault(profesor, Collections.emptySet());
        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        return list;
    }

    public List<String> gruposDe(String profesor, String materia) {
        String k = keyProfesorMateria(profesor, materia);
        Set<String> set = gruposPorProfesorMateria.getOrDefault(k, Collections.emptySet());
        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        return list;
    }

    public List<RegistroAlumno> alumnosDe(String profesor, String materia, String grupo) {
        String k = keyTerna(profesor, materia, grupo);
        return alumnosPorTerna.getOrDefault(k, List.of());
    }

    public void addFilaRaw(String letraGrupo, String profesor, String materia, String matricula, String alumno,
            String numGrupo) {
        String prof = profesor != null ? profesor.trim() : "";
        String mat = materia != null ? materia.trim() : "";
        String letra = letraGrupo != null ? letraGrupo.trim() : "";
        String ngr = numGrupo != null ? numGrupo.trim() : "";
        if (prof.isEmpty() || mat.isEmpty()) {
            return;
        }
        String grupoUi = (letra + "-" + ngr).replaceAll("^-+", "").replaceAll("-+$", "");
        if (grupoUi.isEmpty()) {
            grupoUi = ngr;
        }

        if (!profesoresOrdenados.contains(prof)) {
            profesoresOrdenados.add(prof);
        }
        materiasPorProfesor.computeIfAbsent(prof, p -> new LinkedHashSet<>()).add(mat);
        String pm = keyProfesorMateria(prof, mat);
        gruposPorProfesorMateria.computeIfAbsent(pm, x -> new LinkedHashSet<>()).add(grupoUi);

        RegistroAlumno ra = new RegistroAlumno(matricula, alumno);
        String terna = keyTerna(prof, mat, grupoUi);
        alumnosPorTerna.computeIfAbsent(terna, x -> new ArrayList<>());
        List<RegistroAlumno> lista = alumnosPorTerna.get(terna);
        if (!lista.contains(ra)) {
            lista.add(ra);
        }
    }

    public void finalizarOrden() {
        Collections.sort(profesoresOrdenados);
    }
}
