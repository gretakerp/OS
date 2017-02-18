package Resources;

public class Pertraukimas extends Resource {
	
	static String externalName = "Pertraukimas";
	
	public static String interruptType = "";
	public static int whichVM = 0;
	public static int ID = 0;
	
	public Pertraukimas(int id, int parentID) {
		super(id, parentID, externalName);
	}
}
