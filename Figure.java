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

    /**
     * Inicializa la posicion de la figura con coordenadas (0,0)
     * y se establece en invisible por defecto
     */
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

    /**
     * Hace visible la figura del lienzo y la dibuja
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible la figura del lienzo y cambia su estado a invisible
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    /**
     * Borra la figura del lienzo solo si se esta en estado visible
     */
    protected void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    /**
     * Dibuja una forma geometrica especifica en el lienzo con el color
     * actual solo si la figura es visible
     */
    protected void drawShape(Shape shape){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, shape);
            canvas.wait(10);
        }
    }

    protected abstract void draw();

    /**
     * Desplaza la figura 20 pixeles hacia la derecha
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Desplaza la figura 20 pixeles hacia la izquierda
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Desplaza la figura 2o pixeles hacia arriba
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Desplaza la figura 20 pixeles hacia abajo
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Borra la figura, actualiza su posicion horizontal sumandole la indicada
     * y la vuelve a dibujar
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Borra la figura, actualiza su posicion vertical sumandole la distancia indicada
     * y la vuelve a dibujar
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Desplaza la figura horizontalmente pixel por pixel segun la distancia dada
     */
    public void slowMoveHorizontal(int distance){
        int delta;
        if(distance < 0) { delta = -1; distance = -distance; }
        else { delta = 1; }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Desplaza la figura verticalmente pixel por pixel segun su distancia dada
     */
    public void slowMoveVertical(int distance){
        int delta;
        if(distance < 0) { delta = -1; distance = -distance; }
        else { delta = 1; }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Cambia el color de la figura y la vuelve a dibujar con el nuevo color
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }
}