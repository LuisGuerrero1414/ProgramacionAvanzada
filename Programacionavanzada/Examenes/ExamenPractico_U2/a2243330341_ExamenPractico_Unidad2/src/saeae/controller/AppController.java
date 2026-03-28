package saeae.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import saeae.model.CatalogoDatos;
import saeae.model.Evaluacion;
import saeae.model.EvaluacionEstadoUtil;
import saeae.model.RegistroAlumno;
import saeae.model.RubricaAlumnoFila;
import saeae.view.DashboardPanel;
import saeae.view.MainFrame;

public class AppController {

    private final MainFrame frame;
    private final Path plantillaExamen;
    private final CatalogoService catalogoService;
    private final EvaluacionCrudService crudService;
    private ReporteExcelService reporteService;

    private CatalogoDatos catalogo;
    private Evaluacion draft = new Evaluacion();
    private String idEdicion;

    private Path carpetaReportes;

    public AppController(MainFrame frame, Path baseDir) {
        this.frame = frame;
        Path datosbase = baseDir.resolve("Datosbase.xlsx");
        this.plantillaExamen = baseDir.resolve("examen.xlsx");
        Path dataDir = baseDir.resolve("data");
        this.catalogoService = new CatalogoService(datosbase);
        this.crudService = new EvaluacionCrudService(dataDir.resolve("evaluaciones.json"));
        this.carpetaReportes = baseDir.resolve("reportes");
        this.reporteService = new ReporteExcelService(plantillaExamen, carpetaReportes);
        try {
            Files.createDirectories(dataDir);
            Files.createDirectories(carpetaReportes);
        } catch (IOException ignored) {
        }
        try {
            this.catalogo = catalogoService.cargar();
        } catch (IOException e) {
            this.catalogo = new CatalogoDatos();
            JOptionPane.showMessageDialog(this.frame,
                    "No se pudo leer Datosbase.xlsx: " + e.getMessage(),
                    "Catálogo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void wire(MainFrame f) {
        DashboardPanel d = f.getDashboard();
        d.setTextoCarpeta("Reportes: " + carpetaReportes.toAbsolutePath());
        for (String p : catalogo.getProfesoresOrdenados()) {
            d.getProfesoresModel().addElement(p);
        }
        d.getListaProfesores().addListSelectionListener(this::onProfesor);
        d.getComboMateria().addActionListener(e -> onMateria());
        d.getComboGrupo().addActionListener(e -> actualizarEstadoVista());
        d.getBtnCargar().addActionListener(e -> cargar());
        d.getBtnNuevo().addActionListener(e -> nuevo());
        d.getBtnGuardar().addActionListener(e -> guardar());
        d.getBtnEliminar().addActionListener(e -> eliminar());
        d.getBtnCarpetaReportes().addActionListener(e -> elegirCarpetaReportes());
        d.getBtnSugerirAlumnos().addActionListener(e -> sugerirAlumnos());
        draft.ensureStructure();
        idEdicion = null;
        refrescarVista();
    }

    private void onProfesor(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        DashboardPanel d = frame.getDashboard();
        String prof = d.getListaProfesores().getSelectedValue();
        d.getComboMateria().removeAllItems();
        d.getComboGrupo().removeAllItems();
        if (prof == null) {
            actualizarEstadoVista();
            return;
        }
        List<String> mats = catalogo.materiasDe(prof);
        for (String m : mats) {
            d.getComboMateria().addItem(m);
        }
        if (!mats.isEmpty()) {
            d.getComboMateria().setSelectedIndex(0);
        }
        onMateria();
    }

    private void onMateria() {
        DashboardPanel d = frame.getDashboard();
        String prof = d.getListaProfesores().getSelectedValue();
        Object mo = d.getComboMateria().getSelectedItem();
        d.getComboGrupo().removeAllItems();
        if (prof == null || mo == null) {
            actualizarEstadoVista();
            return;
        }
        String mat = mo.toString();
        for (String g : catalogo.gruposDe(prof, mat)) {
            d.getComboGrupo().addItem(g);
        }
        if (d.getComboGrupo().getItemCount() > 0) {
            d.getComboGrupo().setSelectedIndex(0);
        }
        actualizarEstadoVista();
    }

    private void cargar() {
        Optional<String[]> sel = seleccion();
        if (sel.isEmpty()) {
            return;
        }
        String[] s = sel.get();
        try {
            Optional<Evaluacion> found = crudService.buscarPorTerna(s[0], s[1], s[2]);
            if (found.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No hay evaluación guardada para esta terna.", "Cargar",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            draft = found.get();
            draft.ensureStructure();
            idEdicion = draft.getId();
            refrescarVista();
            JOptionPane.showMessageDialog(frame, "Evaluación cargada desde JSON.", "Cargar",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nuevo() {
        Optional<String[]> sel = seleccion();
        draft = new Evaluacion();
        draft.ensureStructure();
        idEdicion = null;
        if (sel.isPresent()) {
            draft.setAsignatura(sel.get()[0]);
            draft.setProfesor(sel.get()[1]);
            draft.setGrupo(sel.get()[2]);
            draft.getProductoIntegrador().setProgramaEducativo("Ing. Sistemas Computacionales");
            draft.getProductoIntegrador().setAsignatura(sel.get()[0]);
            draft.getProductoIntegrador().setDocente(sel.get()[1]);
            draft.getProductoIntegrador().setGrupoLetra(sel.get()[2]);
            draft.getListaCotejo()
                    .setProductoIntegrador(draft.getProductoIntegrador().getActividad());
            draft.getListaCotejo().setAtributoEgreso(draft.getProductoIntegrador().getAtributoEgreso());
            draft.getListaCotejo().setCriterioDesempeno(draft.getProductoIntegrador().getCriterioDesempeno());
        }
        refrescarVista();
    }

    private void guardar() {
        leerDraftDesdeVista();
        draft.ensureStructure();
        EvaluacionEstadoUtil.aplicarEstado(draft);
        if (draft.getAsignatura() == null || draft.getAsignatura().isBlank()
                || draft.getProfesor() == null || draft.getProfesor().isBlank()
                || draft.getGrupo() == null || draft.getGrupo().isBlank()) {
            JOptionPane.showMessageDialog(frame, "Seleccione asignatura, profesor y grupo en el panel de control.",
                    "Guardar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idPrevio = idEdicion;
        try {
            crudService.guardar(draft, idPrevio);
            reporteService.exportarOActualizar(draft);
            idEdicion = draft.getId();
            EvaluacionEstadoUtil.aplicarEstado(draft);
            refrescarVista();
            Path rutaJson = crudService.getArchivoJson();
            Path rutaXlsx = reporteService.rutaArchivo(draft);
            String nombreXlsx = rutaXlsx.getFileName().toString();
            String mensajeExito = """
                    <html><body style='width: 400px; font-family: sans-serif;'>
                    <p><b>Operación exitosa</b></p>
                    <p>Se actualizó la base de datos JSON y se generó el reporte Excel.</p>
                    <p><b>Archivo Excel</b><br><tt>%s</tt></p>
                    <p><small>Ruta completa:<br><tt>%s</tt></small></p>
                    <p><small>JSON:<br><tt>%s</tt></small></p>
                    </body></html>"""
                    .formatted(escapeHtml(nombreXlsx), escapeHtml(rutaXlsx.toAbsolutePath().toString()),
                            escapeHtml(rutaJson.toAbsolutePath().toString()));
            JOptionPane.showMessageDialog(frame, mensajeExito, "Guardar — éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Duplicado", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idEdicion == null || idEdicion.isBlank()) {
            JOptionPane.showMessageDialog(frame, "Nada seleccionado para eliminar.", "Eliminar",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int ok = JOptionPane.showConfirmDialog(frame,
                "¿Eliminar la evaluación actual del JSON y su reporte Excel asociado?",
                "Confirmar",
                JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Path xlsx = reporteService.rutaArchivo(draft);
            crudService.eliminar(idEdicion);
            Files.deleteIfExists(xlsx);
            JOptionPane.showMessageDialog(frame, "Registro eliminado.", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
            nuevo();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void elegirCarpetaReportes() {
        JFileChooser ch = new JFileChooser(carpetaReportes.toFile());
        ch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (ch.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            carpetaReportes = ch.getSelectedFile().toPath();
            reporteService = new ReporteExcelService(plantillaExamen, carpetaReportes);
            frame.getDashboard().setTextoCarpeta("Reportes: " + carpetaReportes.toAbsolutePath());
            JOptionPane.showMessageDialog(frame, "Carpeta de reportes actualizada.", "Reportes",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void sugerirAlumnos() {
        Optional<String[]> sel = seleccion();
        if (sel.isEmpty()) {
            return;
        }
        String[] s = sel.get();
        List<RegistroAlumno> al = catalogo.alumnosDe(s[1], s[0], s[2]);
        if (al.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Sin alumnos en el catálogo para esta terna.", "Sugerir",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        leerDraftDesdeVista();
        List<RubricaAlumnoFila> rub = draft.getRubrica();
        for (int i = 0; i < rub.size() && i < al.size(); i++) {
            RegistroAlumno ra = al.get(i);
            rub.get(i).setNombre(ra.getNombre());
            rub.get(i).setMatricula(ra.getMatricula());
        }
        refrescarVista();
        JOptionPane.showMessageDialog(frame, "Primeros alumnos del grupo copiados a la rúbrica.", "Sugerir",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private Optional<String[]> seleccion() {
        DashboardPanel d = frame.getDashboard();
        String prof = d.getListaProfesores().getSelectedValue();
        Object mo = d.getComboMateria().getSelectedItem();
        Object go = d.getComboGrupo().getSelectedItem();
        if (prof == null || mo == null || go == null) {
            JOptionPane.showMessageDialog(frame, "Seleccione profesor, asignatura y grupo.", "Selección",
                    JOptionPane.WARNING_MESSAGE);
            return Optional.empty();
        }
        return Optional.of(new String[] { mo.toString(), prof, go.toString() });
    }

    private void leerDraftDesdeVista() {
        frame.getInstrumentos().readTo(draft);
    }

    private void refrescarVista() {
        draft.ensureStructure();
        frame.getInstrumentos().loadFrom(draft);
        EvaluacionEstadoUtil.aplicarEstado(draft);
        frame.mostrarEstado(draft.getEstado());
    }

    private void actualizarEstadoVista() {
        leerDraftDesdeVista();
        EvaluacionEstadoUtil.aplicarEstado(draft);
        frame.mostrarEstado(draft.getEstado());
    }

    private static String escapeHtml(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
