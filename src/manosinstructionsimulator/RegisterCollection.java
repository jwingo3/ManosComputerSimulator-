/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manosinstructionsimulator;

import java.util.ArrayList;

/**
 *
 * @author jameswingo
 */
public class RegisterCollection {
    //collection of all of the register blocks in manos computer
   private RegisterBlock memoryUnit = new RegisterBlock();
   private RegisterBlock ar = new RegisterBlock();
   private RegisterBlock pc = new RegisterBlock();
   private RegisterBlock dr = new RegisterBlock();
   private RegisterBlock ac = new RegisterBlock();
   private RegisterBlock ir = new RegisterBlock();
   private RegisterBlock tr = new RegisterBlock();
   private RegisterBlock outr = new RegisterBlock();
   
   //compund Lines
   ArrayList<DataLine> clock = new ArrayList();
   ArrayList<DataLine> adderDr = new ArrayList();
   ArrayList<DataLine> adderINPR = new ArrayList();
   ArrayList<DataLine> adderAc = new ArrayList();
   
   //single data Lines
   
   DataLine bottomOfBusLine = new DataLine(464,636,108,636);
   DataLine busInputLine = new DataLine(108,38,108,636);


   
   public RegisterCollection(){
       //memory unit
       memoryUnit.setOut(new DataLine(358, 54, 452, 54));
       memoryUnit.setIn(new DataLine(111, 59, 223, 59));
       memoryUnit.setRead(new DataLine(327, 86, 327, 101));
       memoryUnit.setWrite(new DataLine(264, 86, 264, 101));
       memoryUnit.setAddress(new DataLine(370,71,417,71));
       memoryUnit.setAddressInter(new DataLine(417,71,417,136));
       memoryUnit.setBusOutputLine(new DataLine(464,53,464,636));
       
       
       
       memoryUnit.setInputPixelLocation(new Point(360,100));
       memoryUnit.setOutputPixelLocation(new Point(490,63));
       
       memoryUnit.setInputData((short)32768);
       memoryUnit.setOutputData((short)32768);
       
       //AR
       ar.setOut(new DataLine(358, 137, 452, 137));
       ar.setIn(new DataLine(111, 137, 240, 137));
       ar.setLd(new DataLine(261, 149, 261, 164));
       ar.setInr(new DataLine(290, 149, 290, 164));
       ar.setClr(new DataLine(320, 149, 320, 164));
       ar.setBusOutputLine(new DataLine(464,137,464,636));
       
       
       ar.setCurrentPixelLocation(new Point(288,140));
       ar.setCurrentData((short)0);
  

       
       //PC
       pc.setOut(new DataLine(358, 202, 452, 202));
       pc.setIn(new DataLine(111, 202, 240, 202));
       pc.setLd(new DataLine(261, 214, 261, 229));
       pc.setInr(new DataLine(290, 214, 290, 229));
       pc.setClr(new DataLine(320, 214, 320, 229));
       pc.setBusOutputLine(new DataLine(464,202,464,636));

       
       pc.setCurrentPixelLocation (new Point(285,206));
       pc.setCurrentData((short)100);
       

       //DR
       dr.setOut(new DataLine(358, 264, 452, 264));
       dr.setIn(new DataLine(111, 265, 223, 265));
       dr.setLd(new DataLine(242, 276, 242, 291));
       dr.setInr(new DataLine(277, 276, 277, 291));
       dr.setClr(new DataLine(313, 276, 313, 291));
       dr.setBusOutputLine(new DataLine(464,264,464,636));

       dr.setCurrentPixelLocation(new Point(278,267));
       dr.setCurrentData((short)32768);
       
       //AC
       ac.setOut(new DataLine(358, 350, 452, 350));
       ac.setIn(new DataLine(194, 350, 223, 350));
       ac.setLd(new DataLine(242, 364, 242, 379));
       ac.setInr(new DataLine(277, 364, 277, 379));
       ac.setClr(new DataLine(313, 364, 313, 379));
       ac.setBusOutputLine(new DataLine(464,350,464,636));

       
       ac.setCurrentPixelLocation(new Point(274,354));
       ac.setCurrentData((short)14);
       
       
       //INPR
       
       //IR
       ir.setOut(new DataLine(358, 467, 452, 467));
       ir.setIn(new DataLine(111, 467, 223, 467));
       ir.setLd(new DataLine(242, 481, 242, 496));
       
       
       ir.setBusOutputLine(new DataLine(464,468,464,636));

       ir.setCurrentPixelLocation(new Point(278,470));
       ir.setCurrentData((short)0);

       //TR
       tr.setOut(new DataLine(358, 531, 452, 531));
       tr.setIn(new DataLine(111, 531, 223, 531));
       tr.setLd(new DataLine(242, 544, 242, 559));
       tr.setInr(new DataLine(277, 544, 277, 559));
       tr.setClr(new DataLine(313, 544, 313, 559));
       
       tr.setCurrentPixelLocation(new Point(490,535));
       tr.setCurrentData((short)32768);
       
       //compund line initialization 
       initClock();
       initAdderDr();
       initAdderAc();
       
       

       
   }
   
   private void initAdderDr(){
       adderDr.add(new DataLine( 419, 317,419, 265));
       adderDr.add(new DataLine( 131,317,419, 317));
       adderDr.add(new DataLine( 131,337,131, 317));
       adderDr.add(new DataLine( 134,337,131,337));
       
   }
      private void initAdderAc(){
       adderAc.add(new DataLine( 419, 398,419, 350));
       adderAc.add(new DataLine( 131,398,419, 398));
       adderAc.add(new DataLine( 131,363,131, 396));
       adderAc.add(new DataLine( 134,337,131,337));
       
   }
   
   private void initClock(){
              /*************************************/
       /*******COMPUND DATALINES*************/
       /*************************************/
       
       clock.add(new DataLine(381, 164, 381, 618));
       //ToAR
       clock.add(new DataLine(350, 150, 350, 164));
       clock.add(new DataLine(350, 164, 381, 164));
       //toPC
       clock.add(new DataLine(350, 214, 350, 229));
       clock.add(new DataLine(350, 229, 381, 229));
       //toDR
       clock.add(new DataLine(350, 276, 350, 291));
       clock.add(new DataLine(350, 292, 381, 292));
       //toAC
       clock.add(new DataLine(350, 363, 350, 378));
       clock.add(new DataLine(350, 378, 381, 378));
       //toIR
       clock.add(new DataLine(350, 481, 350, 494));
       clock.add(new DataLine(350, 494, 381, 494));
        //toTR
       clock.add(new DataLine(350, 543, 350, 558));
       clock.add(new DataLine(350, 558, 381, 558));      
       //toOUT
       clock.add(new DataLine(285, 608, 285, 619));
       clock.add(new DataLine(285, 619, 401, 619));      
       
   }

    /**
     * @return the memoryUnit
     */
    public RegisterBlock getMemoryUnit() {
        return memoryUnit;
    }

    /**
     * @return the ar
     */
    public RegisterBlock getAr() {
        return ar;
    }

    /**
     * @return the dr
     */
    public RegisterBlock getDr() {
        return dr;
    }

    /**
     * @return the ac
     */
    public RegisterBlock getAc() {
        return ac;
    }

    /**
     * @return the ir
     */
    public RegisterBlock getIr() {
        return ir;
    }

    /**
     * @return the tr
     */
    public RegisterBlock getTr() {
        return tr;
    }

    /**
     * @return the outr
     */
    public RegisterBlock getOutr() {
        return outr;
    }

    /**
     * @return the pc
     */
    public RegisterBlock getPc() {
        return pc;
    }

    /**
     * @param pc the pc to set
     */
    public void setPc(RegisterBlock pc) {
        this.pc = pc;
    }
    
}
