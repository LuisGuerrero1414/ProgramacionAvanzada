package Parte1;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent; // Nueva librería para detectar teclas
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Practica02_b2 extends JFrame implements ActionListener {

    private JPanel PanelPrincipal;
    private JButton Bsalir;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Cambiamos el nombre de la clase a Practica02_b2
                    Practica02_b2 frame = new Practica02_b2();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Practica02_b2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Ajustamos los límites según la nueva imagen (348x233)
        setBounds(100, 100, 348, 233);
        setTitle("Frame Practica02_b2");
        
        PanelPrincipal = new JPanel();
        PanelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(PanelPrincipal);
        PanelPrincipal.setLayout(null);

        Bsalir = new JButton("Salir");
        Bsalir.setBounds(104, 87, 89, 23);
        
        // --- NUEVA VARIACIÓN: MNEMÓNICOS ---
        // Establece 'S' como tecla de acceso rápido (Alt + S)
        Bsalir.setMnemonic(KeyEvent.VK_S);
        // Subraya la 'S' (índice 0) en la palabra "Salir"
        Bsalir.setDisplayedMnemonicIndex(0);
        
        Bsalir.addActionListener(this);
        PanelPrincipal.add(Bsalir);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bsalir) {
            JOptionPane.showMessageDialog(this, "Hasta Luego");
            this.dispose();
        }
    }
}