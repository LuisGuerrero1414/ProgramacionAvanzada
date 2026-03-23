package persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import model.Calificacion;

/**
 * Lectura del archivo CSV de inscripción y calificaciones.
 */
public class LectorCsvCalificaciones {

    private static final String COL_DA = "D.A.";
    private static final String COL_LETRA = "LETRA";
    private static final String COL_PROFESOR = "PROFESOR";
    private static final String COL_MATERIA = "MATERIA";
    private static final String COL_PERIODO_MATERIA = "Periodo Materia";
    private static final String COL_NO_ACTA = "No.ACTA";
    private static final String COL_MATRICULA = "MATRICULA";
    private static final String COL_ALUMNO = "ALUMNO";
    private static final String COL_CALIFICACION = "CALIFICACION";
    private static final String COL_OP_INS = "OP.INS.";
    private static final String COL_OP_EXA = "OP.EXA.";
    private static final String COL_MES = "MES";
    private static final String COL_CICLO = "CICLO ESCOLAR";
    private static final String COL_PEDUCATIVO = "P.EDUCATIVO";

    private LectorCsvCalificaciones() {
    }

    /**
     * Lee todas las filas del CSV y las convierte en {@link Calificacion}.
     * Intenta UTF-8 y, si falla por codificación, reintenta con Windows-1252.
     *
     * @param ruta archivo CSV
     * @return lista de registros (puede estar vacía si solo hay encabezado)
     * @throws IOException si no se puede leer el archivo con ningún charset
     */
    public static List<Calificacion> leer(Path ruta) throws IOException {
        try {
            return leerConCharset(ruta, StandardCharsets.UTF_8);
        } catch (IOException e) {
            if (esErrorCodificacion(e)) {
                return leerConCharset(ruta, Charset.forName("Windows-1252"));
            }
            throw e;
        }
    }

    private static boolean esErrorCodificacion(IOException e) {
        Throwable t = e;
        while (t != null) {
            if (t instanceof CharacterCodingException) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    private static List<Calificacion> leerConCharset(Path ruta, Charset charset) throws IOException {
        List<Calificacion> lista = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(ruta, charset)) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return lista;
            }
            if (!headerLine.isEmpty() && headerLine.charAt(0) == '\uFEFF') {
                headerLine = headerLine.substring(1);
            }
            Map<String, Integer> idx = indiceColumnas(partirLineaCsv(headerLine));
            validarColumnasMinimas(idx);

            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                List<String> celdas = partirLineaCsv(linea);
                Calificacion c = mapearFila(celdas, idx);
                lista.add(c);
            }
        }
        return lista;
    }

    private static void validarColumnasMinimas(Map<String, Integer> idx) throws IOException {
        if (!idx.containsKey(normalizarNombreColumna(COL_PROFESOR))
                || !idx.containsKey(normalizarNombreColumna(COL_MATERIA))
                || !idx.containsKey(normalizarNombreColumna(COL_LETRA))
                || !idx.containsKey(normalizarNombreColumna(COL_CALIFICACION))) {
            throw new IOException("El CSV no contiene las columnas requeridas (PROFESOR, MATERIA, LETRA, CALIFICACION).");
        }
    }

    private static Map<String, Integer> indiceColumnas(List<String> nombres) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < nombres.size(); i++) {
            map.put(normalizarNombreColumna(nombres.get(i)), i);
        }
        return map;
    }

    private static String normalizarNombreColumna(String s) {
        return s != null ? s.trim().toUpperCase(Locale.ROOT) : "";
    }

    private static Calificacion mapearFila(List<String> celdas, Map<String, Integer> idx) {
        Calificacion c = new Calificacion();
        c.setDepartamentoAcademico(valor(celdas, idx, COL_DA));
        c.setProgramaEducativo(valor(celdas, idx, COL_PEDUCATIVO));
        c.setLetraGrupo(valor(celdas, idx, COL_LETRA));
        c.setProfesor(valor(celdas, idx, COL_PROFESOR));
        c.setMateria(valor(celdas, idx, COL_MATERIA));
        c.setPeriodoMateria(valor(celdas, idx, COL_PERIODO_MATERIA));
        c.setNumeroActa(valor(celdas, idx, COL_NO_ACTA));
        c.setMatricula(valor(celdas, idx, COL_MATRICULA));
        c.setAlumno(valor(celdas, idx, COL_ALUMNO));
        c.setOpIns(valor(celdas, idx, COL_OP_INS));
        c.setOpExa(valor(celdas, idx, COL_OP_EXA));
        c.setMes(valor(celdas, idx, COL_MES));
        c.setCicloEscolar(valor(celdas, idx, COL_CICLO));

        String calStr = valor(celdas, idx, COL_CALIFICACION);
        if (calStr == null || calStr.isBlank()) {
            c.setCalificacion(Optional.empty());
        } else {
            try {
                double v = Double.parseDouble(calStr.trim().replace(',', '.'));
                c.setCalificacion(Optional.of(v));
            } catch (NumberFormatException ex) {
                c.setCalificacion(Optional.empty());
            }
        }
        return c;
    }

    private static String valor(List<String> celdas, Map<String, Integer> idx, String columna) {
        Integer i = idx.get(normalizarNombreColumna(columna));
        if (i == null || i >= celdas.size()) {
            return "";
        }
        String v = celdas.get(i);
        return v != null ? v.trim() : "";
    }

    /**
     * Divide una línea CSV respetando comillas dobles.
     */
    static List<String> partirLineaCsv(String linea) {
        List<String> resultado = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean entreComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char ch = linea.charAt(i);
            if (ch == '"') {
                entreComillas = !entreComillas;
            } else if (ch == ',' && !entreComillas) {
                resultado.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(ch);
            }
        }
        resultado.add(actual.toString());
        return resultado;
    }
}
