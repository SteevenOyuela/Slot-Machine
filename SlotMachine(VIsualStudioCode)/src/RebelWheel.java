/**
 * Representa una rueda de tipo "Rebel" en la máquina tragamonedas.
 * Esta clase hereda de Wheel y tiene un comportamiento polimórfico rebelde:
 * es inmune a las alteraciones de estado, por lo que rechaza ser bloqueada.
 */
public class RebelWheel extends Wheel {
    
    /**
     * Constructor de la rueda Rebel.
     * 
     * @param index El índice donde se posicionará inicialmente la rueda (base 0).
     */
    public RebelWheel(int index) {
        super(index);

        bordeCasilla.changeColor("orange");
        
        redibujar();
    }
    
    /**
     * Sobrescribe el comportamiento de bloqueo.
     * Al intentar bloquear una rueda rebelde, esta intercepta y anula la instrucción.
     */
    @Override
    public void lock() {
        this.isLocked = false;
    }
}