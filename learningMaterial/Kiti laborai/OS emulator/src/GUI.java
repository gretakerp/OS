 import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class GUI {

	private static Emulator main = new Emulator();
	
	private static JFrame frame;
	private static JTable table;
	public static JTextField textField = new JTextField();
	private static JTextPane txtConsole = new JTextPane();
	private static JScrollPane tableScroll;
	

	private static JButton btnRodytiVMEnd = new JButton("VM pabaiga");
	private static JTextField fldRodyti = new JTextField(Integer.toString(main.IC));
	private static JLabel lblReiksme = new JLabel("Reikšmė: 0");
	
	/* Registers */
	private static JLabel lblPTR = new JLabel();
	private static JLabel lblIC = new JLabel();
	private static JLabel lblR = new JLabel();
	private static JLabel lblSF = new JLabel();
	private static JLabel lblMODE = new JLabel();
	private static JLabel lblTI = new JLabel();
	private static JLabel lblPI = new JLabel();
	private static JLabel lblSI = new JLabel();
	private static JLabel lblIOI = new JLabel();
	private static JLabel lblCHST = new JLabel();
	public static GUI window;
	
	private static JFileChooser programFileChooser = new JFileChooser();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					window = new GUI();
					window.frame.setResizable(false);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI() {		
		initialize();
	}

	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 50, 1150, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// ---------------------------------------------------------------------------------------------------------------------
		// 								MEMORY TABLE
		// ---------------------------------------------------------------------------------------------------------------------


		JLabel lblAtminits = new JLabel("Atminits:");
		lblAtminits.setBounds(792, 11, 46, 14);
		frame.getContentPane().add(lblAtminits);
		
		String fields[] = { "Žodis", "1b", "2b", "3b", "4b" };
		table = new JTable(main.memoryTable(), fields) {
			DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
			{
				renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
			}

			@Override
			public TableCellRenderer getCellRenderer(int arg0, int arg1) {
				return renderRight;
			}
			
			public boolean isCellEditable(int row, int column) {                
                return false;               
			};
		};
		table.setGridColor(new Color(200, 200, 200));
		table.getTableHeader().setReorderingAllowed(false);
		table.setShowVerticalLines(false);
		table.setTableHeader(null);
		table.setModel(new memoryTable(main.memoryTable()));
		table.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable) e.getSource();
				lblReiksme.setText("Reikšmė: " + Long.toString(main.getNumberValue(target.getSelectedRow())));
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
    
		tableScroll = new JScrollPane(table); // add table to scroll panel
		tableScroll.setBounds(781, 33, 203, 529);
		frame.getContentPane().add(tableScroll);
		
		// ---------------------------------------------------------------------------------------------------------------------
		// 								REGISTERS
		// ---------------------------------------------------------------------------------------------------------------------


		lblPTR.setBounds(10, 11, 89, 14);
		frame.getContentPane().add(lblPTR);

		lblIC.setBounds(142, 11, 46, 14);
		frame.getContentPane().add(lblIC);

		lblR.setBounds(10, 33, 89, 14);
		frame.getContentPane().add(lblR);

		lblSF.setBounds(142, 33, 46, 14);
		frame.getContentPane().add(lblSF);
		
		lblMODE.setBounds(538, 11, 89, 14);
		frame.getContentPane().add(lblMODE);
		
		lblCHST.setBounds(538, 33, 120, 14);
		frame.getContentPane().add(lblCHST);

		lblTI.setBounds(274, 11, 46, 14);
		frame.getContentPane().add(lblTI);

		lblPI.setBounds(274, 33, 46, 14);
		frame.getContentPane().add(lblPI);

		lblSI.setBounds(406, 11, 46, 14);
		frame.getContentPane().add(lblSI);

		lblIOI.setBounds(406, 33, 46, 14);
		frame.getContentPane().add(lblIOI);
		
		// ---------------------------------------------------------------------------------------------------------------------
		// 								EMULATOR BUTTONS
		// ---------------------------------------------------------------------------------------------------------------------

		JButton btnIkrautiPrograma = new JButton("Įkrauti programą");
		btnIkrautiPrograma.setBounds(10, 538, 113, 23);
		btnIkrautiPrograma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionChoose(arg0);
			}
		});
		frame.getContentPane().add(btnIkrautiPrograma);

		JButton btnIvykdytiVM = new JButton("Įvykdyti VM");
		btnIvykdytiVM.setBounds(133, 538, 89, 23);
		btnIvykdytiVM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionIvykdyti(arg0);
			}
		});
		frame.getContentPane().add(btnIvykdytiVM);

		JButton btnIvykdytiZingsni = new JButton("Vykdyti per 1 žingsnį");
		btnIvykdytiZingsni.setBounds(232, 538, 131, 23);
		btnIvykdytiZingsni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionVykdytiZingsni(arg0);
			}
		});
		frame.getContentPane().add(btnIvykdytiZingsni);

		JButton btnPerkrautiEmuliatori = new JButton("Perkrauti emuliatorių");
		btnPerkrautiEmuliatori.setBounds(375, 538, 131, 23);
		btnPerkrautiEmuliatori.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionPerkrauti(arg0);
			}
		});
		frame.getContentPane().add(btnPerkrautiEmuliatori);
		
		
		// ---------------------------------------------------------------------------------------------------------------------
		// 								MEMORY TABLE JUMPS SIDE
		// ---------------------------------------------------------------------------------------------------------------------

		JButton btnRodytiIC = new JButton("IC");
		btnRodytiIC.setBounds(1000, 33, 128, 23);
		btnRodytiIC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jumpTo();
				fldRodyti.setText(Integer.toString(main.pagingConvert(main.IC)));
			}
		});
		frame.getContentPane().add(btnRodytiIC);
		
		JButton btnRodytiVMBegin = new JButton("VM pradžia");
		btnRodytiVMBegin.setBounds(1000, 66, 128, 23);
		btnRodytiVMBegin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jumpTo(main.newVMfrom * 10);
				fldRodyti.setText(Long.toString(main.newVMfrom * 10));
			}
		});
		frame.getContentPane().add(btnRodytiVMBegin);
		
		btnRodytiVMEnd.setBounds(1000, 99, 128, 23);
		btnRodytiVMEnd.setEnabled(false);
		btnRodytiVMEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jumpTo(main.newVMfrom * 10 + main.PTR[0] * 10);
				fldRodyti.setText(Long.toString(main.newVMfrom * 10 + main.PTR[0] * 10 * 10 - 1));
			}
		});
		frame.getContentPane().add(btnRodytiVMEnd);
		
		JButton btnRodytiPsl = new JButton("Puslapiavimo lentelė");
		btnRodytiPsl.setBounds(1000, 132, 128, 23);
		btnRodytiPsl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jumpTo(main.pageTableFrom);
				fldRodyti.setText(Integer.toString(main.pageTableFrom));
			}
		});
		frame.getContentPane().add(btnRodytiPsl);
		
		JButton btnRodytiPTR = new JButton("PTR lentelė");
		btnRodytiPTR.setBounds(1000, 165, 128, 23);
		btnRodytiPTR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jumpTo(main.PTRTableFrom);
				fldRodyti.setText(Integer.toString(main.PTRTableFrom));
			}
		});
		frame.getContentPane().add(btnRodytiPTR);
		
		JButton btnRodytiINT = new JButton("Pertraukimų lentelė");
		btnRodytiINT.setBounds(1000, 198, 128, 23);
		btnRodytiINT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jumpTo(main.interruptTableFrom);
				fldRodyti.setText(Integer.toString(main.interruptTableFrom));
			}
		});
		frame.getContentPane().add(btnRodytiINT);
		
		fldRodyti.setBounds(1000, 231, 128, 23);
		fldRodyti.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {  
					e.consume();
				}
			}
		});
		fldRodyti.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				changeRodyti();
			}

			public void insertUpdate(DocumentEvent e) {
				changeRodyti();
			}

			public void removeUpdate(DocumentEvent e) {
				changeRodyti();
			}
		});
		
		fldRodyti.setHorizontalAlignment(SwingConstants.RIGHT);
		frame.getContentPane().add(fldRodyti);
		
		lblReiksme.setBounds(1000, 256, 128, 23);
		frame.getContentPane().add(lblReiksme);
		
		// ---------------------------------------------------------------------------------------------------------------------
		// 								CONSOLE
		// ---------------------------------------------------------------------------------------------------------------------

		txtConsole.setText("EMULATOR: Emuliatorius paleistas sėkmingai");
		txtConsole.setEditable(false);
	    JScrollPane consoleScroll = new JScrollPane(txtConsole);
	    consoleScroll.setBounds(10, 58, 761, 438);
	    frame.getContentPane().add(consoleScroll);

		textField.setBounds(62, 507, 709, 20);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (main.CHST[0] == 1) {
					synchronized (textField) {
						main.IOI = 1;
						main.buffer = textField.getText();
						textField.notify();
		            }
				}
			}
		});
		
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblKonsol = new JLabel("Konsolė:");
		lblKonsol.setBounds(15, 510, 41, 14);
		frame.getContentPane().add(lblKonsol);
		
		refreshGUI();
	}
	
	public void addConsole(String text) { // text output to the console
		txtConsole.setText(txtConsole.getText() + "\n" + text);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------
	// 								ACTION LISTENERS
	// ---------------------------------------------------------------------------------------------------------------------

	private void actionChoose(ActionEvent arg0) {
		programFileChooser.showDialog(null, "Pasirinkti");
		addConsole("EMULATOR: Pasirinktas programos failas: " + programFileChooser.getSelectedFile().getAbsolutePath());
		File programFile = new File(programFileChooser.getSelectedFile().getAbsolutePath());
		
		main.loadVM(programFile);
		btnRodytiVMEnd.setEnabled(true);
		addConsole("EMULATOR: Programa įkrauta");
		refreshGUI();
		int row = main.getWordNum();
		table.setRowSelectionInterval(row, row);
		table.scrollRectToVisible(table.getCellRect(row + 15, 0, true));
	}
	
	private void actionPerkrauti(ActionEvent arg0) {
		main = new Emulator();
		addConsole("EMULATOR: Emuliatorius perkrautas");
		refreshGUI();
	}
	
	private void actionVykdytiZingsni(ActionEvent arg0) {
		
		if (main.PTR[0] != 0) { // checks if VM is loaded
			addConsole("EMULATOR: Vykdoma viena VM komanda");
			
			Runnable r1 = new Runnable() {
				  public void run() {
				    	main.executeVM();
				    	refreshGUI();
				  }
				};
				
			Thread thread = new Thread(r1);
			thread.start();		
		} else {
			addConsole("EMULATOR: Įkraukite programą, norėdami vykdyti VM");
		}
	}
	
	private void actionIvykdyti(ActionEvent arg0) {
		if (main.PTR[0] != 0) { // checks if VM is loaded
			addConsole("EMULATOR: Pradedama VM");
			while(main.executeVM()){
				refreshGUI();
			}
			addConsole("EMULATOR: VM įvykdyta");
		} else {
			addConsole("EMULATOR: Įkraukite programą, norėdami įvykdyti VM");
		}
	}
	
	private void changeRodyti() {
		String text = fldRodyti.getText();
		int value;
		
		if (text.isEmpty()) {
			value = 0;
		} else {
			value = Integer.parseInt(text);
			if (value > 65535) {
				value = 65535;
			}
		}
		
		lblReiksme.setText("Reikšmė: " + Long.toString(main.getNumberValue(value)));
		jumpTo(value);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------
	// 								VARIOUS COMMANDS
	// ---------------------------------------------------------------------------------------------------------------------

	public static void refreshGUI() {
		table.setModel(new memoryTable(main.memoryTable()));
		lblPTR.setText("PTR = " + Integer.toString(main.PTR[0]) + "  " + Integer.toString(main.PTR[1]));
		lblIC.setText("IC = " + Integer.toString(main.IC));
		lblR.setText("R = " + Integer.toString(main.R));
		lblSF.setText("SF = " + Integer.toString(main.SF));
		lblMODE.setText("MODE = " + Integer.toString(main.MODE));
		lblTI.setText("TI = " + Integer.toString(main.TI));
		lblPI.setText("PI = " + Integer.toString(main.PI));
		lblSI.setText("SI = " + Integer.toString(main.SI));
		lblIOI.setText("IOI = " + Integer.toString(main.IOI));
		lblCHST.setText("CHST[1,2,3] = " + Integer.toString(main.CHST[0]) + "  " + Integer.toString(main.CHST[1]) + "  " + Integer.toString(main.CHST[2]));
		
		jumpTo();
	}
	
	private static void jumpTo() {
		jumpTo(main.getWordNum());
	}
	
	private static void jumpTo(int row) {
		table.setRowSelectionInterval(row, row);
		table.scrollRectToVisible(table.getCellRect(row + 20, 0, false));
		table.scrollRectToVisible(table.getCellRect(row - 10, 0, false));
	}
}
