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
public class RegisterBlock {
    
    private DataLine ld = new DataLine();
    private DataLine inr = new DataLine();
    private DataLine clr = new DataLine();
    private DataLine write = new DataLine();
    private DataLine read = new DataLine();
    
    public RegisterBlock(){
        
    }

    /**
     * @return the ld
     */
    public DataLine getLd() {
        return ld;
    }

    /**
     * @param ld the ld to set
     */
    public void setLd(DataLine ld) {
        this.ld = ld;
    }

    /**
     * @return the inr
     */
    public DataLine getInr() {
        return inr;
    }

    /**
     * @param inr the inr to set
     */
    public void setInr(DataLine inr) {
        this.inr = inr;
    }

    /**
     * @return the clr
     */
    public DataLine getClr() {
        return clr;
    }

    /**
     * @param clr the clr to set
     */
    public void setClr(DataLine clr) {
        this.clr = clr;
    }

    /**
     * @return the write
     */
    public DataLine getWrite() {
        return write;
    }

    /**
     * @param write the write to set
     */
    public void setWrite(DataLine write) {
        this.write = write;
    }

    /**
     * @return the read
     */
    public DataLine getRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(DataLine read) {
        this.read = read;
    }
}
