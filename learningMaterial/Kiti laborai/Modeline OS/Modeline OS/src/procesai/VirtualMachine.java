
package procesai;

import kompiuteris.RealiMasina;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class VirtualMachine extends AbstractProcess {
    
    /** Creates a new instance of VM */
   // public VirtualMachine(int PTR) {
       /// parent.registrai.PTR = (PTR + "").toCharArray();
                //Integer.toString(PTR).toCharArray();
 //   }
    public VirtualMachine()
    {
        
    }
    
    public void doit() {
        RealiMasina.cpu.MODE = 'U';
        RealiMasina.debug.println("Procerius perejo i vartotojo rezima.");
    }
    
}
