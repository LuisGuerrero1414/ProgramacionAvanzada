package Controlador;
import Modelo.*;
import Persistencia.ArchivoCSV;
import Vista.VPuntoVenta;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class CPuntoVenta implements ActionListener {
	
    private VPuntoVenta vista;
    private GestionProducto modelo;
    private ArrayList<Producto> catalogo;

    public CPuntoVenta(VPuntoVenta vista, GestionProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.catalogo = new ArchivoCSV().importarCSV(); 
        llenarCombo();
        this.vista.setController(this);
    }

    private void llenarCombo() {
        vista.cbProductos.addItem("--- Seleccionar ---");
        for (Producto p : catalogo) {
            vista.cbProductos.addItem(p.getId() + " - " + p.getNombre());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAnadir) añadirAlCarrito();
        if (e.getSource() == vista.btnLimpiar) limpiarTodo();
        if (e.getSource() == vista.btnTicket) exportarTicket();
    }

    private void añadirAlCarrito() {
        int index = vista.cbProductos.getSelectedIndex();
        if (index <= 0) return;

        try {
            Producto p = catalogo.get(index - 1);
            int cant = Integer.parseInt(vista.txtCantidad.getText());
            double sub = p.getPrecio() * cant;
            vista.modeloCarrito.addRow(new Object[]{p.getId(), p.getNombre(), cant, p.getPrecio(), sub});
            calcularTotales();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error en cantidad");
        }
    }

    private void calcularTotales() {
        double subtotal = 0;
        for (int i = 0; i < vista.modeloCarrito.getRowCount(); i++) {
            subtotal += (double) vista.modeloCarrito.getValueAt(i, 4);
        }
        double iva = subtotal * 0.16;
        vista.txtSubtotal.setText(String.format("%.2f", subtotal));
        vista.txtIva.setText(String.format("%.2f", iva));
        vista.txtTotal.setText(String.format("%.2f", subtotal + iva));
    }

    private void exportarTicket() {
        try (PrintWriter out = new PrintWriter(new FileWriter("ticket_venta.txt"))) {
            out.println("======= TICKET DE VENTA =======");
            out.println("UAT - Facultad de Ingeniería"); //
            out.println("-------------------------------");
            for (int i = 0; i < vista.modeloCarrito.getRowCount(); i++) {
                out.println(vista.modeloCarrito.getValueAt(i, 1) + " x" + 
                            vista.modeloCarrito.getValueAt(i, 2) + " ... $" + 
                            vista.modeloCarrito.getValueAt(i, 4));
            }
            out.println("-------------------------------");
            out.println("TOTAL A PAGAR: $" + vista.txtTotal.getText());
            out.println("===============================");
            JOptionPane.showMessageDialog(vista, "Ticket generado: ticket_venta.txt");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void limpiarTodo() {
        vista.modeloCarrito.setRowCount(0);
        vista.txtSubtotal.setText("");
        vista.txtIva.setText("");
        vista.txtTotal.setText("");
    }
}