package Parte2Vista;

import Libreria.Archivotxt;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.ArrayList;

public class Practica03_c extends JFrame implements ActionListener {

    private JTextField Tid, Tcategoria;
    private JButton Bagregar, Bsalir;
    private JTextArea Tareacategoria;
    private Archivotxt manejadorArchivo;
    private final String RUTA_ARCHIVO = "categorias.txt";

    public Practica03_c() {
        super("Categorías con Persistencia de Archivos");
        manejadorArchivo = new Archivotxt(RUTA_ARCHIVO);
        
        setBounds(100, 100, 400, 400);
        setLayout(null);

        JLabel l1 = new JLabel("ID:"); l1.setBounds(10, 10, 50, 20); add(l1);
        Tid = new JTextField(); Tid.setBounds(80, 10, 150, 20); add(Tid);

        JLabel l2 = new JLabel("Nombre:"); l2.setBounds(10, 40, 60, 20); add(l2);
        Tcategoria = new JTextField(); Tcategoria.setBounds(80, 40, 150, 20); add(Tcategoria);

        Bagregar = new JButton("Guardar en TXT");
        Bagregar.setBounds(10, 80, 150, 25);
        Bagregar.addActionListener(this);
        add(Bagregar);

        Bsalir = new JButton("Salir");
        Bsalir.setBounds(170, 80, 100, 25);
        Bsalir.addActionListener(this);
        add(Bsalir);

        Tareacategoria = new JTextArea();
        JScrollPane sp = new JScrollPane(Tareacategoria);
        sp.setBounds(10, 120, 360, 220);
        add(sp);

        this.cargarDesdeArchivo();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void cargarDesdeArchivo() {
        ArrayList<String> datos = manejadorArchivo.leer();
        StringBuilder contenido = new StringBuilder();
        for (String linea : datos) {
            contenido.append(linea).append("\n");
        }
        this.Tareacategoria.setText(contenido.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Bagregar) {
            String id = Tid.getText().trim();
            String cat = Tcategoria.getText().trim();

            if (!id.isEmpty() && !cat.isEmpty()) {

                String lineaCsv = id + "," + cat;
                manejadorArchivo.guardar(lineaCsv);
                
                Tid.setText("");
                Tcategoria.setText("");
                cargarDesdeArchivo();
                JOptionPane.showMessageDialog(this, "Guardado exitoso en categorias.txt");
            }
        } else if (e.getSource() == Bsalir) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new Practica03_c();
    }
}