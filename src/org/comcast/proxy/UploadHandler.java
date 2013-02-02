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
public class UploadHandler implements InvocationHandler {
    private ResourceBundle uploadHandler_es_ES;

    private InterfaceWorks works;

    public UploadHandler(InterfaceWorks w) {
        works = w;
        
        try{
            Client c = LoaderProvider.getInstance().getClientConfiguration();
            
            switch(c.getLocalization()){
                case "Español":
                    uploadHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/UploadHandler_es_ES");
                    break;
                case "Ingles":
                    uploadHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/UploadHandler_en_US");
                    break;
                default:
                    uploadHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/UploadHandler_en_US");
                    break;
            }
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        try {
            if (method.getName().equalsIgnoreCase("transferFiles")) {
                return method.invoke(works, args);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException w) {
            w.printStackTrace();
            return null;
        }

        throw new IllegalAccessException(uploadHandler_es_ES.getString("NO TIENES PERMISOS PARA LA EJECUCION."));
    }
}
