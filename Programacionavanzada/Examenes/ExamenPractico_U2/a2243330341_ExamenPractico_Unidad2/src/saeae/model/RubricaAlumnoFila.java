package saeae.model;

import java.util.Locale;

public class RubricaAlumnoFila {
    private String nombre;
    private String matricula;
    private Double c1;
    private Double c2;
    private Double c3;
    private Double c4;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Double getC1() {
        return c1;
    }

    public void setC1(Double c1) {
        this.c1 = c1;
    }

    public Double getC2() {
        return c2;
    }

    public void setC2(Double c2) {
        this.c2 = c2;
    }

    public Double getC3() {
        return c3;
    }

    public void setC3(Double c3) {
        this.c3 = c3;
    }

    public Double getC4() {
        return c4;
    }

    public void setC4(Double c4) {
        this.c4 = c4;
    }

    public double getPromedio() {
        int n = 0;
        double sum = 0;
        for (Double v : new Double[] { c1, c2, c3, c4 }) {
            if (v != null) {
                sum += v;
                n++;
            }
        }
        if (n == 0) {
            return 0;
        }
        return sum / n;
    }

    public boolean filaActiva() {
        return nombre != null && !nombre.isBlank();
    }

    public boolean filaCompleta() {
        if (!filaActiva()) {
            return false;
        }
        return c1 != null && c2 != null && c3 != null && c4 != null;
    }

    public static String formatearPromedio(double p) {
        return String.format(Locale.US, "%.2f", p);
    }
}
