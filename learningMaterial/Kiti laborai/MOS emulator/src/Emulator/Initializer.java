package Emulator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Initializer {

	public static GUI window;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					window = new GUI();
					window.frame.setResizable(false);
					window.frame.setVisible(true);
					window.frame.setBounds(20, 20, 1300, 700);
					window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
