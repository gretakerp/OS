
package procesai;


import kompiuteris.RealiMasina;
import resursai.IsvedimoElementas;
import kompiuteris.KanaluIrenginys;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class PrintF extends AbstractProcess {

    @Override
    public void doit() {
        switch (state) {
            case 0:
                RealiMasina.kernel.requestResource(itself.getId(), "Isvedimas", 1);
                state++;
                break;
            case 1:
                RealiMasina.kernel.requestResource(itself.getId(), "KanaluIrenginys", 1);
                state++;
                break;
            case 2:
                IsvedimoElementas ele = (IsvedimoElementas) itself.getResourceElement("Isvedimas");
                RealiMasina.smemory.takelis = ele.getValue();
                RealiMasina.channel.ST = KanaluIrenginys.Type.SUPERMEMORY;
                RealiMasina.channel.DT = KanaluIrenginys.Type.IOSTREAM;
                RealiMasina.channel.xchange();
                state++;
                break;
            case 3:
                RealiMasina.kernel.freeResource(itself.takeResourceElement("KanaluIrenginys"), -1);
                state++;
                break;
            case 4:  
                itself.takeResourceElement("Isvedimas");
                state = 0;
                break;
        }
    }
}
