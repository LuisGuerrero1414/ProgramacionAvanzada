package Vista;
import javax.swing.*;
import java.awt.*;
public class VistaPrincipal extends JFrame {
	
    public JDesktopPane desktop;
    public JMenuItem itemInventario, itemPuntoVenta, itemProductos;

    public VistaPrincipal() {
        setTitle("Sistema Integral de Ventas e Inventario");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktop = new JDesktopPane();
        desktop.setBackground(new Color(60, 63, 65));
        setContentPane(desktop);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuModulos = new JMenu("Módulos del Sistema");
        
        itemInventario = new JMenuItem("1. Ver Inventario");
        itemPuntoVenta = new JMenuItem("2. Punto de Venta");
        itemProductos = new JMenuItem("3. Catálogo de Productos");

        menuModulos.add(itemInventario);
        menuModulos.add(itemPuntoVenta);
        menuModulos.add(itemProductos);
        menuBar.add(menuModulos);
        setJMenuBar(menuBar);
    }
}