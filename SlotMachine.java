import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Representa una máquina tragamonedas.
 * 
 * La máquina utiliza los componentes gráficos de la librería shapes
 * para construir su representación visual.
 */

public class SlotMachine {
    private Canvas canvas; // Creacion del tablero
    private Rectangle parteTrasera; //Fondo de la traga monedas
    private Rectangle bordeTrasero; // Borde de la traga modenas
    
    private Rectangle base; // Base de la traga monedas
    private int posicionXBase; // Memoria apra poder centrarla
    
    private ArrayList<Wheel> wheels; // Almacenar las ruedas
    private ArrayList<Symbol> symbols; // Almacenar los simbolos 
    
    private boolean isVisible;
    
    private Lever palanca; // Efecto visual de al palanca al crear la maquina
    
    private boolean ok; // Memoria de la última operación

    /**
     * Crea una nueva máquina tragamonedas.
     */
    public SlotMachine() {
        canvas = Canvas.getCanvas();
        
        // =========================
        // PARTE TRASERA DE LA MAQUINA TRAGA MONEDAS
        // =========================
    
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
        
        // =========================
        // BASE DE LA MÁQUINA
        // =========================
        base = new Rectangle();
        base.changeSize(25, 150);
        base.changeColor("black"); 
        
        // Centro para el caso inicial de 3 ruedas
        int anchoInicial = 20 + (3 * 120); 
        posicionXBase = 50 + (anchoInicial / 2) - 75;
        
        base.moveHorizontal(posicionXBase);
        base.moveVertical(272);
        base.makeVisible();
        
        // =========================
        // INICIALIZAMOS LA PALANCA
        // ========================= 
        palanca = new Lever();
        
        // =========================
        // INICIALIZACIÓN DE LAS RUEDAS
        // =========================
        wheels = new ArrayList<Wheel>();
        
        // Creamos las 3 ruedas iniciales usando un ciclo
        for (int i = 0; i < 3; i++) {
            Wheel nuevaRueda = new Wheel(i);
            wheels.add(nuevaRueda);
        }
        
        // =========================
        // INICIALIZACION DE LOS SIMBOLOS
        // =========================
        symbols = new ArrayList<Symbol>();

        // Agregamos símbolos de prueba al catálogo predescrito
        addSymbol("maroon", "circle");
        addSymbol("green", "circle");
        addSymbol("blue", "triangle");
        addSymbol("purple", "triangle");
        addSymbol("yellow", "rectangle");
        addSymbol("turquoise", "rectangle");
        
        wheels = new ArrayList<Wheel>();
        
        // Creamos las 3 ruedas iniciales
        for (int i = 0; i < 3; i++) {
            Wheel nuevaRueda = new Wheel(i);
            asignarSimboloAleatorio(nuevaRueda); // ¡Le damos un símbolo al nacer!
            wheels.add(nuevaRueda);
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
        // Cambiamos el tamaño del fondo gris.
        int nuevoAncho = 20 + (wheels.size() * 120);
        bordeTrasero.changeSize(210, nuevoAncho + 10);
        parteTrasera.changeSize(200, nuevoAncho);
        
        // Se redibujan las reudas correspondientes.
        for (int i = 0; i < wheels.size(); i++) {
            Wheel ruedaActual = wheels.get(i);
            ruedaActual.actualizarPosicion(i);
        }
        
        // Centrar la base negra dinámicamente
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
     * Adiciona una nueva rueda a la máquina en la posición indicada.
     * @param pos La posición donde se desea insertar la rueda (iniciando en 1).
     */
    public void addWheel(int pos) {
        // Validar límite inferior
        if (pos < 1) {
            pos = 1;
        }
        
        // Validar límite superior (al agregar, el límite es el tamaño actual + 1)
        if (pos > wheels.size() + 1) {
            pos = wheels.size() + 1;
        }
        
        // Para el usuario es la base 1 pero Java es la base 0 (por eso se resta)
        int indiceJava = pos - 1;
        
        // Crear la nueva rueda, agregarle un simbolo al azar y agregarla a la lista en la posición exacta
        Wheel nuevaRueda = new Wheel(indiceJava);
        asignarSimboloAleatorio(nuevaRueda);
        wheels.add(indiceJava, nuevaRueda);
        
        // Ajustar visualmente todas las ruedas para hacer espacio
        actualizarRuedas();
        ok = true;
    }
    
    /**
     * Elimina una rueda de la máquina en la posición indicada.
     * @param pos La posición de la rueda que se desea eliminar (iniciando en 1).
     */
    public void delWheel(int pos) {
        // Validar si la máquina ya está vacía
        if (wheels.isEmpty()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: No hay ruedas para eliminar.");
                ok = false;
            }
            return; // Cortamos la ejecución del método aquí
        }
        
        // Validar límite inferior
        if (pos < 1) {
            pos = 1;
        }
        
        // Validar límite superior (al eliminar, el máximo es la cantidad actual de ruedas)
        if (pos > wheels.size()) {
            pos = wheels.size();
        }
        
        // Para el usuario es la base 1 pero Java es la base 0 (por eso se resta)
        int indiceJava = pos - 1;
        
        // Obtener la rueda, borrarla visualmente y eliminarla de la lista
        Wheel ruedaAEliminar = wheels.get(indiceJava);
        ruedaAEliminar.makeInvisible();
        wheels.remove(indiceJava);
        
        // Ajustar visualmente el resto de las ruedas para cerrar el hueco
        actualizarRuedas();
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
        
        // Si pasa la validación, lo creamos y lo guardamos
        Symbol nuevoSimbolo = new Symbol(color, forma);
        symbols.add(nuevoSimbolo);
    }

    /**
     * Elimina un símbolo de la máquina buscando por su color.
     * @param color El color del símbolo a eliminar.
     */
    public void delSymbol(String color,  String forma) {
        Symbol simboloAEliminar = null;
        
        // Buscamos el símbolo en la lista
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
            // Lo borramos del lienzo y de la memoria
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
        // Validamos que existan símbolos en el catálogo
        if (!symbols.isEmpty()) {
            // Generamos un índice aleatorio basado en el tamaño de la lista
            int indiceAleatorio = (int) (Math.random() * symbols.size());
            Symbol simboloElegido = symbols.get(indiceAleatorio);
            
            // Le decimos a la rueda que dibuje este símbolo
            rueda.setSymbol(simboloElegido.getColor(), simboloElegido.getForma());
        }
    }
    
    /**
     * Gira todas las ruedas de la máquina tragamonedas asignando símbolos aleatorios.
     */
    public void spin() {
        // Validamos que existan ruedas y símbolos para poder jugar
        if (wheels.isEmpty() || symbols.isEmpty()) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Faltan ruedas o símbolos para poder girar.");
            }
            return;
        }
        
        // Validar si la máquina ya está en estado ganador (Jackpot)
        if (distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false; // La operación no se logró realizar
            return;
        }
        
        // Baja la palanca para ejercer el cambio de fichas
        palanca.tirar();
        Canvas.getCanvas().wait(200);
        
        // Hacemos girar cada rueda una por una
        for (int i = 0; i < wheels.size(); i++) {
            Wheel ruedaActual = wheels.get(i);
            
            // Elegir un símbolo aleatorio de la lista disponible
            int indiceAleatorio = (int) (Math.random() * symbols.size());
            Symbol simboloElegido = symbols.get(indiceAleatorio);
            
            // Llamamos a nuestro nuevo método con animación
            ruedaActual.girar(simboloElegido.getColor(), simboloElegido.getForma());
        }
        
        // La palanca regresa a su esta inicial
        Canvas.getCanvas().wait(200);
        palanca.soltar();
        
        // Validar el estado del juego justo al terminar de girar
        isJackpot();
        
        ok = true;
    }
    
    /**
     * Retorna un arreglo con los colores de los símbolos visibles actualmente 
     * en todas las ruedas de la máquina, ordenados de izquierda a derecha.
     * @return Arreglo de Strings con los colores.
     */
    public String[] configuration() {
        // Creamos un arreglo del mismo tamaño que nuestra cantidad de ruedas
        String[] config = new String[wheels.size()];
        
        // Recorremos las ruedas para saber el colore
        for (int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).getColorActual();
        }
        
        // El arreglo con el color respectivo
        return config;
    }
    
    /**
     * Consulta cuántos símbolos (colores) distintos se están mostrando 
     * actualmente en las ruedas visibles de la máquina.
     * @return El número entero de símbolos diferentes.
     */
    public int distinctSymbols() {
        // Obtenemos todos los colores visibles
        String[] coloresVisibles = configuration();
        
        // Se crea una lista temporal para guardar los colores sin repetir
        ArrayList<String> coloresUnicos = new ArrayList<String>();
        
        for (String color : coloresVisibles) {
            if (!color.isEmpty() && !coloresUnicos.contains(color)) {
                coloresUnicos.add(color);
            }
        }
        
        // La cantidad de colores final
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
        if (wheels.isEmpty()) {
            return false;
        }

        boolean ganaste = (distinctSymbols() == 1);

        if (ganaste) {
            parteTrasera.changeColor("gold");
            
            // ESTA ES LA CLAVE: Reorganiza las capas visuales correctamente
            actualizarRuedas(); 
            
            JOptionPane.showMessageDialog(null, "¡FELICIDADES HAS GANADO!");
        } else {
            parteTrasera.changeColor("lightgray"); 
            pintarCasillasRuedas("white"); 
            
            // Se mantiene el orden de las capas
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
        
        // Validar si la máquina ya está en estado ganador (Jackpot)
        if (distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false; // La operación no se logró realizar
            return;
        }

        // Ajustamos la posición según las reglas del UML
        int index = wheel - 1;
        if (index < 0) {
            index = 0;
        } else if (index >= wheels.size()) {
            index = wheels.size() - 1;
        }

        // Buscamos la forma geométrica que le corresponde a ese color en nuestro catálogo
        String formaEncontrada = "";
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(symbol)) {
                formaEncontrada = s.getForma();
                break;
            }
        }

        // Si el color existe en el catálogo, lo asignamos a la rueda
        if (!formaEncontrada.isEmpty()) {
            wheels.get(index).setSymbol(symbol, formaEncontrada);
            ok = true;
        } else {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: El símbolo " + symbol + " no existe.");
            }
            ok = false;
        }
        
        // Validar el estado del juego justo al terminar de girar
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
        
        // Validar si la máquina ya está en estado ganador (Jackpot)
        if (distinctSymbols() == 1) {
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "Error: Ya has ganado el Jackpot. No se permiten más movimientos.");
            }
            ok = false; // La operación no se logró realizar
            return;
        }

        // Ajustamos la posición según las reglas del UML
        int index = wheel - 1;
        if (index < 0) {
            index = 0;
        } else if (index >= wheels.size()) {
            index = wheels.size() - 1;
        }

        // Elegimos un símbolo aleatorio
        int indiceAleatorio = (int) (Math.random() * symbols.size());
        Symbol simboloElegido = symbols.get(indiceAleatorio);

        // Bajamos la palanca, giramos la rueda específica y soltamos la palanca
        palanca.tirar();
        Canvas.getCanvas().wait(200);
        
        wheels.get(index).girar(simboloElegido.getColor(), simboloElegido.getForma());
        
        Canvas.getCanvas().wait(200);
        palanca.soltar();
        
        // Verificamos si este giro individual nos dio el Jackpot
        isJackpot();
        ok = true;
    }
}