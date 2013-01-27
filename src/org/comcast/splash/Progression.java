/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.splash;



import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import org.comcast.visual.Main;

/**
 *
 * @author Fox_Nightmare
 */
public class Progression extends Thread {

    private JProgressBar progreso;
    private Presentation pres;

    public Progression(JProgressBar jp, Presentation p) {
        progreso = jp;
        pres = p;
    }

    public void run() {
        int numero = 0;
        int total = 0;
        Random rand = new Random(System.nanoTime());

        while (total < 100) {

            if (total != 100) {
                progreso.setString(total + "%");
                progreso.setValue(total);
            } else {
                progreso.setString(100 + "%");
                progreso.setValue(100);
            }

            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Progression.class.getName()).log(Level.SEVERE, null, ex);
            }

            numero = rand.nextInt(14) + 1;
            total += numero;
        }

        if (total >= 100) {
            Main v = new Main();
            v.main(new String[]{});
            pres.setVisible(false);
        }
    }
}
