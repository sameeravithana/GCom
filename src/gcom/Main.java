/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom;

import gui.GComWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author Praneeth
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            new GComWindow().setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
