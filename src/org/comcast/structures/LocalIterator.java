package org.comcast.structures;

/**
 * Clase que representa la estructura que deben seguir los iteradores que nos son
 * nativos del lenguaje Java.
 * @author Federico Bruera TSB 2010
 */
public interface LocalIterator<E>{

    /**
     * Verifica si existen mas elementos por analizar o iterar.
     * @return true si hay mas elementos. <br>
     * false si no los hay.
     */
    public boolean hasMoreElements();

    /**
     * Retorna un elemento y luego pasa al elemento siguiente inmediato.
     * @return dicho elemento.
     */
    public E returnElement();

    /**
     * Retorna la cantidad de elementos que posee el iterador.
     * @return la cantidad de elementos.
     */
    public int size();
}
