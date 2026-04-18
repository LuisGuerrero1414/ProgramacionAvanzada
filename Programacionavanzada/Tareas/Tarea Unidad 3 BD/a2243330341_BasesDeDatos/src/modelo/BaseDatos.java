package modelo;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * Capa de acceso a datos JDBC con consultas parametrizadas y mapeo reflexivo a POJOs.
 */
public class BaseDatos {

    private final String url;
    private final String user;
    private final String pass;

    public BaseDatos(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    /**
     * Ejecuta un SELECT con {@code condicion} usando marcadores {@code ?}; los valores van en {@code parametros}.
     *
     * @param condicion fragmento SQL para WHERE, p. ej. {@code "CARRERA = ? AND PLAN = ?"} (sin la palabra WHERE)
     */
    public ArrayList<Object[]> consultar(String tabla, String campos, String condicion, Object[] parametros) {
        ArrayList<Object[]> datos = new ArrayList<>();

        String select = (campos == null || campos.isEmpty()) ? "*" : campos;
        StringBuilder sql = new StringBuilder("SELECT ").append(select).append(" FROM ").append(tabla);

        if (condicion != null && !condicion.isEmpty()) {
            sql.append(" WHERE ").append(condicion);
        }

        Connection cnRaw = conectar();
        if (!conexionValida(cnRaw)) {
            return datos;
        }
        try (Connection cn = cnRaw;
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            establecerParametros(ps, parametros);

            try (ResultSet rs = ps.executeQuery()) {
                int numColumnas = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] fila = new Object[numColumnas];
                    for (int i = 0; i < numColumnas; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    datos.add(fila);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
        return datos;
    }

    /**
     * SELECT con ordenación. La condición usa {@code ?}; el fragmento ORDER BY debe ser fiable (no concatenar datos de usuario sin validar).
     */
    public ArrayList<Object[]> consultarOrdenado(String tabla, String campos, String condicion, Object[] parametros,
            String orden) {
        ArrayList<Object[]> datos = new ArrayList<>();

        String select = (campos == null || campos.isEmpty()) ? "*" : campos;
        StringBuilder sql = new StringBuilder("SELECT ").append(select).append(" FROM ").append(tabla);

        if (condicion != null && !condicion.isEmpty()) {
            sql.append(" WHERE ").append(condicion);
        }
        if (orden != null && !orden.isEmpty()) {
            if (!esFragmentoOrdenSeguro(orden)) {
                System.out.println("Error: fragmento ORDER BY rechazado por validación de seguridad.");
                return datos;
            }
            sql.append(" ORDER BY ").append(orden);
        }

        Connection cnRaw = conectar();
        if (!conexionValida(cnRaw)) {
            return datos;
        }
        try (Connection cn = cnRaw;
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            establecerParametros(ps, parametros);

            try (ResultSet rs = ps.executeQuery()) {
                int cols = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] fila = new Object[cols];
                    for (int i = 0; i < cols; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    datos.add(fila);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en consulta ordenada: " + e.getMessage());
        }
        return datos;
    }

    /**
     * Ejecuta una sentencia SQL (sin parámetros) y mapea cada fila al tipo indicado usando nombres de columna y reflexión.
     */
    public <T> ArrayList<T> consultarAObjeto(String sql, Class<T> clase) {
        return consultarAObjeto(sql, null, clase);
    }

    /**
     * Igual que {@link #consultarAObjeto(String, Class)} pero enlaza {@code ?} con {@code parametros}.
     */
    public <T> ArrayList<T> consultarAObjeto(String sql, Object[] parametros, Class<T> clase) {
        ArrayList<T> lista = new ArrayList<>();

        Connection cnRaw = conectar();
        if (!conexionValida(cnRaw)) {
            return lista;
        }
        try (Connection cn = cnRaw;
             PreparedStatement ps = cn.prepareStatement(sql)) {

            establecerParametros(ps, parametros);

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int columnas = md.getColumnCount();

                while (rs.next()) {
                    T objeto = clase.getDeclaredConstructor().newInstance();
                    for (int c = 1; c <= columnas; c++) {
                        String etiqueta = md.getColumnLabel(c);
                        Object valor = rs.getObject(c);
                        Field campo = buscarCampo(clase, etiqueta);
                        if (campo != null) {
                            campo.setAccessible(true);
                            campo.set(objeto, convertirValor(valor, campo.getType()));
                        }
                    }
                    lista.add(objeto);
                }
            }
        } catch (ReflectiveOperationException e) {
            System.out.println("Error de reflexión al mapear resultados: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error SQL al consultar objetos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * UPDATE dinámico: varias columnas desde el mapa; la condición usa {@code ?} y {@code parametrosCondicion}.
     * Las columnas y valores se enlazan por el mismo {@link Map.Entry} en el SET y en {@code setObject} (orden estable si el mapa lo garantiza, p. ej. {@link java.util.LinkedHashMap}).
     */
    public boolean modificar(String tabla, Map<String, Object> valores, String condicion,
            Object[] parametrosCondicion) {
        if (valores == null || valores.isEmpty()) {
            return false;
        }

        StringBuilder sql = new StringBuilder("UPDATE ").append(tabla).append(" SET ");
        int i = 0;
        for (Map.Entry<String, Object> entrada : valores.entrySet()) {
            sql.append("[").append(entrada.getKey()).append("] = ?");
            if (++i < valores.size()) {
                sql.append(", ");
            }
        }
        if (condicion != null && !condicion.isEmpty()) {
            sql.append(" WHERE ").append(condicion);
        }

        Connection cnRaw = conectar();
        if (!conexionValida(cnRaw)) {
            return false;
        }
        try (Connection cn = cnRaw;
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            int index = 1;
            for (Map.Entry<String, Object> entrada : valores.entrySet()) {
                ps.setObject(index++, entrada.getValue());
            }
            if (parametrosCondicion != null) {
                for (Object parametro : parametrosCondicion) {
                    ps.setObject(index++, parametro);
                }
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar: " + e.getMessage());
            return false;
        }
    }

    public Connection conectar() {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }

    private static boolean conexionValida(Connection cn) {
        if (cn == null) {
            System.out.println("Error: no hay conexión disponible.");
            return false;
        }
        return true;
    }

    private static void establecerParametros(PreparedStatement ps, Object[] parametros) throws SQLException {
        if (parametros == null) {
            return;
        }
        for (int i = 0; i < parametros.length; i++) {
            ps.setObject(i + 1, parametros[i]);
        }
    }

    /**
     * Evita patrones típicos de inyección en ORDER BY cuando el orden no puede parametrizarse.
     */
    private static boolean esFragmentoOrdenSeguro(String orden) {
        String t = orden.trim();
        if (t.isEmpty()) {
            return false;
        }
        return !t.contains(";") && !t.contains("--") && !t.contains("/*") && !t.contains("*/");
    }

    private static Field buscarCampo(Class<?> tipo, String nombreColumna) {
        String normalizado = normalizarNombreColumna(nombreColumna);
        Class<?> actual = tipo;
        while (actual != null && actual != Object.class) {
            for (Field f : actual.getDeclaredFields()) {
                if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                if (f.getName().equalsIgnoreCase(normalizado)) {
                    return f;
                }
            }
            actual = actual.getSuperclass();
        }
        return null;
    }

    private static String normalizarNombreColumna(String etiqueta) {
        if (etiqueta == null) {
            return "";
        }
        String s = etiqueta.trim();
        if (s.startsWith("[") && s.endsWith("]") && s.length() >= 2) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }

    private static Object convertirValor(Object valor, Class<?> tipoDestino) {
        if (valor == null) {
            if (tipoDestino.isPrimitive()) {
                return valorPorDefectoPrimitivo(tipoDestino);
            }
            return null;
        }
        if (tipoDestino.isInstance(valor)) {
            return valor;
        }
        if (tipoDestino == String.class) {
            return valor.toString();
        }
        if (tipoDestino == Integer.class || tipoDestino == int.class) {
            if (valor instanceof Number n) {
                return n.intValue();
            }
            return Integer.parseInt(valor.toString());
        }
        if (tipoDestino == Long.class || tipoDestino == long.class) {
            if (valor instanceof Number n) {
                return n.longValue();
            }
            return Long.parseLong(valor.toString());
        }
        if (tipoDestino == Double.class || tipoDestino == double.class) {
            if (valor instanceof Number n) {
                return n.doubleValue();
            }
            return Double.parseDouble(valor.toString());
        }
        if (tipoDestino == Float.class || tipoDestino == float.class) {
            if (valor instanceof Number n) {
                return n.floatValue();
            }
            return Float.parseFloat(valor.toString());
        }
        if (tipoDestino == Boolean.class || tipoDestino == boolean.class) {
            if (valor instanceof Boolean b) {
                return b;
            }
            return Boolean.parseBoolean(valor.toString());
        }
        if (tipoDestino == BigDecimal.class) {
            if (valor instanceof BigDecimal bd) {
                return bd;
            }
            return new BigDecimal(valor.toString());
        }
        if (tipoDestino == LocalDateTime.class && valor instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        }
        if (tipoDestino == LocalDate.class && valor instanceof Date d) {
            return d.toLocalDate();
        }
        return valor;
    }

    private static Object valorPorDefectoPrimitivo(Class<?> tipo) {
        if (tipo == int.class) {
            return 0;
        }
        if (tipo == long.class) {
            return 0L;
        }
        if (tipo == double.class) {
            return 0.0d;
        }
        if (tipo == float.class) {
            return 0.0f;
        }
        if (tipo == boolean.class) {
            return false;
        }
        if (tipo == char.class) {
            return '\0';
        }
        if (tipo == short.class) {
            return (short) 0;
        }
        if (tipo == byte.class) {
            return (byte) 0;
        }
        return null;
    }
}
