package saeae.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import saeae.model.EstadoEvaluacion;

public class SemaphorePanel extends JPanel {
    private EstadoEvaluacion estado = EstadoEvaluacion.SIN_INICIAR;

    public SemaphorePanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(72, 24));
    }

    public void setEstado(EstadoEvaluacion e) {
        this.estado = e != null ? e : EstadoEvaluacion.SIN_INICIAR;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int d = 18;
        int y = (getHeight() - d) / 2;
        drawLamp(g2, 4, y, d, estado == EstadoEvaluacion.SIN_INICIAR ? Color.RED.darker() : Color.LIGHT_GRAY);
        drawLamp(g2, 4 + d + 6, y, d, estado == EstadoEvaluacion.INCOMPLETO ? Color.ORANGE : Color.LIGHT_GRAY);
        drawLamp(g2, 4 + 2 * (d + 6), y, d, estado == EstadoEvaluacion.COMPLETO ? new Color(0, 130, 60) : Color.LIGHT_GRAY);
        g2.dispose();
    }

    private static void drawLamp(Graphics2D g2, int x, int y, int d, Color c) {
        g2.setColor(c);
        g2.fillOval(x, y, d, d);
        g2.setColor(Color.DARK_GRAY);
        g2.drawOval(x, y, d, d);
    }
}
