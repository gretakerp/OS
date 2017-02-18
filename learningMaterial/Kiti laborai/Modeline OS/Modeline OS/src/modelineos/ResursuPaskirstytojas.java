
package modelineos;

import resursai.*;
import resursai.Elementas;
import java.util.Iterator;
import java.util.LinkedList;
import kompiuteris.RealiMasina;
import procesai.Procesas;
import kompiuteris.ZurnalizavimoIrenginys;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

// skirstomasis irenginys
public class ResursuPaskirstytojas {
    
    /** Creates a new instance of Switchgear */
    public ResursuPaskirstytojas() {
    }
    public static void skirstyk() {
        ZurnalizavimoIrenginys.println("Iskviestas resursu skirstytojas.");
        Iterator i = RealiMasina.kernel.getResources().iterator();
        while (i.hasNext()) {
            Resursas res = (Resursas) i.next();
            int amount = res.getAmount();
            if (amount == 0) continue;
            
            Iterator e = res.getAvailableElements().iterator();
            LinkedList<Elementas> elementsToBeRemoved = new LinkedList<Elementas>();
            while (e.hasNext()) {
                //out.println("Available: "+res.getAvailableElements().size());
                Elementas ele = (Elementas) e.next();
                if (ele.getTarget() != -1) {
                    if (res.getWaitingProcess(ele.getTarget()) != null) {                        
                        Procesas procesas = RealiMasina.kernel.getProcess(ele.getTarget());
                        elementsToBeRemoved.add(ele);
                        procesas.addOwnedResourceElement(ele);
                        ZurnalizavimoIrenginys.println("Procesui ("+procesas.getName()+", "+procesas.getId()+") isskirtas resursas ("+ele.getName()+").");
                        LaukiantisProcesas proc =  res.getWaitingProcess(ele.getTarget());
                        proc.decAmount();
                        if (proc.getAmount() == 0) {
                            res.removeWaitingProcess(proc);
                            procesas.unblock();
                        }
                    }
                }
                // @todo target = -1
                else if (res.getWaitingProcesses().size() > 0) {
                    LaukiantisProcesas proc = res.getWaitingProcesses().getFirst();
                    Procesas procesas = RealiMasina.kernel.getProcess(proc.getId());
                    procesas.addOwnedResourceElement(ele);
                    ZurnalizavimoIrenginys.println("Procesui ("+procesas.getName()+", "+procesas.getId()+") isskirtas resursas ("+ele.getName()+").");
                    elementsToBeRemoved.add(ele);
                    proc.decAmount();
                    if (proc.getAmount() == 0) {
                        res.removeWaitingProcess(proc);
                        procesas.unblock();
                    }
                }
            }
            for (Elementas ele : elementsToBeRemoved)
            {
                res.removeElement(ele);
            }
        }
    }
    
}
