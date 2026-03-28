package saeae.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import saeae.model.Evaluacion;

public class InstrumentosPanel extends JPanel {

    private final ProductoIntegradorPanel producto = new ProductoIntegradorPanel();
    private final RubricaTablePanel rubrica = new RubricaTablePanel();
    private final ListaCotejoPanel lista = new ListaCotejoPanel();
    private final JTabbedPane tabs = new JTabbedPane();

    public InstrumentosPanel() {
        super(new BorderLayout(8, 8));
        setOpaque(true);
        setBackground(UiTheme.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(4, 10, 10, 10));
        tabs.setOpaque(true);
        tabs.setBackground(UiTheme.LIGHT_GRAY);
        tabs.addTab("Producto integrador", producto);
        tabs.addTab("Rúbrica (atributos)", rubrica);
        tabs.addTab("Lista de cotejo", lista);
        add(tabs, BorderLayout.CENTER);
    }

    public void loadFrom(Evaluacion e) {
        producto.loadFrom(e);
        rubrica.loadFrom(e);
        lista.loadFrom(e);
    }

    public void readTo(Evaluacion e) {
        producto.readTo(e);
        rubrica.readTo(e);
        lista.readTo(e);
    }

    public ProductoIntegradorPanel getProducto() {
        return producto;
    }

    public RubricaTablePanel getRubrica() {
        return rubrica;
    }

    public ListaCotejoPanel getLista() {
        return lista;
    }
}
