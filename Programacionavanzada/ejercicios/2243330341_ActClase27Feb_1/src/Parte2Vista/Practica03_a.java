package Parte2Vista;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Parte2Controlador.ListaCategorias;
import Parte2Controlador.ListaInsumos;
import Parte2Modelo.Categoria;
import Parte2Modelo.Insumo;

import javax.swing.JScrollPane;

public class Practica03_a extends JFrame implements ActionListener {

    ListaInsumos listainsumo;
    ListaCategorias listacategorias;
    
    private JComboBox ComboCategoria;
    private JTextField Tid, Tinsumo;
    private JButton Bagregar, Beliminar, Bsalir;
    private JTextArea areaProductos;
    private JPanel panelFormulario;

    public Practica03_a() {
        super("Administración de Productos");
        
        this.inicializarcategorias();
        this.listainsumo = new ListaInsumos();
        
        setBounds(0, 0, 390, 370);
        panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        getContentPane().add(panelFormulario, BorderLayout.CENTER);

        JLabel labelId = new JLabel("ID:");
        labelId.setBounds(10, 9, 71, 20);
        panelFormulario.add(labelId);

        this.Tid = new JTextField(10);
        this.Tid.setEditable(false);
        this.Tid.setBounds(91, 9, 147, 20);
        panelFormulario.add(Tid);

        JLabel labelInsumo = new JLabel("Insumo:");
        labelInsumo.setBounds(10, 34, 71, 20);
        panelFormulario.add(labelInsumo);

        this.Tinsumo = new JTextField(20);
        this.Tinsumo.setEditable(false);
        this.Tinsumo.setBounds(91, 35, 147, 20);
        panelFormulario.add(Tinsumo);

        JLabel labelCategoria = new JLabel("Categoría:");
        labelCategoria.setBounds(10, 66, 71, 20);
        panelFormulario.add(labelCategoria);

        this.ComboCategoria = new JComboBox(this.listacategorias.CategoriasArreglo());
        this.ComboCategoria.setEditable(false);
        this.ComboCategoria.setBounds(91, 66, 160, 20);
        this.ComboCategoria.addActionListener(this);
        panelFormulario.add(ComboCategoria);

        this.Bagregar = new JButton("Agregar");
        this.Bagregar.setBounds(20, 104, 111, 20);
        this.Bagregar.addActionListener(this);
        panelFormulario.add(Bagregar);

        this.Beliminar = new JButton("Eliminar");
        this.Beliminar.setBounds(153, 104, 111, 20);
        this.Beliminar.addActionListener(this);
        panelFormulario.add(Beliminar);

        this.Bsalir = new JButton("Salir");
        this.Bsalir.setBounds(274, 104, 79, 20);
        this.Bsalir.addActionListener(this);
        panelFormulario.add(Bsalir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 132, 357, 179);
        panelFormulario.add(scrollPane);

        this.areaProductos = new JTextArea(10, 40);
        this.areaProductos.setEditable(false);
        scrollPane.setViewportView(areaProductos);

        this.Volveralinicio();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void inicializarcategorias() {
        this.listacategorias = new ListaCategorias();
        this.listacategorias.agregarCategoria(new Categoria("01", "Materiales"));
        this.listacategorias.agregarCategoria(new Categoria("02", "Mano de Obra"));
        this.listacategorias.agregarCategoria(new Categoria("03", "Maquinaria y Equipo"));
        this.listacategorias.agregarCategoria(new Categoria("04", "Servicios"));
    }

    public void Altas() {
        if (this.Bagregar.getText().compareTo("Agregar") == 0) {
            this.Bagregar.setText("Salvar");
            this.Bsalir.setText("Cancelar");
            this.Beliminar.setEnabled(false);
            this.Tid.setEditable(true);
            this.Tinsumo.setEditable(true);
            this.ComboCategoria.setEnabled(true);
            this.ComboCategoria.setFocusable(true);
        } else {
            if (esdatoscompletos()) {
                String id = this.Tid.getText().trim();
                String insumo = this.Tinsumo.getText().trim();
                String idcategoria = ((Categoria) this.ComboCategoria.getSelectedItem()).getIdcategoria().trim();

                Insumo nodo = new Insumo(id, insumo, idcategoria);
                if (!this.listainsumo.agregarInsumo(nodo)) {
                    JOptionPane.showMessageDialog(this, "Lo siente el id " + id + " ya existe lo tiene asignado " + this.listainsumo.buscarInsumo(id));
                } else {
                    this.areaProductos.setText(this.listainsumo.toString());
                }
            }
            this.Volveralinicio();
        }
    }

    public void Bajas() {
        String id = this.Tid.getText().trim();
        if (this.listainsumo.eliminarInsumoPorId(id)) {
            this.areaProductos.setText(this.listainsumo.toString());
            JOptionPane.showMessageDialog(this, "Eliminado con éxito");
        } else {
            JOptionPane.showMessageDialog(this, "ID no encontrado");
        }
        this.Volveralinicio();
    }

    public void Volveralinicio() {
        this.Bagregar.setText("Agregar");
        this.Bsalir.setText("Salir");
        this.Beliminar.setEnabled(true);
        this.Tid.setEditable(false);
        this.Tinsumo.setEditable(false);
        this.ComboCategoria.setEditable(false);
        this.Tid.setText("");
        this.Tinsumo.setText("");
        this.ComboCategoria.setSelectedIndex(0);
    }

    public boolean esdatoscompletos() {
        return !this.Tid.getText().trim().isEmpty() && !this.Tinsumo.getText().trim().isEmpty();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bagregar) {
            this.Altas();
        } else if (e.getSource() == this.Beliminar) {
            if (!this.Tid.isEditable()) {
                this.Tid.setEditable(true);
                this.Tid.requestFocus();
            } else {
                this.Bajas();
            }
        } else if (e.getSource() == this.Bsalir) {
            if (this.Bsalir.getText().equals("Cancelar")) {
                this.Volveralinicio();
            } else {
                this.dispose();
            }
        }
    }

    public static void main(String[] args) {
        new Practica03_a();
    }
}