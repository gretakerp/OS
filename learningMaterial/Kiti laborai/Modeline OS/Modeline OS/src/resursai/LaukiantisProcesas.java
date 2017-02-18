
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class LaukiantisProcesas {
    private int id;
        public int getId() {
            return id;
        }
    
    private int amount = 0;
        public int getAmount() {
            return amount;
        }
        public void decAmount() {
            amount--;
        }
    /** Creates a new instance of WaitingProcess */
    public LaukiantisProcesas(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }
    
}
