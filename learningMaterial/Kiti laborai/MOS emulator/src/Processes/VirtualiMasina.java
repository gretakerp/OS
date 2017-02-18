package Processes;

import Emulator.GUI;
import Emulator.Kernel;
import Resources.Pertraukimas;

public class VirtualiMasina extends Process {

	static String externalName = "VirtualiMasina";
	public int timer = 5;
	String[] virtual;
	
	public VirtualiMasina(int id) {
		super(id, externalName);
		state = "ready";
	}

	public void executeProc() {
		
		System.out.println("HERE");
		while (state == "running") {
			System.out.println("HRE2");
			if (timer == 0) {
				//GUI.OS.createResource("Pertraukimas", ID);
				state = "ready";
			}
			
			for (int i = 0; i < Kernel.resources.size(); i++) {
				if (Kernel.resources.get(i).createdBy == ID) {
					break;
				}
			}
			
		
			for (int i = 0; i < Kernel.VM.size(); i++) {
				virtual = Kernel.VM.get(i); 
				if (Integer.parseInt(virtual[virtual.length - 1]) == ID) {	
					break;
				}
			}
			
			System.out.println("Executing");
			executeVM();
			savedState++;
			timer--;
		}
	}
	
	
	public void executeVM() {
		String command = virtual[savedState];
		System.out.println("NOW COMMAND: " + command);
		
		switch (command) {
		case "HALT":
			cmdHL();
			break;
		}

	}
	
	public void cmdHL() {
			GUI.OS.createResource("Pertraukimas", ID);
			
			for (int i = 0; i < Kernel.resources.size(); i++) {
				if (Kernel.resources.get(i).createdBy == ID) {
					Pertraukimas pert = (Pertraukimas) Kernel.resources.get(i);
					pert.interruptType = "HALT";
					pert.ID = ID;
					break;
				}
			}
			
			state = "ready";
	
	}
}
