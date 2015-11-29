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
    
    
    //collection of all data lines accociated with the registers not all lines arem\
    //used in every register. This is dependent on the register. To optimize seperate objects should
    //be made however this configuration allows for flexibilty if other options are added to the 
    //register in the future 
    private DataLine ld = new DataLine();
    private DataLine inr = new DataLine();
    private DataLine clr = new DataLine();
    private DataLine write = new DataLine();
    private DataLine read = new DataLine();
    private DataLine in = new DataLine();
    private DataLine out = new DataLine();
    private DataLine address = new DataLine();
    private DataLine addressInter = new DataLine();
    
    //dataline for bus 
    private DataLine busInputLine = new DataLine();
    private DataLine busOutputLine = new DataLine();
    
    //integers for inputdata 
    private short inputData;
    private short outputData;
    private short currentData;
    
    //location for drawing the text data locations
    private Point inputPixelLocation;
    private Point outputPixelLocation;
    private Point currentPixelLocation;

    

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

    /**
     * @return the in
     */
    public DataLine getIn() {
        return in;
    }

    /**
     * @param in the in to set
     */
    public void setIn(DataLine in) {
        this.in = in;
    }

    /**
     * @return the out
     */
    public DataLine getOut() {
        return out;
    }

    /**
     * @param out the out to set
     */
    public void setOut(DataLine out) {
        this.out = out;
    }

    /**
     * @return the address
     */
    public DataLine getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(DataLine address) {
        this.address = address;
    }

    /**
     * @return the addressIntermediate
     */
    public DataLine getAddressInter() {
        return addressInter;
    }

    /**
     * @param addressIntermediate the addressIntermediate to set
     */
    public void setAddressInter(DataLine addressIntermediate) {
        this.addressInter = addressIntermediate;
    }

    /**
     * @return the inputData
     */
    public short getInputData() {
        return inputData;
    }

    /**
     * @param inputData the inputData to set
     */
    public void setInputData(short inputData) {
        this.inputData = inputData;
    }

    /**
     * @return the outputData
     */
    public short getOutputData() {
        return outputData;
    }

    /**
     * @param outputData the outputData to set
     */
    public void setOutputData(short outputData) {
        this.outputData = outputData;
    }

    /**
     * @return the inputPixelLocation
     */
    public Point getInputPixelLocation() {
        return inputPixelLocation;
    }

    /**
     * @param inputPixelLocation the inputPixelLocation to set
     */
    public void setInputPixelLocation(Point inputPixelLocation) {
        this.inputPixelLocation = inputPixelLocation;
    }

    /**
     * @return the outputPixelLocation
     */
    public Point getOutputPixelLocation() {
        return outputPixelLocation;
    }

    /**
     * @param outputPixelLocation the outputPixelLocation to set
     */
    public void setOutputPixelLocation(Point outputPixelLocation) {
        this.outputPixelLocation = outputPixelLocation;
    }

    
        /**
     * @param outputPixelLocation the outputPixelLocation to set
     */
    public void setCurrentPixelLocation(Point currentPixelLocation) {
        this.currentPixelLocation = currentPixelLocation;
    }
    
    public Point getCurrentPixelLocation() {
        return currentPixelLocation;
    }


    /**
     * @return the currentData
     */
    public short getCurrentData() {
        return currentData;
    }

    /**
     * @param currentData the currentData to set
     */
    public void setCurrentData(short currentData) {
        this.currentData = currentData;
    }

    /**
     * @return the busInputLine
     */
    public DataLine getBusInputLine() {
        return busInputLine;
    }

    /**
     * @param busInputLine the busInputLine to set
     */
    public void setBusInputLine(DataLine busInputLine) {
        this.busInputLine = busInputLine;
    }

    /**
     * @return the busOutputLine
     */
    public DataLine getBusOutputLine() {
        return busOutputLine;
    }

    /**
     * @param busOutputLine the busOutputLine to set
     */
    public void setBusOutputLine(DataLine busOutputLine) {
        this.busOutputLine = busOutputLine;
    }
}
