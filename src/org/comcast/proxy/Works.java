/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.proxy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.comcast.builder.Mail;
import org.comcast.crypto.Crypto;
import org.comcast.crypto.CryptoData;
import org.comcast.crypto.CryptoProvider;
import org.comcast.exceptions.EmptyHashTableException;
import org.comcast.exceptions.InvalidEntryException;
import org.comcast.exceptions.ListPointerOutOfBoundsException;
import org.comcast.logic.DateScheduler;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.schedulers.OutputScheduler;
import org.comcast.structures.BinaryHeap;
import org.comcast.structures.LocalIterator;
import org.comcast.structures.SimpleList;
import org.comcast.xml.Loader;
import org.comcast.xml.LoaderProvider;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Quality of Service
 */
public class Works implements InterfaceWorks {

    private Loader loader;
    private Crypto crypto;
    private OutputScheduler se;

    public Works() {
        this.loader = LoaderProvider.getInstance();
        this.crypto = CryptoProvider.getInstance();
    }

    private CryptoData stringParts(Message full) {
        String partida = full.getLocalPath();

        String aux = partida.substring(partida.lastIndexOf("\\") + 1, partida.indexOf("."));
        String particion = partida.substring(0, partida.lastIndexOf("\\") + 1);
        String ex = partida.substring(partida.lastIndexOf(".") + 1, partida.length());

        String pn = aux + ".public";
        String pv = aux + ".private";
        String cryp = aux + ".crypto";

        String publicName = "C:\\Key\\" + pn;
        String privateName = "C:\\Key\\" + pv;
        String cryptoFile = particion + cryp;

//        System.out.println("FileName: " + aux);
//        System.out.println("Public: " + publicName);
//        System.out.println("Private: " + privateName);
//        System.out.println("Original: " + partida);
//        System.out.println("CryptoFile: " + cryptoFile);
//        System.out.println("Extension: " + ex);
//        System.out.println("Destination: "+ full.getRemotePath()+ cryp);

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

    private boolean isLoadable(String remoteDestination) throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException, EmptyHashTableException, InvalidEntryException {

        CryptoData data = loader.getCryptoData(remoteDestination);

        return (data != null) ? false : true;
    }

    private Properties getEncryptedFiles(SimpleList<Message> plainFiles) throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException, EmptyHashTableException, InvalidEntryException {

        LocalIterator<Message> iter = plainFiles.getIterador();
        SimpleList<Message> encrypted = new SimpleList<>();
        SimpleList<CryptoData> cf = new SimpleList<>();

        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();
            CryptoData cd = stringParts(aux);

            if (isLoadable(cd.getDestination())) {
                Message transfer = new Message(aux.getSource(), aux.getPriority(), cd.getCryptoFile(), cd.getDestination(), aux.getEncapsulation());
                encrypted.addInOrder(transfer);
                cf.addInOrder(cd);
            } else {
                throw new UnsupportedOperationException("Aqui va un mensaje de error porque uno de los archivos ya se subio");
            }
        }

        Properties props = new Properties();
        props.put("encrypted", encrypted);
        props.put("data", cf);

        return props;
    }

    private SimpleList<Message> encryptFiles(SimpleList<Message> plainFiles) throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException, EmptyHashTableException,
            InvalidEntryException, ListPointerOutOfBoundsException, NoSuchAlgorithmException, ClassNotFoundException,
            NoSuchPaddingException, InvalidKeyException, GeneralSecurityException {

        Properties props = getEncryptedFiles(plainFiles);
        SimpleList<Message> encrypted = (SimpleList<Message>) props.get("encrypted");
        SimpleList<CryptoData> data = (SimpleList<CryptoData>) props.get("data");

        LocalIterator<CryptoData> dataIter = data.getIterador();

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
                System.out.println(ori.toString());
                System.out.println(cry.toString());
                System.out.println(aux.toString());
                System.out.println("\n\n\n");

                loader.appendCryptoData(aux);
                crypto.keyGenerateRSA(aux.getPublicKey(), aux.getPrivateKey());
                crypto.encryptRSA(ori.getLocalPath(), cry.getLocalPath(), aux.getPublicKey());
            } else {
                System.out.println("Error");
            }
        }

        return encrypted;
    }

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

        se = new OutputScheduler(config, heap, m);
        se.setScheduler(sf.getScheduler());
        se.setDateScheduler(date);

        se.start();
    }

    public boolean isTaskAlive() {
        try {
            if (se.isAlive()) {
                return true;
            }
        } catch (NullPointerException e) {
        }

        return false;
    }

    public void cancelTask() {
        try {
            if (se.isAlive()) {
                se.interrupt();
            }
        } catch (NullPointerException ex) {
        }
    }
}
