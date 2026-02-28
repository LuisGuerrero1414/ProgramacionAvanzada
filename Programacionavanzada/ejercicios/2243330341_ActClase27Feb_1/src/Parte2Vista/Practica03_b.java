package Parte2Vista;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Parte2Controlador.ListaCategorias;
import Parte2Modelo.Categoria;

public class Practica03_b extends JFrame implements ActionListener {

    ListaCategorias listacategorias;

    private JTextField Tid, Tcategoria;
    private JButton Bagregar, Beliminar, Bsalir;
    private JTextArea Tareacategoria;
    private JPanel panelFormulario;

    public Practica03_b() {
        super("Administracion de Categorias");
        
        this.listacategorias = new ListaCategorias();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 400);
        
        panelFormulario = new JPanel();
        panelFormulario.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelFormulario.setLayout(null);
        setContentPane(panelFormulario);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(10, 11, 46, 14);
        panelFormulario.add(lblId);

        Tid = new JTextField();
        Tid.setBounds(90, 8, 150, 20);
        Tid.setEditable(false);
        panelFormulario.add(Tid);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(10, 41, 80, 14);
        panelFormulario.add(lblCategoria);

        Tcategoria = new JTextField();
        Tcategoria.setBounds(90, 38, 150, 20);
        Tcategoria.setEditable(false); 
        panelFormulario.add(Tcategoria);

        Bagregar = new JButton("Agregar");
        Bagregar.setBounds(10, 80, 100, 23);
        Bagregar.addActionListener(this);
        panelFormulario.add(Bagregar);

        Beliminar = new JButton("Eliminar");
        Beliminar.setBounds(120, 80, 100, 23);
        Beliminar.addActionListener(this);
        panelFormulario.add(Beliminar);

        Bsalir = new JButton("Salir");
        Bsalir.setBounds(230, 80, 100, 23);
        Bsalir.addActionListener(this);
        panelFormulario.add(Bsalir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 120, 410, 220);
        panelFormulario.add(scrollPane);

        Tareacategoria = new JTextArea();
        Tareacategoria.setEditable(false);
        scrollPane.setViewportView(Tareacategoria);

        this.Volveralinicio();
        setVisible(true);
    }

    public void Volveralinicio() {
        this.Bagregar.setText("Agregar");
        this.Bsalir.setText("Salir");
        this.Beliminar.setEnabled(true);
        this.Tid.setEditable(false);
        this.Tcategoria.setEditable(false);
        this.Tid.setText("");
        this.Tcategoria.setText("");
    }

    public void Altas() {
        if (this.Bagregar.getText().equals("Agregar")) {
            this.Bagregar.setText("Salvar");
            this.Bsalir.setText("Cancelar");
            this.Beliminar.setEnabled(false);
            this.Tid.setEditable(true);
            this.Tcategoria.setEditable(true);
            this.Tid.requestFocus();
        } else {
            if (!Tid.getText().trim().isEmpty() && !Tcategoria.getText().trim().isEmpty()) {
                Categoria nueva = new Categoria(Tid.getText(), Tcategoria.getText());
               
                this.listacategorias.agregarCategoria(nueva);
                this.Tareacategoria.setText(this.listacategorias.toLinea());
            }
            this.Volveralinicio();
        }
    }

    public void Eliminar() {
      
        Object[] opciones = this.listacategorias.CategoriasArreglo();
        if (opciones.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay categorías para eliminar.");
            return;
        }
        
        Categoria seleccionada = (Categoria) JOptionPane.showInputDialog(null, 
                "Seleccione una categoría para eliminar:", "Eliminación", 
                JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (seleccionada != null) {
            this.listacategorias.eliminarCategoriaPorId(seleccionada.getIdcategoria());
            this.Tareacategoria.setText(this.listacategorias.toLinea());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Bagregar) {
            this.Altas();
        } else if (e.getSource() == Beliminar) {
            this.Eliminar();
        } else if (e.getSource() == Bsalir) {
            if (this.Bsalir.getText().equals("Cancelar")) {
                this.Volveralinicio();
            } else {
                this.dispose();
            }
        }
    }

    public static void main(String[] args) {
        new Practica03_b();
    }
}