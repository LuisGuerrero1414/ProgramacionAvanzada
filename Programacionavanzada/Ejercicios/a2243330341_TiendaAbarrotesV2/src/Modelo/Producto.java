package Modelo;

public abstract class Producto {
    private int id;
    private String sku;
    private String nombre;
    private double precioCompra;
    private double porcentajeGanancia;
    private String categoria;
    private String subcategoria;
    private String rutaImagen;
    private String unidadMedida;
    private Integer cantidad;

    public Producto(int id, String sku, String nombre, double precioCompra, double porcentajeGanancia, String categoria,
            String subcategoria, String rutaImagen, String unidadMedida, Integer cantidad) {
        this.id = id;
        this.sku = (sku == null || sku.isBlank()) ? String.valueOf(id) : sku;
        this.nombre = nombre;
        this.precioCompra = Math.max(0.0, precioCompra);
        this.porcentajeGanancia = Math.max(0.0, porcentajeGanancia);
        this.categoria = categoria;
        this.subcategoria = (subcategoria == null) ? "" : subcategoria;
        this.rutaImagen = rutaImagen;
        this.unidadMedida = (unidadMedida == null || unidadMedida.isBlank()) ? "pieza" : unidadMedida;
        this.cantidad = cantidad;
    }

    public int getId() { return id; }
    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public double getPrecioCompra() { return precioCompra; }
    public double getPorcentajeGanancia() { return porcentajeGanancia; }
    public String getCategoria() { return categoria; }
    public String getSubcategoria() { return subcategoria == null ? "" : subcategoria; }
    public String getRutaImagen() { return rutaImagen; }
    public String getUnidadMedida() { return unidadMedida; }

    /** Compatibilidad con código existente: precio equivale a precio de venta. */
    public double getPrecio() { return getPrecioVenta(); }

    /** Calcula el precio de venta exclusivamente en el modelo. */
    public double getPrecioVenta() {
        return precioCompra + (precioCompra * (porcentajeGanancia / 100.0));
    }

    public int getCantidad() {
        return cantidad != null ? cantidad : 10;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = Math.max(0.0, precioCompra);
    }

    public void setPorcentajeGanancia(double porcentajeGanancia) {
        this.porcentajeGanancia = Math.max(0.0, porcentajeGanancia);
    }

    public void setSku(String sku) {
        this.sku = (sku == null || sku.isBlank()) ? this.sku : sku;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = (unidadMedida == null || unidadMedida.isBlank()) ? this.unidadMedida : unidadMedida;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = (subcategoria == null) ? "" : subcategoria;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public abstract String getTipoAlmacenamiento();
}
