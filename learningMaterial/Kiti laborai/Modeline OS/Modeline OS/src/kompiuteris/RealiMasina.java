
package kompiuteris;



import modelineos.*;

import procesai.Root;
import modelineos.FileConstants;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class RealiMasina {

    /**
     * Creates a new instance of Pc
     */
    public RealiMasina() {
    }
    public static OperacinesSistemosLangas langas = new OperacinesSistemosLangas();
    public static ZurnalizavimoIrenginys debug = new ZurnalizavimoIrenginys();
    public static IsvedimoIrenginys ostream = new IsvedimoIrenginys();
    public static Branduolys kernel = new Branduolys();
    public static IsorineAtmintis hdd = new IsorineAtmintis();
    public static SupervizorineAtmintis smemory = new SupervizorineAtmintis();
    public static KanaluIrenginys channel = new KanaluIrenginys();
    public static Procesorius cpu = new Procesorius();
    public static VartotojoAtmintis memory = new VartotojoAtmintis();
    public static PuslapiavimoMechanizmas pager = new PuslapiavimoMechanizmas();
    public static InformacinisLangas observeManager = new InformacinisLangas();
    public static VMLangas vmLangas = new VMLangas();

    /**
     * @param args the command line arguments
     */
    public static void start() {
        langas.setVisible(true);
        kernel.createProcess("Root", 99, -1, new Root());
		
        while (true) {
            cpu.vykdyti();
            cpu.test();
            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }
}
