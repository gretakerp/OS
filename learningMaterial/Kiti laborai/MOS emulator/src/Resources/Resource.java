package Resources;

import java.util.ArrayList;
import java.util.List;

import Processes.Process;

public abstract class Resource {
	public int ID; // unique resource ID
	public String externalName;
	public int createdBy;
	public int usingBy;
	public List<Process> waitingProc = new ArrayList<Process>();
		
	Resource(int ID, int parentID, String externalName) {
		this.ID = ID;
		this.externalName = externalName;
		createdBy = parentID;
		usingBy = 0;
	}
	
	Resource(int ID, int parentID, String externalName, int usingBy) {
		this.ID = ID;
		this.externalName = externalName;
		createdBy = parentID;
		this.usingBy = usingBy;
	}
	
}
