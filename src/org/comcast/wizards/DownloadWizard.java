/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.wizards;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.comcast.builder.Client;
import org.comcast.builder.Mail;
import org.comcast.crypto.CryptoData;
import org.comcast.logic.DateScheduler;
import org.comcast.logic.ServerConfig;
import org.comcast.proxy.DecryptHandler;
import org.comcast.proxy.DownloadHandler;
import org.comcast.proxy.InterfaceWorks;
import org.comcast.proxy.UploadHandler;
import org.comcast.proxy.Works;
import org.comcast.router.Message;
import org.comcast.schedulers.InputScheduler;
import org.comcast.structures.LocalIterator;
import org.comcast.structures.SimpleList;
import org.comcast.xml.Loader;
import org.comcast.xml.LoaderProvider;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.netbeans.spi.wizard.ResultProgressHandle;
import org.netbeans.spi.wizard.Summary;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardController;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPanelProvider;

/**
 *
 * @author Quality of Service
 */
public class DownloadWizard {

    public void main(Map args) {
        final Map properties = args;
        Runnable r;
        r = new Runnable() {
            @Override
            public void run() {
                try {
                    Client x = LoaderProvider.getInstance().getClientConfiguration();
                    properties.put("destination", x.getDownloadPath());

                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    WizardDownload mp = new WizardDownload();
                    Wizard wizard = mp.createWizard();
                    Object result;
                    result = WizardDisplayer.showWizard(wizard,
                            new Rectangle(100, 100,
                            1000, 600), null, properties);
                    System.out.println("Result = " + result);
                    //             System.exit (0);
                } catch (Exception ex) {
                    Logger.getLogger(DownloadWizard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        EventQueue.invokeLater(r);
    }

    class WizardDownload extends WizardPanelProvider {

        WizardDownload() {
            super("Asistente para la descarga de archivos",
                    new String[]{"bienvenido", "servidor", "cliente", "mail", "archivosDescarga", "destino"},
                    new String[]{"Bienvenido", "Verificar Servidor", "Configuracion Cliente", "Verificar Mail", "Archivos a Descargar", "Origen Remoto"});
        }

        private CryptoData stringParts(Message full, long serialID) throws Exception {
            Client client = LoaderProvider.getInstance().getClientConfiguration();
            String partida = full.getLocalPath();
            System.out.println(partida);
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

        private boolean isLoadable(String remoteDestination) throws Exception {

            CryptoData data = LoaderProvider.getInstance().getCryptoData(remoteDestination);

            return (data != null) ? false : true;
        }

        @Override
        protected JComponent createPanel(final WizardController wc, String string, final Map map) {

            final Loader l = LoaderProvider.getInstance();

            switch (string) {
                case "bienvenido":

                    JLabel lbl = new JLabel();
                    JLabel lbl2 = new JLabel();
                    lbl.setText("Bienvenido al asistente para la descarga de archivos.");
                    lbl2.setText("Presione next para empezar");

                    JPanel pn1 = new JPanel();
                    pn1.add(lbl);

                    JPanel pn2 = new JPanel();
                    pn2.add(lbl2);

                    JPanel bienvenido = new JPanel();
                    bienvenido.setLayout(new GridLayout(2, 10));
                    bienvenido.add(pn1, 0);
                    bienvenido.add(pn2, 1);
                    return bienvenido;

                case "servidor":
                    final JCheckBox chServer = new JCheckBox();
                    final JCheckBox chUser = new JCheckBox();
                    final JCheckBox chIP = new JCheckBox();
                    final JPanel servidor = new JPanel();

                    try {
                        wc.setProblem("Debe Verificar los datos");

                        ServerConfig config = l.getServerConfiguration();
                        JLabel server = new JLabel("Nombre Servidor");
                        JLabel user = new JLabel("Nombre Usuario");
                        JLabel ip = new JLabel("Numero Ip");

                        JTextField txtServer = new JTextField(40);
                        txtServer.setEnabled(false);
                        txtServer.setText(config.getHostName());

                        JTextField txtUser = new JTextField(40);
                        txtUser.setEnabled(false);
                        txtUser.setText(config.getUserLogin());

                        JTextField txtIP = new JTextField(40);
                        txtIP.setEnabled(false);
                        txtIP.setText(config.getIpAddress());

                        JButton validar = new JButton("Validar");
                        validar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (chServer.isSelected() && chUser.isSelected() && chIP.isSelected()) {
                                    wc.setProblem(null);
                                } else {
                                    wc.setProblem("Debe Verificar los datos");
                                }
                            }
                        });

                        JPanel one = new JPanel();
                        one.add(server);
                        one.add(txtServer);
                        one.add(chServer);

                        JPanel two = new JPanel();
                        two.add(user);
                        two.add(txtUser);
                        two.add(chUser);

                        JPanel three = new JPanel();
                        three.add(ip);
                        three.add(txtIP);
                        three.add(chIP);

                        JPanel four = new JPanel();
                        four.add(validar);

                        servidor.setLayout(new GridLayout(4, 1));
                        servidor.add(one);
                        servidor.add(two);
                        servidor.add(three);
                        servidor.add(four);

                        return servidor;

                    } catch (Exception ex) {
                        wc.setProblem("Se ha producido un error " + ex.toString());
                        chUser.setEnabled(false);
                        chServer.setEnabled(false);
                        chIP.setEnabled(false);
                        return servidor;
                    }

                case "cliente":

                    final JCheckBox chLocale = new JCheckBox();
                    final JCheckBox chPublic = new JCheckBox();
                    final JCheckBox chPrivate = new JCheckBox();
                    final JPanel cliente = new JPanel();

                    try {
                        wc.setProblem("Debe Verificar los datos");

                        Client c = l.getClientConfiguration();

                        JLabel locale = new JLabel("Descarga");
                        JLabel publicStorage = new JLabel("Directorio de llave publica");
                        JLabel privateStorage = new JLabel("Directorio de llave privada");

                        JTextField txtLocale = new JTextField(40);
                        txtLocale.setEnabled(false);
                        txtLocale.setText(c.getDownloadPath());

                        JTextField txtPublic = new JTextField(40);
                        txtPublic.setEnabled(false);
                        txtPublic.setText(c.getPublicStorage());

                        JTextField txtPrivate = new JTextField(40);
                        txtPrivate.setEnabled(false);
                        txtPrivate.setText(c.getPrivateStorage());

                        JButton validar = new JButton("Validar");
                        validar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (chLocale.isSelected() && chPublic.isSelected() && chPrivate.isSelected()) {
                                    wc.setProblem(null);
                                } else {
                                    wc.setProblem("Debe Verificar los datos");
                                }
                            }
                        });
                        JPanel one = new JPanel();
                        one.add(locale);
                        one.add(txtLocale);
                        one.add(chLocale);

                        JPanel two = new JPanel();
                        two.add(publicStorage);
                        two.add(txtPublic);
                        two.add(chPublic);

                        JPanel three = new JPanel();
                        three.add(privateStorage);
                        three.add(txtPrivate);
                        three.add(chPrivate);

                        JPanel four = new JPanel();
                        four.add(validar);

                        cliente.setLayout(new GridLayout(4, 1));
                        cliente.add(one);
                        cliente.add(two);
                        cliente.add(three);
                        cliente.add(four);

                        return cliente;

                    } catch (Exception ex) {
                        wc.setProblem("Se ha producido un error " + ex.toString());
                        chLocale.setEnabled(false);
                        chPrivate.setEnabled(false);
                        chPublic.setEnabled(false);
                        return cliente;
                    }

                case "mail":
                    final JCheckBox chHost = new JCheckBox();
                    final JCheckBox chTLS = new JCheckBox();
                    final JCheckBox chPort = new JCheckBox();
                    final JCheckBox chAuthentication = new JCheckBox();
                    final JPanel mail = new JPanel();
                    try {
                        wc.setProblem("Debe Verificar los datos");

                        Mail mailC = l.getMail();

                        JLabel host = new JLabel("SMTP Host");
                        JLabel tls = new JLabel("Uso de TLS");
                        JLabel port = new JLabel("Puerto de servicio SMNP");
                        JLabel autenti = new JLabel("Usuario");

                        JTextField txtHost = new JTextField(40);
                        txtHost.setEnabled(false);
                        txtHost.setText(mailC.getProperties().getProperty("mail.smtp.host"));

                        JTextField txtTLS = new JTextField(40);
                        txtTLS.setEnabled(false);
                        txtTLS.setText(mailC.getProperties().getProperty("mail.smtp.starttls.enable"));

                        JTextField txtPort = new JTextField(40);
                        txtPort.setEnabled(false);
                        txtPort.setText(mailC.getProperties().getProperty("mail.smtp.port"));

                        JTextField txtAut = new JTextField(40);
                        txtAut.setEnabled(false);
                        txtAut.setText(mailC.getMailUserName());

                        JButton validar = new JButton("Validar");
                        validar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (chHost.isSelected() && chTLS.isSelected() && chPort.isSelected() && chAuthentication.isSelected()) {
                                    wc.setProblem(null);
                                } else {
                                    wc.setProblem("Debe Verificar los datos");
                                }
                            }
                        });
                        JPanel one = new JPanel();
                        one.add(host);
                        one.add(txtHost);
                        one.add(chHost);

                        JPanel two = new JPanel();
                        two.add(tls);
                        two.add(txtTLS);
                        two.add(chTLS);

                        JPanel three = new JPanel();
                        three.add(port);
                        three.add(txtPort);
                        three.add(chPort);

                        JPanel four = new JPanel();
                        four.add(autenti);
                        four.add(txtAut);
                        four.add(chAuthentication);

                        JPanel five = new JPanel();
                        five.add(validar);


                        mail.setLayout(new GridLayout(5, 1));
                        mail.add(one);
                        mail.add(two);
                        mail.add(three);
                        mail.add(four);
                        mail.add(five);

                        return mail;

                    } catch (Exception ex) {
                        wc.setProblem("Se ha producido un error " + ex.toString());
                        chHost.setEnabled(false);
                        chTLS.setEnabled(false);
                        chPort.setEnabled(false);
                        chAuthentication.setEnabled(false);
                        return mail;
                    }

                case "archivosDescarga":
                    JPanel archivosSubir = new JPanel();
                    archivosSubir.setLayout(new GridLayout(4, 1));
                    JTextArea area = new JTextArea();
                    area.setAutoscrolls(true);
                    area.setEditable(false);
                    JScrollPane kk = new JScrollPane(area);
                    StringBuilder builder = new StringBuilder();
                    try {
//                    wc.setProblem("Debe Verificar los datos");
                        final Message[] selectedItems = (Message[]) map.get("selectedFiles");
                        JButton finalizar = new JButton("Finalizar Modificaciones");

                        if (selectedItems != null) {
                            for (int i = 0; i < selectedItems.length; i++) {
                                Message aux = selectedItems[i];
                                CryptoData cd = stringParts(aux, System.nanoTime());
                                System.out.println(aux);
                                System.out.println(cd);

                                if (isLoadable(aux.getRemotePath())) {
                                    wc.setProblem("El archivo " + aux.getLocalFile().getName() + " no es un archivo encriptado ");
                                    finalizar.setEnabled(false);
                                    break;
                                } else {
                                    builder.append(area.getText()).append("\n" + "Archivo #").append(i + 1).append(": ").append(aux.getRemotePath());
                                    wc.setProblem("No ha finalizado las modificaciones");
                                }
                            }
                        } else {
                            builder.append("No hay archivos seleccionados");
                            wc.setProblem("No hay archivos seleccionados");
                        }
                        JLabel p = new JLabel("Asignar Prioridades a los archivos");
                        final JComboBox archivos = getArchivos(selectedItems);
                        final JComboBox prioridades = getPrioridades();
                        JButton añadir = new JButton("Añadir");
                        añadir.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Integer sel = (Integer) archivos.getSelectedItem();
                                int prio = prioridades.getSelectedIndex();

                                System.out.println("Antes: " + selectedItems[sel - 1].getPriority());

                                selectedItems[sel - 1].setPriority(prio + 1);

                                System.out.println("Despues: " + selectedItems[sel - 1].getPriority());
                                System.out.println("\n");

                                JOptionPane.showMessageDialog(null,
                                        "Archivo: " + selectedItems[sel - 1].getLocalPath() + "\n cambio de prioridad a \n" + prioridades.getSelectedItem(),
                                        "Cambios", JOptionPane.INFORMATION_MESSAGE);
                            }
                        });
                        
                        finalizar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                map.put("newItems", selectedItems);
                                wc.setProblem(null);
                            }
                        });

                        JPanel pin = new JPanel();
                        pin.add(finalizar);

                        JPanel panel = new JPanel();
                        panel.add(archivos);
                        panel.add(prioridades);
                        panel.add(añadir);

                        JPanel one = new JPanel();
                        area.setText(builder.toString());
                        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(one);
                        one.setLayout(jPanel9Layout);
                        jPanel9Layout.setHorizontalGroup(
                                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(kk, javax.swing.GroupLayout.Alignment.TRAILING));
                        jPanel9Layout.setVerticalGroup(
                                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(kk, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE));

                        archivosSubir.add(one);
                        archivosSubir.add(p);
                        archivosSubir.add(panel);
                        archivosSubir.add(finalizar);

                        return archivosSubir;

                    } catch (Exception ex) {
                        wc.setProblem("Problema al cargar los archivos: " + ex.toString());
                        ex.printStackTrace();
                        return archivosSubir;
                    }

                case "destino":
                    wc.setProblem("Ruta local no valida o fecha no valida");
                    JLabel desc = new JLabel("Ubicacion local elegida: ");
                    final JLabel selec = new JLabel();

                    JButton ingresar = new JButton("....");
                    ingresar.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String get = (String) map.get("destination");

                            if (get == null || get.equalsIgnoreCase("")) {
                                JOptionPane.showMessageDialog(null, "Debe seleccionar una ruta local valida", "Ruta local", JOptionPane.WARNING_MESSAGE);
                                wc.setProblem("Debe seleccionar una ruta local valida");
                            } else {
                                selec.setText(get);
//                            wc.setProblem(null);
                            }
                        }
                    });

                    JLabel tarea = new JLabel("DD/MM/YYYY HH:MM");
                    JLabel titulo = new JLabel("Configurar Tarea");
                    titulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

                    final JComboBox dias = getDias();
                    final JComboBox meses = getMeses();
                    final JTextField años = new JTextField("2012");
                    años.setEnabled(false);

                    final JComboBox hora = getHoras();
                    final JComboBox minutos = getMinutos();
                    final JTextField segundos = new JTextField("00");
                    segundos.setEnabled(false);

                    JButton buildScheduler = new JButton("Armar Horario");
                    buildScheduler.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                Integer dia = (Integer) dias.getSelectedItem();
                                Integer mes = (Integer) meses.getSelectedItem();
                                int año = Integer.parseInt(años.getText());

                                Integer hor = (Integer) hora.getSelectedItem();
                                Integer min = (Integer) minutos.getSelectedItem();
                                int seg = Integer.parseInt(segundos.getText());

                                DateScheduler date = new DateScheduler(hor, min, seg, dia, mes, año);
                                Date runTime = org.quartz.DateBuilder.dateOf(date.getHour(), date.getMinute(), date.getSecond(),
                                        date.getDay(), date.getMonth(), date.getYear());
                                long t = (runTime.getTime() - System.currentTimeMillis());

                                if (t < 0) {
                                    wc.setProblem("La fecha seleccionada debe ser mayor a la fecha del sistema");
                                } else {
                                    wc.setProblem(null);
                                    map.put("dateScheduler", date);
                                    JOptionPane.showMessageDialog(null, "La tarea fue programada para ejecutarse en: \n" + runTime, "Tarea Subir Archivo", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } catch (Exception ex) {
                                wc.setProblem("La fecha seleccionada debe ser valida");
                            }
                        }
                    });

                    JPanel fecha = new JPanel();
                    fecha.add(dias);
                    fecha.add(meses);
                    fecha.add(años);

                    JPanel horario = new JPanel();
                    horario.add(hora);
                    horario.add(minutos);
                    horario.add(segundos);

                    JPanel one = new JPanel();
                    one.add(desc);

                    JPanel two = new JPanel();
                    two.add(selec);

                    JPanel three = new JPanel();
                    three.add(ingresar);

                    JPanel four = new JPanel();
                    four.add(tarea);

                    JPanel five = new JPanel();
                    five.add(buildScheduler);

                    JPanel intermedio1 = new JPanel();
                    intermedio1.setLayout(new GridLayout(1, 3));
                    intermedio1.add(one);
                    intermedio1.add(two);
                    intermedio1.add(three);

                    JPanel intermedio2 = new JPanel();
                    intermedio2.setLayout(new GridLayout(1, 4));
                    intermedio2.add(four);
                    intermedio2.add(fecha);
                    intermedio2.add(horario);
                    intermedio2.add(five);

                    JPanel destino = new JPanel();
                    destino.setLayout(new GridLayout(3, 1));
                    destino.add(intermedio1);
                    destino.add(new JPanel().add(titulo));
                    destino.add(intermedio2);

                    return destino;
            }

            return null;
        }

        private JComboBox getHoras() {
            Integer[] horas = new Integer[24];
            for (int i = 0; i < 24; i++) {
                horas[i] = i;
            }

            return new JComboBox(horas);
        }

        private JComboBox getMinutos() {
            Integer[] minutos = new Integer[60];
            for (int i = 0; i < 60; i++) {
                minutos[i] = i;
            }
            return new JComboBox(minutos);
        }

        private JComboBox getDias() {
            Integer[] dias = new Integer[31];
            for (int i = 1; i < 32; i++) {
                dias[i - 1] = i;
            }
            return new JComboBox(dias);
        }

        private JComboBox getMeses() {
            Integer[] dias = new Integer[12];
            for (int i = 1; i < 13; i++) {
                dias[i - 1] = i;
            }
            return new JComboBox(dias);
        }

        private JComboBox getArchivos(Message[] mess) {
            Integer[] dias = new Integer[mess.length];
            for (int i = 0; i < mess.length; i++) {
                dias[i] = i + 1;
            }
            return new JComboBox(dias);
        }

        private JComboBox getPrioridades() {
            String[] stgr = new String[]{"HIGH", "MEDIUM", "LOW"};
            return new JComboBox(stgr);
        }

        @Override
        protected Object finish(Map settings) throws WizardException {
            System.out.println("finish called");

//      return "String finishes abs";
            return new ResultDownload();

//      String [] items = new String [2];
//      items [0] = "Name = "+settings.get ("name");
//      items [1] = "Status = "+settings.get ("status");
//      return Summary.create (items, null);
        }
    }
}

class ResultDownload extends DeferredWizardResult {

    public ResultDownload() {
        // Uncomment the following line to make it possible to close the dialog
        // while the operation is running (abort the operation, in other words).
        // super (true);
    }

    @Override
    public void start(Map settings, ResultProgressHandle progress) {
        Message[] selectedItems = (Message[]) settings.get("newItems");
        SimpleList<Message> transfer = null;
        InterfaceWorks behind = null;
        InterfaceWorks dec = null;
        
        progress.setProgress("Preparar Archivos", 0, 4);
        try {
            for (Message aux : selectedItems) {
                String destination = (String) settings.get("destination");
                aux.setLocalPath(destination + aux.getFtpFile().getName());
            }

            Thread.sleep(3000);
        } catch (InterruptedException ie) {
        }

        progress.setProgress("Preparar tarea", 1, 4);
        try {
            InterfaceWorks w = new Works();
            behind = (InterfaceWorks) Proxy.newProxyInstance(w.getClass().getClassLoader(),
                    w.getClass().getInterfaces(), new DownloadHandler(w));
            
            dec = (InterfaceWorks) Proxy.newProxyInstance(w.getClass().getClassLoader(),
                    w.getClass().getInterfaces(), new DecryptHandler(w));

            SimpleList<Message> buffer = new SimpleList<>();
            transfer = buffer.toSimpleList(selectedItems);

            Thread.sleep(1000);
        } catch (InterruptedException ie) {
        }

        progress.setProgress("Descargar Archivos", 2, 4);
        try {

            DateScheduler date = (DateScheduler) settings.get("dateScheduler");
            Date runTime = org.quartz.DateBuilder.dateOf(date.getHour(), date.getMinute(), date.getSecond(),
                    date.getDay(), date.getMonth(), date.getYear());
            long l = runTime.getTime() - System.currentTimeMillis();
            System.out.println("++ " + transfer.get(0).getRemotePath() + "  " + transfer.get(0).getLocalPath());

            behind.downloadFiles(transfer, date);

            Thread.sleep(l + 10L);
        } catch (Exception ie) {
            ie.printStackTrace();
        }
        
        progress.setProgress("Desencriptar Archivos", 3, 4);
        try {

            DateScheduler date = (DateScheduler) settings.get("dateScheduler");
            Date runTime = org.quartz.DateBuilder.dateOf(date.getHour(), date.getMinute(), date.getSecond(),
                    date.getDay(), date.getMonth(), date.getYear());
            long l = runTime.getTime() - System.currentTimeMillis();
            System.out.println(l);

            dec.decryptFiles(transfer);

            Thread.sleep(2000);
        } catch (Exception ie) {
            ie.printStackTrace();
        }

        DateScheduler date = (DateScheduler) settings.get("dateScheduler");
        Date runTime = org.quartz.DateBuilder.dateOf(date.getHour(), date.getMinute(), date.getSecond(),
                date.getDay(), date.getMonth(), date.getYear());

        StringBuilder builder = new StringBuilder();
        LocalIterator<Message> iterador = transfer.getIterador();

        while (iterador.hasMoreElements()) {
            Message aux = iterador.returnElement();
            builder.append("\nArchivo Transferido: " + aux.getLocalFile());
        }

        String[] items = new String[2];
        items[0] = "Tarea Realizada: " + runTime;
        items[1] = builder.toString();

        // Replace null with an object reference to have this object returned
        // from the showWizard() method.

        progress.finished(Summary.create(items, null));
    }
}