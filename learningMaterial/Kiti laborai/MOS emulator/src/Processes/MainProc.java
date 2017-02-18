package Processes;

import Emulator.GUI;
import Emulator.Kernel;
import Resources.*;

public class MainProc extends Process {

	static String externalName = "MainProc";
	
	public MainProc(int id) {
		super(id, externalName);
		state = "ready";
	}

	public void executeProc() {
		while (state == "running") {
	        switch (savedState) {
		        case 0: { // Blokavimas laukiant „Validi programa išorinėje atmintyje“ resurso
		        	GUI.OS.requestResource(ID, "ValidiUzduotisIsorinejeAtmintyje");
			        state = "blocked";
		            savedState++;
		            break;
		        }
		        case 1: { // Imti pirmą laisvą resursą ir su juo dirbti
		        	for (int i = 0; i < Kernel.resources.size(); i++) {
		        		if (Kernel.resources.get(i).usingBy == ID){
		        			ValidiUzduotisIsorinejeAtmintyje resursas = (ValidiUzduotisIsorinejeAtmintyje) Kernel.resources.get(i);

		        			if (resursas.vykdymoLaikas == 0) { //vykdymo laikas
		        				System.out.println("Vykdimolaikas 0");
		        				for (int j = 0; j < Kernel.processes.size(); j++) {
		        					if (Kernel.processes.get(j).ID == resursas.jobID){
		        						GUI.OS.destroyProcess(Kernel.processes.get(j));
		        						break;
		        					}
		        				}
		        			}
		        			else {
		        				GUI.OS.createProcess("JobGoverner", ID);
		        			}
		        			break;
		        		}        		
		        	}
		            savedState = 0;
		            break;
		        }
	        }

		}
	}
}
