package org.comcast.proxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.comcast.builder.Client;
import org.comcast.exceptions.InformationRequiredException;
import org.comcast.xml.LoaderProvider;
import org.xml.sax.SAXException;

/**
 * Clase que utiliza el proxy para realizar la tarea de bajar archivos del
 * servidor ftp.<br> Esta clase es parte del proxy de seguridad implementado. Es
 * decir esta clase impide que se ejecuten otros metodos cuya funcionalidad es
 * distinta a la de bajar archivos.
 *
 * @author Damian Bruera
 * @version 1.0
 * @since Java 7
 */
public class DownloadHandler implements InvocationHandler {

    private ResourceBundle downloadHandler_es_ES;
    private InterfaceWorks works;

    /**
     * Constructor de la clase.
     *
     * @param w Es el trabajo a realizar.
     */
    public DownloadHandler(InterfaceWorks w) {
        this.works = w;

        try {
            Client c = LoaderProvider.getInstance().getClientConfiguration();

            switch (c.getLocalization()) {
                case "Español":
                    downloadHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/DownloadHandler_es_ES");
                    break;
                case "Ingles":
                    downloadHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/DownloadHandler_en_US");
                    break;
                default:
                    downloadHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/DownloadHandler_en_US");
                    break;
            }

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException | URISyntaxException | InformationRequiredException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Este metodo tiene como objetivo controlar que solamente se llame el
     * metodo para bajar archivos. <br> Si se llama a otro metodo se produce una
     * excepcion de acceso no autorizado.
     *
     * @param proxy Proxy implementado.
     * @param method Metodo que se quiere llamar.
     * @param args Argumentos de ese metodo.
     * @return Objeto retornado por el metodo.
     * @throws Throwable Si algo falla.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getName().equalsIgnoreCase("downloadFiles")) {
                return method.invoke(works, args);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException w) {
            return null;
        }

        throw new IllegalAccessException(downloadHandler_es_ES.getString("NO TIENES PERMISOS PARA LA EJECUCION. DESCARGAR ARCHIVO"));
    }
}
