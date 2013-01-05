/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.comcast.builder.Client;
import org.comcast.xml.LoaderProvider;

/**
 *
 * @author Quality of Service
 */
public class DownloadHandler implements InvocationHandler {
    private ResourceBundle downloadHandler_es_ES;

    private InterfaceWorks works;

    public DownloadHandler(InterfaceWorks w) {
        this.works = w;
        
        try{
            Client c = LoaderProvider.getInstance().getClientConfiguration();
            
            switch(c.getLocalization()){
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
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
