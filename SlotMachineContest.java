import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Clase encargada de resolver y simular el problema de la maratón Slot Machine.
 */
public class SlotMachineContest {

    /**
     * Motor de solución algorítmica.
     * @param n Número de ruedas y símbolos (3 <= n <= 50).
     * @return Matriz de enteros donde cada fila representa una acción {rueda, pasos}.
     */
    public int[][] solve(int n) {
        if (n < 3 || n > 50) {
            System.err.println("Error en solve: El número de ruedas (" + n + ") debe estar entre 3 y 50.");
            return new int[0][0];
        }
        
        SlotMachine SlotMachineSolution = new SlotMachine(n);
        SlotMachineSolution.makeInvisible(); 
        
        return solve(SlotMachineSolution, n);
    }
    
    /**
     * Simulación visual de la solución.
     * @param n Número de ruedas y símbolos (3 <= n <= 50).
     */
    public void simulate(int n) {
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
        SlotMachine SlotMachineSolution = new SlotMachine(n);
        SlotMachineSolution.makeVisible(); 
        
        solve(SlotMachineSolution, n);
        
        SlotMachineSolution.makeInvisible(); 
        SlotMachineSolution.exit();          
    }
    
    /**
     * Método privado que centraliza el motor de la solución.
     * Utiliza configuration() para alinear las ruedas superando el mínimo local.
     * 
     * @param maquina La instancia de la máquina a operar.
     * @param n Cantidad de ruedas y símbolos.
     * @return Matriz de acciones ejecutadas.
     */
    private int[][] solve(SlotMachine maquina, int n) {
        ArrayList<int[]> registroAcciones = new ArrayList<>();
        
        // 1. Si la máquina ya nació en estado ganador, no hay que hacer nada
        if (maquina.distinctSymbols() == 1) {
            return new int[0][0];
        }
        
        // 2. Fijamos la primera rueda como nuestra referencia. 
        // Su color actual será el color objetivo para todas las demás.
        String colorObjetivo = maquina.configuration()[0];
        
        // 3. Alinear cada rueda (desde la 2 hasta n) con la rueda 1
        for (int i = 2; i <= n; i++) {
            
            // Girar la rueda actual paso a paso (máximo n-1 giros)
            for (int pasos = 1; pasos < n; pasos++) {
                // Consultamos el color que tiene la rueda 'i' en este preciso instante.
                // Como el arreglo inicia en 0, la rueda 'i' corresponde al índice 'i - 1'.
                String colorActual = maquina.configuration()[i - 1];
                
                // Si el color de la rueda ya es igual al objetivo, pasamos a la siguiente rueda
                if (colorActual.equalsIgnoreCase(colorObjetivo)) {
                    break; 
                }
                
                // Si no es igual, giramos la rueda 1 paso y registramos la acción
                maquina.spin(i, 1);
                registroAcciones.add(new int[]{i, 1});
            }
        }
        
        // 4. Convertir la lista dinámica a la matriz estática exigida por el UML
        int[][] matrizSolucion = new int[registroAcciones.size()][2];
        for (int i = 0; i < registroAcciones.size(); i++) {
            matrizSolucion[i] = registroAcciones.get(i);
        }

        return matrizSolucion;
    }
}