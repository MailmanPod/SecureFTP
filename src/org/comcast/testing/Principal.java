/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.testing;

/**
 *
 * @author Quality of Service
 */
import java.io.File;


import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class Principal extends JFrame {

    JTree explorador;
    DefaultMutableTreeNode Raiz;

    public Principal() {

        Raiz = new DefaultMutableTreeNode("Raiz");
        explorador = new JTree(Raiz);
        DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) explorador.getCellRenderer();
//        render.setLeafIcon(new ImageIcon(this.getClass().getResource("../Imagenes/archivo.png")));
//        render.setOpenIcon(new ImageIcon(this.getClass().getResource("../Imagenes/carpeta.png")));
//        render.setClosedIcon(new ImageIcon(this.getClass().getResource("../Imagenes/carpeta.png")));
        explorador.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                try {
                    DefaultMutableTreeNode sel = (DefaultMutableTreeNode) explorador.getLastSelectedPathComponent();
                    //agregarHijos(sel);
                    for (int i = 0; i < sel.getChildCount(); i++) {
                        DefaultMutableTreeNode nieto = (DefaultMutableTreeNode) sel.getChildAt(i);
                        File fhijo = obtenerRuta(sel);
                        agregarHijos(nieto);
                    }
                } catch (NullPointerException npe) {
                }
            }
        });

        for (int i = 0; i < File.listRoots().length; i++) {
            DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(File.listRoots()[i]);
            Raiz.add(hijo);
        }
        add(new JScrollPane(explorador));
    }

    public void agregarHijos(DefaultMutableTreeNode padre) throws NullPointerException {
        if (padre != Raiz) {
            File fpadre = obtenerRuta(padre);
            if (fpadre.isDirectory()) {
                for (int i = 0; i < fpadre.list().length; i++) {
                    DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(fpadre.list()[i]);
                    padre.add(hijo);
                }
            }
        }
    }

    public File obtenerRuta(DefaultMutableTreeNode p) {
        String ruta = "";
        for (int i = 0; i < p.getPath().length - 1; i++) {
            ruta = ruta + p.getPath()[i + 1] + "\\";
        }
        File f = new File(ruta);
        return f;
    }

    public static void main(String[] args) {
        Principal p = new Principal();
        p.setVisible(true);
        p.setBounds(0, 0, 400, 400);
        p.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
