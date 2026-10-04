import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Pruebas unitarias de la clase SlotMachine.
 * Cada prueba documenta: qué se busca probar, qué acción se ejecuta (When)
 * y qué resultado se espera obtener (Then).
 *
 * Catálogo inicial de SlotMachine(): blue, purple, green, brown (en ese orden).
 * No se prueba exit() porque termina la JVM de las pruebas.
 */
public class SlotMachineC4Test {

    private SlotMachine mach1;

    @Before
    public void setUp() {
        mach1 = new SlotMachine();
        mach1.makeInvisible();
    }

    @After
    public void tearDown() {
        mach1.makeInvisible();
    }

    // ---------------- AUXILIARES ----------------

    /** @return la cantidad actual de ruedas. */
    private int ruedas() {
        return mach1.configuration().length;
    }

    /**
     * addWheel asigna símbolos al azar; si la configuración resulta ganadora,
     * las operaciones que rechazan movimientos tras el Jackpot bloquearían la
     * prueba. Reasigna la última rueda hasta romper ese Jackpot accidental.
     */
    private void evitarJackpotAccidental() {
        while (ruedas() > 1 && mach1.distinctSymbols() == 1) {
            int ultima = ruedas();
            mach1.delWheel(ultima);
            mach1.addWheel(ultima);
        }
    }

    /** Crea n ruedas garantizando que no queden en estado ganador. */
    private void crearRuedas(int n) {
        for (int i = 1; i <= n; i++) {
            mach1.addWheel(i);
        }
        evitarJackpotAccidental();
    }

    /** Fija una configuración exacta y valida que se haya aceptado. */
    private void fijar(String... colores) {
        mach1.spin(colores);
        assertTrue(mach1.ok());
        assertArrayEquals(colores, mach1.configuration());
    }

    /** Elimina todos los símbolos del catálogo. */
    private void vaciarCatalogo() {
        for (String color : mach1.symbols()) {
            mach1.delSymbol(color);
        }
    }
    
    // ---------------- addSymbol(String, String) ----------------

    /**
     * QUÉ SE PRUEBA: que un símbolo "normal" se agrega al final del catálogo.
     * WHEN: se llama addSymbol("normal", "red").
     * THEN: ok() true y "red" es el último elemento de symbols().
     */
    @Test
    public void testAddSymbolTypedNormalAppendsAtEnd() {
        mach1.addSymbol("normal", "red");
        String[] cat = mach1.symbols();
        assertTrue(mach1.ok());
        assertEquals(5, cat.length);
        assertEquals("red", cat[cat.length - 1]);
    }

    /**
     * QUÉ SE PRUEBA: que se puede agregar un símbolo de tipo "ephemeral".
     * WHEN: se llama addSymbol("ephemeral", "orange").
     * THEN: ok() true y symbols() contiene "orange".
     */
    @Test
    public void testAddSymbolTypedEphemeralSucceeds() {
        mach1.addSymbol("ephemeral", "orange");
        assertTrue(mach1.ok());
        assertTrue(Arrays.asList(mach1.symbols()).contains("orange"));
    }

    /**
     * QUÉ SE PRUEBA: que se puede agregar un símbolo de tipo "shy" sin distinguir mayúsculas en el tipo.
     * WHEN: se llama addSymbol("SHY", "pink").
     * THEN: ok() true y symbols() contiene "pink".
     */
    @Test
    public void testAddSymbolTypedShySucceeds() {
        mach1.addSymbol("SHY", "pink");
        assertTrue(mach1.ok());
        assertTrue(Arrays.asList(mach1.symbols()).contains("pink"));
    }

    /**
     * QUÉ SE PRUEBA: que un tipo de símbolo desconocido se rechaza.
     * WHEN: se llama addSymbol("magic", "red").
     * THEN: ok() debe ser false y el catálogo no debe cambiar.
     */
    @Test
    public void testAddSymbolTypedUnknownTypeRejected() {
        int before = mach1.symbols().length;
        mach1.addSymbol("magic", "red");
        assertFalse(mach1.ok());
        assertEquals(before, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que no se agrega un símbolo tipado con un color ya existente.
     * WHEN: se llama addSymbol("shy", "blue") cuando "blue" ya existe.
     * THEN: ok() debe ser false y el catálogo no debe cambiar.
     */
    @Test
    public void testAddSymbolTypedDuplicateColorRejected() {
        int before = mach1.symbols().length;
        mach1.addSymbol("shy", "blue");
        assertFalse(mach1.ok());
        assertEquals(before, mach1.symbols().length);
    }
    
    // ---------------- addLeftyWheel ----------------

    /**
     * QUÉ SE PRUEBA: que se puede insertar una rueda Lefty en una posición existente.
     * WHEN: con 2 ruedas, se llama addLeftyWheel(1).
     * THEN: ok() true y deben quedar 3 ruedas.
     */
    @Test
    public void testAddLeftyWheelInsertsAtExistingPosition() {
        crearRuedas(2);
        mach1.addLeftyWheel(1);
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que la rueda Lefty copia el símbolo de la rueda a su izquierda al girar.
     * WHEN: con 3 ruedas y una Lefty en la posición 2, se llama spin().
     * THEN: la rueda 2 debe mostrar el mismo color que la rueda 1.
     */
    @Test
    public void testAddLeftyWheelCopiesLeftNeighborOnSpin() {
        crearRuedas(3);
        mach1.addLeftyWheel(2);
        mach1.spin();
        assertEquals(mach1.configuration()[0], mach1.configuration()[1]);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede insertar una Lefty en la posición 0.
     * WHEN: con 2 ruedas, se llama addLeftyWheel(0).
     * THEN: ok() false y la cantidad de ruedas no cambia.
     */
    @Test
    public void testAddLeftyWheelPositionZeroRejected() {
        crearRuedas(2);
        mach1.addLeftyWheel(0);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede insertar una Lefty sobre otra Lefty.
     * WHEN: se agrega una Lefty en la posición 1 y se intenta otra vez addLeftyWheel(1).
     * THEN: el segundo intento da ok() false y quedan 3 ruedas.
     */
    @Test
    public void testAddLeftyWheelOnExistingLeftyRejected() {
        crearRuedas(2);
        mach1.addLeftyWheel(1);
        mach1.addLeftyWheel(1);
        assertFalse(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que una rueda Lefty no se puede intercambiar.
     * WHEN: con una Lefty en la posición 1, se llama swap(1, 2).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testAddLeftyWheelCannotBeSwapped() {
        crearRuedas(2);
        mach1.addLeftyWheel(1);
        String[] before = mach1.configuration();
        mach1.swap(1, 2);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    // ---------------- addRebelWheel ----------------

    /**
     * QUÉ SE PRUEBA: que se puede agregar una rueda Rebel a una máquina vacía.
     * WHEN: se llama addRebelWheel(1) sin ruedas previas.
     * THEN: ok() true y debe quedar 1 rueda.
     */
    @Test
    public void testAddRebelWheelOnEmptyMachineSucceeds() {
        mach1.addRebelWheel(1);
        assertTrue(mach1.ok());
        assertEquals(1, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede agregar una Rebel en la posición 0.
     * WHEN: se llama addRebelWheel(0).
     * THEN: ok() false y no se crean ruedas.
     */
    @Test
    public void testAddRebelWheelPositionZeroRejected() {
        mach1.addRebelWheel(0);
        assertFalse(mach1.ok());
        assertEquals(0, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que una rueda Rebel rechaza ser bloqueada.
     * WHEN: se agrega una Rebel y se llama lock(1).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testAddRebelWheelCannotBeLocked() {
        mach1.addRebelWheel(1);
        mach1.lock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que una rueda Rebel no se puede eliminar.
     * WHEN: se agrega una Rebel y se llama delWheel(1).
     * THEN: ok() false y la rueda sigue existiendo.
     */
    @Test
    public void testAddRebelWheelCannotBeDeleted() {
        mach1.addRebelWheel(1);
        mach1.delWheel(1);
        assertFalse(mach1.ok());
        assertEquals(1, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que una rueda Rebel no se puede intercambiar.
     * WHEN: con una Rebel en la posición 1, se llama swap(1, 2).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testAddRebelWheelCannotBeSwapped() {
        crearRuedas(2);
        mach1.addRebelWheel(1);
        String[] before = mach1.configuration();
        mach1.swap(1, 2);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }
}