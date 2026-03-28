package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class VUnidadesMedida extends JInternalFrame {
    private static final Color colorFondo = new Color(245, 245, 247);
    private static final Color colorAzul = new Color(0, 122, 255);
    private static final Color colorRojo = new Color(255, 59, 48);
    private static final Color colorGrisBoton = new Color(230, 230, 235);
    private static final Color bordePanel = new Color(210, 210, 210);

    public JTextField txtClave, txtDescripcion;
    public JButton btnGuardar, btnEliminar;
    public JTable tabla;
    public DefaultTableModel modelo;

    public VUnidadesMedida() {
        super("Configuración de Unidades de Medida", true, true, true, true);
        setSize(720, 480);

        Font fuenteEtiqueta = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteCampo = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 18);

        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15));
        pnlPrincipal.setBackground(colorFondo);
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(pnlPrincipal);

        JLabel lblTitulo = new JLabel("Unidades de medida (kg, lt, pieza, etc.)");
        lblTitulo.setFont(fuenteTitulo);
        JPanel pnlTit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTit.setOpaque(false);
        pnlTit.add(lblTitulo);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        txtClave = new JTextField(20);
        txtDescripcion = new JTextField(28);
        for (JTextField t : new JTextField[]{txtClave, txtDescripcion}) {
            t.setFont(fuenteCampo);
            t.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 14);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel lClave = new JLabel("Clave:");
        lClave.setFont(fuenteEtiqueta);
        JLabel lDesc = new JLabel("Descripción:");
        lDesc.setFont(fuenteEtiqueta);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        pnlForm.add(lClave, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        pnlForm.add(txtClave, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlForm.add(lDesc, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        pnlForm.add(txtDescripcion, gbc);

        btnGuardar = crearBoton("Guardar", colorAzul, Color.WHITE, fuenteBoton);
        btnGuardar.setActionCommand("GUARDAR_UNIDAD");
        btnEliminar = crearBoton("Eliminar", colorGrisBoton, Color.BLACK, fuenteBoton);
        btnEliminar.setForeground(colorRojo);
        btnEliminar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorRojo, 2),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        btnEliminar.setActionCommand("ELIMINAR_UNIDAD");

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnlBotones.setBackground(Color.WHITE);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnEliminar);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        pnlBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlForm.add(pnlBotones, gbc);

        modelo = new DefaultTableModel(new Object[]{"Clave", "Descripción"}, 0);
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setSelectionBackground(new Color(210, 230, 255));
        tabla.setSelectionForeground(Color.BLACK);
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(bordePanel));
        scroll.getViewport().setBackground(Color.WHITE);

        JLabel lblLista = new JLabel("Unidades configuradas");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JPanel pnlTabla = new JPanel(new BorderLayout(0, 10));
        pnlTabla.setOpaque(false);
        pnlTabla.add(lblLista, BorderLayout.NORTH);
        pnlTabla.add(scroll, BorderLayout.CENTER);

        JPanel pnlNorte = new JPanel();
        pnlNorte.setLayout(new BoxLayout(pnlNorte, BoxLayout.Y_AXIS));
        pnlNorte.setOpaque(false);
        pnlNorte.add(pnlTit);
        pnlNorte.add(Box.createVerticalStrut(8));
        pnlForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlNorte.add(pnlForm);

        pnlPrincipal.add(pnlNorte, BorderLayout.NORTH);
        pnlPrincipal.add(pnlTabla, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor, Font fuente) {
        JButton b = new JButton(texto);
        b.setFont(fuente);
        b.setBackground(fondo);
        b.setForeground(textoColor);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
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
