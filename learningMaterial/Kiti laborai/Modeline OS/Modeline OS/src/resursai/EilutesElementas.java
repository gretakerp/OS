
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

// Naudojamas kaip Resuras "Ivedimas". Turima omeny, kad jis saugo uzduoties
// failo vardą išorinėje atmintyje
public class EilutesElementas extends Elementas {

    private String eilute = "";
    
    @Override
    public String toString() {
        return "IvedimasElementas: " + eilute;
    }

    public String getEilute() {
        return eilute;
    }

    public void setEilute(String uzduotis) {
        this.eilute = uzduotis;
    }

    public EilutesElementas(String name, boolean reusable) {
        super(name, reusable);
    }
    
    public EilutesElementas(String name, boolean reusable, String eilute) {
        super(name, reusable);
        this.eilute = eilute;
    }
    
}
