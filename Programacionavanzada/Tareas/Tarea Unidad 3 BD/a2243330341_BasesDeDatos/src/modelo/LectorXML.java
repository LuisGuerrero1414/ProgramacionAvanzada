package modelo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

public class LectorXML {
    public String motor, host, db, user, pass;
    /** Vacío si el XML no define instancia con nombre (instancia por defecto del servidor). */
    public String instancia = "";
    /** Puerto TCP; si está vacío se usa instanceName (requiere SQL Server Browser). */
    public String puerto = "";

    public void cargarConfiguracion() {
        try {
            File archivo = new File("configuracion.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);
            doc.getDocumentElement().normalize();

            this.motor = doc.getElementsByTagName("motor_activo").item(0).getTextContent().trim();

            Element perfil = (Element) doc.getElementsByTagName("db_perfil").item(0);
            
            this.host = perfil.getElementsByTagName("host").item(0).getTextContent().trim();
            if (perfil.getElementsByTagName("instancia").getLength() > 0) {
                this.instancia = perfil.getElementsByTagName("instancia").item(0).getTextContent().trim();
            } else {
                this.instancia = "";
            }
            if (perfil.getElementsByTagName("puerto").getLength() > 0) {
                this.puerto = perfil.getElementsByTagName("puerto").item(0).getTextContent().trim();
            } else {
                this.puerto = "";
            }
            this.db   = perfil.getElementsByTagName("nombre_bd").item(0).getTextContent().trim();
            this.user = perfil.getElementsByTagName("usuario").item(0).getTextContent().trim();
            this.pass = perfil.getElementsByTagName("password").item(0).getTextContent().trim();
            
        } catch (Exception e) {
            System.out.println("Error al leer el nuevo formato XML: " + e.getMessage());
        }
    }
}