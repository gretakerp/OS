package Resources;

import Emulator.GUI;

public class PirmasisKanalas extends Resource {

	static String externalName = "PirmasisKanalas";
	static public String buffer = GUI.bufferIn;
	
	public PirmasisKanalas(int id, int parentID) {
		super(id, parentID, externalName);
	}
}
