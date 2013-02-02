package org.comcast.proxy;

import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.comcast.builder.Client;
import org.comcast.builder.Mail;
import org.comcast.crypto.Crypto;
import org.comcast.crypto.CryptoData;
import org.comcast.crypto.CryptoProvider;
import org.comcast.logic.DateScheduler;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.schedulers.InputScheduler;
import org.comcast.schedulers.OutputScheduler;
import org.comcast.structures.BinaryHeap;
import org.comcast.structures.LocalIterator;
import org.comcast.structures.SimpleList;
import org.comcast.xml.Loader;
import org.comcast.xml.LoaderProvider;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Clase que representa el nucleo del programa.<br> Esta clase realiza las
 * siguientes tareas: <br> 1) Recibe una coleccion de archivos a transferir y
 * los encripta. <br> 2) Dichos archivos encriptados se colocan en una cola de
 * prioridad. <br> 3) Luego transfiere todos los archivos en la cola de
 * prioridad.<br> 4) Descarga los archivos del servidor ftp. Esta peticion de
 * descarga se encuentra tambien en una cola de prioridad. <br> 5) Luego se pasa
 * a la tarea de desencriptar los archivos descargados. 6) La forma de trabajo
 * se basa en tareas programadas.<br> 7) Ademas es el encargado de manejar los
 * nombres de los archivos encriptados. <br> 8) Los archivos que ya fueron
 * subidos se guardan en un archivo xml. <br> 9) Este programa no admite que se
 * carguen al servidor dos veces el mismo archivo.
 *
 * @author Bruera Damian
 * @version 2.1
 * @since 2012.
 */
public class Works implements InterfaceWorks {

    private ResourceBundle works_es_ES;
    private Loader loader;
    private Crypto crypto;
    private OutputScheduler os;
    private InputScheduler is;
    private Client client;

    /**
     * Constructor de la clase.
     */
    public Works() {
        try {
            this.loader = LoaderProvider.getInstance();
            this.crypto = CryptoProvider.getInstance();
            this.client = loader.getClientConfiguration();

            switch (client.getLocalization()) {
                case "Español":
                    this.works_es_ES = ResourceBundle.getBundle("org/comcast/locale/Works_es_ES");
                    break;
                case "Ingles":
                    this.works_es_ES = ResourceBundle.getBundle("org/comcast/locale/Works_en_US");
                    break;
                default:
                    this.works_es_ES = ResourceBundle.getBundle("org/comcast/locale/Works_en_US");
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Este metodo se encarga de crear un objeto CryptoData con la
     * configuracion, para un archivo en particular.<br> Entre otras cosas
     * define el nombre que tendran los archivos de clave publica y privada.<br>
     * Setea la configuracion de encriptacion para el archivo indicado.<br> El
     * nombre del archivo publico y privado, se conforma con el nombre del
     * archivo a encriptar y la hora del sistema en nano segundos.
     *
     * @param full El archivo a encriptar.
     * @param serialID Un numero que indica el tiempo en nano sgundos.
     * @return La configuracion de encriptacion para un archivo.
     */
    private CryptoData stringParts(Message full, long serialID) {
        String partida = full.getLocalPath();
        String general = "key" + serialID;

        String aux = partida.substring(partida.lastIndexOf("\\") + 1, partida.lastIndexOf("."));
        String particion = partida.substring(0, partida.lastIndexOf("\\") + 1);
        String ex = partida.substring(partida.lastIndexOf(".") + 1, partida.length());

        String pn = general + ".public";
        String pv = general + ".private";
        String cryp = aux + ".ftp";

        String publicName = client.getPublicStorage() + pn;
        String privateName = client.getPrivateStorage() + pv;
        String cryptoFile = particion + cryp;

        String[] args = new String[8];
        args[0] = aux; //FILE NAME
        args[1] = partida; //ORIGINAL
        args[2] = cryptoFile; //CRYPTOFILE
        args[3] = publicName; //PUBLIC KEY
        args[4] = privateName; //PRIVATE KEY
        args[5] = ex; //EXTENSION
        args[6] = full.getRemotePath() + cryp; //DESTINATION

        CryptoData data = new CryptoData(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);

        return data;
    }

    /**
     * Este metodo verifica si el archivo que se esta intentando subir al
     * servidor ftp ya existe.
     *
     * @param remoteDestination Con la ruta al archivo remoto.
     * @return True si ya existe o False en caso contrario.
     * @throws Exception Si algo falla.
     */
    private boolean isLoadable(String remoteDestination) throws Exception {

        CryptoData data = loader.getCryptoData(remoteDestination);

        return (data != null) ? false : true;
    }

    /**
     * Este metodo prepara los archivos para ser encriptados. Verifica si los
     * archivos ya existen en el servidor.
     *
     * @param plainFiles Los archivos nativos (sin encriptar).
     * @param serialID Un numero que se agrega para diferenciar los archivos de
     * claves entre si.
     * @return Los archivos ya preparados para su encriptacion.
     * @throws Exception Si algo falla.
     */
    private Properties getEncryptedFiles(SimpleList<Message> plainFiles, long serialID) throws Exception {

        LocalIterator<Message> iter = plainFiles.getIterador();
        SimpleList<Message> encrypted = new SimpleList<>();
        SimpleList<CryptoData> cf = new SimpleList<>();

        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();
            CryptoData cd = stringParts(aux, serialID);

            if (isLoadable(cd.getDestination())) {
                Message transfer = new Message(aux.getSource(), aux.getPriority(), cd.getCryptoFile(), cd.getDestination());
                transfer.setFtpFile(aux.getFtpFile());
                encrypted.addInOrder(transfer);
                cf.addInOrder(cd);
            } else {
                String primero = works_es_ES.getString("EL ARCHIVO YA ES ENCUENTRA EN EL SERVIDOR.");
                String segundo = works_es_ES.getString("ARCHIVO DUPLICADO");
                JOptionPane.showMessageDialog(null, primero + "\n" + aux.getLocalFile().getName(), segundo, JOptionPane.WARNING_MESSAGE);
            }
        }

        Properties props = new Properties();
        props.put("encrypted", encrypted);
        props.put("data", cf);

        return props;
    }

    /**
     * Metodo encargado de realizar la encriptacion de los archivos.<br>
     * "Recordatorio: Utiliza dos listas enlazadas para emparejar los archivos
     * nativos y los encriptados. Correspondencia entre los dos".
     *
     * @param plainFiles Los archivos a encriptar.
     * @return Los archivos encriptados.
     * @throws Exception Por si algo falla.
     */
    private SimpleList<Message> encryptFiles(SimpleList<Message> plainFiles) throws Exception {

        long serialID = System.nanoTime();
        Properties props = getEncryptedFiles(plainFiles, serialID);

        @SuppressWarnings("unchecked")
        SimpleList<Message> encrypted = (SimpleList<Message>) props.get("encrypted");

        @SuppressWarnings("unchecked")
        SimpleList<CryptoData> data = (SimpleList<CryptoData>) props.get("data");
        int i = 0;

        LocalIterator<CryptoData> dataIter = data.getIterador();

        i += encrypted.size();
        crypto.keyGenerateRSA("C:\\Key\\key" + serialID + ".public", "C:\\Key\\key" + serialID + ".private", i);

        while (dataIter.hasMoreElements()) {
            boolean flagOrigi = false;
            boolean flagCrypto = false;

            CryptoData aux = dataIter.returnElement();
            Message ori = null;
            Message cry = null;

            LocalIterator<Message> encryIter = encrypted.getIterador();
            LocalIterator<Message> origiIter = plainFiles.getIterador();

            while (origiIter.hasMoreElements()) {
                ori = origiIter.returnElement();

                if (aux.getOriginal().compareToIgnoreCase(ori.getLocalPath()) == 0) {
                    flagOrigi = true;
                    break;
                }
            }

            while (encryIter.hasMoreElements()) {
                cry = encryIter.returnElement();

                if (aux.getCryptoFile().compareToIgnoreCase(cry.getLocalPath()) == 0) {
                    flagCrypto = true;
                    break;
                }
            }

            if (flagCrypto && flagOrigi) {
                loader.appendCryptoData(aux);
                crypto.encryptRSA(ori.getLocalPath(), cry.getLocalPath(), aux.getPublicKey());
            } else {
                JOptionPane.showMessageDialog(null, works_es_ES.getString("ERROR DE ENCRIPTACION"), works_es_ES.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
            }
        }

        return encrypted;
    }

    /**
     * Este metodo se encarga de colocar los archivos en una cola de prioridad,
     * ademas de programar la tarea de subida de archivos.
     *
     * @param toTransfer Son los archivos a transferir
     * @param date Fecha y hora de la realizacion de la tarea.
     * @throws Exception Por si algo falla.
     */
    @Override
    public void transferFiles(SimpleList<Message> toTransfer, DateScheduler date) throws Exception {

        SimpleList<Message> ready = encryptFiles(toTransfer);
        BinaryHeap<Message> heap = new BinaryHeap<>();

        LocalIterator<Message> iter = ready.getIterador();

        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();

            heap.insert(aux);
        }

        ServerConfig config = loader.getServerConfiguration();
        Mail m = loader.getMail();
        SchedulerFactory sf = new StdSchedulerFactory();

        os = new OutputScheduler(config, heap, m);
        os.setScheduler(sf.getScheduler());
        os.setDateScheduler(date);

        os.startJob();
        os.stopJob();
    }

    /**
     * Metodo que se encarga de recibir una coleccion de archivos para descargar
     * desde el servidor, y colocarlos en una cola de prioridad.<br> Ademas es
     * encargado de programar la tarea de descarga.
     *
     * @param toDownload Todos los archivos a descargar.
     * @param date Fecha y hora de la realizacion de la tarea.
     * @throws Exception Por si algo falla.
     */
    @Override
    public void downloadFiles(SimpleList<Message> toDownload, DateScheduler date) throws Exception {

        BinaryHeap<Message> heap = new BinaryHeap<>();

        LocalIterator<Message> iter = toDownload.getIterador();

        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();

            heap.insert(aux);
        }

        ServerConfig config = loader.getServerConfiguration();
        Mail m = loader.getMail();
        SchedulerFactory sf = new StdSchedulerFactory();

        is = new InputScheduler(config, heap, m);
        is.setScheduler(sf.getScheduler());
        is.setDateScheduler(date);

        is.startJob();
        is.stopJob();
    }

    /**
     * Metodo public que se encarga de procesar uno por uno los archivos que ya
     * fueron descargados desde el servidor. <br> Etso archivos llegan aqui en
     * una lista enlazada.
     *
     * @param toDownload Con los archivos ya descargados.
     * @throws Exception Por si algo falla.
     */
    @Override
    public void decryptFiles(SimpleList<Message> toDownload) throws Exception {

        LocalIterator<Message> iter = toDownload.getIterador();
        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();

            decrypt(aux);
        }
    }

    /**
     * Metodo que se encarga de llamar al desencriptador por cada uno de los
     * archivos.
     *
     * @param toDecrypt Con el archivo a desencriptar.
     * @throws Exception Por si algo falla.
     */
    private void decrypt(Message toDecrypt) throws Exception {
        String toNative = prepareStrings(toDecrypt);
        CryptoData data = loader.getCryptoData(toDecrypt.getRemotePath());

        if (toNative != null && data != null) {
            crypto.decryptRSA(toDecrypt.getLocalPath(), toNative, data.getPrivateKey());
        } else {
            throw new Exception(works_es_ES.getString("ERROR EN EL DESENCRIPTADO"));
        }
    }

    /**
     * Metodo encargado de armar la ruta absoluta donde se almacenara el archivo
     * original.
     *
     * @param toPrepare El archivo a desencriptar.
     * @return String con la ruta absoluta.
     * @throws Exception Por si algo falla.
     */
    private String prepareStrings(Message toPrepare) throws Exception {
        CryptoData data = loader.getCryptoData(toPrepare.getRemotePath());
        String finalMessage = null;

        if (data != null) {
            String partida = toPrepare.getLocalPath();
            String download = partida.substring(0, partida.lastIndexOf("\\") + 1);
            String file = data.getFileName() + "." + data.getOriginalExtension();

            finalMessage = download + file;
        } else {
            throw new Exception(works_es_ES.getString("ERROR EN LOS STRING DE DESCARGA"));
        }

        return finalMessage;
    }
}
