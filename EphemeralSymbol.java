/**
 * Símbolo que reduce su tamaño progresivamente cada vez que es seleccionado,
 * hasta quedar como un punto pequeño.
 */
public class EphemeralSymbol extends Symbol {
    private int tamanoActual;

    public EphemeralSymbol(String color, String forma) {
        super(color, forma);
        this.tamanoActual = 60;
    }

    private EphemeralSymbol(String color, String forma, int tamano) {
        super(color, forma);
        this.tamanoActual = tamano;
        this.cambiarTamano(this.tamanoActual);
    }

    @Override
    public void aplicarEfecto() {
        if (this.tamanoActual > 10) {
            this.tamanoActual -= 10;
        }
    }

    @Override
    public Symbol generarCopia() {
        return new EphemeralSymbol(this.getColor(), this.getForma(), this.tamanoActual);
    }
}