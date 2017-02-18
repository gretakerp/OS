package kompiuteris;

import modelineos.*;
import modelineos.PertraukimuApdorojimas;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */
public class Procesorius extends Thread {

    public char[] PTR = {'0', '0'};
    public char[] R = {'0', '0', '0', '0'};
    // In VirtualMachine first 20 words are DataSegment
    // so CodeSegment starts at 20, because we start numbering from 0
    public char[] IC = {'2', '0'};
    public char MODE = 'S';
    // IR = TI PI SI        
    public char[] IR = {'0', '0', '0'};
    public char[] TMR = {'0', '9'};
    public char C = '0';
    // SP starts at 80, but first command will increment it 
    public char[] SP = {'7', '9'};
    public int ioAddr;

    /** Creates a new instance of Registers */
    public Procesorius() {
    }

    // <editor-fold desc="Procesoriaus komandos" defaultstate="collapsed">
    private void incrementIC() {
        int ip = Integer.parseInt(new String(IC));
        ip++;
        ip = ip % 100;
        String tmp = ip + "";
        if (tmp.length() < 2) {
            tmp = "0" + tmp;
        }
        IC = tmp.toCharArray();
    }

    private void incSP() throws StackOverFlowException {
        int stack = Integer.parseInt(new String(SP));
        stack++;
        if (stack > 99) {
            throw new StackOverFlowException();
        }
        stack = stack % 100;
        String tmp = stack + "";
        if (tmp.length() < 2) {
            tmp = "0" + tmp;
        }
        SP = tmp.toCharArray();
    }

    private void decSP() throws StackOverFlowException {
        int stack = Integer.parseInt(new String(SP));
        stack--;
        if (stack < 79) {
            throw new StackOverFlowException();
        }
        stack = stack % 100;
        String tmp = stack + "";
        if (tmp.length() < 2) {
            tmp = "0" + tmp;
        }
        SP = tmp.toCharArray();
    }

    private char[] multiply(char[] first, char[] second) {
        int w1 = Integer.parseInt(new String(first).trim());
        int w2 = Integer.parseInt(new String(second).trim());
        int result = (w1 * w2) % 10000;
        String tmp = result + "";
        while (tmp.length() < 4) {
            tmp = "0" + tmp;
        }
        return tmp.toCharArray();
    }

    private char[] divide(char[] first, char[] second) {
        int w1 = Integer.parseInt(new String(first).trim());
        int w2 = Integer.parseInt(new String(second).trim());
        int result = (w1 % w2) % 10000;
        String tmp = result + "";
        while (tmp.length() < 4) {
            tmp = "0" + tmp;
        }
        return tmp.toCharArray();
    }

    private char[] add(char[] word1, char[] word2) {
        String tmp = new String(word1);
        tmp = tmp.trim();
        int w1 = Integer.parseInt(tmp);
        tmp = new String(word2);
        tmp = tmp.trim();
        int w2 = Integer.parseInt(tmp);
        int result = (w1 + w2) % 10000;
        tmp = result + "";
        while (tmp.length() < 4) {
            tmp = "0" + tmp;
        }
        return tmp.toCharArray();
    }

    private char[] sub(char[] word1, char[] word2) {
        String tmp = new String(word1);
        tmp = tmp.trim();
        int w1 = Integer.parseInt(tmp);
        tmp = new String(word2);
        tmp = tmp.trim();
        int w2 = Integer.parseInt(tmp);
        int result = (w1 - w2) % 10000;
        tmp = result + "";
        while (tmp.length() < 4) {
            tmp = "0" + tmp;
        }
        return tmp.toCharArray();
    }
    // </editor-fold>

    public void vykdyti() {
        if (RealiMasina.cpu.MODE == 'U') {
            try {
                String command = new String(RealiMasina.pager.getWord(IC, PTR)).toUpperCase();
                if (RealiMasina.langas.jCheckBoxVMStep.isSelected()) {
                    RealiMasina.kernel.waitForStepAndRefresh(
                            RealiMasina.langas.jCheckBoxVMStep.isSelected());
                }
                String xy = command.substring(2).toUpperCase();
                if (command.startsWith("LR")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    R = RealiMasina.pager.getWord(address, PTR);
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("SR")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    RealiMasina.pager.setWord(address, PTR, R);
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("ADD")) {
                    char[] w1 = RealiMasina.pager.getWord(SP, PTR);
                    decSP();
                    char[] w2 = RealiMasina.pager.getWord(SP, PTR);
                    RealiMasina.pager.setWord(SP, PTR, add(w1, w2));
                    incrementIC();
                    tiktak();
                    tiktak();  
                } else if (command.startsWith("AD")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    char[] w = RealiMasina.pager.getWord(address, PTR);
                    R = add(R, w);
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("SUB")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    char[] w = RealiMasina.pager.getWord(address, PTR);
                    R = sub(R, w);
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("SU")) {
                    char[] w1 = RealiMasina.pager.getWord(SP, PTR);
                    decSP();
                    char[] w2 = RealiMasina.pager.getWord(SP, PTR);
                    RealiMasina.pager.setWord(SP, PTR, sub(w1, w2));
                    incrementIC();
                    tiktak();
                    tiktak();
                } else if (command.startsWith("DIV")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    char[] w = RealiMasina.pager.getWord(address, PTR);
                    R = divide(R, w);
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("DI")) {
                    char[] w1 = RealiMasina.pager.getWord(SP, PTR);
                    decSP();
                    char[] w2 = RealiMasina.pager.getWord(SP, PTR);
                    RealiMasina.pager.setWord(SP, PTR, divide(w1, w2));
                    incrementIC();
                    tiktak();
                    tiktak();
                } else if (command.startsWith("CMR")) {
                    char[] w = RealiMasina.pager.getWord(SP, PTR);
                    if ((w[0] == R[0]) && (w[1] == R[1]) && (w[2] == R[2]) && (w[3] == R[3])) {
                        C = '1';
                    } else {
                        C = '0';
                    }
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("CR")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    char[] w = RealiMasina.pager.getWord(address, PTR);
                    if ((w[0] == R[0]) && (w[1] == R[1]) && (w[2] == R[2]) && (w[3] == R[3])) {
                        C = '1';
                    } else {
                        C = '0';
                    }
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("JE")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    if (C == '1') {
                        IC = address;
                    } else {
                        incrementIC();
                    }
                    tiktak();
                } else if (command.startsWith("JN")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    if (C == '0') {
                        IC = address;
                    } else {
                        incrementIC();
                    }
                    tiktak();
                } else if (command.startsWith("JM")) {
                    char[] address = command.substring(2, 4).toCharArray();
                    IC = address;
                    tiktak();
                } else if (command.startsWith("GD")) {
                    ioAddr = Integer.parseInt(command.substring(2, 4));
                    IR[2] = '1';
                    incrementIC();
                    tiktak();
                    tiktak();
                    tiktak();
                } else if (command.startsWith("PD")) {
                    ioAddr = Integer.parseInt(command.substring(2, 4));
                    IR[2] = '2';
                    incrementIC();
                    tiktak();
                    tiktak();
                    tiktak();
                } else if (command.startsWith("PUSH")) {
                    incSP();
                    RealiMasina.pager.setWord(SP, PTR, R);
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("POP")) {
                    R = RealiMasina.pager.getWord(SP, PTR);
                    decSP();
                    incrementIC();
                    tiktak();
                } else if (command.startsWith("MUL")) {
                    char[] first = RealiMasina.pager.getWord(SP, PTR);
                    decSP();
                    char[] second = RealiMasina.pager.getWord(SP, PTR);
                    RealiMasina.pager.setWord(SP, PTR, multiply(first, second));
                    incrementIC();
                    tiktak();
                    tiktak();
                } else if (command.startsWith("HALT")) {
                    IR[2] = '3';
                    tiktak();
                } else {
                    IR[1] = '2';
                }
            } catch (NumberFormatException ex) {
                IR[1] = '1';
            } catch (InvalidAddressException ex) {
                IR[1] = '1';
            } catch (StackOverFlowException ex) {
                IR[1] = '3';
            }

        } else {
            if (RealiMasina.kernel.getActiveProc() != null) {
                RealiMasina.kernel.getActiveProc().getProcess().doit();
                tiktak();
            } else {
                try {
                    //out.println("Im sleeping..zzzz.zzz..");
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void tiktak() {
        int tmp_TMR = Integer.parseInt(new String(TMR));
        tmp_TMR--;
        TMR = Integer.toString(tmp_TMR).toCharArray();
    }

    public void test() {
        int tmp_TMR = Integer.parseInt(new String(RealiMasina.cpu.TMR));
        if (tmp_TMR <= 0) {
            TMR = "09".toCharArray();
            PertraukimuApdorojimas.timer();
        }

        switch (IR[1]) {
            case '1':
                IR[1] = '0';
                PertraukimuApdorojimas.invalidAddress(RealiMasina.kernel.getActiveProc().getPId());
                break;
            case '2':
                IR[1] = '0';
                PertraukimuApdorojimas.invalidOperationCode(RealiMasina.kernel.getActiveProc().getPId());
                break;
            case '3':
                IR[1] = '0';
                PertraukimuApdorojimas.overFlow(RealiMasina.kernel.getActiveProc().getPId());
        }

        switch (IR[2]) {
            case '1':
                PertraukimuApdorojimas.GD(RealiMasina.kernel.getActiveProc().getPId());
                IR[2] = '0';
                break;
            case '2':
                PertraukimuApdorojimas.PD(RealiMasina.kernel.getActiveProc().getPId());
                IR[2] = '0';
                break;
            case '3':
                PertraukimuApdorojimas.HALT(RealiMasina.kernel.getActiveProc().getPId());
                IR[2] = '0';
                break;
            case '4':
                IR[2] = '0';
                PertraukimuApdorojimas.input();
                break;

        }
    }
}
