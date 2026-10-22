import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

/**
 * Pruebas unitarias de refuerzo de cobertura (ciclo 5).
 * Cada prueba documenta: qué se busca probar, qué acción se ejecuta (When)
 * y qué resultado se espera obtener (Then).
 *
 * Las pruebas con la máquina visible provocan JOptionPane modales; un hilo
 * auxiliar los cierra automáticamente para que la prueba no se bloquee.
 * No se prueba exit() ni simulate(n) válido porque terminan la JVM.
 */
public class SlotMachineC5Test {

    private SlotMachine mach1;
    private volatile boolean cerrando;

    @Before
    public void setUp() {
        mach1 = new SlotMachine();
        mach1.makeInvisible();
        cerrando = true;
        Thread cerrador = new Thread(() -> {
            while (cerrando) {
                for (Window w : Window.getWindows()) {
                    if (w instanceof JDialog && w.isShowing()) {
                        SwingUtilities.invokeLater(w::dispose);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        cerrador.setDaemon(true);
        cerrador.start();
    }

    @After
    public void tearDown() {
        cerrando = false;
        mach1.makeInvisible();
    }

    // ---------------- AUXILIARES ----------------

    /** @return la cantidad actual de ruedas. */
    private int ruedas() {
        return mach1.configuration().length;
    }

    /** Reasigna la última rueda hasta romper un Jackpot accidental. */
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

    /** Deja ok() en true sin dejar ruedas en la máquina. */
    private void dejarOkVerdadero() {
        mach1.addWheel(1);
        mach1.delWheel(1);
        assertTrue(mach1.ok());
    }

    /** Muestra la máquina (los diálogos se cierran solos). */
    private void mostrar() {
        mach1.makeVisible();
    }

    // ---------------- addWheel / delWheel (modo visible) ----------------

    /**
     * QUÉ SE PRUEBA: el diálogo de error al agregar en posición inválida.
     * WHEN: con la máquina visible, se llama addWheel(0).
     * THEN: ok() false y no se crean ruedas.
     */
    @Test
    public void testAddWheelInvalidPositionVisibleRejected() {
        mostrar();
        mach1.addWheel(0);
        assertFalse(mach1.ok());
        assertEquals(0, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al agregar sobre una rueda bloqueada.
     * WHEN: se bloquea la rueda 1, se muestra la máquina y se llama addWheel(1).
     * THEN: ok() false y siguen 2 ruedas.
     */
    @Test
    public void testAddWheelLockedPositionVisibleRejected() {
        crearRuedas(2);
        mach1.lock(1);
        mostrar();
        mach1.addWheel(1);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que se puede agregar una rueda al final (posición tamaño + 1).
     * WHEN: con 2 ruedas, se llama addWheel(3).
     * THEN: ok() true y quedan 3 ruedas.
     */
    @Test
    public void testAddWheelAtEndSucceeds() {
        crearRuedas(2);
        mach1.addWheel(3);
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al eliminar en una máquina vacía.
     * WHEN: con la máquina visible y sin ruedas, se llama delWheel(1).
     * THEN: ok() false.
     */
    @Test
    public void testDelWheelEmptyVisibleRejected() {
        mostrar();
        mach1.delWheel(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que la posición 0 no es válida al eliminar.
     * WHEN: con 1 rueda, se llama delWheel(0).
     * THEN: ok() false y la rueda sigue existiendo.
     */
    @Test
    public void testDelWheelPositionZeroRejected() {
        mach1.addWheel(1);
        mach1.delWheel(0);
        assertFalse(mach1.ok());
        assertEquals(1, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al eliminar una posición inexistente.
     * WHEN: con 2 ruedas y la máquina visible, se llama delWheel(5).
     * THEN: ok() false y siguen 2 ruedas.
     */
    @Test
    public void testDelWheelPositionTooHighVisibleRejected() {
        crearRuedas(2);
        mostrar();
        mach1.delWheel(5);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al eliminar una rueda rebelde.
     * WHEN: con una Rebel y la máquina visible, se llama delWheel(1).
     * THEN: ok() false y la rueda sigue existiendo.
     */
    @Test
    public void testDelWheelRebelVisibleRejected() {
        mach1.addRebelWheel(1);
        mostrar();
        mach1.delWheel(1);
        assertFalse(mach1.ok());
        assertEquals(1, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al eliminar una rueda bloqueada.
     * WHEN: se bloquea la rueda 1, se muestra la máquina y se llama delWheel(1).
     * THEN: ok() false y siguen 2 ruedas.
     */
    @Test
    public void testDelWheelLockedVisibleRejected() {
        crearRuedas(2);
        mach1.lock(1);
        mostrar();
        mach1.delWheel(1);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    // ---------------- addSymbol / delSymbol (modo visible) ----------------

    /**
     * QUÉ SE PRUEBA: el diálogo de error al duplicar un color con addSymbol(int, String).
     * WHEN: con la máquina visible, se llama addSymbol(1, "blue").
     * THEN: ok() false y el catálogo mantiene 4 símbolos.
     */
    @Test
    public void testAddSymbolDuplicateVisibleRejected() {
        mostrar();
        mach1.addSymbol(1, "blue");
        assertFalse(mach1.ok());
        assertEquals(4, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al duplicar un color con addSymbol(String, String).
     * WHEN: con la máquina visible, se llama addSymbol("shy", "blue").
     * THEN: ok() false y el catálogo mantiene 4 símbolos.
     */
    @Test
    public void testAddSymbolTypedDuplicateVisibleRejected() {
        mostrar();
        mach1.addSymbol("shy", "blue");
        assertFalse(mach1.ok());
        assertEquals(4, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error por tipo de símbolo desconocido.
     * WHEN: con la máquina visible, se llama addSymbol("magic", "red").
     * THEN: ok() false y el catálogo mantiene 4 símbolos.
     */
    @Test
    public void testAddSymbolTypedUnknownTypeVisibleRejected() {
        mostrar();
        mach1.addSymbol("magic", "red");
        assertFalse(mach1.ok());
        assertEquals(4, mach1.symbols().length);
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al eliminar un símbolo inexistente.
     * WHEN: con la máquina visible, se llama delSymbol("nonexistent").
     * THEN: ok() false y el catálogo mantiene 4 símbolos.
     */
    @Test
    public void testDelSymbolMissingVisibleRejected() {
        mostrar();
        mach1.delSymbol("nonexistent");
        assertFalse(mach1.ok());
        assertEquals(4, mach1.symbols().length);
    }

    // ---------------- spin() ----------------

    /**
     * QUÉ SE PRUEBA: que spin() sin ruedas no hace nada en modo visible.
     * WHEN: con la máquina visible y sin ruedas, se llama spin().
     * THEN: sigue sin haber ruedas.
     */
    @Test
    public void testSpinWithoutWheelsVisibleDoesNothing() {
        mostrar();
        mach1.spin();
        assertEquals(0, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que spin() con ruedas pero sin catálogo no hace nada.
     * WHEN: se vacía el catálogo, se agrega 1 rueda y se llama spin().
     * THEN: la rueda sigue sin símbolo ("").
     */
    @Test
    public void testSpinWithoutSymbolsDoesNothing() {
        vaciarCatalogo();
        mach1.addWheel(1);
        mach1.spin();
        assertEquals("", mach1.configuration()[0]);
    }

    /**
     * QUÉ SE PRUEBA: lo mismo que lo anterior, mostrando el diálogo de error.
     * WHEN: se vacía el catálogo, se agrega 1 rueda, se muestra la máquina y se llama spin().
     * THEN: la rueda sigue sin símbolo ("").
     */
    @Test
    public void testSpinWithoutSymbolsVisibleDoesNothing() {
        vaciarCatalogo();
        mach1.addWheel(1);
        mostrar();
        mach1.spin();
        assertEquals("", mach1.configuration()[0]);
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de rechazo de spin() tras el Jackpot en modo visible.
     * WHEN: con {blue, blue} y la máquina visible, se llama spin().
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinAfterJackpotVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "blue");
        mostrar();
        mach1.spin();
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "blue"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: spin() en modo visible (palanca y animación) respetando una rueda bloqueada.
     * WHEN: con 4 ruedas y la rueda 2 bloqueada, se muestra la máquina y se llama spin().
     * THEN: ok() true y la rueda 2 conserva su color.
     */
    @Test
    public void testSpinVisibleKeepsLockedWheel() {
        crearRuedas(4);
        mach1.lock(2);
        String bloqueada = mach1.configuration()[1];
        mostrar();
        mach1.spin();
        assertTrue(mach1.ok());
        assertEquals(bloqueada, mach1.configuration()[1]);
    }

    // ---------------- placeSymbol ----------------

    /**
     * QUÉ SE PRUEBA: que placeSymbol se rechaza sin ruedas.
     * WHEN: sin ruedas, se llama placeSymbol(1, "blue").
     * THEN: ok() false.
     */
    @Test
    public void testPlaceSymbolWithoutWheelsRejected() {
        dejarOkVerdadero();
        mach1.placeSymbol(1, "blue");
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que placeSymbol se rechaza si el catálogo está vacío.
     * WHEN: se vacía el catálogo, se agrega 1 rueda y se llama placeSymbol(1, "blue").
     * THEN: ok() false.
     */
    @Test
    public void testPlaceSymbolWithoutCatalogRejected() {
        vaciarCatalogo();
        mach1.addWheel(1);
        mach1.placeSymbol(1, "blue");
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que una posición menor a 1 usa la primera rueda.
     * WHEN: con {blue, purple}, se llama placeSymbol(0, "green").
     * THEN: ok() true y la configuración es {green, purple}.
     */
    @Test
    public void testPlaceSymbolPositionBelowRangeUsesFirstWheel() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.placeSymbol(0, "green");
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"green", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de rechazo de placeSymbol tras el Jackpot.
     * WHEN: con {blue, blue} y la máquina visible, se llama placeSymbol(1, "green").
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testPlaceSymbolAfterJackpotVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "blue");
        mostrar();
        mach1.placeSymbol(1, "green");
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "blue"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al colocar en una rueda bloqueada.
     * WHEN: se bloquea la rueda 1, se muestra la máquina y se llama placeSymbol(1, "green").
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testPlaceSymbolLockedVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(1);
        mostrar();
        mach1.placeSymbol(1, "green");
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al colocar un color inexistente.
     * WHEN: con la máquina visible, se llama placeSymbol(1, "nonexistent").
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testPlaceSymbolInvalidColorVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mostrar();
        mach1.placeSymbol(1, "nonexistent");
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el Jackpot logrado con placeSymbol en modo visible (mensaje de felicitación).
     * WHEN: con {blue, purple} y la máquina visible, se llama placeSymbol(2, "blue").
     * THEN: ok() true y las dos ruedas muestran un único color.
     */
    @Test
    public void testPlaceSymbolReachesJackpotVisible() {
        crearRuedas(2);
        fijar("blue", "purple");
        mostrar();
        mach1.placeSymbol(2, "blue");
        assertTrue(mach1.ok());
        assertEquals(1, mach1.distinctSymbols());
    }

    // ---------------- spin(int wheel) ----------------

    /**
     * QUÉ SE PRUEBA: que spin(wheel) sin ruedas se rechaza.
     * WHEN: sin ruedas, se llama spin(1).
     * THEN: ok() false.
     */
    @Test
    public void testSpinWheelWithoutWheelsRejected() {
        dejarOkVerdadero();
        mach1.spin(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que spin(wheel) se rechaza con catálogo vacío.
     * WHEN: se vacía el catálogo, se agrega 1 rueda y se llama spin(1).
     * THEN: ok() false.
     */
    @Test
    public void testSpinWheelWithoutCatalogRejected() {
        vaciarCatalogo();
        mach1.addWheel(1);
        mach1.spin(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de rechazo de spin(wheel) tras el Jackpot.
     * WHEN: con {blue, blue} y la máquina visible, se llama spin(1).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinWheelAfterJackpotVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "blue");
        mostrar();
        mach1.spin(1);
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "blue"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que una posición menor a 1 gira la primera rueda.
     * WHEN: con {blue, purple, green}, se llama spin(0).
     * THEN: ok() true y las ruedas 2 y 3 no cambian.
     */
    @Test
    public void testSpinWheelPositionBelowRangeUsesFirstWheel() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        mach1.spin(0);
        assertTrue(mach1.ok());
        assertEquals("purple", mach1.configuration()[1]);
        assertEquals("green", mach1.configuration()[2]);
    }

    /**
     * QUÉ SE PRUEBA: que una posición mayor al número de ruedas gira la última.
     * WHEN: con {blue, purple, green}, se llama spin(99).
     * THEN: ok() true y las ruedas 1 y 2 no cambian.
     */
    @Test
    public void testSpinWheelPositionAboveRangeUsesLastWheel() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        mach1.spin(99);
        assertTrue(mach1.ok());
        assertEquals("blue", mach1.configuration()[0]);
        assertEquals("purple", mach1.configuration()[1]);
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al girar una rueda bloqueada.
     * WHEN: se bloquea la rueda 1, se muestra la máquina y se llama spin(1).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinWheelLockedVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(1);
        mostrar();
        mach1.spin(1);
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: spin(wheel) en modo visible (palanca y animación).
     * WHEN: con {blue, purple, green} y la máquina visible, se llama spin(2).
     * THEN: ok() true y las ruedas 1 y 3 no cambian.
     */
    @Test
    public void testSpinWheelVisibleSucceeds() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        mostrar();
        mach1.spin(2);
        assertTrue(mach1.ok());
        assertEquals("blue", mach1.configuration()[0]);
        assertEquals("green", mach1.configuration()[2]);
    }

    // ---------------- spin(int wheel, int steps) ----------------

    /**
     * QUÉ SE PRUEBA: que spin(wheel, steps) sin ruedas se rechaza.
     * WHEN: sin ruedas, se llama spin(1, 1).
     * THEN: ok() false.
     */
    @Test
    public void testSpinStepsWithoutWheelsRejected() {
        dejarOkVerdadero();
        mach1.spin(1, 1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error de spin(wheel, steps) sin ruedas en modo visible.
     * WHEN: con la máquina visible y sin ruedas, se llama spin(1, 1).
     * THEN: ok() false.
     */
    @Test
    public void testSpinStepsWithoutWheelsVisibleRejected() {
        dejarOkVerdadero();
        mostrar();
        mach1.spin(1, 1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de rechazo de spin(wheel, steps) tras el Jackpot.
     * WHEN: con {blue, blue} y la máquina visible, se llama spin(1, 1).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinStepsAfterJackpotVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "blue");
        mostrar();
        mach1.spin(1, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "blue"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error por rueda inexistente en spin(wheel, steps).
     * WHEN: con 2 ruedas y la máquina visible, se llama spin(5, 1).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinStepsInvalidWheelVisibleRejected() {
        crearRuedas(2);
        String[] before = mach1.configuration();
        mostrar();
        mach1.spin(5, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al avanzar una rueda bloqueada.
     * WHEN: se bloquea la rueda 1, se muestra la máquina y se llama spin(1, 1).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinStepsLockedVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(1);
        mostrar();
        mach1.spin(1, 1);
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: spin(wheel, steps) en modo visible (animación paso a paso).
     * WHEN: con {blue, purple, green} y la máquina visible, se llama spin(1, 1).
     * THEN: ok() true y la rueda 1 muestra "purple".
     */
    @Test
    public void testSpinStepsVisibleAdvancesOneSymbol() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        mostrar();
        mach1.spin(1, 1);
        assertTrue(mach1.ok());
        assertEquals("purple", mach1.configuration()[0]);
    }

    /**
     * QUÉ SE PRUEBA: que 0 pasos no modifica la rueda.
     * WHEN: con {blue, purple}, se llama spin(1, 0).
     * THEN: ok() true y la configuración no cambia.
     */
    @Test
    public void testSpinStepsZeroKeepsConfiguration() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.spin(1, 0);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que si el color mostrado ya no está en el catálogo se parte del primer símbolo.
     * WHEN: con {blue, purple}, se elimina "blue" del catálogo y se llama spin(1, 1).
     * THEN: la rueda 1 muestra "green" (índice 1 de {purple, green, brown}).
     */
    @Test
    public void testSpinStepsColorMissingFromCatalogStartsFromFirst() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.delSymbol("blue");
        mach1.spin(1, 1);
        assertTrue(mach1.ok());
        assertEquals("green", mach1.configuration()[0]);
    }

    // ---------------- spin(String[]) ----------------

    /**
     * QUÉ SE PRUEBA: el diálogo de error con arreglo nulo en modo visible.
     * WHEN: con 1 rueda y la máquina visible, se llama spin((String[]) null).
     * THEN: ok() false.
     */
    @Test
    public void testSpinArrayNullVisibleRejected() {
        mach1.addWheel(1);
        mostrar();
        mach1.spin((String[]) null);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que spin(String[]) se rechaza si el catálogo está vacío.
     * WHEN: se vacía el catálogo, se agrega 1 rueda y se llama spin({"blue"}).
     * THEN: ok() false.
     */
    @Test
    public void testSpinArrayWithoutCatalogRejected() {
        vaciarCatalogo();
        mach1.addWheel(1);
        mach1.spin(new String[]{"blue"});
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error cuando el arreglo no coincide con las ruedas.
     * WHEN: con 2 ruedas y la máquina visible, se llama spin({"blue"}).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinArrayLengthMismatchVisibleRejected() {
        crearRuedas(2);
        String[] before = mach1.configuration();
        mostrar();
        mach1.spin(new String[]{"blue"});
        assertFalse(mach1.ok());
        assertArrayEquals(before, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de rechazo de spin(String[]) tras el Jackpot.
     * WHEN: con {blue, blue} y la máquina visible, se llama spin({"green", "brown"}).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinArrayAfterJackpotVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "blue");
        mostrar();
        mach1.spin(new String[]{"green", "brown"});
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "blue"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error cuando hay una rueda bloqueada.
     * WHEN: se bloquea la rueda 2, se muestra la máquina y se llama spin({"green", "brown"}).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinArrayLockedVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(2);
        mostrar();
        mach1.spin(new String[]{"green", "brown"});
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error por un color inexistente.
     * WHEN: con la máquina visible, se llama spin({"blue", "nonexistent"}).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSpinArrayInvalidColorVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mostrar();
        mach1.spin(new String[]{"blue", "nonexistent"});
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: spin(String[]) en modo visible (animación de giro).
     * WHEN: con 2 ruedas y la máquina visible, se llama spin({"green", "brown"}).
     * THEN: ok() true y la configuración es {green, brown}.
     */
    @Test
    public void testSpinArrayVisibleAppliesConfiguration() {
        crearRuedas(2);
        mostrar();
        mach1.spin(new String[]{"green", "brown"});
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"green", "brown"}, mach1.configuration());
    }

    // ---------------- swap ----------------

    /**
     * QUÉ SE PRUEBA: el diálogo de error al intercambiar con menos de 2 ruedas.
     * WHEN: con 1 rueda y la máquina visible, se llama swap(1, 1).
     * THEN: ok() false.
     */
    @Test
    public void testSwapWithTooFewWheelsVisibleRejected() {
        mach1.addWheel(1);
        mostrar();
        mach1.swap(1, 1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que la primera posición mayor al rango se rechaza.
     * WHEN: con 2 ruedas, se llama swap(9, 1).
     * THEN: ok() false.
     */
    @Test
    public void testSwapFirstIndexTooHighRejected() {
        crearRuedas(2);
        mach1.swap(9, 1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que la segunda posición 0 se rechaza.
     * WHEN: con 2 ruedas, se llama swap(1, 0).
     * THEN: ok() false.
     */
    @Test
    public void testSwapSecondIndexZeroRejected() {
        crearRuedas(2);
        mach1.swap(1, 0);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error cuando la segunda posición excede el rango.
     * WHEN: con 2 ruedas y la máquina visible, se llama swap(1, 9).
     * THEN: ok() false.
     */
    @Test
    public void testSwapSecondIndexTooHighVisibleRejected() {
        crearRuedas(2);
        mostrar();
        mach1.swap(1, 9);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que se rechaza el intercambio si la primera rueda está bloqueada.
     * WHEN: se bloquea la rueda 1 y se llama swap(1, 2).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSwapFirstWheelLockedRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(1);
        mach1.swap(1, 2);
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al intercambiar una rueda bloqueada.
     * WHEN: se bloquea la rueda 2, se muestra la máquina y se llama swap(1, 2).
     * THEN: ok() false y la configuración no cambia.
     */
    @Test
    public void testSwapLockedVisibleRejected() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.lock(2);
        mostrar();
        mach1.swap(1, 2);
        assertFalse(mach1.ok());
        assertArrayEquals(new String[]{"blue", "purple"}, mach1.configuration());
    }

    /**
     * QUÉ SE PRUEBA: que una Lefty como segunda rueda impide el intercambio.
     * WHEN: con una Lefty en la posición 1, se llama swap(2, 1).
     * THEN: ok() false.
     */
    @Test
    public void testSwapLeftyAsSecondWheelRejected() {
        crearRuedas(2);
        mach1.addLeftyWheel(1);
        mach1.swap(2, 1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al intercambiar una Lefty.
     * WHEN: con una Lefty en la posición 1 y la máquina visible, se llama swap(1, 2).
     * THEN: ok() false.
     */
    @Test
    public void testSwapLeftyVisibleRejected() {
        crearRuedas(2);
        mach1.addLeftyWheel(1);
        mostrar();
        mach1.swap(1, 2);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que una Rebel como segunda rueda impide el intercambio.
     * WHEN: con una Rebel en la posición 1, se llama swap(2, 1).
     * THEN: ok() false.
     */
    @Test
    public void testSwapRebelAsSecondWheelRejected() {
        crearRuedas(2);
        mach1.addRebelWheel(1);
        mach1.swap(2, 1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al intercambiar una Rebel.
     * WHEN: con una Rebel en la posición 1 y la máquina visible, se llama swap(1, 2).
     * THEN: ok() false.
     */
    @Test
    public void testSwapRebelVisibleRejected() {
        crearRuedas(2);
        mach1.addRebelWheel(1);
        mostrar();
        mach1.swap(1, 2);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: un intercambio válido en modo visible.
     * WHEN: con {blue, purple, green} y la máquina visible, se llama swap(1, 3).
     * THEN: ok() true y la configuración es {green, purple, blue}.
     */
    @Test
    public void testSwapVisibleSucceeds() {
        crearRuedas(3);
        fijar("blue", "purple", "green");
        mostrar();
        mach1.swap(1, 3);
        assertTrue(mach1.ok());
        assertArrayEquals(new String[]{"green", "purple", "blue"}, mach1.configuration());
    }

    // ---------------- lock / unlock ----------------

    /**
     * QUÉ SE PRUEBA: que lock se rechaza sin ruedas.
     * WHEN: sin ruedas, se llama lock(1).
     * THEN: ok() false.
     */
    @Test
    public void testLockWithoutWheelsRejected() {
        dejarOkVerdadero();
        mach1.lock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error de lock sin ruedas.
     * WHEN: con la máquina visible y sin ruedas, se llama lock(1).
     * THEN: ok() false.
     */
    @Test
    public void testLockWithoutWheelsVisibleRejected() {
        dejarOkVerdadero();
        mostrar();
        mach1.lock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al bloquear una posición inválida.
     * WHEN: con 1 rueda y la máquina visible, se llama lock(5).
     * THEN: ok() false.
     */
    @Test
    public void testLockInvalidIndexVisibleRejected() {
        mach1.addWheel(1);
        mostrar();
        mach1.lock(5);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al bloquear una rueda rebelde.
     * WHEN: con una Rebel y la máquina visible, se llama lock(1).
     * THEN: ok() false.
     */
    @Test
    public void testLockRebelVisibleRejected() {
        mach1.addRebelWheel(1);
        mostrar();
        mach1.lock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al bloquear una rueda ya bloqueada.
     * WHEN: se bloquea la rueda 1, se muestra la máquina y se vuelve a llamar lock(1).
     * THEN: ok() false.
     */
    @Test
    public void testLockAlreadyLockedVisibleRejected() {
        mach1.addWheel(1);
        mach1.lock(1);
        mostrar();
        mach1.lock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: que unlock se rechaza sin ruedas.
     * WHEN: sin ruedas, se llama unlock(1).
     * THEN: ok() false.
     */
    @Test
    public void testUnlockWithoutWheelsRejected() {
        dejarOkVerdadero();
        mach1.unlock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error de unlock sin ruedas.
     * WHEN: con la máquina visible y sin ruedas, se llama unlock(1).
     * THEN: ok() false.
     */
    @Test
    public void testUnlockWithoutWheelsVisibleRejected() {
        dejarOkVerdadero();
        mostrar();
        mach1.unlock(1);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al desbloquear una posición inválida.
     * WHEN: con 1 rueda y la máquina visible, se llama unlock(5).
     * THEN: ok() false.
     */
    @Test
    public void testUnlockInvalidIndexVisibleRejected() {
        mach1.addWheel(1);
        mostrar();
        mach1.unlock(5);
        assertFalse(mach1.ok());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al desbloquear una rueda libre.
     * WHEN: con la máquina visible, se llama unlock(1) sobre una rueda libre.
     * THEN: ok() false.
     */
    @Test
    public void testUnlockAlreadyFreeVisibleRejected() {
        mach1.addWheel(1);
        mostrar();
        mach1.unlock(1);
        assertFalse(mach1.ok());
    }

    // ---------------- isJackpot ----------------

    /**
     * QUÉ SE PRUEBA: el mensaje de felicitación al detectar el Jackpot en modo visible.
     * WHEN: con {blue, blue} y la máquina visible, se consulta isJackpot().
     * THEN: el resultado debe ser true.
     */
    @Test
    public void testIsJackpotTrueVisible() {
        crearRuedas(2);
        fijar("blue", "blue");
        mostrar();
        assertTrue(mach1.isJackpot());
    }

    // ---------------- addLeftyWheel ----------------

    /**
     * QUÉ SE PRUEBA: el diálogo de error al agregar una Lefty en posición inválida.
     * WHEN: con la máquina visible, se llama addLeftyWheel(99).
     * THEN: ok() false y no se crean ruedas.
     */
    @Test
    public void testAddLeftyWheelPositionTooHighVisibleRejected() {
        mostrar();
        mach1.addLeftyWheel(99);
        assertFalse(mach1.ok());
        assertEquals(0, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al insertar una Lefty sobre una rueda bloqueada.
     * WHEN: se bloquea la rueda 1, se muestra la máquina y se llama addLeftyWheel(1).
     * THEN: ok() false y siguen 2 ruedas.
     */
    @Test
    public void testAddLeftyWheelLockedPositionVisibleRejected() {
        crearRuedas(2);
        mach1.lock(1);
        mostrar();
        mach1.addLeftyWheel(1);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al insertar una Lefty sobre otra Lefty.
     * WHEN: con una Lefty en la posición 1 y la máquina visible, se llama addLeftyWheel(1).
     * THEN: ok() false y siguen 3 ruedas.
     */
    @Test
    public void testAddLeftyWheelOnLeftyVisibleRejected() {
        crearRuedas(2);
        mach1.addLeftyWheel(1);
        mostrar();
        mach1.addLeftyWheel(1);
        assertFalse(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que una Lefty sin vecina izquierda gira con el símbolo recibido.
     * WHEN: con {blue, purple} y una Lefty en la posición 1, se llama spin(1).
     * THEN: ok() true y siguen 3 ruedas.
     */
    @Test
    public void testLeftyWithoutLeftNeighborSpinsNormally() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.addLeftyWheel(1);
        mach1.spin(1);
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que una Lefty cuya vecina no tiene símbolo usa el símbolo recibido.
     * WHEN: con catálogo vacío se crean 2 ruedas y una Lefty en la 2; se agrega "red" y se llama spin(2).
     * THEN: ok() true y la Lefty muestra "red".
     */
    @Test
    public void testLeftyWithEmptyLeftNeighborUsesOwnSymbol() {
        vaciarCatalogo();
        mach1.addWheel(1);
        mach1.addWheel(2);
        mach1.addLeftyWheel(2);
        mach1.addSymbol(1, "red");
        mach1.spin(2);
        assertTrue(mach1.ok());
        assertEquals("red", mach1.configuration()[1]);
    }

    // ---------------- addRebelWheel ----------------

    /**
     * QUÉ SE PRUEBA: que una posición mayor al rango se rechaza para la Rebel.
     * WHEN: se llama addRebelWheel(99) sin ruedas.
     * THEN: ok() false y no se crean ruedas.
     */
    @Test
    public void testAddRebelWheelPositionTooHighRejected() {
        mach1.addRebelWheel(99);
        assertFalse(mach1.ok());
        assertEquals(0, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al agregar una Rebel en posición inválida.
     * WHEN: con la máquina visible, se llama addRebelWheel(0).
     * THEN: ok() false y no se crean ruedas.
     */
    @Test
    public void testAddRebelWheelInvalidPositionVisibleRejected() {
        mostrar();
        mach1.addRebelWheel(0);
        assertFalse(mach1.ok());
        assertEquals(0, ruedas());
    }

    // ---------------- addLazyWheel ----------------

    /**
     * QUÉ SE PRUEBA: que se agrega una Lazy al final de la máquina.
     * WHEN: con 2 ruedas, se llama addLazyWheel(3).
     * THEN: ok() true y quedan 3 ruedas.
     */
    @Test
    public void testAddLazyWheelAtEndSucceeds() {
        crearRuedas(2);
        mach1.addLazyWheel(3);
        assertTrue(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que se agrega una Lazy al inicio desplazando las demás.
     * WHEN: con {blue, purple}, se llama addLazyWheel(1).
     * THEN: ok() true, 3 ruedas y "blue", "purple" quedan en las posiciones 2 y 3.
     */
    @Test
    public void testAddLazyWheelAtBeginningShiftsWheels() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.addLazyWheel(1);
        assertTrue(mach1.ok());
        assertEquals("blue", mach1.configuration()[1]);
        assertEquals("purple", mach1.configuration()[2]);
    }

    /**
     * QUÉ SE PRUEBA: que una posición inválida se rechaza para la Lazy.
     * WHEN: se llama addLazyWheel(0).
     * THEN: ok() false y no se crean ruedas.
     */
    @Test
    public void testAddLazyWheelPositionZeroRejected() {
        mach1.addLazyWheel(0);
        assertFalse(mach1.ok());
        assertEquals(0, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al agregar una Lazy en posición inválida.
     * WHEN: con la máquina visible, se llama addLazyWheel(99).
     * THEN: ok() false y no se crean ruedas.
     */
    @Test
    public void testAddLazyWheelPositionTooHighVisibleRejected() {
        mostrar();
        mach1.addLazyWheel(99);
        assertFalse(mach1.ok());
        assertEquals(0, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que no se inserta una Lazy sobre una rueda bloqueada.
     * WHEN: se bloquea la rueda 1 y se llama addLazyWheel(1).
     * THEN: ok() false y siguen 2 ruedas.
     */
    @Test
    public void testAddLazyWheelLockedPositionRejected() {
        crearRuedas(2);
        mach1.lock(1);
        mach1.addLazyWheel(1);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al insertar una Lazy sobre una rueda bloqueada.
     * WHEN: se bloquea la rueda 1, se muestra la máquina y se llama addLazyWheel(1).
     * THEN: ok() false y siguen 2 ruedas.
     */
    @Test
    public void testAddLazyWheelLockedPositionVisibleRejected() {
        crearRuedas(2);
        mach1.lock(1);
        mostrar();
        mach1.addLazyWheel(1);
        assertFalse(mach1.ok());
        assertEquals(2, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que no se inserta una Lazy sobre otra Lazy.
     * WHEN: con una Lazy en la posición 1, se llama addLazyWheel(1).
     * THEN: ok() false y siguen 3 ruedas.
     */
    @Test
    public void testAddLazyWheelOnLazyRejected() {
        crearRuedas(2);
        mach1.addLazyWheel(1);
        mach1.addLazyWheel(1);
        assertFalse(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error al insertar una Lazy sobre otra Lazy.
     * WHEN: con una Lazy en la posición 1 y la máquina visible, se llama addLazyWheel(1).
     * THEN: ok() false y siguen 3 ruedas.
     */
    @Test
    public void testAddLazyWheelOnLazyVisibleRejected() {
        crearRuedas(2);
        mach1.addLazyWheel(1);
        mostrar();
        mach1.addLazyWheel(1);
        assertFalse(mach1.ok());
        assertEquals(3, ruedas());
    }

    /**
     * QUÉ SE PRUEBA: que una Lazy acepta giros repetidos sin alterar el resto de la máquina.
     * WHEN: con {blue, purple} y una Lazy en la posición 3, se llama spin(3) seis veces.
     * THEN: cada llamada deja ok() true y siguen 3 ruedas.
     */
    @Test
    public void testLazyWheelAcceptsRepeatedSpins() {
        crearRuedas(2);
        fijar("blue", "purple");
        mach1.addLazyWheel(3);
        for (int i = 0; i < 6; i++) {
            mach1.spin(3);
            assertTrue(mach1.ok());
        }
        assertEquals(3, ruedas());
    }

    // ---------------- LazyWheel ----------------

    /**
     * QUÉ SE PRUEBA: que la Lazy a veces gira y a veces conserva su símbolo.
     * WHEN: se invoca girar() repetidamente (máx. 200 veces) hasta ver ambos comportamientos.
     * THEN: debe observarse al menos un giro aceptado y al menos uno abortado.
     */
    @Test
    public void testLazyWheelBothBranchesOfGirar() {
        LazyWheel lazy = new LazyWheel(0);
        lazy.setSymbol(new Symbol("blue", "triangle"));
        boolean giro = false;
        boolean retuvo = false;
        for (int i = 0; i < 200 && !(giro && retuvo); i++) {
            Symbol antes = lazy.getSimboloActual();
            lazy.girar(new Symbol("green", "triangle"));
            if (lazy.getSimboloActual() == antes) {
                retuvo = true;
            } else {
                giro = true;
            }
        }
        lazy.makeInvisible();
        assertTrue(giro);
        assertTrue(retuvo);
    }

    // ---------------- RebelWheel ----------------

    /**
     * QUÉ SE PRUEBA: que la Rebel anula el bloqueo directamente sobre la rueda.
     * WHEN: se llama lock() sobre una RebelWheel.
     * THEN: isLocked() debe seguir en false.
     */
    @Test
    public void testRebelWheelLockIsIgnored() {
        RebelWheel rebel = new RebelWheel(0);
        rebel.lock();
        rebel.makeInvisible();
        assertFalse(rebel.isLocked());
    }

    // ---------------- Wheel ----------------

    /**
     * QUÉ SE PRUEBA: que una rueda sin símbolo soporta reposicionarse, redibujarse y ocultarse.
     * WHEN: sobre un Wheel nuevo se llama actualizarPosicion, redibujar y makeInvisible.
     * THEN: no hay excepción, getColorActual() es "" y getSimboloActual() es null.
     */
    @Test
    public void testWheelWithoutSymbolSupportsBasicOperations() {
        Wheel rueda = new Wheel(0);
        rueda.actualizarPosicion(1);
        rueda.redibujar();
        rueda.makeInvisible();
        assertEquals("", rueda.getColorActual());
        assertNull(rueda.getSimboloActual());
    }

    /**
     * QUÉ SE PRUEBA: que girar() funciona en una rueda que aún no tiene símbolo.
     * WHEN: sobre un Wheel sin símbolo se llama girar(blue).
     * THEN: getColorActual() debe ser "blue".
     */
    @Test
    public void testWheelGirarWithoutPreviousSymbol() {
        Wheel rueda = new Wheel(0);
        rueda.girar(new Symbol("blue", "triangle"));
        rueda.cambiarColorFondo("red");
        rueda.makeInvisible();
        assertEquals("blue", rueda.getColorActual());
    }

    /**
     * QUÉ SE PRUEBA: que una rueda con símbolo se reposiciona y se redibuja con su símbolo.
     * WHEN: a un Wheel con símbolo se le llama actualizarPosicion(2) y redibujar().
     * THEN: el símbolo sigue siendo "green".
     */
    @Test
    public void testWheelWithSymbolRepositionsAndRedraws() {
        Wheel rueda = new Wheel(0);
        rueda.setSymbol(new Symbol("green", "triangle"));
        rueda.actualizarPosicion(2);
        rueda.redibujar();
        rueda.makeInvisible();
        assertEquals("green", rueda.getColorActual());
    }

    // ---------------- Symbol ----------------

    /**
     * QUÉ SE PRUEBA: que un símbolo con forma desconocida no tiene figura y no falla al operarse.
     * WHEN: se crea Symbol("red", "hexagon") y se llaman todos sus métodos gráficos.
     * THEN: no hay excepción y getForma() es "hexagon".
     */
    @Test
    public void testSymbolUnknownShapeHasNoFigure() {
        Symbol s = new Symbol("red", "hexagon");
        s.makeVisible();
        s.makeInvisible();
        s.moverA(5, 5);
        s.moverHorizontal(5);
        s.moverVertical(5);
        s.cambiarTamano(10);
        assertEquals("hexagon", s.getForma());
    }

    /**
     * QUÉ SE PRUEBA: que cambiarTamano funciona con círculo y rectángulo.
     * WHEN: se llama cambiarTamano(20) sobre un Symbol circular y otro rectangular.
     * THEN: no hay excepción y las formas se conservan en minúscula.
     */
    @Test
    public void testSymbolCambiarTamanoCircleAndRectangle() {
        Symbol c = new Symbol("red", "CIRCLE");
        Symbol r = new Symbol("red", "rectangle");
        c.cambiarTamano(20);
        r.cambiarTamano(20);
        assertEquals("circle", c.getForma());
        assertEquals("rectangle", r.getForma());
    }

    /**
     * QUÉ SE PRUEBA: que moverA desplaza la figura del símbolo.
     * WHEN: se llama moverA(10, 20) sobre un símbolo visible.
     * THEN: no hay excepción y el color se conserva.
     */
    @Test
    public void testSymbolMoverAMovesFigure() {
        Symbol s = new Symbol("red", "circle");
        s.makeVisible();
        s.moverA(10, 20);
        s.makeInvisible();
        assertEquals("red", s.getColor());
    }

    /**
     * QUÉ SE PRUEBA: que generarCopia conserva color y forma, y que aplicarEfecto base no falla.
     * WHEN: se llama aplicarEfecto() y luego generarCopia() sobre un Symbol normal.
     * THEN: la copia es otra instancia con el mismo color y forma.
     */
    @Test
    public void testSymbolGenerarCopiaKeepsColorAndShape() {
        Symbol s = new Symbol("red", "triangle");
        s.aplicarEfecto();
        Symbol copia = s.generarCopia();
        assertNotSame(s, copia);
        assertEquals("red", copia.getColor());
        assertEquals("triangle", copia.getForma());
    }

    // ---------------- EphemeralSymbol ----------------

    /**
     * QUÉ SE PRUEBA: que el efímero deja de encogerse al llegar al tamaño mínimo.
     * WHEN: se llama aplicarEfecto() 12 veces y se genera una copia.
     * THEN: la copia es un EphemeralSymbol con el mismo color.
     */
    @Test
    public void testEphemeralSymbolStopsShrinkingAtMinimum() {
        EphemeralSymbol e = new EphemeralSymbol("red", "circle");
        for (int i = 0; i < 12; i++) {
            e.aplicarEfecto();
        }
        Symbol copia = e.generarCopia();
        assertTrue(copia instanceof EphemeralSymbol);
        assertEquals("red", copia.getColor());
    }

    // ---------------- Figure ----------------

    /**
     * QUÉ SE PRUEBA: que la fábrica crea cada figura y devuelve null para una forma desconocida.
     * WHEN: se llama Figure.crear con circle, RECTANGLE, triangle y hexagon.
     * THEN: devuelve Circle, Rectangle, Triangle y null respectivamente.
     */
    @Test
    public void testFigureCrearBuildsEachShape() {
        assertTrue(Figure.crear("circle") instanceof Circle);
        assertTrue(Figure.crear("RECTANGLE") instanceof Rectangle);
        assertTrue(Figure.crear("triangle") instanceof Triangle);
        assertNull(Figure.crear("hexagon"));
    }

    /**
     * QUÉ SE PRUEBA: que los movimientos rápidos desplazan 20 píxeles.
     * WHEN: se llaman moveRight, moveLeft, moveDown y moveUp.
     * THEN: las posiciones reflejan múltiplos de 20.
     */
    @Test
    public void testFigureMoveByTwentyPixels() {
        Figure f = new Circle();
        f.moveRight();
        assertEquals(20, f.xPosition);
        f.moveLeft();
        f.moveLeft();
        assertEquals(-20, f.xPosition);
        f.moveDown();
        assertEquals(20, f.yPosition);
        f.moveUp();
        f.moveUp();
        assertEquals(-20, f.yPosition);
    }

    /**
     * QUÉ SE PRUEBA: los movimientos lentos en ambos sentidos.
     * WHEN: se llama slowMoveHorizontal(3), (-5) y slowMoveVertical(4), (-6).
     * THEN: las posiciones quedan en -2 (x) y -2 (y).
     */
    @Test
    public void testFigureSlowMovesBothDirections() {
        Figure f = new Rectangle();
        f.slowMoveHorizontal(3);
        assertEquals(3, f.xPosition);
        f.slowMoveHorizontal(-5);
        assertEquals(-2, f.xPosition);
        f.slowMoveVertical(4);
        assertEquals(4, f.yPosition);
        f.slowMoveVertical(-6);
        assertEquals(-2, f.yPosition);
    }

    /**
     * QUÉ SE PRUEBA: que una figura visible se puede mover, recolorear y ocultar.
     * WHEN: se hace visible un Circle, se mueve en x e y, se cambia el color y se oculta.
     * THEN: la posición final es (10, 10).
     */
    @Test
    public void testFigureVisibleMovesAndHides() {
        Figure f = new Circle();
        f.makeVisible();
        f.moveHorizontal(10);
        f.moveVertical(10);
        f.changeColor("red");
        f.makeInvisible();
        f.makeInvisible();
        assertEquals(10, f.xPosition);
        assertEquals(10, f.yPosition);
    }

    // ---------------- Lever ----------------

    /**
     * QUÉ SE PRUEBA: que la palanca baja y vuelve a su posición.
     * WHEN: se crea una Lever y se llama tirar() y soltar().
     * THEN: no se lanza ninguna excepción.
     */
    @Test
    public void testLeverTirarAndSoltar() {
        Lever palanca = new Lever();
        palanca.tirar();
        palanca.soltar();
        assertNotNull(palanca);
    }

    // ---------------- Canvas ----------------

    /**
     * QUÉ SE PRUEBA: que el Canvas reconoce todos los nombres de color (y usa negro por defecto).
     * WHEN: se llama setForegroundColor con cada color soportado y uno desconocido.
     * THEN: no hay excepción y el Canvas sigue siendo el mismo singleton.
     */
    @Test
    public void testCanvasSetForegroundColorAcceptsAllNames() {
        String[] colores = {"red", "black", "blue", "yellow", "green", "magenta", "white",
            "cyan", "orange", "pink", "grey", "lightgray", "darkgray", "purple", "brown",
            "gold", "navy", "teal", "maroon", "olive", "silver", "violet", "indigo",
            "turquoise", "coral", "salmon", "khaki", "plum", "lavender", "skyblue",
            "darkgreen", "desconocido"};
        Canvas canvas = Canvas.getCanvas();
        for (String color : colores) {
            canvas.setForegroundColor(color);
        }
        assertSame(canvas, Canvas.getCanvas());
    }

    /**
     * QUÉ SE PRUEBA: que se puede dibujar y borrar una forma directamente en el Canvas.
     * WHEN: se llama draw() y luego erase() con el mismo objeto de referencia.
     * THEN: no se lanza ninguna excepción.
     */
    @Test
    public void testCanvasDrawAndErase() {
        Object ref = new Object();
        Canvas.getCanvas().draw(ref, "red", new java.awt.Rectangle(0, 0, 5, 5));
        Canvas.getCanvas().erase(ref);
        assertNotNull(Canvas.getCanvas());
    }

    /**
     * QUÉ SE PRUEBA: que una interrupción durante wait() se ignora sin lanzar excepción.
     * WHEN: se interrumpe el hilo actual y se llama wait(10).
     * THEN: la llamada retorna normalmente.
     */
    @Test
    public void testCanvasWaitInterruptedDoesNotThrow() {
        Thread.currentThread().interrupt();
        Canvas.getCanvas().wait(10);
        assertFalse(Thread.interrupted());
    }

    // ---------------- SlotMachineContest ----------------

    /**
     * QUÉ SE PRUEBA: que un n por encima del máximo se rechaza.
     * WHEN: se llama solve(51).
     * THEN: devuelve una matriz vacía.
     */
    @Test
    public void testSolveAboveMaximumReturnsEmptyMatrix() {
        assertEquals(0, new SlotMachineContest().solve(51).length);
    }

    /**
     * QUÉ SE PRUEBA: que un n no positivo se rechaza.
     * WHEN: se llama solve(0).
     * THEN: devuelve una matriz vacía.
     */
    @Test
    public void testSolveZeroReturnsEmptyMatrix() {
        assertEquals(0, new SlotMachineContest().solve(0).length);
    }

    /**
     * QUÉ SE PRUEBA: el diálogo de error de simulate con un n inválido.
     * WHEN: se llama simulate(2) y simulate(51).
     * THEN: ambas llamadas retornan sin lanzar excepción ni terminar la JVM.
     */
    @Test
    public void testSimulateInvalidSizeShowsDialogAndReturns() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.simulate(2);
        contest.simulate(51);
        assertEquals(0, contest.solve(2).length);
    }

    /**
     * QUÉ SE PRUEBA: que solve(3) siempre devuelve acciones dentro de rango, incluso cuando
     * el algoritmo termina anticipadamente por Jackpot en cualquiera de sus fases.
     * WHEN: se llama solve(3) ocho veces (la máquina es aleatoria).
     * THEN: en cada acción la rueda está en [1, 3] y los pasos en [1, 3].
     */
    @Test
    public void testSolveN3RepeatedKeepsActionsInRange() {
        SlotMachineContest contest = new SlotMachineContest();
        for (int i = 0; i < 8; i++) {
            for (int[] accion : contest.solve(3)) {
                assertTrue(accion[0] >= 1 && accion[0] <= 3);
                assertTrue(accion[1] >= 1 && accion[1] <= 3);
            }
        }
    }
}
