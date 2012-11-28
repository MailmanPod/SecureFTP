package org.comcast.structures;

import java.io.Serializable;

/**
Clase para representar al Nodo de un Arbol Binario 
@author Ing. Valerio Frittelli
@version Septiembre de 2008
 */
public class NodeTree<E extends Comparable> implements Serializable {

    /**
     * Version de esta clase, para garantizar el paso serializacion - deserializacion.
     */
    private static final long serialversionUID = -4654648314301801715L;
    /**
     * Atributo que almacena la informacion.
     */
    private E info;
    /**
     * Atributos que contienen punteros tanto al hijo derecho como al hijo izquierdo.
     */
    private NodeTree<E> izq, der;

    /**
    Constructor por defecto
     */
    public NodeTree() {
    }

    /**
    Inicializa los atributos tomando los valores de los par�metros
     */
    public NodeTree(E x, NodeTree<E> iz, NodeTree<E> de) {
        info = x;
        izq = iz;
        der = de;
    }

    /**
    Modificador del info
    @param x nuevo valor del info
     */
    public void setInfo(E x) {
        info = x;
    }

    /**
    Modificador de la direcci�n del sub�rbol izquierdo
    @param iz Nuevo valor del atributo izq
     */
    public void setIzquierdo(NodeTree<E> iz) {
        izq = iz;
    }

    /**
    Modificador de la direcci�n del sub�rbol derecho
    @param de Nuevo valor del atributo der
     */
    public void setDerecho(NodeTree<E> de) {
        der = de;
    }

    /**
    Acceso al info
    @return valor del info
     */
    public E getInfo() {
        return info;
    }

    /**
    Acceso a la direcci�n del sub�rbol izquierdo
    @return valor de la direcci�n del sub�rbol izquierdo
     */
    public NodeTree<E> getIzquierdo() {
        return izq;
    }

    /**
    Acceso a la direcci�n del sub�rbol derecho
    @return valor de la direcci�n del sub�rbol derecho
     */
    public NodeTree<E> getDerecho() {
        return der;
    }

    /**
    Redefinici�n de toString
    @return representaci�n del contenido del nodo como un String
     */
    public String toString() {
        return info.toString();
    }
}
