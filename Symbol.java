/**
 * Representa un símbolo en la máquina tragamonedas.
 */
public class Symbol {
    private String color;
    private String forma;

    // ¡LA MAGIA DEL POLIMORFISMO!
    // Symbol solo conoce Figure; la creación concreta la resuelve el factory.
    private Figure figura;

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
}