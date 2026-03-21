package Persistencia;

import Modelo.*;
import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PersistenciaDatos {

    private Gson gson;

    public PersistenciaDatos() {
        // Configuramos un Gson especial que sepa manejar la clase abstracta Producto
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Producto.class, new JsonDeserializer<Producto>() {
            @Override
            public Producto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                // Leemos el campo "categoria" que guardamos en el JSON
                String categoria = jsonObject.get("categoria").getAsString();
                
                // Dependiendo de la categoría, le decimos a Gson qué clase real usar
                return switch (categoria) {
                    case "Bebidas" -> context.deserialize(json, Bebida.class);
                    case "Abarrotes" -> context.deserialize(json, Abarrote.class);
                    case "Carnes y Pescados" -> context.deserialize(json, CarnePescado.class);
                    case "Frutas y Verduras" -> context.deserialize(json, FrutaVerdura.class);
                    case "Lácteos y Huevo" -> context.deserialize(json, LacteoHuevo.class);
                    case "Mascotas" -> context.deserialize(json, Mascota.class);
                    case "Limpieza del Hogar" -> context.deserialize(json, Limpieza.class);
                    case "Cuidado Personal" -> context.deserialize(json, CuidadoPersonal.class);
                    case "Panadería y Tortillería" -> context.deserialize(json, Panaderia.class);
                    case "Salchichonería" -> context.deserialize(json, Salchichoneria.class);
                    case "Snacks y Dulcería" -> context.deserialize(json, SnackDulceria.class);
                    default -> context.deserialize(json, Abarrote.class);
                };
            }
        });
        this.gson = builder.setPrettyPrinting().create();
    }

    /**
     * Guarda cualquier lista de objetos en formato JSON.
     */
    public void guardarJSON(ArrayList<?> lista, String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo + ".json")) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Importa la lista de productos desde el archivo inventario.json.
     */
    public ArrayList<Producto> importarJSON() {
        ArrayList<Producto> lista = new ArrayList<>();
        File file = new File("inventario.json");
        if (!file.exists()) return lista;

        try (FileReader reader = new FileReader(file)) {
            Type tipoLista = new com.google.gson.reflect.TypeToken<ArrayList<Producto>>(){}.getType();
            ArrayList<Producto> temp = gson.fromJson(reader, tipoLista);
            if (temp != null) lista = temp;
        } catch (Exception e) {
            System.out.println("Error al importar: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Exporta la lista de productos a un archivo CSV compatible con Excel.
     * Esto soluciona el error en CInventario.
     */
    public void exportarExcel(ArrayList<Producto> lista, String nombreArchivo) {
        // Usamos .csv porque Excel lo abre directamente y no requiere librerías externas
        try (PrintWriter writer = new PrintWriter(new File(nombreArchivo + ".csv"))) {
            
            // Escribir encabezados
            writer.println("ID,Nombre,Categoria,Precio,Tipo de Almacenamiento");

            // Escribir datos de los productos usando polimorfismo
            for (Producto p : lista) {
                StringBuilder sb = new StringBuilder();
                sb.append(p.getId()).append(",");
                sb.append(p.getNombre()).append(",");
                sb.append(p.getCategoria()).append(",");
                sb.append(p.getPrecio()).append(",");
                // Llama al método abstracto implementado en cada subclase
                sb.append(p.getTipoAlmacenamiento());
                
                writer.println(sb.toString());
            }
            
            System.out.println("Archivo " + nombreArchivo + ".csv generado exitosamente.");
            
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se pudo crear el archivo. " + e.getMessage());
        }
    }
}