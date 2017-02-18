
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class PertraukimoElementas extends Elementas {
    // IA - Invalid Address
    // IOC - Invalid Operation Code
    // OF - OverFlow
    public enum PertraukimoTipas {IA, IOC, OF, GD, PD, HALT, INPUT};
    
    private PertraukimoTipas trikis;
        public PertraukimoTipas getTrikis() {
            return trikis;
        }
    private int address;
        public int getAddress() {
            return address;
        }
    private String output = null;
        public String getOutput() {
            return output;
        }
    /** Creates a new instance of PertraukimoElementas */
    public PertraukimoElementas (String name, boolean reusable, PertraukimoTipas trikis) {
        super(name, reusable);
        this.trikis = trikis;
    }
    
    public PertraukimoElementas (String name, boolean reusable, PertraukimoTipas trikis, int address) {
        super(name, reusable);
        this.trikis = trikis;
        this.address = address;
    }
    
    public PertraukimoElementas (String name, boolean reusable, PertraukimoTipas trikis, String output) {
        super(name, reusable);
        this.trikis = trikis;
        this.output = output;
    }
    
    public String toString() {
        return "Trikis:" + trikis;
    }
    
}
