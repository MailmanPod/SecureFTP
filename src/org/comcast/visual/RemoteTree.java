/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.visual;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.strategy.FileListing;
import org.comcast.strategy.ListingStrategy;
import org.comcast.strategy.NameListing;
import org.comcast.tableModels.RemoteFileTableModel;
import org.comcast.xml.Loader;
import org.comcast.xml.LoaderProvider;

/**
 *
 * @author Quality of Service
 */
public class RemoteTree extends javax.swing.JFrame {

    private Loader loader;
    private ServerConfig config;
    private DefaultTreeModel model;
    private JLabel lbl;
    private JTable remote;

    /**
     * Creates new form RemoteTree
     */
    public RemoteTree(JLabel lbl, JTable r) {
        initComponents();
        initElements();
        centrarPantalla();
        setImageIconFrame();
        this.lbl = lbl;
        this.remote = r;
    }

    private void centrarPantalla() {
        Dimension tamFrame = this.getSize();//para obtener las dimensiones del frame
        Dimension tamPantalla = Toolkit.getDefaultToolkit().getScreenSize();//para obtener el tamaño de la pantalla
        setLocation((tamPantalla.width - tamFrame.width) / 2, (tamPantalla.height - tamFrame.height) / 2);//para posicionar
    }

    private void setImageIconFrame() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("../images/pendrive_32x32.png")));
    }

    private void initElements() {
        try {
            loader = LoaderProvider.getInstance();
            config = loader.getServerConfiguration();
            model = new DefaultTreeModel((TreeNode) this.treeRemote.getModel().getRoot());
            this.treeRemote.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        treeRemote = new javax.swing.JTree();

        setTitle("Carpetas Remotas");
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setResizable(false);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("/");
        treeRemote.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeRemote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeRemoteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(treeRemote);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//    private String buildPath(DefaultMutableTreeNode node){
//        
//    }
    private String pathName;

    private void pathCalculator() {
        DefaultMutableTreeNode current = (DefaultMutableTreeNode) treeRemote.getLastSelectedPathComponent();
        String dir = (String) current.getUserObject();

        if (current.getChildCount() == 0) {
            System.out.println("Es un nodo terminal");
            if (!current.getUserObject().equals("/")) {
                TreeNode[] path = current.getPath();
                int i = 0;

                for (TreeNode aux : path) {
                    DefaultMutableTreeNode no = (DefaultMutableTreeNode) aux;
                    if (i == 0) {
                        pathName = (String) no.getUserObject();
                        i++;
                    } else {
                        pathName += (String) no.getUserObject() + "/";
                    }
                }
            } else {
                pathName = dir;
            }

//            System.out.println("PAthNAme: " + pathName);
            this.lbl.setText(pathName);
            initRemoteTable(pathName);

            FileListing list = new FileListing();
            list.setListingStrategy(new NameListing(config, ListingStrategy.ASC));
            String[] remoteDirectories = list.getRemoteDirectories(pathName);
            int i = 0;

            for (String aux : remoteDirectories) {
                DefaultMutableTreeNode insert = new DefaultMutableTreeNode(aux);
                this.model.insertNodeInto(insert, current, i);
                i++;
            }
        } else {
            System.out.println("No es un nodo terminal");
            if (!current.getUserObject().equals("/")) {
                TreeNode[] path = current.getPath();
                int i = 0;

                for (TreeNode aux : path) {
                    DefaultMutableTreeNode no = (DefaultMutableTreeNode) aux;
                    if (i == 0) {
                        pathName = (String) no.getUserObject();
                        i++;
                    } else {
                        pathName += (String) no.getUserObject() + "/";
                    }
                }
            } else {
                pathName = dir;
            }
            this.lbl.setText(pathName);
            initRemoteTable(pathName);
        }
    }

    private void initRemoteTable(String pathName) {
        try {
            FileListing list = new FileListing();
            list.setListingStrategy(new NameListing(config, ListingStrategy.ASC));
            Message[] localMessage = list.getRemoteMessages(pathName);
            this.remote.setModel(new RemoteFileTableModel(localMessage));
        } catch (Exception ex) {
        }
    }

    private void treeRemoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeRemoteMouseClicked

        if (evt.getButton() == 1 && evt.getClickCount() == 2) {
            pathCalculator();
        }
    }//GEN-LAST:event_treeRemoteMouseClicked
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(RemoteTree.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(RemoteTree.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(RemoteTree.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(RemoteTree.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new RemoteTree().setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree treeRemote;
    // End of variables declaration//GEN-END:variables
}