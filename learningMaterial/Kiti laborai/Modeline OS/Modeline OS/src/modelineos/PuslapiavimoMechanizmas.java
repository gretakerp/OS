
package modelineos;

import kompiuteris.RealiMasina;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class PuslapiavimoMechanizmas {
    
    /** Creates a new instance of PuslapiavimoMechanizmas */
    public PuslapiavimoMechanizmas() {
    }
    
    private int getPageTableAddress(char[] ptr) {
        String tmp = ptr[0] + "" + ptr[1];
        return Integer.parseInt(tmp);
    }
    
//    private int getPageTableLength(char[] ptr) {
//        String tmp = ptr[1] + "";
//        return Integer.parseInt(tmp);
//    }
    
    public int getParagraphNumber(int virtualAddress, char[] ptr) {
        int pt = getPageTableAddress(ptr);
        String tmp = RealiMasina.memory.memory[pt][virtualAddress / 10 * 4 + 1] + "" + RealiMasina.memory.memory[pt][virtualAddress / 10 * 4 + 2] + "" + RealiMasina.memory.memory[pt][virtualAddress / 10 * 4 + 3];
        return Integer.parseInt(tmp.trim());
    }
    
    public char[] getWord(char[] virtualAddress, char[] ptr) throws InvalidAddressException {
        try {
            char[] result = new char[4];
            
            int pt = getPageTableAddress(ptr);
            String paragraph = virtualAddress[0] + "";
            
            int par = Integer.parseInt(paragraph);
            
//            if (par > getPageTableLength(ptr)) throw new InvalidAddressException();
            
            String tmp = RealiMasina.memory.memory[pt][par * 4 + 1] + "" + RealiMasina.memory.memory[pt][par * 4 + 2] + "" + RealiMasina.memory.memory[pt][par * 4 + 3];
            int realPar = Integer.parseInt(tmp.trim());
            
            String wordNum = virtualAddress[1] + "";
            int word = Integer.parseInt(wordNum);
            
            for (int i = 0; i < 4; i++) result[i] = RealiMasina.memory.memory[realPar][word * 4 + i];
            
            return result;
        } catch (Exception ex) {
            throw new InvalidAddressException();
        }
    }

    public void setWord(char[] virtualAddress, char[] ptr, char[] value) throws InvalidAddressException {
        try {
            int pt = getPageTableAddress(ptr);
            String paragraph = virtualAddress[0] + "";
            
            int par = Integer.parseInt(paragraph);
            
//            if (par > getPageTableLength(ptr)) throw new InvalidAddressException();
            
            String tmp = RealiMasina.memory.memory[pt][par * 4 + 1] + "" + RealiMasina.memory.memory[pt][par * 4 + 2] + "" + RealiMasina.memory.memory[pt][par * 4 + 3];
            int realPar = Integer.parseInt(tmp.trim());
            
            String wordNum = virtualAddress[1] + "";
            int word = Integer.parseInt(wordNum);
            
            for (int i = 0; i < 4; i++) RealiMasina.memory.memory[realPar][word * 4 + i] = value[i];
            
        } catch (Exception ex) {
            throw new InvalidAddressException();
        }
    }
}
