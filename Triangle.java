import java.awt.*;

public class Triangle extends Figure {

    public static int VERTICES = 3;

    private int height;
    private int width;

    public Triangle(){
        super();
        height = 30;
        width = 40;
        xPosition = 0;
        color = "green";
    }

    protected void draw(){
        int[] xpoints = { xPosition, xPosition + (width/2), xPosition - (width/2) };
        int[] ypoints = { yPosition, yPosition + height, yPosition + height };
        drawShape(new Polygon(xpoints, ypoints, 3));
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