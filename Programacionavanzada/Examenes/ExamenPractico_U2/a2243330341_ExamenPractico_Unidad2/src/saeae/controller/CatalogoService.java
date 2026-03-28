package saeae.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import saeae.model.CatalogoDatos;

public class CatalogoService {

    private final Path archivoCatalogo;

    public CatalogoService(Path archivoCatalogo) {
        this.archivoCatalogo = archivoCatalogo;
    }

    public CatalogoDatos cargar() throws IOException {
        CatalogoDatos catalogo = new CatalogoDatos();
        DataFormatter fmt = new DataFormatter();
        try (InputStream in = Files.newInputStream(archivoCatalogo);
                Workbook wb = WorkbookFactory.create(in)) {
            Sheet sh = wb.getSheetAt(0);
            Iterator<Row> it = sh.rowIterator();
            if (!it.hasNext()) {
                catalogo.finalizarOrden();
                return catalogo;
            }
            it.next();
            while (it.hasNext()) {
                Row r = it.next();
                String letra = fmt.formatCellValue(cell(r, 0)).trim();
                String profesor = fmt.formatCellValue(cell(r, 1)).trim();
                String materia = fmt.formatCellValue(cell(r, 2)).trim();
                String matricula = fmt.formatCellValue(cell(r, 3)).trim();
                String alumno = fmt.formatCellValue(cell(r, 4)).trim();
                String numGrupo = fmt.formatCellValue(cell(r, 5)).trim();
                if (profesor.isEmpty() && materia.isEmpty()) {
                    continue;
                }
                catalogo.addFilaRaw(letra, profesor, materia, matricula, alumno, numGrupo);
            }
        }
        catalogo.finalizarOrden();
        return catalogo;
    }

    private static Cell cell(Row r, int idx) {
        Cell c = r.getCell(idx);
        return c;
    }
}
