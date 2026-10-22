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
public class SlotMachineC2Test {

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

    // ---------------- SlotMachine() ----------------

    /**
     * QUÉ SE PRUEBA: que una máquina recién creada no tiene ruedas.
     * WHEN: se consulta configuration() sin haber agregado ninguna rueda.
     * THEN: el arreglo devuelto debe tener longitud 0.
     */
    @Test
    public void testInitialConfigurationIsEmpty() {
        assertEquals(0, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que sin ruedas no hay símbolos distintos que contar.
     * WHEN: se consulta distinctSymbols() en una máquina recién creada.
     * THEN: el resultado debe ser 0.
     */
    @Test
    public void testInitialDistinctSymbolsIsZero() {
        assertEquals(0, mach1.distinctSymbols());
    }

    /**
     * QUÉ SE PRUEBA: que el catálogo inicial trae 4 símbolos por defecto.
     * WHEN: se consulta symbols() justo después de crear la máquina.
     * THEN: el arreglo devuelto debe tener longitud 4.
     */
    @Test
    public void testInitialCatalogHasFourSymbols() {
        assertEquals(4, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que el catálogo inicial contiene exactamente los colores por defecto, en orden.
     * WHEN: se consulta symbols() justo después de crear la máquina.
     * THEN: debe devolver {blue, purple, green, brown}.
     */
    @Test
    public void testInitialCatalogContainsDefaultColorsInOrder() {
        assertArrayEquals(new String[]{"blue", "purple", "green", "brown"}, mach1.symbols());
    }

    // ---------------- SlotMachine(int n) ----------------

    /**
     * QUÉ SE PRUEBA: que el constructor con n crea exactamente n ruedas.
     * WHEN: se construye SlotMachine(4).
     * THEN: configuration() debe tener longitud 4.
     */
    @Test
    public void testConstructorNCreatesNWheels() {
        SlotMachine m = new SlotMachine(4);
        m.makeInvisible();
        assertEquals(4, m.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que el constructor con n crea exactamente n símbolos en el catálogo.
     * WHEN: se construye SlotMachine(5).
     * THEN: symbols() debe tener longitud 5.
     */
    @Test
    public void testConstructorNCreatesNSymbols() {
        SlotMachine m = new SlotMachine(5);
        m.makeInvisible();
        assertEquals(5, m.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que los n símbolos generados tienen colores distintos entre sí.
     * WHEN: se construye SlotMachine(6).
     * THEN: el conjunto de colores de symbols() debe tener 6 elementos.
     */
    @Test
    public void testConstructorNSymbolsAreDistinct() {
        SlotMachine m = new SlotMachine(6);
        m.makeInvisible();
        assertEquals(6, new HashSet<String>(Arrays.asList(m.symbols())).size());
    }

    /**
     * QUÉ SE PRUEBA: que cada rueda inicial muestra un símbolo del catálogo.
     * WHEN: se construye SlotMachine(5) y se recorre configuration().
     * THEN: todos los colores mostrados deben pertenecer a symbols().
     */
    @Test
    public void testConstructorNWheelsShowCatalogSymbols() {
        SlotMachine m = new SlotMachine(5);
        m.makeInvisible();
        List<String> cat = Arrays.asList(m.symbols());
        for (String color : m.configuration()) {
            assertTrue(cat.contains(color));
        }
    }

    // ---------------- SlotMachine(int n, String m) ----------------

    /**
     * QUÉ SE PRUEBA: que el constructor con tipos mezclados crea n ruedas.
     * WHEN: se construye SlotMachine(4, "mixed").
     * THEN: configuration() debe tener longitud 4.
     */
    @Test
    public void testConstructorNMCreatesNWheels() {
        SlotMachine m = new SlotMachine(4, "mixed");
        m.makeInvisible();
        assertEquals(4, m.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que el constructor con tipos mezclados crea n símbolos en el catálogo.
     * WHEN: se construye SlotMachine(5, "mixed").
     * THEN: symbols() debe tener longitud 5.
     */
    @Test
    public void testConstructorNMCreatesNSymbols() {
        SlotMachine m = new SlotMachine(5, "mixed");
        m.makeInvisible();
        assertEquals(5, m.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que los colores del catálogo no se repiten aunque los tipos sean aleatorios.
     * WHEN: se construye SlotMachine(6, "mixed").
     * THEN: el conjunto de colores de symbols() debe tener 6 elementos.
     */
    @Test
    public void testConstructorNMSymbolsAreDistinct() {
        SlotMachine m = new SlotMachine(6, "mixed");
        m.makeInvisible();
        assertEquals(6, new HashSet<String>(Arrays.asList(m.symbols())).size());
    }

    /**
     * QUÉ SE PRUEBA: que cada rueda inicial muestra un símbolo del catálogo.
     * WHEN: se construye SlotMachine(5, "mixed") y se recorre configuration().
     * THEN: todos los colores mostrados deben pertenecer a symbols().
     */
    @Test
    public void testConstructorNMWheelsShowCatalogSymbols() {
        SlotMachine m = new SlotMachine(5, "mixed");
        m.makeInvisible();
        List<String> cat = Arrays.asList(m.symbols());
        for (String color : m.configuration()) {
            assertTrue(cat.contains(color));
        }
    }

    // ---------------- makeVisible ----------------

    /**
     * QUÉ SE PRUEBA: que mostrar la máquina no altera el resultado de la última operación.
     * WHEN: se agrega una rueda, se llama makeVisible() y luego makeInvisible().
     * THEN: ok() debe seguir siendo true.
     */
    @Test
    public void testMakeVisibleDoesNotChangeOk() {
        mach1.addWheel(1);
        mach1.makeVisible();
        mach1.makeInvisible();
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que mostrar la máquina conserva la configuración de las ruedas.
     * WHEN: con {blue, purple} fijado, se llama makeVisible().
     * THEN: configuration() debe seguir siendo {blue, purple}.
     */
    @Test
    public void testMakeVisiblePreservesConfiguration() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.makeVisible();
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que se pueden ejecutar operaciones válidas mientras la máquina es visible.
     * WHEN: se llama makeVisible() y luego addWheel(1).
     * THEN: ok() debe ser true y debe existir 1 rueda.
     */
    @Test
    public void testMakeVisibleAllowsValidOperations() {
        mach1.makeVisible();
        mach1.addWheel(1);
        assertTrue(mach1.ok());
        assertEquals(1, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que llamar makeVisible() varias veces seguidas es inofensivo.
     * WHEN: se llama makeVisible() dos veces, luego makeInvisible() y se agrega una rueda.
     * THEN: ok() debe ser true y debe existir 1 rueda.
     */
    @Test
    public void testMakeVisibleTwiceIsHarmless() {
        mach1.makeVisible();
        mach1.makeVisible();
        mach1.makeInvisible();
        mach1.addWheel(1);
        assertTrue(mach1.ok());
        assertEquals(1, ruedas());
    }

    // ---------------- makeInvisible ----------------

    /**
     * QUÉ SE PRUEBA: que la máquina sigue siendo operable aunque esté invisible.
     * WHEN: estando invisible (fijado en setUp), se llama addWheel(1).
     * THEN: ok() debe ser true y debe quedar registrada 1 rueda.
     */
    @Test
    public void testMachineOperableAfterMakeInvisible() {
        mach1.addWheel(1);
        assertTrue(mach1.ok());
        assertEquals(1, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que ocultar la máquina conserva la configuración de las ruedas.
     * WHEN: con {blue, purple} fijado, se llama makeInvisible().
     * THEN: configuration() debe seguir siendo {blue, purple}.
     */
    @Test
    public void testMakeInvisiblePreservesConfiguration() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.makeInvisible();
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que en modo invisible los errores no muestran diálogos que bloqueen.
     * WHEN: estando invisible, se llama addWheel(0) (posición inválida).
     * THEN: la llamada retorna y ok() debe ser false.
     */
    @Test
    public void testMakeInvisibleErrorsDoNotBlock() {
        mach1.addWheel(0);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que en modo invisible el Jackpot se detecta sin mostrar diálogos.
     * WHEN: estando invisible, se fija {blue, blue}.
     * THEN: isJackpot() debe ser true.
     */
    @Test
    public void testMakeInvisibleJackpotWithoutDialog() {
        crearRuedas(2);
        fijar("blue", "blue");
        assertTrue(mach1.isJackpot());
    }

    // ---------------- addWheel ----------------

    /**
     * QUÉ SE PRUEBA: que se puede agregar la primera rueda en la posición 1 de una máquina vacía.
     * WHEN: se llama addWheel(1) sin ruedas previas.
     * THEN: ok() debe ser true y la máquina debe quedar con 1 rueda.
     */
    @Test
    public void testAddWheelPosition1OnEmptyMachineSucceeds() {
        mach1.addWheel(1);
        assertTrue(mach1.ok());
        assertEquals(1, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que insertar al inicio desplaza las ruedas existentes a la derecha.
     * WHEN: con {blue, purple} fijado, se llama addWheel(1).
     * THEN: ok() debe ser true, deben quedar 3 ruedas y las posiciones 2 y 3 deben ser blue y purple.
     */
    @Test
    public void testAddWheelAtBeginningShiftsExistingWheels() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.addWheel(1);
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());
        assertEquals("blue", mach1.configuration()[1]);
        assertEquals("purple", mach1.configuration()[2]);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede agregar una rueda en la posición 0 (fuera de rango).
     * WHEN: se intenta addWheel(0) habiendo ya una rueda.
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testAddWheelPositionZeroRejected() {
        mach1.addWheel(1);
        int before = ruedas();
        mach1.addWheel(0);
        assertFalse(mach1.ok());
        assertEquals(before, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede agregar una rueda más allá de "tamaño actual + 1".
     * WHEN: se intenta agregar en la posición (cantidad actual + 2).
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testAddWheelPositionTooHighRejected() {
        mach1.addWheel(1);
        int before = ruedas();
        mach1.addWheel(before + 2);
        assertFalse(mach1.ok());
        assertEquals(before, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede insertar una rueda sobre una posición bloqueada.
     * WHEN: se bloquea la rueda 1 y se intenta addWheel(1).
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testAddWheelOnLockedPositionRejected() {
        crearRuedas(2);
        mach1.lock(1);
        mach1.addWheel(1);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    // ---------------- delWheel ----------------

    /**
     * QUÉ SE PRUEBA: que se puede eliminar la primera rueda y las demás se recorren correctamente.
     * WHEN: con {blue, purple} fijado, se elimina la rueda en la posición 1.
     * THEN: ok() debe ser true y debe quedar solo la rueda "purple".
     */
    @Test
    public void testDelWheelFirstSucceeds() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.delWheel(1);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que se puede eliminar una rueda intermedia y las de los extremos se conservan.
     * WHEN: con {blue, purple, green} fijado, se elimina la rueda en la posición 2.
     * THEN: ok() debe ser true y deben quedar "blue" y "green", en ese orden.
     */
    @Test
    public void testDelWheelMiddleSucceeds() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        mach1.delWheel(2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"blue", "green"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede eliminar una rueda si la máquina no tiene ninguna.
     * WHEN: se llama delWheel(1) sin haber agregado ruedas.
     * THEN: ok() debe ser false.
     */
    @Test
    public void testDelWheelOnEmptyMachineRejected() {
        mach1.delWheel(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede eliminar una rueda en una posición mayor a las existentes.
     * WHEN: existiendo una rueda, se llama delWheel(cantidad actual + 1).
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testDelWheelPositionTooHighRejected() {
        mach1.addWheel(1);
        int before = ruedas();
        mach1.delWheel(before + 1);
        assertFalse(mach1.ok());
        assertEquals(before, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede eliminar una rueda bloqueada.
     * WHEN: se bloquea la rueda 1 y se llama delWheel(1).
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testDelWheelLockedWheelRejected() {
        crearRuedas(2);
        mach1.lock(1);
        mach1.delWheel(1);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    // ---------------- addSymbol(int, String) ----------------

    /**
     * QUÉ SE PRUEBA: que un símbolo nuevo agregado en posición intermedia queda en esa posición.
     * WHEN: se llama addSymbol(3, "red") con un color que aún no existe.
     * THEN: ok() true, el catálogo crece en 1 y "red" queda en el índice 2.
     */
    @Test
    public void testAddSymbolInMiddlePosition() {
        int before = mach1.symbols().length;
        mach1.addSymbol(3, "red");
        assertTrue(mach1.ok());
        assertEquals(before + 1, mach1.symbols().length);
        assertEquals("red", mach1.symbols()[2]);
    }

    /**
     * QUÉ SE PRUEBA: que la posición 1 (o menor) inserta al inicio del catálogo.
     * WHEN: se llama addSymbol(1, "red").
     * THEN: "red" debe ser el primer elemento de symbols().
     */
    @Test
    public void testAddSymbolPositionOneInsertsAtBeginning() {
        mach1.addSymbol(1, "red");
        assertEquals("red", mach1.symbols()[0]);
    }

    /**
     * QUÉ SE PRUEBA: que una posición mayor al tamaño del catálogo agrega al final.
     * WHEN: se llama addSymbol(99, "red").
     * THEN: "red" debe ser el último elemento de symbols().
     */
    @Test
    public void testAddSymbolPositionBeyondSizeAppendsAtEnd() {
        mach1.addSymbol(99, "red");
        String[] cat = mach1.symbols();
        assertEquals("red", cat[cat.length - 1]);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede duplicar un símbolo cuyo color ya existe.
     * WHEN: se intenta addSymbol(1, "blue"), color ya presente en el catálogo.
     * THEN: ok() debe ser false y el tamaño del catálogo no debe cambiar.
     */
    @Test
    public void testAddSymbolDuplicateColorRejected() {
        int before = mach1.symbols().length;
        mach1.addSymbol(1, "blue");
        assertFalse(mach1.ok());
        assertEquals(before, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que la detección de duplicados no distingue mayúsculas de minúsculas.
     * WHEN: se intenta addSymbol(1, "BLUE") cuando "blue" ya existe.
     * THEN: el tamaño del catálogo no debe cambiar.
     */
    @Test
    public void testAddSymbolDuplicateColorDifferentCaseRejected() {
        int before = mach1.symbols().length;
        mach1.addSymbol(1, "BLUE");
        assertEquals(before, mach1.symbols().length);
    }

    // ---------------- delSymbol ----------------

    /**
     * QUÉ SE PRUEBA: que se puede eliminar del catálogo un símbolo existente por su color.
     * WHEN: se llama delSymbol("blue").
     * THEN: ok() true, el catálogo no contiene "blue" y queda con 3 símbolos.
     */
    @Test
    public void testDelSymbolExistingRemovesFromCatalog() {
        mach1.delSymbol("blue");
        assertTrue(mach1.ok());
        assertFalse(Arrays.asList(mach1.symbols()).contains("blue"));
        assertEquals(3, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que eliminar un símbolo inexistente no afecta al catálogo.
     * WHEN: se llama delSymbol("nonexistent").
     * THEN: ok() false y el tamaño del catálogo no cambia.
     */
    @Test
    public void testDelSymbolNonExistentDoesNotChangeCatalog() {
        int before = mach1.symbols().length;
        mach1.delSymbol("nonexistent");
        assertFalse(mach1.ok());
        assertEquals(before, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que la búsqueda del color a eliminar no distingue mayúsculas de minúsculas.
     * WHEN: se llama delSymbol("BLUE").
     * THEN: "blue" debe quedar eliminado del catálogo.
     */
    @Test
    public void testDelSymbolDifferentCaseRemoves() {
        mach1.delSymbol("BLUE");
        assertFalse(Arrays.asList(mach1.symbols()).contains("blue"));
    }

    /**
     * QUÉ SE PRUEBA: que al eliminar un símbolo, el resto del catálogo conserva su orden.
     * WHEN: se elimina "blue".
     * THEN: symbols() debe ser {purple, green, brown}.
     */
    @Test
    public void testDelSymbolPreservesOtherSymbolsAndOrder() {
        mach1.delSymbol("blue");
        assertArrayEquals(new String[]{"purple", "green", "brown"}, mach1.symbols());
    }

    /**
     * QUÉ SE PRUEBA: que eliminar un símbolo del catálogo no altera lo que ya muestran las ruedas.
     * WHEN: con {blue, purple} fijado, se elimina "blue" del catálogo.
     * THEN: configuration() debe seguir siendo {blue, purple}.
     */
    @Test
    public void testDelSymbolDoesNotAlterWheels() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.delSymbol("blue");
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    // ---------------- placeSymbol ----------------

    /**
     * QUÉ SE PRUEBA: que se puede colocar un color del catálogo en una rueda.
     * WHEN: con 2 ruedas sin Jackpot, se llama placeSymbol(1, "blue").
     * THEN: ok() true y la rueda 1 debe mostrar "blue".
     */
    @Test
    public void testPlaceSymbolValidColorSucceeds() {
        crearRuedas(2);
        mach1.placeSymbol(1, "blue");
        assertTrue(mach1.ok());
        assertEquals("blue", mach1.configuration()[0]);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede colocar un color que no existe en el catálogo.
     * WHEN: se llama placeSymbol(1, "nonexistent").
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testPlaceSymbolInvalidColorRejected() {
        crearRuedas(2);
        String[] before = mach1.configuration();
        mach1.placeSymbol(1, "nonexistent");
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede colocar un símbolo en una rueda bloqueada.
     * WHEN: se bloquea la rueda 1 y se llama placeSymbol(1, "blue").
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testPlaceSymbolOnLockedWheelRejected() {
        crearRuedas(2);
        mach1.lock(1);
        String[] before = mach1.configuration();
        mach1.placeSymbol(1, "blue");
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que tras el Jackpot ya no se permite colocar símbolos.
     * WHEN: se logra {blue, blue} con placeSymbol y luego se llama placeSymbol(1, "green").
     * THEN: ok() false y la configuración ganadora no cambia.
     */
    @Test
    public void testPlaceSymbolRejectedAfterJackpot() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.placeSymbol(2, "blue");
        assertTrue(mach1.isJackpot());
        String[] before = mach1.configuration();
        mach1.placeSymbol(1, "green");
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el caso límite de una posición mayor al número de ruedas.
     * WHEN: con 3 ruedas, se llama placeSymbol(99, "green").
     * THEN: ok() true y la última rueda debe mostrar "green".
     */
    @Test
    public void testPlaceSymbolPositionAboveRangeUsesLastWheel() {
        crearRuedas(3);
        mach1.placeSymbol(99, "green");
        assertTrue(mach1.ok());
        assertEquals("green", mach1.configuration()[2]);
    }

    // ---------------- spin() ----------------

    /**
     * QUÉ SE PRUEBA: que una vez logrado el Jackpot, ya no se permite girar la máquina.
     * WHEN: se logra el Jackpot con {blue, blue} y luego se llama spin().
     * THEN: ok() debe ser false.
     */
    @Test
    public void testSpinRejectedAfterJackpot() {
        crearRuedas(2);
        fijar("blue", "blue");
        assertTrue(mach1.isJackpot());
        mach1.spin();
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que spin() funciona cuando hay ruedas y símbolos.
     * WHEN: con 3 ruedas sin Jackpot, se llama spin().
     * THEN: ok() true y la cantidad de ruedas no cambia.
     */
    @Test
    public void testSpinSucceedsWithWheels() {
        crearRuedas(3);
        mach1.spin();
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que una rueda bloqueada mantiene su símbolo al ejecutar spin().
     * WHEN: se fija {blue, purple}, se bloquea la rueda 2 y se llama spin().
     * THEN: la rueda 2 debe seguir mostrando "purple".
     */
    @Test
    public void testSpinLockedWheelKeepsSymbol() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(2);
        mach1.spin();
        assertEquals("purple", mach1.configuration()[1]);
    }

    /**
     * QUÉ SE PRUEBA: que tras spin() cada rueda muestra un símbolo del catálogo.
     * WHEN: con 3 ruedas, se llama spin().
     * THEN: todos los colores de configuration() deben pertenecer a symbols().
     */
    @Test
    public void testSpinSymbolsBelongToCatalog() {
        crearRuedas(3);
        mach1.spin();
        List<String> cat = Arrays.asList(mach1.symbols());
        for (String color : mach1.configuration()) {
            assertTrue(cat.contains(color));
        }
    }

    /**
     * QUÉ SE PRUEBA: que spin() sin ruedas no hace nada ni crea ruedas.
     * WHEN: tras una operación fallida (addWheel(5)), se llama spin() sin ruedas.
     * THEN: ok() sigue false y configuration() sigue vacío.
     */
    @Test
    public void testSpinWithoutWheelsDoesNothing() {
        mach1.addWheel(5);
        mach1.spin();
        assertFalse(mach1.ok());
        assertEquals(0, ruedas());
    }

    // ---------------- spin(int wheel) ----------------

    /**
     * QUÉ SE PRUEBA: que se puede girar una única rueda indicando una posición válida.
     * WHEN: con 3 ruedas sin Jackpot, se llama spin(2).
     * THEN: ok() debe ser true.
     */
    @Test
    public void testSpinWheelValidIndexSucceeds() {
        crearRuedas(3);
        mach1.spin(2);
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede girar una rueda bloqueada.
     * WHEN: se bloquea la rueda 1 y se llama spin(1).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinWheelLockedRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(1);
        String[] before = mach1.configuration();
        mach1.spin(1);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede girar una rueda individual una vez logrado el Jackpot.
     * WHEN: se logra {blue, blue} y luego se llama spin(1).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testSpinWheelRejectedAfterJackpot() {
        crearRuedas(2);
        fijar("blue", "blue");
        mach1.spin(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que spin(wheel) solo modifica la rueda indicada.
     * WHEN: con {blue, purple, green}, se llama spin(2).
     * THEN: las ruedas 1 y 3 deben seguir mostrando "blue" y "green".
     */
    @Test
    public void testSpinWheelOnlyChosenWheelChanges() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        mach1.spin(2);
        assertEquals("blue", mach1.configuration()[0]);
        assertEquals("green", mach1.configuration()[2]);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede girar una rueda si la máquina no tiene ruedas.
     * WHEN: se llama spin(1) sin ruedas.
     * THEN: ok() debe ser false.
     */
    @Test
    public void testSpinWheelOnEmptyMachineRejected() {
        mach1.spin(1);
        assertFalse(mach1.ok());
    }

    // ---------------- spin(int wheel, int steps) ----------------

    /**
     * QUÉ SE PRUEBA: que un paso avanza la rueda al siguiente símbolo del catálogo.
     * WHEN: con {blue, brown}, se llama spin(1, 1).
     * THEN: la rueda 1 debe mostrar "purple" (siguiente a blue).
     */
    @Test
    public void testSpinStepsOneStepAdvancesToNextSymbol() {
        crearRuedas(2);
        fijar("blue", "brown");
        mach1.spin(1, 1);
        assertTrue(mach1.ok());
        assertEquals("purple", mach1.configuration()[0]);
    }

    /**
     * QUÉ SE PRUEBA: que al pasar el último símbolo del catálogo se vuelve al primero.
     * WHEN: con {brown, green}, se llama spin(1, 1).
     * THEN: la rueda 1 debe mostrar "blue".
     */
    @Test
    public void testSpinStepsWrapsAroundCatalog() {
        crearRuedas(2);
        fijar("brown", "green");
        mach1.spin(1, 1);
        assertEquals("blue", mach1.configuration()[0]);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede avanzar pasos en una rueda bloqueada.
     * WHEN: se bloquea la rueda 1 y se llama spin(1, 2).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinStepsLockedWheelRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(1);
        String[] before = mach1.configuration();
        mach1.spin(1, 2);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede avanzar pasos una vez logrado el Jackpot.
     * WHEN: se logra {blue, blue} y luego se llama spin(1, 1).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinStepsRejectedAfterJackpot() {
        crearRuedas(2);
        fijar("blue", "blue");
        String[] before = mach1.configuration();
        mach1.spin(1, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que una posición de rueda inexistente se rechaza.
     * WHEN: con 2 ruedas, se llama spin(0, 1).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinStepsInvalidWheelRejected() {
        crearRuedas(2);
        String[] before = mach1.configuration();
        mach1.spin(0, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    // ---------------- spin(String[]) ----------------

    /**
     * QUÉ SE PRUEBA: que la máquina queda exactamente en la configuración pedida.
     * WHEN: con 3 ruedas sin Jackpot, se llama spin({blue, purple, green}).
     * THEN: ok() true y configuration() debe ser {blue, purple, green}.
     */
    @Test
    public void testSpinArrayAppliesExactConfiguration() {
        crearRuedas(3);
        mach1.spin(new String[]{"blue", "purple", "green"});
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple", "green"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede pasar un arreglo nulo como configuración deseada.
     * WHEN: se llama spin((String[]) null).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testSpinArrayNullRejected() {
        mach1.addWheel(1);
        mach1.spin((String[]) null);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que el arreglo debe tener tantos elementos como ruedas; si es más corto, se rechaza.
     * WHEN: con 2 ruedas, se llama spin({blue}).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinArrayTooShortRejected() {
        crearRuedas(2);
        String[] before = mach1.configuration();
        mach1.spin(new String[]{"blue"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que si algún color no existe en el catálogo se rechaza todo sin cambios parciales.
     * WHEN: con 2 ruedas, se llama spin({blue, nonexistent}).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinArrayInvalidColorRejected() {
        crearRuedas(2);
        String[] before = mach1.configuration();
        mach1.spin(new String[]{"blue", "nonexistent"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que si alguna rueda está bloqueada, toda la operación se rechaza.
     * WHEN: se bloquea la rueda 1 y se llama spin({green, brown}).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinArrayRejectedWhenWheelLocked() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(1);
        String[] before = mach1.configuration();
        mach1.spin(new String[]{"green", "brown"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    // ---------------- configuration ----------------

    /**
     * QUÉ SE PRUEBA: que la longitud de configuration() coincide con el número de ruedas.
     * WHEN: se agregan 3 ruedas.
     * THEN: configuration().length debe ser 3.
     */
    @Test
    public void testConfigurationLengthMatchesWheelCount() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que configuration() refleja el intercambio de dos ruedas.
     * WHEN: con {blue, purple, green, brown}, se hace swap(2, 3).
     * THEN: configuration() debe devolver {blue, green, purple, brown}.
     */
    @Test
    public void testConfigurationAfterSwap() {
        crearRuedas(4);
        fijar("blue", "purple", "green", "brown");
        mach1.swap(2, 3);
        assertArrayEquals(new String[]{"blue", "green", "purple", "brown"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que el arreglo devuelto es independiente del estado interno.
     * WHEN: se modifica el arreglo devuelto por configuration().
     * THEN: una nueva consulta debe devolver los valores originales.
     */
    @Test
    public void testConfigurationReturnsIndependentArray() {
        crearRuedas(2);
        fijar("blue", "purple");
        String[] copia = mach1.configuration();
        copia[0] = "hack";
        assertEquals("blue", mach1.configuration()[0]);
    }

    /**
     * QUÉ SE PRUEBA: que una rueda sin símbolo se reporta con cadena vacía.
     * WHEN: se vacía el catálogo y se agrega una rueda.
     * THEN: configuration() debe ser {""}.
     */
    @Test
    public void testConfigurationEmptyStringWhenWheelHasNoSymbol() {
        vaciarCatalogo();
        mach1.addWheel(1);
        assertArrayEquals(new String[]{""}, mach1.configuration());
    }

    // ---------------- distinctSymbols ----------------

    /**
     * QUÉ SE PRUEBA: que sin ruedas no hay símbolos distintos que contar.
     * WHEN: se consulta distinctSymbols() en una máquina sin ruedas.
     * THEN: el resultado debe ser 0.
     */
    @Test
    public void testDistinctSymbolsZeroWheels() {
        assertEquals(0, mach1.distinctSymbols());
    }

    /**
     * QUÉ SE PRUEBA: que cuenta bien cuando algunas ruedas repiten el color y otras no.
     * WHEN: con {blue, blue, green}, se consulta distinctSymbols().
     * THEN: el resultado debe ser 2.
     */
    @Test
    public void testDistinctSymbolsSomeSame() {
        crearRuedas(3);
        fijar("blue", "blue", "green");
        assertEquals(2, mach1.distinctSymbols());
    }

    /**
     * QUÉ SE PRUEBA: que devuelve 1 cuando todas las ruedas muestran el mismo color.
     * WHEN: con {blue, blue}, se consulta distinctSymbols().
     * THEN: el resultado debe ser 1.
     */
    @Test
    public void testDistinctSymbolsAllSame() {
        crearRuedas(2);
        fijar("blue", "blue");
        assertEquals(1, mach1.distinctSymbols());
    }

    /**
     * QUÉ SE PRUEBA: que las ruedas sin símbolo no se cuentan.
     * WHEN: se vacía el catálogo y se agregan 2 ruedas sin símbolo.
     * THEN: distinctSymbols() debe ser 0.
     */
    @Test
    public void testDistinctSymbolsIgnoresWheelsWithoutSymbol() {
        vaciarCatalogo();
        mach1.addWheel(1);
        mach1.addWheel(2);
        assertEquals(0, mach1.distinctSymbols());
    }

    // ---------------- symbols ----------------

    /**
     * QUÉ SE PRUEBA: que un símbolo agregado aparece luego en symbols().
     * WHEN: se llama addSymbol(1, "red").
     * THEN: symbols() debe contener "red".
     */
    @Test
    public void testSymbolsAfterAdd() {
        mach1.addSymbol(1, "red");
        assertTrue(Arrays.asList(mach1.symbols()).contains("red"));
    }

    /**
     * QUÉ SE PRUEBA: que un símbolo eliminado ya no aparece en symbols().
     * WHEN: se llama delSymbol("green").
     * THEN: symbols() ya no debe contener "green".
     */
    @Test
    public void testSymbolsAfterDelete() {
        mach1.delSymbol("green");
        assertFalse(Arrays.asList(mach1.symbols()).contains("green"));
    }

    /**
     * QUÉ SE PRUEBA: que el catálogo nunca queda con colores duplicados.
     * WHEN: se intenta addSymbol(1, "blue") cuando "blue" ya existe.
     * THEN: debe haber exactamente una aparición de "blue" (sin distinguir mayúsculas).
     */
    @Test
    public void testSymbolsNoDuplicateColors() {
        mach1.addSymbol(1, "blue");
        long count = Arrays.stream(mach1.symbols()).filter(c -> c.equalsIgnoreCase("blue")).count();
        assertEquals(1, count);
    }

    /**
     * QUÉ SE PRUEBA: que el arreglo devuelto es independiente del catálogo interno.
     * WHEN: se modifica el arreglo devuelto por symbols().
     * THEN: una nueva consulta debe devolver el catálogo original.
     */
    @Test
    public void testSymbolsReturnsIndependentArray() {
        String[] copia = mach1.symbols();
        copia[0] = "hack";
        assertEquals("blue", mach1.symbols()[0]);
    }

    // ---------------- isJackpot ----------------

    /**
     * QUÉ SE PRUEBA: que no puede haber Jackpot si no hay ninguna rueda.
     * WHEN: se consulta isJackpot() en una máquina sin ruedas.
     * THEN: el resultado debe ser false.
     */
    @Test
    public void testIsJackpotFalseWithZeroWheels() {
        assertFalse(mach1.isJackpot());
    }

    /**
     * QUÉ SE PRUEBA: que no puede haber Jackpot con una sola rueda.
     * WHEN: se agrega 1 rueda y se consulta isJackpot().
     * THEN: el resultado debe ser false.
     */
    @Test
    public void testIsJackpotFalseWithOneWheel() {
        mach1.addWheel(1);
        assertFalse(mach1.isJackpot());
    }

    /**
     * QUÉ SE PRUEBA: que se detecta el Jackpot cuando todas las ruedas muestran el mismo símbolo.
     * WHEN: con {blue, blue}, se consulta isJackpot().
     * THEN: el resultado debe ser true.
     */
    @Test
    public void testIsJackpotTrueWhenAllSame() {
        crearRuedas(2);
        fijar("blue", "blue");
        assertTrue(mach1.isJackpot());
    }

    /**
     * QUÉ SE PRUEBA: que no hay Jackpot cuando las ruedas muestran símbolos distintos.
     * WHEN: con {blue, purple, green}, se consulta isJackpot().
     * THEN: el resultado debe ser false.
     */
    @Test
    public void testIsJackpotFalseWhenDifferent() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        assertFalse(mach1.isJackpot());
    }

    /**
     * QUÉ SE PRUEBA: que al ganar el Jackpot todas las ruedas quedan desbloqueadas.
     * WHEN: se bloquea la rueda 1, se coloca el mismo color en ambas ruedas y se intenta bloquear de nuevo la rueda 1.
     * THEN: isJackpot() true y el nuevo lock(1) tiene éxito (ok() true).
     */
    @Test
    public void testJackpotAutoUnlocksLockedWheel() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(1);
        mach1.placeSymbol(2, "blue");
        assertTrue(mach1.isJackpot());
        mach1.lock(1);
        assertTrue(mach1.ok());
    }

    // ---------------- ok() ----------------

    /**
     * QUÉ SE PRUEBA: que ok() refleja el resultado de la última operación.
     * WHEN: addWheel(1) válido, addWheel(0) inválido, delWheel(1) válido, delWheel(1) inválido.
     * THEN: ok() debe ser true, false, true, false respectivamente.
     */
    @Test
    public void testOkTracksLastOperationResult() {
        mach1.addWheel(1);
        assertTrue(mach1.ok());
        mach1.addWheel(0);
        assertFalse(mach1.ok());
        mach1.delWheel(1);
        assertTrue(mach1.ok());
        mach1.delWheel(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que ok() vuelve a true tras una operación exitosa posterior a un error.
     * WHEN: addWheel(0) falla y luego addWheel(1) tiene éxito.
     * THEN: ok() debe ser false y luego true.
     */
    @Test
    public void testOkRecoversAfterSuccessfulOperation() {
        mach1.addWheel(0);
        assertFalse(mach1.ok());
        mach1.addWheel(1);
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que las consultas no modifican ok().
     * WHEN: tras una operación fallida, se llama configuration(), symbols(), distinctSymbols() e isJackpot().
     * THEN: ok() debe seguir siendo false.
     */
    @Test
    public void testOkNotModifiedByQueries() {
        mach1.addWheel(0);
        mach1.configuration();
        mach1.symbols();
        mach1.distinctSymbols();
        mach1.isJackpot();
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que ok() alterna correctamente con lock y unlock.
     * WHEN: lock(1), lock(1), unlock(1), unlock(1) sobre una rueda.
     * THEN: ok() debe ser true, false, true, false.
     */
    @Test
    public void testOkLockUnlockAlternation() {
        mach1.addWheel(1);
        mach1.lock(1);
        assertTrue(mach1.ok());
        mach1.lock(1);
        assertFalse(mach1.ok());
        mach1.unlock(1);
        assertTrue(mach1.ok());
        mach1.unlock(1);
        assertFalse(mach1.ok());
    }

    // ---------------- swap ----------------

    /**
     * QUÉ SE PRUEBA: que se pueden intercambiar la primera y la segunda rueda.
     * WHEN: con {blue, purple, green}, se llama swap(1, 2).
     * THEN: ok() true y la configuración debe quedar {purple, blue, green}.
     */
    @Test
    public void testSwapFirstAndSecond() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        mach1.swap(1, 2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"purple", "blue", "green"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que intercambiar una rueda consigo misma no altera la configuración.
     * WHEN: con {blue, purple, green, brown}, se llama swap(2, 2).
     * THEN: ok() true y la configuración permanece igual.
     */
    @Test
    public void testSwapSelfDoesNotChangeConfiguration() {
        crearRuedas(4);
        fijar("blue", "purple", "green", "brown");
        mach1.swap(2, 2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple", "green", "brown"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede intercambiar usando la posición 0.
     * WHEN: se llama swap(0, 1) con ruedas configuradas.
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSwapIndexZeroRejected() {
        crearRuedas(2);
        String[] before = mach1.configuration();
        mach1.swap(0, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede hacer swap si la máquina no tiene al menos 2 ruedas.
     * WHEN: con solo 1 rueda, se llama swap(1, 1).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testSwapWithTooFewWheelsRejected() {
        mach1.addWheel(1);
        mach1.swap(1, 1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede intercambiar una rueda bloqueada.
     * WHEN: se bloquea la rueda 2 y se llama swap(1, 2).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSwapLockedWheelRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(2);
        mach1.swap(1, 2);
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    // ---------------- lock ----------------

    /**
     * QUÉ SE PRUEBA: que se puede bloquear una rueda intermedia.
     * WHEN: con 3 ruedas, se llama lock(2).
     * THEN: ok() debe ser true.
     */
    @Test
    public void testLockMiddleWheel() {
        crearRuedas(3);
        mach1.lock(2);
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede bloquear dos veces la misma rueda.
     * WHEN: se llama lock(1) dos veces seguidas.
     * THEN: ok() debe ser false en el segundo intento.
     */
    @Test
    public void testLockAlreadyLockedRejected() {
        mach1.addWheel(1);
        mach1.lock(1);
        mach1.lock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede bloquear la posición 0.
     * WHEN: con 1 rueda, se llama lock(0).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testLockIndexZeroRejected() {
        mach1.addWheel(1);
        mach1.lock(0);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede bloquear una posición mayor a la cantidad de ruedas.
     * WHEN: con 1 rueda, se llama lock(2).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testLockIndexTooHighRejected() {
        mach1.addWheel(1);
        mach1.lock(2);
        assertFalse(mach1.ok());
    }

    // ---------------- unlock ----------------

    /**
     * QUÉ SE PRUEBA: que se puede desbloquear una rueda previamente bloqueada.
     * WHEN: se bloquea la rueda 1 y luego se llama unlock(1).
     * THEN: ok() debe ser true.
     */
    @Test
    public void testUnlockLockedWheelSucceeds() {
        mach1.addWheel(1);
        mach1.lock(1);
        mach1.unlock(1);
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede desbloquear una rueda que ya está libre.
     * WHEN: se llama unlock(1) sobre una rueda nunca bloqueada.
     * THEN: ok() debe ser false.
     */
    @Test
    public void testUnlockAlreadyUnlockedRejected() {
        mach1.addWheel(1);
        mach1.unlock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede desbloquear usando una posición inválida.
     * WHEN: con 1 rueda, se llama unlock(0).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testUnlockInvalidIndexRejected() {
        mach1.addWheel(1);
        mach1.unlock(0);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que una rueda desbloqueada vuelve a poder girar.
     * WHEN: se bloquea y desbloquea la rueda 1 y se llama spin(1).
     * THEN: ok() debe ser true.
     */
    @Test
    public void testUnlockedWheelCanSpinAgain() {
        crearRuedas(2);
        mach1.lock(1);
        mach1.unlock(1);
        mach1.spin(1);
        assertTrue(mach1.ok());
    }
}