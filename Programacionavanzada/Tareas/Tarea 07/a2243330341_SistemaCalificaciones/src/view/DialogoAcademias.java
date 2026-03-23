package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JButton;
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

/**
 * CRUD de academias (solo presentación y eventos).
 */
public class DialogoAcademias extends JDialog {

    private final CatalogoController catalogoController;
    private final JTable tabla = new JTable();
    private final JTextField campoNombre = new JTextField(24);

    public DialogoAcademias(JFrame owner, CatalogoController catalogoController) {
        super(owner, "Gestión de Academias", true);
        this.catalogoController = catalogoController;
        setLayout(new BorderLayout(8, 8));
        setSize(520, 360);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Nombre:"));
        form.add(campoNombre);
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

        recargarTabla();
    }

    private void recargarTabla() {
        try {
            var lista = catalogoController.listarAcademias();
            DefaultTableModel m = new DefaultTableModel(new Object[] { "Id", "Nombre" }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (Academia a : lista) {
                m.addRow(new Object[] { a.getId(), a.getNombre() });
            }
            tabla.setModel(m);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear() {
        try {
            catalogoController.crearAcademia(campoNombre.getText());
            campoNombre.setText("");
            recargarTabla();
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
        String nombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", "Editar academia",
                JOptionPane.QUESTION_MESSAGE);
        if (nombre == null) {
            return;
        }
        try {
            if (!catalogoController.actualizarAcademia(id, nombre)) {
                JOptionPane.showMessageDialog(this, "No se encontró el registro.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }
            recargarTabla();
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
        int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar esta academia?", "Confirmar",
                JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            catalogoController.eliminarAcademia(id);
            recargarTabla();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
