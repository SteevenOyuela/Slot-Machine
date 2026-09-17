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

    /**
     * Prueba de Unidad:
     * Valida el comportamiento esperado ante un tamaño fuera de los límites 
     * del contrato ICPC (n = 0). Asegura que el sistema arroje explícitamente 
     * un ArrayIndexOutOfBoundsException al intentar consultar el color pivote.
     */
    @Test
    public void testN0ComportamientoConocido() {
        // NOTA: con n=0 no hay ruedas, y configuration()[0] provoca
        // ArrayIndexOutOfBoundsException al buscar el color pivote (fuera del contrato ICPC: n>=3).
        try {
            contest.solve(0);
            fail("Se esperaba ArrayIndexOutOfBoundsException para n=0 (fuera de contrato)");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    /**
     * Prueba de Unidad:
     * Verifica la resiliencia y el manejo de memoria del algoritmo 
     * llevándolo al límite máximo permitido por las reglas del ICPC (n = 50).
     */
    @Test
    public void testN50NoLanzaExcepcion() {
        int[][] acciones = contest.solve(50);
        assertNotNull(acciones);
    }

    /**
     * Prueba de Unidad:
     * Verifica que las acciones generadas para n=3 apunten exclusivamente a las 
     * ruedas correctas. Como la rueda 1 se usa de pivote, los movimientos 
     * deben aplicarse únicamente a las ruedas 2 y 3.
     */
    @Test
    public void testLimiteMinimoValidoN3() {
        int[][] acciones = contest.solve(3);
        assertNotNull(acciones);
        for (int[] accion : acciones) {
            assertTrue(accion[0] >= 2 && accion[0] <= 3);
        }
    }

    // ---------- 5. Auditoría de solución matemática ----------

    /**
     * Prueba de Integración:
     * Ejecuta una simulación matemática ciega. Genera las acciones con el algoritmo 
     * y luego las aplica iterativamente a una instancia independiente de SlotMachine 
     * para comprobar si, al final del proceso, la máquina realmente alcanza el 
     * estado de Jackpot (un solo símbolo distinto en el tablero).
     */
    @Test
    public void testConfiguracionFinalTieneUnSoloColor() {
        int n = 3;
        int[][] acciones = contest.solve(n);
        SlotMachine mach = new SlotMachine(n);
        mach.makeInvisible();
        for (int[] accion : acciones) {
            mach.spin(accion[0], accion[1]);
        }
        assertEquals(1, mach.distinctSymbols());
    }
}