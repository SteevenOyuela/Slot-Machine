import java.awt.*;

public class Rectangle extends Figure {

    public static int EDGES = 4;

    private int height;
    private int width;

    public Rectangle(){
        super();
        height = 30;
        width = 40;
        color = "magenta";
    }

    protected void draw() {
        drawShape(new java.awt.Rectangle(xPosition, yPosition, width, height));
    }

    /**
     * Change the size to the new size
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidth the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }
}