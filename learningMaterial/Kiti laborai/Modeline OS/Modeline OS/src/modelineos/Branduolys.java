
package modelineos;

import kompiuteris.RealiMasina;
import kompiuteris.ZurnalizavimoIrenginys;
import kompiuteris.IsorineAtmintis;
import java.util.Iterator;
import java.util.LinkedList;
import procesai.Procesas;
import procesai.AbstractProcess;
import resursai.Resursas;
import kompiuteris.KanaluIrenginys;
import resursai.Elementas;
import procesai.Busena;
import procesai.VirtualMachine;
import resursai.LaukiantisProcesas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class Branduolys {

    int procId = 0;
    int resId = 0;

    /** Creates a new instance of Kernel */
    public Branduolys() {
		osStep = !RealiMasina.langas.jCheckBoxOSStep.isSelected();
		vmStep = !RealiMasina.langas.jCheckBoxVMStep.isSelected();
    }
    // visų kitų procesų sąrašas
    private LinkedList<Procesas> processes = new LinkedList<Procesas>();

    public LinkedList getProcesses() {
        return processes;
    }

    public void addProcess(Procesas proc) {
        processes.add(proc);
    }

    public void removeProcess(Procesas proc) {
        processes.remove(proc);
        waitForStepAndRefresh();
    }

    public void removeProcess(int id) {
        Iterator i = processes.iterator();

        while (i.hasNext()) {
            Procesas proc = (Procesas) i.next();
            if (proc.getId() == id) {
                processes.remove(proc);
            }
        }
    }

    public void createProcess(String name, int priority, int pId, AbstractProcess process) {
        Procesas proc = new Procesas(procId, name, priority, pId, process);
        Procesas parent = getProcess(pId);
        if (parent != null) {
            parent.addChildProcess(procId);
        }
        proc.setState(Busena.PASIRUOSES);
        addReadyProcess(proc);
        process.setParent(proc);
        procId++;
        ZurnalizavimoIrenginys.println("Procesas (" + proc.getName() + ", " + proc.getId() + ") sukurtas.");
        ProcesuPlanuotojas.planuok(Busena.PASIRUOSES);
        waitForStepAndRefresh();
    }
    // visų resursų sąrašas
    private LinkedList<Resursas> resources = new LinkedList<Resursas>();

    public LinkedList getResources() {
        return resources;
    }

    public void addResource(Resursas res) {
        resources.add(res);
    }

    public void removeResource(Resursas res) {
        resources.remove(res);
        waitForStepAndRefresh();
    }

    public void removeResource(int id) {
        Iterator i = resources.iterator();

        while (i.hasNext()) {
            Resursas res = (Resursas) i.next();
            if (res.getId() == id) {
                resources.remove(res);
            }
        }
    }

    public Resursas getResource(String name) {
        Resursas res;
        Iterator i = resources.iterator();
        while (i.hasNext()) {
            res = (Resursas) i.next();
            if (res.getName().equals(name)) {
                return res;
            }
        }
        return null;
    }

    public void freeResource(Elementas ele, int target) {
        if (ele == null) {
            return;
        }
        ele.setTarget(target);
        Resursas res = getResource(ele.getName());
        res.addResourceElement(ele);
        ZurnalizavimoIrenginys.println("Elementas (" + ele.getName() + ") atlaisvintas/sukurtas.");
        ResursuPaskirstytojas.skirstyk();
        waitForStepAndRefresh();
    }
	
    public void createResource(Elementas ele, int target) {
        if (ele == null) {
            return;
        }
        ele.setTarget(target);
        Resursas res = getResource(ele.getName());
        res.addResourceElement(ele);
    }

    public void requestResource(int pId, String name, int amount) {
        LaukiantisProcesas proc = new LaukiantisProcesas(pId, amount);
        RealiMasina.kernel.getResource(name).addWaitingProcess(proc);
        ZurnalizavimoIrenginys.println("Procesas(" + RealiMasina.kernel.getProcess(pId).getName() + ", " + RealiMasina.kernel.getProcess(pId).getId() + ") paprase resurso (" + name + ").");
        RealiMasina.kernel.getProcess(pId).block();
        ResursuPaskirstytojas.skirstyk();
        waitForStepAndRefresh();
    }

    public void createResource(int pId, String name, boolean reusable) {
        Resursas res = new Resursas(resId, pId, name, reusable);
        addResource(res);
        getProcess(pId).addCreatedResource(resId);
        ZurnalizavimoIrenginys.println("Resursas (" + res.getName() + ") sukurtas.");
        resId++;
        waitForStepAndRefresh();
    }
    // pasiruošusių (kurie turi visus resursus tik ne procą) sąrašas
    private LinkedList<Procesas> readyProcesses = new LinkedList<Procesas>();

    public LinkedList getReadyProcesses() {
        return readyProcesses;
    }

    public void addReadyProcess(Procesas proc) {
        readyProcesses.add(proc);
    }

    public void removeReadyProcess(Procesas proc) {
        readyProcesses.remove(proc);
    }

    public void removeReadyProcess(int id) {
        Iterator i = readyProcesses.iterator();

        while (i.hasNext()) {
            Procesas proc = (Procesas) i.next();
            if (proc.getId() == id) {
                readyProcesses.remove(proc);
            }
        }
    }
    // einamasis procesas
    private Procesas activeProc = null;

    public Procesas getActiveProc() {
        return activeProc;
    }

    public void setActiveProc(Procesas proc) {
        this.activeProc = proc;
    }

    public Procesas getProcess(int id) {
        Procesas proc;
        Iterator i = processes.iterator();
        while (i.hasNext()) {
            proc = (Procesas) i.next();
            if (proc.getId() == id) {
                return proc;
            }
        }
        i = readyProcesses.iterator();
        while (i.hasNext()) {
            proc = (Procesas) i.next();
            if (proc.getId() == id) {
                return proc;
            }
        }
        if ((activeProc != null) && (activeProc.getId() == id)) {
            return activeProc;
        } else {
            return null;
        }
    }
    
    public void switchProcess(Procesas proc) {
        RealiMasina.cpu.C = proc.getRegistrai().C;
        RealiMasina.cpu.IC = proc.getRegistrai().IC;
        RealiMasina.cpu.PTR = proc.getRegistrai().PTR;
        RealiMasina.cpu.R = proc.getRegistrai().R;
        RealiMasina.cpu.SP = proc.getRegistrai().SP;
        RealiMasina.cpu.TMR = proc.getRegistrai().TMR;
        setActiveProc(proc);
        proc.setState(Busena.AKTYVUS);
        waitForStepAndRefresh();
    }
    
    public boolean vmStep, osStep;
    
    public void waitForStepAndRefresh() {
        waitForStepAndRefresh(false);
    }
    
    public void waitForStepAndRefresh(boolean checkVm) {
        vmStep = !RealiMasina.langas.jCheckBoxVMStep.isSelected();
        osStep = !RealiMasina.langas.jCheckBoxOSStep.isSelected();
        
        if (RealiMasina.kernel.getActiveProc() != null)
        {
            if (RealiMasina.kernel.getActiveProc().getProcess() instanceof VirtualMachine)
                RealiMasina.vmLangas.refresh(true);
            else
                RealiMasina.vmLangas.refresh(false);
        }
		RealiMasina.observeManager.refresh();
        RealiMasina.langas.refresh();
        
        while (!osStep || (!vmStep && checkVm)) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException exception) {
            }
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Failų sistemos funkcijos">
    public boolean writeFileName(String filename, int startAddress, int endAddress) {
        if (filename.length() > 8) {
            filename = filename.substring(0, 8);
        } else if (filename.length() < 8) {
            for (int i = filename.length(); i < 8; i++) {
                filename += " ";
            }
        }
        String write = filename;
        for (int i = 0; i < 3 - Integer.toString(startAddress).length(); i++) {
            write += " ";
        }
        write += startAddress;
        for (int i = 0; i < 3 - Integer.toString(endAddress).length(); i++) {
            write += " ";
        }
        write += endAddress;
        String info = new String();
        int i = IsorineAtmintis.FSSIZE + 1;
        while (!((info = readTrack(--i).toString()).equals(IsorineAtmintis.FILETABLEEND))) {
            if (RealiMasina.hdd.isEmptyTrack(i)) {
                writeTrack(write, i);
                return true;
            }
        }
        writeTrack(write, i--);
        writeTrack(IsorineAtmintis.FILETABLEEND, i);
        return true;
    }

    public boolean fileExists(String filename) {
        String info = null;
		if (filename == null || filename.trim().isEmpty())
			return false;
        if (filename.length() > 8) {
            filename = filename.substring(0, 8);
        } else if (filename.length() < 8) {
            for (int i = filename.length(); i < 8; i++) {
                filename += " ";
            }
        }
        int i = IsorineAtmintis.FSSIZE + 1;
        while (i > 0 &&!((info = readTrack(--i).toString()).equals(IsorineAtmintis.FILETABLEEND))) {
            if (info.contains(filename)) {
                return true;
            }
        }
        return false;
    }

    public boolean writeFile(String info, String filename) {
        if (fileExists(filename)) {
            return false;
        }
        int amount = info.length() / 40;
        if ((info.length() % 40) != 0) {
            amount++;
        }
        int address = RealiMasina.hdd.isAvailable(amount);
        int startAddress = address;
        if (address != -1) {
            String str = null;
            for (int i = 0; i < amount - 1; i++) {
                str = info.substring(i * 40, (i + 1) * 40);
                writeTrack(str, address++);
            }
            str = info.substring((amount - 1) * 40);
            char masyvas[] = new char[40 - str.length()];
            for (int i = 0; i < 40 - str.length(); i++) {
                masyvas[i] = ' ';
            }
            str += new String(masyvas);
            writeTrack(str, address);
            writeFileName(filename, startAddress, address);
            ZurnalizavimoIrenginys.println("Sukurtas naujas failas(\"" + filename + "\").");
            return true;
        } else {
            return false;
        }
    }

    public String listFiles() {
        String info = new String();
        String result = new String();
        int i = IsorineAtmintis.FSSIZE + 1;
        while (!((info = readTrack(--i).toString()).equals(IsorineAtmintis.FILETABLEEND))) {
            result += info.substring(0, 8) + "\n";
        }
        return result;
    }

    public boolean deleteFile(String filename) {
        if (filename.length() > 8) {
            filename = filename.substring(0, 8);
        } else if (filename.length() < 8) {
            for (int i = filename.length(); i < 8; i++) {
                filename += " ";
            }
        }
        boolean ret = false;
        String info = new String();
        int i = IsorineAtmintis.FSSIZE + 1;
        while (!((info = readTrack(--i).toString()).equals(IsorineAtmintis.FILETABLEEND))) {
            if (info.contains(filename)) {
                ret = true;
                String tmp = info.substring(8, 11);
                int startAddress = Integer.parseInt(tmp.substring(tmp.lastIndexOf(" ") + 1));
                tmp = info.substring(11, 14);
                int endAddress = Integer.parseInt(tmp.substring(tmp.lastIndexOf(" ") + 1));
                emptyTrack(i);
                for (i = startAddress; i <= endAddress; i++) {
                    emptyTrack(i);
                }
                ZurnalizavimoIrenginys.println("Failas(\"" + filename + "\") ištrintas.");
                return true;
            }
        }
        return ret;
    }

    public String readFile(String filename) {
        if (filename.length() > 8) {
            filename = filename.substring(0, 8);
        } else if (filename.length() < 8) {
            for (int i = filename.length(); i < 8; i++) {
                filename += " ";
            }
        }
        String info = null;
        String ret = "";
        int i = IsorineAtmintis.FSSIZE + 1;
        int startAddress = 0;
        int endAddress = 0;
        while (i > 0 && !((info = readTrack(--i).toString()).equals(IsorineAtmintis.FILETABLEEND))) {
            if (info.contains(filename)) {
                String tmp = info.substring(8, 11);
                startAddress = Integer.parseInt(tmp.substring(tmp.lastIndexOf(" ") + 1));
                tmp = info.substring(11, 14);
                endAddress = Integer.parseInt(tmp.substring(tmp.lastIndexOf(" ") + 1));
                for (int j = startAddress; j <= endAddress; j++) {
                    ret += readTrack(j);
                }
                break;
            }
        }
        return ret;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Atminties funkcijos">
    public String readMemoryTrack(int takelioNr) {
        RealiMasina.channel.ST = KanaluIrenginys.Type.MEMORY;
        RealiMasina.channel.DT = KanaluIrenginys.Type.SUPERMEMORY;
        RealiMasina.channel.SB = Integer.toString(takelioNr).toCharArray();
        RealiMasina.channel.xchange();
        return RealiMasina.smemory.takelis;
    }

    public void writeMemoryTrack(String info, int takelioNr) {
        RealiMasina.smemory.takelis = info;
        RealiMasina.channel.DT = KanaluIrenginys.Type.MEMORY;
        RealiMasina.channel.ST = KanaluIrenginys.Type.SUPERMEMORY;
        RealiMasina.channel.DB = Integer.toString(takelioNr).toCharArray();
        RealiMasina.channel.xchange();
    }
    public String readTrack(int takelioNr) {
        RealiMasina.channel.ST = KanaluIrenginys.Type.HDD;
        RealiMasina.channel.DT = KanaluIrenginys.Type.SUPERMEMORY;
        RealiMasina.channel.SB = Integer.toString(takelioNr).toCharArray();
        RealiMasina.channel.xchange();
        return RealiMasina.smemory.takelis;
    }

    public void writeTrack(String info, int takelioNr) {
        if (info.length() > 40) {
            info = info.substring(0, 40);
        }
        RealiMasina.smemory.takelis = info;
        RealiMasina.channel.ST = KanaluIrenginys.Type.SUPERMEMORY;
        RealiMasina.channel.DT = KanaluIrenginys.Type.HDD;
        RealiMasina.channel.DB = Integer.toString(takelioNr).toCharArray();
        RealiMasina.channel.xchange();
    }

    private void emptyTrack(int takelioNr) {
        writeTrack(IsorineAtmintis.EMPTY, takelioNr);
    }
    // </editor-fold>
    
    
}
