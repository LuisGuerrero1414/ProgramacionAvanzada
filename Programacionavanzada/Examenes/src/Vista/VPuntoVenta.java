package Vista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
public class VPuntoVenta extends JInternalFrame {
	
    public JComboBox<String> cbProductos;
    public JTextField txtCantidad, txtSubtotal, txtIva, txtTotal;
    public JButton btnAnadir, btnModificar, btnEliminar, btnLimpiar, btnPagar, btnTicket;
    public JTable tablaCarrito;
    public DefaultTableModel modeloCarrito;

    public VPuntoVenta() {
        super("Punto de Venta", true, true, true, true);
        setSize(950, 600);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlTop = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5); g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; pnlTop.add(new JLabel("SELECCIÓN DE PRODUCTO"), g);
        cbProductos = new JComboBox<>();
        g.gridy = 1; pnlTop.add(cbProductos, g);
        
        g.gridx = 1; g.gridy = 0; pnlTop.add(new JLabel("Cantidad:"), g);
        txtCantidad = new JTextField(5);
        g.gridy = 1; pnlTop.add(txtCantidad, g);
        
        JPanel pnlBtns = new JPanel();
        btnAnadir = new JButton("Añadir a Carrito");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        pnlBtns.add(btnAnadir); pnlBtns.add(btnModificar); pnlBtns.add(btnEliminar);
        g.gridx = 2; g.gridy = 1; pnlTop.add(pnlBtns, g);

        String[] col = {"Cód", "Descripción", "Cant", "P.Unit", "Total"};
        modeloCarrito = new DefaultTableModel(col, 0);
        tablaCarrito = new JTable(modeloCarrito);
        
        JPanel pnlInferior = new JPanel(new BorderLayout());
        JPanel pnlTotales = new JPanel(new GridLayout(3, 2, 5, 5));
        txtSubtotal = new JTextField(8); txtSubtotal.setEditable(false);
        txtIva = new JTextField(8); txtIva.setEditable(false);
        txtTotal = new JTextField(8); txtTotal.setEditable(false);
        
        pnlTotales.add(new JLabel("Subtotal:")); pnlTotales.add(txtSubtotal);
        pnlTotales.add(new JLabel("IVA (16%):")); pnlTotales.add(txtIva);
        pnlTotales.add(new JLabel("Total:")); pnlTotales.add(txtTotal);

        JPanel pnlFinal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLimpiar = new JButton("Limpiar Carrito");
        btnPagar = new JButton("Procesar Pago");
        btnTicket = new JButton("Exportar Ticket");
        pnlFinal.add(btnLimpiar); pnlFinal.add(btnPagar); pnlFinal.add(btnTicket);

        pnlInferior.add(pnlTotales, BorderLayout.WEST);
        pnlInferior.add(pnlFinal, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        add(pnlInferior, BorderLayout.SOUTH);
    }

    public void setController(ActionListener c) {
        btnAnadir.addActionListener(c);
        btnModificar.addActionListener(c);
        btnEliminar.addActionListener(c);
        btnLimpiar.addActionListener(c);
        btnPagar.addActionListener(c);
        btnTicket.addActionListener(c);
    }
}