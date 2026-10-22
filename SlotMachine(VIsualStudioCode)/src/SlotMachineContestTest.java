import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Batería masiva de pruebas unitarias y de aceptación para SlotMachineContest.
 * Solo se prueba solve(n); JAMÁS simulate(n) (bloquea el hilo por makeVisible/wait).
 */
public class SlotMachineContestTest {

    private SlotMachineContest contest;

    @Before
    public void setUp() {
        contest = new SlotMachineContest();
    }

    // ---------- 1. Validación estructural de la matriz ----------

    /**
     * Prueba de Unidad:
     * Verifica que el motor algorítmico logre generar y retornar una matriz 
     * válida (no nula) para el caso base inferior permitido por el problema (n = 3).
     */
    @Test
    public void testSolveN3NotNull() {
        assertNotNull(contest.solve(3));
    }

    /**
     * Prueba de Unidad:
     * Verifica que el motor algorítmico logre generar y retornar una matriz 
     * válida (no nula) para un tamaño de máquina intermedio par (n = 4).
     */
    @Test
    public void testSolveN4NotNull() {
        assertNotNull(contest.solve(4));
    }

    /**
     * Prueba de Unidad:
     * Verifica que el motor algorítmico logre generar y retornar una matriz 
     * válida (no nula) para un tamaño de máquina intermedio impar (n = 5).
     */
    @Test
    public void testSolveN5NotNull() {
        assertNotNull(contest.solve(5));
    }

    // ---------- 2. Validación de límites y rangos lógicos ----------

    /**
     * Prueba de Unidad:
     * Audita la matriz de resultados para garantizar que el algoritmo nunca dicte 
     * movimientos nulos (0 pasos) ni rotaciones negativas, respetando la física 
     * de los giros de la máquina.
     */
    @Test
    public void testNoSeRepitenPasosNegativosOCero() {
        int[][] acciones = contest.solve(5);
        for (int[] accion : acciones) {
            assertNotEquals(0, accion[1]);
            assertFalse(accion[1] < 0);
        }
    }

    // ---------- 3. Casos frontera y resiliencia ----------

    /**
     * Prueba de Unidad:
     * Verifica la resiliencia del sistema ante un caso frontera inválido (n = 1).
     * Dado que no hay ruedas ajenas para alinear contra una rueda pivote, 
     * el algoritmo debe abortar de forma segura retornando una matriz vacía.
     */
    @Test
    public void testN1DevuelveMatrizVacia() {
        // Con n=1 no hay ruedas que alinear contra la rueda pivote (bucle 2..n no ejecuta).
        int[][] acciones = contest.solve(1);
        assertEquals(0, acciones.length);
    }

    /**
     * Prueba de Unidad:
     * Verifica que el algoritmo pueda procesar un tamaño de 2 ruedas sin 
     * lanzar excepciones, retornando una estructura de matriz válida.
     */
    @Test
    public void testN2NoLanzaExcepcion() {
        int[][] acciones = contest.solve(2);
        assertNotNull(acciones);
    }
}