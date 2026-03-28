package Controlador;

import Modelo.*;
import Persistencia.PersistenciaDatos;
import Vista.VProductos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CProducto implements ActionListener {
    private VProductos vista;
    private GestionProducto modelo;
    private PersistenciaDatos persistencia;
    private static final Map<String, String[]> SUBCATEGORIAS = crearMapaSubcategorias();

    public CProducto(VProductos vista, GestionProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.persistencia = new PersistenciaDatos();

        ArrayList<Producto> datosCargados = persistencia.importarJSON();
        if (datosCargados != null) {
            this.modelo.getLista().clear();
            this.modelo.getLista().addAll(datosCargados);
            this.modelo.ordenarPorId();
        }

        actualizarTabla();
        cargarSubcategorias();
        vista.getCbCategoria().addActionListener(e -> cargarSubcategorias());
        vista.getCbSubcategoria().addActionListener(e -> actualizarTabla());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("INSERTAR")) {
            ejecutarInsertar();
        }
    }

    private void ejecutarInsertar() {
        try {
            int id = Integer.parseInt(vista.getTxtId().getText().trim());
            String sku = vista.getTxtSku().getText().trim();
            String nombre = vista.getTxtNombre().getText().trim();
            double precioCompra = Double.parseDouble(vista.getTxtPrecioCompra().getText().trim());
            double ganancia = Double.parseDouble(vista.getTxtGanancia().getText().trim());
            int cantidad = Integer.parseInt(vista.getTxtCantidad().getText().trim());
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(vista, "La cantidad no puede ser negativa.");
                return;
            }
            String cat = vista.getSelectedCategoria();
            String subcat = vista.getSelectedSubcategoria();
            String unidad = vista.getSelectedUnidad();
            String rutaImg = vista.getRutaImagen();
            if (rutaImg == null || rutaImg.isBlank()) {
                rutaImg = "src/img/" + sku + ".jpg";
            }

            Producto p = crearInstanciaEspecifica(id, sku, nombre, precioCompra, ganancia, cat, rutaImg, unidad, cantidad);
            p.setSku(sku);
            p.setCategoria(cat);
            p.setSubcategoria(subcat);
            p.setPrecioCompra(precioCompra);
            p.setPorcentajeGanancia(ganancia);
            p.setUnidadMedida(unidad);
            p.setRutaImagen(rutaImg);

            if (modelo.insertar(p)) {
                modelo.ordenarPorId();
                persistencia.guardarJSON(modelo.getLista(), "inventario");
                actualizarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(vista, "Producto registrado y guardado en JSON");
            } else {
                JOptionPane.showMessageDialog(vista, "Error: El ID ya existe");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Revisa ID, precio y cantidad (números válidos).");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error en los datos: " + ex.getMessage());
        }
    }

    private Producto crearInstanciaEspecifica(int id, String sku, String n, double pCompra, double ganancia, String c,
            String img, String unidad, int cantidad) {
        return switch (c) {
            case "Bebidas y Líquidos" -> new Bebida(id, n, pCompra, img, cantidad);
            case "Despensa Básica" -> new Abarrote(id, n, pCompra, img, cantidad);
            case "Carnes y Salchichonería" -> new CarnePescado(id, n, pCompra, img, cantidad);
            case "Frutas y Verduras" -> new FrutaVerdura(id, n, pCompra, img, cantidad);
            case "Lácteos y Huevo" -> new LacteoHuevo(id, n, pCompra, img, cantidad);
            case "Cuidado del Hogar" -> new Limpieza(id, n, pCompra, img, cantidad);
            case "Higiene y Cuidado Personal" -> new CuidadoPersonal(id, n, pCompra, img, cantidad);
            case "Alimentos Preparados/Enlatados" -> new Panaderia(id, n, pCompra, img, cantidad);
            case "Botanas y Dulces" -> new SnackDulceria(id, n, pCompra, img, cantidad);
            case "Mascotas" -> new Mascota(id, n, pCompra, img, cantidad);
            default -> new Abarrote(id, n, pCompra, img, cantidad);
        };
    }

    private void actualizarTabla() {
        DefaultTableModel dtm = (DefaultTableModel) vista.getTable().getModel();
        dtm.setRowCount(0);
        for (Producto p : productosVisibles()) {
            dtm.addRow(new Object[]{
                p.getId(),
                p.getSku(),
                p.getNombre(),
                p.getCategoria(),
                p.getSubcategoria(),
                p.getUnidadMedida(),
                p.getCantidad(),
                p.getPrecioCompra(),
                p.getPorcentajeGanancia(),
                p.getPrecioVenta()
            });
        }
    }

    private ArrayList<Producto> productosVisibles() {
        ArrayList<Producto> r = new ArrayList<>();
        String cat = vista.getSelectedCategoria();
        String sub = vista.getSelectedSubcategoria();
        boolean filtrarSub = sub != null && !sub.equals("--- Seleccionar ---");
        for (Producto p : modelo.getLista()) {
            if (cat != null && !p.getCategoria().equalsIgnoreCase(cat)) {
                continue;
            }
            if (filtrarSub && !p.getSubcategoria().equalsIgnoreCase(sub)) {
                continue;
            }
            r.add(p);
        }
        return r;
    }

    private void limpiarCampos() {
        vista.getTxtId().setText("");
        vista.getTxtSku().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtPrecioCompra().setText("");
        vista.getTxtGanancia().setText("30");
        vista.getTxtCantidad().setText("10");
        vista.reiniciarImagenPorDefecto();
    }

    private void cargarSubcategorias() {
        String cat = vista.getSelectedCategoria();
        var cb = vista.getCbSubcategoria();
        cb.removeAllItems();
        cb.addItem("--- Seleccionar ---");
        for (String s : SUBCATEGORIAS.getOrDefault(cat, new String[0])) {
            cb.addItem(s);
        }
        actualizarTabla();
    }

    private static Map<String, String[]> crearMapaSubcategorias() {
        Map<String, String[]> map = new LinkedHashMap<>();
        map.put("Despensa Básica", new String[]{"Arroz/frijol", "Aceites", "Pastas", "Harinas", "Azúcar"});
        map.put("Lácteos y Huevo", new String[]{"Leche", "Huevos", "Quesos", "Yogur", "Mantequillas"});
        map.put("Bebidas y Líquidos", new String[]{"Agua", "Refrescos", "Jugos", "Bebidas energéticas", "Café/té"});
        map.put("Botanas y Dulces", new String[]{"Papas", "Galletas", "Dulces", "Frutos secos", "Chocolates y caramelos"});
        map.put("Frutas y Verduras", new String[]{"Frutas", "Verduras", "Hierbas", "Corte/conveniencia", "Tubérculos y raíces"});
        map.put("Carnes y Salchichonería", new String[]{"Pollo", "Res", "Cerdo", "Jamones", "Pescados"});
        map.put("Cuidado del Hogar", new String[]{"Detergentes", "Limpiadores", "Papel higiénico", "Desechables", "Insecticidas"});
        map.put("Higiene y Cuidado Personal", new String[]{"Jabones", "Cuidado bucal", "Champú", "Desodorantes", "Afeitado"});
        map.put("Alimentos Preparados/Enlatados", new String[]{"Atún", "Vegetales en conserva", "Chiles/salsas", "Sopas instantáneas", "Leguminosas enlatadas"});
        return map;
    }
}
