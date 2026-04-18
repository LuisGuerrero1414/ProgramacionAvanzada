package modelo;

/**
 * Fábrica de conexión a partir de la configuración XML; delega el acceso a datos en {@link BaseDatos}.
 * <p>
 * Si {@code puerto} está definido en el XML, la URL usa {@code host:puerto} (TCP directo, sin SQL Browser).
 * Si no, usa {@code host} y {@code instanceName} (sin {@code host\instancia} en la URL).
 */
public class Conexion extends BaseDatos {

    public Conexion(LectorXML config) {
        super(construirUrlJdbc(config), config.user, config.pass);
    }

    private static String construirUrlJdbc(LectorXML config) {
        String puerto = config.puerto != null ? config.puerto.trim() : "";
        StringBuilder url = new StringBuilder("jdbc:sqlserver://");

        if (!puerto.isEmpty()) {
            url.append(config.host).append(":").append(puerto);
        } else {
            url.append(config.host);
            if (config.instancia != null && !config.instancia.isEmpty()) {
                url.append(";instanceName=").append(config.instancia);
            }
        }

        url.append(";databaseName=").append(config.db)
                .append(";encrypt=false;trustServerCertificate=true;loginTimeout=30");
        return url.toString();
    }
}
