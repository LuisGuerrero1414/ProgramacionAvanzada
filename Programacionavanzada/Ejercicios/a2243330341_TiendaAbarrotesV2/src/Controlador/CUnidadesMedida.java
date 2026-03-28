package Controlador;

import Modelo.GestionUnidadMedida;
import Modelo.UnidadMedida;
import Persistencia.PersistenciaDatos;
import Vista.VUnidadesMedida;
import com.google.gson.reflect.TypeToken;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class CUnidadesMedida implements ActionListener {
    private final VUnidadesMedida vista;
    private final GestionUnidadMedida modelo;
    private final PersistenciaDatos persistencia;

    public CUnidadesMedida(VUnidadesMedida vista) {
        this.vista = vista;
        this.modelo = new GestionUnidadMedida();
        this.persistencia = new PersistenciaDatos();

        Type tipo = new TypeToken<ArrayList<UnidadMedida>>() {}.getType();
        modelo.getUnidades().addAll(persistencia.cargarListaDesdeJSON("unidades", tipo));
        if (modelo.getUnidades().isEmpty()) {
            modelo.agregar(new UnidadMedida("pieza", "Pieza"));
            modelo.agregar(new UnidadMedida("kg", "Kilogramo"));
            modelo.agregar(new UnidadMedida("lt", "Litro"));
            persistencia.guardarListaEnJSON(modelo.getUnidades(), "unidades");
        }

        vista.btnGuardar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        refrescar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("GUARDAR_UNIDAD".equals(cmd)) {
            UnidadMedida unidad = new UnidadMedida(vista.txtClave.getText().trim(), vista.txtDescripcion.getText().trim());
            if (unidad.getClave().isBlank()) {
                JOptionPane.showMessageDialog(vista, "Indica una clave.");
                return;
            }
            if (modelo.agregar(unidad)) {
                persistencia.guardarListaEnJSON(modelo.getUnidades(), "unidades");
                refrescar();
            } else {
                JOptionPane.showMessageDialog(vista, "Ya existe una unidad con esa clave.");
            }
            return;
        }
        if ("ELIMINAR_UNIDAD".equals(cmd)) {
            int fila = vista.tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(vista, "Selecciona una fila de la tabla para eliminar.");
                return;
            }
            String clave = vista.modelo.getValueAt(fila, 0) != null
                    ? vista.modelo.getValueAt(fila, 0).toString().trim() : "";
            if (clave.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "La fila seleccionada no tiene clave válida.");
                return;
            }
            if (modelo.eliminarPorClave(clave)) {
                persistencia.guardarListaEnJSON(modelo.getUnidades(), "unidades");
                refrescar();
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo eliminar la unidad.");
            }
        }
    }

    private void refrescar() {
        vista.modelo.setRowCount(0);
        for (UnidadMedida u : modelo.getUnidades()) {
            vista.modelo.addRow(new Object[]{u.getClave(), u.getDescripcion()});
        }
    }
}
