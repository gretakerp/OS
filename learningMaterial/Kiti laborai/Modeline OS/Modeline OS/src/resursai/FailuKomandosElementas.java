
package resursai;

/**
 *
 * @author Povilas Panavas & Aurelijus Rožėnas aka TEA GROUP
 */

public class FailuKomandosElementas extends Elementas{

    public FailuKomandosElementas(String name, boolean reusable, String komanda) {
        super(name, reusable);
        this.komanda = komanda;
    }
    
    private String komanda;

    public String getKomanda() {
        return komanda;
    }

    public void setKomanda(String komanda) {
        this.komanda = komanda;
    }
    
    @Override
    public String toString() {
        return "Failo komanda: " + komanda;
    }

}
