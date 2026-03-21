package Vista;
import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {
	
    public JDesktopPane desktop;
    public JMenuItem itemInventario, itemPuntoVenta, itemProductos;

    public VistaPrincipal() {
        setTitle("Sistema Integral de Ventas e Inventario - UAT");
        setSize(1300, 900); // Un poco más grande para el nuevo diseño
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Definición de Colores Modernos
        Color colorFondoApp = new Color(245, 245, 247);
        Color colorBarraMenu = Color.WHITE;
        Font fuenteMenu = new Font("Segoe UI", Font.PLAIN, 14);

        desktop = new JDesktopPane();
        desktop.setBackground(colorFondoApp); // Fondo claro para el área de trabajo
        setContentPane(desktop);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(colorBarraMenu);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 210, 210)));
        menuBar.setPreferredSize(new Dimension(0, 40)); // Barra más alta

        JMenu menuModulos = new JMenu("Módulos del Sistema");
        menuModulos.setFont(fuenteMenu);
        
        // Iconos genéricos para el menú (opcional, pero mejora mucho)
        itemInventario = new JMenuItem("1. Ver Inventario");
        itemPuntoVenta = new JMenuItem("2. Punto de Venta");
        itemProductos = new JMenuItem("3. Catálogo de Productos");

        // Estilo para los items del menú
        JMenuItem[] items = {itemInventario, itemPuntoVenta, itemProductos};
        for (JMenuItem item : items) {
            item.setFont(fuenteMenu);
            item.setPreferredSize(new Dimension(200, 30));
        }

        menuModulos.add(itemInventario);
        menuModulos.add(itemPuntoVenta);
        menuModulos.add(itemProductos);
        menuBar.add(menuModulos);
        setJMenuBar(menuBar);
    }
}