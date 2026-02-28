package Parte2Modelo;

public class Categoria {
    private String idcategoria;
    private String categoria;

    // Constructor
    public Categoria(String idcategoria, String categoria) {
        this.idcategoria = idcategoria;
        this.categoria = categoria;
    }

    // Getter y Setter para idcategoria
    public String getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(String idcategoria) {
        this.idcategoria = idcategoria;
    }

    // Getter y Setter para categoria
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Sobreescritura del método toString
    @Override
    public String toString() {
        return this.getCategoria();
    }
}