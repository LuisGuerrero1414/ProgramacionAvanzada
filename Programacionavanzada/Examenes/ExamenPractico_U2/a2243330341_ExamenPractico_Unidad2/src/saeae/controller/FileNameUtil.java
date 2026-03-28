package saeae.controller;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class FileNameUtil {
    private static final Pattern INVALID = Pattern.compile("[\\\\/:*?\"<>|]");

    private FileNameUtil() {
    }

    public static String nombreArchivoReporte(String asignatura, String profesor, String grupo) {
        String base = safePart(asignatura) + "_" + safePart(profesor) + "_" + safePart(grupo) + ".xlsx";
        return INVALID.matcher(base).replaceAll("_").replaceAll("_{2,}", "_");
    }

    public static String safePart(String s) {
        if (s == null) {
            return "";
        }
        String n = Normalizer.normalize(s.trim(), Normalizer.Form.NFD);
        n = n.replaceAll("\\p{M}+", "");
        n = INVALID.matcher(n).replaceAll("_");
        n = n.replaceAll("\\s+", "_");
        if (n.isEmpty()) {
            return "X";
        }
        return n;
    }

    public static String generarIdEvaluacion(String asignatura, String profesor, String grupo) {
        String a = safePart(asignatura);
        String p = safePart(profesor);
        String g = safePart(grupo);
        return a + "_" + p + "_" + g;
    }
}
