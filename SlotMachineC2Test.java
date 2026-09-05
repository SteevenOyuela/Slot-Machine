import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias de la clase SlotMachine.
 * Cada prueba documenta: qué se busca probar, qué acción se ejecuta (When)
 * y qué resultado se espera obtener (Then).
 */
public class SlotMachineC2Test {

    private SlotMachine mach1;

    @Before
    public void setUp() {
        mach1 = new SlotMachine();
        mach1.makeInvisible();
    }

    // ---------------- ESTADO INICIAL ----------------

    /**
     * QUÉ SE PRUEBA: que una máquina recién creada no tiene ruedas.
     * WHEN: se consulta configuration() sin haber agregado ninguna rueda.
     * THEN: el arreglo devuelto debe tener longitud 0.
     */
    @Test
    public void testInitialConfigurationIsEmpty() {
        assertEquals(0, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que sin ruedas no hay símbolos distintos que contar.
     * WHEN: se consulta distinctSymbols() en una máquina sin ruedas.
     * THEN: el resultado debe ser 0.
     */
    @Test
    public void testInitialDistinctSymbolsIsZero() {
        assertEquals(0, mach1.distinctSymbols());
    }

    /**
     * QUÉ SE PRUEBA: que el catálogo de símbolos inicial trae 6 símbolos por defecto.
     * WHEN: se consulta symbols() justo después de crear la máquina.
     * THEN: el arreglo devuelto debe tener longitud 6.
     */
    @Test
    public void testInitialCatalogHasSixSymbols() {
        assertEquals(6, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que el catálogo inicial contiene exactamente los colores por defecto esperados.
     * WHEN: se consulta symbols() justo después de crear la máquina.
     * THEN: el catálogo debe contener maroon, green, blue, purple, yellow y turquoise.
     */
    @Test
    public void testInitialCatalogContainsDefaultColors() {
        java.util.List<String> cat = java.util.Arrays.asList(mach1.symbols());
        assertTrue(cat.contains("maroon"));
        assertTrue(cat.contains("green"));
        assertTrue(cat.contains("blue"));
        assertTrue(cat.contains("purple"));
        assertTrue(cat.contains("yellow"));
        assertTrue(cat.contains("turquoise"));
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
        assertEquals(1, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que se puede agregar una rueda al final de la lista existente.
     * WHEN: se agrega la rueda 1 y luego la rueda 2 (al final).
     * THEN: ok() debe ser true y deben quedar 2 ruedas.
     */
    @Test
    public void testAddWheelAtEndSucceeds() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        assertTrue(mach1.ok());
        assertEquals(2, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que se pueden agregar varias ruedas seguidas sin errores.
     * WHEN: se agregan 3 ruedas consecutivas en las posiciones 1, 2 y 3.
     * THEN: ok() debe ser true y deben quedar 3 ruedas.
     */
    @Test
    public void testAddMultipleWheelsConsecutively() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        assertTrue(mach1.ok());
        assertEquals(3, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: el caso límite de agregar una rueda justo en la posición
     * "tamaño actual + 1", que es la última posición válida permitida.
     * WHEN: con 2 ruedas ya creadas, se agrega una rueda en la posición (tamaño + 1).
     * THEN: ok() debe ser true y deben quedar 3 ruedas.
     */
    @Test
    public void testAddWheelBoundaryPositionSizePlusOneSucceeds() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        int max = mach1.configuration().length + 1;
        mach1.addWheel(max);
        assertTrue(mach1.ok());
        assertEquals(3, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede agregar una rueda en la posición 0 (fuera de rango).
     * WHEN: se intenta addWheel(0) habiendo ya una rueda.
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testAddWheelPositionZeroRejected() {
        mach1.addWheel(1);
        int before = mach1.configuration().length;
        mach1.addWheel(0);
        assertFalse(mach1.ok());
        assertEquals(before, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede agregar una rueda en una posición negativa.
     * WHEN: se intenta addWheel(-3) habiendo ya una rueda.
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testAddWheelNegativePositionRejected() {
        mach1.addWheel(1);
        int before = mach1.configuration().length;
        mach1.addWheel(-3);
        assertFalse(mach1.ok());
        assertEquals(before, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede agregar una rueda en una posición mayor a la máxima válida.
     * WHEN: se intenta agregar en la posición (cantidad actual + 2), que excede el límite permitido.
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testAddWheelPositionTooHighRejected() {
        mach1.addWheel(1);
        int before = mach1.configuration().length;
        mach1.addWheel(before + 2);
        assertFalse(mach1.ok());
        assertEquals(before, mach1.configuration().length);
    }

    // ---------------- delWheel ----------------

    /**
     * QUÉ SE PRUEBA: que se puede eliminar la primera rueda y las demás se recorren correctamente.
     * WHEN: con 2 ruedas configuradas (maroon, green), se elimina la rueda en la posición 1.
     * THEN: ok() debe ser true y debe quedar solo la rueda "green".
     */
    @Test
    public void testDelWheelFirstSucceeds() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.delWheel(1);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"green"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que se puede eliminar la última rueda de la máquina.
     * WHEN: con 2 ruedas configuradas (maroon, green), se elimina la rueda en la posición 2.
     * THEN: ok() debe ser true y debe quedar solo la rueda "maroon".
     */
    @Test
    public void testDelWheelLastSucceeds() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.delWheel(2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"maroon"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que se puede eliminar una rueda intermedia y las de los extremos se conservan.
     * WHEN: con 3 ruedas configuradas (maroon, green, blue), se elimina la rueda en la posición 2.
     * THEN: ok() debe ser true y deben quedar solo "maroon" y "blue", en ese orden.
     */
    @Test
    public void testDelWheelMiddleSucceeds() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.spin(new String[]{"maroon", "green", "blue"});
        mach1.delWheel(2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"maroon", "blue"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que eliminando ruedas repetidamente en la posición 1 se puede vaciar la máquina.
     * WHEN: se agregan 2 ruedas y se elimina dos veces seguidas la rueda en la posición 1.
     * THEN: ok() debe ser true y la máquina debe quedar con 0 ruedas.
     */
    @Test
    public void testDelWheelUntilEmpty() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.delWheel(1);
        mach1.delWheel(1);
        assertTrue(mach1.ok());
        assertEquals(0, mach1.configuration().length);
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
     * QUÉ SE PRUEBA: que no se puede eliminar la rueda en la posición 0 (fuera de rango).
     * WHEN: existiendo una rueda, se llama delWheel(0).
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testDelWheelPositionZeroRejected() {
        mach1.addWheel(1);
        int before = mach1.configuration().length;
        mach1.delWheel(0);
        assertFalse(mach1.ok());
        assertEquals(before, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede eliminar una rueda usando una posición negativa.
     * WHEN: existiendo una rueda, se llama delWheel(-1).
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testDelWheelNegativePositionRejected() {
        mach1.addWheel(1);
        int before = mach1.configuration().length;
        mach1.delWheel(-1);
        assertFalse(mach1.ok());
        assertEquals(before, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede eliminar una rueda en una posición mayor a las existentes.
     * WHEN: existiendo una rueda, se llama delWheel(cantidad actual + 1).
     * THEN: ok() debe ser false y la cantidad de ruedas no debe cambiar.
     */
    @Test
    public void testDelWheelPositionTooHighRejected() {
        mach1.addWheel(1);
        int before = mach1.configuration().length;
        mach1.delWheel(before + 1);
        assertFalse(mach1.ok());
        assertEquals(before, mach1.configuration().length);
    }

    // ---------------- addSymbol ----------------

    /**
     * QUÉ SE PRUEBA: que se puede agregar un símbolo nuevo (color no repetido) al catálogo.
     * WHEN: se llama addSymbol("brown", "circle") con un color que aún no existe.
     * THEN: el tamaño del catálogo debe aumentar exactamente en 1.
     */
    @Test
    public void testAddSymbolValidIncreasesCatalog() {
        int before = mach1.symbols().length;
        mach1.addSymbol("brown", "circle");
        assertEquals(before + 1, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que no se puede duplicar un símbolo si el color ya existe con la misma forma.
     * WHEN: se intenta addSymbol("maroon", "circle"), color y forma ya presentes en el catálogo.
     * THEN: el tamaño del catálogo no debe cambiar.
     */
    @Test
    public void testAddSymbolDuplicateColorSameShapeCatalogUnchanged() {
        int before = mach1.symbols().length;
        mach1.addSymbol("maroon", "circle");
        assertEquals(before, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que la validación de color duplicado ignora la forma; el color por sí
     * solo ya bloquea la duplicación aunque se use una figura distinta.
     * WHEN: se intenta addSymbol("maroon", "rectangle") cuando "maroon" ya existe con otra forma.
     * THEN: el tamaño del catálogo no debe cambiar.
     */
    @Test
    public void testAddSymbolDuplicateColorDifferentShapeCatalogUnchanged() {
        int before = mach1.symbols().length;
        mach1.addSymbol("maroon", "rectangle");
        assertEquals(before, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que la comparación de colores para detectar duplicados no distingue
     * mayúsculas de minúsculas.
     * WHEN: se intenta addSymbol("MAROON", "circle") cuando "maroon" ya existe en minúsculas.
     * THEN: el tamaño del catálogo no debe cambiar.
     */
    @Test
    public void testAddSymbolDuplicateColorDifferentCaseCatalogUnchanged() {
        int before = mach1.symbols().length;
        mach1.addSymbol("MAROON", "circle");
        assertEquals(before, mach1.symbols().length);
    }

    // ---------------- delSymbol ----------------

    /**
     * QUÉ SE PRUEBA: que se puede eliminar del catálogo un símbolo existente por su color.
     * WHEN: se llama delSymbol("maroon", "circle") cuando "maroon" está en el catálogo.
     * THEN: el catálogo ya no debe contener "maroon" y debe quedar con 5 símbolos.
     */
    @Test
    public void testDelSymbolExistingRemovesFromCatalog() {
        mach1.delSymbol("maroon", "circle");
        assertFalse(java.util.Arrays.asList(mach1.symbols()).contains("maroon"));
        assertEquals(5, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que intentar eliminar un símbolo que no existe no afecta al catálogo.
     * WHEN: se llama delSymbol("nonexistent", "circle") con un color que no está en el catálogo.
     * THEN: el tamaño del catálogo no debe cambiar.
     */
    @Test
    public void testDelSymbolNonExistentDoesNotChangeCatalog() {
        int before = mach1.symbols().length;
        mach1.delSymbol("nonexistent", "circle");
        assertEquals(before, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que la búsqueda del color a eliminar no distingue mayúsculas de minúsculas.
     * WHEN: se llama delSymbol("MAROON", "circle") cuando el catálogo tiene "maroon" en minúsculas.
     * THEN: "maroon" debe quedar eliminado del catálogo.
     */
    @Test
    public void testDelSymbolDifferentCaseRemoves() {
        mach1.delSymbol("MAROON", "circle");
        assertFalse(java.util.Arrays.asList(mach1.symbols()).contains("maroon"));
    }

    /**
     * QUÉ SE PRUEBA: que al eliminar un símbolo, el resto del catálogo permanece intacto.
     * WHEN: se elimina "maroon" del catálogo.
     * THEN: "green" y "blue" deben seguir presentes en el catálogo.
     */
    @Test
    public void testDelSymbolPreservesOtherSymbols() {
        mach1.delSymbol("maroon", "circle");
        java.util.List<String> cat = java.util.Arrays.asList(mach1.symbols());
        assertTrue(cat.contains("green"));
        assertTrue(cat.contains("blue"));
    }

    // ---------------- placeSymbol ----------------

    /**
     * QUÉ SE PRUEBA: que no se puede colocar un color que no existe en el catálogo.
     * WHEN: se llama placeSymbol(1, "nonexistent") con un color inexistente.
     * THEN: ok() debe ser false y la configuración de las ruedas no debe cambiar.
     */
    @Test
    public void testPlaceSymbolInvalidColorRejected() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        String[] before = mach1.configuration();
        mach1.placeSymbol(1, "nonexistent");
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede colocar un símbolo en una rueda que está bloqueada.
     * WHEN: se bloquea la rueda 1 y luego se intenta placeSymbol(1, "blue").
     * THEN: ok() debe ser false y la configuración de las ruedas no debe cambiar.
     */
    @Test
    public void testPlaceSymbolOnLockedWheelRejected() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.lock(1);
        String[] before = mach1.configuration();
        mach1.placeSymbol(1, "blue");
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    // ---------------- configuration ----------------

    /**
     * QUÉ SE PRUEBA: que la longitud de configuration() siempre coincide con el número de ruedas.
     * WHEN: se agregan 3 ruedas a la máquina.
     * THEN: configuration().length debe ser igual a 3.
     */
    @Test
    public void testConfigurationLengthMatchesWheelCount() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        assertEquals(3, mach1.configuration().length);
    }

    /**
     * QUÉ SE PRUEBA: que configuration() refleja correctamente el intercambio de dos ruedas.
     * WHEN: con 4 ruedas configuradas como {maroon, green, blue, purple}, se hace swap(2, 3).
     * THEN: configuration() debe devolver {maroon, blue, green, purple}.
     */
    @Test
    public void testConfigurationAfterSwap() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.addWheel(4);
        mach1.spin(new String[]{"maroon", "green", "blue", "purple"});
        mach1.swap(2, 3);
        assertArrayEquals(new String[]{"maroon", "blue", "green", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que al avanzar una rueda exactamente el tamaño completo del catálogo
     * de símbolos, esta vuelve a mostrar el mismo símbolo con el que empezó.
     * WHEN: se coloca "maroon" en la rueda 1 y se hace spin(1, n) con n = cantidad de símbolos.
     * THEN: la rueda 1 debe seguir mostrando "maroon".
     */
    @Test
    public void testConfigurationAfterSpinWithSteps() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.placeSymbol(1, "maroon");
        int n = mach1.symbols().length;
        mach1.spin(1, n);
        assertEquals("maroon", mach1.configuration()[0]);
    }

    /**
     * QUÉ SE PRUEBA: que spin(String[]) deja la configuración exactamente como se solicitó.
     * WHEN: con 2 ruedas, se llama spin(new String[]{"blue", "purple"}).
     * THEN: configuration() debe devolver exactamente {blue, purple}.
     */
    @Test
    public void testConfigurationAfterSpinArray() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"blue", "purple"});
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    // ---------------- symbols ----------------

    /**
     * QUÉ SE PRUEBA: que el catálogo inicial de símbolos tiene 6 elementos.
     * WHEN: se consulta symbols() recién creada la máquina.
     * THEN: el arreglo devuelto debe tener longitud 6.
     */
    @Test
    public void testSymbolsInitialCatalog() {
        assertEquals(6, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: que un símbolo agregado aparece luego en el catálogo devuelto por symbols().
     * WHEN: se llama addSymbol("brown", "circle").
     * THEN: symbols() debe contener "brown".
     */
    @Test
    public void testSymbolsAfterAdd() {
        mach1.addSymbol("brown", "circle");
        assertTrue(java.util.Arrays.asList(mach1.symbols()).contains("brown"));
    }

    /**
     * QUÉ SE PRUEBA: que un símbolo eliminado ya no aparece en el catálogo devuelto por symbols().
     * WHEN: se llama delSymbol("green", "circle").
     * THEN: symbols() ya no debe contener "green".
     */
    @Test
    public void testSymbolsAfterDelete() {
        mach1.delSymbol("green", "circle");
        assertFalse(java.util.Arrays.asList(mach1.symbols()).contains("green"));
    }

    /**
     * QUÉ SE PRUEBA: que el catálogo nunca queda con colores duplicados, incluso si se
     * intenta agregar el mismo color con otra forma.
     * WHEN: se intenta addSymbol("maroon", "triangle") cuando "maroon" ya existe.
     * THEN: debe haber exactamente una sola aparición de "maroon" (sin distinguir mayúsculas) en el catálogo.
     */
    @Test
    public void testSymbolsNoDuplicateColors() {
        mach1.addSymbol("maroon", "triangle");
        long count = java.util.Arrays.stream(mach1.symbols()).filter(c -> c.equalsIgnoreCase("maroon")).count();
        assertEquals(1, count);
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
     * QUÉ SE PRUEBA: que distinctSymbols() cuenta correctamente cuando todas las ruedas
     * muestran colores diferentes entre sí.
     * WHEN: con 3 ruedas configuradas como {maroon, green, blue}, se consulta distinctSymbols().
     * THEN: el resultado debe ser 3.
     */
    @Test
    public void testDistinctSymbolsAllDifferent() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.spin(new String[]{"maroon", "green", "blue"});
        assertEquals(3, mach1.distinctSymbols());
    }

    /**
     * QUÉ SE PRUEBA: que distinctSymbols() cuenta bien cuando algunas ruedas
     * repiten el mismo color y otras no.
     * WHEN: con 3 ruedas configuradas como {maroon, maroon, blue}, se consulta distinctSymbols().
     * THEN: el resultado debe ser 2 (maroon y blue).
     */
    @Test
    public void testDistinctSymbolsSomeSame() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.spin(new String[]{"maroon", "maroon", "blue"});
        assertEquals(2, mach1.distinctSymbols());
    }

    /**
     * QUÉ SE PRUEBA: que distinctSymbols() devuelve 1 cuando todas las ruedas muestran
     * el mismo color (condición base del Jackpot).
     * WHEN: con 2 ruedas configuradas como {maroon, maroon}, se consulta distinctSymbols().
     * THEN: el resultado debe ser 1.
     */
    @Test
    public void testDistinctSymbolsAllSame() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "maroon"});
        assertEquals(1, mach1.distinctSymbols());
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
     * QUÉ SE PRUEBA: que no puede haber Jackpot con una sola rueda (se necesitan al menos 2).
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
     * WHEN: con 2 ruedas configuradas como {blue, blue}, se consulta isJackpot().
     * THEN: el resultado debe ser true.
     */
    @Test
    public void testIsJackpotTrueWhenAllSame() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"blue", "blue"});
        assertTrue(mach1.isJackpot());
    }

    /**
     * QUÉ SE PRUEBA: que al ganar el Jackpot todas las ruedas quedan desbloqueadas
     * automáticamente, incluso si una estaba bloqueada antes de completar la jugada ganadora.
     * WHEN: se bloquea la rueda 1, luego se coloca el mismo color en ambas ruedas
     * logrando el Jackpot, y después se intenta bloquear la rueda 1 de nuevo.
     * THEN: isJackpot() debe ser true, y el nuevo intento de lock(1) debe tener éxito
     * (ok() true), lo cual confirma que la rueda quedó desbloqueada por el Jackpot.
     */
    @Test
    public void testJackpotAutoUnlocksLockedWheel() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.placeSymbol(1, "maroon");
        mach1.lock(1);
        mach1.placeSymbol(2, "maroon");
        assertTrue(mach1.isJackpot());
        mach1.lock(1);
        assertTrue(mach1.ok());
    }

    // ---------------- spin() ----------------

    /**
     * QUÉ SE PRUEBA: que no se puede girar la máquina si no tiene ruedas.
     * WHEN: se llama spin() sin haber agregado ninguna rueda.
     * THEN: ok() debe ser false.
     */
    @Test
    public void testSpinNoWheelsRejected() {
        mach1.spin();
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que girar la máquina funciona correctamente cuando todas las ruedas
     * están desbloqueadas.
     * WHEN: con 2 ruedas configuradas y sin bloquear, se llama spin().
     * THEN: ok() debe ser true.
     */
    @Test
    public void testSpinWithUnlockedWheelsSucceeds() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.spin();
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que una vez logrado el Jackpot, ya no se permite seguir girando la máquina.
     * WHEN: se logra el Jackpot con {blue, blue} y luego se llama spin() de nuevo.
     * THEN: isJackpot() debe haber sido true y el spin() posterior debe fallar (ok() false).
     */
    @Test
    public void testSpinRejectedAfterJackpot() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"blue", "blue"});
        assertTrue(mach1.isJackpot());
        mach1.spin();
        assertFalse(mach1.ok());
    }

    // ---------------- spin(int wheel) ----------------

    /**
     * QUÉ SE PRUEBA: que se puede girar una única rueda específica indicando su posición válida.
     * WHEN: con 3 ruedas configuradas, se llama spin(2) para girar solo la rueda 2.
     * THEN: ok() debe ser true.
     */
    @Test
    public void testSpinWheelValidIndexSucceeds() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.spin(new String[]{"maroon", "green", "blue"});
        mach1.spin(2);
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede girar una rueda específica si está bloqueada.
     * WHEN: se bloquea la rueda 1 y luego se llama spin(1).
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSpinWheelLockedRejected() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.lock(1);
        String[] before = mach1.configuration();
        mach1.spin(1);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede girar una rueda individual una vez logrado el Jackpot.
     * WHEN: se logra el Jackpot con {blue, blue} y luego se llama spin(1).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testSpinWheelRejectedAfterJackpot() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"blue", "blue"});
        assertTrue(mach1.isJackpot());
        mach1.spin(1);
        assertFalse(mach1.ok());
    }

    // ---------------- swap ----------------

    /**
     * QUÉ SE PRUEBA: que se pueden intercambiar la primera y la segunda rueda correctamente.
     * WHEN: con {maroon, green, blue} configurado, se llama swap(1, 2).
     * THEN: ok() debe ser true y la configuración debe quedar {green, maroon, blue}.
     */
    @Test
    public void testSwapFirstAndSecond() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.spin(new String[]{"maroon", "green", "blue"});
        mach1.swap(1, 2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"green", "maroon", "blue"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que se pueden intercambiar dos ruedas intermedias (segunda y tercera).
     * WHEN: con {maroon, green, blue, purple} configurado, se llama swap(2, 3).
     * THEN: ok() debe ser true y la configuración debe quedar {maroon, blue, green, purple}.
     */
    @Test
    public void testSwapSecondAndThird() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.addWheel(4);
        mach1.spin(new String[]{"maroon", "green", "blue", "purple"});
        mach1.swap(2, 3);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"maroon", "blue", "green", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que intercambiar una rueda consigo misma no altera la configuración.
     * WHEN: con {maroon, green, blue, purple} configurado, se llama swap(2, 2).
     * THEN: ok() debe ser true y la configuración debe permanecer exactamente igual.
     */
    @Test
    public void testSwapSelfDoesNotChangeConfiguration() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.addWheel(4);
        mach1.spin(new String[]{"maroon", "green", "blue", "purple"});
        mach1.swap(2, 2);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"maroon", "green", "blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede intercambiar usando la posición 0 (fuera de rango).
     * WHEN: se llama swap(0, 1) con ruedas ya configuradas.
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSwapIndexZeroRejected() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        String[] before = mach1.configuration();
        mach1.swap(0, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede intercambiar usando una posición negativa.
     * WHEN: se llama swap(-1, 1) con ruedas ya configuradas.
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSwapNegativeIndexRejected() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        String[] before = mach1.configuration();
        mach1.swap(-1, 1);
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

    // ---------------- lock / unlock ----------------

    /**
     * QUÉ SE PRUEBA: que se puede bloquear la primera rueda de la máquina.
     * WHEN: con 2 ruedas creadas, se llama lock(1).
     * THEN: ok() debe ser true.
     */
    @Test
    public void testLockFirstWheel() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.lock(1);
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que se puede bloquear la última rueda de la máquina.
     * WHEN: con 2 ruedas creadas, se llama lock(2).
     * THEN: ok() debe ser true.
     */
    @Test
    public void testLockLastWheel() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.lock(2);
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que se puede bloquear una rueda intermedia.
     * WHEN: con 3 ruedas creadas, se llama lock(2).
     * THEN: ok() debe ser true.
     */
    @Test
    public void testLockMiddleWheel() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addWheel(3);
        mach1.lock(2);
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede bloquear dos veces la misma rueda que ya está bloqueada.
     * WHEN: se llama lock(1) dos veces seguidas sobre la misma rueda.
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
     * QUÉ SE PRUEBA: que no se puede bloquear la posición 0 (fuera de rango).
     * WHEN: con 1 rueda creada, se llama lock(0).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testLockIndexZeroRejected() {
        mach1.addWheel(1);
        mach1.lock(0);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede bloquear usando una posición negativa.
     * WHEN: con 1 rueda creada, se llama lock(-1).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testLockNegativeIndexRejected() {
        mach1.addWheel(1);
        mach1.lock(-1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede bloquear una posición mayor a la cantidad de ruedas existentes.
     * WHEN: con solo 1 rueda creada, se llama lock(2).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testLockIndexTooHighRejected() {
        mach1.addWheel(1);
        mach1.lock(2);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede bloquear ninguna rueda si la máquina no tiene ruedas.
     * WHEN: se llama lock(1) sin haber agregado ruedas.
     * THEN: ok() debe ser false.
     */
    @Test
    public void testLockWithNoWheelsRejected() {
        mach1.lock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que se puede desbloquear una rueda que estaba previamente bloqueada.
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
     * QUÉ SE PRUEBA: que no se puede desbloquear una rueda que ya está libre (no bloqueada).
     * WHEN: se llama unlock(1) sobre una rueda que nunca fue bloqueada.
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
     * WHEN: con 1 rueda creada, se llama unlock(0).
     * THEN: ok() debe ser false.
     */
    @Test
    public void testUnlockInvalidIndexRejected() {
        mach1.addWheel(1);
        mach1.unlock(0);
        assertFalse(mach1.ok());
    }

    // ---------------- lock + spin interaction ----------------

    /**
     * QUÉ SE PRUEBA: que una rueda bloqueada mantiene su símbolo al ejecutar un spin() general,
     * incluso si es la última rueda de la máquina.
     * WHEN: se fija {maroon, green}, se bloquea la rueda 2 y se llama spin().
     * THEN: la rueda 2 debe seguir mostrando "green".
     */
    @Test
    public void testLockedWheelKeepsSymbolAfterSpin() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.lock(2);
        mach1.spin();
        assertEquals("green", mach1.configuration()[1]);
    }

    /**
     * QUÉ SE PRUEBA: que una rueda que fue bloqueada y luego desbloqueada puede volver a girar.
     * WHEN: se bloquea la rueda 1, se desbloquea, y luego se llama spin(1).
     * THEN: ok() debe ser true.
     */
    @Test
    public void testUnlockedWheelCanBeSpunAgain() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.lock(1);
        mach1.unlock(1);
        mach1.spin(1);
        assertTrue(mach1.ok());
    }

    // ---------------- spin(int wheel, int steps) ----------------

    /**
     * QUÉ SE PRUEBA: que no se puede avanzar pasos en una rueda que está bloqueada.
     * WHEN: se bloquea la rueda 1 y luego se llama spin(1, 2).
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSpinStepsLockedWheelRejected() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.lock(1);
        String[] before = mach1.configuration();
        mach1.spin(1, 2);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que no se puede avanzar pasos en ninguna rueda una vez logrado el Jackpot.
     * WHEN: se logra el Jackpot con {maroon, maroon} y luego se llama spin(1, 1).
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSpinStepsRejectedAfterJackpot() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "maroon"});
        assertTrue(mach1.isJackpot());
        String[] before = mach1.configuration();
        mach1.spin(1, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    // ---------------- spin(String[]) ----------------

    /**
     * QUÉ SE PRUEBA: que spin(String[]) puede dejar una configuración ganadora válida.
     * WHEN: se llama spin(new String[]{"blue", "blue"}) sobre 2 ruedas.
     * THEN: ok() debe ser true y la máquina debe quedar en estado de Jackpot.
     */
    @Test
    public void testSpinArrayValidWinning() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"blue", "blue"});
        assertTrue(mach1.ok());
        assertTrue(mach1.isJackpot());
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
     * QUÉ SE PRUEBA: que no se puede pasar un arreglo vacío como configuración deseada
     * cuando sí existen ruedas en la máquina.
     * WHEN: se llama spin(new String[]{}) con 1 rueda ya creada.
     * THEN: ok() debe ser false.
     */
    @Test
    public void testSpinArrayEmptyRejected() {
        mach1.addWheel(1);
        mach1.spin(new String[]{});
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que el arreglo de configuración debe tener exactamente la misma
     * cantidad de elementos que ruedas existen; si es más corto, se rechaza.
     * WHEN: con 2 ruedas creadas, se llama spin(new String[]{"maroon"}) (solo 1 elemento).
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSpinArrayTooShortRejected() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        String[] before = mach1.configuration();
        mach1.spin(new String[]{"maroon"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que el arreglo de configuración se rechaza si tiene más elementos
     * que ruedas existen.
     * WHEN: con solo 1 rueda creada, se llama spin(new String[]{"maroon", "green"}) (2 elementos).
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSpinArrayTooLongRejected() {
        mach1.addWheel(1);
        String[] before = mach1.configuration();
        mach1.spin(new String[]{"maroon", "green"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que si algún color del arreglo no existe en el catálogo, toda la
     * operación se rechaza sin aplicar cambios parciales.
     * WHEN: se llama spin(new String[]{"maroon", "nonexistent"}) con un color inválido.
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSpinArrayInvalidColorRejected() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        String[] before = mach1.configuration();
        mach1.spin(new String[]{"maroon", "nonexistent"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que la búsqueda de colores en el catálogo no distingue mayúsculas
     * de minúsculas al aplicar spin(String[]).
     * WHEN: se llama spin(new String[]{"MAROON", "GREEN"}) usando mayúsculas.
     * THEN: ok() debe ser true.
     */
    @Test
    public void testSpinArrayCaseInsensitiveColorAccepted() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"MAROON", "GREEN"});
        assertTrue(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que si alguna rueda involucrada está bloqueada, toda la operación
     * de spin(String[]) se rechaza sin aplicar cambios.
     * WHEN: se bloquea la rueda 1 y luego se llama spin(new String[]{"blue", "purple"}).
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSpinArrayRejectedWhenWheelLocked() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"maroon", "green"});
        mach1.lock(1);
        String[] before = mach1.configuration();
        mach1.spin(new String[]{"blue", "purple"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que una vez logrado el Jackpot no se puede aplicar una nueva
     * configuración mediante spin(String[]).
     * WHEN: se logra el Jackpot con {blue, blue} y luego se llama spin(new String[]{"maroon", "green"}).
     * THEN: ok() debe ser false y la configuración no debe cambiar.
     */
    @Test
    public void testSpinArrayRejectedAfterJackpot() {
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.spin(new String[]{"blue", "blue"});
        assertTrue(mach1.isJackpot());
        String[] before = mach1.configuration();
        mach1.spin(new String[]{"maroon", "green"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    // ---------------- ok() ----------------

    /**
     * QUÉ SE PRUEBA: que ok() siempre refleja el resultado de la última operación
     * ejecutada, alternando correctamente entre éxito y fracaso según corresponda.
     * WHEN: se ejecuta una secuencia de operaciones válidas e inválidas intercaladas
     * (addWheel(1) válido, addWheel(0) inválido, delWheel(1) válido, delWheel(1) inválido).
     * THEN: ok() debe reflejar true, false, true, false respectivamente después de cada llamada.
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

    // ---------------- makeVisible / makeInvisible ----------------

    /**
     * QUÉ SE PRUEBA: que la máquina sigue siendo completamente operable (su lógica interna
     * funciona) aunque esté en modo invisible (sin representación gráfica activa).
     * WHEN: estando la máquina en modo invisible (fijado en setUp), se llama addWheel(1).
     * THEN: ok() debe ser true y debe quedar registrada 1 rueda en la configuración.
     */
    @Test
    public void testMachineOperableAfterMakeInvisible() {
        mach1.addWheel(1);
        assertTrue(mach1.ok());
        assertEquals(1, mach1.configuration().length);
    }
}