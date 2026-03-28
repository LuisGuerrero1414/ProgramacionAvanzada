package saeae.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import saeae.model.Evaluacion;
import saeae.model.ListaCotejoItem;

public class ListaCotejoPanel extends JPanel {

    private final JTextField productoIntegrador = new JTextField(28);
    private final JTextField atributoEgreso = new JTextField(28);
    private final JTextField criterioDesempeno = new JTextField(28);
    private final List<JCheckBox> checks = new ArrayList<>();
    private final List<JTextField> texts = new ArrayList<>();

    public ListaCotejoPanel() {
        super(new GridBagLayout());
        setOpaque(true);
        setBackground(UiTheme.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JLabel titulo = new JLabel("<html><b>Lista de cotejo</b></html>");
        titulo.setFont(UiTheme.headingFont());
        titulo.setForeground(UiTheme.NAVY);
        add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Producto integrador"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(productoIntegrador, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Atributo de egreso"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(atributoEgreso, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Criterio de desempeño"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(criterioDesempeno, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Cumple"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(new JLabel("Indicador"), gbc);
        for (int i = 0; i < 4; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            JCheckBox cb = new JCheckBox();
            checks.add(cb);
            add(cb, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            JTextField tf = new JTextField(32);
            texts.add(tf);
            add(tf, gbc);
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
        }
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        JButton marcar = new JButton("Marcar todos");
        marcar.addActionListener(a -> checks.forEach(c -> c.setSelected(true)));
        add(marcar, gbc);
    }

    public void loadFrom(Evaluacion e) {
        var lc = e.getListaCotejo();
        productoIntegrador.setText(txt(lc.getProductoIntegrador()));
        atributoEgreso.setText(txt(lc.getAtributoEgreso()));
        criterioDesempeno.setText(txt(lc.getCriterioDesempeno()));
        List<ListaCotejoItem> ind = lc.getIndicadores();
        for (int i = 0; i < 4; i++) {
            ListaCotejoItem it = i < ind.size() ? ind.get(i) : new ListaCotejoItem("", false);
            checks.get(i).setSelected(it.isCumple());
            texts.get(i).setText(it.getDescripcion() != null ? it.getDescripcion() : "");
        }
    }

    public void readTo(Evaluacion e) {
        var lc = e.getListaCotejo();
        lc.setProductoIntegrador(productoIntegrador.getText().trim());
        lc.setAtributoEgreso(atributoEgreso.getText().trim());
        lc.setCriterioDesempeno(criterioDesempeno.getText().trim());
        e.getListaCotejo().getIndicadores().clear();
        for (int i = 0; i < 4; i++) {
            ListaCotejoItem it =
                    new ListaCotejoItem(texts.get(i).getText().trim(), checks.get(i).isSelected());
            e.getListaCotejo().getIndicadores().add(it);
        }
    }

    private static String txt(String s) {
        return s == null ? "" : s;
    }
}
