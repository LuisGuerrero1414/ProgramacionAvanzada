package saeae.model;

import java.util.ArrayList;
import java.util.List;

public class Evaluacion {
    private String id;
    private String asignatura;
    private String profesor;
    private String grupo;
    private EstadoEvaluacion estado = EstadoEvaluacion.SIN_INICIAR;

    private DetalleInstrumento detalleInstrumento = new DetalleInstrumento();
    private ProductoIntegradorData productoIntegrador = new ProductoIntegradorData();
    private List<RubricaAlumnoFila> rubrica = new ArrayList<>();
    private ListaCotejoData listaCotejo = new ListaCotejoData();

    public Evaluacion() {
        for (int i = 0; i < 4; i++) {
            rubrica.add(new RubricaAlumnoFila());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public EstadoEvaluacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvaluacion estado) {
        this.estado = estado;
    }

    public DetalleInstrumento getDetalleInstrumento() {
        return detalleInstrumento;
    }

    public void setDetalleInstrumento(DetalleInstrumento detalleInstrumento) {
        this.detalleInstrumento = detalleInstrumento != null ? detalleInstrumento : new DetalleInstrumento();
    }

    public ProductoIntegradorData getProductoIntegrador() {
        return productoIntegrador;
    }

    public void setProductoIntegrador(ProductoIntegradorData productoIntegrador) {
        this.productoIntegrador = productoIntegrador != null ? productoIntegrador : new ProductoIntegradorData();
    }

    public List<RubricaAlumnoFila> getRubrica() {
        return rubrica;
    }

    public void setRubrica(List<RubricaAlumnoFila> rubrica) {
        this.rubrica = rubrica != null ? rubrica : new ArrayList<>();
        while (this.rubrica.size() < 4) {
            this.rubrica.add(new RubricaAlumnoFila());
        }
        if (this.rubrica.size() > 4) {
            this.rubrica = new ArrayList<>(this.rubrica.subList(0, 4));
        }
    }

    public ListaCotejoData getListaCotejo() {
        return listaCotejo;
    }

    public void setListaCotejo(ListaCotejoData listaCotejo) {
        this.listaCotejo = listaCotejo != null ? listaCotejo : new ListaCotejoData();
    }

    public void ensureStructure() {
        while (rubrica.size() < 4) {
            rubrica.add(new RubricaAlumnoFila());
        }
        while (rubrica.size() > 4) {
            rubrica.remove(rubrica.size() - 1);
        }
        if (listaCotejo == null) {
            listaCotejo = new ListaCotejoData();
        }
        if (listaCotejo.getIndicadores() == null) {
            listaCotejo.setIndicadores(new java.util.ArrayList<>());
        }
        while (listaCotejo.getIndicadores().size() < 4) {
            int n = listaCotejo.getIndicadores().size() + 1;
            listaCotejo.getIndicadores().add(new ListaCotejoItem("Indicador " + n, false));
        }
        while (listaCotejo.getIndicadores().size() > 4) {
            listaCotejo.getIndicadores().remove(listaCotejo.getIndicadores().size() - 1);
        }
    }
}
