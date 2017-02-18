package Emulator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import Emulator.Tables.*;
import Resources.PirmasisKanalas;

public class GUI {
	
	JFrame frame = new JFrame();
	
	static JLabel labelMemory = new JLabel("VM Atminits");
	static JLabel labelResList = new JLabel("Resursų sąrašas");
	static JLabel labelProcList = new JLabel("Procesų sąrašas");
	static JLabel labelEmulator = new JLabel("Emuliatoriaus konsolė");
	static JLabel labelConsole = new JLabel("OS konsolė");
	
	static JList<String> listMemory = new JList<String>();
	
	static JTable tableProc = new JTable();
	static JTable tableRes = new JTable();
	
	static JCheckBox checkRunning = new JCheckBox("Vykdomi");
	static JCheckBox checkReady = new JCheckBox("Pasiruošę");
	static JCheckBox checkBlocked = new JCheckBox("Blokuoti");
	static JCheckBox checkNoSteps = new JCheckBox("Vykdyti nestabdant");
	
	static JButton buttonRestart = new JButton("Perkrauti OS");
	static JButton buttonExecuteProc = new JButton("Vykdyti procesą");
	
	static JComboBox<String> boxVM = new JComboBox<String>();

	public static JTextPane textEmulator = new JTextPane() {{ this.setText("OS inicijuota"); }};
	public static JTextPane textOut = new JTextPane();
	public static JTextField textIn = new JTextField();
	
	public static String bufferIn = "";
	
	public static Kernel OS = new Kernel();
	public static Thread machine = new Thread(){
	    public void run(){
			if (checkNoSteps.isSelected()) {
				OS.executeOS();
			} else {
				OS.doStepOS();
			}
	    }
    };
	
	GUI() {
		
		machine.setPriority(Thread.MIN_PRIORITY);
		
		frame.getContentPane().setLayout(null);
		
		// * Labels *//
		
		labelMemory.setBounds(1071, 11, 60, 14);
		frame.getContentPane().add(labelMemory);
		
		labelProcList.setBounds(10, 11, 93, 14);
		frame.getContentPane().add(labelProcList);
		
		labelResList.setBounds(10, 257, 93, 14);
		frame.getContentPane().add(labelResList);

		labelEmulator.setBounds(10, 478, 130, 14);
		frame.getContentPane().add(labelEmulator);
		
		labelConsole.setBounds(559, 478, 93, 14);
		frame.getContentPane().add(labelConsole);
		
		// * Tables *//
		
		JScrollPane tableScroll_1 = new JScrollPane(listMemory);
		tableScroll_1.setBounds(1071, 30, 203, 555);
		frame.getContentPane().add(tableScroll_1);
		
		tableProc.setGridColor(new Color(200, 200, 200));
		tableProc.getTableHeader().setReorderingAllowed(false);
		tableProc.setShowVerticalLines(false);
		JScrollPane tableScroll_2 = new JScrollPane(tableProc);
		tableScroll_2.setBounds(10, 30, 1049, 197);
		frame.getContentPane().add(tableScroll_2);
		
		tableRes.setGridColor(new Color(200, 200, 200));
		tableRes.getTableHeader().setReorderingAllowed(false);
		tableRes.setShowVerticalLines(false);
		JScrollPane tableScroll_3 = new JScrollPane(tableRes);
		tableScroll_3.setBounds(10, 277, 1049, 190);
		frame.getContentPane().add(tableScroll_3);
		
		//* Check boxes *//
		
		checkRunning.setSelected(true);
		checkRunning.setBounds(10, 227, 97, 23);
		checkRunning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshGUI();
			}
		});
		frame.getContentPane().add(checkRunning);

		checkReady.setSelected(true);
		checkReady.setBounds(109, 227, 97, 23);
		checkReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshGUI();
			}
		});
		frame.getContentPane().add(checkReady);
		
		checkBlocked.setSelected(true);
		checkBlocked.setBounds(208, 227, 97, 23);
		checkBlocked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshGUI();
			}
		});
		frame.getContentPane().add(checkBlocked);
		
		checkNoSteps.setSelected(false);
		checkNoSteps.setBounds(130, 627, 120, 23);
		checkNoSteps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				if (checkNoSteps.isSelected()) {
					buttonExecuteProc.setEnabled(false);
					try {
						machine.start();
					} catch (Exception e) {
						machine.resume();
					}
				} else if (!checkNoSteps.isSelected()) {
					buttonExecuteProc.setEnabled(true);
					machine.suspend();
				}
				
				refreshGUI();
			}
		});
		frame.getContentPane().add(checkNoSteps);
		
		//* Buttons *//
		
		buttonExecuteProc.setBounds(10, 627, 120, 23);
		buttonExecuteProc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				OS.doStepOS();
				
				refreshGUI();
			}
		});
		frame.getContentPane().add(buttonExecuteProc);
		
		//* Combo Box *//
		
		boxVM.setBounds(1070, 596, 120, 22);
		boxVM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//listMemory.setListData(OS.VM.get(Integer.parseInt((String) boxVM.getSelectedItem())));
			}
		});
	    frame.getContentPane().add(boxVM);
		
		
		//* Console *//
		textEmulator.setEditable(false);
	    JScrollPane emulatorScroll = new JScrollPane(textEmulator);
	    emulatorScroll.setBounds(10, 498, 539, 87);
	    frame.getContentPane().add(emulatorScroll);
	    
	    textOut.setEditable(false);
	    JScrollPane consoleScroll = new JScrollPane(textOut);
	    consoleScroll.setBounds(559, 498, 500, 87);
	    frame.getContentPane().add(consoleScroll);
		
		textIn.setBounds(10, 596, 1049, 22);
		textIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				OS.channel1rdy = true;
				bufferIn = textIn.getText();
				PirmasisKanalas.buffer = bufferIn;
				printToEmulator("Įvestis: " + bufferIn);
				
				textIn.setText("");
				
				refreshGUI();
			}
		});
		textIn.setColumns(10);
		frame.getContentPane().add(textIn);
		
		refreshGUI();
	}
	
	public static void printToEmulator(String text) {
		if (textEmulator.getText().isEmpty()) {
			textEmulator.setText(text);
		} else {
			textEmulator.setText(textEmulator.getText() + "\n" + text);
		}
		
		textEmulator.setCaretPosition(textEmulator.getDocument().getLength());
	}
	
	public static void printToConsole(String text) {
		if (textOut.getText().isEmpty()) {
			textOut.setText(text);
		} else {
			textOut.setText(textOut.getText() + "\n" + text);
		}
		
		textOut.setCaretPosition(textOut.getDocument().getLength());
	}
	
	public static void refreshGUI() {
		tableProc.setModel(new tableProc(OS.tableProcData()));
		tableRes.setModel(new tableRes(OS.tableResData()));
		
		boxVM.removeAllItems();
		for(int i=0; i<OS.VM.size(); i++) {
			boxVM.addItem(Integer.toString(i));
		}
	}
}
