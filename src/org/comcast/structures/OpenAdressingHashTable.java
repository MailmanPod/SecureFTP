package org.comcast.structures;

import org.comcast.exceptions.EmptyHashTableException;
import org.comcast.exceptions.InvalidEntryException;
import org.comcast.exceptions.NullObjectParameterException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * Clase que representa una tabla de desbordamiento o tabla hash. <br>
 * Implementa, para el tratamiento de colisiones, la solucion de
 * direccionamiento abierto. <br> Utiliza la exploracion cuadratica para evitar
 * el agrupamiento primario; para esto se utiliza un calculador de numeros
 * primos para garantizar: <br> &nbsp;&nbsp;&nbsp;&nbsp 1) Que la exploracion
 * cuadratica pueda insertar objetos a la tabla sin ningun problema. <br>
 * &nbsp;&nbsp;&nbsp;&nbsp 2) Que la carga inicial de objetos siempre este por
 * debajo del factor de cargar 0.50. <br> &nbsp;&nbsp;&nbsp;&nbsp 3) Que el
 * rehashing siempre de como resultado una tabla mucho mas grande que la
 * original. De esta manera mantener, siempre, que la tabla tenga un valor primo
 * de longitud, y ademas tener un bajo factor de carga. <br> Para eliminar los
 * objetos que se encuentran en la tabla (es decir para eliminar los objetos que
 * se encuentran en las entradas de la tabla), se utiliza el marcado logico, es
 * decir cada entrada tiene un estado que es activo o no activo. Para eliminar
 * una entrada de la tabla solo hace falta setear el estado de activo a no
 * activo. <br> Otra aclaracion que vale la pena resaltar es que <i>esta tabla
 * no adminte valores repetidos</i>.
 *
 * @param <E> tipo de datos que contendra la tabla de hash.
 * @author Federico Bruera TSB 2010.
 * @version 3.4
 * @since 1.6
 */
public class OpenAdressingHashTable<E extends Comparable, K> implements Serializable {

    /**
     * Version de esta clase, para garantizar el paso serializacion -
     * deserializacion.
     */
    private static final long serialversionUID = 7184418435640244888L;
    /**
     * Factor de carga utilizado por la tabla de hash. <br> El factor de carga
     * es calculado, segun el libro de Adam Drozdek, de la siguiente manera:
     * <br> &nbsp;&nbsp;&nbsp;&nbsp <b>FC (Factor de carga) = Numero de
     * elementos en la tabla / tamanio de la tabla</b>. En la tabla del libro
     * del mismo autor, segun la tabla en la columna <i>Sondeo Cuadratico</i>,
     * que mientras el factor de carga sea mas chico que la media, mejor sera la
     * performance en la busqueda; y garantizara que el sondeo cuadratico pueda
     * insertar cualquier objeto sin que este quede afuera, es decir sin
     * cargarse en la tabla de hash.
     */
    private static final float FACTOR = 0.50f;
    /**
     * Tamanio por defecto de la tabla de Hash.
     */
    private static final int DEFAUL_SIZE = 11;
    /**
     * Arreglo de Entradas, la cual contendra todos los objetos cargados por el
     * usuario.
     *
     * @see innerclass Entry<E, K>
     */
    private Entry<E, K>[] items;
    /**
     * Atributo que contiene la cantidad de elementos que se encuentran
     * actualmente en la tabla. <br> <i>Aclaracion: este atributo cuenta todas
     * aquellos casilleros que estan ocupados, sin hacer distincion de su estado
     * <b>Activos o No Activos</b></i>
     */
    private int currentOcupation;
    /**
     * Atributo usado internamente para generar un arreglo de llaves de busqueda
     */
    private K test;

    /**
     * Constructor por defecto de la tabla Hash. <br> Utiliza el constructor con
     * un solo parametro, al cual le pasa el tamanio por defecto especificado en
     * esta misma clase.
     */
    public OpenAdressingHashTable() {
        this(DEFAUL_SIZE);
    }

    /**
     * Constructor <i>no por defecto</i> con un solo parametro, el cual crea el
     * arreglo de entradas que contendran los objetos cargados (metodo
     * <code>allocateItems</code>) y ademas inicializa cada casilla del arreglo
     * en null (metodo
     * <code>clear</code>)
     *
     * @param <code>initSize</code> tamanio inicial del arreglo.
     */
    public OpenAdressingHashTable(int initSize) {
        allocateItems(primeCalculator(initSize));
        clear();
    }

    /**
     * Metodo que realiza la creacion del arreglo de entradas, es decir, crea la
     * tabla Hash.
     *
     * @param <code>initSize</code> el tamanio inicial de la tabla.
     * @throws <code>IllegalArgumentException</code> si el tamanio pasado como
     * parametro es menor o igual a cero
     */
    private void allocateItems(int initSize) {
        if (initSize <= 0) {
            throw new IllegalArgumentException("Parametro menor o igual a cero");
        }

        items = new Entry[initSize];
    }

    /**
     * Metodo que se encarga de calcular un numero primo, tal que:
     * &nbsp;&nbsp;&nbsp;&nbsp 1) Garantize un tamanio de la tabla lo
     * suficientemente grande como para mantener un factor de carga por debajo
     * de 0.50. &nbsp;&nbsp;&nbsp;&nbsp 2) Garantize que sea por lo menos un
     * impar. (<i>Aclaracion: este puede ser primo o no</i>>)
     *
     * @param <code>initSize</code> tamanio a calcular
     * @return <code>int</code> con el tamanio ya calculado y verificado (<i>es
     * impar y es primo</i>)
     */
    private int primeCalculator(int initSize) {
        int cal = (6 * initSize) + 1;

        if (isPrime(cal)) {
            return cal;
        } else {
            return nextPrime(cal);
        }
    }

    /**
     * Metodo que tiene como objetivo calcular el siguiente primo, antes
     * verificando si el numero pasado como parametro es par, convirtiendolo en
     * impar.
     *
     * @param <code>size</code> el numero a calcular.
     * @return <code>int</code> con el numero ya calculado.
     */
    private int nextPrime(int size) {
        if (size % 2 == 0) {
            return size++;
        }

        for (; isPrime(size); size += 2);
        return size;
    }

    /**
     * Metodo que tiene como funcion verificar si el numero pasado como
     * parametro es o no primo. <br> Este algoritmo esta basado en el test de
     * divisibilidad, que consiste en verificar los divisores impares
     * comprendidos entre 3 y <i>raiz de n</i>, siendo <i>n</i> el numero a
     * verificar.
     *
     * @param <code>size</code> el numero que sera sometido a dicho test.
     * @return <code>true</code> si es primo.<br> <code>false</code> en caso
     * contrario.
     */
    private boolean isPrime(int size) {
        for (long i = 3; i * i <= size; i += 2) {
            if (size % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Metodo que retorna el tamanio de la tabla.
     *
     * @return el tamanio de la tabla.
     * @deprecated Este metodo solo es usado para fines de control.
     */
    public int getLength() {
        return items.length;
    }

    /**
     * Metodo que retorna la cantidad de elementos que se encuentran actualmente
     * en la tabla. <br> <i>Aclaracion: este atributo cuenta todas aquellos
     * casilleros que estan ocupados, sin hacer distincion de su estado
     * <b>Activos o No Activos</b></i>
     *
     * @return la cantidad de elementos que se encuentran en la tabla.
     * @deprecated Este metodo solo es usado para fines de control.
     */
    public int getOcupation() {
        return currentOcupation;
    }

    /**
     * Metodo que tiene como objetivo verificar si la tabla contiene entradas
     * cargadas.
     *
     * @return true si la tabla esta vacia. <br> false en caso contrario.
     */
    public boolean isEmpty() {
        return (currentOcupation == 0) ? true : false;
    }

    /**
     * Metodo que tiene como fin colocar todos los casilleros de la tabla de
     * hash en null (vacios). <br> O dicho de otra forma, borrar todos los
     * elementos cargados.
     */
    public void clear() {
        currentOcupation = 0;
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }
    }

    /**
     * Metodo que tiene como finalidad calcular la posicion, en la cual un
     * objeto sera colocado dentro de la tabla, tal que tome, del objeto pasado
     * como parametro, su codigo de hash (<i><b>hashCode</b> metodo viene
     * heredado de Object que se sobrescribe</i>) y retorne un indice valido
     * para la tabla que tiene una capacidad de m objetos.<br> Implementa un
     * algoritmo que calcula el resto de la division entre <i>la clave del
     * objeto tomado como parametro</i> y <i> el tamanio de la tabla</i>,
     * retornando indices validos entre 0 y lenght - 1.
     *
     * @param value objeto al que se le calculara un indice valido en la tabla,
     * para poder insertarlo.
     * @return el indice en el que dicho objeto sera colocado.
     */
    private int hashFunction(E value) {
        int valueHashCode = value.hashCode() % items.length;

        if (valueHashCode < 0) {
            valueHashCode += items.length;
        }

        return valueHashCode;
    }

    /**
     * Sobrecarga del metodo
     * <code>hashFunction</code>.<br> En este caso la funcion calcula el indice
     * de la tabla segun una llave que es pasada como parametro.
     *
     * @param key la llave a calcular la posicion dentro de la tabla.
     * @return el indice de la tabla que representa dicha llave.
     */
    private int hashFunction(K key) {
        int valueHashCode = key.hashCode() % items.length;

        if (valueHashCode < 0) {
            valueHashCode += items.length;
        }

        return valueHashCode;
    }

    /**
     * Metodo que tiene como objetivo recorrer la tabla de hash. <br> Implementa
     * el algoritmo de exploracion cuadratica; solucion que tiende a eliminar el
     * problema de la agrupacion primaria dentro de la tabla. <br> Se basa en;
     * primero calcular la posicion dentro de la tabla, mediante la funcion de
     * hash. Luego recorre, en forma cuadratica preguntando si el casillero es
     * nulo o si el objeto que se pasa como parametro ya se encuentra en la
     * tabla. En el caso de que alguna de las condiciones se cumpla, corta el
     * ciclo y devuelve la ultima posicion computada.
     *
     * @param x Elemento generico al cual se analizara, si esta o no en la tabla
     * @return int con la ultima posicion computada.
     */
    private int finder(E x) {
        int offset = 1;
        int currentPos = hashFunction(x);

        while (items[currentPos] != null
                && !items[currentPos].value.equals(x)) {
            currentPos += offset;
            offset += 2;
            if (currentPos >= items.length) {
                currentPos -= items.length;
            }
        }
        return currentPos;
    }

    /**
     * Sobrecarga del metodo
     * <code>finder</code>. <br> En este caso el metodo intentara encontrar
     * aquella entrada que posea la misma llave que ha sido ingresada como
     * parametro. Por el modo de preceder es similar al otro metodo, salvo que
     * aqui se preguntan si las llaves son o no iguales.
     *
     * @param key Llave generica al cual se analizara, si esta o no en la tabla
     * @return
     */
    private int finder(K key) {
        int offset = 1;
        int currentPos = hashFunction(key);

        while (items[currentPos] != null
                && !items[currentPos].key.equals(key)) {
            currentPos += offset;
            offset += 2;
            if (currentPos >= items.length) {
                currentPos -= items.length;
            }
        }
        return currentPos;
    }

    /**
     * Metodo que tiene como objetivo verificar el estado del casillero en la
     * tabla hash. <br> Verifica si la posicion actual en la tabla no es nula
     * (es decir no esta vacia), o si el objeto que se encuentra esta o no
     * activo.
     *
     * @param current posicion actual en la tabla hash.
     * @return true en el caso de que la casilla este ocupada y que el objeto
     * que se encuentra esta activo. <i>return</i> false en cualquier otro caso.
     */
    private boolean stateChecker(int current) {
        return items[current] != null && items[current].state;
    }

    /**
     * Metodo que tiene como objetivo verificar si el objeto que se pasa como
     * parametro se encuentra en la tabla de hash. <br> Utiliza el metodo
     * <i><b>finder</b></i> para realizar el sondeo o la exploracion en forma
     * cuadratica sobre la tabla, y luego se verifica si esta o no en la tabla
     * con el metodo <i><b>stateChecker</b></i>.
     *
     * @param x el objeto o elemento a buscar en la tabla.
     * @return true si lo encontro.<br> false en caso contrario.
     * @throws EmptyHashTableException si la tabla esta vacia.
     */
    public boolean contains(E x) throws EmptyHashTableException {
        if (currentOcupation == 0) {
            throw new EmptyHashTableException("Tabla de dispersion vacia");
        }

        int current = finder(x);
        return stateChecker(current);
    }

    /**
     * Metodo que tiene como objetivo verificar si la llave colocada como
     * parametro referencia a un lugar y objeto valido dentro de la tabla.<br>
     * Su funcionamiento es similar a la de
     * <code>contains</code> pero sobre una llave.
     *
     * @param key la cual se verificara.
     * @return <code>true</code> si el lugar y objeto son correctos.<br>
     * <code>false</code> en caso contrario.
     * @throws EmptyHashTableException
     */
    public boolean containsKey(K key) throws EmptyHashTableException {
        if (currentOcupation == 0) {
            throw new EmptyHashTableException("Tabla de dispersion vacia");
        }

        int current = finder(key);
        return stateChecker(current);
    }

    /**
     * Metodo que tiene como objetivo eliminar un objeto de la tabla. <br> De
     * nuevo aqui se utiliza el metodo
     * <code>finder</code> para realizar la busqueda de la posicion en la tabla
     * de hash, luego con el metodo <i><b>stateChecker</b></i> se verifica si el
     * elemento a remover se encuentra o no en la tabla. Si esta se utiliza
     * marcado logico para eliminarlo de la tabla.
     *
     * @param <code>x</code> El elemento a eliminar de la tabla.
     * @throws EmptyHashTableException Si la tabla esta vacia.
     * @throws InvalidEntryException si el elemento a eliminar no se encuentra
     * en la tabla.
     */
    public void remove(E x) throws EmptyHashTableException, InvalidEntryException {
        if (currentOcupation == 0) {
            throw new EmptyHashTableException("Tabla de dispersion vacia");
        }

        int current = finder(x);

        if (stateChecker(current)) {
            items[current].state = false;
        } else {
            throw new InvalidEntryException("El elemento que desea eliminar no se encuentra disponible");
        }
    }

    /**
     * Metodo que tiene como objetivo eliminar un objeto de la tabla, segun una
     * cierta llave de busqueda pasada como parametro. <br> De nuevo aqui se
     * utiliza el metodo
     * <code>finder</code> para realizar la busqueda de la posicion en la tabla
     * de hash, luego con el metodo
     * <code>stateChecker</code> se verifica si el elemento a remover se
     * encuentra o no en la tabla. Si esta se utiliza marcado logico para
     * eliminarlo de la tabla.
     *
     * @param <code>key</code> la llave de busqueda que sera utilizada para
     * eliminar un objeto de la tabla.
     * @throws EmptyHashTableException Si la tabla esta vacia.
     * @throws InvalidEntryException si el elemento a eliminar no se encuentra
     * en la tabla.
     */
    public void removeFromKey(K key) throws EmptyHashTableException, InvalidEntryException {
        if (currentOcupation == 0) {
            throw new EmptyHashTableException("Tabla de dispersion vacia");
        }

        int current = finder(key);

        if (stateChecker(current)) {
            items[current].state = false;
        } else {
            throw new InvalidEntryException("El elemento que desea eliminar no se encuentra disponible");
        }
    }

    /**
     * Metodo que tiene como objetivo verificar el factor de carga de la tabla.
     *
     * @return true si el cator de carga actual sobrepasa al preestablecido por
     * esta misma clase. <br> false es caso contrario.
     */
    private boolean ocupationChecker() {
        return ((currentOcupation / (float) items.length) >= FACTOR) ? true : false;
    }

    /**
     * Metodo que tiene como objetivo colocar un nuevo objeto en una entrada en
     * la tabla de hash. <br> Primero verifica si el objeto que se quiere
     * colocar en la tabla ya se encuentra en la tabla. En el caso de no
     * encontrarse en la tabla se crea una nueva entrada en la tabla y se coloca
     * el objeto en la posicion indicada por <i><b>finder</b></i>, ademas de
     * aumentar en uno la ocupacion actual. <br> Por ultimo se verifica si el
     * factor de carga excede el factor por defecto de esta tabla. De ser asi se
     * llama al metodo <i><b>rehash</b></i>.
     *
     * @param x Elemento a insertar en la tabla.
     * @return true si la operacion se hizo con exito.<br> false si el objeto ya
     * se encontraba en la tabla.
     */
    public boolean put(E x, K k) {
        int currentPos = finder(x);

        if (stateChecker(currentPos)) {
            return false;
        }

        items[currentPos] = new Entry<E, K>(x, k, true);
        test = k;
        currentOcupation++;

        if (ocupationChecker()) {
            rehash();
        }

        return true;
    }

    public boolean putByKey(E x, K k) {
        int currentPos = finder(k);

        if (stateChecker(currentPos)) {
            return false;
        }

        items[currentPos] = new Entry<E, K>(x, k, true);
        test = k;
        currentOcupation++;

        if (ocupationChecker()) {
            rehash();
        }

        return true;
    }

    /**
     * Metodo que tiene como objetivo crear una nueva tabla da hash, a partir de
     * la ya existente. <br> Este metodo es llamado <i>si y solo si el factor de
     * carga actual supera al indicado por esta clase</i>. <br> Para crear la
     * tabla se pasa como argumento a <i><b>primeCalculator</b></i> la capacidad
     * de la tabla actual X2, luego crea el nuevo arreglo, para finalmente
     * cargar los objetos que estaban en la tabla vieja a la nueva tabla.
     */
    private void rehash() {
        Entry<E, K>[] oldItems = items;

        int newLength = primeCalculator(2 * oldItems.length);

        allocateItems(primeCalculator(newLength));
        currentOcupation = 0;

        for (int i = 0; i < oldItems.length; i++) {
            if (oldItems[i] != null && oldItems[i].state) {
                put(oldItems[i].value, oldItems[i].key);
            }
        }
    }

    /**
     * Metodo similar al <i><b>contains</b></i>, con la diferencia que en vez de
     * devolver un valor booleano, devuelve el objeto encontrado.
     *
     * @param x es el elemento a buscar.
     * @return E que es el objeto encontrado.
     * @throws InvalidEntryException si el elemento a buscar no se encuentra en
     * la tabla.
     * @throws EmptyHashTableException si la lista esta vacia.
     */
    public E search(E x) throws InvalidEntryException, EmptyHashTableException {
        if (currentOcupation == 0) {
            throw new EmptyHashTableException("Tabla de dispersion vacia");
        }

        int current = finder(x);

        if (items[current] == null || items[current].state == false) {
            throw new InvalidEntryException("El elemento que se desea buscar no se encuentra disponible");
        }

        return items[current].value;
    }

    /**
     * Metodo que tiene como objetivo ubicar dentro de la tabla, un objeto,
     * segun una llave de busqueda ingresada como parametro.
     *
     * @param key la llave del objeto a buscar.
     * @return E que es el objeto encontrado.
     * @throws InvalidEntryException si el elemento a buscar no se encuentra en
     * la tabla.
     * @throws EmptyHashTableException si la lista esta vacia.
     */
    public E get(K key) throws EmptyHashTableException, InvalidEntryException {
        if (currentOcupation == 0) {
            throw new EmptyHashTableException("Tabla de dispersion vacia");
        }

        int current = finder(key);

        if (items[current] == null || items[current].state == false) {
            throw new InvalidEntryException("El elemento que se desea buscar no se encuentra disponible");
        }

        return items[current].value;
    }

    /**
     * Metodo que tiene como objetivo colocar todas las entradas activas de la
     * tabla en un iterador.
     *
     * @return LocalIterator<E> con todas las entradas activas.
     */
    public LocalIterator<E> listView() {
        SimpleList<E> result = new SimpleList<E>();

        for (int i = 0; i < items.length; i++) {
            if (stateChecker(i)) {
                result.addLast(items[i].value);
            }
        }

        return result.getIterador();
    }

    /**
     * Metodo que tiene como objetivo colocar todas las llaves activas de la
     * tabla en un arreglo de llaves de busqueda.
     *
     * @return K[] arreglo que representa a las llaves activas de la tabla.
     */
    public K[] keys() {
        K[] keys = (K[]) Array.newInstance(test.getClass(), currentOcupation);
        int j = 0;

        for (int i = 0; i < items.length; i++) {
            if (stateChecker(i)) {
                keys[j] = items[i].key;
                j++;
            }
        }

        return keys;
    }

    /**
     * Metodo que tiene como objetivo devolver un arbol binario AVL con todas
     * las entradas activas de la tabla.
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

        for (int i = 0; i < items.length; i++) {
            if (stateChecker(i)) {
                tree.add(items[i].value);
            }
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

        for (int i = 0; i < items.length; i++) {
            if (stateChecker(i)) {
                tree.add(items[i].value, c);
            }
        }

        return tree;
    }

    /**
     * Clase que representa a una entrada en la tabla hash. <br> Las entradas
     * que representa esta clase poseen dos estados: <br> &nbsp&nbsp&nbsp&nbsp
     * 1) <i>state = true</i> si la entrada en la tabla esta activa. <br>
     * &nbsp&nbsp&nbsp&nbsp 2) <i>state = false</i> si la entrada en la tabla no
     * esta activa, es decir a sido borrada en forma logica. <br> Cada entrada
     * <i>solo contendra uno y solo un objeto</i>.
     *
     * @param <E> tipo de datos que contendra las entradas de la tabla.
     * @param <K> tipo de dato de la llave de busqueda.
     */
    private final class Entry<E extends Comparable, K> implements Serializable {

        /**
         * Atributo que almacena al objeto en la tabla.
         */
        private E value;
        /**
         * Indica el estado actual en el que se encuentra la entrada actual.
         * <br> <b>Activo (true)</b> si contiene el objeto y todavia no ha sido
         * borrado. <br> <b>No Activo (false)</b> si contiene el objeto y fue
         * borrado de la lista en forma logica.
         */
        private boolean state;
        /**
         * Atributo que representa el valor de la clave de busqueda.
         */
        private K key;

        /**
         * Constructor con un solo parametro. Este indica que por defecto la
         * entrada se encuentra activa.
         *
         * @param v con el objeto a almacenar en la entrada.
         */
        public Entry(E v, K k) {
            this(v, k, true);
        }

        /**
         * Constructo con dos parametros, con la diferencia que el estado se
         * pasa como parametro.
         *
         * @param v
         * @param s
         */
        public Entry(E v, K k, boolean s) {
            value = v;
            state = s;
            key = k;
        }

        /**
         * Metodo toString personalizado.
         *
         * @return String con los datos de la entrada.
         */
        @Override
        public String toString() {
            return value.toString();
        }
    }

    /**
     * Metodo tostring personalizado.
     *
     * @return String con el contenido de la tabla de hash.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < items.length; i++) {
            if (stateChecker(i)) {
                builder.append(items[i].value.toString()).append("------").append(i);
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}
