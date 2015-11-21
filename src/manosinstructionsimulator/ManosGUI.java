/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manosinstructionsimulator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Color.RED;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ManosGUI {
    BufferedImage bi = ImageIO.read(new File("ManosComp.jpg"));
    final ColorPanel cPanel = new ColorPanel(bi);
    final Timer timer = new Timer(100, null);
    JPanel buttonPanel = new JPanel();
    JButton restartButton = new JButton("Restart");
    JButton startButton = new JButton("Start");

    public ManosGUI() throws Exception {

        JFrame frame = new JFrame();
        frame.setTitle("Manos Computer Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        initComponents(frame);
        
        frame.pack();
        frame.setVisible(true);
    }


    private void initComponents(JFrame frame) throws Exception {


        //Container cp = getContentPane();
        
        

        frame.add(cPanel,BorderLayout.WEST);
        buttonPanel.add(restartButton);
        buttonPanel.add(startButton);
        GridLayout layout = new GridLayout(0,2);

        buttonPanel.setLayout(layout);

        
        
        

        restartButton.addActionListener(new ButtonListener());
        startButton.addActionListener(new ButtonListener());
        
        
        

        frame.getContentPane().add(buttonPanel,BorderLayout.EAST);
        
        
        
        
        
        String[] description = { "Ebullient", "Obtuse", "Recalcitrant",
      "Brilliant", "Somnescent", "Timorous", "Florid", "Putrescent" };
        
        JComboBox c = new JComboBox();
        
           for (int i = 0; i < 4; i++)
                c.addItem(description[i++]);
           
        frame.getContentPane().add(c,BorderLayout.CENTER);



        
   
    }
    
    
    class ButtonListener implements ActionListener {
        ButtonListener() {}

        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            if (e.getActionCommand().equals("Restart")) {
                System.out.println("Restart has been clicked");
                try {
                    bi = ImageIO.read(new File("ManosComp.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(ManosGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            
                cPanel.resetTimer();
                cPanel.setBufferedImage(bi);
            
            }else if(e.getActionCommand().equals("Start")){
                ArrayList<DataLine> inputPoints = new ArrayList(2);
                //inputPoints.add(new DataLine(100, 100, 140, 100));
               // inputPoints.add(new DataLine(140, 100, 140, 150));

               // cPanel.animateLineSeqence(inputPoints);
                cPanel.animateDashedLine(new DataLine(358, 54, 452, 54),cPanel.RIGHT);
                cPanel.animateDashedLine(new DataLine(358, 80, 452, 80),cPanel.LEFT);

                cPanel.animateDashedLine(new DataLine(381, 164, 381, 618),cPanel.UP);
                                
                cPanel.animateDashedLine(new DataLine(371, 164, 371, 618),cPanel.DOWN);


                //cPanel.animateLine(260,200,260,400);        
               // cPanel.animateLine(260,200,240,400);        

            }
        }
    }
       

    
}


