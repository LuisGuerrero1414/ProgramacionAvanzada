package Persistencia;
import Modelo.Producto;
import java.io.*;
import java.util.ArrayList;
public class ArchivoCSV {
	
    private final String RUTA = "datos_productos.csv";

    public void exportarCSV(ArrayList<Producto> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (Producto p : lista) {
                pw.println(p.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Producto> importarCSV() {
        ArrayList<Producto> lista = new ArrayList<>();
        File file = new File(RUTA);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                lista.add(new Producto(
                    Integer.parseInt(datos[0]),
                    datos[1],
                    Double.parseDouble(datos[2]),
                    datos[3]
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}