
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class VartotojoAtmintiesElementas extends Elementas {
    private int takelioNr;
        public int getTakelioNr() {
            return takelioNr;
        }
    
    /** Creates a new instance of VartotojoAtmintis */
    public VartotojoAtmintiesElementas(String name, boolean reusable, int takelioNr) {
        super(name, reusable);
        this.takelioNr = takelioNr;
    }
    
    public String toString() {
        return "TakelioNr: "+takelioNr;
    }
    
}
