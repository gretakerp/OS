
package kompiuteris;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class VartotojoAtmintis {
    public static final int MSIZE = 100;
    /** Creates a new instance of Atmintis */
    public VartotojoAtmintis() {
    }
    
    public char [][] memory = new char[MSIZE][40];
        public String readTrack(int takelioNr) {
            return new String(memory[takelioNr]);
        }
        public void writeTrack(String str, int takelioNr) {
            if (str.length() > 40) str = str.substring(0, 40);
            memory[takelioNr] = str.toCharArray();
        }
}
