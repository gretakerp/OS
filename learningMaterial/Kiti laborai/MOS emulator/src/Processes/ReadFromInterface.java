package Processes;

import javax.annotation.Resources;

import Emulator.GUI;
import Emulator.Kernel;
import Resources.*;

public class ReadFromInterface extends Process {
	
	static String externalName = "ReadFromInterface";

	public ReadFromInterface(int id) {
		super(id, externalName);
	}
	
	public void executeProc() {
		
		while (state == "running") {

	        switch (savedState) {
		        case 0: { // tikrina ar jau įvyko įvestis
		            if (GUI.OS.channel1rdy) {
		            	GUI.OS.requestResource(ID, "PirmasisKanalas");
		            	state = "blocked";
		            	savedState++;
		            	
		            } else {
		            	state = "ready";
		            }
		            
		            break;
		        }
		        case 1: { // tikrinama įvestis ir atlaisvinami resursai
		        	GUI.OS.channel1rdy = false;
		        	String[] ivestis = PirmasisKanalas.buffer.split(" ");
		          	System.out.println(ivestis[0]);
		        	if (ivestis[0].equals("SHUTDOWN")) {
		        		GUI.OS.releaseResource("MOSPabaiga");     		
		        	} else if (ivestis[0].equals("Program")){
		        		GUI.OS.releaseResource("UzduotisIsorinejeAtmintyje");
		        		UzduotisIsorinejeAtmintyje.place = ivestis[1];
		        	} else {
		        		GUI.OS.releaseResource("VartotojoIvedimas");
		        		VartotojoIvedimas.ivestis = ivestis[1];
		        	}
		        	
		        	GUI.OS.releaseResource("PirmasisKanalas");
		            savedState = 0;
		            break;
		        }
	        }
		}

	}

}
