
package procesai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public abstract class AbstractProcess {
    public Procesas itself;
    public abstract void doit();

    public Procesas getParent() {
        return itself;
    }

    public void setParent(Procesas parent) {
        this.itself = parent;
    }
    public int state = 0;
}
