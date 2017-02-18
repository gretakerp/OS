
package resursai;

import resursai.Elementas;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class Resursas {
    private int amount = 0;
        public int getAmount() {
            return amount;
        }
    
    private String name;
        public String getName() {
            return name;
        }
        
    private int id;
        public int getId() {
            return id;
        }
        
    private int pId;
        public int getPId() {
            return pId;
        }
        
    private boolean reusable;
        public boolean isReusable() {
            return reusable;
        }
    
    private LinkedList<Elementas> availableElements = new LinkedList<Elementas>();
        public LinkedList<Elementas> getAvailableElements() {
           return availableElements;
        }
        public void removeElement(Elementas ele) {
            availableElements.remove(ele);
            amount--;
        }
                
    private LinkedList<LaukiantisProcesas> waitingProcesses = new LinkedList<LaukiantisProcesas>();
        public LinkedList<LaukiantisProcesas> getWaitingProcesses() {
               return waitingProcesses;
        }
        public LaukiantisProcesas getWaitingProcess(int id) {
            Iterator i = waitingProcesses.iterator();
            while (i.hasNext()) {
                LaukiantisProcesas proc = (LaukiantisProcesas) i.next();
                if (proc.getId() == id) return proc;
            }
            return null;
        }
    
    /** Creates a new instance of Resursas */
    public Resursas(int id, int parent, String name, boolean reusable) {
        this.id = id;
        this.pId = parent;
        this.name = name;
        this.reusable = reusable;
    }

    public void addWaitingProcess(LaukiantisProcesas proc) {
        waitingProcesses.add(proc);
    }
    
    public void removeWaitingProcess(LaukiantisProcesas proc) {
        waitingProcesses.remove(proc);
    }

    public void removeWaitingProcess(int id) {
        Iterator i = waitingProcesses.iterator();
        while (i.hasNext()) {
            LaukiantisProcesas proc = (LaukiantisProcesas) i.next();
            if (proc.getId() == id) waitingProcesses.remove(proc);
        }
    }

    public void addResourceElement(Elementas ele) {
        availableElements.add(ele);
        amount++;
    }
    
    public Elementas takeResourceElement() {
        if (amount > 0) {
            amount--;
            Elementas res = (Elementas) availableElements.getFirst();
            availableElements.removeFirst();
            return res;
        }
        return null;
    }
    public Elementas takeResourceElement(int pId) {
        Iterator i = availableElements.iterator();
        while (i.hasNext()) {
            Elementas ele = (Elementas) i.next();
            if (ele.getTarget() == pId) {
                amount--;
                availableElements.remove(ele);
                return ele;
            }
        }
        return null;
    }
}