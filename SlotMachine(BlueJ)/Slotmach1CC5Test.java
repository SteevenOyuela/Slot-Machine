import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de aceptación (ciclo 5): flujos completos que combinan varias
 * operaciones de la máquina y verifican el estado final.
 */
public class Slotmach1CC5Test {

    private SlotMachine mach1;

    /**
     * Crea una máquina nueva e invisible antes de cada prueba.
     */
    @Before
    public void setUp() {
        mach1 = new SlotMachine();
        mach1.makeInvisible();
    }

    /**
     * @return la cantidad actual de ruedas en la máquina.
     */
    private int ruedas() {
        return mach1.configuration().length;
    }

    /**
     * Agrega n ruedas y reasigna la última hasta romper un Jackpot accidental.
     * @param n cantidad de ruedas a crear.
     */
    private void crearRuedas(int n) {
        for (int i = 1; i <= n; i++) {
            mach1.addWheel(i);
        }
        while (ruedas() > 1 && mach1.distinctSymbols() == 1) {
            int ultima = ruedas();
            mach1.delWheel(ultima);
            mach1.addWheel(ultima);
        }
    }

    /**
     * Verifica que una rueda Lazy agregada al final pueda bloquearse y conserve
     * su símbolo cuando la máquina gira, sin alterar el número de ruedas.
     */
    @Test
    public void accordingDcOaShouldKeepLockedLazyWheelWhenMachineSpins() {
        crearRuedas(2);
        mach1.spin(new String[]{"blue", "purple"});
        assertTrue(mach1.ok());

        mach1.addLazyWheel(3);
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());

        String perezosa = mach1.configuration()[2];
        mach1.lock(3);
        assertTrue(mach1.ok());

        mach1.spin();
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());
        assertEquals(perezosa, mach1.configuration()[2]);
    }

    /**
     * Verifica que intercambiar, eliminar y volver a agregar ruedas deje la
     * configuración esperada.
     */
    @Test
    public void accordingDcOaShouldSwapDeleteAndRebuildConfiguration() {
        crearRuedas(3);
        mach1.spin(new String[]{"blue", "purple", "green"});
        assertTrue(mach1.ok());

        mach1.swap(1, 3);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"green", "purple", "blue"}, mach1.configuration());

        mach1.delWheel(2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"green", "blue"}, mach1.configuration());

        mach1.addWheel(1);
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());
        assertEquals("green", mach1.configuration()[1]);
        assertEquals("blue", mach1.configuration()[2]);
    }

    /**
     * Verifica que al modificar el catálogo (agregar y eliminar símbolos) los giros por
     * pasos usen siempre el catálogo vigente, incluso si el color mostrado ya no existe.
     */
    @Test
    public void accordingDcOaShouldSpinStepsUsingCurrentCatalog() {
        crearRuedas(2);
        mach1.spin(new String[]{"blue", "purple"});
        assertTrue(mach1.ok());

        mach1.addSymbol(1, "red");
        assertTrue(mach1.ok());
        assertEquals("red", mach1.symbols()[0]);

        mach1.spin(2, 2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"blue", "brown"}, mach1.configuration());

        mach1.delSymbol("brown");
        assertTrue(mach1.ok());

        mach1.spin(2, 2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
        assertFalse(mach1.isJackpot());
    }
}
