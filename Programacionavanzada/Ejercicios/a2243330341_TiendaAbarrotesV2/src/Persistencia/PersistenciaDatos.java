package Persistencia;

import Modelo.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDatos {

    private final Gson gson;

    public PersistenciaDatos() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Producto.class, new JsonDeserializer<Producto>() {
            @Override
            public Producto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                String categoria = jsonObject.get("categoria").getAsString();
                return switch (categoria) {
                    case "Bebidas y Líquidos" -> context.deserialize(json, Bebida.class);
                    case "Despensa Básica" -> context.deserialize(json, Abarrote.class);
                    case "Carnes y Salchichonería" -> context.deserialize(json, CarnePescado.class);
                    case "Frutas y Verduras" -> context.deserialize(json, FrutaVerdura.class);
                    case "Lácteos y Huevo" -> context.deserialize(json, LacteoHuevo.class);
                    case "Cuidado del Hogar" -> context.deserialize(json, Limpieza.class);
                    case "Higiene y Cuidado Personal" -> context.deserialize(json, CuidadoPersonal.class);
                    case "Botanas y Dulces" -> context.deserialize(json, SnackDulceria.class);
                    case "Alimentos Preparados/Enlatados" -> context.deserialize(json, Panaderia.class);
                    case "Mascotas" -> context.deserialize(json, Mascota.class);
                    default -> context.deserialize(json, Abarrote.class);
                };
            }
        });
        this.gson = builder.setPrettyPrinting().create();
    }

    public void guardarJSON(ArrayList<?> lista, String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo + ".json")) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public <T> ArrayList<T> cargarListaDesdeJSON(String nombreArchivo, Type tipoLista) {
        File file = new File(nombreArchivo + ".json");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            ArrayList<T> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public <T> void guardarListaEnJSON(List<T> lista, String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo + ".json")) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportarExcel(ArrayList<Producto> lista, String nombreArchivo) {
        File carpetaReportes = new File("reportes");
        if (!carpetaReportes.exists()) {
            carpetaReportes.mkdirs();
        }
        File archivo = new File(carpetaReportes, nombreArchivo + ".xlsx");

        try (Workbook workbook = new XSSFWorkbook();
                FileOutputStream fos = new FileOutputStream(archivo)) {
            Sheet sheet = workbook.createSheet("Inventario");

            Row encabezado = sheet.createRow(0);
            encabezado.createCell(0).setCellValue("ID");
            encabezado.createCell(1).setCellValue("SKU");
            encabezado.createCell(2).setCellValue("Nombre");
            encabezado.createCell(3).setCellValue("Categoria");
            encabezado.createCell(4).setCellValue("Subcategoria");
            encabezado.createCell(5).setCellValue("Unidad");
            encabezado.createCell(6).setCellValue("Stock");
            encabezado.createCell(7).setCellValue("Precio Compra");
            encabezado.createCell(8).setCellValue("% Ganancia");
            encabezado.createCell(9).setCellValue("Precio Venta");
            encabezado.createCell(10).setCellValue("Almacenamiento");

            int fila = 1;
            for (Producto p : lista) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getSku());
                row.createCell(2).setCellValue(p.getNombre());
                row.createCell(3).setCellValue(p.getCategoria());
                row.createCell(4).setCellValue(p.getSubcategoria());
                row.createCell(5).setCellValue(p.getUnidadMedida());
                row.createCell(6).setCellValue(p.getCantidad());
                row.createCell(7).setCellValue(p.getPrecioCompra());
                row.createCell(8).setCellValue(p.getPorcentajeGanancia());
                row.createCell(9).setCellValue(p.getPrecioVenta());
                row.createCell(10).setCellValue(p.getTipoAlmacenamiento());
            }

            for (int i = 0; i <= 10; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(fos);
        } catch (IOException e) {
            System.out.println("Error al exportar XLSX: " + e.getMessage());
        }
    }
}