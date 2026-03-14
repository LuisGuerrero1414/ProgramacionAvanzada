import Vista.VistaPrincipal;
import Controlador.CMenuPrincipal;
public class Main {
    public static void main(String[] args) {
    	
        VistaPrincipal principal = new VistaPrincipal();
        
        new CMenuPrincipal(principal);
        
        principal.setVisible(true);
    }
}