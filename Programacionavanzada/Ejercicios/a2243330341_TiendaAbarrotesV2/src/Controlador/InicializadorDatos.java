package Controlador;

import Modelo.*;
import Persistencia.PersistenciaDatos;
import java.io.File;
import java.util.ArrayList;

public class InicializadorDatos {

    private static final double PORCENTAJE_GANANCIA_INICIAL = 35.0;
    private static final int STOCK_INICIAL = 10;
    private static final long SKU_BASE = 7501000000000L;

    /** Ruta de imagen: nombre de archivo (sin extensión) debe coincidir con el SKU. */
    private static String rutaImagenPorSku(String sku) {
        return "src/img/" + sku + ".jpg";
    }

    public static void cargarTodo(GestionProducto inventario) {

        File file = new File("inventario.json");
        if (file.exists() && file.length() > 0) {
            return;
        }

        ArrayList<Producto> lista = inventario.getLista();
        int id = 1;
        id = agregarSubcategoria(lista, id, "Despensa Básica", "Arroz/frijol", "kg", 22, "Arroz Frijol Mixto", false);
        id = agregarSubcategoria(lista, id, "Despensa Básica", "Aceites", "lt", 42, "Aceite Comestible", false);
        id = agregarSubcategoria(lista, id, "Despensa Básica", "Pastas", "pieza", 14, "Pasta Seca", false);
        id = agregarSubcategoria(lista, id, "Despensa Básica", "Harinas", "kg", 20, "Harina para Cocina", false);
        id = agregarSubcategoria(lista, id, "Despensa Básica", "Azúcar", "kg", 24, "Azúcar Refinada", false);

        id = agregarSubcategoria(lista, id, "Lácteos y Huevo", "Leche", "pieza", 28, "Leche Entera", false);
        id = agregarSubcategoria(lista, id, "Lácteos y Huevo", "Huevos", "pieza", 70, "Huevo Fresco", false);
        id = agregarSubcategoria(lista, id, "Lácteos y Huevo", "Quesos", "pieza", 54, "Queso para Mesa", false);
        id = agregarSubcategoria(lista, id, "Lácteos y Huevo", "Yogur", "pieza", 30, "Yogur Natural", false);
        id = agregarSubcategoria(lista, id, "Lácteos y Huevo", "Mantequillas", "pieza", 34, "Mantequilla", false);

        id = agregarSubcategoria(lista, id, "Bebidas y Líquidos", "Agua", "pieza", 12, "Agua Purificada", false);
        id = agregarSubcategoria(lista, id, "Bebidas y Líquidos", "Refrescos", "pieza", 18, "Refresco Sabor", false);
        id = agregarSubcategoria(lista, id, "Bebidas y Líquidos", "Jugos", "pieza", 20, "Jugo Frutal", false);
        id = agregarSubcategoria(lista, id, "Bebidas y Líquidos", "Bebidas energéticas", "pieza", 24, "Bebida Energética", false);
        id = agregarSubcategoria(lista, id, "Bebidas y Líquidos", "Café/té", "pieza", 30, "Café Té Mezcla", false);

        id = agregarSubcategoria(lista, id, "Botanas y Dulces", "Papas", "pieza", 12, "Papas Botana", false);
        id = agregarSubcategoria(lista, id, "Botanas y Dulces", "Galletas", "pieza", 13, "Galleta Dulce", false);
        id = agregarSubcategoria(lista, id, "Botanas y Dulces", "Dulces", "pieza", 9, "Dulce Tradicional", false);
        id = agregarSubcategoria(lista, id, "Botanas y Dulces", "Frutos secos", "pieza", 22, "Fruto Seco", false);
        id = agregarSubcategoria(lista, id, "Botanas y Dulces", "Chocolates y caramelos", "pieza", 18, "Chocolate Caramelo", false);

        id = agregarSubcategoria(lista, id, "Frutas y Verduras", "Frutas", "kg", 26, "Fruta Fresca", false);
        id = agregarSubcategoria(lista, id, "Frutas y Verduras", "Verduras", "kg", 18, "Verdura Fresca", false);
        id = agregarSubcategoria(lista, id, "Frutas y Verduras", "Hierbas", "pieza", 10, "Hierbas de Cocina", false);
        id = agregarSubcategoria(lista, id, "Frutas y Verduras", "Corte/conveniencia", "pieza", 16, "Corte y Conveniencia", false);
        id = agregarSubcategoria(lista, id, "Frutas y Verduras", "Tubérculos y raíces", "kg", 20, "Tubérculo y Raíz", false);

        id = agregarSubcategoria(lista, id, "Carnes y Salchichonería", "Pollo", "kg", 96, "Pollo Fresco", true);
        id = agregarSubcategoria(lista, id, "Carnes y Salchichonería", "Res", "kg", 145, "Carne de Res", true);
        id = agregarSubcategoria(lista, id, "Carnes y Salchichonería", "Cerdo", "kg", 112, "Carne de Cerdo", true);
        id = agregarSubcategoria(lista, id, "Carnes y Salchichonería", "Jamones", "kg", 80, "Jamón y Embutido", true);
        id = agregarSubcategoria(lista, id, "Carnes y Salchichonería", "Pescados", "kg", 130, "Pescado Fresco", true);

        id = agregarSubcategoria(lista, id, "Cuidado del Hogar", "Detergentes", "pieza", 46, "Detergente Hogar", false);
        id = agregarSubcategoria(lista, id, "Cuidado del Hogar", "Limpiadores", "pieza", 42, "Limpiador Hogar", false);
        id = agregarSubcategoria(lista, id, "Cuidado del Hogar", "Papel higiénico", "pieza", 38, "Papel Higiénico", false);
        id = agregarSubcategoria(lista, id, "Cuidado del Hogar", "Desechables", "pieza", 28, "Desechable Hogar", false);
        id = agregarSubcategoria(lista, id, "Cuidado del Hogar", "Insecticidas", "pieza", 52, "Insecticida Hogar", false);

        id = agregarSubcategoria(lista, id, "Higiene y Cuidado Personal", "Jabones", "pieza", 18, "Jabón Personal", false);
        id = agregarSubcategoria(lista, id, "Higiene y Cuidado Personal", "Cuidado bucal", "pieza", 24, "Cuidado Bucal", false);
        id = agregarSubcategoria(lista, id, "Higiene y Cuidado Personal", "Champú", "pieza", 36, "Champú Cabello", false);
        id = agregarSubcategoria(lista, id, "Higiene y Cuidado Personal", "Desodorantes", "pieza", 30, "Desodorante", false);
        id = agregarSubcategoria(lista, id, "Higiene y Cuidado Personal", "Afeitado", "pieza", 34, "Afeitado Personal", false);

        id = agregarSubcategoria(lista, id, "Alimentos Preparados/Enlatados", "Atún", "pieza", 26, "Atún Enlatado", false);
        id = agregarSubcategoria(lista, id, "Alimentos Preparados/Enlatados", "Vegetales en conserva", "pieza", 18, "Vegetal en Conserva", false);
        id = agregarSubcategoria(lista, id, "Alimentos Preparados/Enlatados", "Chiles/salsas", "pieza", 16, "Chile Salsa", false);
        id = agregarSubcategoria(lista, id, "Alimentos Preparados/Enlatados", "Sopas instantáneas", "pieza", 12, "Sopa Instantánea", false);
        agregarSubcategoria(lista, id, "Alimentos Preparados/Enlatados", "Leguminosas enlatadas", "pieza", 20, "Leguminosa Enlatada", false);

        inventario.ordenarPorId();
        PersistenciaDatos persistencia = new PersistenciaDatos();
        persistencia.guardarJSON(lista, "inventario");
        System.out.println(">>> Carga inicial de inventario completada con exito.");
    }

    private static int agregarSubcategoria(ArrayList<Producto> lista, int idInicio, String categoria, String subcategoria,
            String unidad, double precioBase, String nombreBase, boolean usarSalchichoneria) {
        int id = idInicio;
        for (int i = 1; i <= 6; i++) {
            String sku = String.valueOf(SKU_BASE + id);
            double precioCompra = precioBase + (i * 1.9);
            Producto p = crearProductoPorCategoria(id, sku, nombreBase + " " + i, precioCompra, categoria, unidad, usarSalchichoneria);
            p.setSubcategoria(subcategoria);
            p.setCantidad(STOCK_INICIAL);
            p.setPrecioCompra(precioCompra);
            p.setPorcentajeGanancia(PORCENTAJE_GANANCIA_INICIAL);
            p.setRutaImagen(rutaImagenPorSku(sku));
            lista.add(p);
            id++;
        }
        return id;
    }

    private static Producto crearProductoPorCategoria(int id, String sku, String nombre, double precioCompra,
            String categoria, String unidad, boolean usarSalchichoneria) {
        double g = PORCENTAJE_GANANCIA_INICIAL;
        String ruta = rutaImagenPorSku(sku);
        return switch (categoria) {
            case "Despensa Básica" -> new Abarrote(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            case "Lácteos y Huevo" -> new LacteoHuevo(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            case "Bebidas y Líquidos" -> new Bebida(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            case "Botanas y Dulces" -> new SnackDulceria(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            case "Frutas y Verduras" -> new FrutaVerdura(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            case "Carnes y Salchichonería" -> usarSalchichoneria
                    ? new Salchichoneria(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL)
                    : new CarnePescado(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            case "Cuidado del Hogar" -> new Limpieza(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            case "Higiene y Cuidado Personal" -> new CuidadoPersonal(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            case "Alimentos Preparados/Enlatados" -> new Panaderia(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
            default -> new Abarrote(id, sku, nombre, precioCompra, g, ruta, unidad, STOCK_INICIAL);
        };
    }
}
