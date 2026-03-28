package Controlador;
import Modelo.GestionProducto;
import Modelo.Producto;
import Persistencia.ArchivoCSV;
import Vista.VInventario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class CInventario implements ActionListener {
	
    private VInventario vista;
    private GestionProducto modelo;
    private ArchivoCSV persistencia;

    public CInventario(VInventario vista, GestionProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.persistencia = new ArchivoCSV();
        
        this.modelo.setLista(persistencia.importarCSV());
        actualizarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "ELIMINAR":
                ejecutarEliminar();
                break;
            case "LIMPIAR":
                limpiarFiltros();
                break;
            case "BUSCAR":
                JOptionPane.showMessageDialog(vista, "Función de búsqueda en desarrollo");
                break;
            case "CREAR":
                JOptionPane.showMessageDialog(vista, "Use el Catálogo de Productos para agregar nuevos items");
                break;
            case "MODIFICAR":
                JOptionPane.showMessageDialog(vista, "Seleccione un producto en la tabla para modificar");
                break;
        }
    }

    private void ejecutarEliminar() {
        int fila = vista.tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una fila primero");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este producto?");
        if (respuesta == JOptionPane.YES_OPTION) {
            int id = (int) vista.tabla.getValueAt(fila, 0);
            if (modelo.eliminar(id)) {
                persistencia.exportarCSV(modelo.getLista()); 
                actualizarTabla();
                JOptionPane.showMessageDialog(vista, "Producto eliminado");
            }
        }
    }

    private void actualizarTabla() {
        modelo.setLista(persistencia.importarCSV()); 
        vista.modelo.setRowCount(0);
        for (Producto p : modelo.getLista()) {
            vista.modelo.addRow(new Object[]{
                p.getId(), 
                p.getNombre(), 
                p.getCategoria(), 
                "10",
                p.getPrecio(), 
                "Disponible"
            });
        }
    }

    private void limpiarFiltros() {
        vista.txtIdBusqueda.setText("");
        vista.txtNombreBusqueda.setText("");
        vista.cbTipo.setSelectedIndex(0);
        vista.rbTodos.setSelected(true);
        actualizarTabla();
    }
}