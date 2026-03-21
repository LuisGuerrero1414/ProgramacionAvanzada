package Controlador;

import Modelo.*;
import Persistencia.PersistenciaDatos;
import Vista.VPuntoVenta;
import java.awt.event.*;
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
        }
        
        llenarCombo();
        this.vista.setController(this);
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
            double subtotal = p.getPrecio() * cant;

            vista.modeloCarrito.addRow(new Object[]{
                p.getId(), p.getNombre(), cant, p.getPrecio(), subtotal
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

        persistencia.guardarJSON(datosVenta, folio);
        
        JOptionPane.showMessageDialog(vista, "Venta Exitosa. Folio: " + folio);
        vista.modeloCarrito.setRowCount(0);
        actualizarTotales();
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
}