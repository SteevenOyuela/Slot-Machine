/**
 * Símbolo que alterna su visibilidad (visible/invisible) cada vez que es seleccionado 
 * en una rueda. Hereda de la superclase Symbol y encapsula su propio estado de ocultamiento.
 */
public class ShySymbol extends Symbol {
    
    /**
     * Bandera interna que determina si el símbolo debe ocultarse gráficamente.
     */
    private boolean estaOculto;

    /**
     * Constructor público utilizado para añadir el símbolo al catálogo de la máquina.
     * Por defecto, el símbolo inicia en estado visible.
     * 
     * @param color El color.
     * @param forma La figura geométrica que representará el símbolo.
     */
    public ShySymbol(String color, String forma) {
        super(color, forma);
        this.estaOculto = false;
    }

    /**
     * Constructor privado utilizado exclusivamente por el Patrón Prototype.
     * Permite instanciar copias exactas para las ruedas conservando el estado de visibilidad actual.
     * 
     * @param color El color en formato CSS.
     * @param forma La figura geométrica del símbolo.
     * @param oculto El estado de visibilidad a heredar en la copia.
     */
    private ShySymbol(String color, String forma, boolean oculto) {
        super(color, forma);
        this.estaOculto = oculto;
    }

    /**
     * Sobrescribe el comportamiento polimórfico del símbolo.
     * Invierte el estado lógico de visibilidad (de visible a invisible y viceversa)
     * cada vez que el símbolo es seleccionado durante un giro.
     */
    @Override
    public void aplicarEfecto() {
        this.estaOculto = !this.estaOculto;
    }

    /**
     * Implementa el Patrón Prototype para evitar el bug visual de múltiples referencias en el Canvas.
     * 
     * @return Una nueva instancia física de ShySymbol con las mismas propiedades y estado actual.
     */
    @Override
    public Symbol generarCopia() {
        return new ShySymbol(this.getColor(), this.getForma(), this.estaOculto);
    }

    /**
     * Sobrescribe el método de visibilidad visual.
     * Intercepta la orden de dibujo del Canvas: solo ejecuta la visualización 
     * gráfica real (super.makeVisible()) si el símbolo NO está en su fase oculta.
     */
    @Override
    public void makeVisible() {
        if (!this.estaOculto) {
            super.makeVisible();
        }
    }
}