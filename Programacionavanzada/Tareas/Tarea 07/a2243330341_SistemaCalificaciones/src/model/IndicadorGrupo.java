package model;

/**
 * Resultados agregados por profesor, asignatura y grupo (primera tabla).
 */
public class IndicadorGrupo {

    private String profesor;
    private String asignatura;
    private String grupo;
    private double promedio;
    private int numAlumnos;
    private int numReprobados;
    private double porcentajeAprobados;
    private double porcentajeReprobados;
    private double promedioAcreditados;

    public IndicadorGrupo() {
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public int getNumAlumnos() {
        return numAlumnos;
    }

    public void setNumAlumnos(int numAlumnos) {
        this.numAlumnos = numAlumnos;
    }

    public int getNumReprobados() {
        return numReprobados;
    }

    public void setNumReprobados(int numReprobados) {
        this.numReprobados = numReprobados;
    }

    public double getPorcentajeAprobados() {
        return porcentajeAprobados;
    }

    public void setPorcentajeAprobados(double porcentajeAprobados) {
        this.porcentajeAprobados = porcentajeAprobados;
    }

    public double getPorcentajeReprobados() {
        return porcentajeReprobados;
    }

    public void setPorcentajeReprobados(double porcentajeReprobados) {
        this.porcentajeReprobados = porcentajeReprobados;
    }

    public double getPromedioAcreditados() {
        return promedioAcreditados;
    }

    public void setPromedioAcreditados(double promedioAcreditados) {
        this.promedioAcreditados = promedioAcreditados;
    }
}
