package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class VInventario extends JInternalFrame {
    public JTable tabla;
    public DefaultTableModel modelo;
    public JComboBox<String> cbTipo, cbSubtipo;
    public JTextField txtIdBusqueda;
    public JButton btnEliminar, btnExcelGral, btnExcelCat, btnLimpiar, btnEntrada, btnSalida, btnAjuste;
    public JTextField txtCantidadMovimiento;
    public JLabel lblImagen; // El cuadro de la derecha para la foto

    public VInventario() {
        super("Control de Inventario y Stock", true, true, true, true);
        setSize(1100, 700); // Más espacioso
        
        // --- Paleta de Colores Modernos ---
        Color colorFondo = new Color(245, 245, 247); // Gris muy claro
        Color colorAzul = new Color(0, 122, 255); // Azul moderno
        Color colorRojo = new Color(255, 59, 48); // Rojo peligro
        Color colorGrisBoton = new Color(230, 230, 235);
        
        Font fuenteEtiqueta = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 13);

        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15));
        pnlPrincipal.setBackground(colorFondo);
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(pnlPrincipal);

        // --- Panel Superior: Filtros (Estilo Barra de Herramientas) ---
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFiltros.setBackground(Color.WHITE);
        pnlFiltros.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        txtIdBusqueda = new JTextField(12);
        txtIdBusqueda.setFont(fuenteEtiqueta);
        
        cbTipo = new JComboBox<>(new String[]{"--- Seleccionar ---", "Despensa Básica", "Lácteos y Huevo", "Bebidas y Líquidos", "Botanas y Dulces", "Frutas y Verduras", "Carnes y Salchichonería", "Cuidado del Hogar", "Higiene y Cuidado Personal", "Alimentos Preparados/Enlatados"});
        cbTipo.setFont(fuenteEtiqueta);
        cbTipo.setBackground(Color.WHITE);
        cbSubtipo = new JComboBox<>(new String[]{"--- Seleccionar ---"});
        cbSubtipo.setFont(fuenteEtiqueta);
        cbSubtipo.setBackground(Color.WHITE);

        btnLimpiar = crearBotonModerno("Limpiar Filtros", colorGrisBoton, Color.BLACK, fuenteBoton);
        
        JLabel lblId = new JLabel("Buscar ID:"); lblId.setFont(fuenteEtiqueta);
        JLabel lblCat = new JLabel("Categoría:"); lblCat.setFont(fuenteEtiqueta);

        pnlFiltros.add(lblId);
        pnlFiltros.add(txtIdBusqueda);
        pnlFiltros.add(lblCat);
        pnlFiltros.add(cbTipo);
        pnlFiltros.add(new JLabel("Subcategoría:"));
        pnlFiltros.add(cbSubtipo);
        pnlFiltros.add(btnLimpiar);

        // --- Panel Central: Tabla Modernizada ---
        String[] columnas = {"ID", "SKU", "Nombre", "Tipo", "Subtipo", "Unidad", "Cantidad", "P.Compra", "%Gan", "P.Venta", "Estado"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30); // Filas más altas
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setSelectionBackground(new Color(210, 230, 255)); // Color de selección suave
        tabla.setSelectionForeground(Color.BLACK);

        // Estilo del encabezado de la tabla
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        scroll.getViewport().setBackground(Color.WHITE);

        // --- Panel Derecho: Vista Previa Modernizada ---
        JPanel pnlDerecho = new JPanel(new BorderLayout());
        pnlDerecho.setBackground(colorFondo);
        pnlDerecho.setPreferredSize(new Dimension(280, 0));

        JPanel pnlContenedorImagen = new JPanel(new GridBagLayout());
        pnlContenedorImagen.setBackground(Color.WHITE);
        pnlContenedorImagen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        lblImagen = new JLabel("Seleccione un producto", JLabel.CENTER);
        lblImagen.setFont(fuenteEtiqueta);
        lblImagen.setForeground(Color.GRAY);
        lblImagen.setPreferredSize(new Dimension(250, 250));
        
        // Título estilizado para la vista previa
        JLabel lblTituloPreview = new JLabel("Detalles del Item", JLabel.LEFT);
        lblTituloPreview.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloPreview.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        pnlContenedorImagen.add(lblImagen);
        pnlDerecho.add(lblTituloPreview, BorderLayout.NORTH);
        pnlDerecho.add(pnlContenedorImagen, BorderLayout.CENTER);

        // --- Panel Inferior: Botones de Acción (Separados por función) ---
        JPanel pnlInferior = new JPanel(new BorderLayout(15, 0));
        pnlInferior.setBackground(colorFondo);
        pnlInferior.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Grupo IZQUIERDO: Acciones de Tabla
        JPanel pnlAccionesTabla = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlAccionesTabla.setBackground(colorFondo);
        btnEliminar = crearBotonModerno("Eliminar Producto", colorRojo, Color.WHITE, fuenteBoton);
        txtCantidadMovimiento = new JTextField("1", 5);
        btnEntrada = crearBotonModerno("Entrada", Color.WHITE, Color.BLACK, fuenteBoton);
        btnSalida = crearBotonModerno("Salida", Color.WHITE, Color.BLACK, fuenteBoton);
        btnAjuste = crearBotonModerno("Ajuste", Color.WHITE, Color.BLACK, fuenteBoton);
        btnEntrada.setActionCommand("MOV_ENTRADA");
        btnSalida.setActionCommand("MOV_SALIDA");
        btnAjuste.setActionCommand("MOV_AJUSTE");
        pnlAccionesTabla.add(btnEliminar);
        pnlAccionesTabla.add(txtCantidadMovimiento);
        pnlAccionesTabla.add(btnEntrada);
        pnlAccionesTabla.add(btnSalida);
        pnlAccionesTabla.add(btnAjuste);

        // Grupo DERECHO: Reportes
        JPanel pnlReportes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlReportes.setBackground(colorFondo);
        btnExcelGral = crearBotonModerno("Exportar Inventario General", colorAzul, Color.WHITE, fuenteBoton);
        btnExcelCat = crearBotonModerno("Exportar por Categoría", Color.WHITE, colorAzul, fuenteBoton);
        // Borde azul para el botón secundario
        btnExcelCat.setBorder(BorderFactory.createLineBorder(colorAzul, 2));

        // Asignar comandos (mantener los mismos para el controlador)
        btnEliminar.setActionCommand("ELIMINAR");
        btnExcelGral.setActionCommand("EXCEL_GENERAL");
        btnExcelCat.setActionCommand("EXCEL_CATEGORIA");
        btnLimpiar.setActionCommand("LIMPIAR");

        pnlReportes.add(btnExcelCat);
        pnlReportes.add(btnExcelGral);

        pnlInferior.add(pnlAccionesTabla, BorderLayout.WEST);
        pnlInferior.add(pnlReportes, BorderLayout.EAST);

        // Agregar paneles principales al contenedor BorderLayout
        pnlPrincipal.add(pnlFiltros, BorderLayout.NORTH);
        pnlPrincipal.add(scroll, BorderLayout.CENTER);
        pnlPrincipal.add(pnlDerecho, BorderLayout.EAST);
        pnlPrincipal.add(pnlInferior, BorderLayout.SOUTH);
    }

    // Método utilitario para crear botones estilizados fácilmente
    private JButton crearBotonModerno(String texto, Color fondo, Color textoColor, Font fuente) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente);
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover simple (puedes expandir esto con MouseListener si quieres)
        if (fondo.equals(new Color(0, 122, 255))) { // Si es el azul
             boton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    boton.setBackground(new Color(0, 100, 220));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    boton.setBackground(new Color(0, 122, 255));
                }
            });
        }
        
        return boton;
    }

    // Mantener el método updateImage igual, solo asegurándonos de que lblImagen esté inicializado
    public void updateImage(String ruta) {
        if (ruta == null || ruta.isEmpty()) {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin Imagen");
            return;
        }
        try {
            ImageIcon iconOriginal = new ImageIcon(ruta);
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(
                lblImagen.getWidth() - 15, 
                lblImagen.getHeight() - 15, 
                Image.SCALE_SMOOTH
            );
            lblImagen.setIcon(new ImageIcon(imgEscalada));
            lblImagen.setText(""); 
        } catch (Exception e) {
            lblImagen.setIcon(null);
            lblImagen.setText("Error al cargar");
        }
    }
}