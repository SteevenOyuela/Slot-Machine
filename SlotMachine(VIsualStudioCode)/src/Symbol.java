/**
 * Representa un símbolo en la máquina tragamonedas.
 */
public class Symbol {
    protected String color;
    protected String forma;

    protected Figure figura;

    /**
     * Crea un simbolo asignandole un color y una forma especifica
     */
    public Symbol(String color, String forma) {
        this.color = color;
        this.forma = forma.toLowerCase();

        figura = Figure.crear(this.forma);

        if(figura != null){
            figura.changeColor(color);
        }
    }

    /**
     * Retorna el color actual o la forma
     */
    public String getColor() { return color; }
    public String getForma() { return forma; }

    /**
     * Se muestra o se oculta la figura del simbolo en el lienzo
     */
    public void makeVisible() {
        if (figura != null) figura.makeVisible();
    }
    public void makeInvisible() {
        if (figura != null) figura.makeInvisible();
    }

    /**
     * Desplaza el simbolo a una nueva posicion sumando los valores indicados
     * en coordenadas horizontales y verticales
     */
    public void moverA(int x, int y) {
        if (figura != null) {
            figura.moveHorizontal(x);
            figura.moveVertical(y);
        }
    }

    /**
     * Mueve el simbolo de manera horizontal o vertical segun los pixeles
     * que se especifiquen
     */
    public void moverHorizontal(int distancia) {
        if (figura != null) figura.moveHorizontal(distancia);
    }
    public void moverVertical(int distancia) {
        if (figura != null) figura.moveVertical(distancia);
    }
    
    /**
     * Cambia las dimensiones de la figura interna del simbolo, ademas
     * valida si es un circulo, triangulo o rectangulo
     */
    public void cambiarTamano(int nuevoTamano) {
        if (figura != null) {
            if (figura instanceof Circle) {
                ((Circle) figura).changeSize(nuevoTamano);
            } 
            else if (figura instanceof Triangle) {
                ((Triangle) figura).changeSize(nuevoTamano, nuevoTamano);
            } 
            else if (figura instanceof Rectangle) {
                ((Rectangle) figura).changeSize(nuevoTamano, nuevoTamano);
            }
        }
    }

    /**
     * Es reservado para aplicar efectos visuales especificos de cada simbolo
     */
    public void aplicarEfecto() {
    }

    /**
     * Crea y retorna un nuevo objeto symbol igual al actual con el mismo
     * color y forma
     */
    public Symbol generarCopia() {
        return new Symbol(this.getColor(), this.getForma());
    }
}