package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import controller.AppController;
import controller.CatalogoController;
import model.Academia;
import model.Asignatura;
import model.IndicadorCedula;
import model.IndicadorGrupo;
import persistence.ExportadorExcel;

/**
 * Ventana principal: carga CSV, tablas de indicadores y acceso a catálogos.
 */
public class VentanaPrincipal extends JFrame {

    private static final DecimalFormat FMT = new DecimalFormat("#0.00");

    private final AppController appController;
    private final CatalogoController catalogoController;

    private final JTextField campoArchivo = new JTextField(40);
    private final JTable tablaGrupos = new JTable();
    private final JTable tablaCedula = new JTable();

    private List<IndicadorGrupo> ultimosGrupos = List.of();
    private List<IndicadorCedula> ultimaCedula = List.of();

    public VentanaPrincipal(AppController appController, CatalogoController catalogoController) {
        super("Sistema de análisis de calificaciones — Cédula 3.3.2");
        this.appController = appController;
        this.catalogoController = catalogoController;
        campoArchivo.setEditable(false);

        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 640);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Carga y promedios por grupo", crearPanelGrupos());
        tabs.addTab("Cédula 3.3.2", crearPanelCedula());
        tabs.addTab("Catálogos", crearPanelCatalogos());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearPanelGrupos() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnArchivo = new JButton("Seleccionar archivo CSV...");
        btnArchivo.addActionListener(e -> seleccionarCsv());
        JButton btnCalcular = new JButton("Calcular Promedios");
        btnCalcular.addActionListener(e -> calcularPromedios());
        JButton btnExportar = new JButton("Exportar a Excel...");
        btnExportar.addActionListener(e -> exportarExcel());

        superior.add(btnArchivo);
        superior.add(new JLabel("Archivo:"));
        superior.add(campoArchivo);
        superior.add(btnCalcular);
        superior.add(btnExportar);

        p.add(superior, BorderLayout.NORTH);
        tablaGrupos.setAutoCreateRowSorter(true);
        p.add(new JScrollPane(tablaGrupos), BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelCedula() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btn = new JButton("Generar Datos Cédula 3.3.2");
        btn.addActionListener(e -> generarCedula());
        norte.add(btn);
        p.add(norte, BorderLayout.NORTH);
        tablaCedula.setAutoCreateRowSorter(true);
        p.add(new JScrollPane(tablaCedula), BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelCatalogos() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JButton b1 = new JButton("Gestionar Academias");
        b1.addActionListener(e -> new DialogoAcademias(this, catalogoController).setVisible(true));
        JButton b2 = new JButton("Gestionar Asignaturas");
        b2.addActionListener(e -> new DialogoAsignaturas(this, catalogoController).setVisible(true));
        p.add(b1);
        p.add(b2);
        return p;
    }

    private void seleccionarCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        int r = fc.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path path = fc.getSelectedFile().toPath();
        campoArchivo.setText(path.toString());
        appController.establecerRutaCsv(path);
        try {
            appController.cargarCsv();
            JOptionPane.showMessageDialog(this,
                    "Archivo cargado: " + appController.getRegistrosEnMemoria().size() + " registros.",
                    "Carga correcta", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el CSV: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularPromedios() {
        if (appController.getRutaCsv() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un archivo CSV.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        ultimosGrupos = appController.calcularPromediosPorGrupo();
        DefaultTableModel m = new DefaultTableModel(
                new Object[] { "Profesor", "Asignatura", "Grupo", "Promedio", "Num. Alumnos",
                        "Num. Reprobados", "% Aprobados", "% Reprobados", "Promedio Acreditados" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (IndicadorGrupo g : ultimosGrupos) {
            m.addRow(new Object[] {
                    g.getProfesor(),
                    g.getAsignatura(),
                    g.getGrupo(),
                    FMT.format(g.getPromedio()),
                    g.getNumAlumnos(),
                    g.getNumReprobados(),
                    FMT.format(g.getPorcentajeAprobados()),
                    FMT.format(g.getPorcentajeReprobados()),
                    FMT.format(g.getPromedioAcreditados())
            });
        }
        tablaGrupos.setModel(m);
    }

    private void generarCedula() {
        if (appController.getRutaCsv() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione y cargue primero un archivo CSV.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Academia> academias;
        List<Asignatura> asignaturas;
        try {
            academias = catalogoController.listarAcademias();
            asignaturas = catalogoController.listarAsignaturas();
            if (academias.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay academias registradas.\n\n"
                                + "Para generar la Cédula 3.3.2:\n"
                                + "1) Ve a la pestaña Catálogos.\n"
                                + "2) Crea al menos una academia.\n"
                                + "3) Registra asignaturas asociadas a esa academia.",
                        "Catálogos incompletos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (asignaturas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay asignaturas registradas.\n\n"
                                + "Para generar la Cédula 3.3.2:\n"
                                + "1) Ve a la pestaña Catálogos.\n"
                                + "2) Crea asignaturas ligadas a una academia.\n"
                                + "3) Usa el nombre completo de MATERIA o su código (ej. (G.XX00.000)).",
                        "Catálogos incompletos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ultimaCedula = appController.generarCedula332();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer catálogos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel m = new DefaultTableModel(
                new Object[] { "Academia", "Asignatura", "Número de Grupos", "Promedio",
                        "% Mayor al Promedio", "% de Reprobación", "Profesores" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (IndicadorCedula c : ultimaCedula) {
            m.addRow(new Object[] {
                    c.getAcademia(),
                    c.getAsignatura(),
                    c.getNumeroGrupos(),
                    FMT.format(c.getPromedio()),
                    FMT.format(c.getPorcentajeMayorAlPromedio()),
                    FMT.format(c.getPorcentajeReprobacion()),
                    c.getProfesores()
            });
        }
        tablaCedula.setModel(m);
        if (ultimaCedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hubo coincidencias entre las asignaturas del catálogo y la columna MATERIA del CSV.\n\n"
                            + "Asignaturas registradas: " + asignaturas.size() + "\n"
                            + "Sugerencia: usa el nombre completo de MATERIA o el código entre paréntesis.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportarExcel() {
        if (ultimosGrupos.isEmpty() && ultimaCedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Calcule primero los promedios o genere la cédula antes de exportar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("reporte_calificaciones.xlsx"));
        fc.setFileFilter(new FileNameExtensionFilter("Excel", "xlsx"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path destino = fc.getSelectedFile().toPath();
        String nombre = destino.toString();
        if (!nombre.toLowerCase().endsWith(".xlsx")) {
            destino = Path.of(nombre + ".xlsx");
        }
        try {
            long bytes = ExportadorExcel.exportar(destino, ultimosGrupos, ultimaCedula);
            JOptionPane.showMessageDialog(this,
                    "Guardado en:\n" + destino.toAbsolutePath() + "\nTamaño: " + bytes + " bytes.",
                    "Exportación", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al exportar.\n"
                            + "Posible causa: dependencia faltante de Apache POI en tiempo de ejecución.\n\n"
                            + ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                    "Error de ejecución", JOptionPane.ERROR_MESSAGE);
        } catch (Error ex) {
            JOptionPane.showMessageDialog(this,
                    "Error crítico al exportar.\n"
                            + "Posible causa: dependencia faltante en runtime.\n\n"
                            + ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                    "Error de ejecución", JOptionPane.ERROR_MESSAGE);
        }
    }

}
