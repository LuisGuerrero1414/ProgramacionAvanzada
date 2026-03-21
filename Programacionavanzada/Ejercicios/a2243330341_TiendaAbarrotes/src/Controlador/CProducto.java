package Controlador;

import Modelo.*;
import Persistencia.PersistenciaDatos;
import Vista.VProductos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CProducto implements ActionListener {
    private VProductos vista;
    private GestionProducto modelo;
    private PersistenciaDatos persistencia;

    public CProducto(VProductos vista, GestionProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.persistencia = new PersistenciaDatos();
        
        // CARGA INICIAL: Evita que se borren los productos al reiniciar
        ArrayList<Producto> datosCargados = persistencia.importarJSON();
        if (datosCargados != null) {
            this.modelo.getLista().clear();
            this.modelo.getLista().addAll(datosCargados);
        }
        
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
            int id = Integer.parseInt(vista.getTxtId().getText());
            String nombre = vista.getTxtNombre().getText();
            double precio = Double.parseDouble(vista.getTxtPrecio().getText());
            String cat = vista.getSelectedCategoria(); 
            
            // Aquí puedes usar un JFileChooser en la vista para obtener la ruta real
            String rutaImg = "src/imagenes/producto_default.png"; 

            // Polimorfismo: creamos la subclase específica
            Producto p = crearInstanciaEspecifica(id, nombre, precio, cat, rutaImg);

            if (modelo.insertar(p)) {
                // GUARDADO: Sincroniza la lista con el archivo JSON
                persistencia.guardarJSON(modelo.getLista(), "inventario");
                actualizarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(vista, "Producto registrado y guardado en JSON");
            } else {
                JOptionPane.showMessageDialog(vista, "Error: El ID ya existe");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error en los datos: " + ex.getMessage());
        }
    }

    private Producto crearInstanciaEspecifica(int id, String n, double p, String c, String img) {
        // Switch para decidir qué subclase usar según la categoría de la vista
        return switch (c) {
            case "Bebidas" -> new Bebida(id, n, p, img);
            case "Abarrotes" -> new Abarrote(id, n, p, img);
            case "Carnes y Pescados" -> new CarnePescado(id, n, p, img);
            case "Frutas y Verduras" -> new FrutaVerdura(id, n, p, img);
            case "Lácteos y Huevo" -> new LacteoHuevo(id, n, p, img);
            case "Mascotas" -> new Mascota(id, n, p, img);
            case "Limpieza del Hogar" -> new Limpieza(id, n, p, img);
            case "Cuidado Personal" -> new CuidadoPersonal(id, n, p, img);
            case "Panadería y Tortillería" -> new Panaderia(id, n, p, img);
            case "Salchichonería" -> new Salchichoneria(id, n, p, img);
            case "Snacks y Dulcería" -> new SnackDulceria(id, n, p, img);
            default -> new Abarrote(id, n, p, img);
        };
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
    }
}