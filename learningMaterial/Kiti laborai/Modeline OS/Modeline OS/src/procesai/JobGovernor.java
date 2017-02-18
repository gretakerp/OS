
package procesai;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelineos.InvalidAddressException;
import kompiuteris.RealiMasina;
import resursai.EilutesElementas;
import resursai.Elementas;
import resursai.IsvedimoElementas;
import resursai.PertraukimoElementas;
import resursai.Resursas;
import resursai.UzduotiesFailoElementas;
import resursai.UzduotiesElementas;
import resursai.VartotojoAtmintiesElementas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class JobGovernor extends AbstractProcess {

    int PTRaddress = -1;
    VirtualMachine vm = null;
    PertraukimoElementas ele = null;

    /** Creates a new instance of JobGovernor */
    public JobGovernor() {
    }

    public void doit() {
        switch (state) {
            case 0: {
                RealiMasina.kernel.requestResource(itself.getId(), "UzduotiesFailas", 1);
                state++;
                break;
            }
            case 1: {
                RealiMasina.kernel.requestResource(itself.getId(), "Atmintis", 11);
                state++;
                break;
            }
            case 2: {

                String[] task = returnFormatedTask((UzduotiesFailoElementas) itself.getResourceElement("UzduotiesFailas"));
                definePTRFillMemory(task);
                //@todo reikia į atmintį įrašyti tempfile duomenis!
                state++;
                break;
            }
            case 3:
                // šalinam resursą "Tempfile
                itself.takeResourceElement("UzduotiesFailas");
                state++;
                break;
            case 4:
                vm = new VirtualMachine();
                RealiMasina.kernel.createProcess("VirtualMachine", 50, itself.getId(), vm);
                vm.itself.registrai.PTR = (PTRaddress + "").toCharArray();
                state++;
                break;
            case 5:
                RealiMasina.kernel.requestResource(itself.getId(), "Pertraukimas", 1);
                state++;
                break;
            case 6:
                vm.itself.stop();
                state++;
                break;
            case 7:
                ele = (PertraukimoElementas) itself.getResourceElement("Pertraukimas");
                if (ele.getTrikis() == PertraukimoElementas.PertraukimoTipas.HALT ||
                        ele.getTrikis() == PertraukimoElementas.PertraukimoTipas.IOC ||
                        ele.getTrikis() == PertraukimoElementas.PertraukimoTipas.IA ||
                        ele.getTrikis() == PertraukimoElementas.PertraukimoTipas.OF) {
                    RealiMasina.kernel.getProcess(vm.itself.getId()).destroy();
                    RealiMasina.kernel.freeResource(new IsvedimoElementas("Isvedimas", false,
                            "VM buvo sunaikintas dėl petraukimo: " + ele.getTrikis().name() + "\n"), -1);
                    vm = null;
                    state = 12;
                    break;
                } else if (ele.getTrikis() == PertraukimoElementas.PertraukimoTipas.PD) {
                    Procesas pr = RealiMasina.kernel.getProcess(Integer.parseInt(itself.getChildProcesses().getFirst().toString().trim()));
                    char[] word = null;
                    try {
                        char[] adresas = getCharAdress(ele.getAddress());
                        word = RealiMasina.pager.getWord(adresas, pr.registrai.PTR);
                        if ((new String(word)).equals("nnnn")) word = "\n".toCharArray();
                    } catch (InvalidAddressException ex) {
                        Logger.getLogger(JobGovernor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    RealiMasina.kernel.freeResource(new IsvedimoElementas("Isvedimas", false, new String(word)), -1);
                    itself.takeResourceElement("Pertraukimas");
                    vm.itself.getReady();
                    state = 5;
                    break;
                } else if (ele.getTrikis() == PertraukimoElementas.PertraukimoTipas.GD) {
                    RealiMasina.kernel.freeResource(new EilutesElementas("IvedimoLaukimas", false), -1);
                    state++;
                    break;
                }
            case 8:
                RealiMasina.kernel.requestResource(itself.getId(), "Ivedimas", 1);
                state++;
                break;
            case 9:
                Resursas res = RealiMasina.kernel.getResource("IvedimoLaukimas");
                EilutesElementas elem = (EilutesElementas) res.getAvailableElements().getFirst();
                res.removeElement(ele);
                state++;
                break;
            case 10:
                EilutesElementas ivedimas = (EilutesElementas) itself.getResourceElement("Ivedimas");
                char[] word = {'0', '0', '0', '0'};
                String ivestis = ivedimas.getEilute();

                if (ivestis.length() >= 4) {
                    ivestis = ivestis.substring(0, 4);
                    for (int i = 0; i < 4; i++) {
                        word[i] = ivestis.toCharArray()[i];
                    }
                } else {
                    int tmp = 4;
                    for (int i = ivestis.length() - 1; i >= 0; i--) {
                        tmp--;
                        word[tmp] = ivestis.toCharArray()[i];
                    }
                }
                char[] adresas = getCharAdress(ele.getAddress());
                try {
                    RealiMasina.pager.setWord(adresas, vm.itself.registrai.PTR, word);
                } catch (InvalidAddressException ex) {
                    Logger.getLogger(JobGovernor.class.getName()).log(Level.SEVERE, null, ex);
                }
                state++;
                break;
            case 11:
                itself.takeResourceElement("Ivedimas");
                itself.getOwnedResourceElements().remove(ele);
                ele = null;
                vm.itself.getReady();
                state = 5;
                break;
            case 12:
                // Pertraukimo pašalinimas
                itself.getOwnedResourceElements().remove(ele);
                ele = null;
                state++;
                break;

            case 13:
                // Atminties atlaisvinimas
                // Pastaba: daroma prielaida, kad visi kiti resursai jau atlaisvinti anksčiau
                for (Object obj : (LinkedList) itself.getOwnedResourceElements().clone()) {
                    Elementas elementas = (Elementas) obj;
                    RealiMasina.kernel.freeResource(elementas, -1);
                    itself.takeResourceElement(elementas.getName());
                }
                state++;
                break;
            case 14:
                // Sukuriamas resursas užduotis su tipu naikinti užduotį, šio 
                // JobGovernor ID bei nurodoma, kad tai skirta Analyzer
                RealiMasina.kernel.freeResource(new UzduotiesElementas("Uzduotis", false, UzduotiesElementas.UzduotiesTipas.NAIKINTI, itself.getId()), -1);
                state++;
                break;
            case 15:
                itself.block();
                break;
        }
    }

    // iš tempfile padaro String masyvą, kur kiekvienas elementas
    // yra vieno takelio dydžio. Trūkstamų simbolių vietą užpildo nuliais
    public String[] returnFormatedTask(UzduotiesFailoElementas uzduotiesFailas) {
        String[] task = new String[10];
        String emptyTrack = "0000000000000000000000000000000000000000";
        // stack'ą nunulinam
        task[8] = emptyTrack;
        task[9] = emptyTrack;
        String file = uzduotiesFailas.getOutput();
        int ds = file.indexOf("$$DS") + 4;
        int cs = file.indexOf("$$CS") + 4;
        int end = file.indexOf("$$$$");
        String DS = file.substring(ds, cs - 4);
        String CS = file.substring(cs, end);

        if (DS.length() <= 40) {
            task[0] = DS;
            for (int i = DS.length(); i < 40; i++) {
                task[0] += "0";
            }
            task[1] = emptyTrack;
        } else {
            task[0] = DS.substring(0, 40);
            task[1] = DS.substring(40);
            for (int i = 40 + task[1].length(); i < 80; i++) {
                task[1] += "0";
            }
        }

        for (int i = 2; i < 8; i++) {
            if (CS.length() < (i - 1) * 40) {
                task[i] = CS.substring((i - 2) * 40);
                for (int a = task[i].length(); a < 40; a++) {
                    task[i] += "0";
                }
                if (i < 7) {
                    for (int b = i + 1; b < 8; b++) {
                        task[b] = emptyTrack;
                    }
                }
                break;
            }
            task[i] = CS.substring((i - 2) * 40, ((i - 1) * 40));
        }
        return task;
    }

    public void definePTRFillMemory(String[] task) {
        int[] mas = new int[10];
        int nr = 0;
        String PTR = "";
        Iterator i = ((LinkedList) itself.getOwnedResourceElements().clone()).iterator();
        while (i.hasNext()) {
            Elementas elem = (Elementas) i.next();
            if (elem.getName().contains("Atmintis") == false) {
                continue;
            }
            if (elem.getName().equals("Atmintis")) {
                VartotojoAtmintiesElementas at = (VartotojoAtmintiesElementas) elem;
                if (nr == 10) {
                    PTRaddress = at.getTakelioNr();
                } else {
                    mas[nr] = at.getTakelioNr();
                    RealiMasina.memory.writeTrack(task[nr], at.getTakelioNr());
                    nr++;
                }
            }
        }

        for (int a = 0; a < 10; a++) {
            String tmp = Integer.toString(mas[a]);
            for (int b = 0; b < 4 - tmp.length(); b++) {
                PTR += " ";
            }
            PTR += tmp;
        }
        RealiMasina.memory.writeTrack(PTR, PTRaddress);
    }

    private char[] getCharAdress(int address) {
        if (address < 10) {
            return ("0" + new String(ele.getAddress() + "")).toCharArray();
        } else {
            return new String(ele.getAddress() + "").toCharArray();
        }
    }
}

