package Parte1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practica02_c extends JFrame implements ActionListener {

    // Declaración de opciones para el ComboBox
    String[] opciones = {"Opción 1", "Opción 2", "Opción 3"};
    
    // Declaración de componentes como globales para acceder en actionPerformed
    JComboBox<String> comboBox;
    JPanel panel;
    JButton Bsalir;
    JTextField campoTexto;
    JPasswordField campoPassword;
    JTextArea areaTexto;
    JFormattedTextField campoFormateado;
    JSpinner spinner;
    JSlider slider;

    public Practica02_c() {
        // Configuración básica de la ventana
        setTitle("Ejemplo de componentes de entrada de texto");
        setSize(442, 480); // Ajustado según el contenido
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un panel para contener los componentes
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 403, 365);

        // --- JButton ---
        JLabel etiqueta1 = new JLabel("JButton:");
        etiqueta1.setBounds(0, 3, 178, 36);
        Bsalir = new JButton("Haz clic para Salir");
        Bsalir.setBounds(188, 3, 172, 36);
        Bsalir.addActionListener(this);
        panel.add(etiqueta1);
        panel.add(Bsalir);

        // --- JTextField ---
        JLabel etiqueta2 = new JLabel("JTextField:");
        etiqueta2.setBounds(0, 49, 148, 36);
        campoTexto = new JTextField();
        campoTexto.setBounds(188, 50, 172, 36);
        panel.add(etiqueta2);
        panel.add(campoTexto);

        // --- JPasswordField ---
        JLabel etiqueta3 = new JLabel("JPasswordField:");
        etiqueta3.setBounds(0, 95, 148, 36);
        campoPassword = new JPasswordField();
        campoPassword.setBounds(188, 97, 172, 36);
        panel.add(etiqueta3);
        panel.add(campoPassword);

        // --- JTextArea (dentro de JScrollPane) ---
        JLabel etiqueta4 = new JLabel("JTextArea:");
        etiqueta4.setBounds(0, 141, 148, 36);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(188, 141, 172, 36);
        areaTexto = new JTextArea();
        scrollPane.setViewportView(areaTexto);
        panel.add(etiqueta4);
        panel.add(scrollPane);

        // --- JFormattedTextField ---
        JLabel etiqueta5 = new JLabel("JFormattedTextField:");
        etiqueta5.setBounds(0, 187, 141, 36);
        campoFormateado = new JFormattedTextField();
        campoFormateado.setBounds(188, 187, 172, 36);
        campoFormateado.setValue(12345.67);
        panel.add(etiqueta5);
        panel.add(campoFormateado);

        // --- JSpinner ---
        JLabel etiqueta6 = new JLabel("JSpinner:");
        etiqueta6.setBounds(0, 233, 141, 36);
        SpinnerNumberModel modeloSpinner = new SpinnerNumberModel(0, 0, 100, 1);
        spinner = new JSpinner(modeloSpinner);
        spinner.setBounds(188, 233, 172, 36);
        panel.add(etiqueta6);
        panel.add(spinner);

        // --- JSlider ---
        JLabel etiqueta7 = new JLabel("JSlider:");
        etiqueta7.setBounds(0, 279, 148, 36);
        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setBounds(188, 280, 178, 36);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(etiqueta7);
        panel.add(slider);

        // --- JComboBox ---
        JLabel etiqueta8 = new JLabel("JComboBox:");
        etiqueta8.setBounds(0, 325, 148, 36);
        comboBox = new JComboBox<>(opciones);
        comboBox.setBounds(188, 325, 172, 36);
        panel.add(etiqueta8);
        panel.add(comboBox);

        // Agregar el panel a la ventana y mostrarla
        getContentPane().setLayout(null);
        getContentPane().add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Practica02_c();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bsalir) {
            String cadena = "valor de Jtextfield: " + this.campoTexto.getText().trim();
            cadena += "\nvalor de JPasswordField: " + new String(this.campoPassword.getPassword()).trim();
            cadena += "\nvalor de JTextArea: " + this.areaTexto.getText();
            cadena += "\nvalor de JFormattedTextField: " + this.campoFormateado.getText().trim();
            cadena += "\nvalor de spinner: " + this.spinner.getValue().toString();
            cadena += "\nvalor de slider: " + String.valueOf(this.slider.getValue());
            
            if (comboBox.getSelectedIndex() > -1) {
                cadena += "\nvalor de combo es: " + this.comboBox.getSelectedItem().toString();
            }

            JOptionPane.showMessageDialog(this, cadena);
        }
    }
}