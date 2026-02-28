import Parte2Vista.Practica03_d;
import javax.swing.UIManager;
 
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Practica03_d();
            }
        });
    }
}
 