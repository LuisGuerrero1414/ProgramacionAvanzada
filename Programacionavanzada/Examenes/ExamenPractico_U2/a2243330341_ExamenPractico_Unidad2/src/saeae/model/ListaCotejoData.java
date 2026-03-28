package saeae.model;

import java.util.ArrayList;
import java.util.List;

public class ListaCotejoData {
    private String productoIntegrador;
    private String atributoEgreso;
    private String criterioDesempeno;
    private List<ListaCotejoItem> indicadores = new ArrayList<>();

    public String getProductoIntegrador() {
        return productoIntegrador;
    }

    public void setProductoIntegrador(String productoIntegrador) {
        this.productoIntegrador = productoIntegrador;
    }

    public String getAtributoEgreso() {
        return atributoEgreso;
    }

    public void setAtributoEgreso(String atributoEgreso) {
        this.atributoEgreso = atributoEgreso;
    }

    public String getCriterioDesempeno() {
        return criterioDesempeno;
    }

    public void setCriterioDesempeno(String criterioDesempeno) {
        this.criterioDesempeno = criterioDesempeno;
    }

    public List<ListaCotejoItem> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<ListaCotejoItem> indicadores) {
        this.indicadores = indicadores != null ? indicadores : new ArrayList<>();
    }
}
