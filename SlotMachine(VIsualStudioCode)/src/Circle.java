import java.awt.geom.*;

public class Circle extends Figure {

    public static final double PI = 3.1416;

    private int diameter;

    /**
     * El constructor el cual inicializael circulo con las medidas establecidas
     */
    public Circle(){
        super();
        diameter = 30;
        color = "blue";
    }

    /**
     * Dibuja la figura del circulo en el lienzo segun su posicion y su diametro
     */
    protected void draw(){
        drawShape(new Ellipse2D.Double(xPosition, yPosition, diameter, diameter));
    }

    /**
     * Change the size.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    public void changeSize(int newDiameter){
        erase();
        diameter = newDiameter;
        draw();
    }
}