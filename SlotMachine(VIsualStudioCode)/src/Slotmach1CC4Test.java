import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de integración para Slotmach1 relacionadas con el Jackpot:
 * verifica que se alcance correctamente, que el estado ganador se preserve
 * y que se rechacen operaciones posteriores al ganar.
 */
public class Slotmach1CC4Test {
    
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
     * Obtiene el color del símbolo ubicado en el índice dado del catálogo
     * de símbolos de la máquina, validando que exista.
     * @param indice posición en el catálogo de símbolos.
     * @return el color del símbolo en esa posición.
     */
    private String simbolo(int indice) {
        String[] simbolos = mach1.symbols();
        assertNotNull(simbolos);
        assertTrue(simbolos.length > indice);
        return simbolos[indice];
    }

    /**
     * Agrega ruedas hasta que la máquina tenga exactamente 3.
     */
    private void crearTresRuedas() {
        while (ruedas() < 3) {
            mach1.addWheel(ruedas() + 1);
            assertTrue(mach1.ok());
        }
    }

    /**
     * addWheel asigna símbolos al azar; si por azar la configuración inicial
     * ya resulta ganadora, spin(String[]) quedaría bloqueado antes de poder
     * fijar la configuración deseada en la prueba. Este método reasigna la
     * última rueda hasta romper ese Jackpot accidental.
     */
    private void evitarJackpotAccidental() {
        while (ruedas() > 1 && mach1.distinctSymbols() == 1) {
            int ultima = ruedas();
            mach1.delWheel(ultima);
            mach1.addWheel(ultima);
        }
    }
    
    /**
     * Verifica que una rueda Lefty complete el Jackpot al copiar el símbolo de su
     * vecina izquierda, y que luego se rechace cualquier giro sin alterar el estado ganador.
     */
    @Test
    public void accordingDcOaShouldReachJackpotWhenLeftyWheelCopiesLeftNeighbor() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        evitarJackpotAccidental();
        mach1.addLeftyWheel(2);
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());

        String a = simbolo(0);
        String b = simbolo(1);

        mach1.spin(new String[]{a, b, a});
        assertTrue(mach1.ok());
        assertFalse(mach1.isJackpot());

        // La Lefty (rueda 2) ignora el símbolo aleatorio y copia el de la rueda 1.
        mach1.spin(2);
        assertTrue(mach1.ok());
        assertTrue(mach1.isJackpot());

        String[] ganador = {a, a, a};
        assertArrayEquals(ganador, mach1.configuration());

        mach1.spin(1);
        assertFalse(mach1.ok());
        assertArrayEquals(ganador, mach1.configuration());
    }

    /**
     * Verifica que con una rueda Rebel en la máquina se alcance el Jackpot, y que
     * después se rechacen bloquearla, colocar símbolos y fijar otra configuración,
     * conservando el estado ganador.
     */
    @Test
    public void accordingDcOaShouldReachJackpotWithRebelWheelAndRejectLaterOperations() {
        mach1.addRebelWheel(1);
        assertTrue(mach1.ok());
        mach1.addWheel(2);
        evitarJackpotAccidental();

        String a = simbolo(0);
        String b = simbolo(1);

        mach1.spin(new String[]{a, a});
        assertTrue(mach1.ok());
        assertTrue(mach1.isJackpot());

        String[] ganador = {a, a};

        mach1.lock(1);
        assertFalse(mach1.ok());

        mach1.spin(new String[]{b, b});
        assertFalse(mach1.ok());
        assertArrayEquals(ganador, mach1.configuration());

        mach1.placeSymbol(2, b);
        assertFalse(mach1.ok());
        assertArrayEquals(ganador, mach1.configuration());
    }

    /**
     * Verifica que los símbolos agregados con addSymbol(tipo, color) (ephemeral y shy)
     * puedan formar el Jackpot, y que luego se rechacen spin() y spin(rueda, pasos).
     */
    @Test
    public void accordingDcOaShouldReachJackpotWithTypedSymbolsAndRejectSubsequentSpins() {
        mach1.addSymbol("ephemeral", "red");
        assertTrue(mach1.ok());
        mach1.addSymbol("shy", "orange");
        assertTrue(mach1.ok());

        crearTresRuedas();
        evitarJackpotAccidental();

        mach1.spin(new String[]{"red", "orange", "red"});
        assertTrue(mach1.ok());
        assertFalse(mach1.isJackpot());

        mach1.spin(new String[]{"orange", "orange", "orange"});
        assertTrue(mach1.ok());
        assertTrue(mach1.isJackpot());
        assertEquals(1, mach1.distinctSymbols());

        String[] ganador = {"orange", "orange", "orange"};

        mach1.spin();
        assertFalse(mach1.ok());
        assertArrayEquals(ganador, mach1.configuration());

        mach1.spin(2, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(ganador, mach1.configuration());
    }
}
