package modelo;

public class Asignatura {

    private String CARRERA;
    private String PLAN_ESTUDIO; 
    private String IDMATERIA;    
    private String MATERIA;     

    public Asignatura() {
    }

    public String getCarrera() { return CARRERA; }
    public void setCarrera(String carrera) { this.CARRERA = carrera; }

    public String getPlanEstudio() { return PLAN_ESTUDIO; }
    public void setPlanEstudio(String plan) { this.PLAN_ESTUDIO = plan; }

    public String getIdMateria() { return IDMATERIA; }
    public void setIdMateria(String id) { this.IDMATERIA = id; }

    public String getMateria() { return MATERIA; }
    public void setMateria(String materia) { this.MATERIA = materia; }
}