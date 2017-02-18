
package kompiuteris;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class IsorineAtmintis {
    public static final String EMPTY =          "                                        ";
    public static final String FILETABLEEND =   "$FIN                                    ";
    public static final int FSSIZE = 1000;
	
    private RandomAccessFile file;
	
    /** Creates a new instance of IsorineAtmintis */
    public IsorineAtmintis() {
        try {
            File f = new File("data.dat");
            if (!f.exists()) {
                file = new RandomAccessFile(f, "rw");
                format();
            } else {
                file = new RandomAccessFile(f, "rw");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
			System.exit(0);
        } catch (IOException ex) {
            System.exit(0);
        }
    }
    
    public void format() {
            for (int i = 0; i < FSSIZE; i++) 
                writeTrack(EMPTY.toCharArray(), i);
            writeTrack(FILETABLEEND.toCharArray(), FSSIZE);
    }
	
    public void showMemory() {
        IsvedimoIrenginys.println("*****MEMORY******");
        for (int i = 0; i <= FSSIZE; i++) {
            IsvedimoIrenginys.println("\""+RealiMasina.hdd.readTrack(i)+"\"");
        }
    }
	
    public String readTrack (int takelioNr) {
        try {
            file.seek(takelioNr * 40 * 2);
            char[] ret = new char[40];
            for (int i = 0; i < 40; i++)
                ret[i] = file.readChar();
            return new String(ret);
        } catch (IOException ex) {
            return null;
        }
    }
	
    public void writeTrack (char[] info, int takelioNr) {
        try {
            file.seek(takelioNr * 40 * 2);
            file.writeChars(new String(info));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
    public boolean isEmptyTrack(int takelioNr) {
        return (readTrack(takelioNr).equals(EMPTY));
    }
	
    public int isAvailable(int amount) {
        int i = 0, nr = -1, address = -1;
        while (i<amount) {
            if (isEmptyTrack(++nr)) {
                if (i == 0) address = nr;
                i++;
            }
            else {
                i = 0;
                address = -1;
            }
        }
        return address;
    }
	
	@Override
    protected void finalize() throws Throwable {
        try {
            file.close();
        }
        finally {
            super.finalize();
        }
    }
    
}
