# 🎰 SlotMachine

Simulador de una máquina tragamonedas desarrollado como proyecto inicial del curso **Desarrollo Orientado por Objetos [DOPO-POOB]**
Escuela Colombiana de Ingeniería — Ciclo No. 1, 2026-2.

El proyecto está inspirado en el *Problem I* de la Maratón de Programación Internacional 2025: **Slot Machine**.

> ⚠️ **Nota:** en esta entrega no se resuelve el problema de la maratón; únicamente se construye el simulador de la máquina.

---

## 🎯 Objetivos del Proyecto
* **Gestión dinámica de estado:** Permitir adicionar o eliminar ruedas y símbolos en tiempo real, adaptando la interfaz gráfica de forma automática.
* **Simulación visual interactiva:** Aprovechar una librería de figuras (`shapes`) para recrear el comportamiento de una máquina tragamonedas, incluyendo retardo mecánico y animaciones (palanca).
* **Evaluación de condiciones:** Implementar la lógica matemática para consultar configuraciones visibles y detectar el premio mayor (Jackpot) cuando todas las ruedas coinciden.
* **Control de visibilidad y usabilidad:** Garantizar el correcto funcionamiento de la máquina tanto en modo visible como invisible, manejando excepciones y alertas a través de cuadros de diálogo (`JOptionPane`).

---

## ⚙️ Implementación Práctica
La solución se compone de varias clases articuladas:
1. **`SlotMachine`**: Clase principal que administra las listas de ruedas (`ArrayList<Wheel>`) y el catálogo de símbolos (`ArrayList<Symbol>`). Controla la lógica de victoria, las validaciones de límites y el *redimensionamiento* de la carcasa.
2. **`Wheel`**: Representa cada columna giratoria. Encapsula un recuadro blanco y un borde negro, y se encarga de posicionar el `Symbol` actual exactamente en su centro.
3. **`Symbol`**: Representación gráfica de las fichas (Círculos, Triángulos o Cuadrados). Verifica colores y asegura alineaciones perfectas compensando desfases geométricos.
4. **`Lever`**: Componente visual (palanca) que implementa métodos `tirar()` y `soltar()` para añadir inmersión al evento de giro.

---

## ▶️ Cómo ejecutar el proyecto

1. Abrir **BlueJ** y cargar el proyecto `slotMachine`.
2. Compilar todas las clases (`Project → Compile`, o el botón *Compile* de la barra de herramientas).
3. Hacer clic derecho sobre la clase `SlotMachine` en el diagrama y seleccionar `new SlotMachine()` para crear una instancia.
4. Sobre el objeto creado en la banca de objetos, hacer clic derecho para invocar sus métodos (`addWheel`, `addSymbol`, `spin`, `isJackpot`, etc.) y observar los cambios en el simulador gráfico.
5. Consultar `configuration()`, `symbols()` o `ok()` desde el mismo menú contextual para verificar el estado de la máquina tras cada operación.

---

## 🛠️ Construcción

- El proyecto reutiliza y extiende los componentes gráficos del paquete `shapes` (`Rectangle`, `Circle`, `Triangle`).
- Desarrollado en **BlueJ**, bajo el nombre de proyecto `slotMachine`.
- Documentado siguiendo el estándar Javadoc.

---

## 🔧 Mejoras Futuras

* **Optimización Visual:** Reducir el *flickering* unificando las pausas del ciclo de giro, de modo que todas las ruedas cambien de símbolo de manera simultánea en lugar de secuencial.
* **Integración del Algoritmo de Maratón:** Incorporar el algoritmo original del problema *Slot Machine* para calcular secuencias predictivas.
* **Efectos de Sonido:** Integrar alertas auditivas cuando se produzcan eventos como tirar de la palanca o ganar el Jackpot.
* **Nuevas Formas:** Extender el patrón Wrapper de la clase `Symbol` para soportar figuras más complejas (ej. estrellas, rombos).

---

## 👨‍💻 Autores

Trabajo realizado por estudiantes de tercer semestre de Ingeniería en Inteligencia Artificial de la Escuela Colombiana de Ingeniería Julio Garavito:

* **Andres Steeven Oyuela Mendez**, andres.oyuela-m@mail.escuelaing.edu.co
* **Jhon Eduard Duran Ceballos**, jhon.duran-c@mail.escuelaing.edu.co

---

## 🤖 Uso de IA generativa

Durante el desarrollo se usaron herramientas de IA generativa como apoyo para comprender APIs de Java y resolver dudas puntuales de implementación.

- Anthropic. (2026). *Claude* (Sonnet 5) [Modelo de lenguaje de gran escala]. https://claude.ai
- OpenAI. (2026). *ChatGPT* [Modelo de lenguaje de gran escala]. https://chat.openai.com

---

## 📌 Conclusión

El Simulador de Máquina Tragamonedas DOPO-POOB demuestra con éxito cómo el análisis estructurado de requisitos y la aplicación rigurosa de patrones orientados a objetos permiten desarrollar software interactivo, modular y escalable. A través de 10 iteraciones y superando retos técnicos gráficos, el proyecto culmina en una experiencia visual sólida y una base de código preparada para crecer.
