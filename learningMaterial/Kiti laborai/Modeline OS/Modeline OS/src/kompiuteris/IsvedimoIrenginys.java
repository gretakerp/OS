
package kompiuteris;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class IsvedimoIrenginys {

    public static boolean debugMode = true;

    /**
     * Creates a new instance of out
     */
    public IsvedimoIrenginys() {
    }
    private static String ostream;

    public static void setOstream(String output) {
        ostream = output;
        OutputBuffer();
    }

    public static void OutputBuffer() {
        RealiMasina.langas.getOstream().setText(RealiMasina.langas.getOstream().getText() + ostream);
        RealiMasina.langas.getOstream().setCaretPosition(RealiMasina.langas.getOstream().getDocument().getLength());
    }

    public static void println(String out) {
        if (debugMode) {
            RealiMasina.langas.getOstream().setText(RealiMasina.langas.getOstream().getText() + out + '\n');
            RealiMasina.langas.getOstream().setCaretPosition(RealiMasina.langas.getOstream().getDocument().getLength());
            System.out.println(out);
        }
    }
}
