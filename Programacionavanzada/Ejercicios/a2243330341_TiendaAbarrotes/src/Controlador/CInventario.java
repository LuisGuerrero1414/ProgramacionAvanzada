package Controlador;

import Modelo.*;
import Persistencia.PersistenciaDatos;
import Vista.VInventario;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class CInventario implements ActionListener {
    private VInventario vista;
    private GestionProducto modelo;
    private PersistenciaDatos persistencia;

    public CInventario(VInventario vista, GestionProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.persistencia = new PersistenciaDatos();

        // 1. Intentar cargar datos desde el JSON al iniciar
        ArrayList<Producto> datosCargados = persistencia.importarJSON();
        if (datosCargados != null && !datosCargados.isEmpty()) {
            this.modelo.getLista().clear();
            this.modelo.getLista().addAll(datosCargados);
        }

        // 2. Llenar la tabla visualmente por primera vez
        actualizarTabla();

        // 3. Configurar eventos de botones
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnExcelGral.addActionListener(this);
        this.vista.btnExcelCat.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);

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
                JOptionPane.showMessageDialog(vista, "Reporte Excel generado en la carpeta raíz.");
            }
            case "EXCEL_CATEGORIA" -> ejecutarReporteCategoria();
            case "LIMPIAR" -> {
                vista.txtIdBusqueda.setText("");
                vista.cbTipo.setSelectedIndex(0);
                actualizarTabla();
            }
        }
    }

    private void ejecutarEliminar() {
        int fila = vista.tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un producto de la tabla primero.");
            return;
        }
        int id = (int) vista.tabla.getValueAt(fila, 0);
        if (modelo.eliminar(id)) {
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
        for (Producto p : modelo.getLista()) {
            vista.modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCategoria(),
                "10", // Stock por defecto
                p.getPrecio(),
                p.getTipoAlmacenamiento() // Polimorfismo en acción
            });
        }
    }
}