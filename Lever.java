/**
 * Representa la palanca de la máquina tragamonedas.
 */

public class Lever {
    private Rectangle conector;
    private Rectangle palo;
    private Circle bolita;

    /**
     * Construye una nueva palanca (Lever) y la dibuja en el lienzo.
     * Inicializa, dimensiona, colorea y posiciona el conector, el palo y la manija en su estado de reposo.
     */
    
    public Lever() {
        conector = new Rectangle();
        conector.changeSize(10, 30);
        conector.changeColor("black");
        conector.moveHorizontal(20);
        conector.moveVertical(140);
        conector.makeVisible();

        palo = new Rectangle();
        palo.changeSize(70, 10);
        palo.changeColor("black");
        palo.moveHorizontal(15);
        palo.moveVertical(80);
        palo.makeVisible();

        bolita = new Circle();
        bolita.changeSize(30);
        bolita.changeColor("red");
        bolita.moveHorizontal(5);
        bolita.moveVertical(60); 
        bolita.makeVisible();
    }

    /**
     * Mueve la palanca hacia abajo.
     */
    public void tirar() {
        palo.moveVertical(40);
        bolita.moveVertical(40);
        bolita.changeColor("gold");
    }

    /**
     * Regresa la palanca a su posición original (Estado 1).
     */
    public void soltar() {
        palo.moveVertical(-40);
        bolita.moveVertical(-40);
        bolita.changeColor("red");
    }
}