package Libreria;

import java.io.*;
import java.util.ArrayList;

public class Archivotxt {
    private String nombreArchivo;

    public Archivotxt(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    // Método para guardar una cadena en el archivo (añade al final)
    public void guardar(String linea) {
        try (FileWriter fw = new FileWriter(nombreArchivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println(linea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para leer todo el archivo y devolverlo como una lista de líneas
    public ArrayList<String> leer() {
        ArrayList<String> lineas = new ArrayList<>();
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) return lineas;

        try (FileReader fr = new FileReader(archivo);
             BufferedReader br = new BufferedReader(fr)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineas;
    }
}