import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Resuelve el Problem (Slot Machine) del ICPC World Finals 2025
 * usando SlotMachine unicamente como testing tool.
 *
 * De la maquina solo se usan SlotMachine(n), spin(wheel, steps) y
 * distinctSymbols() para resolver, y makeVisible() para simular. Usando
 * como pista para la solucion la cantidad de simbolos distintos que
 * se ven en este momento.
 */
public class SlotMachineContest {   
    /**
     * Motor de solución algorítmica.
     * @param n Número de ruedas y símbolos (3 <= n <= 50).
     * @return Matriz de enteros donde cada fila representa una acción {rueda, pasos}.
     */
    public int[][] solve(int n){
        if (n < 3 || n > 50) {
            System.err.println("Error en solve: El número de ruedas (" + n + ") debe estar entre 3 y 50.");
            return new int[0][0];
        }
        
        SlotMachine maquina = new SlotMachine(n);
        maquina.makeInvisible();
        
        return resolver(maquina, n);
    }
    
    /**
     * Simulación visual de la solución.
     * @param n Número de ruedas y símbolos (3 <= n <= 50).
     */
    public void simulate(int n){
        if (n < 3 || n > 50) {
            JOptionPane.showMessageDialog(
                null,
                "Error en simulate: Cantidad de ruedas inválida (" + n + ").\n" +
                "El número de ruedas permitidas debe estar entre 3 y 50.",
                "Parámetro Inválido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        SlotMachine maquina = new SlotMachine(n);
        maquina.makeVisible();
        
        int[][] acciones = resolver(maquina, n);
        
        maquina.makeInvisible();
        maquina.exit();
    }
    
    /**
     * Ejecuta el algoritmo central de la solución (ICPC Problem I) sobre la máquina dada.
     * El procedimiento consta de tres fases matemáticas estrictas para garantizar el éxito
     * operando a ciegas (usando únicamente distinctSymbols() como sonda):
     *
     * Fase 1: Ajusta cada rueda hasta garantizar que todas muestren símbolos distintos.
     * Fase 2: Usa la rueda 1 como "pivote" o "sonda". La gira paso a paso y, al mismo tiempo,
     *         retrocede las demás para descubrir a qué distancia (offset) se encuentra cada una.
     * Fase 3: Con las distancias relativas ya descubiertas, calcula y aplica los pasos
     *         exactos restantes a cada rueda para que se alineen simultáneamente con la rueda 1.
     *
     * @param maquina la máquina tragamonedas que se va a resolver.
     * @param n el número de ruedas (y de símbolos) de la máquina.
     * @return una matriz de enteros int[][] con la secuencia de acciones {rueda, pasos}.
     */
    private int[][] resolver(SlotMachine maquina, int n){
        // Inicializa una lista dinámica para guardar el historial de movimientos de manera flexible.
        List<int[]> acciones = new ArrayList<int[]>();
        
        // Verifica el caso base: Si hay menos de 2 ruedas, es imposible jugar/alinear nada.
        if (n < 2){
            return new int[0][0];
        }
    
        // =========================================================================
        // FASE 1: Dejar todas las ruedas con símbolos distintos.
        // =========================================================================
        
        // Inicia un bucle desde la segunda rueda (rueda 2) hasta la última (rueda n).
        for (int t = 2; t <= n; t++){
            // Guarda la cantidad actual de símbolos distintos visibles en el tablero.
            int mejorValor = maquina.distinctSymbols();
            // Inicializa una variable para recordar cuántos pasos nos dieron el mejor resultado.
            int mejorPaso = 0;
            
            // Inicia un bucle interno para probar cada posible giro de la rueda actual 't' (hasta 'n' pasos).
            for (int p = 1; p <= n; p++){
                // Gira la rueda actual 't' exactamente 1 paso hacia adelante.
                maquina.spin(t, 1);
                // Registra esta acción en nuestra lista dinámica.
                acciones.add(new int[]{t, 1});
                
                // VALIDACIÓN TEMPRANA: Verifica si con este giro casualmente todas las ruedas ya coincidieron (Jackpot).
                if (maquina.distinctSymbols() == 1){
                    // Si ganó, convierte la lista dinámica a matriz estática y termina el algoritmo.
                    return acciones.toArray(new int[acciones.size()][]);
                }
                
                // Vuelve a consultar cuántos símbolos distintos hay tras el giro.
                int actual = maquina.distinctSymbols();
                // Si la cantidad de símbolos distintos AUMENTÓ (lo cual es nuestro objetivo en esta fase)...
                if (actual > mejorValor){
                    // Actualiza el 'mejorValor' encontrado hasta el momento.
                    mejorValor = actual;
                    // Guarda el número de paso ('p') en el que se encontró esta mejoría.
                    mejorPaso = p;
                }
            }
            // Después de probar los 'n' giros (la rueda dio una vuelta completa y volvió a su posición original)...
            // Si encontró una posición que mejoraba la cantidad de símbolos distintos ('mejorPaso' ya no es 0)...
            if (mejorPaso != 0){
                // Gira la rueda directamente a esa mejor posición guardada.
                maquina.spin(t, mejorPaso);
                // Registra esta acción final en la lista.
                acciones.add(new int[]{t, mejorPaso});
                
                // VALIDACIÓN TEMPRANA: Vuelve a comprobar si este ajuste final desencadenó el Jackpot.
                if (maquina.distinctSymbols() == 1){
                    // Si ganó, retorna la matriz de acciones y termina.
                    return acciones.toArray(new int[acciones.size()][]);
                }
            }
        }
        
        // =========================================================================
        // FASE 2: Descubrir la permutación usando la Rueda 1 como "Sonda".
        // =========================================================================
        // Crea un arreglo para guardar la distancia (offset) descubierta para cada rueda. El tamaño es n+1 para usar índices 1-based.
        int[] posicion = new int[n + 1];
        // Crea un arreglo booleano para marcar qué ruedas ya han sido "descubiertas" y no volver a evaluarlas.
        boolean[] identificada = new boolean[n + 1];
        // Marca la Rueda 1 como identificada por defecto, ya que es nuestra referencia principal.
        identificada[1] = true;
        // Inicializa la variable que rastreará cuántos pasos se ha movido nuestra rueda sonda (Rueda 1).
        int offsetRueda1 = 0;
        
        // Inicia el bucle principal de la fase 2. Se ejecutará n-1 veces (un ciclo completo de la rueda sonda).
        for (int s = 1; s <= n - 1; s++){
            // Gira la rueda sonda (Rueda 1) un paso hacia adelante. Esto "romperá" intencionalmente el estado de 'n' símbolos distintos (ahora habrá n-1).
            maquina.spin(1, 1);
            // Registra el giro de la rueda 1.
            acciones.add(new int[]{1, 1});
            
            // VALIDACIÓN TEMPRANA: Verifica si este giro de la rueda 1 logró el Jackpot.
            if (maquina.distinctSymbols() == 1){
                // Si ganó, retorna y termina.
                return acciones.toArray(new int[acciones.size()][]);
            }
            
            // Actualiza el rastreador de pasos dados por la rueda 1 con la iteración actual 's'.
            offsetRueda1 = s;
            // Guarda esta posición en el arreglo de distancias relativas para la rueda 1.
            posicion[1] = s;
            
            // Inicia un bucle interno para interrogar al resto de las ruedas (desde la 2 hasta la n).
            for (int w = 2; w <= n; w++){
                // Si esta rueda 'w' ya fue descubierta en un ciclo anterior, se salta la iteración y pasa a la siguiente rueda.
                if (identificada[w]){
                    continue; 
                }
                
                // INTERROGATORIO: Gira la rueda 'w' exactamente n - 1 pasos (esto matemáticamente equivale a dar 1 paso hacia atrás).
                maquina.spin(w, n - 1);
                // Registra el movimiento hacia atrás.
                acciones.add(new int[]{w, n - 1});
                
                // VALIDACIÓN TEMPRANA: Verifica si este movimiento hacia atrás logró el Jackpot.
                if (maquina.distinctSymbols() == 1){
                    // Si ganó, retorna y termina.
                    return acciones.toArray(new int[acciones.size()][]);
                }
                
                // LA PRUEBA FINAL: Si al retroceder esta rueda 'w' se "restauró" el estado máximo de n símbolos distintos...
                // Significa que esta era la rueda que tenía el símbolo que la Rueda 1 acababa de "pisar". ¡La hemos descubierto!
                if (maquina.distinctSymbols() == n){
                    // Guardamos la distancia relativa de esta rueda 'w' basándonos en el paso actual 's'.
                    posicion[w] = s - 1;
                    // Marcamos la rueda 'w' como completamente identificada para no volver a iterarla.
                    identificada[w] = true;
                    // Rompemos el ciclo 'for' interno (break) porque ya encontramos la rueda correspondiente a este ciclo 's', y pasamos al siguiente giro de la Rueda 1.
                    break;
                }
                
                // Si no se cumplió la condición anterior (no era la rueda correcta), deshacemos el interrogatorio girando 1 paso hacia adelante (restaurando su posición original).
                maquina.spin(w, 1);
                // Registramos la acción de restauración.
                acciones.add(new int[]{w, 1});
                
                // VALIDACIÓN TEMPRANA: Verifica si la restauración causó un Jackpot.
                if (maquina.distinctSymbols() == 1){
                    // Si ganó, retorna y termina.
                    return acciones.toArray(new int[acciones.size()][]);
                }
            }
        }
    
        // =========================================================================
        // FASE 3: Alinear todas las ruedas al símbolo de la Rueda 1.
        // =========================================================================
        // Ahora que conocemos la "distancia" de cada rueda, iteramos sobre todas (de la 2 a la n).
        for (int w = 2; w <= n; w++){
            // CÁLCULO MATEMÁTICO FINAL: Usamos aritmética modular para calcular cuántos pasos EXACTOS le faltan a la rueda 'w' 
            // para alcanzar la posición final donde quedó anclada la Rueda 1 (offsetRueda1).
            int pasos = (((offsetRueda1 - posicion[w]) % n) + n) % n;
            
            // Si la cantidad de pasos a dar es diferente de 0 (es decir, la rueda aún no está alineada)...
            if (pasos != 0){
                // Ejecuta el giro final calculando los pasos matemáticos.
                maquina.spin(w, pasos);
                // Registra la acción de alineación final.
                acciones.add(new int[]{w, pasos});
                
                // VALIDACIÓN FINAL: Verifica si, al alinear esta rueda, ya se completó el Jackpot global.
                if (maquina.distinctSymbols() == 1){
                    // Si ganó (lo cual sucederá inevitablemente al alinear la última rueda), retorna la matriz y termina.
                    return acciones.toArray(new int[acciones.size()][]);
                }
            }
        }
        
        // =========================================================================
        // 4. Convertir la lista dinámica a la matriz estática exigida por el UML
        // =========================================================================
        int[][] matrizSolucion = new int[acciones.size()][2];
        for (int i = 0; i < acciones.size(); i++) {
            matrizSolucion[i] = acciones.get(i);
        }

        return matrizSolucion;
    }
}