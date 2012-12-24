/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

/**
 *
 * @author Quality of Service
 */
public class DecryptHandler implements InvocationHandler {
    private ResourceBundle decryptHandler_es_ES = ResourceBundle.getBundle("org/comcast/locale/DecryptHandler_es_ES");

    InterfaceWorks works;

    public DecryptHandler(InterfaceWorks w) {
        this.works = w;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getName().equalsIgnoreCase("decryptFiles")) {
                return method.invoke(works, args);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException w) {
            w.printStackTrace();
            return null;
        }

        throw new IllegalAccessException(decryptHandler_es_ES.getString("NO TIENES PERMISOS PARA LA EJECUCION. DESENCRIPTAR ARCHIVO"));
    }
}
