package Controlador;

import Vista.*;
import Modelo.GestionProducto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;

public class CMenuPrincipal implements ActionListener {
    private VistaPrincipal vista;
    private GestionProducto modeloGlobal; // Para compartir los datos entre ventanas

    // Constructor corregido para aceptar el modelo del Main
    public CMenuPrincipal(VistaPrincipal vista, GestionProducto modelo) {
        this.vista = vista;
        this.modeloGlobal = modelo; // Guardamos el modelo que viene del Main
        
        // Configurar escuchadores
        this.vista.itemInventario.addActionListener(this);
        this.vista.itemPuntoVenta.addActionListener(this);
        this.vista.itemProductos.addActionListener(this);
        this.vista.itemProveedores.addActionListener(this);
        this.vista.itemUnidades.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.itemProductos) {
            abrirVentanaProductos();
        } else if (e.getSource() == vista.itemInventario) {
            abrirVentanaInventario();
        } else if (e.getSource() == vista.itemPuntoVenta) {
            abrirVentanaPuntoVenta();
        } else if (e.getSource() == vista.itemProveedores) {
            abrirVentanaProveedores();
        } else if (e.getSource() == vista.itemUnidades) {
            abrirVentanaUnidades();
        }
    }

    private void abrirVentanaProductos() {
        VProductos vProd = new VProductos();
        // Usamos el modelo global en lugar de crear uno nuevo
        CProducto cProd = new CProducto(vProd, modeloGlobal); 
        vProd.setController(cProd);
        abrirVentana(vProd);
    }

    private void abrirVentanaInventario() {
        VInventario vInv = new VInventario();
        // Al usar el modelo global, el inventario mostrará lo que cargamos en el Main
        CInventario cInv = new CInventario(vInv, modeloGlobal); 
        // Asegúrate de que VInventario tenga el método setController si lo necesitas
        abrirVentana(vInv);
    }

    private void abrirVentanaPuntoVenta() {
        VPuntoVenta vPV = new VPuntoVenta();
        // El punto de venta ahora tendrá acceso a los mismos productos
        CPuntoVenta cPV = new CPuntoVenta(vPV, modeloGlobal);
        vPV.setController(cPV); // Usamos el setController que arreglamos antes
        abrirVentana(vPV);
    }

    private void abrirVentanaProveedores() {
        VProveedores vistaProv = new VProveedores();
        new CProveedores(vistaProv);
        abrirVentana(vistaProv);
    }

    private void abrirVentanaUnidades() {
        VUnidadesMedida vistaUni = new VUnidadesMedida();
        new CUnidadesMedida(vistaUni);
        abrirVentana(vistaUni);
    }

    private void abrirVentana(JInternalFrame frame) {
        vista.desktop.add(frame);
        frame.setVisible(true);
    }
}