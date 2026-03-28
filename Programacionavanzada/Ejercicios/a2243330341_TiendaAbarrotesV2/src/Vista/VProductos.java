package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Controlador.CProducto;
import java.awt.*;

public class VProductos extends JInternalFrame {

    private static final Color colorFondo = new Color(245, 245, 247);
    private static final Color colorAzul = new Color(0, 122, 255);
    private static final Color colorGrisBoton = new Color(230, 230, 235);
    private static final Color bordePanel = new Color(210, 210, 210);

    private JTextField txtId, txtSku, txtNombre, txtPrecioCompra, txtGanancia, txtCantidad;
    private JComboBox<String> cbCategoria, cbSubcategoria, cbUnidad;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnGuardar, btnSeleccionarImg;
    private JLabel lblPreview;
    private String rutaSeleccionada = "src/img/default.png";

    public VProductos() {
        super("Catálogo de Productos", true, true, true, true);
        setSize(1150, 720);

        Font fuenteEtiqueta = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteCampo = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 18);
        Font fuenteSub = new Font("Segoe UI", Font.BOLD, 15);

        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15));
        pnlPrincipal.setBackground(colorFondo);
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(pnlPrincipal);

        JLabel lblTitulo = new JLabel("Registrar nuevo producto");
        lblTitulo.setFont(fuenteTitulo);
        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTitulo.setOpaque(false);
        pnlTitulo.add(lblTitulo);
        pnlPrincipal.add(pnlTitulo, BorderLayout.NORTH);

        txtId = new JTextField();
        txtSku = new JTextField();
        txtNombre = new JTextField();
        txtPrecioCompra = new JTextField();
        txtGanancia = new JTextField("30");
        txtCantidad = new JTextField("10");
        cbCategoria = new JComboBox<>(new String[]{
                "Despensa Básica", "Lácteos y Huevo", "Bebidas y Líquidos", "Botanas y Dulces",
                "Frutas y Verduras", "Carnes y Salchichonería", "Cuidado del Hogar",
                "Higiene y Cuidado Personal", "Alimentos Preparados/Enlatados"});
        cbSubcategoria = new JComboBox<>(new String[]{"--- Seleccionar ---"});
        cbUnidad = new JComboBox<>(new String[]{"pieza", "kg", "lt"});

        for (JTextField t : new JTextField[]{txtId, txtSku, txtNombre, txtPrecioCompra, txtGanancia, txtCantidad}) {
            t.setFont(fuenteCampo);
            aplicarBordeCampo(t);
        }
        cbCategoria.setFont(fuenteCampo);
        cbSubcategoria.setFont(fuenteCampo);
        cbUnidad.setFont(fuenteCampo);
        cbCategoria.setBackground(Color.WHITE);
        cbSubcategoria.setBackground(Color.WHITE);
        cbUnidad.setBackground(Color.WHITE);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel, 1),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;
        agregarFilaForm(pnlForm, gbc, row++, new JLabel("ID Producto:"), txtId, fuenteEtiqueta);
        agregarFilaForm(pnlForm, gbc, row++, new JLabel("SKU:"), txtSku, fuenteEtiqueta);
        agregarFilaForm(pnlForm, gbc, row++, new JLabel("Nombre:"), txtNombre, fuenteEtiqueta);
        agregarFilaForm(pnlForm, gbc, row++, new JLabel("Precio compra:"), txtPrecioCompra, fuenteEtiqueta);
        agregarFilaForm(pnlForm, gbc, row++, new JLabel("% Ganancia:"), txtGanancia, fuenteEtiqueta);
        agregarFilaCombo(pnlForm, gbc, row++, new JLabel("Categoría:"), cbCategoria, fuenteEtiqueta);
        agregarFilaCombo(pnlForm, gbc, row++, new JLabel("Subcategoría:"), cbSubcategoria, fuenteEtiqueta);
        agregarFilaCombo(pnlForm, gbc, row++, new JLabel("Unidad:"), cbUnidad, fuenteEtiqueta);
        agregarFilaForm(pnlForm, gbc, row++, new JLabel("Stock:"), txtCantidad, fuenteEtiqueta);

        btnSeleccionarImg = crearBoton("Elegir foto…", colorGrisBoton, Color.BLACK, fuenteBoton);
        btnGuardar = crearBoton("Guardar en catálogo", colorAzul, Color.WHITE, fuenteBoton);
        btnGuardar.setActionCommand("INSERTAR");
        JPanel pnlBotonesForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnlBotonesForm.setBackground(Color.WHITE);
        pnlBotonesForm.add(btnSeleccionarImg);
        pnlBotonesForm.add(btnGuardar);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        pnlForm.add(pnlBotonesForm, gbc);

        lblPreview = new JLabel("Sin imagen", JLabel.CENTER);
        lblPreview.setFont(fuenteEtiqueta);
        lblPreview.setForeground(Color.GRAY);
        lblPreview.setPreferredSize(new Dimension(200, 160));
        lblPreview.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        lblPreview.setOpaque(true);
        lblPreview.setBackground(new Color(250, 250, 252));
        JLabel lblPrevTit = new JLabel("Vista previa (SKU = nombre de archivo)");
        lblPrevTit.setFont(fuenteEtiqueta);
        JPanel pnlPrevWrap = new JPanel(new BorderLayout(0, 8));
        pnlPrevWrap.setBackground(Color.WHITE);
        pnlPrevWrap.add(lblPrevTit, BorderLayout.NORTH);
        pnlPrevWrap.add(lblPreview, BorderLayout.CENTER);
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlForm.add(pnlPrevWrap, gbc);

        JPanel pnlIzq = new JPanel(new BorderLayout());
        pnlIzq.setOpaque(false);
        pnlIzq.setPreferredSize(new Dimension(400, 0));
        pnlIzq.add(pnlForm, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new Object[]{"ID", "SKU", "Nombre", "Categoría", "Subcategoría", "Unidad", "Stock", "P.Compra", "%Gan", "P.Venta"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(210, 230, 255));
        table.setSelectionForeground(Color.BLACK);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));
        JScrollPane scrollTabla = new JScrollPane(table);
        scrollTabla.setBorder(BorderFactory.createLineBorder(bordePanel));
        scrollTabla.getViewport().setBackground(Color.WHITE);

        JLabel lblTablaTit = new JLabel("Catálogo actual");
        lblTablaTit.setFont(fuenteSub);
        JPanel pnlCentro = new JPanel(new BorderLayout(0, 10));
        pnlCentro.setOpaque(false);
        pnlCentro.add(lblTablaTit, BorderLayout.NORTH);
        pnlCentro.add(scrollTabla, BorderLayout.CENTER);

        JPanel pnlCentroEnv = new JPanel(new BorderLayout());
        pnlCentroEnv.setOpaque(false);
        pnlCentroEnv.add(pnlCentro, BorderLayout.CENTER);

        JPanel pnlMainBody = new JPanel(new BorderLayout(18, 0));
        pnlMainBody.setOpaque(false);
        pnlMainBody.add(pnlIzq, BorderLayout.WEST);
        pnlMainBody.add(pnlCentroEnv, BorderLayout.CENTER);

        pnlPrincipal.add(pnlMainBody, BorderLayout.CENTER);

        btnSeleccionarImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaSeleccionada = fc.getSelectedFile().getAbsolutePath();
                updatePreview(rutaSeleccionada);
            }
        });
    }

    private static void aplicarBordeCampo(JTextField campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
    }

    private static void agregarFilaForm(JPanel p, GridBagConstraints gbc, int row, JLabel lbl, JTextField campo, Font fLbl) {
        lbl.setFont(fLbl);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        p.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        p.add(campo, gbc);
    }

    private static void agregarFilaCombo(JPanel p, GridBagConstraints gbc, int row, JLabel lbl, JComboBox<String> cb, Font fLbl) {
        lbl.setFont(fLbl);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        p.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        p.add(cb, gbc);
    }

    private JButton crearBoton(String texto, Color fondo, Color colorTexto, Font fuente) {
        JButton b = new JButton(texto);
        b.setFont(fuente);
        b.setBackground(fondo);
        b.setForeground(colorTexto);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (fondo.equals(colorAzul)) {
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(0, 100, 220));
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    b.setBackground(colorAzul);
                }
            });
        }
        return b;
    }

    public void updatePreview(String ruta) {
        if (ruta == null || ruta.isEmpty()) {
            lblPreview.setIcon(null);
            lblPreview.setText("Sin imagen");
            return;
        }
        try {
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(lblPreview.getWidth() > 0 ? lblPreview.getWidth() - 16 : 180, 150, Image.SCALE_SMOOTH);
            lblPreview.setIcon(new ImageIcon(img));
            lblPreview.setText("");
        } catch (Exception e) {
            lblPreview.setText("Error");
        }
    }

    public String getSelectedCategoria() { return (String) cbCategoria.getSelectedItem(); }
    public String getSelectedSubcategoria() { return (String) cbSubcategoria.getSelectedItem(); }
    public String getSelectedUnidad() { return (String) cbUnidad.getSelectedItem(); }
    public JComboBox<String> getCbCategoria() { return cbCategoria; }
    public JComboBox<String> getCbSubcategoria() { return cbSubcategoria; }
    public String getRutaImagen() { return rutaSeleccionada; }
    public JTable getTable() { return table; }
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtSku() { return txtSku; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtPrecioCompra() { return txtPrecioCompra; }
    public JTextField getTxtGanancia() { return txtGanancia; }
    public JTextField getTxtCantidad() { return txtCantidad; }

    public void reiniciarImagenPorDefecto() {
        rutaSeleccionada = "src/img/default.png";
        updatePreview(rutaSeleccionada);
    }

    public void setController(CProducto c) {
        btnGuardar.addActionListener(c);
    }
}
