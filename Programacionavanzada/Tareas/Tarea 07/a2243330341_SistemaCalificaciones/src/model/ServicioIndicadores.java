package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Lógica de negocio: promedios, contadores, porcentajes e indicadores de Cédula 3.3.2.
 * <p>
 * Reglas: nota mínima de aprobación {@value #NOTA_MINIMA_APROBACION}. Los porcentajes de
 * aprobación y reprobación se calculan sobre el subconjunto de alumnos con calificación
 * numérica (excluye inscritos sin calificación), para evitar división por cero y
 * porcentajes no representativos.
 */
public class ServicioIndicadores {

    /** Calificación mínima para aprobar (escala 0–10). */
    public static final double NOTA_MINIMA_APROBACION = 6.0;

    private static final Pattern CODIGO_MATERIA = Pattern.compile("\\([Gg][^)]+\\)");

    private ServicioIndicadores() {
    }

    /**
     * Agrupa por profesor, materia y letra de grupo y calcula indicadores por grupo.
     *
     * @param registros filas válidas del CSV
     * @return lista ordenada para presentación en tabla
     */
    public static List<IndicadorGrupo> calcularIndicadoresPorGrupo(List<Calificacion> registros) {
        Map<ClaveGrupo, List<Calificacion>> porGrupo = new HashMap<>();
        for (Calificacion c : registros) {
            ClaveGrupo clave = ClaveGrupo.desde(c);
            porGrupo.computeIfAbsent(clave, k -> new ArrayList<>()).add(c);
        }
        List<IndicadorGrupo> salida = new ArrayList<>();
        for (Map.Entry<ClaveGrupo, List<Calificacion>> e : porGrupo.entrySet()) {
            salida.add(construirIndicadorGrupo(e.getKey(), e.getValue()));
        }
        salida.sort(Comparator
                .comparing(IndicadorGrupo::getAsignatura, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(IndicadorGrupo::getProfesor, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(IndicadorGrupo::getGrupo, String.CASE_INSENSITIVE_ORDER));
        return salida;
    }

    /**
     * Genera indicadores de Cédula 3.3.2 para cada asignatura del catálogo que tenga
     * academia asociada y registros coincidentes en el CSV.
     *
     * @param registros  todas las filas del CSV
     * @param academias  catálogo de academias
     * @param asignaturas catálogo de asignaturas
     * @return filas para la tabla de cédula (solo asignaturas con al menos un registro coincidente)
     */
    public static List<IndicadorCedula> calcularIndicadoresCedula(
            List<Calificacion> registros,
            List<Academia> academias,
            List<Asignatura> asignaturas) {

        Map<Long, Academia> porId = new HashMap<>();
        for (Academia a : academias) {
            porId.put(a.getId(), a);
        }

        List<IndicadorCedula> resultado = new ArrayList<>();
        for (Asignatura asig : asignaturas) {
            Academia ac = porId.get(asig.getIdAcademia());
            if (ac == null) {
                continue;
            }
            List<Calificacion> deMateria = registros.stream()
                    .filter(r -> coincideMateria(r.getMateria(), asig.getNombre()))
                    .collect(Collectors.toList());
            if (deMateria.isEmpty()) {
                continue;
            }
            IndicadorCedula fila = new IndicadorCedula();
            fila.setAcademia(ac.getNombre());
            fila.setAsignatura(asig.getNombre());

            List<Double> todasLasNotas = deMateria.stream()
                    .map(Calificacion::getCalificacion)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            double promedioAsignatura = promedio(todasLasNotas);
            fila.setPromedio(promedioAsignatura);

            Map<ClaveGrupo, List<Calificacion>> grupos = new HashMap<>();
            for (Calificacion c : deMateria) {
                ClaveGrupo cg = ClaveGrupo.desde(c);
                grupos.computeIfAbsent(cg, k -> new ArrayList<>()).add(c);
            }
            fila.setNumeroGrupos(grupos.size());

            int gruposPorEncima = 0;
            for (List<Calificacion> lista : grupos.values()) {
                List<Double> notasGrupo = notasNumericas(lista);
                if (notasGrupo.isEmpty()) {
                    continue;
                }
                double promG = promedio(notasGrupo);
                if (promG > promedioAsignatura) {
                    gruposPorEncima++;
                }
            }
            int numGruposConNotas = (int) grupos.values().stream()
                    .filter(g -> !notasNumericas(g).isEmpty())
                    .count();
            double pctMayor = numGruposConNotas > 0
                    ? (100.0 * gruposPorEncima / numGruposConNotas)
                    : 0.0;
            fila.setPorcentajeMayorAlPromedio(pctMayor);

            long conNota = deMateria.stream().filter(c -> c.getCalificacion().isPresent()).count();
            long reprobados = deMateria.stream()
                    .filter(c -> c.getCalificacion().isPresent()
                            && c.getCalificacion().get() < NOTA_MINIMA_APROBACION)
                    .count();
            double pctReprob = conNota > 0 ? (100.0 * reprobados / conNota) : 0.0;
            fila.setPorcentajeReprobacion(pctReprob);

            Set<String> profs = new HashSet<>();
            for (Calificacion c : deMateria) {
                if (c.getProfesor() != null && !c.getProfesor().isBlank()) {
                    profs.add(c.getProfesor().trim());
                }
            }
            List<String> ordenados = new ArrayList<>(profs);
            ordenados.sort(String.CASE_INSENSITIVE_ORDER);
            fila.setProfesores(String.join(", ", ordenados));

            resultado.add(fila);
        }
        resultado.sort(Comparator
                .comparing(IndicadorCedula::getAcademia, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(IndicadorCedula::getAsignatura, String.CASE_INSENSITIVE_ORDER));
        return resultado;
    }

    private static IndicadorGrupo construirIndicadorGrupo(ClaveGrupo clave, List<Calificacion> filas) {
        IndicadorGrupo ig = new IndicadorGrupo();
        ig.setProfesor(clave.profesor());
        ig.setAsignatura(clave.materia());
        ig.setGrupo(clave.letra());

        int numAlumnos = filas.size();
        List<Double> notas = notasNumericas(filas);
        int conCalificacion = notas.size();

        int reprobados = 0;
        double suma = 0;
        double sumaAcreditados = 0;
        int numAcreditados = 0;
        for (Double n : notas) {
            suma += n;
            if (n < NOTA_MINIMA_APROBACION) {
                reprobados++;
            } else {
                sumaAcreditados += n;
                numAcreditados++;
            }
        }

        ig.setNumAlumnos(numAlumnos);
        ig.setNumReprobados(reprobados);
        ig.setPromedio(conCalificacion > 0 ? suma / conCalificacion : 0.0);
        ig.setPromedioAcreditados(numAcreditados > 0 ? sumaAcreditados / numAcreditados : 0.0);

        if (conCalificacion > 0) {
            int aprobados = conCalificacion - reprobados;
            ig.setPorcentajeAprobados(100.0 * aprobados / conCalificacion);
            ig.setPorcentajeReprobados(100.0 * reprobados / conCalificacion);
        } else {
            ig.setPorcentajeAprobados(0.0);
            ig.setPorcentajeReprobados(0.0);
        }
        return ig;
    }

    private static List<Double> notasNumericas(List<Calificacion> filas) {
        List<Double> r = new ArrayList<>();
        for (Calificacion c : filas) {
            c.getCalificacion().ifPresent(r::add);
        }
        return r;
    }

    private static double promedio(List<Double> valores) {
        if (valores.isEmpty()) {
            return 0.0;
        }
        double s = 0;
        for (Double v : valores) {
            s += v;
        }
        return s / valores.size();
    }

    /**
     * Coincide nombre de materia del CSV con el del catálogo (texto normalizado o código entre paréntesis).
     */
    public static boolean coincideMateria(String materiaCsv, String nombreCatalogo) {
        if (materiaCsv == null || nombreCatalogo == null) {
            return false;
        }
        String n1 = normalizarTexto(materiaCsv);
        String n2 = normalizarTexto(nombreCatalogo);
        if (n1.equalsIgnoreCase(n2)) {
            return true;
        }
        String c1 = extraerCodigoMateria(materiaCsv);
        String c2 = extraerCodigoMateria(nombreCatalogo);
        return c1 != null && c2 != null && c1.equalsIgnoreCase(c2);
    }

    private static String normalizarTexto(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    private static String extraerCodigoMateria(String materia) {
        if (materia == null) {
            return null;
        }
        Matcher m = CODIGO_MATERIA.matcher(materia);
        if (m.find()) {
            return m.group().toUpperCase(Locale.ROOT);
        }
        return null;
    }

    private record ClaveGrupo(String profesor, String materia, String letra) {
        static ClaveGrupo desde(Calificacion c) {
            String prof = c.getProfesor() != null ? c.getProfesor().trim() : "";
            String mat = c.getMateria() != null ? c.getMateria().trim() : "";
            String let = c.getLetraGrupo() != null ? c.getLetraGrupo().trim() : "";
            return new ClaveGrupo(prof, mat, let);
        }
    }
}
