/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Quality of Service
 */
public class UploadHandler implements InvocationHandler {

    private InterfaceWorks works;

    public UploadHandler(InterfaceWorks w) {
        works = w;
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

        throw new IllegalAccessException("No tienes permisos para la ejecucion. Consulte a su administrador");
    }
}
