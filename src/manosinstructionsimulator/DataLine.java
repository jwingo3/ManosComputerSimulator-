/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manosinstructionsimulator;

/**
 *
 * @author jameswingo
 */
public class DataLine {
    
     private int sX;
     private int sY;
     private int eX;
     private int eY;
     
    public DataLine(int startPixelX, int startPixelY, int endPixelX, int endPixelY){
           sX= startPixelX;
           sY = startPixelY;
           eX= endPixelX;
           eY = endPixelY;
    }

    
    public void setDataLine(int startPixelX, int startPixelY, int endPixelX, int endPixelY){
           sX= startPixelX;
           sY = startPixelY;
           eX= endPixelX;
           eY = endPixelY;
    }
    /**
     * @return the sX
     */
    public int getsX() {
        return sX;
    }

    /**
     * @param sX the sX to set
     */
    public void setsX(int sX) {
        this.sX = sX;
    }

    /**
     * @return the sY
     */
    public int getsY() {
        return sY;
    }

    /**
     * @param sY the sY to set
     */
    public void setsY(int sY) {
        this.sY = sY;
    }

    /**
     * @return the eX
     */
    public int geteX() {
        return eX;
    }

    /**
     * @param eX the eX to set
     */
    public void seteX(int eX) {
        this.eX = eX;
    }

    /**
     * @return the eY
     */
    public int geteY() {
        return eY;
    }

    /**
     * @param eY the eY to set
     */
    public void seteY(int eY) {
        this.eY = eY;
    }




    
    
}
