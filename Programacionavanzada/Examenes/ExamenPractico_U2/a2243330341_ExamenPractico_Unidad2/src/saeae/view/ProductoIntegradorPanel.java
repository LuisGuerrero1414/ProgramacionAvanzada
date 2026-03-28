package saeae.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import saeae.model.DetalleInstrumento;
import saeae.model.Evaluacion;
import saeae.model.ProductoIntegradorData;

public class ProductoIntegradorPanel extends JPanel {

    private final JTextField fecha = new JTextField(12);
    private final JTextArea criterios = new JTextArea(3, 40);
    private final JTextArea obsInstrumento = new JTextArea(2, 40);

    private final JTextField programa = new JTextField(28);
    private final JTextField asignatura = new JTextField(20);
    private final JTextField docente = new JTextField(20);
    private final JTextField grupo = new JTextField(8);
    private final JTextField periodo = new JTextField(12);
    private final JTextField actividad = new JTextField(12);
    private final JTextField nivelAtributo = new JTextField(12);
    private final JTextField atributoEgreso = new JTextField(24);
    private final JTextField criterioDesempeno = new JTextField(24);
    private final JTextField indicadores = new JTextField(24);
    private final JTextArea obsProducto = new JTextArea(3, 40);

    public ProductoIntegradorPanel() {
        super(new GridBagLayout());
        setOpaque(true);
        setBackground(UiTheme.LIGHT_GRAY);
        criterios.setLineWrap(true);
        criterios.setWrapStyleWord(true);
        obsInstrumento.setLineWrap(true);
        obsInstrumento.setWrapStyleWord(true);
        obsProducto.setLineWrap(true);
        obsProducto.setWrapStyleWord(true);
        programa.setEditable(false);
        asignatura.setEditable(false);
        docente.setEditable(false);
        grupo.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel secIns = new JLabel("Datos del instrumento");
        secIns.setFont(UiTheme.headingFont());
        secIns.setForeground(UiTheme.NAVY);
        add(secIns, gbc);
        gbc.gridy++;
        add(new JLabel("Fecha"), gbc);
        gbc.gridx = 1;
        add(fecha, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Criterios"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(wrapScroll(criterios), gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Observaciones (instrumento)"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(wrapScroll(obsInstrumento), gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        JLabel secProd = new JLabel("Producto integrador");
        secProd.setFont(UiTheme.headingFont());
        secProd.setForeground(UiTheme.NAVY);
        add(secProd, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Programa educativo"), gbc);
        gbc.gridx = 1;
        add(programa, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Asignatura"), gbc);
        gbc.gridx = 1;
        add(asignatura, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Docente"), gbc);
        gbc.gridx = 1;
        add(docente, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Grupo"), gbc);
        gbc.gridx = 1;
        add(grupo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Periodo"), gbc);
        gbc.gridx = 1;
        add(periodo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Actividad"), gbc);
        gbc.gridx = 1;
        add(actividad, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Nivel atributo"), gbc);
        gbc.gridx = 1;
        add(nivelAtributo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Atributo de egreso"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(atributoEgreso, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Criterio de desempeño"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(criterioDesempeno, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Indicadores"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(indicadores, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Observaciones (producto)"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(wrapScroll(obsProducto), gbc);
    }

    private static JScrollPane wrapScroll(JTextArea ta) {
        JScrollPane sp = new JScrollPane(ta);
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    public void loadFrom(Evaluacion e) {
        DetalleInstrumento d = e.getDetalleInstrumento();
        fecha.setText(txt(d.getFecha()));
        criterios.setText(txt(d.getCriterios()));
        obsInstrumento.setText(txt(d.getObservaciones()));
        ProductoIntegradorData p = e.getProductoIntegrador();
        programa.setText(txt(p.getProgramaEducativo()));
        asignatura.setText(txt(e.getAsignatura()));
        docente.setText(txt(e.getProfesor()));
        grupo.setText(txt(e.getGrupo()));
        periodo.setText(txt(p.getPeriodo()));
        actividad.setText(txt(p.getActividad()));
        nivelAtributo.setText(txt(p.getNivelAtributo()));
        atributoEgreso.setText(txt(p.getAtributoEgreso()));
        criterioDesempeno.setText(txt(p.getCriterioDesempeno()));
        indicadores.setText(txt(p.getIndicadores()));
        obsProducto.setText(txt(p.getObservaciones()));
    }

    public void readTo(Evaluacion e) {
        DetalleInstrumento d = e.getDetalleInstrumento();
        d.setFecha(fecha.getText().trim());
        d.setCriterios(criterios.getText().trim());
        d.setObservaciones(obsInstrumento.getText().trim());
        ProductoIntegradorData p = e.getProductoIntegrador();
        p.setProgramaEducativo(programa.getText().trim());
        p.setAsignatura(asignatura.getText().trim());
        p.setDocente(docente.getText().trim());
        p.setGrupoLetra(grupo.getText().trim());
        p.setPeriodo(periodo.getText().trim());
        p.setActividad(actividad.getText().trim());
        p.setNivelAtributo(nivelAtributo.getText().trim());
        p.setAtributoEgreso(atributoEgreso.getText().trim());
        p.setCriterioDesempeno(criterioDesempeno.getText().trim());
        p.setIndicadores(indicadores.getText().trim());
        p.setObservaciones(obsProducto.getText().trim());
        e.setAsignatura(asignatura.getText().trim());
        e.setProfesor(docente.getText().trim());
        e.setGrupo(grupo.getText().trim());
    }

    private static String txt(String s) {
        return s == null ? "" : s;
    }
}
