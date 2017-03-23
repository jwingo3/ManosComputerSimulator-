/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manosinstructionsimulator;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.TableColumn;

public class ManosGUI {
    BufferedImage bi = ImageIO.read(new File("ManosComp.jpg"));
    final ColorPanel cPanel = new ColorPanel(bi);
    final Timer timer = new Timer(100, null);
    final Timer computerTimer = new Timer(3000,null);
    
    JPanel buttonPanel = new JPanel();
    JPanel editablePanel = new JPanel(); //panel to put combo selector switch and workable memory 
    JButton restartButton = new JButton("Restart");
    JButton startButton = new JButton("Start");
    JComboBox comboSelector = new JComboBox();
    
    //state for the computer 
    String currentState ="MOVEPC";
    Boolean indirectBit = false; 
    String opCode;
    

    

    //strings for combo box 
    String[] comboBoxStr = { "AND","AND-Indirect", "ADD", "ADD-Indirect",
      "LOAD", "LOAD-Indirect", "STORE", "STORE-Indirect", "BRANCH UNCONDIONAL",
      "BRANCH UNCONDIONAL-Indirect","BRANCH SAVE AND RETURN","BRANCH SAVE AND RETURN-Indirect",
      "INC AND SKIP IF 0","INC AND SKIP IF 0-Indirect"};
    
    //Inital Comand will be ADD 
    String selectedCommand = "MOVEPC";
    
    
    //table variables
    String rowData[][] = new String[20][2];
    Object columnNames[] = { "Address Location", "Value" };
    JTable table = new JTable(rowData, columnNames);


    //Output text field 
    JTextField outputText = new JTextField("Time and commands will be displayed here");

    //initialize a instance of a register class.
    //this collection has all of the register objects with the value of the register 
    //as well as the pixel location on the picture
    RegisterCollection registers = new RegisterCollection();

    
    //global String to hold 
    String CurrentState;
    int stateCounter = 0;

    
    //end global Declerations 
    
    //Main GUI code Begins here 
    
    //this is the constructur that will be called from another class
    //this initializes and controls the computer as well as all of the animations 
    //required 
    public ManosGUI() throws Exception {

        JFrame frame = new JFrame();
        frame.setTitle("Manos Computer Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(400,100);
        frame.repaint();
        
        
        initComponents(frame);
        
        //runManosComputer();
        
        
        
        frame.pack();
        frame.setVisible(true);
    }

    //initialize all components that will be needed for execution
    //including adding panels to the frame and adding swing objects to the panel
    private void initComponents(JFrame frame) throws Exception {
        
        //initialize memory of computer 
        for(int i=0; i<rowData.length;i++){
            rowData[i][0]="0x"+Integer.toHexString(i+100);
            rowData[i][1]="0x0000";
        }
        
        rowData[0][1] = "0x0065";
        rowData[1][1] = "0x0066";
        rowData[2][1] = "0x0069";
        rowData[3][1] = "0x0065";
        rowData[4][1] = "0x0065";
        rowData[5][1] = "0x0065";
        rowData[6][1] = "0x0065";
        rowData[7][1] = "0x0065";
        rowData[8][1] = "0x0065";

        
        //draw PC register value since we it will always need to be initalized      
        drawCurrentData(registers.getPc());
        System.out.println(Integer.decode((String) rowData[0][1])); 
        

        frame.add(cPanel,BorderLayout.WEST);//set up boarder layout for cPanel 
        
        setupButtonPanel();//sets up all of the buttons in a button panel

        frame.getContentPane().add(buttonPanel,BorderLayout.EAST);//add button panel 
  
        setupEditablePanel();//set up the editable pannel
        frame.getContentPane().add(editablePanel,BorderLayout.CENTER);
        
        //add an output text box
        frame.getContentPane().add(outputText,BorderLayout.SOUTH);
        
        //draw all data 
        drawAllRegisterData();
        
    }
    
    //run Manos computer
    //function will have a state machine that will represent the process of the instruction cycle 
    /******************************************************************/
    /****THIS IS THE BULK OF THE MACHINE LOGIC*************************/
    /******************************************************************/

    
    
   private void runManosComputer(){
        
        /*switch(CurrentState){
        case "ADD":
        break;
        }*/
        
    
                
                //clear image so that we start from a blank slate while aniamating 
                clearImage();
                //if counter even draw new state 
                //if counter is odd then erase 
                //this is to simulate clock turning off
                
                //static OP code to remember where to go
                
                    switch(currentState){
                        case "MOVEPC":
                            //set output text for current step 
                            System.out.println("T_0 outputText");                        
                            outputText.setText("T_0:AR <-- PC");
                            outputText.repaint();
                            
                            //move Ar<-PC
                            registers.getAr().setCurrentData(registers.getPc().getCurrentData());                          
                            //Animate line from PC 
                            cPanel.animateDashedLine(registers.getPc().getOut(),cPanel.RIGHT);
                            //draw the new register data         
                            drawAllRegisterData();                         
                            //draw bus lines
                            drawBusLines(registers.getPc());   
                            //input ARline
                            cPanel.animateDashedLine(registers.getAr().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getAr().getLd(), cPanel.UP);
                            //draw selector switch 
                            drawBusDecoder(5);
                            //change state 
                            currentState = "FETCH";


                            break;
                        case "FETCH":
                            System.out.println("FETCH");
                            outputText.setText("T_1:IR <-- M[AR], PC=PC+1");
                            
                            //incrementPC and animate increment PC
                            registers.getPc().setCurrentData((short) (registers.getPc().getCurrentData()+1));
                            drawCurrentData(registers.getPc());
                            

                            
                            if(!fetchFromMemory(registers.getIr())) break;
                            
                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
                            
                            //draw the fetch from memory 
                            cPanel.animateDashedLine(registers.getPc().getInr(),cPanel.UP);
                            drawBusLines(registers.getMemoryUnit());
                            cPanel.animateDashedLine(registers.getIr().getIn(),cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getMemoryUnit().getWrite(), cPanel.UP);

                            cPanel.animateDashedLine(registers.getIr().getLd(), cPanel.UP);
                            
                           drawMARLines();
                           
                            drawBusDecoder(7);
                            

                            currentState = "DECODE";
                            stateCounter++;

                            break;
                        case "DECODE":
                            System.out.println("DECODE");
                            outputText.setText("T_2:DO..D7<--Decode IR(12-14),AR<-IR(0-11),I<-IR(15)");


                            cPanel.animateDashedLine(registers.getAr().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getAr().getLd(), cPanel.UP);

                            registers.getAr().setCurrentData((short) (registers.getIr().getCurrentData() & 0xFFF));
                                                //animate AR<-IR(0-11)
                                drawAllRegisterData();
                                drawBusLines(registers.getIr());
                    
                            //draw decoder
                            drawBusDecoder(2);
                            currentState = "INDIRECT";
                       
                            break;
                            

                        case "INDIRECT":
                            System.out.println("Indirect");
                            outputText.setText("T_3: Decode Indirect address");
                            
                            //deciscion tree for next step 
                            opCode=Integer.toHexString((registers.getIr().getCurrentData()& 0xF000 )>>12);//only tested for Add 
                            
                            //if indirect animate if not dont do this animation 
                            if(0x8<=(registers.getIr().getCurrentData()& 0xF000 )>>12){
                                System.out.println("indirect");
                                outputText.setText("D_7'IT_3: AR<--M[AR] (Indirect Address)");
                                
                                //move indirect adress 
                                if(!fetchFromMemory(registers.getAr())) break;
                
                                //DRAW LINES FOR INDIRECT ADRESSING 
                                cPanel.animateDashedLine(registers.getPc().getInr(),cPanel.UP);
                                drawBusLines(registers.getMemoryUnit());
                                cPanel.animateDashedLine(registers.getAr().getIn(),cPanel.RIGHT);
                                cPanel.animateDashedLine(registers.getMemoryUnit().getAddressInter(), cPanel.UP);
                                cPanel.animateDashedLine(registers.getMemoryUnit().getAddress(), cPanel.UP);
                                cPanel.animateDashedLine(registers.getMemoryUnit().getWrite(), cPanel.UP);
                                cPanel.animateDashedLine(registers.getAr().getLd(), cPanel.UP);

                                
                                //animate a short line from AR for the input to the memory register 
                                cPanel.animateDashedLine(new DataLine(359,137,417,137), cPanel.RIGHT);
                                
                                drawBusDecoder(7);
                                drawAllRegisterData();
                                

                            
                            }else{
                                outputText.setText("D_7'I'T_3: Do nothing not indirect ");
                                drawAllRegisterData();

                            }
                            
 
                            //decode opCode. In real 
                            //for ADD 
                            if(opCode.equals("8")||opCode.equals("0")){
                                System.out.println("Decode:AND");
                                currentState="AND";
                            }
                            else if (opCode.equals("1")||opCode.equals("9")){
                                System.out.println("Decode:ADD");
                                currentState="ADD";              
                            }else if(opCode.equals("2")||opCode.equalsIgnoreCase("a")){
                                System.out.println("Decode:LDA");
                                currentState="LOAD"; 
                            }else if(opCode.equals("3")||opCode.equalsIgnoreCase("b")){
                                currentState="STORE"; 
                            }else if(opCode.equals("4")||opCode.equalsIgnoreCase("c")){
                                currentState="BUN"; 

                            }else if(opCode.equals("5")||opCode.equalsIgnoreCase("d")){
                                currentState="BSA"; 

                            }else if(opCode.equals("6")||opCode.equalsIgnoreCase("e")){
                                currentState="ISZ"; 
                            }
                            else{
                                outputText.setText("OpCode Doesnt Exist-Command Canceled");
                                System.out.println("current OPcode: " + opCode);
                                currentState="Stop";
                            }
                            break;
                                
   

                        case "AND":
                            System.out.println("AND");
                            outputText.setText("T_4: DR <--M[AR]");
                            
                            fetchFromMemory(registers.getDr());
                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
                            
                            //draw animation lines 
                            drawBusLines(registers.getMemoryUnit());
                            cPanel.animateDashedLine(registers.getMemoryUnit().getRead(), cPanel.UP);
                            cPanel.animateDashedLine(registers.getDr().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getDr().getLd(), cPanel.UP);
                            
                            drawBusDecoder(7);
                            drawMARLines();
                            currentState="AND2";              

                            break;
                        case "AND2":
                            System.out.println("AND2");
                            outputText.setText("T_5: AC <--AC^DR");
                            registers.getAc().setCurrentData((short) (registers.getAc().getCurrentData() & registers.getDr().getCurrentData()));
                            
                            //draw Animiations 
                            cPanel.animateDashedLine(registers.getAc().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getAc().getOut(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getDr().getOut(), cPanel.RIGHT);

                            drawCompundDataLine(registers.adderAc);
                            drawCompundDataLine(registers.adderDr);
                            
                            drawAllRegisterData();
                            currentState="Stop";              

                            break;
                        case "ADD":
                            System.out.println("ADD");
                            outputText.setText("T_4: DR <--M[AR] ");
                            
                            fetchFromMemory(registers.getDr());
                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
                            
                            //draw animation lines 
                            drawBusLines(registers.getMemoryUnit());
                            cPanel.animateDashedLine(registers.getMemoryUnit().getRead(), cPanel.UP);
                            cPanel.animateDashedLine(registers.getDr().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getDr().getLd(), cPanel.UP);
                  
                            drawMARLines();
                            
                            drawBusDecoder(7);
                            currentState="ADD2";              

                            break;
                            
                        case "ADD2":
                            System.out.println("ADD2");
                            outputText.setText("T_5: AC <--AC+DR");
                            registers.getAc().setCurrentData((short) (registers.getAc().getCurrentData() + registers.getDr().getCurrentData()));
                            
                            //draw Animiations 
                            cPanel.animateDashedLine(registers.getAc().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getAc().getOut(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getDr().getOut(), cPanel.RIGHT);

                            drawCompundDataLine(registers.adderAc);
                            drawCompundDataLine(registers.adderDr);
                            
                            drawAllRegisterData();
                            
                            currentState="Stop";              

                            break;    
                         case "LOAD":
                            System.out.println("ADD");
                            outputText.setText("T_4: DR <--M[AR] ");
                            
                            fetchFromMemory(registers.getDr());
                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
                            
                            //draw animation lines 
                            drawBusLines(registers.getMemoryUnit());
                            cPanel.animateDashedLine(registers.getMemoryUnit().getRead(), cPanel.UP);
                            cPanel.animateDashedLine(registers.getDr().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getDr().getLd(), cPanel.UP);
   
                            
                            drawBusDecoder(7);
                            drawMARLines();               
                            currentState="LOAD2";              

                            break;
                            
                        case "LOAD2":
                            System.out.println("ADD2");
                            outputText.setText("T_5: AC <--DR");
                            registers.getAc().setCurrentData((short) ( registers.getDr().getCurrentData()));
                            
                            //draw Animiations 
                            cPanel.animateDashedLine(registers.getAc().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getAc().getOut(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getDr().getOut(), cPanel.RIGHT);

                            drawCompundDataLine(registers.adderAc);
                            drawCompundDataLine(registers.adderDr);
  
                            drawBusDecoder(4);                          
                            drawAllRegisterData();
                            currentState="Stop";              

                            break;    
                        case "STORE":
                            System.out.println("ADD2");
                            outputText.setText("T_4: M[AR]<--AC");
                            
                            //store AC to M[AR]
                            int indexOfMAR = -1;
                            
                            //find which row M[AR] is at 
                            for (int i = 0; i<rowData.length;i++){
                                if(rowData[i][0].equals("0x"+Integer.toHexString(registers.getAr().getCurrentData()))){
                                   indexOfMAR=i;
                                }
                            }
                            if(indexOfMAR!=-1){
                                rowData[indexOfMAR][1] = "0x"+Integer.toHexString(registers.getAc().getCurrentData());//put ac into the point that indexOfMAR found 
                            }else{

                                System.out.println("ERROR: PC address not found in Memory");
                                outputText.setText("ERROR: PC address not found in Memory-Stop Processing");
                                currentState="Stop";

                            }
   
                            drawBusDecoder(3);                         
                            clearImage();
                            drawAllRegisterData();
                       
                            table.repaint();
                            
                            //animate lines 
                            drawBusLines(registers.getAc());
                            cPanel.animateDashedLine(registers.getMemoryUnit().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getMemoryUnit().getWrite(), cPanel.UP);
                            currentState="Stop";              
                            drawMARLines();  
                            
                            break;
                        case "BUN":
                            System.out.println("BUN");
                            outputText.setText("T_4: PC<--AR");
                            //put AR into PC
                            registers.getPc().setCurrentData(registers.getAr().getCurrentData());
                            
                            clearImage();
                            drawAllRegisterData();
                            
                            //animate move 
                            drawBusLines(registers.getAr());
                            
                            cPanel.animateDashedLine(registers.getPc().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getPc().getLd(), cPanel.UP);

                            drawBusDecoder(6);
                            currentState = "Stop";
                            break;
                        case "BSA":
                            System.out.println("BSA");
                            outputText.setText("T_4: M[AR]<--PC, AR<-AR+1");
                            
     
                            //store AC to M[AR]
                            indexOfMAR = -1;
                            
                            //find which row M[AR] is at 
                            for (int i = 0; i<rowData.length;i++){
                                if(rowData[i][0].equals("0x"+Integer.toHexString(registers.getAr().getCurrentData()))){
                                   indexOfMAR=i;
                                }
                            }
                            if(indexOfMAR!=-1){
                                rowData[indexOfMAR][1] = "0x"+Integer.toHexString(registers.getPc().getCurrentData());//put ac into the point that indexOfMAR found 
                            }else{

                                System.out.println("ERROR: PC address not found in Memory");
                                outputText.setText("ERROR: PC address not found in Memory-Stop Processing");
                                currentState="Stop";

                            }
                            
                            
                            //incrementAR
                            registers.getAr().setCurrentData( (short)(registers.getAr().getCurrentData()+1));
                            
                            //clear and redraw new data 
                            clearImage();
                            drawAllRegisterData();
                            table.repaint();
                            
                            //animate lines 
                            drawBusLines(registers.getPc());
                            
                            cPanel.animateDashedLine(registers.getMemoryUnit().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getMemoryUnit().getWrite(), cPanel.RIGHT);

                            cPanel.animateDashedLine(registers.getAr().getInr(), cPanel.UP);
              
                            drawBusDecoder(5);
                            drawMARLines();              
                            
                            currentState = "BSA2";
                            break;
                        case "BSA2":
                            System.out.println("BSA2");
                            outputText.setText("T_5: PC<--AR");
                            //put AR into PC
                            registers.getPc().setCurrentData(registers.getAr().getCurrentData());
                            
                            clearImage();
                            drawAllRegisterData();
                            
                            //animate move 
                            drawBusLines(registers.getAr());
                            
                            
                            
                            drawBusDecoder(6);
                            cPanel.animateDashedLine(registers.getPc().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getPc().getLd(), cPanel.UP);
                            currentState = "Stop";
                            break;

                        case "ISZ":
                            System.out.println("ISZ");
                            outputText.setText("T_4: DR <--M[AR] ");
                            
                            fetchFromMemory(registers.getDr());
                            
                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
                            
                            //draw animation lines 
                            drawBusLines(registers.getMemoryUnit());
                            cPanel.animateDashedLine(registers.getMemoryUnit().getRead(), cPanel.UP);
                            cPanel.animateDashedLine(registers.getDr().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getDr().getLd(), cPanel.UP);

                            drawBusDecoder(7);
                            drawMARLines();
                            
                            currentState = "ISZ2";
                            break;
                        case "ISZ2":
        
                            System.out.println("ISZ2");
                            outputText.setText("T_5: DR <-- DR + 1 ");
                            //increment DR
                            registers.getDr().setCurrentData( (short)(registers.getDr().getCurrentData()+1));

                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
          
                            cPanel.animateDashedLine(registers.getDr().getInr(), cPanel.UP);

                            
                            currentState = "ISZ3";
                            break;                        
                        case "ISZ3":
                            System.out.println("ISZ2");
                            outputText.setText("T_5: M[AR] <-- DR, if (DR==0) then PC<-- PC + 1 ");
                            //store DR to M[AR]
                            indexOfMAR = -1;
                            
                            //find which row M[AR] is at 
                            for (int i = 0; i<rowData.length;i++){
                                if(rowData[i][0].equals("0x"+Integer.toHexString(registers.getAr().getCurrentData()))){
                                   indexOfMAR=i;
                                }
                            }
                            //set dt into M[AR]
                            if(indexOfMAR!=-1){
                                rowData[indexOfMAR][1] = "0x"+Integer.toHexString(registers.getDr().getCurrentData());//put ac into the point that indexOfMAR found 
                            }else{

                                System.out.println("ERROR: PC address not found in Memory");
                                outputText.setText("ERROR: PC address not found in Memory-Stop Processing");
                                currentState="Stop";
                                break;
                            }
                            //conditional PC<--PC+1
                            if(registers.getDr().getCurrentData()==0){
                                registers.getPc().setCurrentData((short)(registers.getPc().getCurrentData()+1));
                                cPanel.animateDashedLine(registers.getPc().getInr(), cPanel.UP);

                            }
                            
                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
                            
                            table.repaint();
                            
                            //animate Lines
                            drawBusLines(registers.getDr());
                            cPanel.animateDashedLine(registers.getMemoryUnit().getIn(), cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getMemoryUnit().getWrite(), cPanel.UP);
                            drawBusDecoder(4);                  

                           drawMARLines();
                            
                            
                            currentState = "Stop";
                            break;


                                    
                            
                            
                        case "Stop":
                            clearImage();
                            drawAllRegisterData();
                            outputText.setText("SC<-0 (Command Done)");


                            System.out.println("stopped");
                            currentState = "MOVEPC";
                            return;

                    }
                    

        
    }
   
    //button listener for start and reset 
    class ButtonListener implements ActionListener {
        ButtonListener() {}

        public void actionPerformed(ActionEvent e) {
            int count = 0;
            System.out.println(e.getActionCommand());
            if (e.getActionCommand().equals("Restart")) {
                clearImage();
            
            }else if(e.getActionCommand().equals("Start")){
                

                clearImage();
                drawAllRegisterData();
                runManosComputer();
                //System.out.println(count++);

                
                

            }
        }
    }
    
    //ComboBox Listener 
    class ComboListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox)e.getSource();
            selectedCommand = (String)cb.getSelectedItem();
            
            if (selectedCommand.equals("AND")){
                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set memory for example
                rowData[0][1] = "0x0065";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0065";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
            }
            else if(selectedCommand.equals("AND-Indirect")){
                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set AC to a good value for AND with the following memory 
                registers.getAc().setCurrentData((short)14);

                       
                //set memory for example
                rowData[0][1] = "0x8065";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0065";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
            }else if (selectedCommand.equals("ADD")){
                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set memory for example
                rowData[0][1] = "0x1065";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0065";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
            }else if(selectedCommand.equals("ADD-Indirect")){
                                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set AC to a good value for AND with the following memory 
                registers.getAc().setCurrentData((short)14);

                       
                //set memory for example
                rowData[0][1] = "0x9065";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0065";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
                
            }else if (selectedCommand.equals("LOAD")){
                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set memory for example
                rowData[0][1] = "0x2065";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0065";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
            }else if(selectedCommand.equals("LOAD-Indirect")){
                                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set AC to a good value for AND with the following memory 
                registers.getAc().setCurrentData((short)14);

                       
                //set memory for example
                rowData[0][1] = "0xA065";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0065";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
                
            }else if (selectedCommand.equals("STORE")){
                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set memory for example
                rowData[0][1] = "0x3065";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0065";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
            }else if(selectedCommand.equals("STORE-Indirect")){
                                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set AC to a good value for AND with the following memory 
                registers.getAc().setCurrentData((short)14);

                       
                //set memory for example
                rowData[0][1] = "0xB065";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0065";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
                
            }else if (selectedCommand.equals("BRANCH UNCONDIONAL")){
                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                
                //set memory for example
                rowData[0][1] = "0x4067";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0075";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
            }
            else if(selectedCommand.equals("BRANCH UNCONDIONAL-Indirect")){
                                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set AC to a good value for AND with the following memory 
                registers.getAc().setCurrentData((short)14);

                       
                //set memory for example
                rowData[0][1] = "0xC067";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0075";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
                
            }
            else if (selectedCommand.equals("BRANCH SAVE AND RETURN")){
                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                
                //set memory for example
                rowData[0][1] = "0x5067";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0075";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
            }
            else if(selectedCommand.equals("BRANCH SAVE AND RETURN-Indirect")){
                                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set AC to a good value for AND with the following memory 
                registers.getAc().setCurrentData((short)14);

                       
                //set memory for example
                rowData[0][1] = "0xD067";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0075";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
                
            }
             else if (selectedCommand.equals("INC AND SKIP IF 0")){
                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                
                //set memory for example
                rowData[0][1] = "0x6067";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0xFFFF";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
            }
            else if(selectedCommand.equals("INC AND SKIP IF 0-Indirect")){
                                //set PC to x64 
                registers.getPc().setCurrentData((short) 0x64);
                //set AC to a good value for AND with the following memory 
                registers.getAc().setCurrentData((short)14);

                       
                //set memory for example
                rowData[0][1] = "0xE067";
                rowData[1][1] = "0x0066";
                rowData[2][1] = "0x0069";
                rowData[3][1] = "0x0075";
                rowData[4][1] = "0x0065";
                rowData[5][1] = "0x0065";
                rowData[6][1] = "0x0065";
                rowData[7][1] = "0x0065";
                rowData[8][1] = "0x0065";
                
            }
            
            
            clearImage();
            drawAllRegisterData();
            table.repaint();
        }
        
        
    }
    
    //code to clear the Manos computer image back to blank 
    private void clearImage(){
        
        System.out.println("Clear image");
        try {
           bi = ImageIO.read(new File("ManosComp.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(ManosGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
            
                cPanel.resetTimer();
                cPanel.setBufferedImage(bi);
    }
    
    //functino to draw datalines that require multiple lines 
    private void drawCompundDataLine(ArrayList<DataLine> input){
        for (int i = 0; i < input.size(); i++) {
           cPanel.animateDashedLine(input.get(i),cPanel.UP);

        }

    }
    
    private void setupButtonPanel(){
        buttonPanel.add(restartButton);
        buttonPanel.add(startButton);
        GridLayout layout = new GridLayout(0,2);

        buttonPanel.setLayout(layout);

        restartButton.addActionListener(new ButtonListener());
        startButton.addActionListener(new ButtonListener());
    }
    
    private void setupEditablePanel(){
               GridLayout layout = new GridLayout(2,0);//make a layout object
        //use layout object to make editable panel a 2,0 grid 
        editablePanel.setLayout(layout);

        JScrollPane scrollPane = new JScrollPane(table);

        
         TableColumn column = null;
            column = table.getColumnModel().getColumn(0);
            column.setWidth(10); //sport column is bigger
        
       
        editablePanel.add(scrollPane);
        
        for (int i = 0; i < comboBoxStr.length; i++)
                comboSelector.addItem(comboBoxStr[i]);
        
        //call listener 
        comboSelector.addActionListener(new ComboListener());
           
        editablePanel.add(comboSelector);
    }
    

    
    //draws all of the registers data 
    private void drawAllRegisterData(){
        drawCurrentData(registers.getAr());
        drawCurrentData(registers.getPc());
        drawCurrentData(registers.getDr());
        drawCurrentData(registers.getIr());
        drawCurrentData(registers.getAc());
        
    }
    
    //writes current at the location Current Data location 
    private void drawCurrentData(RegisterBlock inRegBlock){
        cPanel.drawText("0x"+Integer.toHexString(inRegBlock.getCurrentData() & 0xffff), inRegBlock.getCurrentPixelLocation().getX(), inRegBlock.getCurrentPixelLocation().getY());
        
    }
    
    private void drawBusLines(RegisterBlock fromBlock){
                   cPanel.animateDashedLine(fromBlock.getOut(),cPanel.RIGHT);
                   cPanel.animateDashedLine(fromBlock.getBusOutputLine(),cPanel.DOWN);
                   cPanel.animateDashedLine(registers.busInputLine,cPanel.UP);
                   cPanel.animateDashedLine(registers.bottomOfBusLine, cPanel.RIGHT);
    }
    //this function simply moves M[AR] into whatevever register you specify 
    private Boolean fetchFromMemory(RegisterBlock inputReg){
        //fetch from memory registers.getA r()
        
    
        int indexOfMAR = -1;
        for (int i = 0; i<rowData.length;i++){
            if(rowData[i][0].equals("0x"+Integer.toHexString(registers.getAr().getCurrentData()))){
               indexOfMAR=i;
            }
        }
        if(indexOfMAR!=-1){
            inputReg.setCurrentData((short) Long.parseLong(rowData[indexOfMAR][1].substring(2), 16));//only grab the string past the 0x of the hex value 
            return true; 
        }else{
            
            System.out.println("ERROR: PC address not found in Memory");
            outputText.setText("ERROR: PC address not found in Memory-Stop Processing");
            currentState="Stop";
            return false; 

        }
        
    }
    
    private void drawMARLines(){
        cPanel.animateDashedLine(registers.getMemoryUnit().getAddress(), cPanel.UP);
        cPanel.animateDashedLine(registers.getMemoryUnit().getAddressInter(), cPanel.UP);
                           
        //animate a short line from AR for the input to the memory register 
                            
        cPanel.animateDashedLine(new DataLine(359,137,417,137), cPanel.RIGHT);
    }
   
 
    private void drawBusDecoder(int inputNumber){
        
        switch(inputNumber){
            case 0:
                
                break;
            case 1:
                drawS0();
                
                break;
                       
            case 2:
                drawS1();
                break;      
            case 3:     
                drawS0();
                drawS1();
                break;
            case 4:
                drawS2();
                break;
                       
            case 5:
                drawS2();
                drawS0();
                break;
                      
            case 6:
                drawS2();
                drawS1();
                break;
                      
            case 7:
                
                drawS2();
                drawS1();
                drawS0();
            
                break;
                 
                 
                 
                               
            
        }


    }
    private void drawS0(){
        cPanel.animateDashedLine(new DataLine(421,37,432,37), cPanel.RIGHT);
    }private void drawS1(){
        cPanel.animateDashedLine(new DataLine(421,26,432,26), cPanel.RIGHT);
    }private void drawS2(){
       cPanel.animateDashedLine(new DataLine(421,14,432,14), cPanel.RIGHT);
    }
}



