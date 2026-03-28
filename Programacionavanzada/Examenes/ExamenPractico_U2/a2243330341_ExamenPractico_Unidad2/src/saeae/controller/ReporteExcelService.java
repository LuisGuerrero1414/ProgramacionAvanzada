package saeae.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import saeae.model.Evaluacion;
import saeae.model.ListaCotejoItem;
import saeae.model.RubricaAlumnoFila;

public class ReporteExcelService {

    public static final String SHEET_PRODUCTO = "ReporteProductoIntegrador";
    public static final String SHEET_RUBRICA = "RubricaProducto";
    public static final String SHEET_LISTA = "ListaCotejoAtributo";

    private final Path plantilla;
    private final Path carpetaReportes;

    public ReporteExcelService(Path plantillaPath, Path carpetaReportes) {
        this.plantilla = plantillaPath;
        this.carpetaReportes = carpetaReportes;
    }

    public Path rutaArchivo(Evaluacion e) {
        String name = FileNameUtil.nombreArchivoReporte(e.getAsignatura(), e.getProfesor(), e.getGrupo());
        return carpetaReportes.resolve(name);
    }

    public void exportarOActualizar(Evaluacion e) throws IOException {
        Files.createDirectories(carpetaReportes);
        Path destino = rutaArchivo(e);
        if (!Files.isRegularFile(destino)) {
            Files.copy(plantilla, destino, StandardCopyOption.REPLACE_EXISTING);
        }
        Path tmp = Files.createTempFile(carpetaReportes, "saeae_", ".xlsx");
        try {
            try (InputStream in = Files.newInputStream(destino);
                    Workbook wb = WorkbookFactory.create(in)) {
                llenar(wb, e);
                try (OutputStream out = Files.newOutputStream(tmp)) {
                    wb.write(out);
                }
            }
            try {
                Files.move(tmp, destino, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException ex) {
                Files.copy(tmp, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    private void llenar(Workbook wb, Evaluacion e) {
        llenarProducto(wb.getSheet(SHEET_PRODUCTO), e);
        llenarRubrica(wb.getSheet(SHEET_RUBRICA), e);
        llenarLista(wb.getSheet(SHEET_LISTA), e);
    }

    private void llenarProducto(Sheet sh, Evaluacion e) {
        if (sh == null) {
            return;
        }
        var p = e.getProductoIntegrador();
        var d = e.getDetalleInstrumento();
        set(sh, 1, 2, p.getProgramaEducativo());
        set(sh, 2, 2, nullToEmpty(e.getAsignatura()));
        set(sh, 2, 6, nullToEmpty(e.getGrupo()));
        set(sh, 3, 2, nullToEmpty(e.getProfesor()));
        set(sh, 4, 2, p.getPeriodo());
        set(sh, 4, 5, p.getActividad());
        set(sh, 5, 2, p.getAtributoEgreso());
        set(sh, 6, 2, p.getCriterioDesempeno());
        set(sh, 7, 2, p.getIndicadores());
        if (d.getObservaciones() != null && !d.getObservaciones().isBlank()) {
            set(sh, 8, 2, d.getObservaciones());
        } else {
            set(sh, 8, 2, p.getObservaciones());
        }
    }

    private void llenarRubrica(Sheet sh, Evaluacion e) {
        if (sh == null) {
            return;
        }
        var p = e.getProductoIntegrador();
        set(sh, 1, 5, p.getProgramaEducativo());
        set(sh, 2, 4, nullToEmpty(e.getAsignatura()));
        set(sh, 3, 4, nullToEmpty(e.getProfesor()));
        set(sh, 4, 4, p.getActividad());
        set(sh, 8, 4, p.getNivelAtributo());
        set(sh, 5, 2, p.getAtributoEgreso());
        set(sh, 6, 2, p.getCriterioDesempeno());
        set(sh, 7, 2, p.getIndicadores());
        int rAlumno = 12;
        for (RubricaAlumnoFila f : e.getRubrica()) {
            if (rAlumno > 15) {
                break;
            }
            if (!f.filaActiva()) {
                rAlumno++;
                continue;
            }
            set(sh, rAlumno, 1, f.getNombre());
            setNum(sh, rAlumno, 6, f.getC1());
            setNum(sh, rAlumno, 8, f.getC2());
            setNum(sh, rAlumno, 10, f.getC3());
            setNum(sh, rAlumno, 13, f.getC4());
            rAlumno++;
        }
    }

    private void llenarLista(Sheet sh, Evaluacion e) {
        if (sh == null) {
            return;
        }
        var p = e.getProductoIntegrador();
        var lc = e.getListaCotejo();
        set(sh, 1, 2, p.getProgramaEducativo());
        set(sh, 2, 2, nullToEmpty(e.getAsignatura()));
        set(sh, 2, 6, nullToEmpty(e.getGrupo()));
        set(sh, 3, 2, nullToEmpty(e.getProfesor()));
        set(sh, 4, 2, p.getPeriodo());
        set(sh, 4, 6, p.getNivelAtributo());
        set(sh, 5, 2, lc.getProductoIntegrador());
        set(sh, 6, 2, lc.getAtributoEgreso());
        set(sh, 7, 2, lc.getCriterioDesempeno());
        int r = 8;
        for (ListaCotejoItem it : lc.getIndicadores()) {
            if (r > 11) {
                break;
            }
            set(sh, r, 2, it.getDescripcion());
            set(sh, r, 3, it.isCumple() ? "X" : "");
            r++;
        }
        int rAl = 14;
        for (RubricaAlumnoFila f : e.getRubrica()) {
            if (!f.filaActiva()) {
                continue;
            }
            if (rAl > 17) {
                break;
            }
            set(sh, rAl, 0, f.getNombre());
            rAl++;
        }
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static void set(Sheet sh, int row0, int col0, String v) {
        Row row = sh.getRow(row0);
        if (row == null) {
            row = sh.createRow(row0);
        }
        Cell c = row.getCell(col0);
        if (c == null) {
            c = row.createCell(col0);
        }
        c.setCellValue(v != null ? v : "");
    }

    private static void setNum(Sheet sh, int row0, int col0, Double v) {
        Row row = sh.getRow(row0);
        if (row == null) {
            row = sh.createRow(row0);
        }
        Cell c = row.getCell(col0);
        if (c == null) {
            c = row.createCell(col0);
        }
        if (v == null) {
            c.setBlank();
        } else {
            c.setCellValue(v);
        }
    }
}
