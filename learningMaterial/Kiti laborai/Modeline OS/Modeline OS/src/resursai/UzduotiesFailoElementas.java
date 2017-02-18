
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class UzduotiesFailoElementas extends Elementas {
    private String source;
        public String getsource() {
            return source;
        }
        
    private String output;
        public String getOutput() {
            return output;
        }

    
    public UzduotiesFailoElementas(String name, boolean reusable, String source, String output) {
        super(name, reusable);
        this.source = source;
        this.output = output;
    }
    public String toString() {
        return "Source: " + source;
    }
    
}
