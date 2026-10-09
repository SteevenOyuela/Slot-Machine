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
     * El procedimiento consta de tres fases matemáticas estrictas para garantizar el éxito
     * operando a ciegas:
     *
     * Fase 1: Ajusta cada rueda hasta garantizar que todas muestren símbolos distintos.
     * Fase 2: Usa la rueda 1 como "sonda". La gira paso a paso y, al mismo tiempo,
     *         retrocede las demás para descubrir a qué distancia (offset) se encuentra cada una.
     * Fase 3: Con las distancias relativas ya descubiertas, calcula y aplica los pasos
     *         exactos restantes a cada rueda para que se alineen simultáneamente con la rueda 1.
     *
     * @param maquina la máquina tragamonedas que se va a resolver.
     * @param n el número de ruedas (y de símbolos) de la máquina.
     * @return una matriz de enteros int[][] con la secuencia de acciones {rueda, pasos}.
     */
    private int[][] resolver(SlotMachine maquina, int n){
        List<int[]> acciones = new ArrayList<int[]>();
        
        if (n < 2){
            return new int[0][0];
        }
    
        // FASE 1: Dejar todas las ruedas con símbolos distintos.
        
        for (int t = 2; t <= n; t++){
            int mejorValor = maquina.distinctSymbols();
            int mejorPaso = 0;
            
            for (int p = 1; p <= n; p++){
                maquina.spin(t, 1);
                acciones.add(new int[]{t, 1});
                
                if (maquina.distinctSymbols() == 1){
                    return acciones.toArray(new int[acciones.size()][]);
                }
                
                int actual = maquina.distinctSymbols();
                if (actual > mejorValor){
                    mejorValor = actual;
                    mejorPaso = p;
                }
            }

            if (mejorPaso != 0){
                maquina.spin(t, mejorPaso);
                acciones.add(new int[]{t, mejorPaso});
            
                if (maquina.distinctSymbols() == 1){
                    return acciones.toArray(new int[acciones.size()][]);
                }
            }
        }
        
        // FASE 2: Descubrir la permutación
        
        int[] posicion = new int[n + 1];
        boolean[] identificada = new boolean[n + 1];
        identificada[1] = true;
        int offsetRueda1 = 0;
        
        for (int s = 1; s <= n - 1; s++){
            maquina.spin(1, 1);
            acciones.add(new int[]{1, 1});
            
            if (maquina.distinctSymbols() == 1){
                return acciones.toArray(new int[acciones.size()][]);
            }
            
            offsetRueda1 = s;
            posicion[1] = s;
            
            for (int w = 2; w <= n; w++){
                if (identificada[w]){
                    continue; 
                }
                
                maquina.spin(w, n - 1);
                acciones.add(new int[]{w, n - 1});
                
                if (maquina.distinctSymbols() == 1){
                    return acciones.toArray(new int[acciones.size()][]);
                }
                
                if (maquina.distinctSymbols() == n){
                    posicion[w] = s - 1;
                    identificada[w] = true;
                    break;
                }
                
                maquina.spin(w, 1);
                acciones.add(new int[]{w, 1});
                
                if (maquina.distinctSymbols() == 1){
                    // Si ganó, retorna y termina.
                    return acciones.toArray(new int[acciones.size()][]);
                }
            }
        }
    
        // FASE 3: Alinear todas las ruedas al símbolo de la Rueda 1.

        for (int w = 2; w <= n; w++){
            // IA gen para calcular la distacia desde al rueda objetivo a las sigueintes
            int pasos = (((offsetRueda1 - posicion[w]) % n) + n) % n;
            
            if (pasos != 0){
                maquina.spin(w, pasos);
                acciones.add(new int[]{w, pasos});
                
                if (maquina.distinctSymbols() == 1){
                    return acciones.toArray(new int[acciones.size()][]);
                }
            }
        }
        
        int[][] matrizSolucion = new int[acciones.size()][2];
        for (int i = 0; i < acciones.size(); i++) {
            matrizSolucion[i] = acciones.get(i);
        }

        return matrizSolucion;
    }
}