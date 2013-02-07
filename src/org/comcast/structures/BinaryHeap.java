package org.comcast.structures;

import org.comcast.exceptions.UnderflowException;

/**
 *CONSTRUCCION: Con capacidad opcional (que por defecto es 100) o una matriz que 
 * contiene todos los elementos iniciales
 *
 *******************OPERACIONES PUBLICAS*********************
 *void insert( x )       --> Añade x
 *Comparable deleteMin( )--> Retorna y elimina el item mas pequeño
 *Comparable findMin( )  --> Retorna el item mas pequeño
 *boolean isEmpty( )     --> Retorna true si esta vacio o false en caso contrario.
 *void makeEmpty( )      --> Elimina todos los items.
 *******************ERRORES********************************
 *Throws UnderflowException en caso de que se quiera ingresar con la cola de prioridad vacia.
 *
 * Implementa una cola binaria de prioridad. Notar que todas las comparaciones se
 * basan en el metodo compareTo.
 *
 * @author Damian Bruera
 * @since Java 6
 * @version 1.0
 */
public class BinaryHeap<E extends Comparable<? super E>> {

    /**
     * Construye la cola de prioridades con la capacidad por default.
     */
    public BinaryHeap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor de la clase.
     *
     * @param capacity Con la capacidad de la cola binaria.
     */
    public BinaryHeap(int capacity) {
        currentSize = 0;
        array = (E[]) new Comparable[capacity + 1];
    }

    /**
     * Construye una cola de prioridades dada una matriz de elementos.
     * 
     * @param items Elementos a añadir.
     */
    public BinaryHeap(E[] items) {
        currentSize = items.length;
        array = (E[]) new Comparable[(currentSize + 2) * 11 / 10];

        int i = 1;
        for (E item : items) {
            array[ i++] = item;
        }
        buildHeap();
    }

    /**
     * Insertar en la cola de prioridades, manteniendo el orden del montón. 
     * Duplicados son permitidos.
     * 
     * @param x El item a insertar.
     */
    public void insert(E x) {
        if (currentSize == array.length - 1) {
            enlargeArray(array.length * 2 + 1);
        }

        // Percolate up
        int hole = ++currentSize;
        for (array[ 0] = x; x.compareTo(array[ hole / 2]) < 0; hole /= 2) {
            array[ hole] = array[ hole / 2];
        }
        array[ hole] = x;
    }

    /**
     * Redimenciona la cola de prioridad.
     * 
     * @param newSize Con el nuevo tamaño.
     */
    private void enlargeArray(int newSize) {
        E[] old = array;
        array = (E[]) new Comparable[newSize];
        for (int i = 0; i < old.length; i++) {
            array[ i] = old[ i];
        }
    }

    /**
     * Buscar el elemento más pequeño en la cola de prioridades.
     * 
     * @return el item mas pequeño.
     * @throws UnderflowException Si esta vacia la cola.
     */
    public E findMin() throws UnderflowException {
        if (isEmpty()) {
            throw new UnderflowException();
        }
        return array[ 1];
    }

    /**
     * Quita el elemento más pequeño de la cola de prioridad.
     * 
     * @return El item mas pequeño
     * @throws UnderflowException Si esta vacia la cola.
     */
    public E deleteMin() throws UnderflowException {
        if (isEmpty()) {
            throw new UnderflowException();
        }

        E minItem = findMin();
        array[ 1] = array[ currentSize--];
        percolateDown(1);

        return minItem;
    }

    /**
     * Establece el orden del montón de un arreglo arbitrario de elementos.
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }
    
    /**
     * Verifica si la cola de prioridad esta vacia
     * @return True si esta vacia o false si no lo esta.
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Hace la cola de prioridad lógicamente vacía. 
     */
    public void makeEmpty() {
        currentSize = 0;
    }
    private static final int DEFAULT_CAPACITY = 10;
    private int currentSize;      // Number of elements in heap
    private E[] array; // The heap array

    /**
     * Método interno que filtra hacia abajo en el montón.
     *
     * @param hole indice en donde empieza el filtrado.
     */
    private void percolateDown(int hole) {
        int child;
        E tmp = array[ hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize
                    && array[ child + 1].compareTo(array[ child]) < 0) {
                child++;
            }
            if (array[ child].compareTo(tmp) < 0) {
                array[ hole] = array[ child];
            } else {
                break;
            }
        }
        array[ hole] = tmp;
    }

    @Override
    public String toString() {
        StringBuilder cadena = new StringBuilder();
        LocalIterator<E> itr = this.getIterator();

        while (itr.hasMoreElements()) {
            cadena.append("\n").append(itr.returnElement());
        }

        return cadena.toString();
    }

    /**
     * Metodo que retorna un iterador sobre la cola de prioridad.<br>
     * Este iterador recorre el array de manera secuencial.
     *
     * @return LocalIterator<E> con el iterador.
     */
    public LocalIterator<E> getIterator() {
        return new Iterator();
    }

    private class Iterator implements LocalIterator<E> {

        private int i;

        public Iterator() {
            i = 1;
        }

        @Override
        public boolean hasMoreElements() {
            return i < (currentSize + 1);
        }

        @Override
        public E returnElement() {
            E buffer = array[i];
            i++;

            return buffer;
        }

        @Override
        public int size() {
            return currentSize;
        }
    }
}
