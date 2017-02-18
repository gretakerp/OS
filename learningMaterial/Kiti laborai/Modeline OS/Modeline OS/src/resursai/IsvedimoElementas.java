
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class IsvedimoElementas extends Elementas {
    
    private String str;
        public String getValue() {
           return str;
        }
    /**
     * Creates a new instance of EiluteElementas
     */
    public IsvedimoElementas(String name, boolean reusable, String str) {
        super(name, reusable);
        this.str = str;
    }

    public String toString() {
        return "String:"+str;
    }
    
}
