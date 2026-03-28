package saeae.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import saeae.model.Evaluacion;
import saeae.model.EvaluacionEstadoUtil;
import saeae.model.RubricaAlumnoFila;

public class RubricaTablePanel extends JPanel {

    private static final String[] COLS = { "Alumno", "Matrícula", "C1", "C2", "C3", "C4", "Promedio" };

    private final DefaultTableModel model = new DefaultTableModel(COLS, 4) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column < 6;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
    };
    private final JTable table = new JTable(model);
    /** Evita recursión al escribir la columna Promedio (disparaba el mismo listener). */
    private boolean actualizandoPromedios;

    public RubricaTablePanel() {
        super(new BorderLayout(8, 8));
        setOpaque(true);
        setBackground(UiTheme.LIGHT_GRAY);
        table.setRowHeight(22);
        table.setFillsViewportHeight(true);
        for (int i = 0; i < 4; i++) {
            model.setValueAt("", i, 0);
            model.setValueAt("", i, 1);
            model.setValueAt("", i, 2);
            model.setValueAt("", i, 3);
            model.setValueAt("", i, 4);
            model.setValueAt("", i, 5);
            model.setValueAt("", i, 6);
        }
        table.getModel().addTableModelListener(e -> {
            if (actualizandoPromedios) {
                return;
            }
            recalcPromedios();
        });
        table.setDefaultRenderer(Object.class, new RubricaCellRenderer());
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);
        add(sp, BorderLayout.CENTER);
        JLabel rub = new JLabel("<html><b>Rúbrica</b> (máx. 4 alumnos; promedio mín. 7 en verde)</html>");
        rub.setFont(UiTheme.headingFont());
        rub.setForeground(UiTheme.NAVY);
        add(rub, BorderLayout.NORTH);
    }

    public void loadFrom(Evaluacion rubricaParent) {
        var rows = rubricaParent.getRubrica();
        for (int i = 0; i < 4; i++) {
            RubricaAlumnoFila f = rows.get(i);
            model.setValueAt(nullToEmpty(f.getNombre()), i, 0);
            model.setValueAt(nullToEmpty(f.getMatricula()), i, 1);
            model.setValueAt(f.getC1() != null ? String.valueOf(f.getC1()) : "", i, 2);
            model.setValueAt(f.getC2() != null ? String.valueOf(f.getC2()) : "", i, 3);
            model.setValueAt(f.getC3() != null ? String.valueOf(f.getC3()) : "", i, 4);
            model.setValueAt(f.getC4() != null ? String.valueOf(f.getC4()) : "", i, 5);
        }
        recalcPromedios();
    }

    public void readTo(Evaluacion rubricaParent) {
        var rows = rubricaParent.getRubrica();
        for (int i = 0; i < 4; i++) {
            RubricaAlumnoFila f = rows.get(i);
            f.setNombre(str(model.getValueAt(i, 0)));
            f.setMatricula(str(model.getValueAt(i, 1)));
            f.setC1(dbl(model.getValueAt(i, 2)));
            f.setC2(dbl(model.getValueAt(i, 3)));
            f.setC3(dbl(model.getValueAt(i, 4)));
            f.setC4(dbl(model.getValueAt(i, 5)));
        }
    }

    private void recalcPromedios() {
        actualizandoPromedios = true;
        try {
            for (int i = 0; i < 4; i++) {
                RubricaAlumnoFila tmp = new RubricaAlumnoFila();
                tmp.setC1(dbl(model.getValueAt(i, 2)));
                tmp.setC2(dbl(model.getValueAt(i, 3)));
                tmp.setC3(dbl(model.getValueAt(i, 4)));
                tmp.setC4(dbl(model.getValueAt(i, 5)));
                model.setValueAt(RubricaAlumnoFila.formatearPromedio(tmp.getPromedio()), i, 6);
            }
        } finally {
            actualizandoPromedios = false;
        }
        table.repaint();
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String str(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    private static Double dbl(Object o) {
        if (o == null || "".equals(o)) {
            return null;
        }
        if (o instanceof Number n) {
            return n.doubleValue();
        }
        try {
            return NumberFormat.getNumberInstance().parse(o.toString()).doubleValue();
        } catch (ParseException e) {
            try {
                return Double.parseDouble(o.toString().replace(',', '.'));
            } catch (NumberFormatException ex) {
                return null;
            }
        }
    }

    private class RubricaCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            JLabel lb = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lb.setHorizontalAlignment(column >= 2 ? SwingConstants.RIGHT : SwingConstants.LEFT);
            if (!isSelected && column == 6) {
                RubricaAlumnoFila t = new RubricaAlumnoFila();
                t.setC1(dbl(model.getValueAt(row, 2)));
                t.setC2(dbl(model.getValueAt(row, 3)));
                t.setC3(dbl(model.getValueAt(row, 4)));
                t.setC4(dbl(model.getValueAt(row, 5)));
                String name = str(model.getValueAt(row, 0));
                if (!name.isBlank() && t.getPromedio() > 0 && t.getPromedio() < EvaluacionEstadoUtil.umbralReprobado()) {
                    lb.setBackground(new Color(255, 220, 220));
                } else {
                    lb.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }
            } else {
                lb.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            }
            lb.setOpaque(true);
            return lb;
        }
    }
}
