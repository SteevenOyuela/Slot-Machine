/**
 * Símbolo que alterna su visibilidad cada vez que es seleccionado en una rueda.
 */
public class ShySymbol extends Symbol {
    private boolean estaOculto;

    public ShySymbol(String color, String forma) {
        super(color, forma);
        this.estaOculto = false;
    }

    private ShySymbol(String color, String forma, boolean oculto) {
        super(color, forma);
        this.estaOculto = oculto;
    }

    @Override
    public void aplicarEfecto() {
        this.estaOculto = !this.estaOculto;
    }

    @Override
    public Symbol generarCopia() {
        return new ShySymbol(this.getColor(), this.getForma(), this.estaOculto);
    }

    @Override
    public void makeVisible() {
        if (!this.estaOculto) {
            super.makeVisible();
        }
    }
}