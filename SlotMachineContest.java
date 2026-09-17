import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada de resolver algorítmicamente el "Problem I" de la maratón ICPC.
 */
public class SlotMachineContest {

    /**
     * Motor de solución algorítmica (Requisito 14).
     * Complejidad: O(n²)
     * 
     * @param n Número de ruedas y símbolos (3 <= n <= 50).
     * @return Matriz de enteros donde cada fila representa una acción {rueda, pasos}.
     */
    public int[][] solve(int n) {
        SlotMachine testingMachine = new SlotMachine(n);
        testingMachine.makeInvisible(); 
        return resolverAlgoritmo(testingMachine, n);
    }
    
    /**
     * Simulación visual de la solución (Requisito 15).
     * Complejidad: O(n²)
     * 
     * @param n Número de ruedas y símbolos (3 <= n <= 50).
     */
    public void simulate(int n) {
        SlotMachine simMachine = new SlotMachine(n);
        simMachine.makeVisible(); 
        resolverAlgoritmo(simMachine, n);
        simMachine.exit();
    }
    
    /**
     * Motor matemático optimizado: calcula la distancia exacta necesaria
     * para alinear cada rueda en una sola operación.
     * 
     * @param maquina La instancia de la máquina a operar.
     * @param n Cantidad de ruedas y símbolos.
     * @return Matriz de acciones ejecutadas.
     */
    private int[][] resolverAlgoritmo(SlotMachine maquina, int n) {
        ArrayList<int[]> registroAcciones = new ArrayList<>();
        
        // 1. Caso base: máquina ya ganadora
        if (maquina.distinctSymbols() == 1) {
            return new int[0][0];
        }
        
        // 2. Definir referencia y catálogo
        String colorObjetivo = maquina.configuration()[0];
        String[] simbolos = maquina.symbols();
        
        // 3. Cachear mapeo color -> índice en catálogo (O(n) una sola vez)
        Map<String, Integer> indiceSimbolos = new HashMap<>();
        for (int i = 0; i < simbolos.length; i++) {
            indiceSimbolos.put(simbolos[i].toLowerCase(), i);
        }
        
        int indiceObjetivo = indiceSimbolos.get(colorObjetivo.toLowerCase());
        
        // 4. Alinear cada rueda con UNA SOLA OPERACIÓN
        //    Calculamos exactamente cuántos pasos necesita cada rueda
        for (int i = 2; i <= n; i++) {
            String colorActual = maquina.configuration()[i - 1];
            
            // Si no coincide, calcular distancia y girar exactamente esa cantidad
            if (!colorActual.equalsIgnoreCase(colorObjetivo)) {
                Integer indiceActualObj = indiceSimbolos.get(colorActual.toLowerCase());
                
                // Aritmética modular: distancia hacia adelante en el catálogo circular
                int pasosPrefijo = (indiceObjetivo - indiceActualObj + n) % n;
                
                // UNA SOLA LLAMADA a spin() en lugar de n iteraciones
                maquina.spin(i, pasosPrefijo);
                registroAcciones.add(new int[]{i, pasosPrefijo});
            }
        }
        
        // 5. Convertir lista dinámica a matriz estática
        int[][] matrizSolucion = new int[registroAcciones.size()][2];
        for (int i = 0; i < registroAcciones.size(); i++) {
            matrizSolucion[i] = registroAcciones.get(i);
        }

        return matrizSolucion;
    }
}