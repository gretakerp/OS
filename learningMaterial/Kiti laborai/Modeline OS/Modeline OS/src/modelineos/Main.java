
package modelineos;

import java.awt.Image;
import java.awt.Toolkit;
import kompiuteris.RealiMasina;
import kompiuteris.OperacinesSistemosLangas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static Thread machine = new Thread() {
        @Override
            public void run() {
                RealiMasina.start();
            }
        };
        
    static OperacinesSistemosLangas langas = new OperacinesSistemosLangas();
    
    public static void main(String args[]) {
        
        machine.setPriority(Thread.MIN_PRIORITY);
        machine.start();
        
        RealiMasina.langas = langas;
        RealiMasina.observeManager.setVisible(true);
        RealiMasina.observeManager.setLocation(
                RealiMasina.langas.getLocation().x + RealiMasina.langas.getWidth(),
                RealiMasina.langas.getLocation().y);
        RealiMasina.observeManager.setSize(RealiMasina.observeManager.getWidth(), RealiMasina.langas.getHeight());
        RealiMasina.vmLangas.setLocation(RealiMasina.langas.getLocation().x + 100,
                RealiMasina.observeManager.getLocation().y + RealiMasina.observeManager.getHeight());
        
        Image icon = Toolkit.getDefaultToolkit().getImage("program.png");
        RealiMasina.langas.setIconImage(icon);
        RealiMasina.observeManager.setIconImage(icon);
        RealiMasina.vmLangas.setIconImage(icon);
    }
}
