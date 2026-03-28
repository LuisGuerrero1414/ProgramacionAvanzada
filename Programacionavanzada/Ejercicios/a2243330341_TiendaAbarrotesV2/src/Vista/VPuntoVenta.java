package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;

public class VPuntoVenta extends JInternalFrame {
    
    // Componentes de Selección
    public JComboBox<String> cbProductos;
    public JTextField txtCantidad;
    public JButton btnAnadir;
    
    // Tabla de Carrito
    public JTable tablaCarrito;
    public DefaultTableModel modeloCarrito;
    
    // Totales
    public JTextField txtSubtotal, txtIva, txtTotal;
    
    // Botones de Acción
    public JButton btnLimpiar, btnPagar, btnTicket;
    
    // Imagen
    public JLabel lblFotoProducto;

    public VPuntoVenta() {
        super("Terminal de Punto de Venta", true, true, true, true);
        setSize(1100, 750); // Espacioso

        // --- Paleta de Colores y Fuentes Moderna ---
        Color colorFondo = new Color(245, 245, 247);
        Color colorAzul = new Color(0, 122, 255);
        Color colorVerde = new Color(52, 199, 89); // Para pagar
        Color colorGrisBoton = new Color(230, 230, 235);
        
        Font fuenteEtiqueta = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteCampo = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteTotal = new Font("Segoe UI", Font.BOLD, 22);

        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15));
        pnlPrincipal.setBackground(colorFondo);
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(pnlPrincipal);

        // --- 1. PANEL SUPERIOR (SELECCIÓN MODERNA) ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        cbProductos = new JComboBox<>();
        cbProductos.setFont(fuenteCampo); cbProductos.setPreferredSize(new Dimension(300, 35));
        cbProductos.setBackground(Color.WHITE);

        txtCantidad = new JTextField("1", 5);
        txtCantidad.setFont(fuenteCampo); txtCantidad.setHorizontalAlignment(JTextField.CENTER);
        // Borde estilizado para el campo de cantidad
        txtCantidad.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        btnAnadir = crearBotonModerno("Añadir al Carrito", colorGrisBoton, Color.BLACK, fuenteBoton);
        
        JLabel lblProd = new JLabel("Producto:"); lblProd.setFont(fuenteEtiqueta);
        JLabel lblCant = new JLabel("Cantidad:"); lblCant.setFont(fuenteEtiqueta);

        pnlTop.add(lblProd);
        pnlTop.add(cbProductos);
        pnlTop.add(lblCant);
        pnlTop.add(txtCantidad);
        pnlTop.add(btnAnadir);

        // --- 2. PANEL CENTRAL: TABLA DE CARRITO Modernizada ---
        String[] columnas = {"ID", "Descripción", "Cant.", "Precio Unit.", "Subtotal"};
        modeloCarrito = new DefaultTableModel(columnas, 0);
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCarrito.setRowHeight(30);
        tablaCarrito.setGridColor(new Color(230, 230, 230));

        JTableHeader header = tablaCarrito.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE); header.setPreferredSize(new Dimension(0, 35));

        JScrollPane scrollTabla = new JScrollPane(tablaCarrito);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        scrollTabla.getViewport().setBackground(Color.WHITE);

        // --- 3. PANEL DERECHO: VISTA DEL PRODUCTO Modernizada ---
        JPanel pnlDerecho = new JPanel(new BorderLayout());
        pnlDerecho.setBackground(colorFondo);
        pnlDerecho.setPreferredSize(new Dimension(280, 0));

        JPanel pnlContenedorImagen = new JPanel(new GridBagLayout());
        pnlContenedorImagen.setBackground(Color.WHITE);
        pnlContenedorImagen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        lblFotoProducto = new JLabel("Sin Imagen", JLabel.CENTER);
        lblFotoProducto.setFont(fuenteEtiqueta);
        lblFotoProducto.setForeground(Color.GRAY);
        lblFotoProducto.setPreferredSize(new Dimension(250, 250));
        
        JLabel lblTituloPreview = new JLabel("Vista del Producto", JLabel.LEFT);
        lblTituloPreview.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloPreview.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        pnlContenedorImagen.add(lblFotoProducto);
        pnlDerecho.add(lblTituloPreview, BorderLayout.NORTH);
        pnlDerecho.add(pnlContenedorImagen, BorderLayout.CENTER);

        // --- 4. PANEL INFERIOR: TOTALES Y BOTONES MODERNIZADOS ---
        JPanel pnlInferior = new JPanel(new BorderLayout(20, 0));
        pnlInferior.setBackground(colorFondo);
        pnlInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Sub-panel de TOTALES estilizado
        JPanel pnlTotales = new JPanel(new GridBagLayout());
        pnlTotales.setBackground(Color.WHITE);
        pnlTotales.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15); gbc.anchor = GridBagConstraints.EAST;

        txtSubtotal = crearCampoTotal(12, fuenteCampo);
        txtIva = crearCampoTotal(12, fuenteCampo);
        txtTotal = crearCampoTotal(12, fuenteTotal);
        txtTotal.setForeground(colorAzul); // Resaltar el total en azul
        txtTotal.setBackground(new Color(235, 245, 255)); // Fondo azul suave

        JLabel lblSub = new JLabel("Subtotal: $"); lblSub.setFont(fuenteEtiqueta);
        JLabel lblIva = new JLabel("IVA (16%): $"); lblIva.setFont(fuenteEtiqueta);
        JLabel lblTot = new JLabel("TOTAL: $"); lblTot.setFont(new Font("Segoe UI", Font.BOLD, 16));

        gbc.gridx = 0; gbc.gridy = 0; pnlTotales.add(lblSub, gbc); gbc.gridx = 1; pnlTotales.add(txtSubtotal, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlTotales.add(lblIva, gbc); gbc.gridx = 1; pnlTotales.add(txtIva, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 1.0; pnlTotales.add(lblTot, gbc); gbc.gridx = 1; pnlTotales.add(txtTotal, gbc);

        // Sub-panel de BOTONES DE ACCIÓN (Separados)
        JPanel pnlAccionesFinales = new JPanel(new BorderLayout(15, 0));
        pnlAccionesFinales.setBackground(colorFondo);

        btnLimpiar = crearBotonModerno("Limpiar Carrito", colorGrisBoton, Color.BLACK, fuenteBoton);
        
        JPanel pnlPagarTicket = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlPagarTicket.setBackground(colorFondo);
        
        btnPagar = crearBotonModerno("Procesar Pago", colorVerde, Color.WHITE, fuenteBoton);
        btnTicket = crearBotonModerno("Exportar Ticket (JSON)", colorAzul, Color.WHITE, fuenteBoton);

        pnlPagarTicket.add(btnPagar);
        pnlPagarTicket.add(btnTicket);
        
        pnlAccionesFinales.add(btnLimpiar, BorderLayout.WEST);
        pnlAccionesFinales.add(pnlPagarTicket, BorderLayout.EAST);

        pnlInferior.add(pnlTotales, BorderLayout.WEST);
        pnlInferior.add(pnlAccionesFinales, BorderLayout.CENTER);

        // --- 5. AGREGAR PANELES PRINCIPALES ---
        pnlPrincipal.add(pnlTop, BorderLayout.NORTH);
        pnlPrincipal.add(scrollTabla, BorderLayout.CENTER);
        pnlPrincipal.add(pnlDerecho, BorderLayout.EAST);
        pnlPrincipal.add(pnlInferior, BorderLayout.SOUTH);
    }

    // Métodos utilitarios de diseño
    private JButton crearBotonModerno(String texto, Color fondo, Color textoColor, Font fuente) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente);
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22)); // Padding generoso
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JTextField crearCampoTotal(int columnas, Font fuente) {
        JTextField txt = new JTextField(columnas);
        txt.setFont(fuente);
        txt.setEditable(false);
        txt.setHorizontalAlignment(JTextField.RIGHT);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return txt;
    }

    // Mantener métodos updateImage y setController iguales (ya estaban corregidos)
    public void updateImage(String ruta) {
        if (ruta == null || ruta.isEmpty()) {
            lblFotoProducto.setIcon(null); lblFotoProducto.setText("Sin Imagen"); return;
        }
        try {
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(230, 230, Image.SCALE_SMOOTH);
            lblFotoProducto.setIcon(new ImageIcon(img));
            lblFotoProducto.setText("");
        } catch (Exception e) {
            lblFotoProducto.setText("Error");
        }
    }

    public void setController(ActionListener c) {
        btnAnadir.addActionListener(c);
        btnLimpiar.addActionListener(c);
        btnPagar.addActionListener(c);
        btnTicket.addActionListener(c);
        cbProductos.addActionListener(c); 
    }
}