package org.comcast.structures;

import java.io.Serializable;

/**
 *  Segunda versión de una clase para representar un nodo para una lista simple genérica.
 *  Se usa parametrización de clases (generics) para controlar automáticamente el contenido homogéneo.
 *  @param <E>
 *  @author Ing. Valerio Frittelli
 *  @version Agosto de 2008
 */
public final class Node<E extends Comparable> implements Serializable {

    /**
     * Version de esta clase, para garantizar el paso serializacion - deserializacion.
     */
    private static final long serialversionUID = -4855415733160736279L;
    /**
     * Atributo que almacena la informacion.
     */
    private E info;
    /**
     * Atributo que contiene un puntero al siguiente nodo en la lista.
     */
    private Node<E> nodo;

    /**
     *  Constructor por defecto.
     */
    public Node() {
    }

    /**
     * Crea un nodo incializando todos los atributos en base a parámetros
     * @param x
     * @param p
     */
    public Node(E info, Node<E> n) {
        this.info = info;
        this.nodo = n;
    }

    /**
     *  Accede a la dirección del sucesor
     *  @return la dirección del nodo sucesor
     */
    public Node<E> getNext() {
        return nodo;
    }

    /**
     * Cambia la dirección del sucesor
     * @param p dirección del nuevo sucesor
     */
    public void setNext(Node<E> nodo) {
        this.nodo = nodo;
    }

    /**
     *  Accede al valor del info
     *  @return el valor del info
     */
    public E getInfo() {
        return info;
    }

    /**
     * Cambia el valor del info
     * @param p nuevo valor del info
     */
    public void setInfo(E info) {
        this.info = info;
    }

    /**
     * Convierte el contenido del nodo en String
     * @return el contenido del nodo convertido en String
     */
    @Override
    public String toString() {
        return info.toString();
    }
}
