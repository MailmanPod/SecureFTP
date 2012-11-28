package org.comcast.structures;

import org.comcast.exceptions.NullObjectParameterException;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Clase para representar un Arbol Binario de B�squeda.
 * @param <E> tipo de dato que contendra el arbol.
 * @author Ing. Valerio Frittelli
 * @version Septiembre de 2008 
 */
public class TreeSearch<E extends Comparable> implements Serializable {

    /**
     * Version de esta clase, para garantizar el paso serializacion - deserializacion.
     */
    private static final long serialversionUID = 6132737248488624072L;
    /**
     * Atributo que representa al nodo raiz del arbol.
     */
    private NodeTree<E> raiz;

    /**
    Constructor. Garantiza que el �rbol arranca vac�o
     */
    public TreeSearch() {
        raiz = null;
    }

    /**
     * Retorna la direcci�n del nodo raiz.
     * @return un referencia al nodo raiz.
     */
    public NodeTree<E> getRaiz() {
        return raiz;
    }

    /**
     * Cambia la direcci�n del nodo raiz.
     * @param r la referencia al nuevo nodo raiz.
     */
    public void setRaiz(NodeTree<E> r) {
        raiz = r;
    }

    /**
    Busca un objeto en el �rbol y retorna la direcci�n del nodo que lo contiene,
    o null si no lo encuentra. Se considera que el �rbol es de b�squeda, y por lo
    tanto no es heterog�neo. Se supone que el m�todo de inserci�n usado para
    crear el �rbol es el provisto por esta clase, el cual verifica que el �rbol se
    mantenga homog�neo.
    @param x el objeto a buscar
    @return la direcci�n del objeto encontrado que coincide con x, o null si x no se
    encuentra. Tambi�n sale con null si se detecta que el objeto x no es
    compatible con el tipo del info en los nodos del �rbol.
     */
    public E search(E x) {
//       if(x == null || (raiz != null && x.getClass() != raiz.getInfo().getClass())) return null;

        NodeTree<E> p = raiz;
        while (p != null) {
            E y = p.getInfo();
            if (x.compareTo(y) == 0) {
                break;
            }
            if (x.compareTo(y) < 0) {
                p = p.getIzquierdo();
            } else {
                p = p.getDerecho();
            }
        }
        return (p != null) ? p.getInfo() : null;
    }

    /**
    Inserta un objeto en el �rbol. Si el objeto a insertar ya exist�a, no lo inserta y sale
    retornando false. Si no exist�a, lo inserta y retorna true. El m�todo cuida que el �rbol
    se mantenga homog�neo, retornando false sin hacer nada si se intenta insertar un objeto
    cuya clase no coincida con la de los que ya est�n en el �rbol.
    @return true si el objeto pudo insertarse - false en caso contrario
    @param x el objeto a insertar
     */
    public boolean add(E x) {

//       if(x == null || (raiz != null && x.getClass() != raiz.getInfo().getClass())) return false;

        NodeTree<E> p = raiz, q = null;
        while (p != null) {
            E y = p.getInfo();
            if (x.compareTo(y) == 0) {
                break;
            }

            q = p;
            if (x.compareTo(y) < 0) {
                p = p.getIzquierdo();
            } else {
                p = p.getDerecho();
            }
        }

        // si ya exist�a... retorne false.
        if (p != null) {
            return false;
        }

        // si no exist�a... hay que insertarlo.
        NodeTree<E> nuevo = new NodeTree<E>(x, null, null);
        if (q == null) {
            raiz = nuevo;
        } else {
            if (x.compareTo(q.getInfo()) < 0) {
                q.setIzquierdo(nuevo);
            } else {
                q.setDerecho(nuevo);
            }
        }
        return true;
    }

    /**
     * Similar al metodo <><>add</b></i> con la unica diferencia es que el ordenamiento
     * de la insercion de los objetos al arbol se realiza segun un objeto Comparator. <br>
     * Este metodo a sido modificado para que el arbol binario contenga elementos repetidos. <br>
     * <b><i>Aclaracion muy importante: No realizar ninguna otra operacion sobre este arbol, una vez
     * que ya alla sido ejecutado este metodo. Puede que el arbol se comporte arrojando
     * resultados inesperados o erroneos.</i></b>
     * @param x con el nuevo elemento.
     * @param c Comparator con el criterio de insercion.
     * @return true si el proceso ha sido exitoso. <br>
     * false en caso contrario.
     * @throws NullObjectParameterException si no se ha especificado algun comparador.
     */
    public boolean add(E x, Comparator<E> c) throws NullObjectParameterException {

//       if(x == null || (raiz != null && x.getClass() != raiz.getInfo().getClass())) return false;

        if (c == null) {
            throw new NullObjectParameterException("No se ha indicado el comparador");
        }

        NodeTree<E> p = raiz, q = null;
        while (p != null) {
            E y = p.getInfo();
//            if (c.compare(x, y) == 0) {
//                break;
//            }

            q = p;
            if (c.compare(x, y) < 0) {
                p = p.getIzquierdo();
            } else {
                p = p.getDerecho();
            }
        }

//        // si ya exist�a... retorne false.
//        if (p != null) {
//            return false;
//        }

        // si no exist�a... hay que insertarlo.
        NodeTree<E> nuevo = new NodeTree<E>(x, null, null);
        if (q == null) {
            raiz = nuevo;
        } else {
            if (c.compare(x, q.getInfo()) < 0) {
                q.setIzquierdo(nuevo);
            } else {
                q.setDerecho(nuevo);
            }
        }
        return true;
    }
    /**
     * Atributo que representa al primer elemento de un recorrido en entreorden.
     */
    private E first;
    /**
     * Atributo que representa al ultimo elemento de un recorrido en entreorden.
     */
    private E last;

    /**
     * Metodo que devuelve el primer elemento de un recorrido en entreorden.
     * @return el primer elemento del recorrido en entreorden.
     */
    public E getFirst() {
        first = null;
        iteratorFirst(raiz);

        if (first == null) {
            return null;
        }

        return first;
    }

    /**
     * Metodo recursivo que se encarga cual es el objeto que, segun el recorrido en entreorden,
     * es el primero.
     * @param p nodo del arbol.
     */
    private void iteratorFirst(NodeTree<E> p) {
        if (p != null && first == null) {
            iteratorFirst(p.getIzquierdo());
            if (first == null) {
                first = p.getInfo();
            }
        }
    }

    /**
     * Similar al <i><b>getFirst</i></b> nada mas que devuelve el ultimo elemento de la iteracion.
     * @return el ultimo elemento del recorrido en entreorden.
     */
    public E getLast() {
        last = null;
        iteratorLast(raiz);

        if (last == null) {
            return null;
        }

        return last;
    }

    /**
     * Similar al metodo <i><b>iteratorFirst</b></i> nada mas que calcula al ultimo de
     * la iteracion.
     * @param p nodo del arbol.
     */
    private void iteratorLast(NodeTree<E> p) {
        if (p != null && last == null) {
            iteratorLast(p.getDerecho());
            if (last == null) {
                last = p.getInfo();
            }
        }
    }

    /**
     * Atributo que representa a una lista simple con todos los elementos que se
     * encuentran en el arbol binario.
     */
//    private SimpleList<E> fullTree;
    /**
     * Retorna un iterador con todos los elementos ordenados de menor a mayor
     * @return LocalIterator<E> un iterador con los elementos ordenados.
     */
    public LocalIterator<E> getAscendingIterator() {
        SimpleList<E> fullTree = new SimpleList<E>();
        ascendingIterator(raiz, fullTree);

        return fullTree.getIterador();
    }

    /**
     * Retorna un iterador con todos los elementos ordenados de mayor a menor
     * @return LocalIterator<E> un iterador con los elementos ordenados.
     */
    public LocalIterator<E> getDescendingIterator() {
        SimpleList<E> fullTree = new SimpleList<E>();
        descendingIterator(raiz, fullTree);

        return fullTree.getIterador();
    }

    /**
     * Metodo recursivo que inserta los elementos a la lista simple de menor a
     * mayor. <br>
     * Utiliza un recorrido de precedencia izquierda.
     * @param p nodo del arbol.
     * @param fullTree lista simple en donde se cargaran los datos del arbol.
     */
    private void ascendingIterator(NodeTree<E> p, SimpleList<E> fullTree) {
        if (p != null) {
            ascendingIterator(p.getIzquierdo(), fullTree);
            fullTree.addLast(p.getInfo());
            ascendingIterator(p.getDerecho(), fullTree);
        }
    }

    /**
     * Metodo recursivo que inserta los elementos a la lista simple de mayor a
     * menor.<br>
     * Utiliza un recorrido de precedencia derecha.
     * @param p nodo del arbol.
     * @param fullTree lista simple en donde se cargaran los datos del arbol.
     */
    private void descendingIterator(NodeTree<E> p, SimpleList<E> fullTree) {
        if (p != null) {
            descendingIterator(p.getDerecho(), fullTree);
            fullTree.addLast(p.getInfo());
            descendingIterator(p.getIzquierdo(), fullTree);
        }
    }

    /**
    Borra el nodo del Arbol que contiene al objeto x. Si el objeto x no es compatible con la
    clase de los nodos del �rbol, el m�todo sale sin hacer nada y retorna false.
    @param x el objeto a borrar.
    @return true si la eliminaci�n pudo hacerse, o false en caso contrario.
     */
    public boolean remove(E x) {
//       if(x == null || (raiz != null && x.getClass() != raiz.getInfo().getClass())) return false;

        raiz = eliminar(raiz, x);
        return true;
    }

    /**
    Redefinici�n de toString
    @return el contenido del arbol, en secuencia de entre orden, como un String
     */
    public String toString() {
        StringBuffer cad = new StringBuffer("");
        armarEntreOrden(raiz, cad);
        return cad.toString();
    }

    /**
    Genera un String con el contenido del arbol en pre orden
    @return el contenido del arbol, en secuencia de pre orden, como un String
     */
    public String toPreOrdenString() {
        StringBuffer cad = new StringBuffer("");
        armarPreOrden(raiz, cad);
        return cad.toString();
    }

    /**
    Genera un String con el contenido del arbol en entre orden. Genera el mismo String que el
    m�todo toString() redefinido en la clase
    @return el contenido del arbol, en secuencia de entre orden, como un String
     */
    public String toEntreOrdenString() {
        return this.toString();
    }

    /**
    Genera un String con el contenido del arbol en post orden
    @return el contenido del arbol, en secuencia de post orden, como un String
     */
    public String toPostOrdenString() {
        StringBuffer cad = new StringBuffer("");
        armarPostOrden(raiz, cad);
        return cad.toString();
    }

    private void armarPreOrden(NodeTree<E> p, StringBuffer cad) {
        if (p != null) {
            cad = cad.append(p.getInfo().toString() + " ");
            armarPreOrden(p.getIzquierdo(), cad);
            armarPreOrden(p.getDerecho(), cad);
        }
    }

    private void armarEntreOrden(NodeTree<E> p, StringBuffer cad) {
        if (p != null) {
            armarEntreOrden(p.getIzquierdo(), cad);
            cad = cad.append(p.getInfo().toString() + " ");
            armarEntreOrden(p.getDerecho(), cad);
        }
    }

    private void armarPostOrden(NodeTree<E> p, StringBuffer cad) {
        if (p != null) {
            armarPostOrden(p.getIzquierdo(), cad);
            armarPostOrden(p.getDerecho(), cad);
            cad = cad.append(p.getInfo().toString() + " ");
        }
    }

    /*
    Auxiliar del m�todo de borrado. Borra un nodo que contenga al objeto x si el mismo
    tiene un hijo o ninguno.
    @param p nodo que est� siendo procesado
    @param x Objeto a borrar
    @return direcci�n del nodo que qued� en lugar del que ven�a en "p" al comenzar el proceso
     */
    private NodeTree<E> eliminar(NodeTree<E> p, E x) {
        if (p != null) {
            E y = p.getInfo();
            if (x.compareTo(y) < 0) {
                NodeTree<E> menor = eliminar(p.getIzquierdo(), x);
                p.setIzquierdo(menor);
            } else {
                if (x.compareTo(y) > 0) {
                    NodeTree<E> mayor = eliminar(p.getDerecho(), x);
                    p.setDerecho(mayor);
                } else {
                    // Objeto encontrado... debe borrarlo.
                    if (p.getIzquierdo() == null) {
                        p = p.getDerecho();
                    } else {
                        if (p.getDerecho() == null) {
                            p = p.getIzquierdo();
                        } else {
                            // Tiene dos hijos... que lo haga otra!!!
                            NodeTree<E> dos = dosHijos(p.getIzquierdo(), p);
                            p.setIzquierdo(dos);
                        }
                    }
                }
            }
        }
        return p;
    }

    /*
    Auxiliar del m�todo de borrado. Reemplaza al nodo que ven�a en "p" por su mayor descendiente izquierdo "d",
    y luego borra a "d" de su posici�n original
    @param d nodo que est� siendo procesado
    @param p nodo a reemplazar por d
    @return direcci�n del nodo que qued� en lugar del que ven�a en "d" al comenzar el proceso.
     */
    private NodeTree<E> dosHijos(NodeTree<E> d, NodeTree<E> p) {
        if (d.getDerecho() != null) {
            NodeTree<E> der = dosHijos(d.getDerecho(), p);
            d.setDerecho(der);
        } else {
            p.setInfo(d.getInfo());
            d = d.getIzquierdo();
        }
        return d;
    }
}
