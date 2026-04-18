package main;

import modelo.Conexion;
import modelo.LectorXML;
import modelo.Asignatura;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Prueba {
    public static void main(String[] args) {
   
        LectorXML xml = new LectorXML();
        xml.cargarConfiguracion();

        System.out.println("*** SISTEMA DE GESTION DE MATERIAS ***");
        System.out.println("\n--- CONFIGURACION LEIDA DEL XML ---");
        System.out.println("Motor activo:  " + xml.motor);
        System.out.println("Host:          " + xml.host);
        System.out.println("Instancia:     "
                + (xml.instancia == null || xml.instancia.isEmpty() ? "(predeterminada)" : xml.instancia));
        System.out.println("Puerto TCP:    "
                + (xml.puerto == null || xml.puerto.isEmpty() ? "(usa instanceName / Browser)" : xml.puerto));
        System.out.println("Base de datos: " + xml.db);
        System.out.println("Usuario:       " + xml.user);
        System.out.println("Password:      " + xml.pass); 
        System.out.println("---------------------------------------\n");

      
        Conexion con = new Conexion(xml);
        
      
        String sqlAsignaturas = "SELECT * FROM ASIGNATURA WHERE CARRERA = ?";
        Object[] parametros = {"IC"};

        ArrayList<Asignatura> listaMaterias = con.consultarAObjeto(sqlAsignaturas, parametros, Asignatura.class);

        System.out.println("--- RESULTADOS (CONSULTA SEGURA + REFLEXIÓN) ---");
        if (listaMaterias == null || listaMaterias.isEmpty()) {
            System.out.println("No se encontraron registros.");
        } else {
            for (Asignatura m : listaMaterias) {
                System.out.printf("Materia: %-45s | ID: %-15s | Plan: %s %n", 
                                   m.getMateria(), m.getIdMateria(), m.getPlanEstudio());
            }
        }
        System.out.println("---------------------------------------");

        System.out.println("\n--- PROBANDO MODIFICACION MULTIPLE ---");

        Map<String, Object> cambios = new HashMap<>();
        cambios.put("MATERIA", "HIDROLOGIA AVANZADA"); 
        cambios.put("PLAN_ESTUDIO", "2026-X"); 

        String filtroUpdate = "IDMATERIA = ?"; 
        Object[] paramUpdate = {"IT17.042"}; 

        if (con.modificar("ASIGNATURA", cambios, filtroUpdate, paramUpdate)) {
            System.out.println("¡Registro actualizado con éxito en SQL Server!");
        } else {
            System.out.println("No se pudo realizar la actualización.");
        }
        System.out.println("---------------------------------------");
    }
}