/**
 * Símbolo que reduce su tamaño progresivamente cada vez que es seleccionado en una rueda,
 * hasta quedar como un punto pequeño. Hereda de la superclase Symbol y maneja su propia escala.
 */
public class EphemeralSymbol extends Symbol {
    
    /**
     * Estado interno que almacena el tamaño actual de la figura geométrica.
     */
    private int tamanoActual;

    /**
     * Constructor público utilizado para añadir el símbolo al catálogo de la máquina.
     * Por defecto, el símbolo inicia con un tamaño inicial de 60.
     * 
     * @param color El color en formato CSS.
     * @param forma La figura geométrica que representará el símbolo.
     */
    public EphemeralSymbol(String color, String forma) {
        super(color, forma);
        this.tamanoActual = 60;
    }

    /**
     * Constructor privado utilizado exclusivamente por el Patrón Prototype.
     * Permite instanciar copias exactas para las ruedas conservando el nivel de encogimiento actual.
     * 
     * @param color El color.
     * @param forma La figura geométrica del símbolo.
     * @param tamano El tamaño actual a heredar en la copia.
     */
    private EphemeralSymbol(String color, String forma, int tamano) {
        super(color, forma);
        this.tamanoActual = tamano;
        this.cambiarTamano(this.tamanoActual);
    }

    /**
     * Sobrescribe el comportamiento polimórfico del símbolo.
     * Reduce el tamaño del símbolo en 10 unidades lógicas cada vez que su efecto es activado,
     * estableciendo un límite mínimo de 10 unidades para evitar que desaparezca por completo.
     */
    @Override
    public void aplicarEfecto() {
        if (this.tamanoActual > 10) {
            this.tamanoActual -= 10;
        }
    }

    /**
     * Implementa el Patrón Prototype para instanciar clones físicos del símbolo.
     * 
     * @return Una nueva instancia de EphemeralSymbol con el mismo color, forma y tamaño degradado actual.
     */
    @Override
    public Symbol generarCopia() {
        return new EphemeralSymbol(this.getColor(), this.getForma(), this.tamanoActual);
    }
}