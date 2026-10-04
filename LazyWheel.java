/**
 * Representa una rueda de tipo "Rebel" en la máquina tragamonedas.
 * Esta clase hereda de Wheel y tiene un comportamiento polimórfico rebelde:
 * es inmune a las alteraciones de estado, por lo que rechaza ser bloqueada.
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
     * Antes de hacer el giro, Tiene un 60% de probabilidad de girar normalmente, 
     * y un 40% de probabilidad de abortar el giro y quedarse con la ficha que ya tenía.
     *
     * @param nuevoColor Color aleatorio enviado por la máquina.
     */
    @Override
    public void girar(Symbol nuevoSimbolo) {
        if (Math.random() >= 0.4) {
            super.girar(nuevoSimbolo);
        } else {
        }
    }
}