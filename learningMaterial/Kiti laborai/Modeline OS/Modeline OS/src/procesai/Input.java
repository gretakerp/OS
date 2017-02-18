
package procesai;


import kompiuteris.RealiMasina;
import resursai.EilutesElementas;
import kompiuteris.KanaluIrenginys;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class Input extends AbstractProcess {
    
    EilutesElementas eilEl = null;
    
    @Override
    public void doit() {
        
        switch (state) {
            case 0:
                RealiMasina.kernel.requestResource(itself.getId(), "IvedimoPertraukimas", 1);
                state++;
                break;
            case 1:
                RealiMasina.kernel.requestResource(itself.getId(), "KanaluIrenginys", 1);
                state++;
                break;
            case 2:
                RealiMasina.channel.ST = KanaluIrenginys.Type.IOSTREAM;
                RealiMasina.channel.DT = KanaluIrenginys.Type.SUPERMEMORY;
                RealiMasina.channel.xchange();
                eilEl = new EilutesElementas("IvestaEilute", false, RealiMasina.smemory.takelis);
                state++;
                break;
            case 3:
                RealiMasina.kernel.freeResource(itself.takeResourceElement("KanaluIrenginys"), -1);
                itself.takeResourceElement("IvedimoPertraukimas");
                state++;
                break;
            case 4:
                RealiMasina.kernel.freeResource(eilEl, -1);
                eilEl = null;
                state = 0;
                break;
                
        }
    }
}
