/**
 * Representa Las ruedas.
 * 
 * Estas casillas estan represetnadas de color balnco y se encuentran distribuidas en el tablero.
 */

public class Wheel {
    private Rectangle casilla;
    private int posicionX; // Guardamos la posición actual
    private Symbol simboloActual; // Memoria de cada rueda conforme a cada simbolo
    private Rectangle bordeCasilla; // simulacion del borde

    /**
     * Crea una nueva rueda y la posiciona dinámicamente en el lienzo.
     * @param index El índice de la rueda (0 para la primera, 1 para la segunda, etc.)
     */
    public Wheel(int index) {
        casilla = new Rectangle();
        casilla.changeSize(140, 100);
        casilla.changeColor("white");
        
        // Calculamos la posición X basada en el índice
        posicionX = 70 + (index * 120);
        
        casilla.moveHorizontal(posicionX);
        casilla.moveVertical(100);
        casilla.makeVisible();
        
        bordeCasilla = new Rectangle();
        bordeCasilla.changeSize(150, 110); 
        bordeCasilla.changeColor("black");
        bordeCasilla.moveHorizontal(posicionX - 5); 
        bordeCasilla.moveVertical(95);
        bordeCasilla.makeVisible();
    }
    
    /**
     * Recalcula la posición de la rueda y desplaza el rectángulo.
     * @param nuevoIndex El nuevo índice de la rueda en la máquina.
     */
    public void actualizarPosicion(int nuevoIndex) {
        int nuevaPosicionX = 70 + (nuevoIndex * 120);
        int distanciaAMover = nuevaPosicionX - posicionX; // Calcula el desplazamiento
        
        bordeCasilla.moveHorizontal(distanciaAMover);
        casilla.moveHorizontal(distanciaAMover);
        
        // Si la rueda tiene un símbolo, lo movemos la misma distancia
        if (simboloActual != null) {
            simboloActual.moverHorizontal(distanciaAMover);
        }
        
        posicionX = nuevaPosicionX; 
    }
    
    /**
     * Método para asegurar que la rueda quede encima del fondo gris
     */
    
    public void redibujar() {
        bordeCasilla.makeVisible();
        casilla.makeVisible();
        if (simboloActual != null) {
            simboloActual.makeVisible();
        }
    }
    
    /**
     * Borra la representación visual de la rueda del lienzo.
     */
    public void makeInvisible() {
        bordeCasilla.makeInvisible();
        casilla.makeInvisible();
        
        if (simboloActual != null) {
            simboloActual.makeInvisible();
        }
    }
    
    /**
     * Asigna un nuevo símbolo a esta rueda y lo dibuja centrado.
     * @param color El color del símbolo.
     * @param forma La figura geométrica ("circle", "triangle", "rectangle").
     */
    public void setSymbol(String color, String forma) {
        // Ocultar el anterior si existe
        if (simboloActual != null) {
            simboloActual.makeInvisible();
        }

        // Crear el nuevo
        simboloActual = new Symbol(color, forma);

        // Calibración exacta para centrar:
        // EJE X: Posición de la rueda + 20 px de margen
        int ajusteHorizontal = posicionX + 20; 
        
        // EJE Y: 100 (donde empieza la rueda) + 38 px de margen
        int ajusteVertical = 138; 
        
        // Aplicamos los movimientos
        simboloActual.moverHorizontal(ajusteHorizontal);
        simboloActual.moverVertical(ajusteVertical); 
        
        simboloActual.makeVisible();
    }
    
    /**
     * Realiza una animación de giro poniendo la casilla en negro por un instante
     * antes de revelar el nuevo símbolo.
     */
    public void girar(String nuevoColor, String nuevaForma) {
        // Ocultar el símbolo actual y poner la casilla "en negro"
        if (simboloActual != null) {
            simboloActual.makeInvisible();
        }
        casilla.changeColor("black");
        
        // Pausa para el cambio de color (implementación con IA)
        Canvas.getCanvas().wait(200); 
        
        // Vuelve al color blanco
        casilla.changeColor("white"); 
        
        // Dibuja el nuevo símbolo
        setSymbol(nuevoColor, nuevaForma);
    }
    
    /**
     * Consulta el color del símbolo que la rueda está mostrando actualmente.
     * @return El color en formato String, o una cadena vacía si no tiene símbolo.
     */
    public String getColorActual() {
        if (simboloActual != null) {
            return simboloActual.getColor();
        }
        return "";
    }
    
    /**
     * Cambia el color de fondo de la casilla de la rueda.
     * @param color El color deseado.
     */
    public void cambiarColorFondo(String color) {
        if (casilla != null) {
            casilla.changeColor(color);
        }
    }
}