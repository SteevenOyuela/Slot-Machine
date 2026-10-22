import java.util.ArrayList;
/**
 * Representa una rueda de tipo "Lefty" en la máquina tragamonedas.
 * Esta clase hereda de Wheel, al momento de girar, ignora los parámetros aleatorios que recibe 
 * y en su lugar copia el color y la forma de la rueda a su izquierda.
 */
public class LeftyWheel extends Wheel {
    /** 
     * Referencia a la lista completa de ruedas de la máquina.
     * Es esencial para que esta rueda pueda encontrar su propio índice 
     * dinámicamente y consultar el estado de su vecina.
     */
    private ArrayList<Wheel> wheelsRef;
    
    /**
     * Constructor de la rueda Lefty.
     * 
     * @param index El índice donde se posicionará inicialmente la rueda (base 0).
     * @param wheels La lista de ruedas actual de la clase SlotMachine.
     */
    public LeftyWheel(int index, ArrayList<Wheel> wheels) {
        super(index); 
        this.wheelsRef = wheels;

        bordeCasilla.changeColor("purple");
        
        redibujar(); 
    }
    
    /**
     * Sobrescribe el comportamiento de giro.
     * Si la rueda tiene una vecina a su izquierda, intercepta y reemplaza 
     * el nuevo color y forma antes de ejecutar el giro real.
     * 
     * @param nuevoColor Color aleatorio enviado por la máquina.
     * @param nuevaForma Forma aleatoria enviada por la máquina.
     */
    @Override
    public void girar(Symbol nuevoSimbolo) {
        int indexActual = wheelsRef.indexOf(this);
        if (indexActual > 0) {
            Wheel ruedaIzquierda = wheelsRef.get(indexActual - 1);
            Symbol symIzq = ruedaIzquierda.getSimboloActual();
            
            if (symIzq != null) {
                nuevoSimbolo = symIzq.generarCopia();
            }
        }
        super.girar(nuevoSimbolo);
    }
}