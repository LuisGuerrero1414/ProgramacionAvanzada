package model;

/**
 * Indicadores consolidados para la Cédula 3.3.2 por academia y asignatura del catálogo.
 */
public class IndicadorCedula {

    private String academia;
    private String asignatura;
    private int numeroGrupos;
    private double promedio;
    private double porcentajeMayorAlPromedio;
    private double porcentajeReprobacion;
    private String profesores;

    public IndicadorCedula() {
    }

    public String getAcademia() {
        return academia;
    }

    public void setAcademia(String academia) {
        this.academia = academia;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public int getNumeroGrupos() {
        return numeroGrupos;
    }

    public void setNumeroGrupos(int numeroGrupos) {
        this.numeroGrupos = numeroGrupos;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public double getPorcentajeMayorAlPromedio() {
        return porcentajeMayorAlPromedio;
    }

    public void setPorcentajeMayorAlPromedio(double porcentajeMayorAlPromedio) {
        this.porcentajeMayorAlPromedio = porcentajeMayorAlPromedio;
    }

    public double getPorcentajeReprobacion() {
        return porcentajeReprobacion;
    }

    public void setPorcentajeReprobacion(double porcentajeReprobacion) {
        this.porcentajeReprobacion = porcentajeReprobacion;
    }

    public String getProfesores() {
        return profesores;
    }

    public void setProfesores(String profesores) {
        this.profesores = profesores;
    }
}
