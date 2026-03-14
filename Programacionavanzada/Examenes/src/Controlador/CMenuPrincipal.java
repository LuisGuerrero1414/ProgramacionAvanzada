package Controlador;
import Vista.*;
import Modelo.GestionProducto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
public class CMenuPrincipal implements ActionListener {
	
    private VistaPrincipal vista;

    public CMenuPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        this.vista.itemInventario.addActionListener(this);
        this.vista.itemPuntoVenta.addActionListener(this);
        this.vista.itemProductos.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.itemProductos) {
            abrirVentanaProductos();
        } else if (e.getSource() == vista.itemInventario) {
            abrirVentanaInventario();
        } else if (e.getSource() == vista.itemPuntoVenta) {
            abrirVentanaPuntoVenta();
        }
    }

    private void abrirVentanaProductos() {
        VProductos vProd = new VProductos();
        GestionProducto mProd = new GestionProducto();
        CProducto cProd = new CProducto(vProd, mProd);
        vProd.setController(cProd);
        abrirVentana(vProd);
    }

    private void abrirVentana(JInternalFrame frame) {
        vista.desktop.add(frame);
        frame.setVisible(true);
    }
    
    private void abrirVentanaInventario() {
        VInventario vInv = new VInventario();
        GestionProducto mProd = new GestionProducto();
        CInventario cInv = new CInventario(vInv, mProd);
        vInv.setController(cInv);
        abrirVentana(vInv);
    }
    
    private void abrirVentanaPuntoVenta() {
        VPuntoVenta vVenta = new VPuntoVenta();
        GestionProducto mProd = new GestionProducto(); 
        
        new CPuntoVenta(vVenta, mProd);
        
        vista.desktop.add(vVenta);
        vVenta.setVisible(true);
    }
}