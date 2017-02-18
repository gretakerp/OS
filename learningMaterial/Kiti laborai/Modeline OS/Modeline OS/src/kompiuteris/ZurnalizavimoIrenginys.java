
package kompiuteris;

import kompiuteris.*;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class ZurnalizavimoIrenginys {

    public static boolean debugMode = true;

    /**
     * Creates a new instance of out
     */
    public ZurnalizavimoIrenginys() {
    }
    private static String ostream;

    public static void setOstream(String output) {
        ostream = output;
        OutputBuffer();
    }

    public static void OutputBuffer() {
        RealiMasina.langas.getDebugOutput().setText(RealiMasina.langas.getDebugOutput().getText() + ostream);
        RealiMasina.langas.getDebugOutput().setCaretPosition(RealiMasina.langas.getDebugOutput().getDocument().getLength());
    }

    public static void println(String out) {
        if (debugMode) {
            RealiMasina.langas.getDebugOutput().setText(RealiMasina.langas.getDebugOutput().getText() + out + '\n');
            RealiMasina.langas.getDebugOutput().setCaretPosition(RealiMasina.langas.getDebugOutput().getDocument().getLength());
            System.out.println(out);
        }
    }
}
