/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.xml;

/**
 *
 * @author Quality of Service
 */
public class LoaderProvider {

    private static Loader instance = null;

    private LoaderProvider() {
    }

    public static Loader getInstance() {
        if (instance == null) {
            instance = new Loader();
        }

        return instance;
    }
}
