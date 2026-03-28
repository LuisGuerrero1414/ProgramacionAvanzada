import Modelo.GestionProducto;
import Controlador.CMenuPrincipal;
import Controlador.InicializadorDatos;
import Vista.VistaPrincipal;
public class Main {
	
    public static void main(String[] args) {
    	
        // 1. Instanciar el modelo que guardará los productos en memoria
        GestionProducto modeloGlobal = new GestionProducto();

        // 2. PRECARGA: Generar el archivo inventario.json con tus 11 categorías
        // Esto solo ocurre si el archivo no existe o está vacío
        InicializadorDatos.cargarTodo(modeloGlobal);

        // 3. Iniciar la interfaz gráfica principal
        VistaPrincipal vista = new VistaPrincipal();
        
        // 4. El CMenuPrincipal se encarga de abrir las subventanas (Punto de Venta, Inventario, etc.)
        new CMenuPrincipal(vista, modeloGlobal);
        
        vista.setVisible(true);
        System.out.println("Sistema iniciado correctamente.");
    }
}