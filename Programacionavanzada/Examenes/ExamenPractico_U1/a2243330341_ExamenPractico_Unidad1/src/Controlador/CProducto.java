package Controlador;
import Modelo.*;
import Persistencia.ArchivoCSV;
import Vista.VProductos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class CProducto implements ActionListener {
	
    private VProductos vista;
    private GestionProducto modelo;
    private ArchivoCSV persistencia;

    public CProducto(VProductos vista, GestionProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.persistencia = new ArchivoCSV();
        
        this.modelo.setLista(persistencia.importarCSV());
        actualizarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("INSERTAR")) {
            ejecutarInsertar();
        }
    }

    private void ejecutarInsertar() {
        try {
            if (vista.getTxtId().getText().isEmpty()) throw new Exception("ID Requerido");
            
            int id = Integer.parseInt(vista.getTxtId().getText());
            String nombre = vista.getTxtNombre().getText();
            double precio = Double.parseDouble(vista.getTxtPrecio().getText());
            String cat = vista.getSelectedCategoria(); // Método a implementar en vista

            Producto p = new Producto(id, nombre, precio, cat);
            if (modelo.insertar(p)) {
                persistencia.exportarCSV(modelo.getLista());
                actualizarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(vista, "Guardado exitosamente");
            } else {
                JOptionPane.showMessageDialog(vista, "Error: ID Duplicado");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Error: Ingrese números válidos");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage());
        }
    }

    private void actualizarTabla() {
        DefaultTableModel dtm = (DefaultTableModel) vista.getTable().getModel();
        dtm.setRowCount(0);
        for (Producto p : modelo.getLista()) {
            dtm.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getCategoria()});
        }
    }

    private void limpiarCampos() {
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtPrecio().setText("");
        vista.getBtnGroup().clearSelection();
    }
}