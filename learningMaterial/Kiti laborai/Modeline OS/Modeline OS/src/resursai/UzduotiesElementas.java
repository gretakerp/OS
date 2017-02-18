
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class UzduotiesElementas extends Elementas {
    public enum UzduotiesTipas {KURTI, NAIKINTI, UZKRAUTI };
    
    private UzduotiesTipas veiksmas;
    private String input, source;
    private int id;
    /** Creates a new instance of UzduotisElementas */
    public UzduotiesElementas (String name, boolean reusable, UzduotiesTipas veiksmas, int id) {
        super(name, reusable);
        this.veiksmas = veiksmas;
        this.id = id;
    }
    
    public UzduotiesElementas (String name, boolean reusable, UzduotiesTipas veiksmas, String input) {
        super(name, reusable);
        this.veiksmas = veiksmas;
        this.input = input;
    }
	
	public UzduotiesElementas (String name, boolean reusable, UzduotiesTipas veiksmas, String input, String source) {
        super(name, reusable);
        this.veiksmas = veiksmas;
        this.input = input;
		this.source = source;
    }

//********************************geteriai*************************************
    public UzduotiesTipas getVeiksmas() {
        return veiksmas;
    }

    public String getInput() {
        return input;
    }
	
	public String getSource() {
		return source;
	}

    public int getId() {
        return id;
    }
    public String toString() {
        return "UzduotiesTipas:"+veiksmas+" "+input;
    }
    
}
