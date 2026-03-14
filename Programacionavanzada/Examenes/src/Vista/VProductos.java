package Vista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import Controlador.CProducto;

public class VProductos extends JInternalFrame {
	
    private JTextField txtId, txtNombre, txtPrecio;
    private JRadioButton rbCat1, rbCat2;
    private ButtonGroup btnGroup;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnGuardar, btnEliminar, btnModificar, btnConsultar;

    public VProductos() {
        super("Gestión de Productos", true, true, true, true);
        setBounds(100, 100, 600, 500);
        setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("ID:"), gbc);
        txtId = new JTextField(10);
        gbc.gridx = 1; add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(10);
        gbc.gridx = 1; add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Precio:"), gbc);
        txtPrecio = new JTextField(10);
        gbc.gridx = 1; add(txtPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Categoría:"), gbc);
        rbCat1 = new JRadioButton("Electrónica");
        rbCat2 = new JRadioButton("Hogar");
        btnGroup = new ButtonGroup();
        btnGroup.add(rbCat1); btnGroup.add(rbCat2);
        JPanel pnlRadio = new JPanel();
        pnlRadio.add(rbCat1); pnlRadio.add(rbCat2);
        gbc.gridx = 1; add(pnlRadio, gbc);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setActionCommand("INSERTAR");
        gbc.gridx = 0; gbc.gridy = 4; add(btnGuardar, gbc);
        
        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Categoría"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        add(scroll, gbc);
    }

    public void setController(CProducto c) {
        btnGuardar.addActionListener(c);
    }

    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public ButtonGroup getBtnGroup() { return btnGroup; }
    public JTable getTable() { return table; }
    
    public String getSelectedCategoria() {
        if (rbCat1.isSelected()) return rbCat1.getText();
        if (rbCat2.isSelected()) return rbCat2.getText();
        return "Sin Categoría";
    }
}