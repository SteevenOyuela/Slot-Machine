/**
 * Representa un símbolo en la máquina tragamonedas.
 * Puede tomar la forma de un círculo, triángulo o cuadrado.
 */

public class Symbol {
    private String color;
    private String forma; // "circle", "triangle", o "rectangle"
    
    private Circle circulo;
    private Triangle triangulo;
    private Rectangle cuadrado;

    /**
     * Crea un nuevo símbolo.
     * @param color El color en formato CSS (ej. "red", "blue", "yellow").
     * @param forma La figura que representará el símbolo ("circle", "triangle", "rectangle").
     */
    public Symbol(String color, String forma) {
        this.color = color;
        this.forma = forma.toLowerCase();

        if (this.forma.equals("circle")) {
            circulo = new Circle();
            circulo.changeColor(color);
            circulo.changeSize(60);
        } 
        else if (this.forma.equals("triangle")) {
            triangulo = new Triangle();
            triangulo.changeColor(color);
            triangulo.changeSize(60, 60);
        } 
        else if (this.forma.equals("rectangle")) {
            cuadrado = new Rectangle();
            cuadrado.changeColor(color);
            cuadrado.changeSize(60, 60);
        }
    }

    /**
     * Retorna el color del símbolo (necesario para la lógica de validación).
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Retorna la forma geométrica del símbolo ("circle", "triangle", "rectangle").
     */
    public String getForma() {
        return forma;
    }

    public void makeVisible() {
        if (circulo != null) circulo.makeVisible();
        if (triangulo != null) triangulo.makeVisible();
        if (cuadrado != null) cuadrado.makeVisible();
    }

    public void makeInvisible() {
        if (circulo != null) circulo.makeInvisible();
        if (triangulo != null) triangulo.makeInvisible();
        if (cuadrado != null) cuadrado.makeInvisible();
    }

    /**
     * Mueve el símbolo a una coordenada específica (útil para posicionarlo dentro de la rueda).
     */
    public void moverA(int x, int y) {
        if (circulo != null) {
            circulo.moveHorizontal(x);
            circulo.moveVertical(y);
        }
        if (triangulo != null) {
            triangulo.moveHorizontal(x);
            triangulo.moveVertical(y);
        }
        if (cuadrado != null) {
            cuadrado.moveHorizontal(x);
            cuadrado.moveVertical(y);
        }
    }
    
    /**
     * Desplaza el símbolo horizontalmente.
     * @param distancia Píxeles a mover (positivo a la derecha, negativo a la izquierda).
     */
    public void moverHorizontal(int distancia) {
        if (circulo != null) circulo.moveHorizontal(distancia);
        if (triangulo != null) triangulo.moveHorizontal(distancia);
        if (cuadrado != null) cuadrado.moveHorizontal(distancia);
    }
    
    /**
     * Desplaza el símbolo verticalmente.
     * @param distancia Píxeles a mover (positivo hacia abajo, negativo hacia arriba).
     */
    public void moverVertical(int distancia) {
        if (circulo != null) circulo.moveVertical(distancia);
        if (triangulo != null) triangulo.moveVertical(distancia);
        if (cuadrado != null) cuadrado.moveVertical(distancia);
    }
}