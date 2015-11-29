/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manosinstructionsimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author jameswingo
 */
public class ManosInstructionSimulator {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {


SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                //try Pixel dots
                try {
                    new ManosGUI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }            
            }
        });
   

    }
    

}
