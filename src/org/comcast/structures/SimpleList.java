package org.comcast.structures;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Comparator;
import org.comcast.exceptions.EmptyListException;
import org.comcast.exceptions.ListPointerOutOfBoundsException;
import org.comcast.exceptions.NullObjectParameterException;

/**
 * Una lista genérica. Suponemos ahora que cada nodo referencia a un Comparable,
 * y no a un Object. La clase controla homogeneidad mediante generics, y también
 * provee métodos para facilitar su recorrido externo a modo de implementación
 * liviana del patrón Iterator.
 *
 * @author Ing. Valerio Frittelli.
 * @version Agosto de 2008.
 */
public class SimpleList<E extends Comparable> implements Serializable {

    /**
     * Version de esta clase, para garantizar el paso serializacion -
     * deserializacion.
     */
    private static final long serialversionUID = -2913702428605817301L;
    /**
     * Atributo que representa un puntero al principio de la lista.
     */
    private Node<E> frente;
    /**
     * atributo que representa el final de la lista.
     */
    private Node<E> fondo;
    /**
     * Atributo que representa el tamanio de la lista.
     */
    private int size;

    /**
     * Constructor por defecto
     */
    public SimpleList() {
        this.frente = null;
        this.fondo = null;
        this.size = 0;
    }

    /**
     * Inserta un objeto al principio de la lista. La inserción se hará sólo si
     * el parámetro recibido no es null.
     *
     * @param x el objeto a almacenar en la lista.
     */
    public void addFirst(E x) {
        Node<E> nuevo = new Node<E>(x, frente);

        if (frente == null) {
            fondo = nuevo;
        }

        frente = nuevo;
        size++;
    }

    /**
     * Inserta un objeto al final de la lista. La inserción se hará sólo si el
     * parámetro recibido no es null.
     *
     * @param x el objeto a almacenar en la lista.
     */
    public void addLast(E x) {

        Node<E> nuevo = new Node<E>(x, null);

        if (frente == null) {
            frente = nuevo;
            fondo = frente;
        } else {
            fondo.setNext(nuevo);
            fondo = nuevo;
        }

        size++;
    }

    /**
     * Inserta un objeto en forma ordenada en la lista. La inserción se hará
     * sólo si el parámetro recibido no es null. Se supone que la lista está ya
     * ordenada (es decir, se supone que todas las inserciones fueron realizadas
     * llamando a este método). Este método no viene en la clase LinkedList
     * tomada como modelo para el planteo realizado en clase de SimpleList: se
     * incorpora desde la materia TSB por tratarse de un algoritmo clásico e
     * interesante.
     *
     * @param x el objeto a almacenar en la lista.
     */
    public void addInOrder(E x) {

        Node<E> nuevo = new Node(x, null);
        Node<E> p = frente, q = null;

        while (p != null && x.compareTo(p.getInfo()) >= 0) {
            q = p;
            p = p.getNext();
        }

        nuevo.setNext(p);

        if (frente == null || p == null) {
            fondo = nuevo;
        }

        if (q != null) {
            q.setNext(nuevo);
        } else {
            frente = nuevo;
        }

        size++;
    }

    public void addInOrder(E x, Comparator<E> c) {

        Node<E> nuevo = new Node(x, null);
        Node<E> p = frente, q = null;

        while (p != null && c.compare(x, p.getInfo()) >= 0) {
            q = p;
            p = p.getNext();
        }

        nuevo.setNext(p);

        if (frente == null || p == null) {
            fondo = nuevo;
        }

        if (q != null) {
            q.setNext(nuevo);
        } else {
            frente = nuevo;
        }

        size++;
    }

    /**
     * Metodo que inserta un objeto segun el index que se pasa como parametro en
     * la lista simple.
     *
     * @param index la posicion donde se insertara el nuevo objeto.
     * @param x el nuevo objeto a insertar
     * @throws ListPointerOutOfBoundsException si el indice es negativo o es
     * mayor al largo actual de la lista.
     */
    @SuppressWarnings("empty-statement")
    public void add(int index, E x) throws ListPointerOutOfBoundsException {

        if (index < 0 || index > size) {
            throw new ListPointerOutOfBoundsException("Puntero fuera de rango");
        }

        int i = 0;
        Node<E> p = frente;
        Node<E> q = null;

        for (; i < index; i++, q = p, p = p.getNext());

        Node<E> nuevo = new Node<E>(x, p);

        if (frente == null || p == null) {
            fondo = nuevo;
        }

        if (q != null) {
            q.setNext(nuevo);
        } else {
            frente = nuevo;
        }

        size++;
    }

    /**
     * Remueve todos los elementos de la lista.
     */
    public void clear() {
        frente = fondo = null;
    }

    /**
     * Metodo que tiene como objetivo verificar si el objeto que se pasa como
     * parametro se encuentra en la lista simple.
     *
     * @param x elemento a buscar en la lista.
     * @return true si encontro el objeto en la lista. <br> false en el caso
     * contrario.
     */
    public boolean contains(E x) {
        Node<E> p = frente;
        while (p != null && x.compareTo(p.getInfo()) != 0) {
            p = p.getNext();
        }
        return (p != null);
    }

    /**
     * Retorna (pero sin removerlo) el objeto ubicado al principio de la lista.
     *
     * @return una referencia al primer elemento de la lista.
     * @throws EmptyListException si la lista estaba vacía.
     */
    public E getFirst() throws EmptyListException {
        if (frente == null) {
            throw new EmptyListException("Error: la lista está vacía...");
        }

        return frente.getInfo();
    }

    /**
     * Retorna (pero sin removerlo) el objeto ubicado al final de la lista.
     *
     * @return una referencia al primer elemento de la lista.
     * @throws EmptyListException si la lista estaba vacía.
     */
    public E getLast() throws EmptyListException {

        if (frente == null || fondo == null) {
            throw new EmptyListException("No existen elementos en la lista");
        }

        return fondo.getInfo();
    }

    /**
     * Metodo que tiene como objetivo retornar un objeto que esta en la lista
     * segun un indice que se pasa como parametro.
     *
     * @param index con la posicion donde posiblemente se encuentre el objeto.
     * @return el objeto encontrado.
     * @throws ListPointerOutOfBoundsException si el inidice esta fuera del
     * rango o cuando el puntero se "cayo de la lista".
     */
    @SuppressWarnings("empty-statement")
    public E get(int index) throws ListPointerOutOfBoundsException {

        Node<E> p = frente;

        for (int i = 0; i < index && p != null; i++, p = p.getNext());

        if (p != null && index >= 0) {
            return p.getInfo();
        } else {
            throw new ListPointerOutOfBoundsException("Puntero fuera de rango");
        }
    }

    /**
     * Retorna true si la lista está vacía.
     *
     * @return true si la lista está vacía - false en caso contrario.
     */
    public boolean isEmpty() {
        return (frente == null);
    }

    /**
     * Retorna (y remueve) el objeto ubicado al final de la lista.
     *
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista estaba vacía.
     */
    public E removeLast() throws EmptyListException {

        if (frente == null) {
            throw new EmptyListException("No existen elementos en la lista");
        }

        Node<E> p = frente;
        Node<E> buffer = null;


        while (p.getNext() != null && p.getNext() != fondo) {
            p = p.getNext();
        }


        if (p == fondo) {
            buffer = frente;
            frente = frente.getNext();
        } else {
            buffer = fondo;
            fondo = p;
            p.setNext(null);
        }

        size--;

        return buffer.getInfo();
    }

    /**
     * Retorna (y remueve) el objeto ubicado al principio de la lista.
     *
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista estaba vacía.
     */
    public E removeFirst() throws EmptyListException {
        if (frente == null) {
            throw new EmptyListException("Error: la lista está vacía...");
        }

        E x = frente.getInfo();
        frente = frente.getNext();
        size--;
        return x;
    }

    /**
     * Metodo que tiene como objetivo eliminar y retornar un objeto, segun la
     * posicion pasada como parametro.
     *
     * @param index con la posicion del objeto a eliminar.
     * @return el objeto eliminado.
     * @throws ListPointerOutOfBoundsException si la lista esta vacia, o si el
     * indice pasado como parametro es negativo, o si el indice es mayor al
     * tamanio de la lista.
     */
    @SuppressWarnings("empty-statement")
    public E remove(int index) throws ListPointerOutOfBoundsException {

        if (frente == null || index >= size || index < 0) {
            throw new ListPointerOutOfBoundsException("Puntero fuera de rango");
        }

        Node<E> p = frente;
        Node<E> q = null;

        for (int i = 0; i < index && p != null; i++, q = p, p = p.getNext());

        if (p.getNext() == null) {
            fondo = q;
        }

        if (q != null) {
            q.setNext(p.getNext());
        } else {
            frente = frente.getNext();
        }

        size--;

        return p.getInfo();
    }

    /**
     * Metodo que tiene como finalidad buscar un determinado objeto en la lista.
     *
     * @param x el objeto a buscar.
     * @return E si el objeto se encuentra en la lista. <br> null en caso
     * contrario.
     */
    public E search(E x) {

        //E r = null;
        Node<E> p = frente;
        while (p != null && x.compareTo(p.getInfo()) != 0) {
            p = p.getNext();
        }
        if (p != null) {
            //r = p.getInfo();
            return p.getInfo();
        }
        return null;
        //return r;
    }

    /**
     * Metodo que tiene como objetivo reemplazar el elemento ubicado en el nodo
     * numero index, por el elemento x.
     *
     * @param index indice o posicion del objeto a reemplazar.
     * @param x nuevo objeto.
     * @return el objeto antiguo (el que se encontraba antes de ser
     * reemplazado).
     * @throws EmptyListException si la lsita esta vacia.
     */
    @SuppressWarnings({"unchecked", "empty-statement"})
    public E set(int index, E x) throws EmptyListException {

        if (frente == null) {
            throw new EmptyListException("No existen elementos en la lista");
        }

        Node<E> p = frente;
        Node<E> anterior = null;

        for (int i = 0; i < index; i++, p = p.getNext());

        anterior = p;

        p.setInfo(x);

        return anterior.getInfo();

    }

    /**
     * Retorna el inidice o posicion de la primera ocurrencia de x en la lista,
     * o retorna -1 si x no esta en la lista.
     *
     * @param x elemento a ser analizado.
     * @return int que es el indice de la primera ocurrencia. <br> -1 en caso
     * contrario
     * @throws EmptyListException si la lista esta vacia.
     */
    @SuppressWarnings({"unchecked", "empty-statement"})
    public int indexOf(E x) throws EmptyListException {

        if (frente == null) {
            throw new EmptyListException("No existen elementos en la lista");
        }

        int i = 0;
        Node<E> p = frente;

        while (p != null) {

            if (p.getInfo().compareTo(x) == 0) {
                return i;
            }
            i++;
            p = p.getNext();
        }

        return -1;
    }

    /**
     * Retorna el inidice o posicion de la primera ocurrencia de x en la lista,
     * o retorna -1 si x no esta en la lista.
     *
     * @param x elemento a ser analizado.
     * @return int que es el indice de la primera ocurrencia. <br> -1 en caso
     * contrario
     * @throws EmptyListException si la lista esta vacia.
     */
    @SuppressWarnings("unchecked")
    public int lastIndexOf(E x) throws EmptyListException {

        if (frente == null) {
            throw new EmptyListException("No existen elementos en la lista");
        }

        int i = 0;
        int last = -1;
        Node<E> p = frente;

        while (p != null) {

            if (p.getInfo().compareTo(x) == 0) {
                last = i;
            }
            i++;
            p = p.getNext();
        }

        return last;
    }

    /**
     * Retorna el numero de elementos que contiene la lista.
     *
     * @return el numero de elementos.
     */
    public int size() {
        return size;
    }

    /**
     * Redefine el método toString heredado desde Object.
     *
     * @return el contenido de la lista convertido a String.
     */
    @Override
    public String toString() {
        StringBuilder cadena = new StringBuilder();
        LocalIterator<E> itr = this.getIterador();

        while (itr.hasMoreElements()) {
            cadena.append("\n").append(itr.returnElement());
        }

        return cadena.toString();
    }

    /**
     * Metodo que tiene como objetivo crear un arreglo que contiene todos los
     * elementos que se encuentran en la lista.
     *
     * @param clase representa al tipo de elementos que contendra el arreglo. En
     * otras palabras, indicamos de que clase son los objetos que contiene la
     * lista.
     * @param objects SimpleList<E> con todos los elementos.
     * @return E[] arreglo que representa a todos los elementos.
     */
    public E[] toArray(Class<E> clase, SimpleList<E> objects) {
        E[] array = (E[]) Array.newInstance(clase, objects.size());
        LocalIterator<E> iter = objects.getIterador();
        int i = 0;

        while (iter.hasMoreElements()) {
            array[i] = iter.returnElement();
            i++;
        }

        return array;
    }

    public E[] toArray(Class<E> clase) {
        E[] array = (E[]) Array.newInstance(clase, this.size);
        LocalIterator<E> iter = this.getIterador();
        int i = 0;

        while (iter.hasMoreElements()) {
            array[i] = iter.returnElement();
            i++;
        }

        return array;
    }

    /**
     * Metodo que tiene como objetivo transferir todos los elementos que se
     * encuentran en un arreglo a una lista simple.
     *
     * @param array con todos los objetos.
     * @return SimpleList<E> con la representacion de todos los objetos
     * contenidos en el arreglo.
     */
    public SimpleList<E> toSimpleList(E[] array) {
        SimpleList<E> resultado = new SimpleList<E>();

        for (int i = 0; i < array.length; i++) {
            resultado.addLast(array[i]);
        }

        return resultado;
    }

    /**
     * Metodo que representa al algoritmo de ordenamiento quicksort.
     *
     * @param array con el arreglo a ordenar.
     * @param c Comparator con el criterio de ordenacion.
     * @return E[] arreglo ya ordenado.
     */
    public E[] quickSort(E[] array, Comparator<E> c) {
        procedureQuicksort(array, 0, array.length - 1, c);
        return array;
    }

    /**
     * Metodo que tiene como objetivo llevar a cabo la ejecucion del algoritmo
     * de ordenamiento quicksort.
     *
     * @param vector arreglo a ordenar.
     * @param primero puntero que se encarga de la parte izquierda del vector.
     * @param ultimo puntero que se encarga de la parte derecha del vector.
     * @param c Comparator con el criterio de ordenacion.
     */
    private void procedureQuicksort(E[] vector, int primero, int ultimo, Comparator<E> c) {
        int i = primero, j = ultimo;
        E pivote = vector[(primero + ultimo) / 2];
        E auxiliar;

        do {
            while (c.compare(vector[i], pivote) < 0) {
                i++;
            }
            while (c.compare(vector[j], pivote) > 0) {
                j--;
            }

            if (i <= j) {
                auxiliar = vector[j];
                vector[j] = vector[i];
                vector[i] = auxiliar;
                i++;
                j--;
            }

        } while (i <= j);

        if (primero < j) {
            procedureQuicksort(vector, primero, j, c);
        }
        if (ultimo > i) {
            procedureQuicksort(vector, i, ultimo, c);
        }
    }

    /**
     * Metodo que retorna un iterador sobre la lista.
     *
     * @return LocalIterator<E> con el iterador.
     */
    public LocalIterator<E> getIterador() {
        return new Iterador();
    }

    /**
     * Metodo que tiene como objetivo devolver un arbol binario AVL con todos
     * los objetos que componen la lista enlazada.
     *
     * @return AVLTreeSearch<E> con todas las entradas activas.
     */
    public AVLTreeSearch<E> treeView() {
        return naturalOrderTree();
    }

    /**
     * Sobrecarga del metodo treeView. Recibe un objeto Comaprator, que
     * especifica cual es el critrierio de carga / ordenacion, que seguira el
     * arbol binario AVL.
     *
     * @param c Comparator especificando el criterio de ordenacion / carga.
     * @return AVLTreeSearch<E> con todas las entradas activas.
     * @throws NullObjectParameterException si no se ha especificado un
     * comparador.
     */
    public AVLTreeSearch<E> treeView(Comparator<E> c) throws NullObjectParameterException {
        if (c == null) {
            throw new NullObjectParameterException("No se ha especificado el comparador");
        }
        return comparatorOrderTree(c);
    }

    /**
     * Este metodo tiene como objetivo construir un arbol binario AVL, siguiendo
     * el criterio de ordenacion natural de los objetos. Esto quiere decir que
     * los objetos seran insertados en el arbol binario segun el metodo
     * compareTo de la clase Comparator.
     *
     * @return AVLTreeSearch<E> con todos los objetos activos.
     */
    private AVLTreeSearch<E> naturalOrderTree() {
        AVLTreeSearch<E> tree = new AVLTreeSearch<E>();

        LocalIterator<E> iter = this.getIterador();

        while (iter.hasMoreElements()) {
            E buffer = iter.returnElement();
            tree.add(buffer);
        }

        return tree;
    }

    /**
     * Este metodo tiene como objetivo construir un arbol binario, siguiendo el
     * criterio de ordenacion segun el objeto Comparator que alla sido
     * suministrado de los objetos. Esto quiere decir que los objetos seran
     * insertados en el arbol binario segun el metodo compare de la clase
     * Comparator.
     *
     * @param c Comparator especificando el criterio de ordenacion / carga.
     * @return TreeSearch<E> con todos los objetos activos.
     * @throws NullObjectParameterException si no se especifico algun
     * Comparator.
     */
    private AVLTreeSearch<E> comparatorOrderTree(Comparator<E> c) throws NullObjectParameterException {
        AVLTreeSearch<E> tree = new AVLTreeSearch<E>();
        LocalIterator<E> iter = this.getIterador();

        while (iter.hasMoreElements()) {
            E buffer = iter.returnElement();
            tree.add(buffer, c);
        }
        return tree;
    }

    /**
     * Clase que representa al patron iterador.
     */
    private class Iterador implements LocalIterator<E> {

        /**
         * Atributo que representa un puntero al nodo actual, al cual se esta
         * visitando.
         */
        private Node<E> actual;

        /**
         * Constructor de la clase.
         */
        public Iterador() {
            this.actual = frente;
        }

        /**
         * Metodo que chequea si exsiten mas elementos en la iteracion.
         *
         * @return true si hay elementos. <br> false en caso contrario.
         */
        public boolean hasMoreElements() {
            return actual != null;
        }

        /**
         * Metodo que retorna un elemento, y a continuacion se coloca en el nodo
         * siguiente al visitado.
         *
         * @return E con el elemento.
         */
        public E returnElement() {

            Node<E> buffer = actual;
            actual = actual.getNext();

            return buffer.getInfo();
        }

        /**
         * Metodo que retorna la cantidad de elementos que posee el iterador.
         *
         * @return la cantidad de elementos.
         */
        public int size() {
            return size;
        }
    }
}
