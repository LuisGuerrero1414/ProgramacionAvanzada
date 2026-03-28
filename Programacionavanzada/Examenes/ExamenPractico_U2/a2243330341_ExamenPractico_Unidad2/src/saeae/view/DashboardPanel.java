package saeae.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class DashboardPanel extends JPanel {

    private final DefaultListModel<String> profesoresModel = new DefaultListModel<>();
    private final JList<String> listaProfesores = new JList<>(profesoresModel);
    private final JComboBox<String> comboMateria = new JComboBox<>();
    private final JComboBox<String> comboGrupo = new JComboBox<>();
    private final JButton btnCargar = new JButton("Cargar");
    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnCarpetaReportes = new JButton("Carpeta reportes…");
    private final JButton btnSugerirAlumnos = new JButton("Sugerir alumnos");
    private final JLabel lblCarpeta = new JLabel(" ");

    public DashboardPanel() {
        super(new BorderLayout(0, 0));
        setOpaque(true);
        setBackground(UiTheme.LIGHT_GRAY);

        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        encabezado.setOpaque(true);
        encabezado.setBackground(UiTheme.NAVY);
        JLabel titulo = new JLabel("Panel de control");
        titulo.setFont(UiTheme.headingFont());
        titulo.setForeground(UiTheme.HEADER_ON_NAVY);
        encabezado.add(titulo);
        add(encabezado, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(8, 8));
        cuerpo.setOpaque(false);

        JPanel subtitulos = new JPanel(new GridBagLayout());
        subtitulos.setOpaque(true);
        subtitulos.setBackground(new Color(0xdde1e6));
        GridBagConstraints h = new GridBagConstraints();
        h.insets = new Insets(6, 8, 6, 8);
        h.anchor = GridBagConstraints.WEST;
        h.gridx = 0;
        h.weightx = 0.35;
        JLabel lp = new JLabel("Profesores (eje)");
        lp.setFont(UiTheme.uiFont().deriveFont(Font.BOLD));
        lp.setForeground(UiTheme.NAVY);
        subtitulos.add(lp, h);
        h.gridx = 1;
        h.weightx = 0.4;
        JLabel lm = new JLabel("Asignatura");
        lm.setFont(UiTheme.uiFont().deriveFont(Font.BOLD));
        lm.setForeground(UiTheme.NAVY);
        subtitulos.add(lm, h);
        h.gridx = 2;
        h.weightx = 0.25;
        JLabel lg = new JLabel("Grupo");
        lg.setFont(UiTheme.uiFont().deriveFont(Font.BOLD));
        lg.setForeground(UiTheme.NAVY);
        subtitulos.add(lg, h);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 3;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        centro.add(subtitulos, g);
        g.gridy = 1;
        g.fill = GridBagConstraints.BOTH;
        g.weighty = 1;
        g.insets = new Insets(6, 8, 6, 8);
        g.gridwidth = 1;
        g.gridx = 0;
        g.weightx = 0.35;
        listaProfesores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProfesores.setVisibleRowCount(6);
        JScrollPane spLista = new JScrollPane(listaProfesores);
        spLista.getViewport().setBackground(Color.WHITE);
        centro.add(spLista, g);
        g.gridx = 1;
        g.weightx = 0.4;
        centro.add(comboMateria, g);
        g.gridx = 2;
        g.weightx = 0.25;
        centro.add(comboGrupo, g);
        cuerpo.add(centro, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(4, 4));
        sur.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        actions.setOpaque(false);
        actions.add(btnCargar);
        actions.add(btnNuevo);
        actions.add(btnGuardar);
        actions.add(btnEliminar);
        actions.add(btnSugerirAlumnos);
        actions.add(btnCarpetaReportes);
        btnGuardar.putClientProperty("JButton.buttonType", "default");
        sur.add(actions, BorderLayout.NORTH);
        lblCarpeta.setForeground(new Color(0x37474f));
        lblCarpeta.setBorder(BorderFactory.createEmptyBorder(0, 8, 6, 8));
        sur.add(lblCarpeta, BorderLayout.SOUTH);
        cuerpo.add(sur, BorderLayout.SOUTH);

        add(cuerpo, BorderLayout.CENTER);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xb0bec5)),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
    }

    public DefaultListModel<String> getProfesoresModel() {
        return profesoresModel;
    }

    public JList<String> getListaProfesores() {
        return listaProfesores;
    }

    public JComboBox<String> getComboMateria() {
        return comboMateria;
    }

    public JComboBox<String> getComboGrupo() {
        return comboGrupo;
    }

    public JButton getBtnCargar() {
        return btnCargar;
    }

    public JButton getBtnNuevo() {
        return btnNuevo;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnCarpetaReportes() {
        return btnCarpetaReportes;
    }

    public JButton getBtnSugerirAlumnos() {
        return btnSugerirAlumnos;
    }

    public void setTextoCarpeta(String s) {
        lblCarpeta.setText(s != null ? s : " ");
    }
}
