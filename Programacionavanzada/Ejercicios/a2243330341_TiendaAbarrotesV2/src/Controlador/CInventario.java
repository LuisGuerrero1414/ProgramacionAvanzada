package Controlador;

import Modelo.*;
import Persistencia.PersistenciaDatos;
import Vista.VInventario;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CInventario implements ActionListener {
    private VInventario vista;
    private GestionProducto modelo;
    private PersistenciaDatos persistencia;
    private GestionMovimientoInventario movimientos;
    private static final Map<String, String[]> SUBCATEGORIAS = crearMapaSubcategorias();

    public CInventario(VInventario vista, GestionProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.persistencia = new PersistenciaDatos();
        this.movimientos = new GestionMovimientoInventario();

        // 1. Intentar cargar datos desde el JSON al iniciar
        ArrayList<Producto> datosCargados = persistencia.importarJSON();
        if (datosCargados != null && !datosCargados.isEmpty()) {
            this.modelo.getLista().clear();
            this.modelo.getLista().addAll(datosCargados);
            this.modelo.ordenarPorId();
        }

        // 2. Llenar la tabla visualmente por primera vez
        actualizarTabla();

        // 3. Configurar eventos de botones
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnExcelGral.addActionListener(this);
        this.vista.btnExcelCat.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnEntrada.addActionListener(this);
        this.vista.btnSalida.addActionListener(this);
        this.vista.btnAjuste.addActionListener(this);

        // Filtros: ID (al escribir) y categoría (al cambiar el combo)
        DocumentListener refrescarTabla = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { actualizarTabla(); }
            @Override
            public void removeUpdate(DocumentEvent e) { actualizarTabla(); }
            @Override
            public void changedUpdate(DocumentEvent e) { actualizarTabla(); }
        };
        this.vista.txtIdBusqueda.getDocument().addDocumentListener(refrescarTabla);
        this.vista.cbTipo.addActionListener(e -> actualizarTabla());
        this.vista.cbTipo.addActionListener(e -> cargarSubcategorias());
        this.vista.cbSubtipo.addActionListener(e -> actualizarTabla());
        cargarSubcategorias();

        // 4. Evento de Clic en la Tabla para la Imagen
        this.vista.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.tabla.getSelectedRow();
                if (fila != -1) {
                    // Obtener el ID de la fila seleccionada (columna 0)
                    int id = (int) vista.tabla.getValueAt(fila, 0);
                    Producto p = modelo.buscar(id);
                    if (p != null) {
                        vista.updateImage(p.getRutaImagen());
                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        switch (comando) {
            case "ELIMINAR" -> ejecutarEliminar();
            case "EXCEL_GENERAL" -> {
                persistencia.exportarExcel(modelo.getLista(), "Inventario_Completo");
                JOptionPane.showMessageDialog(vista, "Reporte Excel generado en carpeta reportes/.");
            }
            case "EXCEL_CATEGORIA" -> ejecutarReporteCategoria();
            case "LIMPIAR" -> {
                vista.txtIdBusqueda.setText("");
                vista.cbTipo.setSelectedIndex(0);
                vista.cbSubtipo.setSelectedIndex(0);
                actualizarTabla();
            }
            case "MOV_ENTRADA" -> aplicarMovimiento("ENTRADA");
            case "MOV_SALIDA" -> aplicarMovimiento("SALIDA");
            case "MOV_AJUSTE" -> aplicarMovimiento("AJUSTE");
        }
    }

    private void aplicarMovimiento(String tipo) {
        int fila = vista.tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto.");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(vista.txtCantidadMovimiento.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Cantidad de movimiento invalida.");
            return;
        }
        int id = (int) vista.tabla.getValueAt(fila, 0);
        Producto p = modelo.buscar(id);
        if (p == null) return;

        int stockActual = p.getCantidad();
        int nuevoStock = stockActual;
        if ("ENTRADA".equals(tipo)) {
            nuevoStock = stockActual + cantidad;
        } else if ("SALIDA".equals(tipo)) {
            nuevoStock = Math.max(0, stockActual - cantidad);
        } else if ("AJUSTE".equals(tipo)) {
            nuevoStock = Math.max(0, cantidad);
        }

        p.setCantidad(nuevoStock);
        movimientos.registrarMovimiento(id, tipo, cantidad);
        persistencia.guardarJSON(movimientos.getMovimientos(), "movimientos_inventario");
        persistencia.guardarJSON(modelo.getLista(), "inventario");
        actualizarTabla();
    }

    private void ejecutarEliminar() {
        int fila = vista.tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto de la tabla primero.");
            return;
        }
        int id = (int) vista.tabla.getValueAt(fila, 0);
        if (modelo.eliminar(id)) {
            modelo.ordenarPorId();
            persistencia.guardarJSON(modelo.getLista(), "inventario");
            actualizarTabla();
            vista.lblImagen.setIcon(null);
            vista.lblImagen.setText("Producto eliminado");
        }
    }

    private void ejecutarReporteCategoria() {
        String cat = (String) vista.cbTipo.getSelectedItem();
        if (cat.equals("--- Seleccionar ---")) {
            JOptionPane.showMessageDialog(vista, "Por favor, selecciona una categoría.");
            return;
        }
        ArrayList<Producto> filtrados = new ArrayList<>();
        for (Producto p : modelo.getLista()) {
            if (p.getCategoria().equalsIgnoreCase(cat)) filtrados.add(p);
        }
        persistencia.exportarExcel(filtrados, "Reporte_" + cat);
        JOptionPane.showMessageDialog(vista, "Excel de " + cat + " exportado.");
    }

    private void actualizarTabla() {
        vista.modelo.setRowCount(0);
        for (Producto p : productosVisibles()) {
            vista.modelo.addRow(new Object[]{
                p.getId(),
                p.getSku(),
                p.getNombre(),
                p.getCategoria(),
                p.getSubcategoria(),
                p.getUnidadMedida(),
                String.valueOf(p.getCantidad()),
                p.getPrecioCompra(),
                p.getPorcentajeGanancia(),
                p.getPrecioVenta(),
                p.getTipoAlmacenamiento() // Polimorfismo en acción
            });
        }
    }

    /** Filtros AND: categoría (si no es la opción por defecto) e ID (si el campo tiene un entero válido). */
    private ArrayList<Producto> productosVisibles() {
        ArrayList<Producto> resultado = new ArrayList<>();
        String idText = vista.txtIdBusqueda.getText().trim();
        String cat = (String) vista.cbTipo.getSelectedItem();
        String subcat = (String) vista.cbSubtipo.getSelectedItem();
        boolean filtrarCat = cat != null && !cat.equals("--- Seleccionar ---");
        boolean filtrarSubcat = subcat != null && !subcat.equals("--- Seleccionar ---");
        Integer idFiltro = null;
        if (!idText.isEmpty()) {
            try {
                idFiltro = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                return resultado;
            }
        }
        for (Producto p : modelo.getLista()) {
            if (filtrarCat && !p.getCategoria().equalsIgnoreCase(cat)) {
                continue;
            }
            if (filtrarSubcat && !p.getSubcategoria().equalsIgnoreCase(subcat)) {
                continue;
            }
            if (idFiltro != null && p.getId() != idFiltro) {
                continue;
            }
            resultado.add(p);
        }
        return resultado;
    }

    private void cargarSubcategorias() {
        String cat = (String) vista.cbTipo.getSelectedItem();
        vista.cbSubtipo.removeAllItems();
        vista.cbSubtipo.addItem("--- Seleccionar ---");
        for (String s : SUBCATEGORIAS.getOrDefault(cat, new String[0])) {
            vista.cbSubtipo.addItem(s);
        }
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