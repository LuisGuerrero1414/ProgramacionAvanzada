package Controlador;

import Modelo.*;
import Persistencia.PersistenciaDatos;
import Vista.VPuntoVenta;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class CPuntoVenta implements ActionListener {
    private VPuntoVenta vista;
    private GestionProducto modelo;
    private PersistenciaDatos persistencia;

    public CPuntoVenta(VPuntoVenta vista, GestionProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.persistencia = new PersistenciaDatos();
        
        // CARGA INICIAL: El catálogo del combo se llena con lo que hay en el JSON
        ArrayList<Producto> inventarioReal = persistencia.importarJSON();
        if (inventarioReal != null) {
            this.modelo.getLista().clear();
            this.modelo.getLista().addAll(inventarioReal);
            this.modelo.ordenarPorId();
        }
        
        llenarCombo();
    }

    private void llenarCombo() {
        vista.cbProductos.removeAllItems();
        vista.cbProductos.addItem("--- Seleccionar ---");
        for (Producto p : modelo.getLista()) {
            vista.cbProductos.addItem(p.getId() + " - " + p.getNombre());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAnadir) añadirAlCarrito();
        if (e.getSource() == vista.btnTicket) generarTicketJSON();
        if (e.getSource() == vista.btnLimpiar) vista.modeloCarrito.setRowCount(0);
        if (e.getSource() == vista.cbProductos) actualizarImagenSeleccion();
    }

    private void añadirAlCarrito() {
        int index = vista.cbProductos.getSelectedIndex();
        if (index <= 0) return;

        try {
            // Extraer ID del String del combo (ej: "101 - Coca Cola")
            String item = (String) vista.cbProductos.getSelectedItem();
            int id = Integer.parseInt(item.split(" - ")[0]);
            Producto p = modelo.buscar(id);
            
            int cant = Integer.parseInt(vista.txtCantidad.getText());
            if (cant <= 0) {
                JOptionPane.showMessageDialog(vista, "Cantidad invalida.");
                return;
            }
            if (p == null || p.getCantidad() < cant) {
                JOptionPane.showMessageDialog(vista, "Stock insuficiente.");
                return;
            }
            double subtotal = p.getPrecioVenta() * cant;

            vista.modeloCarrito.addRow(new Object[]{
                p.getId(), p.getNombre(), cant, p.getPrecioVenta(), subtotal
            });
            
            actualizarTotales();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error en cantidad");
        }
    }

    private void generarTicketJSON() {
        if (vista.modeloCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "Carrito vacío");
            return;
        }

        // Requisito: Nombre del archivo como Folio (usamos timestamp)
        String folio = "TICKET_" + System.currentTimeMillis();
        
        // Convertir la tabla a una lista de objetos para el JSON
        ArrayList<Object> datosVenta = new ArrayList<>();
        for (int i = 0; i < vista.modeloCarrito.getRowCount(); i++) {
            datosVenta.add(vista.modeloCarrito.getDataVector().get(i));
        }

        for (int i = 0; i < vista.modeloCarrito.getRowCount(); i++) {
            int idProducto = (int) vista.modeloCarrito.getValueAt(i, 0);
            int cantidadVendida = (int) vista.modeloCarrito.getValueAt(i, 2);
            Producto p = modelo.buscar(idProducto);
            if (p != null) {
                p.setCantidad(Math.max(0, p.getCantidad() - cantidadVendida));
            }
        }
        persistencia.guardarJSON(datosVenta, folio);
        persistencia.guardarJSON(modelo.getLista(), "inventario");
        
        JOptionPane.showMessageDialog(vista, "Venta Exitosa. Folio: " + folio);
        vista.modeloCarrito.setRowCount(0);
        actualizarTotales();
        llenarCombo();
    }

    private void actualizarTotales() {
        double sub = 0;
        for (int i = 0; i < vista.modeloCarrito.getRowCount(); i++) {
            sub += (double) vista.modeloCarrito.getValueAt(i, 4);
        }
        double iva = sub * 0.16;
        vista.txtSubtotal.setText(String.format("%.2f", sub));
        vista.txtIva.setText(String.format("%.2f", iva));
        vista.txtTotal.setText(String.format("%.2f", sub + iva));
    }

    private void actualizarImagenSeleccion() {
        int index = vista.cbProductos.getSelectedIndex();
        if (index <= 0) {
            vista.updateImage(null);
            return;
        }
        String item = (String) vista.cbProductos.getSelectedItem();
        int id = Integer.parseInt(item.split(" - ")[0]);
        Producto p = modelo.buscar(id);
        if (p == null) return;

        String[] extensiones = {"jpg", "jpeg", "png", "webp"};
        for (String ext : extensiones) {
            String ruta = "src/img/" + p.getSku() + "." + ext;
            if (new File(ruta).exists()) {
                vista.updateImage(ruta);
                return;
            }
        }
        vista.updateImage(p.getRutaImagen());
    }
}