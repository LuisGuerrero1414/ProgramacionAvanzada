package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class VProveedores extends JInternalFrame {
    private static final Color colorFondo = new Color(245, 245, 247);
    private static final Color colorAzul = new Color(0, 122, 255);
    private static final Color colorRojo = new Color(255, 59, 48);
    private static final Color colorVerde = new Color(52, 199, 89);
    private static final Color bordePanel = new Color(210, 210, 210);

    public JTextField txtId, txtNombre, txtTelefono, txtEmail, txtIdProducto, txtIdProveedor;
    public JButton btnGuardar, btnEliminar, btnAsociar, btnDesasociar;
    public JTable tablaProveedores, tablaRelaciones;
    public DefaultTableModel modeloProveedores, modeloRelaciones;

    public VProveedores() {
        super("Gestión de Proveedores", true, true, true, true);
        setSize(1020, 680);

        Font fuenteEtiqueta = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteCampo = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 15);

        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15));
        pnlPrincipal.setBackground(colorFondo);
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(pnlPrincipal);

        // --- Directorio: varias filas para no recortar email ni botones (FlowLayout en una sola línea desborda) ---
        JPanel pnlProv = new JPanel();
        pnlProv.setLayout(new BoxLayout(pnlProv, BoxLayout.Y_AXIS));
        pnlProv.setBackground(Color.WHITE);
        pnlProv.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        txtId = campo(fuenteCampo, 8);
        txtNombre = campo(fuenteCampo, 22);
        txtTelefono = campo(fuenteCampo, 14);
        txtEmail = campo(fuenteCampo, 0);

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 6));
        fila1.setBackground(Color.WHITE);
        fila1.add(etiqueta("ID:", fuenteEtiqueta));
        fila1.add(txtId);
        fila1.add(etiqueta("Nombre:", fuenteEtiqueta));
        fila1.add(txtNombre);
        fila1.add(etiqueta("Teléfono:", fuenteEtiqueta));
        fila1.add(txtTelefono);
        fila1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel filaEmail = new JPanel(new BorderLayout(12, 0));
        filaEmail.setBackground(Color.WHITE);
        JLabel lblEmail = etiqueta("Email:", fuenteEtiqueta);
        lblEmail.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
        filaEmail.add(lblEmail, BorderLayout.WEST);
        filaEmail.add(txtEmail, BorderLayout.CENTER);
        filaEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        btnGuardar = crearBoton("Guardar proveedor", colorVerde, Color.WHITE, fuenteBoton);
        btnGuardar.setActionCommand("GUARDAR_PROV");
        btnEliminar = crearBoton("Eliminar proveedor", colorRojo, Color.WHITE, fuenteBoton);
        btnEliminar.setActionCommand("ELIMINAR_PROV");
        Dimension tamBot = new Dimension(200, 40);
        btnGuardar.setPreferredSize(tamBot);
        btnGuardar.setMinimumSize(tamBot);
        btnEliminar.setPreferredSize(tamBot);
        btnEliminar.setMinimumSize(tamBot);

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));
        filaBotones.setBackground(Color.WHITE);
        filaBotones.add(btnGuardar);
        filaBotones.add(btnEliminar);
        filaBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlProv.add(fila1);
        pnlProv.add(Box.createVerticalStrut(6));
        pnlProv.add(filaEmail);
        pnlProv.add(Box.createVerticalStrut(10));
        pnlProv.add(filaBotones);

        // --- Asociaciones ---
        JPanel pnlAsoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        pnlAsoc.setBackground(Color.WHITE);
        pnlAsoc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));

        txtIdProducto = campo(fuenteCampo, 6);
        txtIdProveedor = campo(fuenteCampo, 6);
        pnlAsoc.add(etiqueta("ID producto:", fuenteEtiqueta));
        pnlAsoc.add(txtIdProducto);
        pnlAsoc.add(etiqueta("ID proveedor:", fuenteEtiqueta));
        pnlAsoc.add(txtIdProveedor);

        btnAsociar = crearBoton("Asociar", colorAzul, Color.WHITE, fuenteBoton);
        btnAsociar.setActionCommand("ASOCIAR");
        btnDesasociar = crearBoton("Desasociar", Color.WHITE, colorAzul, fuenteBoton);
        btnDesasociar.setBorder(BorderFactory.createLineBorder(colorAzul, 2));
        btnDesasociar.setActionCommand("DESASOCIAR");
        pnlAsoc.add(btnAsociar);
        pnlAsoc.add(btnDesasociar);

        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setOpaque(false);
        JLabel lblDir = new JLabel("Directorio de proveedores");
        lblDir.setFont(fuenteTitulo);
        lblDir.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTop.add(lblDir);
        pnlTop.add(Box.createVerticalStrut(8));
        pnlProv.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlAsoc.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTop.add(pnlProv);
        pnlTop.add(Box.createVerticalStrut(10));
        JLabel lblRel = new JLabel("Relación producto ↔ proveedor");
        lblRel.setFont(fuenteTitulo);
        lblRel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTop.add(lblRel);
        pnlTop.add(Box.createVerticalStrut(8));
        pnlTop.add(pnlAsoc);

        modeloProveedores = new DefaultTableModel(new Object[]{"ID", "Nombre", "Teléfono", "Email"}, 0);
        tablaProveedores = tablaEstilo(modeloProveedores);
        modeloRelaciones = new DefaultTableModel(new Object[]{"ID producto", "ID proveedor"}, 0);
        tablaRelaciones = tablaEstilo(modeloRelaciones);

        JLabel lblTabProv = new JLabel("Proveedores registrados");
        lblTabProv.setFont(fuenteTitulo);
        JPanel wrapProv = new JPanel(new BorderLayout(0, 8));
        wrapProv.setOpaque(false);
        wrapProv.add(lblTabProv, BorderLayout.NORTH);
        wrapProv.add(envolverTabla(tablaProveedores), BorderLayout.CENTER);

        JLabel lblTabRel = new JLabel("Asociaciones");
        lblTabRel.setFont(fuenteTitulo);
        JPanel wrapRel = new JPanel(new BorderLayout(0, 8));
        wrapRel.setOpaque(false);
        wrapRel.add(lblTabRel, BorderLayout.NORTH);
        wrapRel.add(envolverTabla(tablaRelaciones), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, wrapProv, wrapRel);
        split.setResizeWeight(0.55);
        split.setBorder(null);

        JPanel pnlSplitEnv = new JPanel(new BorderLayout());
        pnlSplitEnv.setOpaque(false);
        pnlSplitEnv.add(split, BorderLayout.CENTER);

        pnlPrincipal.add(pnlTop, BorderLayout.NORTH);
        pnlPrincipal.add(pnlSplitEnv, BorderLayout.CENTER);
    }

    private static JLabel etiqueta(String t, Font f) {
        JLabel l = new JLabel(t);
        l.setFont(f);
        return l;
    }

    private static JTextField campo(Font f, int cols) {
        JTextField t = cols > 0 ? new JTextField(cols) : new JTextField();
        t.setFont(f);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        return t;
    }

    private JTable tablaEstilo(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(30);
        t.setGridColor(new Color(230, 230, 230));
        t.setSelectionBackground(new Color(210, 230, 255));
        t.setSelectionForeground(Color.BLACK);
        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(Color.WHITE);
        h.setPreferredSize(new Dimension(0, 35));
        return t;
    }

    private JScrollPane envolverTabla(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createLineBorder(bordePanel));
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor, Font fuente) {
        JButton b = new JButton(texto);
        b.setFont(fuente);
        b.setBackground(fondo);
        b.setForeground(textoColor);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(true);
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
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
}
