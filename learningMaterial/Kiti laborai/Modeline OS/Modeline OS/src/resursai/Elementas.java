
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public abstract class Elementas {
    protected String name;
        public String getName() {
            return name;
        }
		
    protected int target = -1;
        public int getTarget() {
            return target;
        }
        public void setTarget (int target) {
            this.target = target;
        }
		
    protected boolean reusable;
        public boolean isReusable() {
            return reusable;
        }
		
    /** Creates a new instance of Elementas */
    public Elementas(String name, boolean reusable) {
        this.name = name;
        this.reusable = reusable;
    }   
	
    public abstract String toString();
}
