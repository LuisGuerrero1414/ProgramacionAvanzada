package saeae.model;

import java.util.ArrayList;
import java.util.List;

public class EvaluacionesRoot {
    private List<Evaluacion> evaluaciones = new ArrayList<>();

    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones != null ? evaluaciones : new ArrayList<>();
    }
}
