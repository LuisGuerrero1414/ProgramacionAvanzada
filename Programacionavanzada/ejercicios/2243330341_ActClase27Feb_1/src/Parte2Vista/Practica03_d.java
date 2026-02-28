package Parte2Vista;

import Libreria.Archivotxt;
import Parte2Controlador.ListaCategorias;
import Parte2Modelo.Categoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.ArrayList;

public class Practica03_d extends JFrame implements ActionListener {

    private JTextField Tid, Tinsumo;
    private JComboBox<Categoria> ComboCategoria;
    private JButton Bagregar, Bsalir;
    private JTextArea areaProductos;
    
    private ListaCategorias listacategorias;
    private Archivotxt manejadorArchivo;
    private final String NOMBRE_ARCHIVO = "insumos.txt";

    public Practica03_d() {
        super("Registro de Insumos con Archivo TXT");
        manejadorArchivo = new Archivotxt(NOMBRE_ARCHIVO);
        
        this.inicializarcategorias();
        
        setBounds(100, 100, 420, 450);
        setLayout(null);

        JLabel l1 = new JLabel("ID Producto:"); l1.setBounds(10, 10, 100, 20); add(l1);
        Tid = new JTextField(); Tid.setBounds(120, 10, 150, 20); add(Tid);

        JLabel l2 = new JLabel("Nombre Insumo:"); l2.setBounds(10, 40, 100, 20); add(l2);
        Tinsumo = new JTextField(); Tinsumo.setBounds(120, 40, 150, 20); add(Tinsumo);

        JLabel l3 = new JLabel("Categoría:"); l3.setBounds(10, 70, 100, 20); add(l3);
        ComboCategoria = new JComboBox<>(this.listacategorias.CategoriasArreglo());
        ComboCategoria.setBounds(120, 70, 180, 20);
        add(ComboCategoria);

        Bagregar = new JButton("Guardar Insumo");
        Bagregar.setBounds(10, 110, 150, 25);
        Bagregar.addActionListener(this);
        add(Bagregar);

        Bsalir = new JButton("Salir");
        Bsalir.setBounds(170, 110, 100, 25);
        Bsalir.addActionListener(this);
        add(Bsalir);

        areaProductos = new JTextArea();
        areaProductos.setEditable(false);
        JScrollPane sp = new JScrollPane(areaProductos);
        sp.setBounds(10, 150, 380, 240);
        add(sp);

        this.cargarDesdeArchivo();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void inicializarcategorias() {
        this.listacategorias = new ListaCategorias();
        this.listacategorias.agregarCategoria(new Categoria("01", "Materiales"));
        this.listacategorias.agregarCategoria(new Categoria("02", "Mano de Obra"));
        this.listacategorias.agregarCategoria(new Categoria("03", "Maquinaria y Equipo"));
        this.listacategorias.agregarCategoria(new Categoria("04", "Servicios"));
    }

    private void cargarDesdeArchivo() {
        ArrayList<String> lineas = manejadorArchivo.leer();
        StringBuilder sb = new StringBuilder();
        sb.append("ID\tINSUMO\t\tCATEGORÍA\n");
        sb.append("--------------------------------------------------\n");
        for (String l : lineas) {
            sb.append(l.replace(",", "\t\t")).append("\n");
        }
        this.areaProductos.setText(sb.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Bagregar) {
            String id = Tid.getText().trim();
            String nombre = Tinsumo.getText().trim();
            Categoria catSeleccionada = (Categoria) ComboCategoria.getSelectedItem();

            if (!id.isEmpty() && !nombre.isEmpty()) {
                String registro = id + "," + nombre + "," + catSeleccionada.getCategoria();
                manejadorArchivo.guardar(registro);
                
                Tid.setText("");
                Tinsumo.setText("");
                cargarDesdeArchivo();
                JOptionPane.showMessageDialog(this, "Insumo guardado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.");
            }
        } else if (e.getSource() == Bsalir) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new Practica03_d();
    }
}