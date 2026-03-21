package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Controlador.CProducto;

import java.awt.*;
import java.util.Objects;

public class VProductos extends JInternalFrame {
	
    private JTextField txtId, txtNombre, txtPrecio;
    private JComboBox<String> cbCategoria;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnGuardar, btnSeleccionarImg;
    private JLabel lblPreview; 
    private String rutaSeleccionada = "src/img/default.png"; // Ruta corregida según Inicializador

    public VProductos() {
        super("Gestión del Catálogo de Productos", true, true, true, true);
        setSize(850, 750); // Un poco más ancho

        // --- Paleta de Colores y Fuentes ---
        Color colorFondo = new Color(245, 245, 247);
        Color colorAzul = new Color(0, 122, 255);
        Color colorGrisBoton = new Color(230, 230, 235);
        
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 18);
        Font fuenteEtiqueta = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteCampo = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteBotonPrincipal = new Font("Segoe UI", Font.BOLD, 14);

        JPanel pnlPrincipal = new JPanel(new BorderLayout(20, 20));
        pnlPrincipal.setBackground(colorFondo);
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(pnlPrincipal);

        // --- Título de la Sección ---
        JLabel lblTituloSeccion = new JLabel("Registrar Nuevo Producto");
        lblTituloSeccion.setFont(fuenteTitulo);
        pnlPrincipal.add(lblTituloSeccion, BorderLayout.NORTH);

        // --- Panel Izquierdo: Formulario (GridBagLayout para control preciso) ---
        JPanel pnlFormulario = new JPanel(new GridBagLayout());
        pnlFormulario.setBackground(Color.WHITE);
        pnlFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 10); gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Estilo común para JTextFields
        Dimension dimCampo = new Dimension(250, 35);

        // ID
        gbc.gridx = 0; gbc.gridy = 0; pnlFormulario.add(crearEtiquetaForm("ID Producto:", fuenteEtiqueta), gbc);
        txtId = new JTextField(); txtId.setPreferredSize(dimCampo); 
        txtId.setFont(fuenteCampo); // <-- CORREGIDO
        gbc.gridx = 1; addCampoConBorde(pnlFormulario, txtId, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1; pnlFormulario.add(crearEtiquetaForm("Nombre:", fuenteEtiqueta), gbc);
        txtNombre = new JTextField(); txtNombre.setPreferredSize(dimCampo); 
        txtNombre.setFont(fuenteCampo); // <-- CORREGIDO
        gbc.gridx = 1; addCampoConBorde(pnlFormulario, txtNombre, gbc);

        // Precio
        gbc.gridx = 0; gbc.gridy = 2; pnlFormulario.add(crearEtiquetaForm("Precio Unitario:", fuenteEtiqueta), gbc);
        txtPrecio = new JTextField(); txtPrecio.setPreferredSize(dimCampo); 
        txtPrecio.setFont(fuenteCampo); // <-- CORREGIDO
        gbc.gridx = 1; addCampoConBorde(pnlFormulario, txtPrecio, gbc);

        // Categoría
        gbc.gridx = 0; gbc.gridy = 3; pnlFormulario.add(crearEtiquetaForm("Categoría:", fuenteEtiqueta), gbc);
        cbCategoria = new JComboBox<>(new String[]{"Bebidas", "Abarrotes", "Carnes y Pescados", "Frutas y Verduras", "Lácteos y Huevo", "Mascotas", "Limpieza del Hogar", "Cuidado Personal", "Panadería y Tortillería", "Salchichonería", "Snacks y Dulcería"});
        cbCategoria.setPreferredSize(dimCampo); cbCategoria.setFont(fuenteCampo); cbCategoria.setBackground(Color.WHITE);
        gbc.gridx = 1; pnlFormulario.add(cbCategoria, gbc);

        // Imagen Preview y Botón
        JPanel pnlImagenAcciones = new JPanel(new BorderLayout(10, 0));
        pnlImagenAcciones.setBackground(Color.WHITE);
        pnlImagenAcciones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnSeleccionarImg = crearBotonModerno("Elegir Foto...", colorGrisBoton, Color.BLACK, new Font("Segoe UI", Font.PLAIN, 12));
        
        lblPreview = new JLabel("Sin imagen", JLabel.CENTER);
        lblPreview.setPreferredSize(new Dimension(80, 80));
        lblPreview.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        lblPreview.setOpaque(true); lblPreview.setBackground(colorFondo);

        pnlImagenAcciones.add(btnSeleccionarImg, BorderLayout.WEST);
        pnlImagenAcciones.add(lblPreview, BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; 
        pnlFormulario.add(pnlImagenAcciones, gbc);

        // Botón Guardar
        btnGuardar = crearBotonModerno("Guardar Producto en Catálogo", colorAzul, Color.WHITE, fuenteBotonPrincipal);
        btnGuardar.setActionCommand("INSERTAR");
        gbc.gridy = 5; gbc.insets = new Insets(25, 0, 10, 0);
        pnlFormulario.add(btnGuardar, gbc);
        
        pnlPrincipal.add(pnlFormulario, BorderLayout.WEST);

        // --- Panel Derecho: Tabla Modernizada ---
        JPanel pnlDerecho = new JPanel(new BorderLayout());
        pnlDerecho.setBackground(colorFondo);
        
        JLabel lblTituloTabla = new JLabel("Catálogo Actual");
        lblTituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Categoría"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE); header.setPreferredSize(new Dimension(0, 30));

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        scrollTable.getViewport().setBackground(Color.WHITE);

        pnlDerecho.add(lblTituloTabla, BorderLayout.NORTH);
        pnlDerecho.add(scrollTable, BorderLayout.CENTER);

        pnlPrincipal.add(pnlDerecho, BorderLayout.CENTER);
        
        // --- Acción para el botón de imagen ---
        btnSeleccionarImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Seleccionar Imagen del Producto");
            if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaSeleccionada = fc.getSelectedFile().getAbsolutePath();
                updatePreview(rutaSeleccionada);
            }
        });
    }

    // Métodos utilitarios de diseño
    private JLabel crearEtiquetaForm(String texto, Font fuente) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuente);
        return lbl;
    }

    private void addCampoConBorde(JPanel panel, JTextField campo, GridBagConstraints gbc) {
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8) // Padding interno
        ));
        panel.add(campo, gbc);
    }

    private JButton crearBotonModerno(String texto, Color fondo, Color textoColor, Font fuente) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente);
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFocusPainted(false);
        // Padding ajustable
        int paddingV = (fuente.getSize() > 13) ? 12 : 8;
        boton.setBorder(BorderFactory.createEmptyBorder(paddingV, 18, paddingV, 18));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public void updatePreview(String ruta) {
        if (ruta == null || ruta.isEmpty()) {
             lblPreview.setIcon(null); lblPreview.setText("Sin imagen"); return;
        }
        try {
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(lblPreview.getWidth()-4, lblPreview.getHeight()-4, Image.SCALE_SMOOTH);
            lblPreview.setIcon(new ImageIcon(img));
            lblPreview.setText("");
        } catch (Exception e) {
            lblPreview.setText("Error");
        }
    }

    // Getters y Setters (mantener iguales)
    public String getSelectedCategoria() { return (String) cbCategoria.getSelectedItem(); }
    public String getRutaImagen() { return rutaSeleccionada; }
    public JTable getTable() { return table; }
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtPrecio() { return txtPrecio; }

    public void setController(CProducto c) {
        btnGuardar.addActionListener(c);
    }
}