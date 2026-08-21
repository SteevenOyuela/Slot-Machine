# 🎰 Simulador de Máquina Tragamonedas (DOPO-POOB)

## 📘 Descripción General
Este proyecto es un simulador interactivo de una máquina tragamonedas (Slot Machine), desarrollado íntegramente en Java utilizando el entorno **BlueJ**. El simulador permite gestionar dinámicamente ruedas y símbolos, ejecutar giros con animaciones (incluyendo una palanca interactiva), y detectar de forma automática cuando el jugador ha alcanzado el estado ganador (Jackpot). 

El problema está inspirado en el **Problem I de la maratón de programación internacional 2025 (Slot Machine)** y fue diseñado con un fuerte enfoque en buenas prácticas de Programación Orientada a Objetos (POO), asegurando que el código sea limpio, cohesivo y altamente extensible.

---

## 🎯 Objetivos del Proyecto
* **Crear una arquitectura extensible:** Diseñar un sistema orientado a objetos donde los componentes puedan escalarse fácilmente (ej. agregar más ruedas o formas de símbolos).
* **Gestión dinámica de estado:** Permitir adicionar o eliminar ruedas y símbolos en tiempo real, adaptando la interfaz gráfica de forma automática.
* **Simulación visual interactiva:** Aprovechar una librería de figuras (`shapes`) para recrear el comportamiento de una máquina tragamonedas, incluyendo retardo mecánico y animaciones (palanca).
* **Evaluación de condiciones:** Implementar la lógica matemática para consultar configuraciones visibles y detectar el premio mayor (Jackpot) cuando todas las ruedas coinciden.
* **Control de visibilidad y usabilidad:** Garantizar el correcto funcionamiento de la máquina tanto en modo visible como invisible, manejando excepciones y alertas a través de cuadros de diálogo (`JOptionPane`).

---

## 🧠 Fundamento Teórico
El desarrollo del proyecto se fundamenta fuertemente en principios de diseño de software:
* **Encapsulamiento y Responsabilidad Única:** Cada clase maneja exclusivamente su lógica. `Wheel` se encarga de sus propias coordenadas y de centrar su figura; `SlotMachine` orquesta el juego y gestiona las colecciones; `Lever` controla sus propios estados de animación.
* **Patrón Wrapper (Envoltorio):** Utilizado en la clase `Symbol` para unificar figuras con comportamientos dispares (`Circle`, `Triangle`, `Rectangle`) bajo una sola interfaz que la máquina pueda manipular fácilmente, estandarizando sus coordenadas de origen.
* **Metodologías Ágiles (XP):** El proyecto se gestionó mediante *Entregas Pequeñas* (Small Releases), dividiendo la construcción en 10 mini-ciclos para asegurar un código robusto antes de integrar funcionalidades complejas.
* **Geometría en 2D (Canvas):** Manejo de sistemas de coordenadas (X, Y) absolutas y relativas para posicionar dinámicamente los elementos, recalculando márgenes y desplazamientos para mantener la máquina centrada y alineada.

---

## ⚙️ Implementación Práctica
La solución se compone de varias clases articuladas:
1. **`SlotMachine`**: Clase principal que administra las listas de ruedas (`ArrayList<Wheel>`) y el catálogo de símbolos (`ArrayList<Symbol>`). Controla la lógica de victoria, las validaciones de límites y el *redimensionamiento* de la carcasa.
2. **`Wheel`**: Representa cada columna giratoria. Encapsula un recuadro blanco y un borde negro, y se encarga de posicionar el `Symbol` actual exactamente en su centro.
3. **`Symbol`**: Representación gráfica de las fichas (Círculos, Triángulos o Cuadrados). Verifica colores y asegura alineaciones perfectas compensando desfases geométricos.
4. **`Lever`**: Componente visual (palanca) que implementa métodos `tirar()` y `soltar()` para añadir inmersión al evento de giro.

---

## ▶️ Ejecución
Para ejecutar el simulador:
1. Abre el proyecto en el entorno **BlueJ**.
2. Compila todas las clases.
3. En el *Code Pad* (Terminal) o mediante la interfaz de BlueJ, crea la máquina y actívala:
   ```java
   // 1. Instanciar y hacer visible
   SlotMachine mach1 = new SlotMachine();
   mach1.makeVisible(); 
   
   // 2. Agregar símbolos al catálogo (colores CSS y formas)
   mach1.addSymbol("red", "circle");
   mach1.addSymbol("blue", "triangle");
   mach1.addSymbol("yellow", "rectangle");
   
   // 3. (Opcional) Modificar el número de ruedas
   mach1.addWheel(4);
   
   // 4. Jugar
   mach1.spin();

```

---

## 📊 Aprendizajes Obtenidos

* **Manejo de Capas en Renderizado (Z-Index):** Uno de los mayores retos fue evitar que los fondos superpusieran a las casillas. Se aprendió a estructurar métodos de redibujado (`redibujar()`) para forzar un orden de renderizado lógico.
* **Traducción de Requisitos a Código:** Se aplicó la transformación de reglas de negocio dadas desde la perspectiva del usuario (base 1) a estructuras lógicas de programación (arreglos en base 0).
* **Manejo de Parpadeo Gráfico (Flickering):** Comprensión del funcionamiento interno de `Canvas.redraw()` y de cómo las pausas (`wait()`) afectan la fluidez de la animación.

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

## 📌 Conclusión

El Simulador de Máquina Tragamonedas DOPO-POOB demuestra con éxito cómo el análisis estructurado de requisitos y la aplicación rigurosa de patrones orientados a objetos permiten desarrollar software interactivo, modular y escalable. A través de 10 iteraciones y superando retos técnicos gráficos, el proyecto culmina en una experiencia visual sólida y una base de código preparada para crecer.
