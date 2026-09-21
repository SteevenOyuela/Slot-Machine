import java.awt.geom.*;

public class Circle extends Figure {

    public static final double PI = 3.1416;

    private int diameter;

    public Circle(){
        super();
        diameter = 30;
        color = "blue";
    }

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