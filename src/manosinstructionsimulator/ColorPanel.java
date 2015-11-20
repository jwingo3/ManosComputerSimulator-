/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manosinstructionsimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import static java.awt.Color.RED;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author jameswingo
 */


//a customized color panel class used to draw animations on image. Not general 
///this is because there isn't a check to draw off the picture


class ColorPanel extends JPanel {

    
    final int BOXDIM = 4;
    final int LEFT = 1;
    final int RIGHT = 2;
    final int UP = 3;
    final int DOWN = 4;
    
    //global variable to tell if something is being drawn
    //this is not mutexed locked so it is up to the programer to only call one 
    //draw function at a 
    boolean drawing = false;
    
    //global so that functions can see where the last pixel was drawn
    public int dataLineX;
    public int dataLineY;

    
    private BufferedImage bimg;
    private Dimension dims;
    Timer timer = new Timer(5, null);
    


    public ColorPanel(BufferedImage image) {
        bimg = image;
        dims = new Dimension(bimg.getWidth(), bimg.getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bimg, 0, 0, null);
    }

    //this method will allow the changing of image
    public void setBufferedImage(BufferedImage newImg) {
        bimg = newImg;
        repaint();
    }

    //ths method will colour a pixel red//code isn't used just a proof of concept 
    public boolean drawDot(int x, int y) {

        if (x > dims.getHeight() || y > dims.getWidth()) {
            return false;
        }

        bimg.setRGB(x, y,  0xff0000);//Red

        repaint();
        return true;
    }
    
    public boolean drawBox(int x, int y) {

        if (x > dims.getHeight() || y > dims.getWidth()) {
            
        dataLineX=x;
        dataLineY=y;
               
        System.out.println("drawBox " +x+ " "+ y);
                    return false;

        }

        Graphics2D g = bimg.createGraphics();//Red
        g.setColor(RED);
        g.fillRect(x, y, BOXDIM, BOXDIM);
                System.out.println("drawBox " +dataLineX+ " "+ dataLineY);

        repaint();
        return true;
    }
     
    
    //animates a line at size BOXDIM given a start index and a direction using 
    //the constansts found in this file 
    //input should be changed to use a dataLine object 
    Boolean animateLine(int startX, int startY,int endX,int endY){
        //create timer to color random pixels
        System.out.println("startPic"+ startX+ "  " +startY);

        timer.setCoalesce(false);
        
        timer.addActionListener(new AbstractAction(){

            int dataLineX=startX;
            int dataLineY=startY;
            @Override
            public void actionPerformed(ActionEvent ae) {
                
               

                if( dataLineX<(endX)){
                    if(drawBox(dataLineX,dataLineY)){
                        dataLineX++;
                    }
                }else if(dataLineX>(endX)){
                    if(drawBox(dataLineX,dataLineY)){
                        dataLineX--;
                    }
                }else if(dataLineY<(endY)){
                    if(drawBox(dataLineX,dataLineY)){
                        dataLineY++;
                    }                    
                }else if(dataLineY>(endY)){
                    if(drawBox(dataLineX,dataLineY)){
                        dataLineY--;
                    }                    
                }
                else{
                    timer.removeActionListener(this);
                    timer.stop();
                    


                    System.out.println("stopped");
                    
                    //change drawing mutex
                    drawing=false;
                    return;

                }
                

            }
        });

        if(!timer.isRunning())
            timer.start();
        
        return true;

        
    }
    

    Boolean animateLineSeqence(ArrayList<DataLine> inArray){
               timer.setCoalesce(false);

        animateLine(inArray.get(0).getsX(),inArray.get(0).getsY(),inArray.get(0).geteX(),inArray.get(0).geteY());
        
        animateLine(inArray.get(1).getsX(),inArray.get(1).getsY(),inArray.get(1).geteX(),inArray.get(1).geteY());
            
       
        
        return true;
    }


    @Override
    public Dimension getPreferredSize() {
        return dims;
    }
}


