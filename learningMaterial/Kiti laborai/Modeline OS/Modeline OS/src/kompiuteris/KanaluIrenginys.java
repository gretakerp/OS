
package kompiuteris;

import kompiuteris.RealiMasina;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class KanaluIrenginys {
    public enum Type {MEMORY, SUPERMEMORY, HDD, IOSTREAM};
    public char[] SB = new char[3];
    public char[] DB = new char[3];
    public Type ST;
    public Type DT;
    /** Creates a new instance of KanaluIrenginys */
    public KanaluIrenginys() {
    }
    
    public void xchange() {
        String takelis = null;
        switch (ST){
            case MEMORY: {
                takelis = RealiMasina.memory.readTrack(Integer.parseInt(new String(SB)));
                break;
            }
            case SUPERMEMORY: {
                takelis = RealiMasina.smemory.takelis;
                break;
            }
            case HDD: {
                takelis = RealiMasina.hdd.readTrack(Integer.parseInt(new String(SB)));
                break;
            }
            case IOSTREAM: {
                takelis = RealiMasina.langas.getInput();
                break;
            }
        }
        switch (DT) {
            case MEMORY: {
                RealiMasina.memory.writeTrack(takelis, Integer.parseInt(new String(DB)));
                break;
            }
            case SUPERMEMORY: {
                RealiMasina.smemory.takelis = takelis;
                break;
            }
            case HDD: {
                RealiMasina.hdd.writeTrack(takelis.toCharArray(), Integer.parseInt(new String(DB)));
                break;
            }
            case IOSTREAM: {
                // padaryti, kad dingtų šitas įspėjimas
                // esmė, kad reikia debug ne statinį padaryt, nes jis naudojamas debuginimui
                RealiMasina.ostream.setOstream(takelis);
                break;
            }
        }
        
    }
    
}
