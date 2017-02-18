
package modelineos;

import procesai.Busena;
import procesai.Procesas;
import kompiuteris.RealiMasina;
import resursai.PertraukimoElementas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class PertraukimuApdorojimas {

    /**
     * Creates a new instance of Interrupt
     */
    public PertraukimuApdorojimas() {
    }

    public static void timer() {
        RealiMasina.cpu.MODE = 'S';
        Procesas activeProc = RealiMasina.kernel.getActiveProc();
        if (activeProc != null) {
            activeProc.decPriority();
            if (activeProc.getPriority() == 80 || activeProc.getPriority() == 0) {
                activeProc.restorePriority();
            }
        }
        ProcesuPlanuotojas.planuok(Busena.PASIRUOSES);
    }

    public static void invalidAddress(int pId) {
        RealiMasina.cpu.MODE = 'S';
        RealiMasina.kernel.freeResource(new PertraukimoElementas("Pertraukimas", false, PertraukimoElementas.PertraukimoTipas.IA), pId);
    }

    public static void invalidOperationCode(int pId) {
        RealiMasina.cpu.MODE = 'S';
        RealiMasina.kernel.freeResource(new PertraukimoElementas("Pertraukimas", false, PertraukimoElementas.PertraukimoTipas.IOC), pId);
    }

    public static void overFlow(int pId) {
        RealiMasina.cpu.MODE = 'S';
        RealiMasina.kernel.freeResource(new PertraukimoElementas("Pertraukimas", false, PertraukimoElementas.PertraukimoTipas.OF), pId);
    }

    public static void GD(int id) {
        RealiMasina.cpu.MODE = 'S';
        RealiMasina.kernel.freeResource(new PertraukimoElementas("Pertraukimas", false, PertraukimoElementas.PertraukimoTipas.GD, RealiMasina.cpu.ioAddr), id);
    }

    public static void PD(int id) {
        RealiMasina.cpu.MODE = 'S';
        RealiMasina.kernel.freeResource(new PertraukimoElementas("Pertraukimas", false, PertraukimoElementas.PertraukimoTipas.PD, RealiMasina.cpu.ioAddr), id);
    }

    public static void HALT(int id) {
        RealiMasina.cpu.MODE = 'S';
        RealiMasina.kernel.freeResource(new PertraukimoElementas("Pertraukimas", false, PertraukimoElementas.PertraukimoTipas.HALT), id);
    }

    public static void input() {
        RealiMasina.cpu.MODE = 'S';
        RealiMasina.kernel.freeResource(new PertraukimoElementas("IvedimoPertraukimas", false, PertraukimoElementas.PertraukimoTipas.INPUT), -1);
    }
}
