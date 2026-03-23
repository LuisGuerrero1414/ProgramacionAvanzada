package persistence;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.IndicadorCedula;
import model.IndicadorGrupo;

/**
 * Exportación de reportes a Excel (.xlsx) mediante Apache POI.
 */
public class ExportadorExcel {

    private ExportadorExcel() {
    }

    /**
     * Crea un libro con dos hojas: indicadores por grupo y cédula 3.3.2.
     *
     * @param destino ruta del archivo .xlsx
     * @param grupos  primera tabla
     * @param cedula  segunda tabla
     * @throws IOException error al escribir
     */
    public static long exportar(Path destino, List<IndicadorGrupo> grupos, List<IndicadorCedula> cedula)
            throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet hojaGrupos = wb.createSheet("Promedios por grupo");
            escribirEncabezadoGrupo(hojaGrupos.createRow(0));
            int fila = 1;
            for (IndicadorGrupo g : grupos) {
                Row r = hojaGrupos.createRow(fila++);
                int c = 0;
                r.createCell(c++).setCellValue(g.getProfesor());
                r.createCell(c++).setCellValue(g.getAsignatura());
                r.createCell(c++).setCellValue(g.getGrupo());
                r.createCell(c++).setCellValue(g.getPromedio());
                r.createCell(c++).setCellValue(g.getNumAlumnos());
                r.createCell(c++).setCellValue(g.getNumReprobados());
                r.createCell(c++).setCellValue(g.getPorcentajeAprobados());
                r.createCell(c++).setCellValue(g.getPorcentajeReprobados());
                r.createCell(c++).setCellValue(g.getPromedioAcreditados());
            }

            Sheet hojaCedula = wb.createSheet("Cedula 3.3.2");
            escribirEncabezadoCedula(hojaCedula.createRow(0));
            fila = 1;
            for (IndicadorCedula ic : cedula) {
                Row r = hojaCedula.createRow(fila++);
                int c = 0;
                r.createCell(c++).setCellValue(ic.getAcademia());
                r.createCell(c++).setCellValue(ic.getAsignatura());
                r.createCell(c++).setCellValue(ic.getNumeroGrupos());
                r.createCell(c++).setCellValue(ic.getPromedio());
                r.createCell(c++).setCellValue(ic.getPorcentajeMayorAlPromedio());
                r.createCell(c++).setCellValue(ic.getPorcentajeReprobacion());
                r.createCell(c++).setCellValue(ic.getProfesores());
            }

            Path parent = destino.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (OutputStream out = Files.newOutputStream(destino)) {
                wb.write(out);
            }
            if (!Files.exists(destino)) {
                throw new IOException("No se creó el archivo de salida: " + destino);
            }
            long bytes = Files.size(destino);
            if (bytes <= 0L) {
                throw new IOException("El archivo Excel se generó vacío: " + destino);
            }
            return bytes;
        } catch (IOException e) {
            throw new IOException("No se pudo exportar el archivo Excel: " + destino, e);
        }
    }

    private static void escribirEncabezadoGrupo(Row r) {
        String[] cols = {
                "Profesor", "Asignatura", "Grupo", "Promedio", "Num. Alumnos", "Num. Reprobados",
                "% Aprobados", "% Reprobados", "Promedio Acreditados"
        };
        for (int i = 0; i < cols.length; i++) {
            r.createCell(i).setCellValue(cols[i]);
        }
    }

    private static void escribirEncabezadoCedula(Row r) {
        String[] cols = {
                "Academia", "Asignatura", "Numero de Grupos", "Promedio", "% Mayor al Promedio",
                "% Reprobacion", "Profesores"
        };
        for (int i = 0; i < cols.length; i++) {
            r.createCell(i).setCellValue(cols[i]);
        }
    }
}
