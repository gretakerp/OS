
package procesai;

import modelineos.ProcesuPlanuotojas;
import java.util.Iterator;
import java.util.LinkedList;
import kompiuteris.Procesorius;
import resursai.Elementas;
import kompiuteris.RealiMasina;
import resursai.Resursas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class Procesas {
    
    private AbstractProcess process;
        public AbstractProcess getProcess() {
            return process;
        }
    
    private Busena state;
        public Busena getState() {
            return state;
        }
        public void setState(Busena state) {
            this.state = state;
        }
        
    private int id;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        
    private int priority;      
        public int getPriority() {
            return priority;
        }
        public void setPriority(int priority) {
            this.priority = priority;
        }
        public void decPriority() {
            this.priority--;
        }
    private int defaultPriority;
        public int getDefaultPriority() {
            return defaultPriority;
        }
        public void restorePriority() {
            this.priority = this.defaultPriority;
        }
        
    private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        
    // išsaugotos registrų reikšmės
    Procesorius registrai = new Procesorius();
        public Procesorius getRegistrai() {
            return registrai;
        }
    
    private int pId;
        public int getPId() {
            return pId;
        }
        public void setPId(int pId) {
            this.pId = pId;
        }
        
    private LinkedList<Integer> childProcesses = new LinkedList<Integer>();
        public LinkedList getChildProcesses() {
            return childProcesses;
        }
        public void addChildProcess (int proc) {
            childProcesses.add(new Integer(proc));
        }
        public void removeChildPorcess(int id) {
            Iterator i = childProcesses.iterator();
            while (i.hasNext()) {
                Integer cId = (Integer) i.next();
                if (cId.intValue() == id) {
                    childProcesses.remove(cId);
                }
            }
        }
    
    private LinkedList<Elementas> ownedResourceElements = new LinkedList<Elementas>();
        public LinkedList<Elementas> getOwnedResourceElements() {
            return ownedResourceElements;
        }
        public void addOwnedResourceElement (Elementas res) {
            ownedResourceElements.add(res);
        }
        public void removeResourceElement(Elementas res) {
            ownedResourceElements.remove(res);
        }
        public Elementas getResourceElement(String name){
            Iterator i = ownedResourceElements.iterator();
            while (i.hasNext()) {
                Elementas ele = (Elementas) i.next();
                if (ele.getName().equals(name)) return ele;
            }
            return null;
        }
        public Elementas takeResourceElement(String name) {
            Iterator i = ownedResourceElements.iterator();
            while (i.hasNext()) {
                Elementas ele = (Elementas) i.next();
                if (ele.getName().equals(name)) {
                    ownedResourceElements.remove(ele);
                    return ele;
                }
            }
            return null;
        }
        
        
    private LinkedList<Integer> createdResources = new LinkedList<Integer>(); 
        public LinkedList<Integer> getCreatedResources() {
            return createdResources;
        }
        public void addCreatedResource (int id) {
            createdResources.add(new Integer(id));
        }
        public void removeCreatedResource(int id) {
            Iterator i = createdResources.iterator();
            while (i.hasNext()) {
                Integer rId = (Integer) i.next();
                if (rId.intValue() == id) {
                    createdResources.remove(rId);
                }
            }
        }
        
    /** Creates a new instance of Procesas */
    
    public Procesas(int id, String name, int priority, int pId, AbstractProcess process) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.defaultPriority = priority;
        this.pId = pId;
        this.process = process;
    }    
    
    public void block() {
        switch (state) {
            case PASIRUOSES: {
                state = Busena.BLOKUOTAS;
                break;
            }
            case PASIRUOSES_SUSTABDYTAS: {
                state = Busena.BLOKUOTAS_SUSTABDYTAS;
                break;
            }
            //@todo case active , procesu planuotojas etc
            case AKTYVUS: {
                ProcesuPlanuotojas.planuok(Busena.BLOKUOTAS);
                break;
            }
        }
    }
    public void unblock() {
        switch (state) {
            case BLOKUOTAS: {
                state = Busena.PASIRUOSES;
                RealiMasina.kernel.removeProcess(this);
                RealiMasina.kernel.addReadyProcess(this);
                ProcesuPlanuotojas.planuok(Busena.PASIRUOSES);
                break;
            }
            case BLOKUOTAS_SUSTABDYTAS: {
                state = Busena.PASIRUOSES_SUSTABDYTAS;
                break;
            }
        }
    }
    public void getReady() {
        switch (state) {
            case PASIRUOSES_SUSTABDYTAS: {
                state = Busena.PASIRUOSES;
                RealiMasina.kernel.removeProcess(this);
                RealiMasina.kernel.addReadyProcess(this);
                ProcesuPlanuotojas.planuok(Busena.PASIRUOSES);
                break;
            }
            case BLOKUOTAS_SUSTABDYTAS: {
                state = Busena.BLOKUOTAS;
                break;
            }
        }
    }
    public void stop() {
        switch (state) {
            case PASIRUOSES: {
                state = Busena.PASIRUOSES_SUSTABDYTAS;
                RealiMasina.kernel.removeReadyProcess(this);
                RealiMasina.kernel.addProcess(this);
                break;
            }
            case BLOKUOTAS: {
                state = Busena.BLOKUOTAS_SUSTABDYTAS;
                break;
            }
            case AKTYVUS: {
                ProcesuPlanuotojas.planuok(Busena.PASIRUOSES_SUSTABDYTAS);
                break;
            }
        }
    }
    public void destroy() {
        Iterator p = childProcesses.iterator();
        while (p.hasNext()) {
            Integer n = (Integer) p.next();
            Procesas proc = RealiMasina.kernel.getProcess(n.intValue());
            proc.destroy();
        }
        Iterator c = RealiMasina.kernel.getProcess(pId).getChildProcesses().iterator();
        while (c.hasNext()) {
            Integer n = (Integer) c.next();
            if (n.intValue() == id) {
                RealiMasina.kernel.getProcess(pId).getChildProcesses().remove(n);
                break;
            }
        }
        if (RealiMasina.kernel.getActiveProc() == this) {
            RealiMasina.kernel.setActiveProc(null);
            ProcesuPlanuotojas.planuok(null);
        }
        else {
            RealiMasina.kernel.removeProcess(this);
            RealiMasina.kernel.removeReadyProcess(this);
        }
        Iterator i = RealiMasina.kernel.getResources().iterator();
        while (i.hasNext()) {
            Resursas r = (Resursas) i.next();
            r.removeWaitingProcess(id);
        }
        i = ownedResourceElements.iterator();
        while (i.hasNext()) {
            Elementas r = (Elementas) i.next();
            if (r.isReusable()) RealiMasina.kernel.freeResource(r, -1);
        }
    }
    
    public boolean isSystemProcess() {
        return (priority > 80);
    }
}
