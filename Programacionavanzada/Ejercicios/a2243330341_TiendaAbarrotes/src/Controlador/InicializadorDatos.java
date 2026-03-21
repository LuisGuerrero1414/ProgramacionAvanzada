package Controlador;

import Modelo.*;
import Persistencia.PersistenciaDatos;
import java.util.ArrayList;
import java.io.File;

public class InicializadorDatos {

    public static void cargarTodo(GestionProducto inventario) {
        // 1. Verificamos si ya existe el archivo para no duplicar datos
        File file = new File("inventario.json");
        if (file.exists() && file.length() > 0) {
            return; 
        }

        ArrayList<Producto> lista = inventario.getLista();

        // --- ABARROTES ---
        lista.add(new Abarrote(101, "Aceite 1L", 45.0, "src/img/aceite.jpg"));
        lista.add(new Abarrote(102, "Arroz 1kg", 22.5, "src/img/arroz.jpg"));
        lista.add(new Abarrote(103, "Frijol 1kg", 35.0, "src/img/frijol.jpg"));
        lista.add(new Abarrote(104, "Pasta 500g", 12.0, "src/img/pasta.jpg"));
        lista.add(new Abarrote(105, "Azúcar 1kg", 28.0, "src/img/azucar.jpg"));

        // --- BEBIDAS ---
        lista.add(new Bebida(201, "Agua 600ml", 15.0, "src/img/agua.jpg"));
        lista.add(new Bebida(202, "Refresco 2L", 38.0, "src/img/refresco.jpg"));
        lista.add(new Bebida(203, "Jugo de Naranja", 25.0, "src/img/jugo.jpg"));
        lista.add(new Bebida(204, "Café Soluble", 85.0, "src/img/cafe.jpg"));
        lista.add(new Bebida(205, "Té Helado", 40.0, "src/img/te.jpg"));

        // --- LÁCTEOS Y HUEVO ---
        lista.add(new LacteoHuevo(301, "Leche Entera 1L", 26.0, "src/img/leche.jpg"));
        lista.add(new LacteoHuevo(302, "Huevo 12 pzas", 85.0, "src/img/huevo.jpg"));
        lista.add(new LacteoHuevo(303, "Yogurt Natural", 42.0, "src/img/yogurt.jpg"));
        lista.add(new LacteoHuevo(304, "Mantequilla", 28.5, "src/img/mante.jpg"));
        lista.add(new LacteoHuevo(305, "Crema Ácida", 32.0, "src/img/crema.jpg"));

        // --- FRUTAS Y VERDURAS ---
        lista.add(new FrutaVerdura(401, "Manzana Roja kg", 45.0, "src/img/manzana.jpg"));
        lista.add(new FrutaVerdura(402, "Plátano kg", 22.0, "src/img/platano.jpg"));
        lista.add(new FrutaVerdura(403, "Tomate kg", 35.0, "src/img/tomate.jpg"));
        lista.add(new FrutaVerdura(404, "Lechuga Romana", 18.0, "src/img/lechuga.jpg"));
        lista.add(new FrutaVerdura(405, "Aguacate kg", 75.0, "src/img/aguacate.jpg"));

        // --- CARNES Y PESCADOS ---
        lista.add(new CarnePescado(501, "Pechuga de Pollo", 120.0, "src/img/pollo.jpg"));
        lista.add(new CarnePescado(502, "Bistec de Res", 185.0, "src/img/res.jpg"));
        lista.add(new CarnePescado(503, "Chuleta de Cerdo", 110.0, "src/img/cerdo.jpg"));
        lista.add(new CarnePescado(504, "Filete de Pescado", 140.0, "src/img/pescado.jpg"));
        lista.add(new CarnePescado(505, "Camarón Limpio", 220.0, "src/img/camaron.jpg"));

        // --- SALCHICHONERÍA ---
        lista.add(new Salchichoneria(601, "Jamón de Pavo", 65.0, "src/img/jamon.jpg"));
        lista.add(new Salchichoneria(602, "Salchicha Viena", 42.0, "src/img/salchicha.jpg"));
        lista.add(new Salchichoneria(603, "Tocino Ahumado", 78.0, "src/img/tocino.jpg"));
        lista.add(new Salchichoneria(604, "Queso Panela", 85.0, "src/img/queso.jpg"));
        lista.add(new Salchichoneria(605, "Chorizo Casero", 55.0, "src/img/chorizo.jpg"));

        // --- PANADERÍA ---
        lista.add(new Panaderia(701, "Pan de Caja", 48.0, "src/img/pan.jpg"));
        lista.add(new Panaderia(702, "Concha Vainilla", 12.0, "src/img/concha.jpg"));
        lista.add(new Panaderia(703, "Bolillo pza", 2.5, "src/img/bolillo.jpg"));
        lista.add(new Panaderia(704, "Tortilla kg", 22.0, "src/img/tortilla.jpg"));
        lista.add(new Panaderia(705, "Dona Chocolate", 15.0, "src/img/dona.jpg"));

        // --- LIMPIEZA ---
        lista.add(new Limpieza(801, "Detergente Polvo", 95.0, "src/img/det.jpg"));
        lista.add(new Limpieza(802, "Suavizante 1L", 45.0, "src/img/suav.jpg"));
        lista.add(new Limpieza(803, "Cloro", 18.0, "src/img/cloro.jpg"));
        lista.add(new Limpieza(804, "Papel Higiénico", 35.0, "src/img/papel.jpg"));
        lista.add(new Limpieza(805, "Escoba", 65.0, "src/img/escoba.jpg"));

        // --- CUIDADO PERSONAL ---
        lista.add(new CuidadoPersonal(901, "Shampoo 400ml", 75.0, "src/img/shamp.jpg"));
        lista.add(new CuidadoPersonal(902, "Jabón de Barra", 18.5, "src/img/jabon.jpg"));
        lista.add(new CuidadoPersonal(903, "Pasta Dental", 32.0, "src/img/pasta.jpg"));
        lista.add(new CuidadoPersonal(904, "Desodorante", 58.0, "src/img/deso.jpg"));
        lista.add(new CuidadoPersonal(905, "Crema Corporal", 85.0, "src/img/crema.jpg"));

        // --- SNACKS Y DULCERÍA ---
        lista.add(new SnackDulceria(1001, "Papas Original", 18.0, "src/img/papas.jpg"));
        lista.add(new SnackDulceria(1002, "Galletas Oreo", 22.0, "src/img/galletas.jpg"));
        lista.add(new SnackDulceria(1003, "Chocolate Barra", 15.5, "src/img/choco.jpg"));
        lista.add(new SnackDulceria(1004, "Gomitas", 12.0, "src/img/gomitas.jpg"));
        lista.add(new SnackDulceria(1005, "Cacahuates", 14.0, "src/img/cacahuate.jpg"));

        // --- MASCOTAS ---
        lista.add(new Mascota(1101, "Croquetas Perro", 180.0, "src/img/perro.jpg"));
        lista.add(new Mascota(1102, "Sobre Gato", 12.5, "src/img/gato.jpg"));
        lista.add(new Mascota(1103, "Arena Gato", 145.0, "src/img/arena.jpg"));
        lista.add(new Mascota(1104, "Juguete Hueso", 55.0, "src/img/jueg.jpg"));
        lista.add(new Mascota(1105, "Collar Perro", 85.0, "src/img/collar.jpg"));

        // 2. Guardar la carga inicial en el JSON
        PersistenciaDatos persistencia = new PersistenciaDatos();
        persistencia.guardarJSON(lista, "inventario");
        System.out.println(">>> Carga inicial de la UAT completada con éxito.");
    }
}