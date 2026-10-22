# 🎰 SlotMachine

Simulador de una máquina tragamonedas desarrollado como proyecto inicial del curso **Desarrollo Orientado por Objetos [DOPO-POOB]**
Escuela Colombiana de Ingeniería.

El proyecto está inspirado en el *Problem I* de la Maratón de Programación Internacional 2025: **Slot Machine**.

---

## 🎯 Objetivos del Proyecto
- **Gestión dinámica de estado:** Permitir adicionar o eliminar ruedas y símbolos en tiempo real, adaptando la interfaz gráfica de forma automática.
- **Simulación visual interactiva:** Aprovechar una librería de figuras (`shapes`) para recrear el comportamiento de una máquina tragamonedas, incluyendo retardo mecánico y animaciones (palanca).
- **Evaluación de condiciones:** Implementar la lógica matemática para consultar configuraciones visibles y detectar el premio mayor (Jackpot) cuando todas las ruedas coinciden.
- **Control de visibilidad y usabilidad:** Garantizar el correcto funcionamiento de la máquina tanto en modo visible como invisible, manejando excepciones y alertas a través de cuadros de diálogo (`JOptionPane`).

---

## ⚙️ Implementación Práctica
La solución se compone de varias clases articuladas:
1. **`SlotMachine`**: Clase principal que administra las listas de ruedas (`ArrayList<Wheel>`) y el catálogo de símbolos (`ArrayList<Symbol>`). Controla la lógica de victoria, las validaciones de límites y el *redimensionamiento* de la carcasa.
2. **`Wheel`**: Representa cada columna giratoria. Encapsula un recuadro blanco y un borde negro, y se encarga de posicionar el `Symbol` actual exactamente en su centro.
3. **`Symbol`**: Representación gráfica de las fichas (Círculos, Triángulos o Cuadrados). Verifica colores y asegura alineaciones perfectas compensando desfases geométricos.
4. **`Lever`**: Componente visual (palanca) que implementa métodos `tirar()` y `soltar()` para añadir inmersión al evento de giro.
5. **`SlotMachineContest`**: Clase controladora y algorítmica que resuelve el problema de la maratón interactuando con la máquina de forma silenciosa e invisible.

---

## 🧩 Funcionalidades y Tipos de Ruedas (SlotMachine)
La máquina cuenta con un conjunto robusto de métodos y comportamientos polimórficos que le permiten administrar ruedas estándar y ruedas con comportamientos anómalos (inéditos):

* **`SlotMachine()`:** Crea una máquina vacía (sin ruedas) con un catálogo inicial de símbolos: dos efímeros (`blue`, `purple`) y dos tímidos (`green`, `brown`).
* **`SlotMachine(n)`:** Crea una máquina con `n` ruedas y `n` símbolos normales de colores distintos elegidos al azar, lista para jugar y visible.
* **`SlotMachine(n, m)`:** Igual que el anterior, pero cada símbolo toma al azar un tipo (`normal`, `ephemeral` o `shy`).
* **`makeVisible()` / `makeInvisible()`:** Muestran u ocultan la máquina. En modo invisible no aparecen diálogos ni animaciones, lo que permite correr pruebas y el algoritmo del concurso sin bloqueos.
* **`addSymbol(pos, color)`:** Agrega un símbolo normal al catálogo en la posición indicada. Rechaza colores repetidos sin distinguir mayúsculas.
* **`addSymbol(tipo, color)`:** Agrega al final del catálogo un símbolo polimórfico: `normal`, `ephemeral` (se encoge en cada selección) o `shy` (alterna su visibilidad). Rechaza tipos desconocidos.
* **`delSymbol(color)`:** Elimina del catálogo el símbolo del color indicado. No altera lo que ya muestran las ruedas.
* **`spin(wheel)`:** Gira una única rueda con un símbolo aleatorio, respetando bloqueos y el estado de Jackpot.
* **`spin(String[])`:** Deja la máquina en una configuración exacta (un color por rueda). Si algo falla (rueda bloqueada, color inexistente, tamaño distinto), no cambia nada.
* **`configuration()`:** Devuelve los colores visibles en cada rueda, de izquierda a derecha. Una rueda sin símbolo se reporta como cadena vacía.
* **`distinctSymbols()`:** Cuenta cuántos colores distintos se ven en las ruedas. Es la única "pista" que usa `SlotMachineContest`.
* **`symbols()`:** Devuelve el catálogo de colores en su orden actual.
* **`isJackpot()`:** Indica si todas las ruedas muestran el mismo símbolo. Al ganar, la carcasa se pone dorada, se desbloquean todas las ruedas y se bloquean nuevos movimientos.
* **`ok()`:** Indica si la última operación se realizó con éxito. Las consultas no modifican su valor.
* **`exit()`:** Cierra el simulador y termina la ejecución del programa.
* **`addWheel(pos)` / `delWheel(pos)`:** Permite agregar o eliminar ruedas estándar de la máquina en una posición específica, redimensionando la carcasa y ajustando las demás ruedas de forma automática.
* **`addLeftyWheel(pos)`:** Inserta una **LeftyWheel** (Rueda Zurda). Se distingue por tener un borde color morado y su comportamiento consiste en ignorar los giros aleatorios, copiando en su lugar el símbolo exacto de la rueda que tiene a su izquierda.
* **`addRebelWheel(pos)`:** Inserta una **RebelWheel** (Rueda Rebelde). Se distingue por su borde naranja. Es completamente inmune a las alteraciones de estado, por lo que rechaza ser bloqueada, intercambiada o eliminada.
* **`addLazyWheel(pos)`:** Inserta una **LazyWheel** (Rueda Perezosa). Se distingue por su borde marrón. Simula un engranaje oxidado: al tirar de la palanca, tiene un 60% de probabilidad de girar normalmente y un 40% de probabilidad de atascarse, abortando el giro y reteniendo su ficha actual.
* **`spin()` / `spin(wheel, steps)`:** Métodos sobrecargados que permiten girar todas las ruedas al azar, o girar una rueda específica avanzando una cantidad exacta de pasos en el catálogo.
* **`lock(wheel)` / `unlock(wheel)`:** Congela o libera una rueda. Una rueda bloqueada no cambiará de símbolo durante los giros generales.
* **`swap(wheel1, wheel2)`:** Intercambia la posición de dos ruedas en el tablero.
* **`placeSymbol(wheel, color)`:** Permite "hacer trampa", forzando a una rueda específica a mostrar el símbolo del color indicado.

---

## 🧠 Motor Algorítmico (SlotMachineContest)
Para resolver el *Problem I* de la maratón ICPC, se implementó la clase `SlotMachineContest`. Esta clase no usa colores directos ni hace trampa mirando el tablero; opera la máquina a ciegas (`invisible`) utilizando **exclusivamente** la consulta matemática `distinctSymbols()` (cantidad de símbolos diferentes visibles). 

El algoritmo supera el problema de los mínimos locales y garantiza la victoria en menos de 10,000 movimientos a través de **tres fases matemáticas infalibles**:

1. **Fase 1 (Diferenciación):** El motor itera desde la segunda rueda, girando cada una individualmente paso a paso hasta garantizar que en el tablero haya exactamente $n$ símbolos distintos (es decir, todos son diferentes y no hay duplicados).
2. **Fase 2 (Descubrimiento):** Utiliza la Rueda 1 como "sonda" o pivote. Al mover la Rueda 1 un paso, el número de símbolos distintos disminuye (se duplica un símbolo y se pierde otro). Luego, prueba girar las demás ruedas hacia atrás hasta descubrir cuál de ellas restaura los $n$ símbolos distintos. Así, el algoritmo adivina a ciegas la permutación exacta del catálogo.
3. **Fase 3 (Alineación final):** Con las distancias relativas descubiertas en la Fase 2, el motor calcula mediante aritmética modular cuántos pasos exactos le faltan a cada rueda para encajar simultáneamente con la Rueda 1, logrando así el Jackpot absoluto.

---

## 📁 Estructura del Repositorio
De acuerdo con la organización final del proyecto, en este repositorio encontrarás los siguientes directorios y archivos principales:

- **`.vscode/`**: Configuraciones específicas del entorno de desarrollo Visual Studio Code.
- **`1. Descripción por ciclo/`**: Documentación detallada con el registro, justificación y retrospectiva de cada uno de los mini-ciclos de desarrollo.
- **`2. Indicaciones descripción por ciclo/`**: Guías, requerimientos y rúbricas base seguidas durante cada iteración del proyecto.
- **`SlotMachine(BlueJ)/`**: Código fuente y estructura del proyecto configurado específicamente para ser ejecutado de forma nativa en el entorno **BlueJ**.
- **`SlotMachine(VisualStudioCode)/`**: Código fuente y estructura del proyecto refactorizado y adaptado para ser ejecutado en **Visual Studio Code**.
- **`package`**: Archivo de configuración de dependencias y empaquetado propio del entorno BlueJ.
- **`README.md`**: Este documento de presentación y guía general del proyecto.
- **`Slot-Machine (Diagramas)/`**: Archivos correspondientes al diseño y arquitectura del proyecto, incluyendo los diagramas UML (clases y secuencia) realizados en Astah.

---

## ▶️ Cómo ejecutar el proyecto

1. Abrir **BlueJ** y cargar el proyecto ubicado en la carpeta `SlotMachine(BlueJ)`.
2. Compilar todas las clases (`Project → Compile`, o el botón *Compile* de la barra de herramientas).
3. Hacer clic derecho sobre la clase `SlotMachine` en el diagrama y seleccionar `new SlotMachine()` para crear una instancia.
4. Sobre el objeto creado en la banca de objetos, hacer clic derecho para invocar sus métodos (`addWheel`, `addSymbol`, `spin`, `isJackpot`, etc.) y observar los cambios en el simulador gráfico.
5. Consultar `configuration()`, `symbols()` o `ok()` desde el mismo menú contextual para verificar el estado de la máquina tras cada operación.

*(Nota: Si prefieres usar VS Code, puedes abrir la carpeta `SlotMachine(VisualStudioCode)` y ejecutar el proyecto desde allí utilizando la extensión de Java).*

---

## 🛠️ Construcción

- El proyecto reutiliza y extiende los componentes gráficos del paquete `shapes` (`Rectangle`, `Circle`, `Triangle`).
- Desarrollado aplicando buenas prácticas de POO y documentado siguiendo el estándar Javadoc.

---

## 🔧 Mejoras Futuras

- **Optimización Visual:** Reducir el *flickering* unificando las pausas del ciclo de giro, de modo que todas las ruedas cambien de símbolo de manera simultánea en lugar de secuencial.
- **Integración del Algoritmo de Maratón:** Incorporar el algoritmo original del problema *Slot Machine* para calcular secuencias predictivas.
- **Efectos de Sonido:** Integrar alertas auditivas cuando se produzcan eventos como tirar de la palanca o ganar el Jackpot.
- **Nuevas Formas:** Extender el patrón Wrapper de la clase `Symbol` para soportar figuras más complejas (ej. estrellas, rombos).

---

## 👨‍💻 Autores

Trabajo realizado por estudiantes de tercer semestre de Ingeniería en Inteligencia Artificial de la Escuela Colombiana de Ingeniería Julio Garavito:

- **Andres Steeven Oyuela Mendez**, andres.oyuela-m@mail.escuelaing.edu.co
- **Jhon Eduard Duran Ceballos**, jhon.duran-c@mail.escuelaing.edu.co

---

## 🤖 Uso de IA generativa

Durante el desarrollo se usaron herramientas de IA generativa como apoyo para comprender APIs de Java, resolver dudas puntuales de implementación y ayudar en la estructuración de pruebas.

- Anthropic. (2026). *Claude* (Sonnet 5.5) [Modelo de lenguaje de gran escala]. https://claude.ai
- Google. (2026). *Gemini* [Modelo de lenguaje de gran escala]. https://gemini.google.com

---

## 📌 Conclusión

El Simulador de Máquina Tragamonedas DOPO-POOB demuestra con éxito cómo el análisis estructurado de requisitos y la aplicación rigurosa de patrones orientados a objetos permiten desarrollar software interactivo, modular y escalable. A través de múltiples iteraciones y superando retos técnicos gráficos, el proyecto culmina en una experiencia visual sólida y una base de código preparada para crecer.