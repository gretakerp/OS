
package procesai;

import kompiuteris.RealiMasina;
import resursai.EilutesElementas;
import resursai.FailuKomandosElementas;
import resursai.Resursas;
import resursai.UzduotiesElementas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class Shell extends AbstractProcess {

    String command = null;

    @Override
    public void doit() {
        switch (state) {
            case 0:
                RealiMasina.kernel.requestResource(itself.getId(), "IvestaEilute", 1);
                state++;
                break;
            case 1:
                Resursas res = RealiMasina.kernel.getResource("IvedimoLaukimas");
                EilutesElementas ele = (EilutesElementas) itself.getResourceElement("IvestaEilute");
                if (res.getAmount() > 0) {
                    RealiMasina.kernel.freeResource(new EilutesElementas("Ivedimas", false, ele.getEilute()), -1);
                    state = 5;
                    break;
                }
                else
                {
                    command = ele.getEilute(); 
                    state++;    
                    break;
                }
            case 2:
//                command = RealiMasina.smemory.takelis;
                if (RealiMasina.kernel.fileExists(command)) {
                    state = 4;
                    break;
                }
                else
                {
                    // Kursim failo komandą
                    state = 3;
                    break;
                }
            case 3:
                RealiMasina.kernel.freeResource(new FailuKomandosElementas("FailineKomanda", false, command), -1);
                state = 5;
                break;
            case 4:
                RealiMasina.kernel.freeResource(new UzduotiesElementas("Uzduotis", true, UzduotiesElementas.UzduotiesTipas.KURTI, command), -1);
                state++;
                break;
            case 5:
                itself.takeResourceElement("IvestaEilute");
                state = 0;
                break;
        }
    }
}
