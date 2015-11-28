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
    

    //strings for combo box 
    String[] comboBoxStr = { "ADD", "Obtuse", "Recalcitrant",
      "Brilliant", "Somnescent", "Timorous", "Florid", "Putrescent" };
    
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
        
        rowData[0][1] = "0x8065";
        rowData[1][1] = "0x0125";
        rowData[2][1] = "0x0065";
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
        
        computerTimer.addActionListener(new AbstractAction(){
            String currentState = selectedCommand;
            String nextState = selectedCommand;
    
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                //clear image so that we start from a blank slate while aniamating 
                clearImage();
                //if counter even draw new state 
                //if counter is odd then erase 
                //this is to simulate clock turning off 
                if ((stateCounter%2)== 0) {
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
                            
                            //set output line value 
                            registers.getPc().setOutputData(registers.getPc().getCurrentData());
                            
                            //drawCompundDataLine(registers.clock);                                
                            drawAllRegisterData();
                            
                            //draw bus lines
                            drawBusLines(registers.getPc());
                            
                            //input ARline
                            cPanel.animateDashedLine(registers.getAr().getIn(), cPanel.RIGHT);

                            //increment state counter 
                            stateCounter++;
                            
                            //change state 
                            currentState = "FETCH";


                            break;
                        case "FETCH":
                            System.out.println("FETCH");
                            outputText.setText("T_1:IR <-- M[AR], PC=PC+1");

                            
                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
                            
                            //fetch from memory 
                            System.out.println("getAR currentData "+(Integer.toHexString(registers.getAr().getCurrentData())));
                            int indexOfMAR = Arrays.asList(rowData[0]).indexOf("0x"+Integer.toHexString(registers.getAr().getCurrentData()));
                            if(indexOfMAR!=-1){
                                registers.getIr().setCurrentData((short) Long.parseLong(rowData[indexOfMAR][1].substring(2), 16));//only grab the string past the 0x of the hex value 
                            }else{
                                System.out.println("ERROR: PC address not found in Memory");
                                outputText.setText("ERROR: PC address not found in Memory-Stop Processing");
                                currentState="Stop";
                                break;

                            }
                            //draw the fetch from memory 
                            cPanel.animateDashedLine(registers.getPc().getInr(),cPanel.UP);
                            drawBusLines(registers.getMemoryUnit());
                            cPanel.animateDashedLine(registers.getIr().getIn(),cPanel.RIGHT);
                            cPanel.animateDashedLine(registers.getMemoryUnit().getAddressInter(), cPanel.UP);
                            cPanel.animateDashedLine(registers.getMemoryUnit().getAddress(), cPanel.UP);
                            //animate a short from AR for the input to the memory register 
                            cPanel.animateDashedLine(new DataLine(359,137,417,137), cPanel.RIGHT);

                            //incrementPC and animate increment PC
                            registers.getPc().setCurrentData((short) (registers.getPc().getCurrentData()+1));
                            drawCurrentData(registers.getPc());

                            currentState = "DECODE";
                            stateCounter++;

                            break;
                        case "DECODE":
                            System.out.println("DECODE");
                            outputText.setText("T_2:DO..D7<--Decode IR(12-14),AR<-IR(0-11),I<-IR(15)");
                            
                            //animate AR<-IR(0-11)
                            drawBusLines(registers.getIr());
                            registers.getAr().setCurrentData((short) (registers.getIr().getCurrentData() & 0xFFF));
                            
                            //clear and redraw the register data 
                            clearImage();
                            drawAllRegisterData();
                            
                            
                            
                            //deciscion tree for next step 
                            
                            String opCode=Integer.toHexString((registers.getIr().getCurrentData()& 0xF000 )>>12);//only tested for Add  
                            
                            
                            

                            if(opCode.equals("8")||opCode.equals("0")){
                                System.out.println("Decode:ADD");
                                currentState="ADD";
                                break;
                                
                            }else{
                                outputText.setText("OpCode Doesnt Exist-Command Canceled");
                                //currentState="Stop";
                                stateCounter++;
                                break;
                            }
                        
                        


                        case "ADD":
                            System.out.println("ADD");
                            outputText.setText("T_3: ");
                            break;
                            
                        case "Stop":
                            clearImage();
                            drawAllRegisterData();

                            computerTimer.removeActionListener(this);
                            computerTimer.stop();

                            System.out.println("stopped");
                            currentState = "MOVEPC";
                            return;

                    }
                    
                }
                else{
                  clearImage();
                  drawAllRegisterData();
                  stateCounter++;
                }
                

                

                

            }
        });
        
      computerTimer.setInitialDelay(0);

      computerTimer.start();
        
        
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
                

                
                runManosComputer();
                System.out.println(count++);

                
                

            }
        }
    }
    
    //ComboBox Listener 
    class ComboListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox)e.getSource();
            selectedCommand = (String)cb.getSelectedItem();
            
            if (selectedCommand.equals("ADD")){
               rowData[0][1] = "0x8065";
            }
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
                comboSelector.addItem(comboBoxStr[i++]);
        
        //call listener 
        comboSelector.addActionListener(new ComboListener());
           
        editablePanel.add(comboSelector);
    }
    
    //draws current data of the reg block to the output data loctiaon 
    private void drawOutputData(RegisterBlock inRegBlock){
        cPanel.drawText("0x"+Integer.toHexString(inRegBlock.getCurrentData() & 0xffff), inRegBlock.getOutputPixelLocation().getX(), inRegBlock.getOutputPixelLocation().getY());
    }
    
    //draws all of the registers data 
    private void drawAllRegisterData(){
        drawCurrentData(registers.getAr());
        drawCurrentData(registers.getPc());
        drawCurrentData(registers.getDr());
        drawCurrentData(registers.getIr());
        
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
    
   
    /*this block of code animates everything that can be animated. Was part of old test code 
    but can now be used as a reference on how to use each of the fucntions 
    
                //animate memory unit
                cPanel.animateDashedLine(registers.getMemoryUnit().getIn(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getMemoryUnit().getAddress(),cPanel.LEFT);
                cPanel.animateDashedLine(registers.getMemoryUnit().getAddressInter(),cPanel.UP);
                cPanel.animateDashedLine(registers.getMemoryUnit().getWrite(),cPanel.UP);
                cPanel.animateDashedLine(registers.getMemoryUnit().getRead(),cPanel.UP);

                //animate AR 
                cPanel.animateDashedLine(registers.getAr().getOut(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getAr().getIn(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getAr().getLd(),cPanel.UP);
                cPanel.animateDashedLine(registers.getAr().getInr(),cPanel.UP);
                cPanel.animateDashedLine(registers.getAr().getClr(),cPanel.UP);

                
                //animate PC 
                cPanel.animateDashedLine(registers.getPc().getOut(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getPc().getIn(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getPc().getLd(),cPanel.UP);
                cPanel.animateDashedLine(registers.getPc().getInr(),cPanel.UP);
                cPanel.animateDashedLine(registers.getPc().getClr(),cPanel.UP);
                
                //animate DR 
                cPanel.animateDashedLine(registers.getDr().getOut(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getDr().getIn(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getDr().getLd(),cPanel.UP);
                cPanel.animateDashedLine(registers.getDr().getInr(),cPanel.UP);
                cPanel.animateDashedLine(registers.getDr().getClr(),cPanel.UP);
                //animate AC 
                cPanel.animateDashedLine(registers.getAc().getOut(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getAc().getIn(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getAc().getLd(),cPanel.UP);
                cPanel.animateDashedLine(registers.getAc().getInr(),cPanel.UP);
                cPanel.animateDashedLine(registers.getAc().getClr(),cPanel.UP);
                
                //animate ir 
                cPanel.animateDashedLine(registers.getIr().getOut(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getIr().getIn(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getIr().getLd(),cPanel.UP);

                
                //Animate TR 
                
                cPanel.animateDashedLine(registers.getTr().getOut(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getTr().getIn(),cPanel.RIGHT);
                cPanel.animateDashedLine(registers.getTr().getLd(),cPanel.UP);
                cPanel.animateDashedLine(registers.getTr().getInr(),cPanel.UP);
                cPanel.animateDashedLine(registers.getTr().getClr(),cPanel.UP);
                
                //draw compound lines for clock and adder 
                drawCompundDataLine(registers.clock);                                
                drawCompundDataLine(registers.adderDr); 
                drawCompundDataLine(registers.adderAC);                                

                drawInputData(registers.getMemoryUnit());
                drawOutputData(registers.getMemoryUnit());
                
                drawOutputData(registers.getAr());
                
                drawOutputData(registers.getPc());
                
                drawOutputData(registers.getDr());
                
                drawOutputData(registers.getAc());
                
                drawOutputData(registers.getIr());
                
                drawOutputData(registers.getTr());
                
                */
}


