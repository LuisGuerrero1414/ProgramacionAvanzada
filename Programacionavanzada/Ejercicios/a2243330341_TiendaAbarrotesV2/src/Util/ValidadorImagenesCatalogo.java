package Util;

import Modelo.Producto;
import Persistencia.PersistenciaDatos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;

public class ValidadorImagenesCatalogo {

    public static void main(String[] args) {
        boolean descargar = args.length > 0 && "--download".equalsIgnoreCase(args[0]);
        PersistenciaDatos persistencia = new PersistenciaDatos();
        ArrayList<Producto> productos = persistencia.importarJSON();
        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos en inventario.json");
            return;
        }

        int faltantes = 0;
        StringBuilder reporte = new StringBuilder();
        reporte.append("REPORTE IMAGENES POR SKU\n");
        reporte.append("========================\n");

        Map<String, String> urlPorSku = cargarUrlPorSku();
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(12)).build();

        for (Producto p : productos) {
            String sku = p.getSku();
            Path imagen = Path.of("src", "img", sku + ".jpg");
            if (!Files.exists(imagen) || imagen.toFile().length() == 0) {
                faltantes++;
                reporte.append("FALTA: ").append(sku).append(" -> ").append(p.getNombre()).append("\n");
                if (descargar && urlPorSku.containsKey(sku)) {
                    descargarImagen(client, urlPorSku.get(sku), imagen);
                }
            }
        }

        File out = new File("reporte_imagenes.txt");
        try (FileWriter writer = new FileWriter(out)) {
            writer.write(reporte.toString());
        } catch (IOException e) {
            System.out.println("No se pudo escribir reporte_imagenes.txt: " + e.getMessage());
        }

        System.out.println("Total productos: " + productos.size());
        System.out.println("Imágenes faltantes/vacías: " + faltantes);
        System.out.println("Reporte generado: " + out.getAbsolutePath());
    }

    private static Map<String, String> cargarUrlPorSku() {
        File f = new File("catalogo_urls.json");
        if (!f.exists()) {
            return Map.of();
        }
        Type tipoMap = new TypeToken<Map<String, String>>() {}.getType();
        try (FileReader r = new FileReader(f)) {
            Map<String, String> map = new Gson().fromJson(r, tipoMap);
            return map != null ? map : Map.of();
        } catch (Exception e) {
            return Map.of();
        }
    }

    private static void descargarImagen(HttpClient client, String url, Path destino) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().timeout(Duration.ofSeconds(20)).build();
            HttpResponse<byte[]> resp = client.send(req, HttpResponse.BodyHandlers.ofByteArray());
            if (resp.statusCode() == 200 && resp.body().length > 0) {
                Files.createDirectories(destino.getParent());
                Files.write(destino, resp.body(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (Exception ignored) {
            // La utilidad es de apoyo; si falla una descarga se reporta como faltante.
        }
    }
}
