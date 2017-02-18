package Processes;

import Emulator.GUI;
public class StartStop extends Process {
	
	static String externalName = "StartStop";
	
	public StartStop(int id) {
		super(id, externalName);
		state = "running";
	}

	public void executeProc() {
		
		while (state == "running") {
	        switch (savedState) {
		        case 0: { // resursų kūrimas
		        	GUI.OS.createResource("Atmintis", ID);
		        	GUI.OS.createResource("PirmasisKanalas", ID);
		        	GUI.OS.createResource("AntrasisKanalas", ID);
		        	GUI.OS.createResource("TreciasisKanalas", ID);
		        	GUI.OS.createResource("UzduotisIsorinejeAtmintyje", ID);
		        	GUI.OS.createResource("VartotojoIvedimas", ID);
		        	GUI.OS.createResource("MOSPabaiga", ID);
		            savedState++;
		            break;
		        }
		        case 1: { // procesų kūrimas
		            GUI.OS.createProcess("ReadFromInterface", ID);
		            GUI.OS.createProcess("Validator", ID);
		            GUI.OS.createProcess("MainProc", ID);
		            savedState++;
		            break;
		        }
		        case 2: { // prašom MOSPabaiga resurso
		            GUI.OS.requestResource(ID, "MOSPabaiga");
		            state = "blocked";
		            savedState++;
		            break;
		        }
		        case 3: { // sisteminių procesų naikinimas
		        	GUI.OS.destroyProcess(this);
		            break;
		        }
		
	        }
		}
	}
}