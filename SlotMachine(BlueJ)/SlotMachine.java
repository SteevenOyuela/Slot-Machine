import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Random;
import java.util.*;

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

        addSymbol(1, "maroon");
        addSymbol(2, "green");
        addSymbol(3, "purple");
        addSymbol(4, "yellow");
        addSymbol(6, "turquoise");
        
        symbols = new ArrayList<Symbol>();
        addSymbol("ephemeral", "blue");
        addSymbol("ephemeral", "purple");
        
        addSymbol("shy", "green");
        addSymbol("shy", "brown");
        
        wheels = new ArrayList<Wheel>();
        
        actualizarRuedas();
        
        makeInvisible();
    }
    
    /**
     * Crea una máquina tragamonedas de n ruedas inicializada aleatoriamente.
     * @param n Cantidad de ruedas y símbolos a generar (3 <= n <= 50).
     */

    public SlotMachine(int n) {
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

        wheels = new ArrayList<Wheel>();

        List<String> disponibles = new ArrayList<>();

        disponibles.add("blue");
        disponibles.add("yellow");
        disponibles.add("orange");
        disponibles.add("green");
        disponibles.add("grey");
        disponibles.add("pink");
        disponibles.add("purple");
        disponibles.add("maroon");
        disponibles.add("silver");
        disponibles.add("turquoise");
        disponibles.add("salmon");
        disponibles.add("darkgreen");
        disponibles.add("lavender");
        
        Collections.shuffle(disponibles); // Mezclamos los colores al azar IA IMPLEMENTATIVA

        for (int i = 0; i < n; i++) {
            String colorUnico = disponibles.get(i % disponibles.size()); // Usamos (i % disponibles.size()) por si 'n' es mayor a 13 IA IMPLEMENTATIVA
            String formaAsignada = "triangle"; 
            addSymbol(i, colorUnico);
            addWheel(i+1);
        }
        
        makeVisible();

    }

    /**
     * Crea una máquina tragamonedas de n ruedas, n combinaciones de tipo de simbolos,
     * inicializada aleatoriamente.
     * @param n Cantidad de ruedas y símbolos a generar (3 <= n <= 50).
     */
    public SlotMachine(int n, String m) {
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
        
        wheels = new ArrayList<Wheel>();
        
        List<String> disponibles = new ArrayList<>();
        disponibles.add("blue");
        disponibles.add("yellow");
        disponibles.add("orange");
        disponibles.add("green");
        disponibles.add("grey");
        disponibles.add("pink");
        disponibles.add("purple");
        disponibles.add("maroon");
        disponibles.add("silver");
        disponibles.add("turquoise");
        disponibles.add("salmon");
        disponibles.add("darkgreen");
        disponibles.add("lavender");
        
        Collections.shuffle(disponibles);
        
        String[] tiposDisponibles = {"normal", "ephemeral", "shy"};
        
        // IA IMPLEMENTATIVA
        
         for (int i = 0; i < n; i++) {

            String colorUnico = disponibles.get(i % disponibles.size()); // Usamos (i % disponibles.size()) por si 'n' es mayor a 13 IA IMPLEMENTATIVA

            String tipoElegido = tiposDisponibles[(int)(Math.random() * tiposDisponibles.length)];
            
            String formaAsignada = "triangle"; 

            addSymbol(tipoElegido, colorUnico);

            addWheel(i+1);

        }
        
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
        
        if (indiceJava < wheels.size()) {
            Wheel ruedaEnPosicion = wheels.get(indiceJava);
            
            if (ruedaEnPosicion.isLocked()) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, 
                        "Error: La rueda en la posición " + pos + " está bloqueada.\n" +
                        "No puedes agregar una rueda aquí porque desplazaría a la que está congelada.");
                }
                ok = false;
                return;
            }
        }
        
        Wheel nuevaRueda = new Wheel(indiceJava);
        asignarSimboloAleatorio(nuevaRueda); 
        wheels.add(indiceJava, nuevaRueda);
        
        actualizarRuedas();
        isJackpot();
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
        
        if (ruedaAEliminar instanceof RebelWheel) {
            if(isVisible) {
                JOptionPane.showMessageDialog(null, "Error: La rueda rebelde no se puede eliminar.");
            }
            ok = false;
            return;
        }
        
        if (indiceJava < wheels.size()) {
            Wheel ruedaEnPosicion = wheels.get(indiceJava);
            
            if (ruedaEnPosicion.isLocked()) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, 
                        "Error: La rueda en la posición " + pos + " está bloqueada.\n" +
                        "No puedes eliminar una rueda aquí");
                }
                ok = false;
                return;
            }
        }
        
        ruedaAEliminar.makeInvisible();
        wheels.remove(indiceJava);
        
        actualizarRuedas();
        isJackpot();
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
     * Agrega un nuevo símbolo a la máquina tragamonedas en la posición indicada.
     * El símbolo solo se agrega si no existe previamente.
     * Si la posición es mayor que el número de símbolos, se agrega al final.
     * Si la posición es menor o igual a uno, se agrega al principio.
     *
     * @param pos la posición deseada para el nuevo símbolo
     * @param color el color del símbolo que se desea agregar. Los colores
     * disponibles son "red", "black", "blue", "yellow", "green", "white",
     * "orange" y "cyan".
     */
    public void addSymbol(int pos, String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(color)) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, "Error: Ya existe un símbolo de color " + color);
                }
                ok = false;
                return;
            }
        }
        
        int indiceJava;
        if (pos <= 1) {
            indiceJava = 0;
        } else if (pos > symbols.size()) {
            indiceJava = symbols.size();
        } else {
            indiceJava = pos - 1;
        }
        
        Symbol nuevoSimbolo = new Symbol(color, "triangle"); 
        
        symbols.add(indiceJava, nuevoSimbolo);
        ok = true;
    }
    
    /**
     * Adiciona un símbolo polimórfico al catálogo de la máquina.
     * @param tipo "normal", "ephemeral" o "shy".
     * @param color El color del símbolo.
     * @param forma La figura ("circle", "triangle", "rectangle").
     */
    public void addSymbol(String tipo, String color) {
        if (existeColor(color)) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya existe un símbolo de color " + color);
            }
            ok = false;
            return;
        }

        Symbol nuevoSimbolo = null;

        if (tipo.equalsIgnoreCase("normal")) {
            nuevoSimbolo = new Symbol(color, "triangle");
        } else if (tipo.equalsIgnoreCase("ephemeral")) {
            nuevoSimbolo = new EphemeralSymbol(color, "triangle");
        } else if (tipo.equalsIgnoreCase("shy")) {
            nuevoSimbolo = new ShySymbol(color, "triangle");
        } else {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Tipo de símbolo desconocido (" + tipo + ")");
            }
            ok = false;
            return;
        }

        symbols.add(nuevoSimbolo);
        ok = true;
    }

    /**
     * Elimina un símbolo de la máquina tragamonedas.
     * Si el símbolo indicado no existe, la operación no se realiza
     * y se muestra un mensaje de error.
     *
     * @param color el color del símbolo que se desea eliminar
     */
    public void delSymbol(String color) {
        Symbol simboloAEliminar = null;
        
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(color)) {
                simboloAEliminar = s;
                break;
            }
        }
        
        if (simboloAEliminar == null) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: No se encontró un símbolo de color " + color + " para eliminar.");
            }
            ok = false;
            return;
        } 
        
        simboloAEliminar.makeInvisible();
        symbols.remove(simboloAEliminar);
        ok = true;
    }
    
    /**
     * Asigna un símbolo aleatorio de la lista de símbolos disponibles a una rueda.
     * Si no hay símbolos en la lista, la rueda se queda vacía por el momento.
     * @param rueda La rueda a la que se le asignará el símbolo.
     */
    private void asignarSimboloAleatorio(Wheel rueda) {
        if (!symbols.isEmpty()) {
            int indiceAleatorio = (int) (Math.random() * symbols.size());
            Symbol simboloDeCatalogo = symbols.get(indiceAleatorio);
            
            simboloDeCatalogo.aplicarEfecto();
            
            rueda.setSymbol(simboloDeCatalogo.generarCopia());
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
        
        if (wheels.size() > 1 && distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false;
            return;
        }
        
        if (isVisible) {
            palanca.tirar();
            Canvas.getCanvas().wait(200);
        }
        
        for (int i = 0; i < wheels.size(); i++) {
            Wheel ruedaActual = wheels.get(i);
            
            if (!ruedaActual.isLocked()) {
                int indiceAleatorio = (int) (Math.random() * symbols.size());
                Symbol simboloElegido = symbols.get(indiceAleatorio);
                
                simboloElegido.aplicarEfecto();
                Symbol copiaSimbolo = simboloElegido.generarCopia();
                
                ruedaActual.girar(copiaSimbolo);
            }
        }
        
        if (isVisible) {
            Canvas.getCanvas().wait(200);
            palanca.soltar();
        }
        
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
            
            for (Wheel ruedaActual : wheels) {
                ruedaActual.unlock(); 
            }
            
            actualizarRuedas();
            
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "¡FELICIDADES HAS GANADO!");
            }
            
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
        
        Wheel ruedaActual = wheels.get(index);
        
        if (ruedaActual.isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: La rueda " + (index + 1) + " está bloqueada y no puede girar para hacer la respectiva accion de cambio.");
            }
            ok = false;
            return;
        }

        Symbol simboloEncontrado = null;
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(symbol)) {
                simboloEncontrado = s;
                break;
            }
        }

        if (simboloEncontrado != null) {
            wheels.get(index).setSymbol(simboloEncontrado.generarCopia());
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
        
        Wheel ruedaActual = wheels.get(index);
        
        if (ruedaActual.isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: La rueda " + (index + 1) + " está bloqueada y no puede girar.");
            }
            ok = false;
            return;
        }

        int indiceAleatorio = (int) (Math.random() * symbols.size());
        Symbol simboloElegido = symbols.get(indiceAleatorio);
        
        simboloElegido.aplicarEfecto();
        Symbol copiaSimbolo = simboloElegido.generarCopia();

        if (isVisible) {
            palanca.tirar();
            Canvas.getCanvas().wait(200);
        }
        
        wheels.get(index).girar(copiaSimbolo);
        
        if (isVisible) {
            Canvas.getCanvas().wait(200);
            palanca.soltar();
        }
        
        isJackpot();
        ok = true;
    }
    
    /**
     * Intercambia la posición de dos ruedas dentro de la máquina tragamonedas.
     * @param wheel1 La posición de la primera rueda a intercambiar (iniciando en 1).
     * @param wheel2 La posición de la segunda rueda a intercambiar (iniciando en 1).
     */
    public void swap(int wheel1, int wheel2) {
        if (wheels.isEmpty() || wheels.size() < 2) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: No hay suficientes ruedas para intercambiar.");
            }
            ok = false;
            return;
        }
        
        int maxPosValida = wheels.size();
        
        if (wheel1 < 1 || wheel1 > maxPosValida || wheel2 < 1 || wheel2 > maxPosValida) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, 
                    "Error: No se pueden intercambiar las posiciones " + wheel1 + " y " + wheel2 + ".\n" +
                    "Actualmente solo puedes usar posiciones del 1 al " + maxPosValida + ".");
            }
            ok = false;
            return;
        }
        
        int indice1 = wheel1 - 1;
        int indice2 = wheel2 - 1;
        
        Wheel ruedaActual1 = wheels.get(indice1);
        Wheel ruedaActual2 = wheels.get(indice2);
        
        if (ruedaActual1.isLocked() || ruedaActual2.isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Una de las ruedas está bloqueada y no se puede realizar el cambio, \n");
            }
            ok = false;
            return;
        }
        
        if (ruedaActual1 instanceof LeftyWheel || ruedaActual2 instanceof LeftyWheel) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Una de las ruedas es Lefty, no se pueden intercambiar.");
            }
            ok = false;
            return;
        }
        
        if (ruedaActual1 instanceof RebelWheel || ruedaActual2 instanceof RebelWheel) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Una de las ruedas es rebelde, no se pueden intercambiar.");
            }
            ok = false;
            return;
        }

        if (indice1 != indice2) {
            Wheel ruedaTemporal = wheels.get(indice1);
            wheels.set(indice1, wheels.get(indice2));
            wheels.set(indice2, ruedaTemporal);

            actualizarRuedas();
            isJackpot();
        }
        ok = true;
    }
    
    /**
     * Bloquea una rueda específica para que no gire.
     * @param wheel La posición de la rueda (iniciando en 1).
     */
    public void lock(int wheel) {
        if (wheels.isEmpty()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: No hay ruedas para bloquear.");
            }
            ok = false;
            return;
        }
        
        int maxPosValida = wheels.size();
        if (wheel < 1 || wheel > maxPosValida) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, 
                    "Error: No se puede bloquear la posición " + wheel + ".\n" +
                    "Rango válido actual: 1 a " + maxPosValida + ".");
            }
            ok = false;
            return;
        }
        
        Wheel ruedaObjetivo = wheels.get(wheel - 1);
        
        if (ruedaObjetivo instanceof RebelWheel) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: La rueda rebelde no se puede bloquear.");
            }
            ok = false;
            return;
        }

        if (ruedaObjetivo.isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: La rueda en la posición " + wheel + " ya se encuentra bloqueada.");
            }
            ok = false;
            return;
        }

        ruedaObjetivo.lock();
        ok = true;
    }

    /**
     * Desbloquea una rueda específica para que vuelva a girar.
     * @param wheel La posición de la rueda (iniciando en 1).
     */
    public void unlock(int wheel) {
        if (wheels.isEmpty()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: No hay ruedas para desbloquear.");
            }
            ok = false;
            return;
        }
        
        int maxPosValida = wheels.size();
        if (wheel < 1 || wheel > maxPosValida) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, 
                    "Error: No se puede desbloquear la posición " + wheel + ".\n" +
                    "Rango válido actual: 1 a " + maxPosValida + ".");
            }
            ok = false;
            return;
        }
        
        Wheel ruedaObjetivo = wheels.get(wheel - 1);
        
        if (!ruedaObjetivo.isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: La rueda en la posición " + wheel + " ya se encuentra libre.");
            }
            ok = false;
            return;
        }

        ruedaObjetivo.unlock();
        ok = true;
    }
    
    /**
     * Gira una rueda específica avanzando una cantidad determinada de pasos.
     * @param wheel La posición de la rueda (iniciando en 1).
     * @param steps La cantidad de posiciones a avanzar en el catálogo de símbolos.
     */
    public void spin(int wheel, int steps) {
        if (wheels.isEmpty() || symbols.isEmpty()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Faltan ruedas o símbolos para poder girar.");
            }
            ok = false;
            return;
        }
        
        if (wheels.size() > 1 && distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false;
            return;
        }
        
        int maxPosValida = wheels.size();
        
        if (wheel < 1 || wheel > maxPosValida) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, 
                    "Error: No se puede girar la rueda en la posición " + wheel + ".\n" +
                    "Actualmente solo puedes usar posiciones del 1 al " + maxPosValida + ".");
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
        
        Wheel ruedaActual = wheels.get(index);
        
        if (ruedaActual.isLocked()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: La rueda " + (index + 1) + " está bloqueada y no puede girar.");
            }
            ok = false;
            return;
        }

        int indiceCatalogo = 0;
        String colorActual = ruedaActual.getColorActual();
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equalsIgnoreCase(colorActual)) {
                indiceCatalogo = i;
                break;
            }
        }
        
        if (isVisible) {
            palanca.tirar();
            Canvas.getCanvas().wait(200);
        }
        
        for (int i = 0; i < steps; i++) {
            indiceCatalogo = (indiceCatalogo + 1) % symbols.size();
            Symbol siguienteSimbolo = symbols.get(indiceCatalogo);
            
            siguienteSimbolo.aplicarEfecto();
            Symbol copiaSimbolo = siguienteSimbolo.generarCopia();
            
            if (isVisible) {
                Canvas.getCanvas().wait(400);
                ruedaActual.girar(copiaSimbolo);
                Canvas.getCanvas().wait(400);
            } else {
                ruedaActual.setSymbol(copiaSimbolo);
            }
        }
        
        if (isVisible) {
            Canvas.getCanvas().wait(200);
            palanca.soltar();
        }
        
        isJackpot();
        ok = true;
    }
    
    /**
     * Deja la máquina tragamonedas en una configuración exacta dada por el usuario.
     * @param setSymbols Arreglo de Strings con los colores deseados para cada rueda, de izquierda a derecha.
     */
    public void spin(String[] setSymbols) {
        if (wheels.isEmpty() || symbols.isEmpty() || setSymbols == null) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Faltan ruedas, símbolos, o el arreglo ingresado es nulo.");
            }
            ok = false;
            return;
        }

        if (setSymbols.length != wheels.size()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, 
                    "Error: La cantidad de símbolos dados (" + setSymbols.length + 
                    ") no coincide con el número de ruedas (" + wheels.size() + ").");
            }
            ok = false;
            return;
        }

        if (wheels.size() > 1 && distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false;
            return;
        }
        
        for (int i = 0; i < wheels.size(); i++) {
            if (wheels.get(i).isLocked()) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, "Error: La rueda " + (i + 1) + " está bloqueada. No se puede aplicar la configuración.");
                }
                ok = false;
                return;
            }
        }

        Symbol[] simbolosAAsignar = new Symbol[setSymbols.length];
        
        for (int i = 0; i < setSymbols.length; i++) {
            String colorBuscado = setSymbols[i];
            Symbol simboloEncontrado = null;
            
            for (Symbol s : symbols) {
                if (s.getColor().equalsIgnoreCase(colorBuscado)) {
                    simboloEncontrado = s;
                    break;
                }
            }
            
            if (simboloEncontrado == null) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, "Error: El símbolo de color '" + colorBuscado + "' no existe en el catálogo.");
                }
                ok = false;
                return; 
            }
            simbolosAAsignar[i] = simboloEncontrado;
        }

        if (isVisible) {
            palanca.tirar();
            Canvas.getCanvas().wait(200);
        }

        for (int i = 0; i < wheels.size(); i++) {
            Wheel ruedaActual = wheels.get(i);
            
            if (!ruedaActual.isLocked()) {
                Symbol simboloBase = simbolosAAsignar[i];
                simboloBase.aplicarEfecto();
                Symbol copiaSimbolo = simboloBase.generarCopia();
                
                if (isVisible) {
                    ruedaActual.girar(copiaSimbolo);
                } else {
                    ruedaActual.setSymbol(copiaSimbolo);
                }
            }
        }

        isJackpot();
        ok = true;
    }
    
    /**
     * Adiciona una nueva rueda tipo Lefty a la máquina, validando estrictamente los casos frontera.
     * @param pos La posición donde se desea insertar la rueda (iniciando en 1).
     */
    public void addLeftyWheel(int pos) {
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
        
        int index = pos - 1;
        if (index >= 0 && index <= wheels.size()) {
            Wheel ruedaEnPosicion = wheels.get(index);
            
            if (ruedaEnPosicion.isLocked()) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, 
                        "Error: La rueda en la posición " + pos + " está bloqueada.\n" +
                        "No puedes agregar una rueda aquí porque está en estado de bloqueo.");
                }
                ok = false; 
                return;
            }
            
            if (ruedaEnPosicion instanceof LeftyWheel) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, 
                        "Error: Ya se encuentra una rueda en estado Lefty en la posición " + pos + ".\n" +
                        "Por lo tanto, no se puede insertar otra rueda aquí y desplazarla.");
                }
                ok = false;
                return;
            }
        }
        
        LeftyWheel nuevaLefty = new LeftyWheel(index, wheels);
        asignarSimboloAleatorio(nuevaLefty);  
        wheels.add(index, nuevaLefty);
            
        actualizarRuedas();
        isJackpot();
        ok = true;
    }
    
    /**
     * Adiciona una nueva rueda tipo Rebel a la máquina, alc ual tiene como función principal no se deja bloquear, ni intercambiar, ni eliminar.
     * @param pos La posición donde se desea insertar la rueda (iniciando en 1).
     */
    public void addRebelWheel(int pos) {
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
        
        int index = pos - 1;
        if (index >= 0 && index <= wheels.size()) {
            RebelWheel nuevaRebel = new RebelWheel(index);
            
            asignarSimboloAleatorio(nuevaRebel);

            wheels.add(index, nuevaRebel);
            actualizarRuedas();
            
            ok = true;
        } else {
            ok = false;
        }
    }
    
    /**
     * Adiciona una nueva rueda tipo Lazy a la máquina, validando estrictamente los casos frontera.
     * @param pos La posición donde se desea insertar la rueda (iniciando en 1).
     */
    public void addLazyWheel(int pos) {
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
        
        if (indiceJava < wheels.size()) {
            Wheel ruedaEnPosicion = wheels.get(indiceJava);
            
            if (ruedaEnPosicion.isLocked()) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, 
                        "Error: La rueda en la posición " + pos + " está bloqueada.\n" +
                        "No puedes agregar una rueda aquí porque desplazaría a la que está congelada.");
                }
                ok = false; 
                return;
            }
            
            if (ruedaEnPosicion instanceof LazyWheel) {
                if (isVisible) {
                    JOptionPane.showMessageDialog(null, 
                        "Error: Ya se encuentra una rueda Lazy en la posición " + pos + ".\n" +
                        "Por lo tanto, no se puede insertar otra rueda aquí y desplazarla.");
                }
                ok = false;
                return;
            }
        }
        
        LazyWheel nuevaRueda = new LazyWheel(indiceJava);
        asignarSimboloAleatorio(nuevaRueda); 
        wheels.add(indiceJava, nuevaRueda);
        
        actualizarRuedas();
        isJackpot();
        ok = true;
    }
}