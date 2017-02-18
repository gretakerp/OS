
package modelineos;

import procesai.Busena;
import procesai.Procesas;
import kompiuteris.RealiMasina;
import java.util.Iterator;
import kompiuteris.ZurnalizavimoIrenginys;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class ProcesuPlanuotojas {
    
    /** Creates a new instance of ProcesuPlanuotojas */
    public ProcesuPlanuotojas() {
    }
    
    public static void planuok (Busena busena) {
        ZurnalizavimoIrenginys.println("Iskviestas procesu planuotojas.");
        Procesas activeProc = RealiMasina.kernel.getActiveProc();
        if (activeProc != null) {
            Procesas proc = activeProc;
            proc.getRegistrai().C = RealiMasina.cpu.C;
            proc.getRegistrai().IC = RealiMasina.cpu.IC;
            proc.getRegistrai().PTR = RealiMasina.cpu.PTR;
            proc.getRegistrai().R = RealiMasina.cpu.R;
            proc.getRegistrai().SP = RealiMasina.cpu.SP;
            proc.getRegistrai().TMR = RealiMasina.cpu.TMR;
            
            if (busena != null) activeProc.setState(busena);{
                if (activeProc.getState() == Busena.PASIRUOSES) {
                    RealiMasina.kernel.addReadyProcess(activeProc);
                }
                else RealiMasina.kernel.addProcess(activeProc);
            }
            RealiMasina.kernel.setActiveProc(null);
        }
        if (RealiMasina.kernel.getReadyProcesses().size() > 0) {
            Procesas proc, maxproc = null;
            int maxpriority = 0;
            Iterator i = RealiMasina.kernel.getReadyProcesses().iterator();
            while (i.hasNext()) {
                if ((proc = (Procesas)i.next()).getPriority() > maxpriority) {
                    maxpriority = proc.getPriority();
                    maxproc = proc;
                }
            }
            RealiMasina.kernel.removeReadyProcess(maxproc);
            RealiMasina.kernel.switchProcess(maxproc);
        }
    }
}
