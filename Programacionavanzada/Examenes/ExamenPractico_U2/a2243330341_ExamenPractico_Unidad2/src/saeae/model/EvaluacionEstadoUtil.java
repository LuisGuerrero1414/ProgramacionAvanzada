package saeae.model;

import java.util.List;

public final class EvaluacionEstadoUtil {
    private static final double UMBRAL_REPROBADO = 7.0;

    private EvaluacionEstadoUtil() {
    }

    public static double umbralReprobado() {
        return UMBRAL_REPROBADO;
    }

    public static EstadoEvaluacion calcular(Evaluacion e) {
        if (e == null) {
            return EstadoEvaluacion.SIN_INICIAR;
        }
        boolean algo = hayAlgunaCaptura(e);
        if (!algo) {
            return EstadoEvaluacion.SIN_INICIAR;
        }
        if (esCompleto(e)) {
            return EstadoEvaluacion.COMPLETO;
        }
        return EstadoEvaluacion.INCOMPLETO;
    }

    private static boolean hayAlgunaCaptura(Evaluacion e) {
        DetalleInstrumento d = e.getDetalleInstrumento();
        if (nonBlank(d.getFecha()) || nonBlank(d.getCriterios()) || nonBlank(d.getObservaciones())) {
            return true;
        }
        ProductoIntegradorData p = e.getProductoIntegrador();
        if (nonBlank(p.getPeriodo()) || nonBlank(p.getActividad()) || nonBlank(p.getAtributoEgreso())
                || nonBlank(p.getCriterioDesempeno()) || nonBlank(p.getObservaciones()) || nonBlank(p.getIndicadores())) {
            return true;
        }
        for (RubricaAlumnoFila f : e.getRubrica()) {
            if (f.filaActiva()) {
                return true;
            }
        }
        ListaCotejoData lc = e.getListaCotejo();
        if (nonBlank(lc.getProductoIntegrador()) || nonBlank(lc.getAtributoEgreso())) {
            return true;
        }
        for (ListaCotejoItem it : lc.getIndicadores()) {
            if (it.isCumple()) {
                return true;
            }
        }
        return false;
    }

    private static boolean esCompleto(Evaluacion e) {
        DetalleInstrumento d = e.getDetalleInstrumento();
        if (!nonBlank(d.getFecha()) || !nonBlank(d.getCriterios())) {
            return false;
        }
        ProductoIntegradorData p = e.getProductoIntegrador();
        if (!nonBlank(p.getPeriodo()) || !nonBlank(p.getActividad()) || !nonBlank(p.getAtributoEgreso())
                || !nonBlank(p.getCriterioDesempeno())) {
            return false;
        }
        List<RubricaAlumnoFila> rub = e.getRubrica();
        boolean algunaRubrica = false;
        for (RubricaAlumnoFila f : rub) {
            if (f.filaActiva()) {
                algunaRubrica = true;
                if (!f.filaCompleta()) {
                    return false;
                }
            }
        }
        if (!algunaRubrica) {
            return false;
        }
        ListaCotejoData lc = e.getListaCotejo();
        if (!nonBlank(lc.getProductoIntegrador()) || !nonBlank(lc.getAtributoEgreso())
                || !nonBlank(lc.getCriterioDesempeno())) {
            return false;
        }
        List<ListaCotejoItem> ind = lc.getIndicadores();
        if (ind.isEmpty()) {
            return false;
        }
        for (ListaCotejoItem it : ind) {
            if (!nonBlank(it.getDescripcion())) {
                return false;
            }
        }
        return true;
    }

    private static boolean nonBlank(String s) {
        return s != null && !s.isBlank();
    }

    public static void aplicarEstado(Evaluacion e) {
        e.setEstado(calcular(e));
    }
}
