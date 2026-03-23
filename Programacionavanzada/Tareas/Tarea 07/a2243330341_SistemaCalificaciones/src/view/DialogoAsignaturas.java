package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.CatalogoController;
import model.Academia;
import model.Asignatura;

/**
 * CRUD de asignaturas (solo presentación y eventos).
 */
public class DialogoAsignaturas extends JDialog {

    private final CatalogoController catalogoController;
    private final JTable tabla = new JTable();
    private final JTextField campoNombre = new JTextField(28);
    private final JComboBox<AcademiaCombo> comboAcademia = new JComboBox<>();

    public DialogoAsignaturas(JFrame owner, CatalogoController catalogoController) {
        super(owner, "Gestión de Asignaturas", true);
        this.catalogoController = catalogoController;
        setLayout(new BorderLayout(8, 8));
        setSize(720, 400);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Nombre (debe coincidir con MATERIA del CSV o su código):"));
        form.add(campoNombre);
        form.add(new JLabel("Academia:"));
        form.add(comboAcademia);
        JButton btnAdd = new JButton("Crear");
        btnAdd.addActionListener(e -> crear());
        form.add(btnAdd);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEdit = new JButton("Actualizar seleccionada");
        btnEdit.addActionListener(e -> actualizar());
        JButton btnDel = new JButton("Eliminar seleccionada");
        btnDel.addActionListener(e -> eliminar());
        botones.add(btnEdit);
        botones.add(btnDel);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        recargarComboYTabla();
    }

    private void recargarComboYTabla() {
        try {
            comboAcademia.removeAllItems();
            List<Academia> ac = catalogoController.listarAcademias();
            for (Academia a : ac) {
                comboAcademia.addItem(new AcademiaCombo(a.getId(), a.getNombre()));
            }

            List<Asignatura> lista = catalogoController.listarAsignaturas();
            DefaultTableModel m = new DefaultTableModel(
                    new Object[] { "Id", "Nombre", "Id Academia" }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (Asignatura as : lista) {
                m.addRow(new Object[] { as.getId(), as.getNombre(), as.getIdAcademia() });
            }
            tabla.setModel(m);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear() {
        AcademiaCombo sel = (AcademiaCombo) comboAcademia.getSelectedItem();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Registre al menos una academia.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            catalogoController.crearAsignatura(campoNombre.getText(), sel.id);
            campoNombre.setText("");
            recargarComboYTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long id = ((Number) tabla.getValueAt(row, 0)).longValue();
        JTextField nombre = new JTextField(String.valueOf(tabla.getValueAt(row, 1)));
        JComboBox<AcademiaCombo> combo = new JComboBox<>();
        try {
            for (Academia a : catalogoController.listarAcademias()) {
                combo.addItem(new AcademiaCombo(a.getId(), a.getNombre()));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long idAcActual = ((Number) tabla.getValueAt(row, 2)).longValue();
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).id == idAcActual) {
                combo.setSelectedIndex(i);
                break;
            }
        }
        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombre);
        panel.add(new JLabel("Academia:"));
        panel.add(combo);
        int r = JOptionPane.showConfirmDialog(this, panel, "Editar asignatura",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }
        AcademiaCombo sel = (AcademiaCombo) combo.getSelectedItem();
        if (sel == null) {
            return;
        }
        try {
            catalogoController.actualizarAsignatura(id, nombre.getText(), sel.id);
            recargarComboYTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long id = ((Number) tabla.getValueAt(row, 0)).longValue();
        int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar esta asignatura?", "Confirmar",
                JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            catalogoController.eliminarAsignatura(id);
            recargarComboYTabla();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static final class AcademiaCombo {
        final long id;
        final String nombre;

        AcademiaCombo(long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return id + " — " + nombre;
        }
    }
}
