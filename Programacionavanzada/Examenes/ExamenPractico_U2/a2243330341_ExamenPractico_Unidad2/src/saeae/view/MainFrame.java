package saeae.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import saeae.controller.AppController;
import saeae.model.EstadoEvaluacion;

public class MainFrame extends JFrame {

    private final DashboardPanel dashboard = new DashboardPanel();
    private final InstrumentosPanel instrumentos = new InstrumentosPanel();
    private final SemaphorePanel semaphore = new SemaphorePanel();
    private final JLabel lblEstado = new JLabel(" ", SwingConstants.LEFT);

    public MainFrame() {
        setTitle("SAE-AE — Sistema Automatizado de Evaluación");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(UiTheme.LIGHT_GRAY);
        setLayout(new BorderLayout(8, 8));
        add(dashboard, BorderLayout.NORTH);
        add(instrumentos, BorderLayout.CENTER);
        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        status.setBackground(UiTheme.LIGHT_GRAY);
        status.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xb0bec5)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JLabel lblEstatusTitulo = new JLabel("Estatus:");
        lblEstatusTitulo.setFont(UiTheme.uiFont().deriveFont(Font.BOLD));
        lblEstatusTitulo.setForeground(UiTheme.NAVY);
        status.add(lblEstatusTitulo);
        status.add(semaphore);
        lblEstado.setForeground(UiTheme.NAVY);
        status.add(lblEstado);
        add(status, BorderLayout.SOUTH);
        setSize(980, 720);
        setLocationRelativeTo(null);
    }

    public void bind(AppController c) {
        c.wire(this);
    }

    public DashboardPanel getDashboard() {
        return dashboard;
    }

    public InstrumentosPanel getInstrumentos() {
        return instrumentos;
    }

    public void mostrarEstado(EstadoEvaluacion e) {
        EstadoEvaluacion st = e != null ? e : EstadoEvaluacion.SIN_INICIAR;
        semaphore.setEstado(st);
        lblEstado.setText(switch (st) {
        case SIN_INICIAR -> "Sin iniciar";
        case INCOMPLETO -> "Incompleto";
        case COMPLETO -> "Completo";
        });
        lblEstado.setForeground(st == EstadoEvaluacion.COMPLETO ? new Color(0, 120, 40)
                : st == EstadoEvaluacion.INCOMPLETO ? new Color(180, 100, 0) : Color.DARK_GRAY);
    }
}
