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
public class DecryptHandler implements InvocationHandler {
    private ResourceBundle decryptHandler_es_ES;

    InterfaceWorks works;

    public DecryptHandler(InterfaceWorks w) {
        this.works = w;
        
        try{
            Client c = LoaderProvider.getInstance().getClientConfiguration();
            
            switch(c.getLocalization()){
                case "Español":
                    decryptHandler_es_ES  = ResourceBundle.getBundle("org/comcast/locale/DecryptHandler_es_ES");
                    break;
                case "Ingles":
                    decryptHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/DecryptHandler_en_US");
                    break;
                default:
                    decryptHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/DecryptHandler_en_US");
                    break;
            }
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getName().equalsIgnoreCase("decryptFiles")) {
                return method.invoke(works, args);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException w) {
            return null;
        }

        throw new IllegalAccessException(decryptHandler_es_ES.getString("NO TIENES PERMISOS PARA LA EJECUCION. DESENCRIPTAR ARCHIVO"));
    }
}
