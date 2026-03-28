package Vista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
public class VInventario extends JInternalFrame {
	
    public JTextField txtIdBusqueda, txtNombreBusqueda;
    public JComboBox<String> cbTipo;
    public JRadioButton rbTodos, rbDisponible, rbAgotado;
    public JButton btnBuscar, btnLimpiar, btnCrear, btnModificar, btnEliminar;
    public JTable tabla;
    public DefaultTableModel modelo;

    public VInventario() {
        super("Inventario", true, true, true, true);
        setSize(900, 500);
        setLayout(new BorderLayout(5, 5));

        JPanel pnlFiltros = new JPanel(new GridBagLayout());
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros y Búsqueda"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlFiltros.add(new JLabel("ID:"), gbc);
        txtIdBusqueda = new JTextField(10);
        gbc.gridy = 1; pnlFiltros.add(txtIdBusqueda, gbc);

        gbc.gridy = 2; pnlFiltros.add(new JLabel("Nombre:"), gbc);
        txtNombreBusqueda = new JTextField(10);
        gbc.gridy = 3; pnlFiltros.add(txtNombreBusqueda, gbc);

        gbc.gridy = 4; pnlFiltros.add(new JLabel("Tipo:"), gbc);
        cbTipo = new JComboBox<>(new String[]{"Todos", "Electrónica", "Hogar"});
        gbc.gridy = 5; pnlFiltros.add(cbTipo, gbc);

        JPanel pnlEstado = new JPanel(new GridLayout(3, 1));
        pnlEstado.setBorder(BorderFactory.createTitledBorder("Estado"));
        rbTodos = new JRadioButton("Todos", true);
        rbDisponible = new JRadioButton("Disponible");
        rbAgotado = new JRadioButton("Agotado");
        ButtonGroup grupoEstado = new ButtonGroup();
        grupoEstado.add(rbTodos); grupoEstado.add(rbDisponible); grupoEstado.add(rbAgotado);
        pnlEstado.add(rbTodos); pnlEstado.add(rbDisponible); pnlEstado.add(rbAgotado);
        gbc.gridy = 6; pnlFiltros.add(pnlEstado, gbc);

        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar Filtros");
        JPanel pnlBtnsFiltro = new JPanel();
        pnlBtnsFiltro.add(btnBuscar); pnlBtnsFiltro.add(btnLimpiar);
        gbc.gridy = 7; pnlFiltros.add(pnlBtnsFiltro, gbc);

        JPanel pnlDerecha = new JPanel(new BorderLayout());
        pnlDerecha.setBorder(BorderFactory.createTitledBorder("Vista de Inventario"));
        
        String[] col = {"ID", "Nombre", "Tipo", "Cantidad", "Precio", "Estado"};
        modelo = new DefaultTableModel(col, 0);
        tabla = new JTable(modelo);
        pnlDerecha.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        pnlAcciones.setBorder(BorderFactory.createTitledBorder("Acciones de Selección"));
        btnCrear = new JButton("Crear Nuevo");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        
        btnCrear.setActionCommand("CREAR");
        btnModificar.setActionCommand("MODIFICAR");
        btnEliminar.setActionCommand("ELIMINAR");
        btnBuscar.setActionCommand("BUSCAR");
        btnLimpiar.setActionCommand("LIMPIAR");

        pnlAcciones.add(btnCrear); pnlAcciones.add(btnModificar); pnlAcciones.add(btnEliminar);
        pnlDerecha.add(pnlAcciones, BorderLayout.SOUTH);

        add(pnlFiltros, BorderLayout.WEST);
        add(pnlDerecha, BorderLayout.CENTER);
    }

    public void setController(ActionListener c) {
        btnBuscar.addActionListener(c);
        btnLimpiar.addActionListener(c);
        btnCrear.addActionListener(c);
        btnModificar.addActionListener(c);
        btnEliminar.addActionListener(c);
    }
}