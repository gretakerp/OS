package procesai;

import kompiuteris.RealiMasina;
import kompiuteris.ZurnalizavimoIrenginys;
import modelineos.FileConstants;
import kompiuteris.KanaluIrenginys.Type;
import resursai.IsvedimoElementas;
import resursai.UzduotiesFailoElementas;
import resursai.UzduotiesElementas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */
public class Analyzer extends AbstractProcess {

    /** Creates a new instance of Analyzer */
    public Analyzer() {
    }

    public void doit() {
        ZurnalizavimoIrenginys.println("Vykdomas procesas Analyzer:" + getParent().getPriority());
        switch (state) {
            case 0: {
                RealiMasina.kernel.requestResource(itself.getId(), "Uzduotis", 1);
                state++;
                break;
            }
            case 1: {
                UzduotiesElementas ele = (UzduotiesElementas) itself.getResourceElement("Uzduotis");
                if (ele.getVeiksmas() == UzduotiesElementas.UzduotiesTipas.NAIKINTI) {
                    int id = ele.getId();
                    RealiMasina.kernel.getProcess(id).destroy();
                    ZurnalizavimoIrenginys.println("JobGovernor with ID " + ele.getId() + " was destroyed");
                    // Naikinam resursą "Uzduotis"
                    state = 7;
                } else if (ele.getVeiksmas() == UzduotiesElementas.UzduotiesTipas.KURTI) {
                    RealiMasina.kernel.requestResource(itself.getId(), "KanaluIrenginys", 1);
                    state++;
                } else if (ele.getVeiksmas() == UzduotiesElementas.UzduotiesTipas.UZKRAUTI) {
                    state = 4;
                }
                break;
            }
            case 2: {
                RealiMasina.channel.ST = Type.HDD;
                RealiMasina.channel.DT = Type.SUPERMEMORY;
                //RealiMasina.channel.xchange();
                state++;
                break;
            }
            case 3: {
                RealiMasina.kernel.freeResource(itself.takeResourceElement("KanaluIrenginys"), -1);
                state++;
                break;
            }
            case 4: {
                UzduotiesElementas ele = (UzduotiesElementas) itself.getResourceElement("Uzduotis");
                String file = null;
                if (ele.getVeiksmas() == UzduotiesElementas.UzduotiesTipas.UZKRAUTI) {
                    file = ele.getSource();
                } else if (RealiMasina.kernel.fileExists(ele.getInput())) {
                    file = RealiMasina.kernel.readFile(ele.getInput()).trim();
                } else {
                    // Tokio failo nera, arba klaidinga struktura
                    state = 7;
                    break;
                }
                // struktūros tikrinimas
                if (file.startsWith(FileConstants.DATASEGMENT) &&
                        file.contains(FileConstants.CODESEGMENT) && file.endsWith(FileConstants.FILEEND)) {
                    state++;
                } else {
                    RealiMasina.kernel.freeResource(new IsvedimoElementas("Isvedimas",
                            false, "Failo struktūra neteisinga: " + ele.getInput() + "\n"), -1);
                    state = 7;
                }
                break;
            }
            case 5: {
                UzduotiesElementas ele = (UzduotiesElementas) itself.getResourceElement("Uzduotis");
                String file = "";
                RealiMasina.kernel.readFile(ele.getInput());
                if (ele.getVeiksmas() == UzduotiesElementas.UzduotiesTipas.UZKRAUTI) {
                    file = ele.getSource();
                } else {
                    file = RealiMasina.kernel.readFile(ele.getInput());
                }
                UzduotiesFailoElementas tmp = new UzduotiesFailoElementas("UzduotiesFailas", false, ele.getInput(), file);
                RealiMasina.kernel.freeResource(tmp, -1);
                state++;
                break;
            }
            case 6: {
                RealiMasina.kernel.createProcess("JobGovernor", 85, itself.getId(), new JobGovernor());
                state++;
                break;
            }
            case 7:
                itself.takeResourceElement("Uzduotis");
                state = 0;
                break;
        }
    }
}
