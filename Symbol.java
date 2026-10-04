/**
 * Representa un símbolo en la máquina tragamonedas.
 */
public class Symbol {
    protected String color;
    protected String forma;

    protected Figure figura;

    public Symbol(String color, String forma) {
        this.color = color;
        this.forma = forma.toLowerCase();

        figura = Figure.crear(this.forma);

        if(figura != null){
            figura.changeColor(color);
        }
    }

    public String getColor() { return color; }
    public String getForma() { return forma; }

    public void makeVisible() {
        if (figura != null) figura.makeVisible();
    }

    public void makeInvisible() {
        if (figura != null) figura.makeInvisible();
    }

    public void moverA(int x, int y) {
        if (figura != null) {
            figura.moveHorizontal(x);
            figura.moveVertical(y);
        }
    }

    public void moverHorizontal(int distancia) {
        if (figura != null) figura.moveHorizontal(distancia);
    }

    public void moverVertical(int distancia) {
        if (figura != null) figura.moveVertical(distancia);
    }
    
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

    public void aplicarEfecto() {
    }

    public Symbol generarCopia() {
        return new Symbol(this.getColor(), this.getForma());
    }
}