package org.comcast.strategy;

import java.io.IOException;
import java.net.SocketException;
import java.text.Collator;
import java.util.Comparator;
import org.comcast.exceptions.FTPConectionRefusedException;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.router.RouterRetrieve;
import org.comcast.structures.SimpleList;

/**
 * Clase parte del patron strategy, el cual se encarga de listar todos los
 * archivos, ordenandolos segun el tipo de archivo.
 *
 * @author Damian Bruera
 * @version 2.0
 * @since Java 7
 */
public class FileTypeListing implements ListingStrategy {

    private int orderType;
    private RouterRetrieve results;

    public FileTypeListing(ServerConfig c, int orderType) {
        this.orderType = orderType;
        results = new RouterRetrieve(c);
    }

    @Override
    public Message[] listLocalMessages(String pathName) {
        try {
            SimpleList<Message> localMessages = results.getLocalMessages(pathName);
            Message[] unsorted = localMessages.toArray(Message.class);

            Message[] sorted = localMessages.quickSort(unsorted, new FileTypeComparator());

            return sorted;

        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public Message[] listRemoteFiles(String pathName) {
        try {
            SimpleList<Message> localMessages = results.getSimpleListCurrent(pathName);
            Message[] unsorted = localMessages.toArray(Message.class);

            Message[] sorted = localMessages.quickSort(unsorted, new FileTypeComparator());

            return sorted;

        } catch (SocketException | FTPConectionRefusedException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public String[] getRemoteDirectories(String pathName) {
        try {
            return results.getDirNamesCurrent(pathName);
        } catch (IOException | FTPConectionRefusedException ex) {
            return null;
        }
    }

    private class FileTypeComparator implements Comparator<Message> {

        private Collator textComparator = Collator.getInstance();

        @Override
        public int compare(Message o1, Message o2) {

            return (orderType == ListingStrategy.ASC) ? textComparator.compare(o1.getFileType(), o2.getFileType())
                    : textComparator.compare(o2.getFileType(), o1.getFileType());
        }
    }
}
