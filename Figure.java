import java.awt.*;

/**
 * Superclase abstracta que centraliza el estado y comportamiento común
 * de toda figura geométrica dibujable en el Canvas.
 */
public abstract class Figure {

    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;

    public Figure(){
        xPosition = 0;
        yPosition = 0;
        isVisible = false;
    }

    /**
     * Factory Method: crea y configura la figura correspondiente al
     * tamaño estándar de símbolo (60px), sin exponer los tipos concretos
     * a las clases que la invoquen.
     */
    public static Figure crear(String forma){
        Figure figura;
        switch(forma.toLowerCase()){
            case "circle":
                Circle c = new Circle();
                c.changeSize(60);
                figura = c;
                break;
            case "triangle":
                Triangle t = new Triangle();
                t.changeSize(60, 60);
                t.moveHorizontal(30);
                figura = t;
                break;
            case "rectangle":
                Rectangle r = new Rectangle();
                r.changeSize(60, 60);
                figura = r;
                break;
            default:
                figura = null;
        }
        return figura;
    }

    public void makeVisible(){
        isVisible = true;
        draw();
    }

    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    protected void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    protected void drawShape(Shape shape){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, shape);
            canvas.wait(10);
        }
    }

    protected abstract void draw();

    public void moveRight(){
        moveHorizontal(20);
    }

    public void moveLeft(){
        moveHorizontal(-20);
    }

    public void moveUp(){
        moveVertical(-20);
    }

    public void moveDown(){
        moveVertical(20);
    }

    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    public void slowMoveHorizontal(int distance){
        int delta;
        if(distance < 0) { delta = -1; distance = -distance; }
        else { delta = 1; }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    public void slowMoveVertical(int distance){
        int delta;
        if(distance < 0) { delta = -1; distance = -distance; }
        else { delta = 1; }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    public void changeColor(String newColor){
        color = newColor;
        draw();
    }
}