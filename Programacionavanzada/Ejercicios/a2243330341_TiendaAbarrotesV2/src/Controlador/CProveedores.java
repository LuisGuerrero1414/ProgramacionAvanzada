package Controlador;

import Modelo.GestionProveedor;
import Modelo.GestionRelacionProductoProveedor;
import Modelo.ProductoProveedorRelacion;
import Modelo.Proveedor;
import Persistencia.PersistenciaDatos;
import Vista.VProveedores;
import com.google.gson.reflect.TypeToken;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CProveedores implements ActionListener {
    private final VProveedores vista;
    private final GestionProveedor proveedores;
    private final GestionRelacionProductoProveedor relaciones;
    private final PersistenciaDatos persistencia;

    public CProveedores(VProveedores vista) {
        this.vista = vista;
        this.proveedores = new GestionProveedor();
        this.relaciones = new GestionRelacionProductoProveedor();
        this.persistencia = new PersistenciaDatos();

        Type tipoProv = new TypeToken<ArrayList<Proveedor>>() {}.getType();
        Type tipoRel = new TypeToken<ArrayList<ProductoProveedorRelacion>>() {}.getType();
        proveedores.getLista().addAll(persistencia.cargarListaDesdeJSON("proveedores", tipoProv));
        relaciones.getRelaciones().addAll(persistencia.cargarListaDesdeJSON("producto_proveedor", tipoRel));

        vista.btnGuardar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnAsociar.addActionListener(this);
        vista.btnDesasociar.addActionListener(this);
        refrescarTablas();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "GUARDAR_PROV" -> guardarProveedor();
            case "ELIMINAR_PROV" -> eliminarProveedor();
            case "ASOCIAR" -> asociar();
            case "DESASOCIAR" -> desasociar();
        }
    }

    private void guardarProveedor() {
        int id = Integer.parseInt(vista.txtId.getText().trim());
        Proveedor p = new Proveedor(id, vista.txtNombre.getText().trim(), vista.txtTelefono.getText().trim(), vista.txtEmail.getText().trim());
        if (proveedores.insertar(p)) {
            persistencia.guardarListaEnJSON(proveedores.getLista(), "proveedores");
            refrescarTablas();
        }
    }

    private void eliminarProveedor() {
        int id = Integer.parseInt(vista.txtId.getText().trim());
        if (proveedores.eliminar(id)) {
            persistencia.guardarListaEnJSON(proveedores.getLista(), "proveedores");
            refrescarTablas();
        }
    }

    private void asociar() {
        int idProducto = Integer.parseInt(vista.txtIdProducto.getText().trim());
        int idProveedor = Integer.parseInt(vista.txtIdProveedor.getText().trim());
        if (relaciones.agregarRelacion(idProducto, idProveedor)) {
            persistencia.guardarListaEnJSON(relaciones.getRelaciones(), "producto_proveedor");
            refrescarTablas();
        }
    }

    private void desasociar() {
        int idProducto = Integer.parseInt(vista.txtIdProducto.getText().trim());
        int idProveedor = Integer.parseInt(vista.txtIdProveedor.getText().trim());
        if (relaciones.eliminarRelacion(idProducto, idProveedor)) {
            persistencia.guardarListaEnJSON(relaciones.getRelaciones(), "producto_proveedor");
            refrescarTablas();
        }
    }

    private void refrescarTablas() {
        vista.modeloProveedores.setRowCount(0);
        for (Proveedor p : proveedores.getLista()) {
            vista.modeloProveedores.addRow(new Object[]{p.getId(), p.getNombre(), p.getTelefono(), p.getEmail()});
        }

        vista.modeloRelaciones.setRowCount(0);
        for (ProductoProveedorRelacion r : relaciones.getRelaciones()) {
            vista.modeloRelaciones.addRow(new Object[]{r.getIdProducto(), r.getIdProveedor()});
        }
    }
}
