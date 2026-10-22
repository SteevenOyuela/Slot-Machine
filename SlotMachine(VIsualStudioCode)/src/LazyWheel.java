/**
 * Representa una rueda de tipo "Lazy" (perezosa) en la máquina tragamonedas.
 * Esta clase hereda de Wheel y redefine el comportamiento de giro mediante polimorfismo:
 * tiene una probabilidad del 60% de aceptar el giro y actualizarse, y un 40% de 
 * probabilidad de abortar la acción reteniendo su símbolo actual.
 */
public class LazyWheel extends Wheel {
    /**
     * Constructor de la rueda Lazy.
     * 
     * @param index El índice donde se posicionará inicialmente la rueda (base 0).
     */
    public LazyWheel(int index) {
        super(index);

        bordeCasilla.changeColor("brown");
        
        redibujar();
    }
    
    /**
     * Sobrescribe el comportamiento de giro.
     * Antes de hacer el giro:
     * Tiene un 60% de probabilidad de girar normalmente.
     * TIene un 40% de probabilidad de abortar el giro y quedarse con la ficha que ya tenía.
     *
     * @param nuevoSimbolo Instancia del objeto Symbol enviado por la máquina tragamonedas.
     */
    @Override
    public void girar(Symbol nuevoSimbolo) {
        if (Math.random() >= 0.4) {
            super.girar(nuevoSimbolo);
        } else {
        }
    }
}