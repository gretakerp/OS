
package procesai;

import java.util.Random;
import kompiuteris.VartotojoAtmintis;
import kompiuteris.ZurnalizavimoIrenginys;
import kompiuteris.RealiMasina;
import resursai.Kanalas;
import resursai.VartotojoAtmintiesElementas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class Root extends AbstractProcess {

    /** Creates a new instance of Root */
    public Root() {
    }

    public void doit() {
        ZurnalizavimoIrenginys.println("ROOT:" + getParent().getPriority());
        switch (state) {
            case 0: {
                RealiMasina.kernel.createResource(itself.getId(), "Uzduotis", false);
                RealiMasina.kernel.createResource(itself.getId(), "Pertraukimas", false);
                RealiMasina.kernel.createResource(itself.getId(), "IvedimoPertraukimas", false);
                RealiMasina.kernel.createResource(itself.getId(), "MOSpabaiga", false);
                RealiMasina.kernel.createResource(itself.getId(), "Isvedimas", false);
                RealiMasina.kernel.createResource(itself.getId(), "Ivedimas", false);
                RealiMasina.kernel.createResource(itself.getId(), "KanaluIrenginys", true);
                RealiMasina.kernel.createResource(itself.getId(), "UzduotiesFailas", false);
                RealiMasina.kernel.createResource(itself.getId(), "Atmintis", true);
                RealiMasina.kernel.createResource(itself.getId(), "IvestaEilute", true);
                RealiMasina.kernel.createResource(itself.getId(), "FailineKomanda", true);
                RealiMasina.kernel.createResource(itself.getId(), "IvedimoLaukimas", true);
                state++;
                break;
            }
            // Atminties sukūrimas su atsitiktiniais takelių numeriais
            case 1: {
                boolean[] taken = new boolean[VartotojoAtmintis.MSIZE];
                for (int i = 0; i < VartotojoAtmintis.MSIZE; i++) {
                    taken[i] = false;
                }
                int i = 0;
                Random r = new Random();
                while (i < VartotojoAtmintis.MSIZE) {
                    int rand = r.nextInt(VartotojoAtmintis.MSIZE);
                    if (taken[rand] == false) {
                        RealiMasina.kernel.createResource(new VartotojoAtmintiesElementas("Atmintis", true, rand), -1);
                        taken[rand] = true;
                        i++;
                    }
                }
                RealiMasina.kernel.createResource(new Kanalas(), -1);
                state++;
                break;
            }
            case 2: {
                RealiMasina.kernel.createProcess("Shell", 98, itself.getId(), new Shell());
                state++;
                break;
            }
            case 3: {     
                RealiMasina.kernel.createProcess("Analyzer", 97, itself.getId(), new Analyzer());
                state++;
                break;
            }
            case 4: {
                RealiMasina.kernel.createProcess("UserCommands", 98, itself.getId(), new Shell());
                state++;
                break;
            }
            case 5: {
                RealiMasina.kernel.createProcess("PrintF", 96, itself.getId(), new PrintF());
                state++;
                break;
            }
            case 6: {
                RealiMasina.kernel.createProcess("Input", 95, itself.getId(), new Input());
                state++;
                break;
            }
            case 7: {
                RealiMasina.kernel.createProcess("Files", 94, itself.getId(), new Files());
                state++;
                break;
            }
            case 8: 
                RealiMasina.kernel.requestResource(itself.getId(), "MOSpabaiga", 1);
                break;
        }

    }
}
