package model;

import java.util.Optional;

/**
 * Representa un registro individual del archivo CSV de calificaciones.
 */
public class Calificacion {

    private String departamentoAcademico;
    private String programaEducativo;
    private String letraGrupo;
    private String profesor;
    private String materia;
    private String periodoMateria;
    private String numeroActa;
    private String matricula;
    private String alumno;
    /** Vacío si el alumno no tiene calificación registrada. */
    private Optional<Double> calificacion;
    private String opIns;
    private String opExa;
    private String mes;
    private String cicloEscolar;

    public Calificacion() {
        this.calificacion = Optional.empty();
    }

    public String getDepartamentoAcademico() {
        return departamentoAcademico;
    }

    public void setDepartamentoAcademico(String departamentoAcademico) {
        this.departamentoAcademico = departamentoAcademico;
    }

    public String getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(String programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public String getLetraGrupo() {
        return letraGrupo;
    }

    public void setLetraGrupo(String letraGrupo) {
        this.letraGrupo = letraGrupo;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getPeriodoMateria() {
        return periodoMateria;
    }

    public void setPeriodoMateria(String periodoMateria) {
        this.periodoMateria = periodoMateria;
    }

    public String getNumeroActa() {
        return numeroActa;
    }

    public void setNumeroActa(String numeroActa) {
        this.numeroActa = numeroActa;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public Optional<Double> getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Optional<Double> calificacion) {
        this.calificacion = calificacion;
    }

    public String getOpIns() {
        return opIns;
    }

    public void setOpIns(String opIns) {
        this.opIns = opIns;
    }

    public String getOpExa() {
        return opExa;
    }

    public void setOpExa(String opExa) {
        this.opExa = opExa;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(String cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }
}
