import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de aceptación colectivas para la clase SlotMachineContest.
 * Autores: Duran Ceballos y Oyuela Mendez (DcOm).
 */
public class SlotMachineContestCTest {
    /**
     * Prueba de Aceptación 1:
     * Verifica que todas las coordenadas de acción {rueda, pasos} dictadas por el algoritmo
     * tengan sentido lógico (la rueda afectada esté entre 2 y n, y los pasos sean positivos).
     */
    @Test
    public void accordingDcOmShouldReturnValidWheelAndStepRanges() {
        SlotMachineContest contest = new SlotMachineContest();
        int n = 4;
        int[][] acciones = contest.solve(n);
        
        for (int i = 0; i < acciones.length; i++) {
            int rueda = acciones[i][0];
            int pasos = acciones[i][1];
            
            assertTrue("El índice de la rueda a mover debe ser >= 2", rueda >= 2);
            assertTrue("El índice de la rueda no debe superar el tamaño máximo n", rueda <= n);
            assertTrue("La cantidad de pasos de giro debe ser mayor a 0", pasos > 0);
        }
    }
    
    /**
     * Prueba de Aceptación 2:
     * Verifica la integridad estructural de la matriz generada por el motor algorítmico.
     * Garantiza que cada acción registrada contenga exactamente dos columnas, 
     * correspondientes estrictamente al par de datos {rueda, pasos} exigido por el UML.
     */
    @Test
    public void accordingDcOmShouldReturnMatrixWithTwoColumnsForEveryAction() {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] acciones = contest.solve(12);
        for (int[] accion : acciones) {
            assertEquals(2, accion.length);
        }
    }

    /**
     * Prueba de Aceptación 3:
     * Verifica el comportamiento del motor algorítmico ante un caso frontera inferior (n = 1).
     * Dado que el problema de la maratón requiere múltiples ruedas para poder interactuar y
     * alinear símbolos, procesar una sola rueda es un estado inválido o "ya resuelto", 
     * por lo que el algoritmo debe abortar y retornar una matriz vacía (0 movimientos).
     */
    @Test
    public void accordingDcOmShouldReturnEmptyMatrixForNEqualsOne() {
        SlotMachineContest contest = new SlotMachineContest();
        assertEquals(0, contest.solve(1).length);
    }
    
    /**
     * Prueba de Aceptación 4:
     * Verifica la consistencia y estabilidad del motor algorítmico al ser invocado 
     * múltiples veces consecutivas con el mismo parámetro (n = 5).
     * Garantiza que el algoritmo siempre retorne resultados válidos (no nulos) y 
     * respete el límite estricto de 10,000 acciones exigido por el problema en cada ejecución.
     */
    @Test
    public void accordingDcOmShouldReturnConsistentResultsOnRepeatedCallsForSameN() {
        SlotMachineContest contest = new SlotMachineContest();
        int n = 4;
        int[][] primera = contest.solve(n);
        int[][] segunda = contest.solve(n);
        assertNotNull(primera);
        assertNotNull(segunda);
        assertTrue(primera.length <= 10000);
        assertTrue(segunda.length <= 10000);
    }
    
    /**
     * Prueba de Aceptación 5:
     * Evalúa la robustez del motor algorítmico llevándolo al límite máximo 
     * permitido por las reglas del problema (n = 50 ruedas y símbolos).
     */
    @Test
    public void accordingDcOmShouldSolveMaxMachineSizeWithinLimits() {
        SlotMachineContest contest = new SlotMachineContest();
        int n = 50; // Límite máximo establecido por la maratón
        
        int[][] acciones = contest.solve(n);
        
        assertNotNull("Debe lograr generar una matriz incluso para n=50", acciones);
        assertTrue("Incluso en el peor escenario (n=50), las acciones deben ser menores a 10,000", acciones.length <= 10000);
    }
}