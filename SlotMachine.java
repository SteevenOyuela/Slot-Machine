import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Representa una máquina tragamonedas.
 * 
 * La máquina utiliza los componentes gráficos de la librería shapes
 * para construir su representación visual.
 */

public class SlotMachine {
    private Canvas canvas;
    private Rectangle parteTrasera;
    private Rectangle bordeTrasero;
    
    private Rectangle base;
    private int posicionXBase;
    
    private ArrayList<Wheel> wheels;
    private ArrayList<Symbol> symbols; 
    
    private boolean isVisible;
    
    private Lever palanca;
    
    private boolean ok;

    /**
     * Crea una nueva máquina tragamonedas.
     */
    public SlotMachine() {
        canvas = Canvas.getCanvas();
    
        bordeTrasero = new Rectangle();
        bordeTrasero.changeSize(210, 390);
        bordeTrasero.changeColor("black");
        bordeTrasero.moveHorizontal(45);
        bordeTrasero.moveVertical(65);
        bordeTrasero.makeVisible();
    
        parteTrasera = new Rectangle();
        parteTrasera.changeSize(200, 380);
        parteTrasera.changeColor("lightgray");
        parteTrasera.moveHorizontal(50);
        parteTrasera.moveVertical(70);
        parteTrasera.makeVisible();
        
        base = new Rectangle();
        base.changeSize(25, 150);
        base.changeColor("black"); 
        
        int anchoInicial = 20 + (3 * 120); 
        posicionXBase = 50 + (anchoInicial / 2) - 75;
        
        base.moveHorizontal(posicionXBase);
        base.moveVertical(272);
        base.makeVisible();
        
        palanca = new Lever();
        
        symbols = new ArrayList<Symbol>();

        addSymbol("maroon", "circle");
        addSymbol("green", "circle");
        addSymbol("blue", "triangle");
        addSymbol("purple", "triangle");
        addSymbol("yellow", "rectangle");
        addSymbol("turquoise", "rectangle");
        
        wheels = new ArrayList<Wheel>();
        
        actualizarRuedas();
        
        makeVisible();
    }

    /**
     * Hace visible la máquina tragamonedas.
     */
    public void makeVisible()
    {
        isVisible = true;
        canvas.setVisible(true);
    }

    /**
     * Hace invisible la máquina tragamonedas.
     */
    public void makeInvisible()
    {
        isVisible = false;
        canvas.setVisible(false);
    }
    
    /**
     * Recorre la lista de ruedas y actualiza la posición gráfica de cada una.
     * También ajusta el tamaño del fondo de la máquina para que coincida.
     */
    private void actualizarRuedas() {
        int nuevoAncho = 20 + (wheels.size() * 120);
        bordeTrasero.changeSize(210, nuevoAncho + 10);
        parteTrasera.changeSize(200, nuevoAncho);
        
        for (int i = 0; i < wheels.size(); i++) {
            Wheel ruedaActual = wheels.get(i);
            ruedaActual.actualizarPosicion(i);
        }
        
        int nuevaPosicionXBase = 50 + (nuevoAncho / 2) - 75;
        int distanciaAMoverBase = nuevaPosicionXBase - posicionXBase;
        
        base.moveHorizontal(distanciaAMoverBase);
        posicionXBase = nuevaPosicionXBase;
        base.makeVisible();
    }
    
    /**
     * Cambia el color de las casillas de todas las ruedas.
     * @param color El color al que se cambiarán las casillas.
     */
    private void pintarCasillasRuedas(String color) {
        for (Wheel rueda : wheels) {
            rueda.cambiarColorFondo(color);
        }
    }
    
    /**
     * Adiciona una nueva rueda a la máquina validando estrictamente los casos frontera.
     * @param pos La posición donde se desea insertar la rueda (iniciando en 1).
     */
    public void addWheel(int pos) {
        int maxPosValida = wheels.size() + 1;
        
        if (pos < 1 || pos > maxPosValida) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, 
                    "Error: No se puede agregar en la posición " + pos + ".\n" +
                    "Actualmente solo puedes usar posiciones del 1 al " + maxPosValida + ".");
            }
            ok = false;
            return;
        }
        
        int indiceJava = pos - 1;
        
        Wheel nuevaRueda = new Wheel(indiceJava);
        asignarSimboloAleatorio(nuevaRueda); 
        wheels.add(indiceJava, nuevaRueda);
        
        actualizarRuedas();
        isJackpot(); //Al agregar una rueda comprueba si es estado ganador 
        ok = true;
    }
    
    /**
     * Elimina una rueda de la máquina validando estrictamente los casos frontera.
     * @param pos La posición de la rueda que se desea eliminar (iniciando en 1).
     */
    public void delWheel(int pos) {
        if (wheels.isEmpty()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: No hay ruedas para eliminar.");
            }
            ok = false;
            return;
        }
        
        int maxPosValida = wheels.size();
        
        if (pos < 1 || pos > maxPosValida) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, 
                    "Error: No existe la rueda en la posición " + pos + ".\n" +
                    "Actualmente solo puedes eliminar posiciones del 1 al " + maxPosValida + ".");
            }
            ok = false;
            return; 
        }
        
        int indiceJava = pos - 1;
        
        Wheel ruedaAEliminar = wheels.get(indiceJava);
        ruedaAEliminar.makeInvisible();
        wheels.remove(indiceJava);
        
        actualizarRuedas();
        isJackpot(); // Al elimianr una rueda comprueba si es estado eprdedor
        ok = true;
    }
    
    /**
     * Verifica si un símbolo con el color indicado ya existe en la máquina.
     * @param color El color a buscar.
     * @return true si el color ya existe, false en caso contrario.
     */
    private boolean existeColor(String color) {
        for (Symbol s : symbols) {
            // Usamos equalsIgnoreCase como recomendación de IA para que no reconozca matusculas y minusculas
            if (s.getColor().equalsIgnoreCase(color)) {
                return true; 
            }
        }
        return false;
    }
    
    /**
     * Adiciona un nuevo símbolo a la máquina si su color no está repetido.
     * @param color El color del nuevo símbolo.
     * @param forma La figura ("circle", "triangle", "rectangle").
     */
    public void addSymbol(String color, String forma) {
        if (existeColor(color)) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya existe un símbolo de color " + color + " y forma de " + forma);
            }
            return;
        }
        
        Symbol nuevoSimbolo = new Symbol(color, forma);
        symbols.add(nuevoSimbolo);
    }

    /**
     * Elimina un símbolo de la máquina buscando por su color.
     * @param color El color del símbolo a eliminar.
     */
    public void delSymbol(String color,  String forma) {
        Symbol simboloAEliminar = null;
        
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(color)) {
                simboloAEliminar = s;
                break;
            }
        }
        
        if (simboloAEliminar == null) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: No se encontró el símbolo de color " + color + " con forma de " + forma);
            }
        } else {
            simboloAEliminar.makeInvisible();
            symbols.remove(simboloAEliminar);
        }
    }
    
    /**
     * Asigna un símbolo aleatorio de la lista de símbolos disponibles a una rueda.
     * Si no hay símbolos en la lista, la rueda se queda vacía por el momento.
     * @param rueda La rueda a la que se le asignará el símbolo.
     */
    private void asignarSimboloAleatorio(Wheel rueda) {
        if (!symbols.isEmpty()) {
            int indiceAleatorio = (int) (Math.random() * symbols.size());
            Symbol simboloElegido = symbols.get(indiceAleatorio);
            
            rueda.setSymbol(simboloElegido.getColor(), simboloElegido.getForma());
        }
    }
    
    /**
     * Gira todas las ruedas de la máquina tragamonedas asignando símbolos aleatorios.
     */
    public void spin() {
        if (wheels.isEmpty() || symbols.isEmpty()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Faltan ruedas o símbolos para poder girar.");
            }
            return;
        }
        
        if (distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false;
            return;
        }
        
        palanca.tirar();
        Canvas.getCanvas().wait(200);
        
        for (int i = 0; i < wheels.size(); i++) {
            Wheel ruedaActual = wheels.get(i);
        
            int indiceAleatorio = (int) (Math.random() * symbols.size());
            Symbol simboloElegido = symbols.get(indiceAleatorio);
            
            ruedaActual.girar(simboloElegido.getColor(), simboloElegido.getForma());
        }
        
        Canvas.getCanvas().wait(200);
        palanca.soltar();
        
        isJackpot();
        
        ok = true;
    }
    
    /**
     * Retorna un arreglo con los colores de los símbolos visibles actualmente 
     * en todas las ruedas de la máquina, ordenados de izquierda a derecha.
     * @return Arreglo de Strings con los colores.
     */
    public String[] configuration() {
        String[] config = new String[wheels.size()];
        
        for (int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).getColorActual();
        }
        
        return config;
    }
    
    /**
     * Consulta cuántos símbolos (colores) distintos se están mostrando 
     * actualmente en las ruedas visibles de la máquina.
     * @return El número entero de símbolos diferentes.
     */
    public int distinctSymbols() {
        String[] coloresVisibles = configuration();
        
        ArrayList<String> coloresUnicos = new ArrayList<String>();
        
        for (String color : coloresVisibles) {
            if (!color.isEmpty() && !coloresUnicos.contains(color)) {
                coloresUnicos.add(color);
            }
        }
        
        return coloresUnicos.size();
    }
    
    /**
     * Retorna los colores de los símbolos en el orden que están en la rueda.
     * @return Arreglo de Strings con el catálogo de colores.
     */
    public String[] symbols() {
        String[] catalogo = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++) {
            catalogo[i] = symbols.get(i).getColor();
        }
        return catalogo;
    }
    
    /**
     * Consulta si la configuración actual de las ruedas es ganadora.
     * Se gana el Jackpot únicamente si todas las ruedas muestran el mismo símbolo.
     * @return true si la configuración es ganadora, false en caso contrario.
     */
    public boolean isJackpot() {
        if (wheels.size() <= 1) {
            parteTrasera.changeColor("lightgray"); 
            pintarCasillasRuedas("white"); 
            actualizarRuedas(); 
            return false;
        }

        boolean ganaste = (distinctSymbols() == 1);

        if (ganaste) {
            parteTrasera.changeColor("gold");
            
            actualizarRuedas(); 
            
            JOptionPane.showMessageDialog(null, "¡FELICIDADES HAS GANADO!");
        } else {
            parteTrasera.changeColor("lightgray"); 
            pintarCasillasRuedas("white"); 
            
            actualizarRuedas(); 
        }

        return ganaste;
    }
    
    /**
     * Indica si se logró realizar la última operación solicitada.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean ok() {
        return ok;
    }

    /**
     * Termina la ejecución del simulador cerrando la ventana y el programa.
     */
    public void exit() {
        System.exit(0);
    }
    
    /**
     * Asigna un símbolo específico a una rueda específica.
     * @param wheel La posición de la rueda (iniciando en 1).
     * @param symbol El color del símbolo a asignar.
     */
    public void placeSymbol(int wheel, String symbol) {
        if (wheels.isEmpty() || symbols.isEmpty()) {
            ok = false;
            return;
        }
        
        if (distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false;
            return;
        }

        int index = wheel - 1;
        if (index < 0) {
            index = 0;
        } else if (index >= wheels.size()) {
            index = wheels.size() - 1;
        }

        String formaEncontrada = "";
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(symbol)) {
                formaEncontrada = s.getForma();
                break;
            }
        }

        if (!formaEncontrada.isEmpty()) {
            wheels.get(index).setSymbol(symbol, formaEncontrada);
            ok = true;
        } else {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: El símbolo " + symbol + " no existe.");
            }
            ok = false;
        }
        
        isJackpot();
    }
    
    /**
     * Gira una única rueda específica asignándole un símbolo aleatorio.
     * @param wheel La posición de la rueda (iniciando en 1).
     */
    public void spin(int wheel) {
        if (wheels.isEmpty() || symbols.isEmpty()) {
            ok = false;
            return;
        }
        
        if (distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false;
            return;
        }

        int index = wheel - 1;
        if (index < 0) {
            index = 0;
        } else if (index >= wheels.size()) {
            index = wheels.size() - 1;
        }

        int indiceAleatorio = (int) (Math.random() * symbols.size());
        Symbol simboloElegido = symbols.get(indiceAleatorio);

        palanca.tirar();
        Canvas.getCanvas().wait(200);
        
        wheels.get(index).girar(simboloElegido.getColor(), simboloElegido.getForma());
        
        Canvas.getCanvas().wait(200);
        palanca.soltar();
        
        isJackpot();
        ok = true;
    }
}