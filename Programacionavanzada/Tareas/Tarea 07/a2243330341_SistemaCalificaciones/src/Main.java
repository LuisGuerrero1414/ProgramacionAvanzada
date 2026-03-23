import java.nio.file.Path;

import javax.swing.SwingUtilities;

import controller.AppController;
import controller.CatalogoController;
import persistence.RepositorioAcademias;
import persistence.RepositorioAsignaturas;
import view.VentanaPrincipal;

/**
 * Punto de entrada: inicia controladores y la interfaz Swing.
 */
public class Main {

    public static void main(String[] args) {
        Path datos = Path.of(System.getProperty("user.dir"), "data");
        RepositorioAcademias repoA = new RepositorioAcademias(datos);
        RepositorioAsignaturas repoS = new RepositorioAsignaturas(datos);
        AppController app = new AppController(repoA, repoS);
        CatalogoController cat = new CatalogoController(repoA, repoS);
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal v = new VentanaPrincipal(app, cat);
            v.setVisible(true);
        });
    }
}
