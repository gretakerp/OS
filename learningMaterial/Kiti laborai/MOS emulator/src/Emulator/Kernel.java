package Emulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Emulator.Tables.tableProc;
import Emulator.Tables.tableRes;
import Processes.Process;
import Processes.*;
import Resources.*;

public class Kernel {
	
	public static List<Process> processes = new ArrayList<Process>();
	public static List<Resource> resources = new ArrayList<Resource>();
	public static List<String[]> VM = new ArrayList<String[]>();
	public static List<int[]> pagingTable = new ArrayList<int[]>();
	public static List<String[]> nonExsistingRes = new ArrayList<String[]>();

	public boolean channel1rdy = false;
	public boolean channel2rdy = false;
	public boolean channel3rdy = false;
	
	Kernel() {
		createProcess("StartStop");
	}
	
	public void executeOS() {

		while(GUI.checkNoSteps.isSelected()) {
			doStepOS();
			try {
				GUI.machine.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void doStepOS() {
		
		Process running = getFirstReady();
		GUI.printToEmulator("Planuotojas: (" + running.ID + ") " + running.externalName);
		
		running.execute();
		
		
		manageExecutedProc(running); // set the proc to the queue end or delete if terminated
		manageResources();
		
		if (processes.size() > 0) {
			getFirstReady().state = "running";
			GUI.refreshGUI();
		} else {
			GUI.printToEmulator("OS išjungta");
			GUI.tableProc.setModel(new tableProc(new String[0][3]));
			GUI.tableRes.setModel(new tableRes(new String[0][5]));
		}
		
		GUI.refreshGUI();
	}
	
	public Process getFirstReady() {
		int i = 0;

		while (i < processes.size()) {
			if ((processes.get(i).state == "running") || (processes.get(i).state == "ready")){
				break;
			}
			i++;
		}
		
		return processes.get(i);
	}
	
	public void manageResources() {
		
		GUI.printToEmulator("Suveikė res. paskirstytojas");
		;
		for (int i = 0; i < nonExsistingRes.size(); i++) {
			if (doesResExist(nonExsistingRes.get(i)[1])) {
				getRes(nonExsistingRes.get(i)[1]).waitingProc.add(getProc(Integer.parseInt(nonExsistingRes.get(i)[0])));
				nonExsistingRes.remove(i);
			}
		}

		for (int i = 0; i < resources.size(); i++) {
			if (resources.get(i).usingBy == 0 && resources.get(i).waitingProc.size() > 0){
				resources.get(i).usingBy = resources.get(i).waitingProc.get(0).ID;
				resources.get(i).waitingProc.get(0).state = "ready";
				resources.get(i).waitingProc.remove(0);
			} else {
				try {
					if (resources.get(i).usingBy > 0) {
						getRes(resources.get(i).usingBy);
					}
				} catch (Exception e) {
					resources.get(i).usingBy = 0;
				}
			}
		}
	}
	
// ----------------------------------------------------------------------------------------------------------------
//	 								PROCESS PRIMITIVES
// ----------------------------------------------------------------------------------------------------------------

	public void createProcess(String name) {
		createProcess(name, 0);
	}
	
	public void createProcess(String name, int parentID) {
		int id = getNewProcID(processes);
		boolean added = false;
		
		switch (name) {
		case "StartStop":
			processes.add(new StartStop(id));
			added = true;
			break;
		case "ReadFromInterface":
			processes.add(new ReadFromInterface(id));
			added = true;
			break;
		case "Validator":
			processes.add(new Validator(id));
			added = true;
			break;
		case "MainProc":
			processes.add(new MainProc(id));
			added = true;
			break;
		case "JobGoverner":
			processes.add(new JobGoverner(id));
			added = true;
			break;
		case "VirtualiMasina":
			processes.add(new VirtualiMasina(id));
			added = true;
			break;
		}
		
		
		
		if (added) {
			if (parentID > 0 ) {
				getProc(parentID).procChildren.add(getProc(id));
			}
			GUI.printToEmulator("Sukurtas proc. (" + id + ") " + name);
		}
	}

	public void destroyProcess(Process process) {
		List<Process> procToDelete = process.procChildren;
		
		for (int i = 0; i < procToDelete.size(); i++) {
			destroyProcess(procToDelete.get(i));
		}
		
		List<Resource> resToDelete = process.resCreated;
		
		for (int i = 0; i < resToDelete.size(); i++) {
			try {
				destroyResource(resToDelete.get(i).ID);
			} catch (Exception e) {}
		}
		
		
		//TODO: ištrinti ir iš fake notExsistingRes.get();
		process.state = "terminated";
		processes.remove(process);
	}
	
	
// ----------------------------------------------------------------------------------------------------------------
//		 								RESOURCES PRIMITIVES
// ----------------------------------------------------------------------------------------------------------------

	public void createResource(String name, int parentID) {
		int id = getNewResID(resources);
		boolean added = false;
		
		switch (name) {
		case "Atmintis":
			resources.add(new Atmintis(id, parentID));
			added = true;
			break;
		case "PirmasisKanalas":
			resources.add(new PirmasisKanalas(id, parentID));
			added = true;
			break;
		case "AntrasisKanalas":
			resources.add(new AntrasisKanalas(id, parentID));
			added = true;
			break;
		case "TreciasisKanalas":
			resources.add(new TreciasisKanalas(id, parentID));
			added = true;
			break;
		case "UzduotisIsorinejeAtmintyje":
			resources.add(new UzduotisIsorinejeAtmintyje(id, parentID));
			added = true;
			break;
		case "ValidiUzduotisIsorinejeAtmintyje":
			resources.add(new ValidiUzduotisIsorinejeAtmintyje(id, parentID));
			added = true;
			break;
		case "VartotojoIvedimas":
			resources.add(new VartotojoIvedimas(id, parentID));
			added = true;
			break;
		case "MOSPabaiga":
			resources.add(new MOSPabaiga(id, parentID));
			added = true;
			break;
		case "Pertraukimas":
			resources.add(new Pertraukimas(id, parentID));
			added = true;
			break;
		}
		
		
		if (added) {
			getProc(parentID).resCreated.add(getRes(id));
			GUI.printToEmulator("Proc. (" + parentID + ") sukūrė res. [" + id + "] " + name);
		}
	}

	public void destroyResource(int id) {
		Resource resToDelete = getRes(id);
		getProc(resToDelete.ID).resCreated.remove(resToDelete);
		resources.remove(resToDelete);
	}
	
	public void requestResource(int id, String name) {
		if (doesResExist(name)) {
			getRes(name).waitingProc.add(getProc(id));
		} else {
			nonExsistingRes.add(new String[] {Integer.toString(id), name});
		}
	}
	
	public void releaseResource(String name) {
		getRes(name).usingBy = 0;
	}
	
	public static int getNewProcID(List<Process> list) {
		int newID = 0;
		
		do {
			newID++;
		} while (doesExistProcID(list, newID));
		
		return newID;
	}
	
	private static int getNewResID(List<Resource> list) {
		int newID = 0;
		
		do {
			newID++;
		} while (doesExistResID(list, newID));
		
		return newID;
	}
	
	private static boolean doesExistResID(List<Resource> list, int value) {
		boolean found = false;
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).ID == value) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	private static boolean doesExistProcID(List<Process> list, int value) {
		boolean found = false;
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).ID == value) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public Process getProc(int id) {
		int i = 0;
		
		if (id != -1) {
			for (i = 0; i < processes.size(); i++) {
					if (processes.get(i).ID == id) {
						break;
					}
				}
				if (processes.size() >= i + 1) {
				return processes.get(i);
				} else
				{
				return processes.get(i-1);
				}
				
				
		} else {
			return getProc(1);
		}
		
	}
	
	public Resource getRes(int id) {
		int i;
		
		for (i = 0; i < resources.size(); i++) {
			if (resources.get(i).ID == id) {
				break;
			}
		}
		
		return resources.get(i);
	}
	
	public Resource getRes(String name) {
		int i;
		
		for (i = 0; i < resources.size(); i++) {
			if (resources.get(i).externalName == name) {
				break;
			}
		}
		
		return resources.get(i);
	}
	
	public static boolean doesResExist(String name) {
		boolean found = false;
		
		for (int i = 0; i < resources.size(); i++) {
			if (resources.get(i).externalName == name) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public boolean doesProcHasRes(int procId, String resName) {
		if (doesResExist(resName)) {
			if (getRes(resName).usingBy == procId) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private void manageExecutedProc(Process running) {
		if (running.state == "terminated") {
			processes.remove(running);
		} else {
			processes.remove(running);
			processes.add(running);
		}
	}
	
	
// ----------------------------------------------------------------------------------------------------------------
// 								TABLES DATA GENERATING
// ----------------------------------------------------------------------------------------------------------------

	
	public String[][] tableProcData() { // creates array[][] for table printing
		String[][] data = new String[processes.size()][3];
		int added = 0;

		for (int i = 0; i < processes.size(); i++) {
			if (
					(GUI.checkRunning.isSelected() && processes.get(i).state == "running") ||
					(GUI.checkReady.isSelected() && processes.get(i).state == "ready") ||
					(GUI.checkBlocked.isSelected() && processes.get(i).state == "blocked")
			) {
				data[added][0] = Integer.toString(processes.get(i).ID);
				data[added][1] = processes.get(i).externalName;
				data[added][2] = processes.get(i).state;
				
				added++;
			}
		}
		
		data = Arrays.copyOf(data, added);

		return data;
	}
	
	public String[][] tableResData() { // creates array[][] for table printing
		String[][] data = new String[resources.size()][5];

		for (int i = 0; i < resources.size(); i++) {
			data[i][0] = Integer.toString(resources.get(i).ID);
			data[i][1] = resources.get(i).externalName;
			
			if (resources.get(i).usingBy == 0) {
				data[i][2] = "Laisvas";
			} else {
				data[i][2] = "Užimtas";
			}
			
			data[i][3] = "(" + getProc(resources.get(i).createdBy).ID + ") " + getProc(resources.get(i).createdBy).externalName;
			
			if (resources.get(i).usingBy > 0) {
				data[i][4] = "(" + getProc(resources.get(i).usingBy).ID + ") " + getProc(resources.get(i).usingBy).externalName;
			} else {
				data[i][4] = "";
			}
			
		}

		return data;
	}
	
	public String[][] tsadasasdablesResData() { // creates array[][] for table printing
		String[][] data = new String[resources.size()][1];

		for (int i = 0; i < resources.size(); i++) {
			data[i][0] = Integer.toString(resources.get(i).ID);
			data[i][1] = resources.get(i).externalName;
			
			if (resources.get(i).usingBy == 0) {
				data[i][2] = "Laisvas";
			} else {
				data[i][2] = "Užimtas";
			}
			
			data[i][3] = "(" + getProc(resources.get(i).createdBy).ID + ") " + getProc(resources.get(i).createdBy).externalName;
			
			if (resources.get(i).usingBy > 0) {
				data[i][4] = "(" + getProc(resources.get(i).usingBy).ID + ") " + getProc(resources.get(i).usingBy).externalName;
			} else {
				data[i][4] = "";
			}
			
		}

		return data;
	}
}
