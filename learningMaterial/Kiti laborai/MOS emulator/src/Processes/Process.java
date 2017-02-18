package Processes;

import java.util.ArrayList;
import java.util.List;

import Emulator.GUI;
import Resources.*;

public abstract class Process {
	
	public int ID; // unique process ID
	public String state = "ready";
	public String externalName;
	public int savedState = 0;
	public List<Resource> resCreated = new ArrayList<Resource>();
	public List<Process> procChildren = new ArrayList<Process>();

	public abstract void executeProc();
	
	Process(int ID, String name) {
		this.ID = ID;
		externalName = name;
	}
	
	public void execute() {
		GUI.printToEmulator("Vykdoma: (" + ID + ") " + externalName);
		executeProc();
	}
}
