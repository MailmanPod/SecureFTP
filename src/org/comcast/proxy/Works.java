/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.proxy;

import java.util.Properties;
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
 *
 * @author Quality of Service
 */
public class Works implements InterfaceWorks {

    private Loader loader;
    private Crypto crypto;
    private OutputScheduler os;
    private InputScheduler is;
    private Client client;

    public Works() {
        try {
            this.loader = LoaderProvider.getInstance();
            this.crypto = CryptoProvider.getInstance();
            this.client = loader.getClientConfiguration();
        } catch (Exception ex) {
        }
    }

    private CryptoData stringParts(Message full) {
        String partida = full.getLocalPath();
        String general = "key" + 1;

        String aux = partida.substring(partida.lastIndexOf("\\") + 1, partida.indexOf("."));
        String particion = partida.substring(0, partida.lastIndexOf("\\") + 1);
        String ex = partida.substring(partida.lastIndexOf(".") + 1, partida.length());

        String pn = general + ".public";
        String pv = general + ".private";
        String cryp = aux + ".crypto";

//        "C:\\Key\\" 
        String publicName = client.getPublicStorage() + pn;
        String privateName = client.getPrivateStorage() + pv;
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

    private boolean isLoadable(String remoteDestination) throws Exception {

        CryptoData data = loader.getCryptoData(remoteDestination);

        return (data != null) ? false : true;
    }

    private Properties getEncryptedFiles(SimpleList<Message> plainFiles) throws Exception {

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

    private SimpleList<Message> encryptFiles(SimpleList<Message> plainFiles) throws Exception {

        Properties props = getEncryptedFiles(plainFiles);
        SimpleList<Message> encrypted = (SimpleList<Message>) props.get("encrypted");
        SimpleList<CryptoData> data = (SimpleList<CryptoData>) props.get("data");
        int i = 0;

        LocalIterator<CryptoData> dataIter = data.getIterador();

        i += encrypted.size();
        crypto.keyGenerateRSA("C:\\Key\\key" + 1 + ".public", "C:\\Key\\key" + 1 + ".private", i);

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

        os = new OutputScheduler(config, heap, m);
        os.setScheduler(sf.getScheduler());
        os.setDateScheduler(date);

        os.start();
    }

    public boolean isTaskAlive() {
        try {
            if (os.isAlive() || is.isAlive()) {
                return true;
            }
        } catch (NullPointerException e) {
        }

        return false;
    }

    public void cancelTask() {
        try {
            if (os.isAlive() || is.isAlive()) {
                os.interrupt();
            }
        } catch (NullPointerException ex) {
        }
    }

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

        is.start();
    }

    public void decryptFiles(SimpleList<Message> toDownload) throws Exception {

        LocalIterator<Message> iter = toDownload.getIterador();
        while (iter.hasMoreElements()) {
            System.out.println("Previo a desencriptar");
            Message aux = iter.returnElement();

            decrypt(aux);
        }
    }

    private void decrypt(Message toDecrypt) throws Exception {
        String toNative = prepareStrings(toDecrypt);
        CryptoData data = loader.getCryptoData(toDecrypt.getRemotePath());

        if (toNative != null && data != null) {
            System.out.println("Desencriptando");
            System.out.println(toDecrypt.getLocalPath());
            System.out.println(toNative);
            System.out.println(data.getPrivateKey());
            System.out.println("\n\n");
            crypto.decryptRSA(toDecrypt.getLocalPath(), toNative, data.getPrivateKey());
        } else {
            System.out.println("Error");
        }
    }

    private String prepareStrings(Message toPrepare) throws Exception {
        CryptoData data = loader.getCryptoData(toPrepare.getRemotePath());
        String finalMessage = null;

        if (data != null) {
            String partida = toPrepare.getLocalPath();
            String download = partida.substring(0, partida.lastIndexOf("\\") + 1);
            String file = data.getFileName() + "." + data.getOriginalExtension();

            finalMessage = download + file;
        } else {
            System.out.println("Error");
        }

        return finalMessage;
    }
}
