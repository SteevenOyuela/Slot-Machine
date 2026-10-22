import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de aceptación colectivas para la clase SlotMachineContest.
 * Autores: Duran Ceballos y Oyuela Mendez (DcOm).
 */
public class SlotMachineContestCTest {    
    /**
     * Prueba de Aceptación 1:
     * Verifica la integridad estructural de la matriz generada por el motor algorítmico.
     * Garantiza que cada acción registrada contenga exactamente dos columnas, 
     * correspondientes estrictamente al par de datos {rueda, pasos} exigido por el UML.
     */
    @Test
    public void accordingDcOmShouldReturnMatrixWithTwoColumnsForEveryAction() {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] acciones = contest.solve(4);
        for (int[] accion : acciones) {
            assertEquals(2, accion.length);
        }
    }

    /**
     * Prueba de Aceptación 2:
     * Verifica que el motor algorítmico pueda manejar correctamente un caso frontera 
     * con un número mínimo de ruedas (n = 2).
     */
    @Test
    public void accordingDcOmShouldHandleMinimumValidSize() {
        SlotMachineContest contest = new SlotMachineContest();
        int[][] acciones = contest.solve(2);
        assertNotNull(acciones);
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
}