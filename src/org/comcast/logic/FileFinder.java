package org.comcast.logic;

import java.io.IOException;
import java.net.SocketException;
import org.comcast.exceptions.EmptyHashTableException;
import org.comcast.exceptions.FTPConectionRefusedException;
import org.comcast.exceptions.InvalidEntryException;
import org.comcast.router.Message;
import org.comcast.router.RouterRetrieve;
import org.comcast.structures.LocalIterator;
import org.comcast.structures.OpenAdressingHashTable;
import org.comcast.structures.SimpleList;

/**
 * Clase que se encarga de realizar las busquedas de los archivos, tanto de
 * manera local, como remota.
 *
 * @author Damian Bruera.
 * @since 2012.
 * @version 2.0
 */
public class FileFinder {

    private RouterRetrieve results;

    /**
     * Constructor de la Clase.
     *
     * @param c Con la configuracion del servidor ftp.
     */
    public FileFinder(ServerConfig c) {
        results = new RouterRetrieve(c);
    }

    /**
     *
     * @param pathName
     * @return
     * @throws IOException
     */
    private OpenAdressingHashTable<Message, String> reloadLocal(String pathName) throws IOException {
        SimpleList<Message> localMessages = results.getLocalMessages(pathName);
        LocalIterator<Message> iter = localMessages.getIterador();

        OpenAdressingHashTable<Message, String> map = new OpenAdressingHashTable<>();

        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();
            map.putByKey(aux, aux.getLocalFile().getName());
        }

        return map;
    }

    public Message getLocalExactName(String pathName, String localFileName) {
        Message r = null;
        try {
            OpenAdressingHashTable<Message, String> reloadLocal = reloadLocal(pathName);

            r = reloadLocal.get(localFileName);
            return r;
        } catch (IOException | EmptyHashTableException | InvalidEntryException ex) {
            return null;
        }
    }

    public SimpleList<Message> getLocalNameAprox(String pathName, String localFileName) {
        SimpleList<Message> aprox = new SimpleList<>();

        try {
            SimpleList<Message> localMessages = results.getLocalMessages(pathName);
            LocalIterator<Message> iter = localMessages.getIterador();

            while (iter.hasMoreElements()) {
                Message aux = iter.returnElement();

                if (aux.getLocalFile().getName().startsWith(localFileName)) {
                    aprox.addLast(aux);
                }
            }

            return aprox;
        } catch (Exception ex) {
            return aprox;
        }
    }

    public SimpleList<Message> getLocalExtAprox(String pathName, String localFileName) {
        SimpleList<Message> aprox = new SimpleList<>();
        String ph = "." + localFileName;

        try {
            SimpleList<Message> localMessages = results.getLocalMessages(pathName);
            LocalIterator<Message> iter = localMessages.getIterador();

            while (iter.hasMoreElements()) {
                Message aux = iter.returnElement();

                if (aux.getLocalFile().getName().endsWith(ph)) {
                    aprox.addLast(aux);
                }
            }

            return aprox;
        } catch (Exception ex) {
            return aprox;
        }
    }

    private OpenAdressingHashTable<Message, String> reloadRemote(String pathName) throws IOException, SocketException, FTPConectionRefusedException {
        SimpleList<Message> localMessages = results.getSimpleListCurrent(pathName);
        LocalIterator<Message> iter = localMessages.getIterador();

        OpenAdressingHashTable<Message, String> map = new OpenAdressingHashTable<>();

        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();
            map.putByKey(aux, aux.getFtpFile().getName());
        }

        return map;
    }

    public Message getRemoteExactName(String pathName, String remoteFileName) {
        Message r = null;
        try {
            OpenAdressingHashTable<Message, String> reloadRemote = reloadRemote(pathName);

            r = reloadRemote.get(remoteFileName);
            return r;
        } catch (IOException | FTPConectionRefusedException | EmptyHashTableException | InvalidEntryException ex) {
            return null;
        }
    }

    public SimpleList<Message> getRemoteNameAprox(String pathName, String remoteFileName) {
        SimpleList<Message> aprox = new SimpleList<>();

        try {
            SimpleList<Message> remoteMessages = results.getSimpleListCurrent(pathName);
            LocalIterator<Message> iter = remoteMessages.getIterador();

            while (iter.hasMoreElements()) {
                Message aux = iter.returnElement();

                if (aux.getLocalFile().getName().startsWith(remoteFileName)) {
                    aprox.addLast(aux);
                }
            }

            return aprox;
        } catch (IOException | FTPConectionRefusedException ex) {
            return aprox;
        }
    }

    public SimpleList<Message> getRemoteExtAprox(String pathName, String remoteFileName) {
        SimpleList<Message> aprox = new SimpleList<>();
        String ph = "." + remoteFileName;

        try {
            SimpleList<Message> remoteMessages = results.getSimpleListCurrent(pathName);
            LocalIterator<Message> iter = remoteMessages.getIterador();

            while (iter.hasMoreElements()) {
                Message aux = iter.returnElement();

                if (aux.getLocalFile().getName().endsWith(ph)) {
                    aprox.addLast(aux);
                }
            }

            return aprox;
        } catch (IOException | FTPConectionRefusedException ex) {
            return aprox;
        }
    }
}
